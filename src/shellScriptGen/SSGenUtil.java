package shellScriptGen;

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

/**
 * A singleton utility class used by various components
 * of the plugin that contains a series of helper methods.
 * 
 * @author Amit Nithian (ANithian@vt.edu)
 *
 */
public class SSGenUtil
{
  private IJavaModel m_jModel;
  private static SSGenUtil m_util;
  private IWorkspaceRoot m_rWorkspace;
  
  private ILaunchConfigurationType m_javaApplicationType;
  
  /**
   * Returns the instance of the utility class.
   * 
   * @return
   */
  public static SSGenUtil getInstance()
  {
    if(m_util==null)
    {
      m_util = new SSGenUtil();
    }
    return m_util;
  }
  
  private SSGenUtil()
  {
    m_rWorkspace = ResourcesPlugin.getWorkspace().getRoot();
    m_jModel = JavaCore.create(m_rWorkspace);
    //We are only concerned with Java Applications so let's
    //restrict to just those so that we don't show targets for
    //other things like JUnit, Ant etc.
    ILaunchConfigurationType[] types = 
        DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationTypes();
    for(ILaunchConfigurationType i:types)
    {
        if(i.getName().equals("Java Application"))
        {
            m_javaApplicationType = i;
            break;
        }
    }
    
  }
  
  /**
   * Returns the JavaModel associated with the workspace.
   * @return
   */
  public IJavaProject getJavaProject(String pProjectName)
  {
    return m_jModel.getJavaProject(pProjectName);
  }
  /**
   * Returns a list of launch configurations for a given java project.
   * 
   * @param pProject The desired java project to query the launch configurations.
   * @return
   */
  public ArrayList<ILaunchConfiguration> getLaunchConfigurations(IJavaProject pProject)
  {
    ArrayList aList = new ArrayList();
    try
    {
      ILaunchConfiguration[] launches = DebugPlugin.getDefault()
          .getLaunchManager().getLaunchConfigurations(m_javaApplicationType);
 
      for(ILaunchConfiguration i : launches)
      {
        String sProjectName = i.getAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
        //Null category seems to mean only those that are runtime configurations
        //and not those like ant build.xml targets.
        if(sProjectName.equalsIgnoreCase(pProject.getElementName())
            && i.getCategory() == null)
          aList.add(i);
        //System.out.println(i.getName());
        //System.out.println(i.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,""));
      }
    }
    catch(CoreException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    return aList;
  }

  /**
   * Returns a list of the java projects associated with the current workspace.
   * @return
   */
  public IJavaProject[] getProjects()
  {
    IJavaProject[] javaProjects;
    try
    {
      javaProjects = m_jModel.getJavaProjects();
    }
    catch(JavaModelException ee)
    {
      javaProjects = new IJavaProject[0];
    }
    return javaProjects;
  }
  
  public IResource getResource(IPath pPath)
  {
    return m_rWorkspace.findMember(pPath);
  }
}
