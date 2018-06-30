package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ShowHideEntityRelationAction extends AbstractCyAction {
    // Variables
    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    CyApplicationManager manager;
    CyNetworkView networkView;
    CyNetwork network;
    CyTable table;
    FilterUtil filter;
    List<CyNode> neighbours;

    public ShowHideEntityRelationAction(CytoVisProject cytoVisProject){
        // Initializations
        super("<html>Show / Hide<br/>Entity Relation</html>");
        this.cytoVisProject = cytoVisProject;
        if(cytoVisProject.getAdapter().getCyApplicationManager().getCurrentNetwork() == null){
            this.setEnabled(false);
        }else{
            this.setEnabled(true);
        }
    }

    // This will show / hide of an selected entity's relations
    public void actionPerformed(ActionEvent e){
        // Organizing necessary variables
        this.adapter = cytoVisProject.getAdapter();
        this.manager = adapter.getCyApplicationManager();
        this.networkView = manager.getCurrentNetworkView();
        this.network = networkView.getModel();
        this.table = network.getDefaultNodeTable();
        this.filter = new FilterUtil(network,table);
        this.neighbours = new ArrayList<CyNode>();

        // getting all selected nodes
        int countOfNonEntityNodes = 0;
        List<CyNode> selected = CyTableUtil.getNodesInState(network,"selected",true);
        for(CyNode node : selected){
            String nodeType = filter.findNodeType(node);
            // If node type is equal to entity then it will show / hide its relation
            if(nodeType.equals("entity")){
                List<CyNode> agents = filter.findNodeNeighbors("agent",node);
                List<CyNode> activities = filter.findNodeNeighbors("activity",node);
                integrate(activities, agents);
            }else{
                countOfNonEntityNodes++;
            }
        }
        List<CyRow> allRows = network.getDefaultNodeTable().getAllRows();
        for(CyRow row : allRows){
            row.set("selected",false);
        }
        action(neighbours);
        if(countOfNonEntityNodes > 0){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                    "There are " + countOfNonEntityNodes + " non-entity node(s).",
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }

    public void integrate(List<CyNode> activities, List<CyNode> agents){
        for(int i=0;i<agents.size();i++){
            if(neighbours.contains(agents.get(i)) == false){
                neighbours.add(agents.get(i));
            }
        }
        for(int i=0;i<activities.size();i++){
            if(neighbours.contains(activities.get(i)) == false){
                neighbours.add(activities.get(i));
            }
        }
    }
    // This will show / hide a nodes neighbors according to it's locking value
    public void action(List<CyNode> nodes){
        for(int i=0;i<nodes.size();i++){
            // If node is locked before then makes it visible and clears lock
            if(networkView.getNodeView(nodes.get(i)).getVisualProperty(BasicVisualLexicon.NODE_VISIBLE) == true){
                networkView.getNodeView(nodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
            }else{  // If node is not locked then lock it and make it invisible
                networkView.getNodeView(nodes.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
            }
        }
    }

}