package shellScriptGen.builders;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import shellScriptGen.SSGenUtil;
import shellScriptGen.ScriptModel;

/**
 * Helper class used to build the classpath string
 * for a given java project and launch configuration. The
 * classpath construction is a fairly complex task given all the
 * various things that can be on the classpath and how they relate to the
 * file system. This class encapsulates and handles the logic for extracting
 * path information for the various classpath entry types and 
 * constructs a shell script compatible classpath.
 * 
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class ClasspathBuilder
{
  private String m_sSeparator;
  private IJavaProject m_jProject;
  private IPath m_JavaPath;
  private boolean m_bMakeAbsolutePaths;
  /**
   * Constructs a new instance of the classpath builder
   * @param pSeparator The separator of the classpath (; or :) depending on the shell script
   * @param pProject The Java project for which the classpath is being built.
   */
  public ClasspathBuilder(String pSeparator,IJavaProject pProject, boolean pUseAbsolutePath)
  {
    m_sSeparator = pSeparator;
    m_jProject = pProject;
    m_bMakeAbsolutePaths = pUseAbsolutePath;
  }

  /**
   * Builds the classpath for the given project and returns a shell script compatible string that
   * can be passed to the Java VM.
   * @return Shell script compatible classpath string.
   * @throws ClasspathBuildingException 
   */
  public String buildClasspath() throws ClasspathBuildingException
  {
    StringBuilder sClasspath = new StringBuilder();
    Set<String> sClassPathEntries = new HashSet<String>();
    
    IClasspathEntry[] iEntries = null;
    try
    {
      iEntries = m_jProject.getRawClasspath();
    }
    catch(JavaModelException e)
    {
      // TODO Auto-generated catch block
      throw new ClasspathBuildingException(e);
    }
    //Loop across the classpath entries and extract the path information.
    for(IClasspathEntry i:iEntries)
    {
        processClasspathEntry(i,sClasspath,sClassPathEntries);
    }
    return sClasspath.toString();
  }
  
  private void processClasspathEntry(IClasspathEntry pEntry,StringBuilder pBuilder,Set<String> pClasspathEntries) throws ClasspathBuildingException
  {
      IPath transformedPath = null;
      SSGenUtil sUtil = SSGenUtil.getInstance();
      transformedPath = pEntry.getPath();
      switch(pEntry.getEntryKind())
      {
        case IClasspathEntry.CPE_SOURCE:
        {
          transformedPath = pEntry.getOutputLocation();
          //Null path means to use the project default path instead of the 
          //source folder output path.
          if(transformedPath == null)
          {
            try
            {
              transformedPath = m_jProject.getOutputLocation();
            }
            catch (JavaModelException e) 
            {
              throw new ClasspathBuildingException(e);
            }
          }
          break;
        }
        case IClasspathEntry.CPE_PROJECT: //Project references (pEntry.e. referencing the output of another project).
        {
          IJavaProject jTemp = sUtil.getJavaProject(pEntry.getPath().toString());
          try
          {
            transformedPath = jTemp.getOutputLocation();
          }
          catch(JavaModelException e)
          {
            // TODO Auto-generated catch block
            throw new ClasspathBuildingException(e);
          }
          break;
        }
        case IClasspathEntry.CPE_CONTAINER:
        {
            try
            {
                IClasspathContainer container = JavaCore.getClasspathContainer(pEntry.getPath(), m_jProject);
                IClasspathEntry[] entries = container.getClasspathEntries();
                if(container.getKind() != IClasspathContainer.K_DEFAULT_SYSTEM)
                {
                    for(IClasspathEntry i:entries)
                    {
                        processClasspathEntry(i, pBuilder,pClasspathEntries);
                    }
                }
                else
                {
                    //Get the base path of the JDK
                    m_JavaPath=entries[0].getPath();
                    //C:/Program Files (x86)/Java/jre6/lib/resources.jar
                    m_JavaPath = m_JavaPath.removeLastSegments(2).addTrailingSeparator();
                }
            }
            catch (JavaModelException e)
            {
                // TODO Auto-generated catch block
                throw new ClasspathBuildingException(e);
            }
            return;
        }
      }
      if(!pClasspathEntries.contains(transformedPath.toString()))
      {
          pClasspathEntries.add(transformedPath.toString());
          transformedPath = makeAbsolutePath(transformedPath);
          pBuilder.append(transformedPath.toPortableString());
          pBuilder.append(m_sSeparator);
      }
  }
  /*
   * Returns a file system absolute path given the incoming path which is
   * either absolute or workspace relative.
   */
  private IPath makeAbsolutePath(IPath pIncomingPath)
  {
    boolean bForceUseAbsolute = false;
    
    //Use the SSGenUtil to find a resource with the given transformed path (either
    //workspace relative OR absolute). If the resource found is null, then it's assumed that
    //the path is absolute and do nothing further. If the resource is not null AND the 
    //entry type is not a container or variable, then get the raw location.
    
    //If the type of resource obtained is a linked folder, then always set the path in the wrapper
    //to the absolute path.
    IResource rTemp = SSGenUtil.getInstance().getResource(pIncomingPath);
    if(rTemp != null)
    {
      //Two cases where we need to always set the path to absolute:
      //1) Jars in a project other than the one selected
      //2) Resources that are linked folders.
      bForceUseAbsolute = (!rTemp.getProject().equals(m_jProject.getProject()))
      || (rTemp.isLinked(IResource.CHECK_ANCESTORS));
      
      //IF the user specified to produce absolute paths then convert to absolute.
      if(m_bMakeAbsolutePaths || bForceUseAbsolute)
      {
        return rTemp.getLocation();
      }
      else if(!m_bMakeAbsolutePaths)
      {
        return pIncomingPath.removeFirstSegments(1);
      }      
    }
    return pIncomingPath;
  }
  
  public String getJavaHome()
  {
      return m_JavaPath.toOSString();
  }
}
