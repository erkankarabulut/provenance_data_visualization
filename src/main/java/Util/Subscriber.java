package Util;

import App.MyControlPanel;
import jdk.nashorn.internal.scripts.JO;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.*;
import org.cytoscape.task.write.ExportNetworkImageTaskFactory;
import org.cytoscape.task.write.ExportNetworkViewTaskFactory;
import org.cytoscape.task.write.ExportTableTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import redis.clients.jedis.JedisPubSub;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Subscriber extends JedisPubSub {

    private CySwingAppAdapter adapter;
    private boolean flag;
    private MyControlPanel panel;

    public Subscriber(MyControlPanel panel){
        this.panel = panel;
        this.adapter = panel.getAdapter();
        flag = false;
    }

    @Override
    public void onMessage(String channel, String message) {

        CyNetworkFactory cnf = adapter.getCyNetworkFactory();
        CyNetworkViewFactory cnvf = adapter.getCyNetworkViewFactory();
        CyNetworkViewManager networkViewManager = adapter.getCyNetworkViewManager();
        CyNetworkManager networkManager = adapter.getCyNetworkManager();
        CyNetwork myNet = null;
        CyNetworkView networkView = adapter.getCyApplicationManager().getCurrentNetworkView();

        JSONObject data = new JSONObject();

        try {
            data = (JSONObject) new JSONParser().parse(message);
            System.out.println("Data: " + data);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(adapter.getCyApplicationManager().getCurrentNetwork() == null){
            myNet = cnf.createNetwork();
        }else {
            myNet = adapter.getCyApplicationManager().getCurrentNetwork();
        }

        FilterUtil filterUtil = new FilterUtil(myNet, myNet.getDefaultNodeTable());
        networkManager.addNetwork(myNet);

        if (networkView == null) {
            // create a new view for my network
            networkView = cnvf.createNetworkView(myNet);
            networkViewManager.addNetworkView(networkView);
        }

        if(myNet.getDefaultEdgeTable().getColumn("Connection Name") == null){
            myNet.getDefaultEdgeTable().createColumn("Connection Name", String.class, false);
        }

        if(myNet.getDefaultEdgeTable().getColumn("Interaction") == null){
            myNet.getDefaultEdgeTable().createColumn("Interaction", String.class, false);
        }

        if(myNet.getDefaultEdgeTable().getColumn("TimeStamp") == null){
            myNet.getDefaultEdgeTable().createColumn("TimeStamp", String.class, false);
        }

        if(myNet.getDefaultEdgeTable().getColumn("Source") == null){
            myNet.getDefaultEdgeTable().createColumn("Source", String.class, false);
        }

        if(myNet.getDefaultEdgeTable().getColumn("Destination") == null){
            myNet.getDefaultEdgeTable().createColumn("Destination", String.class, false);
        }

        if(myNet.getDefaultNodeTable().getColumn("Node ID") == null){
            myNet.getDefaultNodeTable().createColumn("Node ID", String.class, false);
        }
        if(myNet.getDefaultNodeTable().getColumn("nodeType") == null){
            myNet.getDefaultNodeTable().createColumn("nodeType", String.class, false);
        }
        if(myNet.getDefaultNodeTable().getColumn("TimeStamp") == null){
            myNet.getDefaultNodeTable().createColumn("TimeStamp", String.class, false);
        }
        if(myNet.getDefaultNodeTable().getColumn("startTime") == null){
            myNet.getDefaultNodeTable().createColumn("startTime", String.class, false);
        }
        if(myNet.getDefaultNodeTable().getColumn("endTime") == null){
            myNet.getDefaultNodeTable().createColumn("endTime", String.class, false);
        }

        CyTable table = adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable();
        CyColumn nodeIdColumn = table.getColumn("Node ID");
        List<String> nodeIdList = nodeIdColumn.getValues(String.class);

        String type = data.get("type").toString();
        if(type.equals("triple")){
            String nodeID1 = data.get("nodeID1").toString();
            String nodeType1 = data.get("nodeType1").toString();
            String nodeID2 = data.get("nodeID2").toString();
            String nodeType2 = data.get("nodeType2").toString();
            String nodeID3 = data.get("nodeID3").toString();
            String nodeType3 = data.get("nodeType3").toString();
            String edgeID = data.get("edgeID").toString();
            String edgeType = data.get("edgeType").toString();
            String edgeID2 = data.get("edgeID2").toString();
            String edgeType2 = data.get("edgeType2").toString();
            String edgeID3 = data.get("edgeID3").toString();
            String edgeType3 = data.get("edgeType3").toString();
            String start     = data.get("startTime").toString();
            String end       = data.get("endTime").toString();

            CyNode node;
            CyNode node2;
            CyNode node3;

            if(nodeIdList.contains(nodeID1)){
                node = filterUtil.getNode(nodeID1, adapter, "Node ID");
            }else{
                node = myNet.addNode();
            }

            if(nodeIdList.contains(nodeID2)){
                node2 = filterUtil.getNode(nodeID2, adapter, "Node ID");
            }else{
                node2 = myNet.addNode();
            }

            if(nodeIdList.contains(nodeID3)){
                node3 = filterUtil.getNode(nodeID3, adapter, "Node ID");
            }else{
                node3 = myNet.addNode();
            }

            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("name", nodeID1);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("shared name", nodeID1);

            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", nodeID2);
            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("shared name", nodeID2);

            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("name", nodeID3);
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("shared name", nodeID3);

            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("name", nodeID1);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("Node ID", nodeID1);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("nodeType", nodeType1);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("name", nodeID2);
            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("Node ID", nodeID2);
            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("nodeType", nodeType2);
            myNet.getDefaultNodeTable().getRow(node2.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("name", nodeID3);
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("Node ID", nodeID3);
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("nodeType", nodeType3);
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("startTime", start);
            myNet.getDefaultNodeTable().getRow(node3.getSUID()).set("endTime", end);

            CyEdge edge = myNet.addEdge(node2, node3, true);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Connection Name", edgeID);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Interaction", edgeType);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared name", edgeType);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared interaction", edgeType);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", edgeType);
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Source", myNet.getDefaultNodeTable().getRow(node2.getSUID()).get("name", String.class));
            myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Destination", myNet.getDefaultNodeTable().getRow(node3.getSUID()).get("name", String.class));

            CyEdge edge2 = myNet.addEdge(node, node3, true);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("Connection Name", edgeID2);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("Interaction", edgeType2);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("shared name", edgeType2);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("shared interaction", edgeType2);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("name", edgeType2);
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("Source", myNet.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class));
            myNet.getDefaultEdgeTable().getRow(edge2.getSUID()).set("Destination", myNet.getDefaultNodeTable().getRow(node3.getSUID()).get("name", String.class));

            CyEdge edge3 = myNet.addEdge(node, node2, true);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("Connection Name", edgeID3);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("Interaction", edgeType3);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("shared name", edgeType3);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("shared interaction", edgeType3);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("name", edgeType3);
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("Source", myNet.getDefaultNodeTable().getRow(node.getSUID()).get("name", String.class));
            myNet.getDefaultEdgeTable().getRow(edge3.getSUID()).set("Destination", myNet.getDefaultNodeTable().getRow(node2.getSUID()).get("name", String.class));

        }else{
            String node1     = data.get("nodeID1").toString();
            String node2     = data.get("nodeID2").toString();
            String edge      = data.get("edgeID").toString();
            String nodeType1 = data.get("nodeType1").toString();
            String nodeType2 = data.get("nodeType2").toString();
            String edgeType  = data.get("edgeType").toString();
            String start     = new String();
            String end       = new String();
            if(nodeType1.equals("activity") || nodeType2.equals("activity")){
                start = data.get("startTime").toString();
                end   = data.get("endTime").toString();
            }

            CyNode doubleNode1;
            CyNode doubleNode2;

            if(nodeIdList.contains(node1)){
                doubleNode1 = filterUtil.getNode(node1, adapter, "Node ID");
                System.out.println("burada");
            }else{
                System.out.println("burada2");
                doubleNode1 = myNet.addNode();
            }

            if(nodeIdList.contains(node2)){
                System.out.println("burada3");
                doubleNode2 = filterUtil.getNode(node2, adapter, "Node ID");
            }else{
                System.out.println("burada4");
                doubleNode2 = myNet.addNode();
            }

            System.out.println("1:: " + doubleNode1);
            System.out.println("2:: " + doubleNode1.getSUID());
            System.out.println("3:: " + node1.toString());
            System.out.println("4::" + myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()));

            myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("name", node1);
            myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("shared name", node1);
            myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("Node ID", node1);
            myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("nodeType", nodeType1);
            myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            if(nodeType1.equals("activity")){
                myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("startTime", start);
                myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).set("endTime", end);
            }

            myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("name", node2);
            myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("shared name", node2);
            myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("Node ID", node2);
            myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("nodeType", nodeType2);
            myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            if(nodeType2.equals("activity")){
                myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("startTime", start);
                myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).set("endTime", end);
            }

            CyEdge doubleEdge = myNet.addEdge(doubleNode1, doubleNode2, true);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("Connection Name", edge);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("Interaction", edgeType);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("shared name", edgeType);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("shared interaction", edgeType);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("name", edgeType);
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("Source", myNet.getDefaultNodeTable().getRow(doubleNode1.getSUID()).get("name", String.class));
            myNet.getDefaultEdgeTable().getRow(doubleEdge.getSUID()).set("Destination", myNet.getDefaultNodeTable().getRow(doubleNode2.getSUID()).get("name", String.class));

        }
        System.out.println("burada6");
        NetworkViewOrganizer networkViewOrganizer = new NetworkViewOrganizer(panel);
        networkView.updateView();
        networkViewOrganizer.reOrganizeNetwork();
        panel.getInstance().getAdapter().getCyApplicationManager().getCurrentNetworkView().updateView();
        System.out.println("burada5");

        /*

        if(type.contains("node")){
            CyNode node = myNet.addNode();
            String nodeName = data.get("name").toString();
            String nodeID   = data.get("id").toString();
            String nodeType = data.get("nodeType").toString();

            networkView.updateView();
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("name", nodeName);
            networkView.updateView();
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("shared name", nodeName);

            if(myNet.getDefaultNodeTable().getColumn("Node ID") == null){
                myNet.getDefaultNodeTable().createColumn("Node ID", String.class, false);
            }

            if(myNet.getDefaultNodeTable().getColumn("nodeType") == null){
                myNet.getDefaultNodeTable().createColumn("nodeType", String.class, false);
            }

            if(myNet.getDefaultNodeTable().getColumn("startTime") == null){
                myNet.getDefaultNodeTable().createColumn("startTime", String.class, false);
            }

            if(myNet.getDefaultNodeTable().getColumn("endTime") == null){
                myNet.getDefaultNodeTable().createColumn("endTime", String.class, false);
            }

            if(myNet.getDefaultNodeTable().getColumn("TimeStamp") == null){
                myNet.getDefaultNodeTable().createColumn("TimeStamp", String.class, false);
            }

            if(nodeType.contains("activity")){
                if(myNet.getDefaultNodeTable().getColumn("startTime") == null){
                    myNet.getDefaultNodeTable().createColumn("startTime", String.class, false);
                }

                if(myNet.getDefaultNodeTable().getColumn("endTime") == null){
                    myNet.getDefaultNodeTable().createColumn("endTime", String.class, false);
                }

                String startTime    = data.get("startTime").toString();
                String endDateTime      = data.get("endDateTime").toString();

                myNet.getDefaultNodeTable().getRow(node.getSUID()).set("startTime", startTime);
                myNet.getDefaultNodeTable().getRow(node.getSUID()).set("endDateTime", endDateTime);

            }

            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("Node ID", nodeID);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("nodeType", nodeType);
            myNet.getDefaultNodeTable().getRow(node.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());

        }else if(type.contains("edge")){

            String nodeID1          = data.get("id1").toString();
            String nodeID2          = data.get("id2").toString();
            String connectionName   = data.get("name").toString();
            String edgeType         = data.get("edgeType").toString();
            CyNode node1            = null;
            CyNode node2            = null;

            List<CyRow> nodes = adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable().getAllRows();
            for(CyRow row : nodes){
                if(row.get("Node ID", String.class).equals(nodeID1)){
                    node1 = myNet.getNode(row.get(CyIdenti, "Node ID"fiable.SUID, Long.class));
                }else if(row.get("Node ID", String.class).equals(nodeID2)){
                    node2 = myNet.getNode(row.get(CyIdenti, "Node ID"fiable.SUID, Long.class));
                }
            }

            if(node1 == null || node2 == null){
                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                        "There is no nodes with the given Node ID value ..!",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
                return;
            }else{
                CyEdge edge = myNet.addEdge(node1, node2, true);
                if(!isFlag()){
                    flag = true;
                }

                if(myNet.getDefaultEdgeTable().getColumn("Connection Name") == null){
                    myNet.getDefaultEdgeTable().createColumn("Connection Name", String.class, false);
                }

                if(myNet.getDefaultEdgeTable().getColumn("Interaction") == null){
                    myNet.getDefaultEdgeTable().createColumn("Interaction", String.class, false);
                }

                if(myNet.getDefaultEdgeTable().getColumn("TimeStamp") == null){
                    myNet.getDefaultEdgeTable().createColumn("TimeStamp", String.class, false);
                }

                if(myNet.getDefaultEdgeTable().getColumn("Source") == null){
                    myNet.getDefaultEdgeTable().createColumn("Source", String.class, false);
                }

                if(myNet.getDefaultEdgeTable().getColumn("Destination") == null){
                    myNet.getDefaultEdgeTable().createColumn("Destination", String.class, false);
                }

                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Connection Name", connectionName);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Interaction", edgeType);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared name", connectionName);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared interaction", edgeType);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", edgeType);
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("TimeStamp", Calendar.getInstance().getTime().toString());
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Source", myNet.getDefaultNodeTable().getRow(node1.getSUID()).get("name", String.class));
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("Destination", myNet.getDefaultNodeTable().getRow(node2.getSUID()).get("name", String.class));

                networkView.updateView();
            }

        }

        NetworkViewOrganizer networkViewOrganizer = new NetworkViewOrganizer(panel);
        networkView.updateView();
        networkViewOrganizer.reOrganizeNetwork();
        panel.getInstance().getAdapter().getCyApplicationManager().getCurrentNetworkView().updateView();*/
    }


    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {

    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
