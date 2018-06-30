package App;

import java.awt.*;
import javax.swing.*;

public class CytoVisProjectHelp extends JFrame {
    // Variables
    private String helpString;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    public CytoVisProjectHelp() {
        // Initializations and visualization of help frame
        this.textArea = new JTextArea(5,20);
        this.scrollPane = new JScrollPane(textArea);
        this.setCursor(new Cursor(0));
        this.setMinimumSize(new Dimension(800,600));
        this.setPreferredSize(new Dimension(800,600));
        this.setVisible(true);
        textArea.setBackground(Color.LIGHT_GRAY);
        // Setting text to the help frame
        this.setText();
    }
    // This will sets text to the help frame and makes it uneditable
    public void setText() {
        this.helpString = "This app is made for visualize PROV-O files and help to analyze of it. Here you will learn\n" +
                "how to use the tools of CytoVisProject app.\n" +
                "\n" +
                "PROV-O Import:\n" +
                "In the PROV-O import section you can transform a PROV-O formatted .xml file to .csv files\n" +
                "which is an excel table and also can visualize in Cytoscape. You need to have a XSLT Engine\n" +
                "named Saxon which you can download from below link.\n" +
                "http://saxon.sourceforge.net/\n" +
                "\n" +
                "For this transform process you need to complete 3 steps below.\n" +
                "\n" +
                "- Choose Saxon File\n" +
                "- Choose XSL File (which can tell to transformer how to extract edge and node files.)\n" +
                "- Choose XML File (PROV-O file)\n" +
                "\n" +
                "After these steps click the \"Extract Files\" button and then the edges and nodes files will\n" +
                "be extracted in \"C:\\\\provoTransformerPlugin\" path. Now you can import your network(edges) and\n" +
                "your table(nodes) from the \"Import Network\" and \"Import Table\" buttons. Here if you do want \n" +
                "to import files that you just extract then right click to those buttons to choose another\n" +
                "file. And you can also import your visual style file from \"Import Visual Style\" part.\n" +
                "\n" +
                "Toolbox:\n" +
                "Secon part of CytoVisProject app is for help to analyze these networks. Toolbox section has\n" +
                "8 features. All of these features will be active after a network and a table is imported.\n" +
                "\n" +
                "Show Only: You can show only agent/activity/entity nodes in the network. But there is an \n" +
                "error occure while trying to show all nodes. To show all nodes and edges click to Select \n" +
                "menu on top and then click “Show all nodes and edges” tab.\n" +
                "Hide: You can hide agent/activity/entity nodes in the network.\n" +
                "Highlight: You can highlight agent/activity/entity nodes in the network.\n" +
                "Show Relations: When you make active this part you can see the hiden relations of a node\n" +
                "when you select it.\n" +
                "Group By Node Type: This feature allows you to highlight all the node which has same node\n" +
                "type with the selected nodes\n" +
                "Sort Activities by Time: It sorts all activites by time(from left to right). And the\n" +
                "relations(agents and entities) of all activities will be close to it. The nodes which is\n" +
                "not connected with any activities will be located at the bottom side of network.\n" +
                "Sort Entities Based on Activity Time: This sorts all activities of an entity based on their\n" +
                "activity time and the agents which is related to this entity will be close to the entity.\n" +
                "Others will be located at the bottom side of network.\n" +
                "Show / Hide Entity Relation: This will show / hide all the nodes of selected entities.";
        this.setTitle("CytoVisProject Help :");
        this.textArea.setText(this.helpString);
        this.textArea.setCaretPosition(0);
        this.textArea.setEditable(false);
        this.add(scrollPane);
    }
}