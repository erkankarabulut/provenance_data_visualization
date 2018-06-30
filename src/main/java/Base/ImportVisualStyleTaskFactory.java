package Base;

import Action.ImportVisualStyleTask;
import App.CytoVisProject;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

import java.io.File;

public class ImportVisualStyleTaskFactory extends AbstractTaskFactory {

    private CytoVisProject cytoVisProject;

    public ImportVisualStyleTaskFactory(CytoVisProject cytoVisProject){
        this.cytoVisProject = cytoVisProject;
    }

    public TaskIterator createTaskIterator() {
        return new TaskIterator(new ImportVisualStyleTask(cytoVisProject));
    }
}
