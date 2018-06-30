package Action;
import App.MyControlPanel;
import java.awt.event.ActionEvent;
import java.util.Properties;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.*;

public class ControlPanelAction extends AbstractCyAction {
    // Variables
    private static final long serialVersionUID = 1L;
    private CySwingApplication swingApplication;
    private final CytoPanel cytoPanelWest;
    private MyControlPanel myControlPanel;
    private CySwingAppAdapter adapter;
    // Initialization of variables
    public ControlPanelAction(CySwingApplication desktopApp,
                              MyControlPanel myCytoPanel, CySwingAppAdapter adapter){
        super("CytoVisProject");
        setPreferredMenu("Apps");

        this.swingApplication = desktopApp;
        this.adapter = adapter;

        this.cytoPanelWest = this.swingApplication.getCytoPanel(CytoPanelName.WEST);
        this.myControlPanel = myCytoPanel;
    }
    // Opening a new tab on Control Panel
    public void actionPerformed(ActionEvent e) {
        adapter.getCyServiceRegistrar().registerService(myControlPanel, CytoPanelComponent.class,new Properties());

        if (cytoPanelWest.getState() == CytoPanelState.HIDE) {
            cytoPanelWest.setState(CytoPanelState.DOCK);
        }

        if (cytoPanelWest.indexOfComponent(myControlPanel) == -1) {
            return;
        }
        cytoPanelWest.setSelectedIndex(cytoPanelWest.indexOfComponent(myControlPanel));
    }
}
