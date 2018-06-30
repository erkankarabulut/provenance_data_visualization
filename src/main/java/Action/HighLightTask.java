package Action;

import java.awt.*;
import java.util.ArrayList;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.CyAppAdapter;
import org.cytoscape.model.*;

import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;

public class HighLightTask extends AbstractTask {
    // Variables
    private CyAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private String nodeType;

    public HighLightTask(CytoVisProject cytoVisProject, String nodeType){
        super();
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.nodeType = nodeType;
    }

    public void run(TaskMonitor taskMonitor){
        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        // Gets all nodes which has same node type value with "nodeType" string, and gets all nodes
        FilterUtil filter = new FilterUtil(network, table);
        ArrayList<CyNode> nodes = filter.FilterRowByNodeType(nodeType, "nodeType");
        ArrayList<CyNode> allNodes = filter.getAllNodes();
        // Highlights(by making color green) all nodes of the given node type
        if(nodeType.compareTo("None") == 0){
            for(CyNode node : allNodes){
                networkView.getNodeView(node).clearValueLock(BasicVisualLexicon.NODE_FILL_COLOR);
            }
        }else{
            for (CyNode node : nodes) {
                networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.GREEN);
            }
        }
    }
}