package Action;

import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.awt.*;
import java.util.List;

public class GroupByNodeTypeTask extends AbstractTask{
    // Variables
    private CySwingAppAdapter adapter;

    public GroupByNodeTypeTask(CySwingAppAdapter adapter){
        // Initialization
        this.adapter = adapter;
    }
    // This will highlight( by making color green ) the nodes which is in same node type with selected node(s)
    public void run(TaskMonitor taskMonitor){
        taskMonitor.setStatusMessage("Grouping by node type ...");
        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        // Getting all selected nodes
        List<CyNode> nodes = CyTableUtil.getNodesInState(network,"selected",true);
        for(CyNode node:nodes){

            FilterUtil filter = new FilterUtil(network,table);
            String nodeType=filter.findNodeType(node);
            // Getting all nodes with same node type with selected node
            List<CyNode> matchingNodes=filter.FilterRowByNodeType(nodeType,"nodeType");
            // If value is not locked this means it is not hiden on the network, because it lockes green color value to node
            if(networkView.getNodeView(node).isValueLocked(BasicVisualLexicon.NODE_FILL_COLOR)){
                for(CyNode node2:matchingNodes){
                    networkView.getNodeView(node2).clearValueLock(BasicVisualLexicon.NODE_FILL_COLOR);
                }
            } else{
                for(CyNode node2:matchingNodes){
                    networkView.getNodeView(node2).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.GREEN);
                }
            }
        }
    }
}
