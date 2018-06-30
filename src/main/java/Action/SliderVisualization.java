package Action;

import App.MyControlPanel;
import Util.FilterUtil;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SliderVisualization {

    private MyControlPanel myControlPanel;
    private JSlider slider;

    public SliderVisualization(MyControlPanel myControlPanel){
        this.myControlPanel = myControlPanel;
        this.slider = myControlPanel.getSlider();
    }

    public void hideFutureNodes(List<String> timeList, FilterUtil filter, CyNetwork network, CyNetworkView networkView){
        ArrayList<CyNode> activities = filter.FilterRowByNodeType("activity", "nodeType");
        sortActivityTimes(timeList, activities);
        myControlPanel.sliderLabel.setText(timeList.get(slider.getValue()));
        for(int i=0;i<activities.size();i++){
            if(timeList.get(i).compareTo(timeList.get(slider.getValue())) > 0){
                List<CyNode> neighborList = network.getNeighborList(activities.get(i), CyEdge.Type.ANY);
                networkView.getNodeView(activities.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
                for(CyNode node : neighborList){
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,false);
                    List<CyEdge> edges = network.getAdjacentEdgeList(node, CyEdge.Type.ANY);
                    for(CyEdge edge : edges){
                        networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, false);
                    }
                }
            }else{
                List<CyNode> neighborList = network.getNeighborList(activities.get(i), CyEdge.Type.ANY);
                networkView.getNodeView(activities.get(i)).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
                for(CyNode node : neighborList){
                    networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_VISIBLE,true);
                    List<CyEdge> edges = network.getAdjacentEdgeList(node, CyEdge.Type.ANY);
                    for(CyEdge edge : edges){
                        networkView.getEdgeView(edge).setVisualProperty(BasicVisualLexicon.EDGE_VISIBLE, true);
                    }
                }
            }
        }
    }

    public void sortActivityTimes(List<String> timeList, ArrayList<CyNode> activities){
        int i,min,j;
        String tmp;
        CyNode nodeTmp;
        for (i=0;i<(timeList.size()-1);i++){
            min=i;
            for(j=i+1;j<timeList.size();j++){
                if(timeList.get(min).compareTo(timeList.get(j)) > 0){
                    tmp = timeList.get(min);
                    timeList.set(min,timeList.get(j));
                    timeList.set(j,tmp);
                    nodeTmp = activities.get(j);
                    activities.set(j,activities.get(min));
                    activities.set(min, nodeTmp);
                }
            }
        }
    }
}
