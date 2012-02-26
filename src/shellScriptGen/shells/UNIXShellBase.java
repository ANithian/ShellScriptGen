package shellScriptGen.shells;



/**
 * This class provides a base for all UNIX shell script types. Since
 * there are so many flavors of shell scripts (C Shell, BASH etc), I wanted any future UNIX shell
 * scripts to derive from this one to make things easy. For all UNIX scripts, things such as the 
 * classpath separator, comment character etc are the same so this allows the sub-classes to implement
 * only those methods that are specific to that particular shell script type.
 *  
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public abstract class UNIXShellBase implements ShellScriptInterface
{
  public String getFileSeparator()
  {
      return "/";
  }
  
  public String getCPSeparator()
  {
    // TODO Auto-generated method stub
    return ":";
  }

  public String getComment()
  {
    // TODO Auto-generated method stub
    return "#";
  }
  
  public String getExecuteScriptLine(String pScriptToExec)
  {
    // TODO Auto-generated method stub
    return "source " + pScriptToExec;
  }

  
  public String getVariableReference(String pVariableName)
  {
    // TODO Auto-generated method stub
    return "${" + pVariableName + "}";
  }

  /**
   * Helper method to create a blank shell script given a file handle.
   * 
   * @param pFile The file handle of the script to be created.
   *
  protected final void createBlankScript(File pFile)
  {
    try
    {
      PrintWriter pWriter = new PrintWriter(new FileWriter(pFile));
      pWriter.println(m_sShellLine);
      pWriter.println("#Put any pre-config script here");
      pWriter.flush();
      pWriter.close();
      addFileToFinishMessage(pFile);
    }
    catch(IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  } 
  */ 
}
