package shellScriptGen.builders;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

import shellScriptGen.ScriptModel;
import shellScriptGen.shells.ShellScriptInterface;

/**
 * The main class responsible for the construction of the scripts. This makes use of the classpath builder
 * to obtain a classpath string and will create scripts to launch the selected launch configurations.
 *  
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class ScriptBuilder
{
  private ShellScriptInterface m_shellScript;
  private ClasspathBuilder m_cBuilder;
  private String m_sScriptLocation;
  private Map<String,String> m_mNativeEnvironment = null;
  private static final String CLASSPATH_VARIABLE="CP";
  private static final String JAVAHOME = "JAVAHOME";
  private boolean m_bCreatePreLaunch;
  private boolean m_bCreatePostLaunch;
  private boolean m_bUseAbsolute;
  /**
   * Constructs a new script builder object given a java project and a particular shell
   * script interface.
   * @param pProject The selected java project that the script is being created against.
   * @param pInterface The concrete target shell script. 
   */
  public ScriptBuilder(IJavaProject pProject, ShellScriptInterface pScriptType, boolean pCreatePreLaunch, boolean pCreatePostLaunch, boolean pUseAbsolute)
  {
    m_bCreatePostLaunch=pCreatePostLaunch;
    m_bCreatePreLaunch = pCreatePreLaunch;
    m_sScriptLocation = pProject.getProject().getLocation().toOSString();
    m_shellScript = pScriptType;
    m_bUseAbsolute = pUseAbsolute;
    m_cBuilder = new ClasspathBuilder(m_shellScript.getCPSeparator(),pProject,pUseAbsolute);
    m_mNativeEnvironment = (Map<String, String>)DebugPlugin.getDefault().getLaunchManager().getNativeEnvironmentCasePreserved();
  }

  /**
   * Returns a list of files created by the script building.
   * @param pConfiguration Selected launch configurations to create scripts for.
   * @return
   */
  public List<String> buildScript(ILaunchConfiguration pConfiguration,IProgressMonitor pMonitor) throws ScriptBuildingException
  {
    List<String> lFiles = new ArrayList<String>();
    String sFileName = null;
    String sJavaCommand = null;
    PrintWriter pTemp = null;
    pMonitor.beginTask("Creating scripts for " + pConfiguration.getName(), 100);
    String[] sEnvVars = null;
    try
    {
      sJavaCommand = getJavaCommand(pConfiguration);
      sEnvVars = DebugPlugin.getDefault().getLaunchManager().getEnvironment(pConfiguration);
    }
    catch(CoreException e)
    {
      pMonitor.done();
      throw new ScriptBuildingException(e);
    }
    
    //1) Obtain a handle to the main script.
    sFileName = pConfiguration.getName() + m_shellScript.getFileExtension();
    PrintWriter pMainScript = getBlankScript(sFileName, 
        "This is the main script for " + pConfiguration.getName() + 
        ". If you find yourself modifying this script needlessly, "
        + "submit a bug report at http://sourceforge.net/tracker/?group_id=174241&atid=868466",true);
    
    lFiles.add(sFileName);
    //2) Optionally build the pre-launch script and write script code to invoke prelaunch
    if(m_bCreatePreLaunch)
    {
      sFileName = "preLaunch" + m_shellScript.getFileExtension();
      pTemp = getBlankScript(sFileName, "Place any prelaunch configuration and execution here",false);
      if(pTemp != null)
      {
        pTemp.close();
        lFiles.add(sFileName);
      }
      //Write the script code to invoke the prelaunch script.
      pMainScript.println(m_shellScript.getExecuteScriptLine(sFileName));
      pMainScript.println();      
      sleep(500);
      pMonitor.worked(33);
    }
    //Deal with the environment variables
    processEnvironmentVariables(sEnvVars, m_shellScript, pMainScript);
    //3) Put the classpath variable
    String sClassPath;
    try
    {
      sClassPath = m_cBuilder.buildClasspath();
    }
    catch(ClasspathBuildingException e)
    {
      // TODO Auto-generated catch block
      throw new ScriptBuildingException(e);
    }
    
    pMainScript.println(m_shellScript.createVariableDeclaration(CLASSPATH_VARIABLE, sClassPath));
    pMainScript.println(m_shellScript.createVariableDeclaration(JAVAHOME, m_cBuilder.getJavaHome()));
    //4) Put the Java command
    StringBuilder javaCommand = new StringBuilder();
    if(m_bUseAbsolute)
    {
        javaCommand.append(m_shellScript.getVariableReference(JAVAHOME));
        javaCommand.append(m_shellScript.getFileSeparator());
        javaCommand.append("bin");
        javaCommand.append(m_shellScript.getFileSeparator());
    }
    javaCommand.append("java");
    javaCommand.append(sJavaCommand);
    pMainScript.println(javaCommand.toString());
    sleep(500);
    pMonitor.worked(33);
    //5) Optionally build the post-launch script and write script code to invoke postLaunch
    if(m_bCreatePostLaunch)
    {
      //TODO: DO NOT CREATE FILE IF EXISTS
      sFileName = "postLaunch" + m_shellScript.getFileExtension();
      pTemp = getBlankScript(sFileName, "Place any post launch configuration and execution here",false);
      if(pTemp != null)
      {
        pTemp.close();
        lFiles.add(sFileName);
      }
      pMainScript.println();
      //Write the script code to invoke the prelaunch script.
      pMainScript.println(m_shellScript.getExecuteScriptLine(sFileName));
      sleep(500);
      pMonitor.worked(33);
    }
    pMainScript.flush();
    pMainScript.close();
    sleep(500);
    pMonitor.worked(33);
    pMonitor.done();
    return lFiles;
  }

  private void processEnvironmentVariables(String[] pVars, ShellScriptInterface pScriptIntf, PrintWriter pScriptWriter)
  {
      if(pVars == null)
          return;
      for(String s:pVars)
      {
          //TODO: How to parse (if possible) environment variable name/value if name and/or value contains equals.
          //Maybe throw an exception or assume no such variables exist.
          int iEqualLoc = s.indexOf("=");
          //Don't include native environment variables since the machine 
          //where the script is generated and the target machine may not be the same.
          if(m_mNativeEnvironment.containsKey(s.substring(0,iEqualLoc)))
              continue;
          String sVar = pScriptIntf.createVariableDeclaration(s.substring(0,iEqualLoc), s.substring(iEqualLoc + 1));
          pScriptWriter.println(sVar);
      }
  }
  /*
   * Returns a generic script header that will appear at the top of each 
   * script generated by this plugin.
   */
  private String getHeader()
  {
    CharArrayWriter cWriter = new CharArrayWriter();
    PrintWriter pWriter = new PrintWriter(cWriter);
    String sComment = m_shellScript.getComment() + " ";
    pWriter.println(sComment + "Automatically generated by EclipseSSGen." );
    pWriter.println(sComment + "For more information, please visit http://www.sourceforge.net/projects/eclipsessgen"
        + " or contact ANithian@vt.edu.");
    
    return cWriter.toString();
  }
  
  //pFileName is simply a filename, the location will be added by this
  //method.
  private PrintWriter getBlankScript(String pFileName, String pDescription, boolean pOverwrite)
  {
    PrintWriter wReturn = null;
    try
    {
      File fFile = new File(m_sScriptLocation + "/" + pFileName);
      
      if(!fFile.exists() || pOverwrite)
      {
        wReturn = new PrintWriter(new FileWriter(fFile));
        wReturn.println(m_shellScript.getScriptLine());
        wReturn.println(getHeader());
        if(pDescription != null)
          wReturn.println(m_shellScript.getComment() + " " + pDescription);
      }
    }
    catch(IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    return wReturn;
  }
 
  private String getJavaCommand(ILaunchConfiguration pConfig) throws CoreException
  {
    StringBuffer sJavaLine = new StringBuffer();
    sJavaLine.append(" -cp " + m_shellScript.getVariableReference(CLASSPATH_VARIABLE));
    String sVMArgs = pConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,"");
    String sMainClass = pConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,"");
    String sProgArgs = pConfig.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,"");
    sJavaLine.append(" " + sVMArgs);
    sJavaLine.append(" " + sMainClass);
    sJavaLine.append(" " + sProgArgs);
    return sJavaLine.toString();
  }
  
  //For dramatic effect to make the progress bar look cool :-)
  public static void sleep(long pMillis)
  {
      try
    {
        Thread.sleep(pMillis);
    }
    catch (InterruptedException e)
    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  }
}
