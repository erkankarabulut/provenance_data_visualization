package App;

import Base.*;
import Action.*;
import Util.FilterUtil;

import Util.NetworkViewOrganizer;
import Util.Subscriber;
import cytoscape.generated.Network;
import jdk.nashorn.internal.scripts.JO;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.cytoscape.model.*;
import org.cytoscape.task.write.ExportNetworkImageTaskFactory;
import org.cytoscape.task.write.ExportNetworkViewTaskFactory;
import org.cytoscape.task.write.ExportTableTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MyControlPanel extends JPanel implements CytoPanelComponent{

    // Variables and Tools

    private ProvoImportCore provoImportCore;
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private SliderVisualization sliderVisualization;
    private CompareGraphsCore compareGraphsCore;
    private DrawComparedGraphs drawComparedGraphs;

    private List<String> nodeTypes;
    private JSlider slider;
    private boolean sliderStop;
    private Double cytoPanelHeight;
    private Double cytoPanelWidth;
    private Double mainPanelHeight;
    private Double mainPanelWidth;
    private Double visualStyleTemplatePanelWidth;
    private Integer maxNode;

    private JButton showHideRelationButton;
    private JButton entityBasedSorting;
    public  JButton importVisStyleButton;
    public  JButton importNetworkButton;
    public  JButton importTableButton;
    private JButton extractFilesButton;
    private JButton closeButton;
    private JButton helpButton;
    private JButton chooseXmlButton;
    private JButton chooseVisStyleButton;
    private JButton chooseSaxonButton;
    private JButton groupByNodeTypeButton;
    private JButton showOnlyButton;
    private JButton hideButton;
    private JButton highLightButton;
    private JButton sortActivitiesByTime;
    private JButton svLeftArrow;
    private JButton svRightArrow;
    private JButton svPlay;
    private JButton svStop;
    private JButton showNodeProperties;
    private JButton chooseFirstGraphsNodeButton;
    private JButton chooseFirstGraphsEdgeButton;
    private JButton chooseSecondGraphsNodeButton;
    private JButton chooseSecondGraphsEdgeButton;
    private JButton compareGraphsButton;
    private JButton exportAsPngButton;
    private JButton exportNetworkButton;
    private JButton exportTableButton;
    private JButton importGraphsButton;
    private JButton startClusteringButton;

    private JCheckBox sliderCheckBox;
    private JCheckBox compareAllProperties;
    private JRadioButton active;
    private JRadioButton inactive;
    private JRadioButton vsTemplate1;
    private JRadioButton vsTemplate2;
    private JRadioButton vsTemplate3;
    private JRadioButton activateRealTime;
    private JRadioButton deactivateRealTime;
    private ButtonGroup radioButtons;
    private ButtonGroup templatesButtonGroup;
    private ButtonGroup realTimeVisButtonGroup;
    private Timer timer1;
    private JComboBox showOnly;
    private JComboBox hide;
    private JComboBox highLight;

    private JPanel mainPanel;
    private JPanel appNamePanel;
    private JPanel provoPanel;
    private JPanel helpExitPanel;
    private JPanel toolboxPanel;
    private JPanel showRelationsPanel;
    private JPanel showOnlyPanel;
    private JPanel hidePanel;
    private JPanel highLightPanel;
    private JPanel sliderVisualizationPanel;
    private JPanel visualStyleTemplatesPanel;
    private JPanel relationsPanel;
    private JPanel sortPanel;
    private JPanel realTimeVisPanel;
    private JPanel compareGraphsPanel;
    private JPanel exportPanel;
    private JPanel clusteringPanel;

    private JScrollPane scrollPane;

    private JSpinner clusteringSpinner;
    private JSpinner nodeCount;
    private JSpinner nodeWeight;
    private JSpinner edgeWeight;
    private JSpinner neighbourNodeWeight;
    private JSpinner threshold;

    private JLabel statusLabel;
    private JLabel appName;
    private JLabel xmlFileNameLabel;
    private JLabel visStyleFileNameLabel;
    private JLabel saxonFileNameLabel;
    private JLabel activeLabel;
    private JLabel inactiveLabel;
    public JLabel sliderLabel;
    private JLabel vsTemplate1Label;
    private JLabel vsTemplate2Label;
    private JLabel vsTemplate3Label;
    private JLabel versionLabel;
    private JLabel nodeCountString;
    private JLabel firstGraphsNodeLabel;
    private JLabel firstGraphsEdgeLabel;
    private JLabel secondGraphsNodeLabel;
    private JLabel secondGraphsEdgeLabel;
    private JLabel firstGraphLabel;
    private JLabel secondGraphLabel;
    private JLabel nodeWeightLabel;
    private JLabel edgeWeightLabel;
    private JLabel neighbourNodeWeightLabel;
    private JLabel thresholdLabel;

    private Subscriber subscriber;


    public MyControlPanel(CytoVisProject cytoVisProject){
        super();
        // Initializing Variables and Tools
        cytoPanelHeight = (Toolkit.getDefaultToolkit().getScreenSize().height * 0.81);
        cytoPanelWidth = (Toolkit.getDefaultToolkit().getScreenSize().width * 0.20);
        mainPanelHeight = (cytoPanelHeight * 2.6);
        mainPanelWidth = (cytoPanelWidth * 0.9);
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.provoImportCore = new ProvoImportCore(cytoVisProject);
        this.setPreferredSize(new Dimension(cytoPanelWidth.intValue(), cytoPanelHeight.intValue()));
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.sliderStop = false;
        // Initializing tools
        initializeToolbox();
        nodeWeight.setEnabled(false);
        edgeWeight.setEnabled(false);
        neighbourNodeWeight.setEnabled(false);
        threshold.setEnabled(false);
        this.setAutoscrolls(true);
        compareGraphsButton.setEnabled(false);
        this.compareGraphsCore = new CompareGraphsCore(cytoVisProject);
        compareGraphsCore.changeFile(1, 0.0, 0.0, 0.0,0.0);
    }

    // This will initialize all the tools which will be on the control panel
    public void initializeToolbox(){
        initializePanels();
        initializeAppNameToolbox();
        initializeFileToolBox();
        initializeImportToolBox();
        initializeShowHideToolbox();
        initializeRelationsPanel();
        initializeVisualStyleTemplatesToolBox();
        initializeShowRelationsToolbox();
        initializeSortPanel();
        initializeRealTimeVisToolBox();
        initializeActivityToolbox();
        initializeSliderToolbox();
        initializeCompareGraphsPanel();
        initializeExportPanel();
        initializeClusteringPanel();
        initializeHelpCloseToolbox();
        initializeNetworkAvailability();
        actionListeners();

        addingComponentsToProvoPanel();
        addingComponentsToShowHidePanels();
        addinComponentsToVisualStyleTemplatePanel();
        addingComponentsToRelationsPanel();
        addingComponentsToSortPanel();
        addingComponentsToSliderPanel();
        addingComponentsToRealTimeVisPanel();
        addingComponentsToToolboxPanel();
        addingComponentsToClusteringPanel();
        addingComponentsToCompareGraphsPanel();
        addingComponentsToExportPanel();
        addingComponentsToHelpClosePanel();
        addingComponentsToMainPanel();
    }

    public void initializePanels(){
        this.mainPanel                  = new JPanel();
        this.showOnlyPanel              = new JPanel();
        this.hidePanel                  = new JPanel();
        this.highLightPanel             = new JPanel();
        this.helpExitPanel              = new JPanel();
        this.provoPanel                 = new JPanel();
        this.toolboxPanel               = new JPanel();
        this.showRelationsPanel         = new JPanel();
        this.sliderVisualizationPanel   = new JPanel();
        this.visualStyleTemplatesPanel  = new JPanel();
        this.relationsPanel             = new JPanel();
        this.sortPanel                  = new JPanel();
        this.realTimeVisPanel           = new JPanel();
        this.compareGraphsPanel         = new JPanel();
        this.exportPanel                = new JPanel();
        this.clusteringPanel            = new JPanel();

        this.sliderVisualizationPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.9)).intValue(), ((Double)(mainPanelHeight * 0.08)).intValue()));
        this.mainPanel.setPreferredSize(new Dimension(mainPanelWidth.intValue(), mainPanelHeight.intValue()));
        this.showOnlyPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.045)).intValue()));
        this.hidePanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.045)).intValue()));
        this.highLightPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.25)).intValue(), ((Double)(mainPanelHeight * 0.045)).intValue()));
        this.showRelationsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.7)).intValue(), ((Double)(mainPanelHeight * 0.035)).intValue()));
        this.provoPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.095)).intValue()));
        this.helpExitPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.040)).intValue()));
        this.toolboxPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.31)).intValue()));
        this.visualStyleTemplatesPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.067)).intValue()));
        this.relationsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.085)).intValue()));
        this.sortPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.85)).intValue(), ((Double)(mainPanelHeight * 0.039)).intValue()));
        this.realTimeVisPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.055)).intValue()));
        this.compareGraphsPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.24)).intValue()));
        this.exportPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.075)).intValue()));
        this.clusteringPanel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.95)).intValue(), ((Double)(mainPanelHeight * 0.041)).intValue()));

        // Setting border and titles to all panels
        this.mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        this.showRelationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Show Relations"));
        this.provoPanel.setBorder(BorderFactory.createTitledBorder("PROV-O Import"));
        this.helpExitPanel.setBorder(BorderFactory.createTitledBorder("Help & Exit"));
        this.toolboxPanel.setBorder(BorderFactory.createTitledBorder("Filter / Highlight"));
        this.sliderVisualizationPanel.setBorder(BorderFactory.createTitledBorder("Slider Visualization"));
        this.visualStyleTemplatesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Visual Styles"));
        this.relationsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Relations"));
        this.sortPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Sort"));
        this.realTimeVisPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Real Time Visualization"));
        this.compareGraphsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Compare Graphs"));
        this.exportPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Export"));
        this.clusteringPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED), "Clustering"));

        this.scrollPane = new JScrollPane();
        this.scrollPane.setViewportView(this.mainPanel);
        this.scrollPane.setPreferredSize(new Dimension(cytoPanelWidth.intValue(),cytoPanelHeight.intValue()));
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.scrollPane.setMaximumSize(new Dimension(((Double)(mainPanelWidth * 0.1875)).intValue(), 1000000));
    }

    public void initializeAppNameToolbox(){
        this.appNamePanel = new JPanel();
        this.appName = new JLabel();
        appName.setText("CytoVisToolBox");
        appName.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.5)).intValue(), ((Double)(mainPanelHeight * 0.0260)).intValue()));
        appName.setFont(new Font("Serif",Font.BOLD,18));
        appNamePanel.add(appName);
        appNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeFileToolBox(){
        this.chooseSaxonButton = new JButton("Choose Saxon File");
        this.chooseVisStyleButton = new JButton("Choose XSL File");
        this.chooseXmlButton = new JButton("Choose XML File");
        this.xmlFileNameLabel = new JLabel("None");
        this.visStyleFileNameLabel = new JLabel("None");
        this.saxonFileNameLabel = new JLabel("None");
        this.extractFilesButton = new JButton("Extract Files");

        chooseSaxonButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        chooseVisStyleButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        chooseXmlButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.2)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        saxonFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        xmlFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        visStyleFileNameLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/2.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        extractFilesButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.15)).intValue(),((Double)(mainPanelWidth * 0.07)).intValue()));
    }

    public void initializeImportToolBox(){
        this.importVisStyleButton = new JButton("<html>Import<br/>Vis Style</html>");
        this.importNetworkButton = new JButton("<html>Import<br/>Network</html>");
        this.importTableButton = new JButton("<html>Import<br/>Table</html>");
        this.statusLabel = new JLabel();
        importVisStyleButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.3)).intValue(),(provoPanel.getPreferredSize().width/9)));
        importNetworkButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.7)).intValue(),(provoPanel.getPreferredSize().width/9)));
        importTableButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/3.7)).intValue(),(provoPanel.getPreferredSize().width/9)));
        statusLabel.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.28)).intValue(), ((Double)(mainPanelWidth * 0.06)).intValue()));
    }

    public void initializeShowHideToolbox(){
        this.showOnly = new JComboBox();
        this.hide = new JComboBox();
        this.highLight = new JComboBox();
        this.showOnly.setPreferredSize(new Dimension(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue())));
        this.hide.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.highLight.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.showOnlyButton = new JButton("Action");
        this.highLightButton = new JButton("Action");
        this.hideButton = new JButton("Action");
        this.showOnlyButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.hideButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.highLightButton.setPreferredSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/4.5)).intValue(),((Double)(mainPanelWidth * 0.06)).intValue()));
        this.showOnlyPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Show Only"));
        this.hidePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Hide"));
        this.highLightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),"Highlight"));
    }

    public void initializeRelationsPanel(){
        this.showHideRelationButton = new JButton("Show / Hide Entity Relation");
        this.showHideRelationButton.setPreferredSize(new Dimension(230,40));
    }

    public void initializeVisualStyleTemplatesToolBox(){
        this.vsTemplate1 = new JRadioButton();
        this.vsTemplate2 = new JRadioButton();
        this.vsTemplate3 = new JRadioButton();
        this.templatesButtonGroup = new ButtonGroup();
        this.vsTemplate1Label = new JLabel("Visual Style Template 1: ");
        this.vsTemplate2Label = new JLabel("Visual Style Template 2: ");
        this.vsTemplate3Label = new JLabel("Visual Style Template 3: ");
        this.vsTemplate1Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.8)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsTemplate2Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.8)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.vsTemplate3Label.setSize(new Dimension(((Double)(provoPanel.getPreferredSize().width/1.8)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.visualStyleTemplatesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeSortPanel(){
        this.entityBasedSorting = new JButton("<html>Entity Based<br/>Sort</html");
        this.sortActivitiesByTime = new JButton("<html>Activity<br/>Based Sort</html>");

        this.entityBasedSorting.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.36)).intValue(),((Double)(mainPanelWidth * 0.11)).intValue()));
        this.sortActivitiesByTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.36)).intValue(),((Double)(mainPanelWidth * 0.11)).intValue()));
        this.sortPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeClusteringPanel(){
        this.startClusteringButton = new JButton("Start Clustering");
        this.clusteringSpinner     = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));

        startClusteringButton.setMargin(new Insets(5,5,5,5));
    }

    public void initializeShowRelationsToolbox(){
        this.radioButtons = new ButtonGroup();
        this.active = new JRadioButton();
        this.inactive = new JRadioButton();
        this.inactive.setSelected(true);
        this.activeLabel = new JLabel("Active: ");
        this.inactiveLabel = new JLabel("Inactive: ");
        this.showRelationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    public void initializeActivityToolbox(){
        this.groupByNodeTypeButton = new JButton("<html>Group By <br/>Node Type</html>");
        this.showNodeProperties = new JButton("Show Node Properties");

        this.showNodeProperties.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.80)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
        this.groupByNodeTypeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), ((Double)(mainPanelWidth * 0.11)).intValue()));
    }

    public void initializeSliderToolbox(){
        this.slider = new JSlider();
        this.sliderCheckBox = new JCheckBox();
        this.svRightArrow = new JButton();
        this.svLeftArrow = new JButton();
        this.svPlay = new JButton();
        this.svStop = new JButton();
        setIcons();
        this.sliderVisualization = new SliderVisualization(this);
        this.sliderLabel = new JLabel();
        this.sliderLabel.setText("None");
        this.sliderLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        this.sliderLabel.setFont(new Font("Serif",Font.BOLD,12));
        sliderCheckBox.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.06)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        sliderCheckBox.setSelected(false);
        slider.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.65)).intValue(), ((Double)(mainPanelWidth * 0.07)).intValue()));
        svLeftArrow.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svRightArrow.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svPlay.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
        svStop.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.09)).intValue(), ((Double)(mainPanelWidth * 0.09)).intValue()));
    }

    public void initializeRealTimeVisToolBox(){
        this.activateRealTime = new JRadioButton("Activate");
        this.deactivateRealTime = new JRadioButton("Deactivate");
        this.activateRealTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.30)).intValue(), ((Double)(mainPanelHeight*0.016)).intValue()));
        this.deactivateRealTime.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.30)).intValue(), ((Double)(mainPanelHeight*0.016)).intValue()));
        this.realTimeVisButtonGroup = new ButtonGroup();

        this.nodeCount = new JSpinner();
        this.nodeCountString = new JLabel("Maximum Node Count: ");
        this.nodeCountString.setPreferredSize(new Dimension(((Double)(mainPanelWidth*0.5)).intValue(), ((Double)(mainPanelHeight*0.014)).intValue()));
        nodeCount.setValue(20);
        setMaxNode(20);

        realTimeVisButtonGroup.add(activateRealTime);
        realTimeVisButtonGroup.add(deactivateRealTime);
    }

    public void initializeCompareGraphsPanel(){
        this.chooseFirstGraphsEdgeButton    = new JButton("Choose Edge");
        this.chooseFirstGraphsNodeButton    = new JButton("Choose Node");
        this.chooseSecondGraphsNodeButton   = new JButton("Choose Node");
        this.chooseSecondGraphsEdgeButton   = new JButton("Choose Edge");
        this.importGraphsButton             = new JButton("Import");

        this.compareAllProperties           = new JCheckBox("Include all properties to comparison");
        this.nodeWeight                     = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
        this.edgeWeight                     = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
        this.neighbourNodeWeight            = new JSpinner(new SpinnerNumberModel(0.0, -1000.0, 1000.0, 0.1));
        this.threshold                      = new JSpinner(new SpinnerNumberModel(0.1, 0.0, 1, 0.1));
        this.nodeWeightLabel                = new JLabel("Node Weight:");
        this.edgeWeightLabel                = new JLabel("Edge Weight:");
        this.neighbourNodeWeightLabel       = new JLabel("Adjacent Node Weight:");
        this.thresholdLabel                 = new JLabel("Threshold:");

        chooseFirstGraphsNodeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        chooseFirstGraphsEdgeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        chooseSecondGraphsNodeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        chooseSecondGraphsEdgeButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        importGraphsButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.39)).intValue(), 30));
        compareAllProperties.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.75)).intValue(), 30));

        nodeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), 30));
        edgeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), 30));
        neighbourNodeWeight.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), 30));
        threshold.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.20)).intValue(), 30));
        this.nodeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), 30));
        this.edgeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), 30));
        this.neighbourNodeWeightLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), 30));
        this.thresholdLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.60)).intValue(), 30));

        this.firstGraphsEdgeLabel = new JLabel("None");
        this.firstGraphsNodeLabel = new JLabel("None");
        this.secondGraphsEdgeLabel = new JLabel("None");
        this.secondGraphsNodeLabel = new JLabel("None");

        firstGraphsEdgeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        firstGraphsNodeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        secondGraphsEdgeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));
        secondGraphsNodeLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.4)).intValue(), 30));

        this.compareGraphsButton = new JButton("Compare");
        compareGraphsButton.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.39)).intValue(), 30));

        firstGraphLabel = new JLabel("First Graph", SwingConstants.CENTER);
        firstGraphLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), 20));

        secondGraphLabel = new JLabel("Second Graph", SwingConstants.CENTER);
        secondGraphLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.8)).intValue(), 20));

    }

    public void initializeExportPanel(){
        this.exportPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.exportAsPngButton   = new JButton("Export as PNG");
        this.exportNetworkButton = new JButton("Export Network");
        this.exportTableButton   = new JButton("Export Table");

        this.exportAsPngButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
        this.exportTableButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
        this.exportNetworkButton.setPreferredSize(new Dimension(((Double)(exportPanel.getPreferredSize().width*0.6)).intValue(), 30));
    }

    public void addingComponentsToToolboxPanel(){
        toolboxPanel.add(showOnlyPanel);
        toolboxPanel.add(hidePanel);
        toolboxPanel.add(highLightPanel);
        toolboxPanel.add(visualStyleTemplatesPanel);
        toolboxPanel.add(sortPanel);
        toolboxPanel.add(relationsPanel);
        // toolboxPanel.add(groupByNodeTypeButton);
        toolboxPanel.add(showNodeProperties);
    }

    public void initializeHelpCloseToolbox(){
        this.closeButton = new JButton("Close");
        this.helpButton = new JButton("Help");
        this.versionLabel = new JLabel("Version: 1.4");
        this.versionLabel.setPreferredSize(new Dimension(((Double)(mainPanelWidth * 0.7)).intValue(), 20));
    }

    public void addingComponentsToProvoPanel(){
        provoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        provoPanel.add(chooseSaxonButton);
        provoPanel.add(saxonFileNameLabel);
        provoPanel.add(chooseVisStyleButton);
        provoPanel.add(visStyleFileNameLabel);
        provoPanel.add(chooseXmlButton);
        provoPanel.add(xmlFileNameLabel);
        provoPanel.add(extractFilesButton);
        provoPanel.add(importVisStyleButton);
        provoPanel.add(importNetworkButton);
        provoPanel.add(importTableButton);
        provoPanel.add(statusLabel);
    }

    public void addingComponentsToShowHidePanels(){
        showOnlyPanel.add(showOnly);
        showOnlyPanel.add(showOnlyButton);
        hidePanel.add(hide);
        hidePanel.add(hideButton);
        highLightPanel.add(highLight);
        highLightPanel.add(highLightButton);
    }

    public void addingComponentsToRelationsPanel(){
        addingComponentsToShowRelationPanel();
        this.relationsPanel.add(showRelationsPanel);
        this.relationsPanel.add(showHideRelationButton);
    }

    public void addinComponentsToVisualStyleTemplatePanel(){
        templatesButtonGroup.add(vsTemplate1);
        templatesButtonGroup.add(vsTemplate2);
        templatesButtonGroup.add(vsTemplate3);
        visualStyleTemplatesPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        visualStyleTemplatesPanel.add(vsTemplate1Label);
        visualStyleTemplatesPanel.add(vsTemplate1);
        visualStyleTemplatesPanel.add(vsTemplate2Label);
        visualStyleTemplatesPanel.add(vsTemplate2);
        visualStyleTemplatesPanel.add(vsTemplate3Label);
        visualStyleTemplatesPanel.add(vsTemplate3);
    }

    public void addingComponentsToSortPanel(){
        sortPanel.add(sortActivitiesByTime);
        sortPanel.add(entityBasedSorting);
    }

    public void addingComponentsToRealTimeVisPanel(){
        realTimeVisPanel.add(activateRealTime);
        realTimeVisPanel.add(deactivateRealTime);
        realTimeVisPanel.add(nodeCountString);
        realTimeVisPanel.add(nodeCount);
    }

    public void addingComponentsToShowRelationPanel(){
        radioButtons.add(active);
        radioButtons.add(inactive);
        showRelationsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        showRelationsPanel.add(activeLabel);
        showRelationsPanel.add(active);
        showRelationsPanel.add(inactiveLabel);
        showRelationsPanel.add(inactive);
    }

    public void addingComponentsToSliderPanel(){
        sliderVisualizationPanel.add(sliderCheckBox);
        sliderVisualizationPanel.add(slider);
        sliderVisualizationPanel.add(sliderLabel);
        sliderVisualizationPanel.add(svLeftArrow);
        sliderVisualizationPanel.add(svPlay);
        sliderVisualizationPanel.add(svStop);
        sliderVisualizationPanel.add(svRightArrow);
    }

    public void addingComponentsToCompareGraphsPanel(){
        compareGraphsPanel.add(firstGraphLabel);

        compareGraphsPanel.add(chooseFirstGraphsNodeButton);
        compareGraphsPanel.add(firstGraphsNodeLabel);
        compareGraphsPanel.add(chooseFirstGraphsEdgeButton);
        compareGraphsPanel.add(firstGraphsEdgeLabel);

        compareGraphsPanel.add(secondGraphLabel);

        compareGraphsPanel.add(chooseSecondGraphsNodeButton);
        compareGraphsPanel.add(secondGraphsNodeLabel);
        compareGraphsPanel.add(chooseSecondGraphsEdgeButton);
        compareGraphsPanel.add(secondGraphsEdgeLabel);

        compareGraphsPanel.add(importGraphsButton);
        compareGraphsPanel.add(compareGraphsButton);

        compareGraphsPanel.add(compareAllProperties);
        compareGraphsPanel.add(nodeWeightLabel);
        compareGraphsPanel.add(nodeWeight);
        compareGraphsPanel.add(edgeWeightLabel);
        compareGraphsPanel.add(edgeWeight);
        compareGraphsPanel.add(neighbourNodeWeightLabel);
        compareGraphsPanel.add(neighbourNodeWeight);
        compareGraphsPanel.add(thresholdLabel);
        compareGraphsPanel.add(threshold);
    }

    public void addingComponentsToExportPanel(){
        exportPanel.add(exportAsPngButton);
        exportPanel.add(exportNetworkButton);
        exportPanel.add(exportTableButton);
    }

    public void addingComponentsToHelpClosePanel(){
        helpExitPanel.add(helpButton);
        helpExitPanel.add(closeButton);
    }

    public void addingComponentsToClusteringPanel(){
        clusteringPanel.add(startClusteringButton);
        clusteringPanel.add(clusteringSpinner);
    }

    public void addingComponentsToMainPanel(){
        this.mainPanel.add(appNamePanel);
        this.mainPanel.add(provoPanel);
        this.mainPanel.add(toolboxPanel);
        this.mainPanel.add(sliderVisualizationPanel);
        this.mainPanel.add(realTimeVisPanel);
        this.mainPanel.add(clusteringPanel);
        this.mainPanel.add(compareGraphsPanel);
        this.mainPanel.add(exportPanel);
        this.mainPanel.add(helpExitPanel);
        this.mainPanel.add(versionLabel);
        this.add(scrollPane);
    }

    public void initializeNetworkAvailability(){
        // Check for a network is available or not
        if(adapter.getCyApplicationManager().getCurrentNetwork() == null){
            // deactivate tools if there are no network loaded
            deActivateTools();
            this.chooseXmlButton.setEnabled(false);
            this.chooseVisStyleButton.setEnabled(false);
            this.extractFilesButton.setEnabled(false);
        }else{
            chooseXmlButton.setEnabled(false);
            chooseVisStyleButton.setEnabled(false);
            extractFilesButton.setEnabled(false);

            // Finding different node types and setting it to the related tools (Same as TableSetListener class)
            FilterUtil filter = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                    adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable());
            List<String> newNodeTypes = new ArrayList<String>();
            newNodeTypes.add("None");
            ArrayList<CyNode> nodes = filter.getAllNodes();
            for(CyNode node : nodes){
                if(newNodeTypes.contains(filter.findNodeType(node)) == false){
                    newNodeTypes.add(filter.findNodeType(node));
                }
            }
            setNodeTypes(newNodeTypes);
            // Activating Tools
            activateTools();
        }
    }

    public void actionListeners(){
        // Setting action listener to "Choose XML File" button
        this.chooseXmlButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseXmlFile();
                if(provoImportCore.isFileControl() == true){
                    xmlFileNameLabel.setText(provoImportCore.getXmlFileName());
                    extractFilesButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });
        // Setting action listener to "Choose Visual Style File" button
        this.chooseVisStyleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseVisMapFile();
                if(provoImportCore.isFileControl() == true){
                    visStyleFileNameLabel.setText(provoImportCore.getVisStyleFileName());
                    chooseXmlButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });
        // Setting action listener to "Choose Saxon File" button
        this.chooseSaxonButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.chooseSaxonFile();
                if(provoImportCore.isFileControl() == true){
                    saxonFileNameLabel.setText(provoImportCore.getSaxonFileName());
                    chooseVisStyleButton.setEnabled(true);
                }else {
                    showInvalidWarning();
                }
            }
        });
        // Setting action listener to "Extract Files" button
        this.extractFilesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.extractFiles();
                importNetworkButton.setEnabled(true);
            }
        });
        // Setting action to "Import Network" button
        this.importNetworkButton.setAction(new ImportEdgesAction(cytoVisProject, "C:\\provoTransformerPlugin\\edges.csv"));
        this.importNetworkButton.addMouseListener(new ImportEdgesRightClickAction(cytoVisProject));

        // Setting action to "Import Visual Style" Button
        this.importTableButton.setAction(new ImportNodesAction(cytoVisProject, "C:\\provoTransformerPlugin\\nodes.csv"));
        this.importTableButton.addMouseListener(new ImportNodesRightClickAction(cytoVisProject));

        this.importVisStyleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provoImportCore.importVisStyleTask();
            }
        });

        // Adding action listener to the components of the Toolbox panel

        // Setting action listener to action button of Show Only panel
        showOnlyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager.execute(new TaskIterator(new ShowOnlyTask(adapter,showOnly.getSelectedItem().toString())));
            }
        });
        // Setting action listener to action button of Hide panel
        hideButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager2 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager2.execute(new TaskIterator(new HideTask(adapter,nodeTypes.get(hide.getSelectedIndex()))));
            }
        });
        // Setting action listener to action button of highlight panel
        highLightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager3 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager3.execute(new TaskIterator(new HighLightTask(cytoVisProject,nodeTypes.get(highLight.getSelectedIndex()))));
            }
        });

        vsTemplate1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(1);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });

        vsTemplate2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(2);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });

        vsTemplate3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ChangeVisualStyleTemplate changeVisualStyleTemplate = new ChangeVisualStyleTemplate(cytoVisProject);
                changeVisualStyleTemplate.setTemplateNumber(3);
                changeVisualStyleTemplate.changeVisualStyle();
            }
        });
        // Setting action listener to radio button named active which is in the show relations panel
        // This flag used for understanding which radio button is selected
        active.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cytoVisProject.getNodeSelectedListener().setFlag(true);
            }
        });
        // Setting action listener to radio button named inactive which is in the show relations panel
        inactive.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cytoVisProject.getNodeSelectedListener().setFlag(false);
            }
        });
        // Setting action listener to "Group By Node Type" button
        groupByNodeTypeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager6 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager6.execute(new GroupByNodeTypeTaskFactory(adapter).createTaskIterator());
            }
        });
        // Setting action listener to "Sort Activities By Time" button
        sortActivitiesByTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager7 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager7.execute(new SortActivitesByTimeTaskFactory(adapter).createTaskIterator());
            }
        });
        // Setting action listener to "Sort Entities Based on Activity Time" button
        entityBasedSorting.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TaskManager taskManager8 = adapter.getCyServiceRegistrar().getService(TaskManager.class);
                taskManager8.execute(new EntityBasedSortingTaskFactory(cytoVisProject).createTaskIterator());
            }
        });
        // Setting action listener to "Show / Hide Entity Relation" button
        showHideRelationButton.setAction(new ShowHideEntityRelationAction(cytoVisProject));

        this.showNodeProperties.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CyNetwork network = cytoVisProject.getAdapter().getCyApplicationManager().getCurrentNetwork();
                List<CyNode> selected = CyTableUtil.getNodesInState(network,"selected",true);
                for(CyNode node : selected){
                    NodePropertyWindow nodePropertyWindow = new NodePropertyWindow(cytoVisProject, node);
                }
            }
        });

        this.activateRealTime.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final JedisPoolConfig poolConfig = new JedisPoolConfig();
                final JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
                final Jedis subscriberJedis = jedisPool.getResource();
                subscriber = new Subscriber(getInstance());

                new Thread(new Runnable() {

                    public void run() {
                        try {
                            subscriberJedis.subscribe(subscriber, "channel");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        this.nodeCount.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                setMaxNode((Integer) nodeCount.getValue());

                if(adapter.getCyApplicationManager().getCurrentNetwork() != null){
                    NetworkViewOrganizer networkViewOrganizer = new NetworkViewOrganizer(getInstance());
                    networkViewOrganizer.reOrganizeNetwork();
                }
            }
        });

        deactivateRealTime.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subscriber.unsubscribe();
            }
        });

        // Setting action listener to "Close" button
        this.closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        // Setting action listener to "Help" button
        this.helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                CytoVisProjectHelp cytoVisProjectHelp = new CytoVisProjectHelp();
            }
        });

        this.slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(sliderCheckBox.isSelected()){
                    CyApplicationManager manager = adapter.getCyApplicationManager();
                    CyNetworkView networkView = manager.getCurrentNetworkView();
                    CyNetwork network = networkView.getModel();
                    CyTable table = network.getDefaultNodeTable();
                    FilterUtil filter = new FilterUtil(network,table);
                    ArrayList<CyNode> activities = filter.FilterRowByNodeType("activity", "nodeType");

                    CyColumn timeColumn = table.getColumn("startTime"); // Getting start time column
                    List<String> timeList = filter.getTimeFromColumn(timeColumn); // Gets value of start time column without null value
                    sliderVisualization.hideFutureNodes(timeList, filter, network, networkView);
                    networkView.updateView();
                }

            }
        });

        this.svStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderStop = true;
            }
        });

        this.svPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sliderStop = false;
                if(sliderCheckBox.isSelected() == true){
                    timer1.start();
                }
            }
        });

        ActionListener actionListenerForTimer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue() + 1);
                if(slider.getValue() == slider.getMaximum() || sliderStop == true){
                    timer1.stop();
                }
            }
        };

        this.timer1 = new Timer(500, actionListenerForTimer);

        this.sliderCheckBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue());
            }
        });

        this.svRightArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue()+1);
            }
        });

        this.svLeftArrow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slider.setValue(slider.getValue()-1);
            }
        });

        this.chooseFirstGraphsNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseFirstGraphsNode()){
                    firstGraphsNodeLabel.setText(compareGraphsCore.getNode1FileName());
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        this.chooseFirstGraphsEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseFirstGraphsEdge()){
                    firstGraphsEdgeLabel.setText(compareGraphsCore.getEdge1FileName());
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        this.chooseSecondGraphsNodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseSecondGraphsNode()){
                    secondGraphsNodeLabel.setText(compareGraphsCore.getNode2FileName());
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        this.chooseSecondGraphsEdgeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareGraphsCore.chooseSecondGraphsEdge()){
                    secondGraphsEdgeLabel.setText(compareGraphsCore.getEdge2FileName());
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);}
            }
        });

        importGraphsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Integer result = compareGraphsCore.compareGraphs();
                if(result == 0){
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"Please choose all the files ..!",
                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }else if(result == -1){
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                            "Graphs must have 1 root ..!",
                            "Warning!", JOptionPane.INFORMATION_MESSAGE);
                }else if(result == 1){
                    drawComparedGraphs = new DrawComparedGraphs(compareGraphsCore, cytoVisProject);
                    drawComparedGraphs.draw();
                    compareGraphsButton.setEnabled(true);
                }
            }
        });

        compareGraphsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                compareGraphsCore.createAttendanceList();
                compareGraphsCore.findSimilarNodePairs();
                drawComparedGraphs.changeColors(compareGraphsCore);
            }
        });

        this.compareAllProperties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(compareAllProperties.isSelected()){
                    compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                            Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                            Double.parseDouble(threshold.getValue().toString()));
                    nodeWeight.setEnabled(true);
                    edgeWeight.setEnabled(true);
                    neighbourNodeWeight.setEnabled(true);
                    threshold.setEnabled(true);
                }else {
                    compareGraphsCore.changeFile(1, 0.0, 0.0, 0.0,0.0);
                    nodeWeight.setEnabled(false);
                    edgeWeight.setEnabled(false);
                    neighbourNodeWeight.setEnabled(false);
                    threshold.setEnabled(false);
                }
            }
        });

        nodeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()));
            }
        });

        edgeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()));
            }
        });

        neighbourNodeWeight.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()));
            }
        });

        threshold.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                compareGraphsCore.changeFile(2, Double.parseDouble(nodeWeight.getValue().toString()),
                        Double.parseDouble(edgeWeight.getValue().toString()), Double.parseDouble(neighbourNodeWeight.getValue().toString()),
                        Double.parseDouble(threshold.getValue().toString()));
            }
        });

        this.exportTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    ExportTableTaskFactory exportTableTaskFactory = adapter.get_ExportTableTaskFactory();
                    TaskIterator taskIterator = exportTableTaskFactory.createTaskIterator(adapter.getCyApplicationManager().
                            getCurrentNetwork().getDefaultNodeTable());
                    adapter.getTaskManager().execute(taskIterator);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        this.exportNetworkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    ExportNetworkViewTaskFactory exportNetworkViewTaskFactory = adapter.get_ExportNetworkViewTaskFactory();
                    TaskIterator taskIterator = exportNetworkViewTaskFactory.createTaskIterator(adapter.getCyApplicationManager().getCurrentNetworkView());
                    adapter.getTaskManager().execute(taskIterator);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        this.exportAsPngButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(adapter.getCyApplicationManager().getCurrentNetworkView() != null){
                    ExportNetworkImageTaskFactory exportNetworkImageTaskFactory = adapter.get_ExportNetworkImageTaskFactory();
                    TaskIterator taskIterator = exportNetworkImageTaskFactory.createTaskIterator(adapter.getCyApplicationManager().getCurrentNetworkView());
                    adapter.getTaskManager().execute(taskIterator);
                }else{
                    JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no visualization loaded yet ..!",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        });

        startClusteringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

    }

    // This method sets image icons to the buttons of sliderVisualization panel
    public void setIcons(){
        try {
            Image img = ImageIO.read(getClass().getClassLoader().getResource("next.png"));
            Image img2 = ImageIO.read(getClass().getClassLoader().getResource("previous.png"));
            Image img3 = ImageIO.read(getClass().getClassLoader().getResource("play.png"));
            Image img4 = ImageIO.read(getClass().getClassLoader().getResource("pause.png"));

            this.svRightArrow.setIcon(new ImageIcon(img));
            this.svLeftArrow.setIcon(new ImageIcon(img2));
            this.svPlay.setIcon(new ImageIcon(img3));
            this.svStop.setIcon(new ImageIcon(img4));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // This method close the control panel when the close button is clicked.
    private void closeButtonActionPerformed(ActionEvent evt) {
        adapter.getCyServiceRegistrar().unregisterService(this,CytoPanelComponent.class);
    }
    // This method works when the wrong type of file was tryed to choose.
    public void showInvalidWarning(){
        JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you choosed are not valid!",
                "Error!", JOptionPane.INFORMATION_MESSAGE);
        this.statusLabel.setText("Files are not valid!");
    }

    public void setStatus(String message){
        this.statusLabel.setText(message);
    }

    public void reCreateSlider(){
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        FilterUtil filter = new FilterUtil(network,table);

        ArrayList<CyNode> activities = filter.FilterRowByNodeType("activity", "nodeType");
        slider.setMaximum(activities.size()-1);

    }

    // Activating all tools
    public void activateTools(){
        this.showOnlyButton.setEnabled(true);
        this.hideButton.setEnabled(true);
        this.highLightButton.setEnabled(true);
        setShowOnly();
        setHide();
        setHighLight();
        this.groupByNodeTypeButton.setEnabled(true);
        this.importVisStyleButton.setEnabled(true);
        this.sortActivitiesByTime.setEnabled(true);
        this.showHideRelationButton.setEnabled(true);
        this.entityBasedSorting.setEnabled(true);
        this.svStop.setEnabled(true);
        this.svPlay.setEnabled(true);
        this.svLeftArrow.setEnabled(true);
        this.svRightArrow.setEnabled(true);
        this.slider.setEnabled(true);
        this.sliderCheckBox.setEnabled(true);
        this.showNodeProperties.setEnabled(true);
        reCreateSlider();
    }
    // Deactivating tools
    public void deActivateTools(){
        nodeTypes = new ArrayList<String>();
        nodeTypes.add("None");
        this.highLight.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.hide.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.showOnly.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        this.importVisStyleButton.setEnabled(false);
        this.showOnlyButton.setEnabled(false);
        this.hideButton.setEnabled(false);
        this.highLightButton.setEnabled(false);
        this.groupByNodeTypeButton.setEnabled(false);
        this.sortActivitiesByTime.setEnabled(false);
        this.showHideRelationButton.setEnabled(false);
        this.entityBasedSorting.setEnabled(false);
        this.svStop.setEnabled(false);
        this.svPlay.setEnabled(false);
        this.svLeftArrow.setEnabled(false);
        this.svRightArrow.setEnabled(false);
        this.slider.setEnabled(false);
        this.sliderCheckBox.setEnabled(false);
        this.showNodeProperties.setEnabled(false);
    }

    // Getter and setter methods.

    public MyControlPanel getInstance(){
        return this;
    }

    public Integer getMaxNode(){
        return maxNode;
    }

    public void setMaxNode(Integer maxNode){
        this.maxNode = maxNode;
    }

    public JSlider getSlider() {
        return slider;
    }

    public JLabel getSliderLabel() {
        return sliderLabel;
    }

    public CySwingAppAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CySwingAppAdapter adapter) {
        this.adapter = adapter;
    }

    public JRadioButton getActive() {
        return active;
    }

    public void setActive(JRadioButton active) {
        this.active = active;
    }

    public JRadioButton getInactive() {
        return inactive;
    }

    public void setInactive(JRadioButton inactive) {
        this.inactive = inactive;
    }

    public void setHighLight() {
        this.highLight.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
    }

    public void setHide() {
        this.hide.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
    }

    public void setShowOnly() {
        this.showOnly.setModel(new DefaultComboBoxModel(this.nodeTypes.toArray()));
        showOnly.addItem("All");
    }

    public ProvoImportCore getProvoImportCore() {
        return provoImportCore;
    }

    public void setProvoImportCore(ProvoImportCore provoImportCore) {
        this.provoImportCore = provoImportCore;
    }

    public void setXmlFileNameLabel(String xmlFileName) {
        this.xmlFileNameLabel.setText(xmlFileName);
    }

    public void setVisStyleFileNameLabel(String visStyleFileName) {
        this.visStyleFileNameLabel.setText(visStyleFileName);
    }

    public void setNodeTypes(List<String> nodeTypes){
        this.nodeTypes = nodeTypes;
    }

    public Component getComponent() {
        return this;
    }

    public CytoPanelName getCytoPanelName() {
        return CytoPanelName.WEST;
    }

    public String getTitle() {
        return "CytoVisProject Panel";
    }

    public Icon getIcon() {
        return null;
    }

    public JButton getEntityBasedSorting() {
        return entityBasedSorting;
    }

    public void setEntityBasedSorting(JButton entityBasedSorting) {
        this.entityBasedSorting = entityBasedSorting;
    }

    public CytoVisProject getCytoVisProject() {
        return cytoVisProject;
    }

    public void setCytoVisProject(CytoVisProject cytoVisProject) {
        this.cytoVisProject = cytoVisProject;
    }

    public JButton getChooseFirstGraphsNodeButton() {
        return chooseFirstGraphsNodeButton;
    }

    public void setChooseFirstGraphsNodeButton(JButton chooseFirstGraphsNodeButton) {
        this.chooseFirstGraphsNodeButton = chooseFirstGraphsNodeButton;
    }

    public JButton getChooseFirstGraphsEdgeButton() {
        return chooseFirstGraphsEdgeButton;
    }

    public void setChooseFirstGraphsEdgeButton(JButton chooseFirstGraphsEdgeButton) {
        this.chooseFirstGraphsEdgeButton = chooseFirstGraphsEdgeButton;
    }

    public JButton getChooseSecondGraphsNodeButton() {
        return chooseSecondGraphsNodeButton;
    }

    public void setChooseSecondGraphsNodeButton(JButton chooseSecondGraphsNodeButton) {
        this.chooseSecondGraphsNodeButton = chooseSecondGraphsNodeButton;
    }

    public JButton getChooseSecondGraphsEdgeButton() {
        return chooseSecondGraphsEdgeButton;
    }

    public void setChooseSecondGraphsEdgeButton(JButton chooseSecondGrapshEdgeButton) {
        this.chooseSecondGraphsEdgeButton = chooseSecondGrapshEdgeButton;
    }

    public JButton getCompareGraphsButton() {
        return compareGraphsButton;
    }

    public void setCompareGraphsButton(JButton compareGraphsButton) {
        this.compareGraphsButton = compareGraphsButton;
    }

    public JLabel getFirstGraphsNodeLabel() {
        return firstGraphsNodeLabel;
    }

    public void setFirstGraphsNodeLabel(JLabel firstGraphsNodeLabel) {
        this.firstGraphsNodeLabel = firstGraphsNodeLabel;
    }

    public JLabel getFirstGraphsEdgeLabel() {
        return firstGraphsEdgeLabel;
    }

    public void setFirstGraphsEdgeLabel(JLabel firstGraphsEdgeLabel) {
        this.firstGraphsEdgeLabel = firstGraphsEdgeLabel;
    }

    public JLabel getSecondGraphsNodeLabel() {
        return secondGraphsNodeLabel;
    }

    public void setSecondGraphsNodeLabel(JLabel secondGraphsNodeLabel) {
        this.secondGraphsNodeLabel = secondGraphsNodeLabel;
    }

    public JLabel getSecondGraphsEdgeLabel() {
        return secondGraphsEdgeLabel;
    }

    public void setSecondGraphsEdgeLabel(JLabel secondGraphsEdgeLabel) {
        this.secondGraphsEdgeLabel = secondGraphsEdgeLabel;
    }

    public JButton getImportVisStyleButton() {
        return importVisStyleButton;
    }

    public void setImportVisStyleButton(JButton importVisStyleButton) {
        this.importVisStyleButton = importVisStyleButton;
    }

    public JButton getImportNetworkButton() {
        return importNetworkButton;
    }

    public void setImportNetworkButton(JButton importNetworkButton) {
        this.importNetworkButton = importNetworkButton;
    }

    public JButton getImportTableButton() {
        return importTableButton;
    }

    public void setImportTableButton(JButton importTableButton) {
        this.importTableButton = importTableButton;
    }
}