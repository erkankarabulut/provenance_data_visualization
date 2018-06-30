package Action;

import App.CytoVisProject;
import Util.FilterUtil;
import jdk.nashorn.internal.scripts.JO;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;

public class NodePropertyWindow extends JFrame{

    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private Object[][] data;
    private JTable table;
    private JScrollPane scrollPane;
    private CyNode node;
    int i;

    public NodePropertyWindow(CytoVisProject cytoVisProject, CyNode node){
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.node = node;
        this.setMinimumSize(new Dimension(400, 400));
        this.setPreferredSize(new Dimension(400,400));
        this.setTitle("Node Property Window");
        this.setBackground(Color.lightGray);
        this.setVisible(true);
        this.initializeTable();
    }

    public void initializeTable(){
        CyApplicationManager manager = this.adapter.getCyApplicationManager();
        CyNetworkView networkView = manager.getCurrentNetworkView();
        CyNetwork network = networkView.getModel();
        CyTable cyTable = network.getDefaultNodeTable();

        FilterUtil filterUtil = new FilterUtil(network, cyTable);
        Map<String, Object> sampleRow = cyTable.getAllRows().get(filterUtil.findIndex(filterUtil.getAllNodes(), node)).getAllValues();
        Collection<Object> values = sampleRow.values();
        Set<String> attributeNames = sampleRow.keySet();

        String[] columnNames = {"Attribute Name", "Value"};
        this.data = new Object[values.size()][2];
        Object[] attributeNamesObjects = attributeNames.toArray();
        Iterator iterator = values.iterator();

        for(i=0; i<values.size(); i++){
            this.data[i][0] = attributeNamesObjects[i];
            Object object = iterator.next();
            this.data[i][1] = object;

        }

        TableModel model = new DefaultTableModel(this.data, columnNames){
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        this.table = new JTable(this.data, columnNames);
        this.table.setModel(model);
        this.table.setPreferredScrollableViewportSize(new Dimension(500,300));
        this.table.setFillsViewportHeight(true);
        this.table.setLayout(new BorderLayout());
        this.table.getColumnModel().getColumn(1).setWidth(40);
        this.table.setGridColor(Color.black);
        this.table.setShowGrid(true);

        JScrollPane scrollPane = new JScrollPane(this.table);
        this.add(scrollPane);
        this.setAlwaysOnTop(true);
    }


}