package shellScriptGen;

import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;

import shellScriptGen.shells.ShellScriptInterface;

public class ScriptModel
{
    private  boolean useAbsolute;
    private  boolean createPostLaunch;
    private  boolean createPreLaunch;
    private List<ILaunchConfiguration> launchConfigurations;
    private IJavaProject selectedProject;
    private ShellScriptInterface selectedScriptType;
    private String javaRuntimePath;
    public boolean isUseAbsolute()
    {
        return useAbsolute;
    }
    public void setUseAbsolute(boolean useAbsolute)
    {
        this.useAbsolute = useAbsolute;
    }
    public boolean isCreatePostLaunch()
    {
        return createPostLaunch;
    }
    public void setCreatePostLaunch(boolean createPostLaunch)
    {
        this.createPostLaunch = createPostLaunch;
    }
    public boolean isCreatePreLaunch()
    {
        return createPreLaunch;
    }
    public void setCreatePreLaunch(boolean createPreLaunch)
    {
        this.createPreLaunch = createPreLaunch;
    }
    public List<ILaunchConfiguration> getLaunchConfigurations()
    {
        return launchConfigurations;
    }
    public void setLaunchConfigurations(
            List<ILaunchConfiguration> launchConfigurations)
    {
        this.launchConfigurations = launchConfigurations;
    }
    public IJavaProject getSelectedProject()
    {
        return selectedProject;
    }
    public void setSelectedProject(IJavaProject selectedProject)
    {
        this.selectedProject = selectedProject;
    }
    public ShellScriptInterface getSelectedScriptType()
    {
        return selectedScriptType;
    }
    public void setSelectedScriptType(ShellScriptInterface selectedScriptType)
    {
        this.selectedScriptType = selectedScriptType;
    }
    public String getJavaRuntimePath()
    {
        return javaRuntimePath;
    }
    public void setJavaRuntimePath(String javaRuntimePath)
    {
        this.javaRuntimePath = javaRuntimePath;
    }
    
}
