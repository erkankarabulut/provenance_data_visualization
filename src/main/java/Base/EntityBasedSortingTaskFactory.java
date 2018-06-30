package Base;

import Action.EntityBasedSortingTask;
import App.CytoVisProject;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class EntityBasedSortingTaskFactory extends AbstractTaskFactory {
    // Variable
    private CytoVisProject cytoVisProject;

    public EntityBasedSortingTaskFactory(CytoVisProject cytoVisProject){
        // Initialization
        this.cytoVisProject = cytoVisProject;
    }
    // Creating a task iterator for sorting entities based on activity times
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new EntityBasedSortingTask(cytoVisProject));
    }
}
