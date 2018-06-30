package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.work.TaskIterator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ImportEdgesAction extends AbstractCyAction {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private File file;

    public ImportEdgesAction(CytoVisProject cytoVisProject, String path){
        // Initializations of Variables
        super("<html>Import<br/>Network</html>");
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.file = new File(path);
    }

    public void actionPerformed(ActionEvent e){
        // Loading edges which has been extracted
        if (file == null){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"File does not" +
                    "exist","Error",JOptionPane.INFORMATION_MESSAGE);
        }else{
            try {
                LoadNetworkFileTaskFactory EdgeFile = adapter.get_LoadNetworkFileTaskFactory();
                TaskIterator taskIterator = EdgeFile.createTaskIterator(file);
                adapter.getTaskManager().execute(taskIterator);
                cytoVisProject.getMyControlPanel().setStatus("Network is loaded.");
                cytoVisProject.getMyControlPanel().importTableButton.setEnabled(true);
            }catch (Exception es){
                JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The files that" +
                        " you choosed are not valid!","error", JOptionPane.INFORMATION_MESSAGE);
                es.printStackTrace();
            }
        }
    }
}