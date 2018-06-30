package Base;

import Action.SortActivitiesByTimeTask;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
// This class is for creating a new task iterator for sorting activities by their time task
public class SortActivitesByTimeTaskFactory extends AbstractTaskFactory{

    private CySwingAppAdapter adapter;

    public SortActivitesByTimeTaskFactory(CySwingAppAdapter adapter){
        this.adapter = adapter;
    }

    public TaskIterator createTaskIterator() {
        return new TaskIterator(new SortActivitiesByTimeTask(adapter));
    }
}