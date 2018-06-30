package App;

import Action.ControlPanelAction;
import Base.NetworkDeletedListener;
import Base.NodeSelectedListener;
import Base.TableSetListener;
import org.cytoscape.app.swing.AbstractCySwingApp;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.model.events.RowsSetListener;

import java.util.Properties;

public class CytoVisProject extends AbstractCySwingApp{
    // Variables
    private MyControlPanel myControlPanel;
    private NodeSelectedListener nodeSelectedListener;
    private TableSetListener tableSetListener;

    public CytoVisProject(CySwingAppAdapter adapter){
        super(adapter);
        // Initializations and registrations
        // Creating and registering a new control panel tab
        this.myControlPanel = new MyControlPanel(this);
        adapter.getCyServiceRegistrar().registerService(myControlPanel, CytoPanelComponent.class,new Properties());

        ControlPanelAction controlPanelAction = new ControlPanelAction(adapter.getCySwingApplication(),myControlPanel,adapter);
        adapter.getCyServiceRegistrar().registerService(controlPanelAction, CyAction.class,new Properties());
        // Creating and registering a new RowsSetListener
        this.tableSetListener = new TableSetListener(this);
        adapter.getCyServiceRegistrar().registerService(tableSetListener, RowsSetListener.class,new Properties());
        // Creating and registering a new NetworkDestroyedListener
        NetworkDeletedListener networkDeletedListener = new NetworkDeletedListener(this);
        adapter.getCyServiceRegistrar().registerService(networkDeletedListener, NetworkDestroyedListener.class,new Properties());

        this.nodeSelectedListener = new NodeSelectedListener(this);
        adapter.getCyServiceRegistrar().registerService(nodeSelectedListener,RowsSetListener.class,new Properties());

    }
    // Getter and setter methods

    public NodeSelectedListener getNodeSelectedListener() {
        return nodeSelectedListener;
    }

    public void setNodeSelectedListener(NodeSelectedListener nodeSelectedListener) {
        this.nodeSelectedListener = nodeSelectedListener;
    }

    public MyControlPanel getMyControlPanel() {
        return myControlPanel;
    }

    public CySwingAppAdapter getAdapter(){
        return this.swingAdapter;
    }

    public void setMyControlPanel(MyControlPanel myControlPanel) {
        this.myControlPanel = myControlPanel;
    }

    public TableSetListener getTableSetListener() {
        return tableSetListener;
    }

    public void setTableSetListener(TableSetListener tableSetListener) {
        this.tableSetListener = tableSetListener;
    }
}
