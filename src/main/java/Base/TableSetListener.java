package Base;

import App.CytoVisProject;
import App.MyControlPanel;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.*;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;

import java.util.ArrayList;
import java.util.List;

public class TableSetListener implements RowsSetListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private MyControlPanel myControlPanel;

    public TableSetListener(CytoVisProject cytoVisProject) {
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        myControlPanel = cytoVisProject.getMyControlPanel();
    }
    // Method for finding all different kind of node types.
    public void handleEvent(RowsSetEvent e) {
        // Create a new filter
        if(adapter.getCyApplicationManager().getCurrentNetwork() != null &&
                adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable() != null){
            FilterUtil filter = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                    adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable());
            List<String> nodeTypes = new ArrayList<String>();
            nodeTypes.add("None");
            // Get all nodes
            ArrayList<CyNode> nodes = filter.getAllNodes();
            // Looking all nodes for new node types
            for(CyNode node : nodes){
                if(nodeTypes.contains(filter.findNodeType(node)) == false){
                    nodeTypes.add(filter.findNodeType(node));
                }
            }
            myControlPanel.setNodeTypes(nodeTypes);
            myControlPanel.activateTools();
            adapter.getCyApplicationManager().getCurrentNetworkView().updateView();
        }

    }
}