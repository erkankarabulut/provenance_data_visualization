package Action;

import App.CytoVisProject;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.task.visualize.ApplyVisualStyleTaskFactory;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.Tunable;

import javax.swing.*;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ImportVisualStyleTask extends AbstractTask {

    private File file;
    private CytoVisProject cytoVisProject;
    private CySwingAppAdapter adapter;
    private final LoadVizmapFileTaskFactory loadVizmapFileTaskFactory;
    private CyNetworkView view;
    private final ApplyVisualStyleTaskFactory applyVisualStyleTaskFactory;

    public ImportVisualStyleTask(CytoVisProject cytoVisProject){
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.loadVizmapFileTaskFactory = adapter.getCyServiceRegistrar().getService(LoadVizmapFileTaskFactory.class);
        this.view = adapter.getCyApplicationManager().getCurrentNetworkView();
        this.applyVisualStyleTaskFactory = adapter.getCyServiceRegistrar().getService(ApplyVisualStyleTaskFactory.class);
        chooseFile();
    }

    public void chooseFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Visual Style File");
        if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
            this.file = fileChooser.getSelectedFile();
        }
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        Set<VisualStyle> vsSet = loadVizmapFileTaskFactory.loadStyles(this.file);
        if (this.view == null || vsSet.size() == 0)
            return;

        final Set<CyNetworkView> views = new HashSet<CyNetworkView>();
        views.add(this.view);
        TaskIterator taskIterator = this.applyVisualStyleTaskFactory.createTaskIterator(views);
        this.insertTasksAfterCurrentTask(taskIterator);
        this.cytoVisProject.getMyControlPanel().setStatus("Visual Style is loaded.");
    }
}
