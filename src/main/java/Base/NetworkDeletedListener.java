package Base;

import App.CytoVisProject;
import App.MyControlPanel;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;

public class NetworkDeletedListener implements NetworkDestroyedListener {
    // Variables
    private CytoVisProject cytoVisProject;
    private MyControlPanel myControlPanel;
    // Initializations
    public NetworkDeletedListener(CytoVisProject cytoVisProject){
        this.cytoVisProject = cytoVisProject;
        this.myControlPanel = cytoVisProject.getMyControlPanel();
    }
    // Deactivating tools when a network is deleted
    public void handleEvent(NetworkDestroyedEvent networkDestroyedEvent) {
        if(cytoVisProject.getAdapter().getCyNetworkManager().getNetworkSet().size() == 0){
            myControlPanel.deActivateTools();
        }
    }
}
