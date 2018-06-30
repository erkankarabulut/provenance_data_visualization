package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.AbstractCyAction;
import org.cytoscape.task.read.LoadTableFileTaskFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class ImportNodesAction extends AbstractCyAction {

    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private String path;

    public ImportNodesAction(CytoVisProject cytoVisProject, String path){
        super("<html>Import<br/>Table</html>");
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.path = path;
    }

    public void actionPerformed(ActionEvent e){
        File file = new File(path);
        try{
            LoadTableFileTaskFactory NodeFile = adapter.getCyServiceRegistrar().getService(LoadTableFileTaskFactory.class);
            adapter.getTaskManager().execute(NodeFile.createTaskIterator(file));
            cytoVisProject.getMyControlPanel().setStatus("Table is loaded.");
        }catch (Exception es){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"The file that you " +
                    "choosed are not valid!","Error",JOptionPane.INFORMATION_MESSAGE);
            es.printStackTrace();
        }
        cytoVisProject.getMyControlPanel().importVisStyleButton.setEnabled(true);
    }
}
