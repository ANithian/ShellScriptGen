package shellScriptGen.shells;

/**
 * Provides abstract methods that all concrete shell
 * script types would implement. The purpose of this class
 * is to allow the script builder and classpath builder to
 * remain independent of the nuances of the different shell
 * scripts by calling generic methods on an interface.
 * 
 * <br><br>
 * This script interface provides all the necessary
 * methods needed to abstractly build a shell script.
 * 
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public interface ShellScriptInterface
{
  /**
   * Returns the comment sequence for the shell script.
   * @return
   */
  public String getComment();
  /**
   * Returns the first line of the shell script (i.e. the
   * line that dictates the script interpreter to execute). 
   * This may not apply to all shell script types.
   * @return
   */
  public String getScriptLine();
  
  /**
   * Returns the separator character for the Java classpath.
   * @return
   */
  public String getCPSeparator();
  
  /**
   * Returns the file extension associated with the shell
   * script.
   * @return
   */
  public String getFileExtension();
  
  /**
   * Returns the string in the native script format that
   * one would write to execute the following script. For example,
   * if the script name is "filename.csh" and the script is UNIX based,
   * then the return would be "source filename.csh"
   * @param pScriptToExec
   * @return
   */
  public String getExecuteScriptLine(String pScriptToExec);
  
  /**
   * Creates a variable declaration string in the native shell script language given
   * a name and value.
   * @param pName The name of the variable to create
   * @param pValue The value of the variable to create
   * @return The variable declaration in the native shell script language. <b>Note:</b>The value will be
   * placed in double quotes in the return string.
   */
  public String createVariableDeclaration(String pName, String pValue);
  
  /**
   * Returns a string representing the native script representation of referencing
   * the given variable.
   * @param pVariableName The name of the variable
   * @return The native script representation of referencing the named variable in a script.
   */
  public String getVariableReference(String pVariableName);
  
  /**
   * Returns the file path separator for the given shell script type.
   * @return
   */
  public String getFileSeparator();
}
