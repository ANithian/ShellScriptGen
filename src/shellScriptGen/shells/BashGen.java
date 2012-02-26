package shellScriptGen.shells;



/**
 * This is a concrete implementation that generates BASH scripts to 
 * export the project.
 * 
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class BashGen extends UNIXShellBase
{
  public String getComment()
  {
    // TODO Auto-generated method stub
    return "#";
  }


  public String getFileExtension()
  {
    // TODO Auto-generated method stub
    return ".sh";
  }


  public String createVariableDeclaration(String pName, String pValue)
  {
    // TODO Auto-generated method stub
    return pName + "=\"" + pValue + "\"";
  }


  public String getScriptLine()
  {
    // TODO Auto-generated method stub
    return "#!/bin/bash";
  }


/*
  protected void buildMainShellScript(ILaunchConfiguration pConfig)
    {
      // TODO Auto-generated method stub
      // TODO Auto-generated method stub
  //  Create a shell script that will 
      //1) Source the preLaunch file
      //2) Execute the code
      //3) Source the post-launch file.    
      try
      {
        StringBuffer sJavaLine = new StringBuffer();
        
        File fScript= getFile(pConfig.getName() + ".sh");
        PrintWriter pWriter = new PrintWriter(new FileWriter(fScript));
        pWriter.println("#!/bin/bash");
        pWriter.println("#Automatically Generated");
        pWriter.println("source preLaunch.sh");
        String sClassPath = "export CP=" + getClassPath();
        pWriter.println(sClassPath);
        sJavaLine.append("java -cp ${CP} ");
        sJavaLine.append(getJavaCommand(pConfig));
        pWriter.println(sJavaLine.toString());
        pWriter.println("source postLaunch.sh");
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


  protected void buildPostLaunchScript()
  {
    // TODO Auto-generated method stub
    File fPost = getFile("postLaunch.sh");
    if(!fPost.exists())
      createBlankScript(fPost);
  }


  protected void buildPreLaunchScript()
  {
    // TODO Auto-generated method stub
    File fPreFile = getFile("preLaunch.sh");
    if(!fPreFile.exists())
      createBlankScript(fPreFile);
  }
*/
  
}
