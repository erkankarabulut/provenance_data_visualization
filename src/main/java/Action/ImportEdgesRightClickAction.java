package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadNetworkFileTaskFactory;
import org.cytoscape.work.TaskIterator;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class ImportEdgesRightClickAction implements MouseListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private File file;

    public ImportEdgesRightClickAction(CytoVisProject cytoVisProject){
        // Initializations of Variables
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            // Making a choice to the user for file selection
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Network File");
            if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
            }
            // Loading edges which has choosen by the user
            LoadNetworkFileTaskFactory EdgeFile = adapter.get_LoadNetworkFileTaskFactory();
            TaskIterator taskIterator = EdgeFile.createTaskIterator(file);
            adapter.getTaskManager().execute(taskIterator);
            cytoVisProject.getMyControlPanel().setStatus("Network is loaded.");
            cytoVisProject.getMyControlPanel().importTableButton.setEnabled(true);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}