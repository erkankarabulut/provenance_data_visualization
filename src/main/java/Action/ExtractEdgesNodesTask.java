package Action;

import App.CytoVisProject;
import Base.ProvoImportCore;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;

public class ExtractEdgesNodesTask extends AbstractTask{
    // Variables
    private String path;
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private ProvoImportCore provoImportCore;

    public ExtractEdgesNodesTask(CytoVisProject cytoVisProject, String path){
        // Initializations of variables
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.path = path;
        this.provoImportCore = cytoVisProject.getMyControlPanel().getProvoImportCore();
    }

    public void run(TaskMonitor taskMonitor){
        taskMonitor.setStatusMessage("Extracting Edges and Nodes ...");
        // Code for extracting edges and nodes files
        try {
            // This will run a command which is created in provoImportCore class and command will create edges and nodes files
            Process process = Runtime.getRuntime().exec(path);
            process.waitFor();
            // Setting status after extracting
            cytoVisProject.getMyControlPanel().setStatus("Files are extracted to C:/provoTransformerPlugin.");
        } catch (Exception es) {
            JOptionPane.showMessageDialog(this.adapter.getCySwingApplication().getJFrame(),
                    "The file that you choosed are not valid."  ,
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
            es.printStackTrace(System.err);
            cytoVisProject.getMyControlPanel().showInvalidWarning();
        }
    }
}