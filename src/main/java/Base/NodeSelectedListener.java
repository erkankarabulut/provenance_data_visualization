package Base;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTableUtil;
import org.cytoscape.model.events.RowsSetEvent;
import org.cytoscape.model.events.RowsSetListener;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import java.util.List;

public class NodeSelectedListener implements RowsSetListener {
    // Varibles
    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private boolean flag;

    public NodeSelectedListener(CytoVisProject cytoVisProject){
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.flag = false;
    }

    public void handleEvent(RowsSetEvent e){
        // This flag for the active - inactive button which is in the MyControlPanel class.
        // If it is active then method will show the neighbors of the selected node.
        if(isFlag()){
            CyApplicationManager manager = adapter.getCyApplicationManager();
            CyNetworkView networkView = manager.getCurrentNetworkView();
            CyNetwork network = networkView.getModel();
            FilterUtil filter = new FilterUtil(network,network.getDefaultNodeTable());
            List<CyNode> selectedNodes = CyTableUtil.getNodesInState(network, "selected", true);
            // Showing neighbors of the selected nodes
            for(int i=0;i<selectedNodes.size();i++){
                List<CyNode> neighborList = network.getNeighborList(selectedNodes.get(i), CyEdge.Type.ANY);
                for (int j=0;j<neighborList.size();j++){
                    networkView.getNodeView(neighborList.get(j)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
                    List<CyEdge> edges = network.getAdjacentEdgeList(neighborList.get(j), CyEdge.Type.ANY);
                    for(CyEdge edge : edges){
                        networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
                    }
                }
            }
            networkView.updateView();
        }
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
