package Action;

import App.CytoVisProject;
import Base.CompareGraphsCore;
import Base.ImportVisualStyleTaskFactory;
import Util.FilterUtil;

import java.awt.*;
import java.io.File;
import java.util.*;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.TaskIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DrawComparedGraphs {

    private CytoVisProject      cytoVisProject;
    private CySwingAppAdapter   adapter;
    private FilterUtil          filterUtil;
    private CyNetwork           network;
    private CyTable             nodeTable;
    private CompareGraphsCore   compareGraphsCore;
    private JSONArray           firstGraphNodes;
    private JSONArray           secondGraphNodes;
    private JSONArray           firstGraphEdges;
    private JSONArray           secondGraphEdges;
    private ArrayList           attendanceList;
    private CyNetworkFactory    cnf;
    private CyNetworkViewFactory cnvf;
    private CyNetworkViewManager networkViewManager;
    private CyNetworkManager    networkManager;
    private CyNetwork           myNet;
    private CyNetworkView       networkView;

    public DrawComparedGraphs(CompareGraphsCore compareGraphsCore, CytoVisProject cytoVisProject){
        this.compareGraphsCore = compareGraphsCore;
        this.adapter           = compareGraphsCore.getAdapter();
        this.firstGraphNodes   = compareGraphsCore.getFirstGraphsNodes();
        this.secondGraphNodes  = compareGraphsCore.getSecondGraphsNodes();
        this.firstGraphEdges   = compareGraphsCore.getFirstGraphsEdges();
        this.secondGraphEdges  = compareGraphsCore.getSecondGraphsEdges();
        this.attendanceList    = compareGraphsCore.getSimilarNodePairs();
        this.cytoVisProject    = cytoVisProject;
    }

    public void draw(){
        Integer i;
        Integer j;

        cnf = adapter.getCyNetworkFactory();
        cnvf = adapter.getCyNetworkViewFactory();
        networkViewManager = adapter.getCyNetworkViewManager();
        networkManager = adapter.getCyNetworkManager();
        myNet = null;
        networkView = adapter.getCyApplicationManager().getCurrentNetworkView();

        myNet = cnf.createNetwork();
        networkManager.addNetwork(myNet);

        networkView = cnvf.createNetworkView(myNet);
        networkViewManager.addNetworkView(networkView);

        // Adding properties to the network tables
        Set<Map.Entry> properties = ((JSONObject) firstGraphNodes.get(0)).entrySet();
        Iterator iterator = properties.iterator();
        addNodeProperties(myNet, iterator);

        properties = ((JSONObject) secondGraphNodes.get(0)).entrySet();
        iterator = properties.iterator();
        addNodeProperties(myNet, iterator);

        properties = ((JSONObject) firstGraphEdges.get(0)).entrySet();
        iterator = properties.iterator();
        addEdgeProperties(myNet, iterator);

        properties = ((JSONObject) secondGraphEdges.get(0)).entrySet();
        iterator = properties.iterator();
        addEdgeProperties(myNet, iterator);

        FilterUtil filterUtil = new FilterUtil(myNet, myNet.getDefaultNodeTable());
        // Visualizing nodes and edges
        visualizeNodes(myNet, networkView, firstGraphNodes);
        visualizeEdges(myNet, networkView, filterUtil, firstGraphEdges);

        visualizeNodes(myNet, networkView, secondGraphNodes);
        visualizeEdges(myNet, networkView, filterUtil, secondGraphEdges);

        TaskIterator taskIterator = new ImportVisualStyleTaskFactory(cytoVisProject).createTaskIterator();
        adapter.getTaskManager().execute(taskIterator);

        networkView.updateView();
    }

    public void changeColors(CompareGraphsCore compareGraphsCore){
        Integer i;
        //Changing color of similar and non-similar nodes
        this.attendanceList = compareGraphsCore.getSimilarNodePairs();
        FilterUtil filterUtil = new FilterUtil(adapter.getCyApplicationManager().getCurrentNetwork(),
                adapter.getCyApplicationManager().getCurrentNetwork().getDefaultNodeTable());
        ArrayList<CyNode> allNodes = filterUtil.getAllNodes();

        for(CyNode node:allNodes){
            networkView.getNodeView(node).clearValueLock(BasicVisualLexicon.NODE_FILL_COLOR);
        }

        for(i=0; i<attendanceList.size(); i++){
            ArrayList<String> nodeDatas = (ArrayList<String>) attendanceList.get(i);
            String firstNode            = nodeDatas.get(0);
            String secondNode           = nodeDatas.get(1);

            for(CyNode node : allNodes){
                if(myNet.getRow(node).get("nodeID", String.class).equals(firstNode) ||
                        myNet.getRow(node).get("nodeID", String.class).equals(secondNode)){
                    networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.GREEN);
                }
            }
        }

        for(CyNode node : allNodes){
            if(!networkView.getNodeView(node).isValueLocked(BasicVisualLexicon.NODE_FILL_COLOR)){
                networkView.getNodeView(node).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, Color.RED);
            }
        }
    }

    private void addEdgeProperties(CyNetwork myNet, Iterator iterator) {
        while (iterator.hasNext()){
            String tmp = iterator.next().toString();
            tmp = tmp.substring(8, tmp.length());
            if(myNet.getDefaultEdgeTable().getColumn(tmp) == null){
                myNet.getDefaultEdgeTable().createColumn(tmp, String.class, false);
            }
        }
    }

    private void addNodeProperties(CyNetwork myNet, Iterator iterator) {
        while (iterator.hasNext()){
            String tmp = iterator.next().toString();
            tmp = tmp.substring(8, tmp.length());
            if(myNet.getDefaultNodeTable().getColumn(tmp) == null){
                myNet.getDefaultNodeTable().createColumn(tmp, String.class, false);
            }
        }
    }

    private void visualizeEdges(CyNetwork myNet, CyNetworkView networkView, FilterUtil filterUtil, JSONArray edgeList) {
        Integer i;
        Set<Map.Entry> properties;
        Iterator iterator;
        for(i=1; i<edgeList.size(); i++){
            JSONObject nodeObject = (JSONObject) edgeList.get(i);
            CyEdge edge = myNet.addEdge(filterUtil.getNode(nodeObject.get("node1ID").toString(), adapter, "nodeID"),
                    filterUtil.getNode(nodeObject.get("node2ID").toString(), adapter, "nodeID"), true);
            properties = ((JSONObject) edgeList.get(0)).entrySet();
            iterator = properties.iterator();
            while (iterator.hasNext()){
                String tmp = iterator.next().toString();
                tmp = tmp.substring(8, tmp.length());
                myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set(tmp, nodeObject.get(tmp));
                if(tmp.equals("edgeID")){
                    myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("name", nodeObject.get(tmp));
                    myNet.getDefaultEdgeTable().getRow(edge.getSUID()).set("shared name", nodeObject.get(tmp));
                }
            }
            networkView.updateView();
        }
    }

    private void visualizeNodes(CyNetwork myNet, CyNetworkView networkView, JSONArray nodeList) {
        Integer i;
        Set<Map.Entry> properties;
        Iterator iterator;
        for(i=1; i< nodeList.size(); i++){
            CyNode node = myNet.addNode();
            JSONObject nodeObject = (JSONObject) nodeList.get(i);
            properties = ((JSONObject) nodeList.get(0)).entrySet();
            iterator = properties.iterator();
            while (iterator.hasNext()){
                String tmp = iterator.next().toString();
                tmp = tmp.substring(8, tmp.length());
                myNet.getDefaultNodeTable().getRow(node.getSUID()).set(tmp, nodeObject.get(tmp));
                if(tmp.equals("nodeID")){
                    myNet.getDefaultNodeTable().getRow(node.getSUID()).set("name", nodeObject.get(tmp));
                    myNet.getDefaultNodeTable().getRow(node.getSUID()).set("shared name", nodeObject.get(tmp));
                }
            }
            networkView.updateView();
        }
    }

    public ArrayList findNodeTypes(ArrayList idList, JSONArray nodes){
        Integer i = 0;
        Integer j;
        ArrayList<String> nodeTypes = new ArrayList<>();

        while(i < idList.size()){
            for(j=1; j<nodes.size(); j++){

                Integer id = Integer.parseInt(((JSONObject) nodes.get(j)).get("nodeID").toString());
                if(id == (Integer)idList.get(i)){
                    String type = ((JSONObject) nodes.get(j)).get("nodeType").toString();
                    nodeTypes.add(type);
                }

            }

            i++;
        }

        return nodeTypes;
    }

}
