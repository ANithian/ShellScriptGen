package shellScriptGen;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.progress.IProgressService;

import shellScriptGen.builders.ScriptBuilder;
import shellScriptGen.builders.ScriptBuildingException;
import shellScriptGen.ui.MainPage;

/**
 * This is the export wizard class. This performs the finish action when the
 * user clicks the "Finish" button on the main page.
 * 
 * @author Amit Nithian (ANithian@vt.edu) (ANithian@vt.edu)
 * @copyright 2006
 * 
 */
public class SSExportWizard1 extends Wizard implements IExportWizard
{
    private MainPage sPage;

    public SSExportWizard1()
    {
        super();
        // TODO Auto-generated constructor stub
        this.setWindowTitle("Export to Shell Scripts");
        this.setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish()
    {
        // TODO Auto-generated method stub
        try
        {
            // Calls on the page to create the scripts and gets
            // the message to display to the user OR prints an error
            // if the scripts failed to create.
            
            // MessageDialog.openInformation(getShell(),"Finished Shell Script Generation",sMessage);
            getContainer().run(false, false, new IRunnableWithProgress()
            {
                public void run(IProgressMonitor pm) throws InvocationTargetException
                {
                    try
                    {
                        pm.beginTask("Creating Scripts", 150);
                        String sMessage = sPage.createShellScripts(pm);
                        MessageDialog.openInformation(getShell(),"Finished Shell Script Generation",sMessage);
                        
                    }
                    catch (ScriptBuildingException e)
                    {
                        // TODO Auto-generated catch block
                        MessageDialog.openError(getShell(), "Error:", e.getMessage());
                        throw new InvocationTargetException(e);
                    }
//                    pm.beginTask("Test", 100);
//                    for(int i=0; i < 10; i++)
//                    {
//                        pm.worked(10);
//                        ScriptBuilder.sleep(1000);
//                    }
//                    pm.done();
                }
            });
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            return false;
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
        ScriptModel model = new ScriptModel();
        if(selection != null && selection.getFirstElement() instanceof IJavaProject)
        {
            model.setSelectedProject((IJavaProject)selection.getFirstElement());
        }
        sPage = new MainPage("Select Projects",model);
        sPage.setDescription("Select the projects and runtime configurations to export to shell scripts.");
        
        addPage(sPage);
    }

}
