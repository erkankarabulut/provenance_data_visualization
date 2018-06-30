package Util;

import App.MyControlPanel;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;

import java.util.ArrayList;

public class NetworkViewOrganizer {

    private CySwingAppAdapter adapter;
    private MyControlPanel panel;
    private Integer maxNode;
    private CyNetworkView networkView;

    public NetworkViewOrganizer(MyControlPanel panel){
        this.panel          = panel;
        this.adapter        = panel.getAdapter();
        this.maxNode        = panel.getMaxNode();
        this.networkView    = adapter.getCyApplicationManager().getCurrentNetworkView();
    }

    public void reOrganizeNetwork(){

        CyTable nodeTable = adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable();
        CyNetwork network = adapter.getCyApplicationManager().getCurrentNetwork();
        Integer i;

        FilterUtil filter               = new FilterUtil(network,nodeTable);
        ArrayList<CyNode> allNodes      = filter.getAllNodes();
        ArrayList<CyNode> deletedNodes  = new ArrayList<CyNode>();

        if(allNodes.size() > maxNode){

            Integer deleteCount = allNodes.size() - maxNode;

            for(i=0; i<deleteCount; i++){
                deletedNodes.add(allNodes.get(i));
            }

            network.removeNodes(deletedNodes);
        }

        panel.getEntityBasedSorting().doClick();
        networkView.updateView();

    }

}
