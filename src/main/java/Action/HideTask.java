package Action;

import java.util.ArrayList;
import java.util.List;

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

public class HideTask extends AbstractTask {
    // Variables
    private CyAppAdapter adapter;
    private String nodeType;

    public HideTask(CyAppAdapter adapter, String nodeType){
        super();
        // Initialization
        this.adapter = adapter;
        this.nodeType = nodeType;
    }
    // This will hide nodes which has same node type with "nodeType" string
    public void run(TaskMonitor taskMonitor){
        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();

        FilterUtil filter = new FilterUtil(network, table);
        ArrayList<CyNode> nodes = filter.FilterRowByNodeType(nodeType, "nodeType");
        // Hiding all nodes with "nodeType"
        if(nodeType.compareTo("None") == 0){
            ArrayList<CyNode> allNodes = filter.getAllNodes();
            for(CyNode node: allNodes){
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE, true);
            }
            List<CyRow> allRows = network.getDefaultNodeTable().getAllRows();
            for(CyRow row : allRows){
                row.set("selected",false);
            }
        }else {
            for(CyNode node : nodes){
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
            }
        }
    }
}