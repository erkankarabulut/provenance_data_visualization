package Action;

import Util.FilterUtil;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SortActivitiesByTimeTask extends AbstractTask {
    // Variables
    private CySwingAppAdapter adapter;
    private FilterUtil filter;
    CyApplicationManager manager;
    CyNetworkView networkView;
    CyNetwork network;
    CyTable table;
    int entityCount;
    int agentCount;

    public SortActivitiesByTimeTask(CySwingAppAdapter adapter){
        // Initializations
        this.adapter = adapter;
        this.manager = adapter.getCyApplicationManager();
        this.networkView = manager.getCurrentNetworkView();
        this.network = networkView.getModel();
        this.table = network.getDefaultNodeTable();
        this.filter = new FilterUtil(network,table);
        this.entityCount = 0;
        this.agentCount = 0;
    }
    // This will sort activities by their start time
    public void run(TaskMonitor taskMonitor) {
        taskMonitor.setStatusMessage("Sorting Activities ...");
        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable table = network.getDefaultNodeTable();

        FilterUtil filter = new FilterUtil(network,table);
        CyColumn startTimeColumn = table.getColumn("startTime"); // Getting start time column
        List<String> startTimeList = filter.getTimeFromColumn(startTimeColumn); // Gets value of start time column without null values

        if(startTimeList == null){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"There is no proper (with start time" +
                            " and end time) date in the table.",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }else{
            // Getting all activities
            List<CyNode> activities = filter.FilterRowByNodeType("activity","nodeType");
            // Parsing start times to Date
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            List<Date> dates = new ArrayList<Date>();

            try{
                for(int i=0;i<startTimeList.size();i++){
                    Date date = format.parse(startTimeList.get(i));
                    dates.add(date);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            List<Date> sortedDates = new ArrayList<Date>();
            sortedDates.addAll(dates);
            // Sorting dates, removing irrelevant nodes and relocating them
            sortedDates = sort(sortedDates);
            removeIrrelevant();
            relocation(dates,sortedDates,activities);
        }
    }
    // This will locate nodes(especially agents), which does not have any relation with any activity, to the bottom side of network
    public void removeIrrelevant(){
        double tmpX = 0;
        boolean flag;
        // getting all agents
        List<CyNode> nodes = filter.FilterRowByNodeType("agent","nodeType");
        for(CyNode node : nodes){

            List<CyNode> neighbors = network.getNeighborList(node, CyEdge.Type.ANY);
            flag = false;
            // If there is no entity in a neighbors of an agent then relocate it
            for(int i=0;i<neighbors.size();i++){
                if(filter.findNodeType(neighbors.get(i)) == "entity"){
                    flag = true;
                }
            }
            if(flag == false){
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, tmpX);
                networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION, 500.0);
                tmpX = tmpX + 80;
            }
        }
    }
    // This will sort dates with using selection sort algorithm
    public List<Date> sort(List<Date> dates){
        int i,min,j;
        Date tmp;
        for (i=0;i<(dates.size()-1);i++){
            min=i;
            for(j=i+1;j<dates.size();j++){
                if(dates.get(min).compareTo(dates.get(j)) > 0){
                    tmp = dates.get(min);
                    dates.set(min,dates.get(j));
                    dates.set(j,tmp);
                }
            }
        }
        return dates;
    }
    // Relocating activities and the nodes which is related with activities
    public void relocation(List<Date> startTimeList, List<Date> sortedDates,List<CyNode> activities){
        double activityX = 0, entityX, entityY, agentX, agentY;
        FilterUtil filter = new FilterUtil(network,table);
        for(int i=0;i<startTimeList.size();i++){
            int index = findIndex(startTimeList,sortedDates.get(i));
            // Relocating activities
            this.networkView.getNodeView(activities.get(index)).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, activityX);
            this.networkView.getNodeView(activities.get(index)).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION,0.0);
            // Getting neighbors of activity
            List<CyNode> neigbours = network.getNeighborList(activities.get(index), CyEdge.Type.ANY);
            entityX = 0; entityY = 120;
            agentX = 0; agentY = -120;
            entityCount = 0;
            agentCount = 0;
            for(CyNode node : neigbours){
                // Relocating entities
                if(filter.findNodeType(node).compareTo("entity") == 0){
                    this.networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION,activityX + entityX);
                    this.networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION,entityY);
                    entityX = entityX + 20;
                    entityY = entityY + 120;
                    entityCount++;
                    // Relocating agents
                }else if(filter.findNodeType(node).compareTo("agent") == 0){
                    this.networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION,activityX + agentX);
                    this.networkView.getNodeView(node).setVisualProperty(BasicVisualLexicon.NODE_Y_LOCATION,agentY);
                    agentX = agentX + 20;
                    agentY = agentY - 120;
                    agentCount++;
                }
            }
            if(agentCount > entityCount){
                activityX = activityX + 150 + agentCount*20;
            }else{
                activityX = activityX + 150 + entityCount*20;
            }
        }
        this.networkView.updateView();
    }
    // This will find index of a date in the sorted list of dates
    public int findIndex(List<Date> dates, Date date){
        int j,index = 0;
        for(j=0;j<dates.size();j++){
            if(dates.get(j).compareTo(date) == 0){
                index = j;
            }
        }
        return index;
    }
    // Gets value of a column without null values

}