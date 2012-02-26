package shellScriptGen.shells;


/**
 * This is the concrete UNIX shell base that generates C-Shell 
 * scripts to export a java project.
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class CShellGen extends UNIXShellBase
{

  public String getScriptLine()
  {
    // TODO Auto-generated method stub
    return "#!/bin/csh";
  }

  
  public String createVariableDeclaration(String pName, String pValue)
  {
    // TODO Auto-generated method stub
    return "set " + pName + "=\"" + pValue + "\"";
  }


  public String getFileExtension()
  {
    // TODO Auto-generated method stub
    return ".csh";
  }


  /*
  @Override
  protected void buildPreLaunchScript()
  {
    // TODO Auto-generated method stub
    File fPost = getFile("preLaunch.csh");
    if(!fPost.exists())
      createBlankScript(fPost);
  }

  @Override
  protected void buildMainShellScript(ILaunchConfiguration pConfig)
  {
    // TODO Auto-generated method stub
//  Create a shell script that will 
    //1) Source the preLaunch file
    //2) Execute the code
    //3) Source the post-launch file.    
    try
    {
      StringBuffer sJavaLine = new StringBuffer();
      
      File fScript = getFile(pConfig.getName() + ".csh");
      PrintWriter pWriter = new PrintWriter(new FileWriter(fScript));
      pWriter.println("#!/bin/tcsh");
      pWriter.println("#Automatically Generated");
      pWriter.println("source preLaunch.csh");
      pWriter.println("setenv CP " + getClassPath());
      sJavaLine.append("java -cp ${CP} ");
      sJavaLine.append(getJavaCommand(pConfig));
      pWriter.println(sJavaLine.toString());
      pWriter.println("source postLaunch.csh");
      pWriter.flush();
      pWriter.close();
      super.addFileToFinishMessage(fScript);
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
    File fPost = getFile("postLaunch.csh");
    if(!fPost.exists())
      createBlankScript(fPost);
  }
*/
}
