package shellScriptGen;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.\
 * 
 * @author Amit Nithian (ANithian@vt.edu) (ANithian@vt.edu)
 * @copyright 2006
 * 
 * This plugin is my first ever plugin and was designed to
 * export Java projects to shell scripts. Many times I have found myself
 * having to manually export runtime configurations to shell scripts so that
 * I can run them on computers without eclipse. Requiring eclipse should not be a requirement
 * to run complex java applications and also sometimes setting up ant is not always the most
 * desirable option. ALL machines have some sort of shell scripting environment and so to export
 * a project's runtime configuration to this format is universally guaranteed to run on any machine provided
 * that the project's folder structure is the same as the eclipse project.
 * 
 * I am learning how to write eclipse plugins so if you have any suggestions, please
 * email me at ANithian@gmail.com. I don't care whether you take this plugin and modify it and
 * release it again but two conditions:
 * <ol>
 * <li>Do NOT sell this plugin or any of its code
 * <li>At least recognize the fact that your plugin is a modification of mine or that
 * it was inspired by this plugin.
 * </ol>
 * 
 * This plugin was inspired by the developers of the eclipse2ant plugin and I relied on that code
 * to figure out how to write this one, especially when dealing with obtaining the java projects and the
 * runtime configurations.
 */
public class ShellScriptGenPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static ShellScriptGenPlugin plugin;
    //Resource bundle.
    private ResourceBundle resourceBundle;
	/**
	 * The constructor.
	 */
	public ShellScriptGenPlugin() {
		plugin = this;
        try
        {
            this.resourceBundle = ResourceBundle
                    .getBundle("shellScriptGen.ShellScriptGenResources");
        }
        catch (MissingResourceException x)
        {
            resourceBundle = null;
        }        
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ShellScriptGenPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("ShellScriptGen", path);
	}
    
    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not
     * found.
     */
    public static String getResourceString(String key)
    {
        ResourceBundle bundle = ShellScriptGenPlugin.getDefault()
                .getResourceBundle();
        try
        {
            return (bundle != null) ? bundle.getString(key) : key;
        }
        catch (MissingResourceException e)
        {
            return key;
        }
    }

    /**
     * Returns the plugin's resource bundle,
     */
    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }    
}
