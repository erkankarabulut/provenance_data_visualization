package Base;

import App.CytoVisProject;
import Action.ExtractEdgesNodesTask;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class ExtractEdgesNodesTaskFactory extends AbstractTaskFactory{
    // Variables
    private CytoVisProject cytoVisProject;
    private String path;

    public ExtractEdgesNodesTaskFactory(CytoVisProject cytoVisProject, String path){
        // Initializations
        this.cytoVisProject = cytoVisProject;
        this.path = path;
    }
    // Creating a new task iterator for extracting edges and nodes
    public TaskIterator createTaskIterator(){
        return new TaskIterator(new ExtractEdgesNodesTask(cytoVisProject,path));
    }
}
