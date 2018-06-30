package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import java.awt.*;
import java.util.ArrayList;

public class ChangeVisualStyleTemplate{

    int templateNumber;
    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;

    public ChangeVisualStyleTemplate(CytoVisProject cytoVisProject){
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.templateNumber = 1;
    }

    public void changeVisualStyle() {
        CyApplicationManager manager = this.adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();

        FilterUtil filterUtil = new FilterUtil(network, table) ;
        ArrayList<CyNode> agents = filterUtil.FilterRowByNodeType("agent", "nodeType");
        ArrayList<CyNode> entities = filterUtil.FilterRowByNodeType("entity", "nodeType");
        ArrayList<CyNode> activities = filterUtil.FilterRowByNodeType("activity", "nodeType");

        if(templateNumber == 1){
            for(CyNode node: agents) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.ORANGE);
            }
            for(CyNode node: entities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.yellow);
            }
            for(CyNode node: activities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.BLUE);
            }
        }else if(templateNumber == 2){
            for(CyNode node: agents) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.red);
            }
            for(CyNode node: entities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.MAGENTA);
            }
            for(CyNode node: activities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.gray);
            }
        }else{
            for(CyNode node: agents) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.pink);
            }
            for(CyNode node: entities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.green);
            }
            for(CyNode node: activities) {
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_FILL_COLOR, Color.orange);
            }
        }
    }

    public void setTemplateNumber(int templateNumber) {
        this.templateNumber = templateNumber;
    }
}
