package Base;

import Action.GroupByNodeTypeTask;
import Action.SortActivitiesByTimeTask;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
// This class is for creating a new task iterator for grouping nodes by their node types
public class GroupByNodeTypeTaskFactory extends AbstractTaskFactory{

    private CySwingAppAdapter adapter;

    public GroupByNodeTypeTaskFactory(CySwingAppAdapter adapter){
        this.adapter = adapter;
    }

    public TaskIterator createTaskIterator() {
        return new TaskIterator(new GroupByNodeTypeTask(adapter));
    }
}