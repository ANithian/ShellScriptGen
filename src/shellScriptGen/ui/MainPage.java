package shellScriptGen.ui;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import shellScriptGen.ScriptModel;
import shellScriptGen.builders.ScriptBuilder;
import shellScriptGen.builders.ScriptBuildingException;

/**
 * This is the main page of the plugin. This is a very simple plugin
 * and does NOT require multiple pages to work. I have not figured out
 * how to handle multiple pages and once I do, I will update this plugin
 * maybe to take advantage of multiple pages if necessary.
 * 
 * @author Amit Nithian (ANithian@vt.edu) (ANithian@vt.edu)
 * @copyright 2006
 *
 */
public class MainPage extends WizardPage
{
  private SSNewExportComposite sComposite;
  private ScriptModel mScriptModel;
  public MainPage(String pageName,ScriptModel pModel)
  {
    super(pageName);
    mScriptModel = pModel;
  }

  public MainPage(String pageName, String title, ImageDescriptor titleImage)
  {
    super(pageName, title, titleImage);
    // TODO Auto-generated constructor stub
  }

  public void createControl(Composite parent)
  {
    sComposite = new SSNewExportComposite(this,parent,SWT.NONE,mScriptModel);
    setControl(sComposite);
    setPageComplete(false);
  }
  
  public String createShellScripts(IProgressMonitor pMonitor) throws ScriptBuildingException
  {
//    sComposite.getBuilder().buildScript();
//    String sFinishMessage = sComposite.getBuilder().getFinishMessage();
    IJavaProject jSelectedProject = mScriptModel.getSelectedProject();
    List<ILaunchConfiguration> lConfigs = mScriptModel.getLaunchConfigurations();
    //sComposite.getConfigurations();
    int iTimePerConfig = 100/lConfigs.size();
    
    ScriptBuilder sBuilder = new ScriptBuilder(mScriptModel.getSelectedProject(),
            mScriptModel.getSelectedScriptType(),mScriptModel.isCreatePreLaunch(),
            mScriptModel.isCreatePostLaunch(),mScriptModel.isUseAbsolute());
    
    List<String> sCreatedFiles = null;
    for(ILaunchConfiguration i:lConfigs)
    {
      sCreatedFiles = sBuilder.buildScript(i,new SubProgressMonitor(pMonitor,iTimePerConfig));
      ScriptBuilder.sleep(1000);
      pMonitor.worked(iTimePerConfig);
    }
    try
    {
        pMonitor.beginTask("Refreshing Project..", 50);
        jSelectedProject.getResource().refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(pMonitor,50));
    }
    catch (CoreException e)
    {
        // TODO Auto-generated catch block
        throw new ScriptBuildingException(e);
    }
    finally
    {
        pMonitor.done();
    }
    return "Files created in " + jSelectedProject.getProject().getLocation();
  }
}
