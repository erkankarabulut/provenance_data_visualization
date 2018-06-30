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

public class ShowOnlyTask extends AbstractTask {
    // Variables
    private CyAppAdapter adapter;
    private String nodeType;
    // Initializations
    public ShowOnlyTask(CyAppAdapter adapter, String nodeType){
        super();
        this.adapter = adapter;
        this.nodeType = nodeType;
    }
    // This method shows only the nodes which has the specified node type
    public void run(TaskMonitor taskMonitor){
        // Initializing components
        CyApplicationManager manager = this.adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();
        // Getting all nodes and getting all nodes which has the specified node type.
        FilterUtil filter = new FilterUtil(network, table);
        List<CyNode> allNodes = network.getNodeList();
        ArrayList<CyNode> nodes = filter.FilterRowByNodeType(nodeType, "nodeType");
        // Hiding all nodes but the others which is choosen by user
        if(nodeType.compareToIgnoreCase("All") == 0){
            for(int i=0;i<allNodes.size();i++){
                networkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
                List<CyEdge> edges = network.getAdjacentEdgeList(allNodes.get(i), CyEdge.Type.ANY);
                for(CyEdge edge : edges){
                    networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
                }
            }
        }else{
            for (int i = 0; i < allNodes.size(); i++) {
                networkView.getNodeView(allNodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
                List<CyEdge> edges = network.getAdjacentEdgeList(allNodes.get(i), CyEdge.Type.ANY);
                for(CyEdge edge : edges){
                    networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
                }
            }
            for (CyNode node : nodes) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
                List<CyEdge> edges = network.getAdjacentEdgeList(node, CyEdge.Type.ANY);
                for(CyEdge edge : edges){
                    networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
                }
            }
        }
        networkView.updateView();
    }
}