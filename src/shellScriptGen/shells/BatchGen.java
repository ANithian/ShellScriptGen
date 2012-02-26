package shellScriptGen.shells;


/**
 * This is the extension of the shell base that generates windows batch
 * files. I didn't believe there to be multiple flavors of windows batch files so
 * I didn't create a windows base that is later subclassed like the UNIX shell base case.
 * 
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class BatchGen implements ShellScriptInterface
{
  public String getComment()
  {
    // TODO Auto-generated method stub
    return "REM";
  }
  
  public String getCPSeparator()
  {
    // TODO Auto-generated method stub
    return ";";
  }

  public String getFileExtension()
  {
    // TODO Auto-generated method stub
    return ".bat";
  }

  public String getScriptLine()
  {
    // TODO Auto-generated method stub
    return "@echo off";
  }


  public String getExecuteScriptLine(String pScriptToExec)
  {
    // TODO Auto-generated method stub
    return "call " + pScriptToExec;
  }

  
  public String createVariableDeclaration(String pName, String pValue)
  {
    // TODO Auto-generated method stub
    return "set " + pName + "=\"" + pValue + "\"";
  }



  
  public String getVariableReference(String pVariableName)
  {
    // TODO Auto-generated method stub
    return "%" + pVariableName + "%";
  }

@Override
public String getFileSeparator()
{
    // TODO Auto-generated method stub
    return "\\";
}
  
  /*
  @Override
  protected void buildPreLaunchScript()
  {
    // TODO Auto-generated method stub
    File fPreLaunch = getFile("preLaunch.bat");
    try
    {
      if(fPreLaunch.createNewFile())
      {
        addFileToFinishMessage(fPreLaunch);
      }
    }
    catch(IOException p_ioe)
    {
      p_ioe.printStackTrace();
    }
  }
  
  @Override
  protected void buildMainShellScript(ILaunchConfiguration pConfig)
  {
    // TODO Auto-generated method stub
    try
    {
      StringBuffer sJavaLine = new StringBuffer();
      
      File fScript= getFile(pConfig.getName() + ".bat");
      PrintWriter pWriter = new PrintWriter(new FileWriter(fScript));
      pWriter.println("call preLaunch.bat");
      pWriter.println("set CP=" + getClassPath());
      sJavaLine.append("java -cp %CP% ");
      sJavaLine.append(getJavaCommand(pConfig));
      pWriter.println(sJavaLine.toString());
      pWriter.println("call postLaunch.bat");
      pWriter.flush();
      pWriter.close();
      addFileToFinishMessage(fScript);
    }
    catch(IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch(CoreException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  protected void buildPostLaunchScript()
  {
    // TODO Auto-generated method stub
    File fPreLaunch = getFile("postLaunch.bat");
    try
    {
      if(fPreLaunch.createNewFile())
      {
        addFileToFinishMessage(fPreLaunch);
      }
    }
    catch(IOException p_ioe)
    {
      p_ioe.printStackTrace();
    }
  }
*/
}
