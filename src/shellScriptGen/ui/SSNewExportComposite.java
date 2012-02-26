

package shellScriptGen.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import shellScriptGen.SSGenUtil;
import shellScriptGen.ScriptModel;
import shellScriptGen.shells.BashGen;
import shellScriptGen.shells.BatchGen;
import shellScriptGen.shells.CShellGen;
import shellScriptGen.shells.ShellScriptInterface;

/**
 * This is the SWT composite that is the GUI for the plugin. I used
 * the Eclipse Visual Editor plugin to generate this page and it helped
 * tremendously. I just added some logic to get the java projects and the 
 * corresponding runtime targets.
 * 
 * I associate each radio button with the corresponding shell script builder (generat0r
 * so that when the user selects a radio button, it's automatically known what shell script
 * builder to return out to the MainPage class.
 * 
 * @author Amit Nithian (ANithian@vt.edu) (ANithian@vt.edu)
 * @copyright 2006.
 */
public class SSNewExportComposite extends Composite implements SelectionListener
{

  private Label lblSelProjects = null;
  private Label lblSelTarget = null;
  private Label label = null;
  private Table tblTargets = null;
  private Label label2 = null;
  private List lstProjects = null;
  private CheckboxTableViewer m_cViewer=null;
  private WizardPage m_wPage;
  private ArrayList<ILaunchConfiguration> m_lSelectedConfigs;
  private SSGenUtil m_sUtil;


  private Group grpOutput = null;
  private Label label1 = null;
  private Button radBash = null;
  private Button radCShell = null;
  private Button radBatch = null;
  private Map<Button,ShellScriptInterface> m_hBuilders;
  private Group grpOptions = null;
  private Button chkUseAbsolute = null;
  private Button chkPreLaunch = null;
  private Button chkPostLaunch = null;
  
  private ScriptModel m_scriptModel = null;
  
  public SSNewExportComposite(WizardPage pPage, Composite parent, int style,ScriptModel pModel)
  {
    super(parent, style);
    m_sUtil = SSGenUtil.getInstance();
    m_scriptModel = pModel;
    m_wPage = pPage;
    m_lSelectedConfigs = new ArrayList<ILaunchConfiguration>();
    m_scriptModel.setLaunchConfigurations(m_lSelectedConfigs);
    initialize();
    //Map the buttons to the builder
    m_hBuilders = new HashMap<Button,ShellScriptInterface>();
    m_hBuilders.put(radCShell,new CShellGen());
    m_hBuilders.put(radBatch,new BatchGen());
    m_hBuilders.put(radBash,new BashGen());
    
//    m_bCurrentlySelected = radBash;
    radBash.setSelection(true);
    
    //Setup some model defaults
    m_scriptModel.setSelectedScriptType(m_hBuilders.get(radBash));
    m_scriptModel.setCreatePostLaunch(chkPostLaunch.getSelection());
    m_scriptModel.setCreatePreLaunch(chkPreLaunch.getSelection());
    m_scriptModel.setUseAbsolute(chkUseAbsolute.getSelection());
  }
  
  private void initialize()
  {
    GridData gridData3 = new GridData();
    gridData3.grabExcessHorizontalSpace = true;
    gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
    gridData3.grabExcessVerticalSpace = true;
    GridData gridData1 = new GridData();
    gridData1.grabExcessHorizontalSpace = true;
    gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    GridData gridData = new GridData();
    gridData.grabExcessHorizontalSpace = true;
    gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    lblSelProjects = new Label(this, SWT.CENTER);
    lblSelProjects.setText("Select Project");
    lblSelProjects.setLayoutData(gridData1);
    lblSelTarget = new Label(this, SWT.CENTER);
    lblSelTarget.setText("Select Target");
    lblSelTarget.setLayoutData(gridData);
    label = new Label(this, SWT.NONE);
    lstProjects = new List(this, SWT.BORDER);
    lstProjects.setLayoutData(gridData3);
    
    ListViewer lViewer = new ListViewer(lstProjects);
    
    lViewer.setContentProvider(new BaseWorkbenchContentProvider()
    {
      public Object[] getElements(Object element)
      {
        if(element instanceof IJavaProject[])
        {
          return (IJavaProject[])element;
        }
        return null;
      }
    });
    lViewer.setLabelProvider(new WorkbenchLabelProvider());
    lViewer.addPostSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        // TODO Auto-generated method stub
        StructuredSelection sSelection = (StructuredSelection)event
            .getSelection();
        IJavaProject selectedProject = (IJavaProject)sSelection.getFirstElement();
        m_cViewer.setInput(m_sUtil.getLaunchConfigurations(selectedProject));
        m_scriptModel.setSelectedProject(selectedProject);
        m_lSelectedConfigs.clear();
      }

    });
    lViewer.setInput(m_sUtil.getProjects());    
    
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 3;
    this.setLayout(gridLayout);
    createTblTargets();
    setSize(new Point(412, 270));
    label2 = new Label(this, SWT.NONE);
    createGrpOptions();
    label1 = new Label(this, SWT.NONE);
    Label filler = new Label(this, SWT.NONE);
    createGrpOptions2();
    if(m_scriptModel.getSelectedProject() != null)
        lViewer.setSelection(new StructuredSelection(m_scriptModel.getSelectedProject()));
    lViewer.refresh();
  }

  /**
   * This method initializes tblTargets	
   *
   */
  private void createTblTargets()
  {
    GridData gridData2 = new GridData();
    gridData2.grabExcessHorizontalSpace = true;
    gridData2.grabExcessVerticalSpace = true;
    gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
    gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    tblTargets = new Table(this, SWT.CHECK | SWT.BORDER);
    tblTargets.setHeaderVisible(false);
    tblTargets.setLayoutData(gridData2);
    tblTargets.setLinesVisible(false);
    m_cViewer = new CheckboxTableViewer(tblTargets);
    m_cViewer.addCheckStateListener(new ICheckStateListener()
        {
          public void checkStateChanged(CheckStateChangedEvent event)
          {
            if(event.getChecked())
            {
              m_lSelectedConfigs.add((ILaunchConfiguration)event.getElement());
            }
            else
            {
              m_lSelectedConfigs.remove((ILaunchConfiguration)event.getElement());
            }
            if(m_lSelectedConfigs.size() > 0)
              m_wPage.setPageComplete(true);
            else
              m_wPage.setPageComplete(false);
          }
        });

    m_cViewer.setContentProvider(new BaseWorkbenchContentProvider()
        {
          public Object[] getElements(Object element)
          {
            if(element instanceof ArrayList)
            {
              ArrayList a = (ArrayList)element;
              return a.toArray();
            }
            return null;
          }
        });
      m_cViewer.setLabelProvider(new LabelProvider()
        {
          @Override
          public String getText(Object element)
          {
            ILaunchConfiguration i = (ILaunchConfiguration)element;
            return i.getName();
          }
        }
        );    
  }

  /**
   * This method initializes grpOptions	
   *
   */
  private void createGrpOptions()
  {
    GridData gridData5 = new GridData();
    gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.CENTER;
    gridData5.grabExcessHorizontalSpace = true;
    GridLayout gridLayout1 = new GridLayout();
    gridLayout1.numColumns = 2;
    GridData gridData4 = new GridData();
    gridData4.grabExcessHorizontalSpace = true;
    gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
    gridData4.verticalSpan = 2;
    gridData4.horizontalSpan = 2;
    grpOutput = new Group(this, SWT.NONE);
    grpOutput.setText("Output Format");
    grpOutput.setLayout(gridLayout1);
    grpOutput.setLayoutData(gridData4);
    radBash = new Button(grpOutput, SWT.RADIO);
    radBash.setText("UNIX Bash Script");
    radCShell = new Button(grpOutput, SWT.RADIO);
    radCShell.setText("UNIX C Shell Script");
    radCShell.setLayoutData(gridData5);
    radBatch = new Button(grpOutput, SWT.RADIO);
    radBatch.setText("Windows Batch");
    
    //Add listeners
    radBash.addSelectionListener(this);
    radCShell.addSelectionListener(this);
    radBatch.addSelectionListener(this);
  }

  public void widgetDefaultSelected(SelectionEvent e)
  {
  }

  public void widgetSelected(SelectionEvent e)
  {
      if(e.getSource() instanceof Button)
      {
          //Check if it's a radio button
          Button b = (Button)e.getSource();
          if(m_hBuilders.containsKey(b))
          {
              m_scriptModel.setSelectedScriptType(m_hBuilders.get(b));
          }
          else
          {
              boolean bSelection = b.getSelection();
              //Checkbox
              if(b == chkPostLaunch)
                  m_scriptModel.setCreatePostLaunch(bSelection);
              else if(b == chkPreLaunch)
                  m_scriptModel.setCreatePreLaunch(bSelection);
              else if(b == chkUseAbsolute)
                  m_scriptModel.setUseAbsolute(bSelection);
          }
      }
  }
  
  /**
   * This method initializes grpOptions	
   *
   */
  private void createGrpOptions2()
  {
    GridData gridData6 = new GridData();
    gridData6.horizontalSpan = 2;
    GridLayout gridLayout2 = new GridLayout();
    gridLayout2.numColumns = 2;
    grpOptions = new Group(this, SWT.NONE);
    grpOptions.setText("Output Options");
    grpOptions.setLayoutData(gridData6);
    grpOptions.setLayout(gridLayout2);
    chkPreLaunch = new Button(grpOptions, SWT.CHECK);
    chkPreLaunch.setText("Generate pre-launch script");
    
    chkUseAbsolute = new Button(grpOptions, SWT.CHECK);
    chkUseAbsolute.setText("Output Absolute Paths");
    
    chkPostLaunch = new Button(grpOptions, SWT.CHECK);
    chkPostLaunch.setText("Generate post-launch script");
    
    chkPostLaunch.addSelectionListener(this);
    chkPreLaunch.addSelectionListener(this);
    chkUseAbsolute.addSelectionListener(this);
    
    chkPreLaunch.setSelection(true);
    chkPostLaunch.setSelection(true);
  }
//  /*
//   * Public methods for getting information about this 
//   * panel
//  */
//  public ShellScriptInterface getSelectedShellScript()
//  {
//    SSGenOptions.setCreatePostLaunch(chkPostLaunch.getSelection());
//    SSGenOptions.setCreatePreLaunch(chkPreLaunch.getSelection());
//    SSGenOptions.setUseAbsolute(chkUseAbsolute.getSelection());
//    
//    return m_hBuilders.get(m_bCurrentlySelected); 
//  }
}  //  @jve:decl-index=0:visual-constraint="24,103"



