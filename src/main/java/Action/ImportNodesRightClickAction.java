package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadTableFileTaskFactory;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class ImportNodesRightClickAction implements MouseListener {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private File file;

    public ImportNodesRightClickAction(CytoVisProject cytoVisProject){
        // Initializations of Variables
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
    }

    public void mouseClicked(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            // Making a choice to the user for file selection
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Choose Table File");
            if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
                file = fileChooser.getSelectedFile();
            }
            // Loading edges which has choosen by the user
            LoadTableFileTaskFactory NodeFile = adapter.getCyServiceRegistrar().getService(LoadTableFileTaskFactory.class);
            adapter.getTaskManager().execute(NodeFile.createTaskIterator(file));
            cytoVisProject.getMyControlPanel().setStatus("Table is loaded.");
            cytoVisProject.getMyControlPanel().importVisStyleButton.setEnabled(true);
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