package Base;

import App.CytoVisProject;
import App.MyControlPanel;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.work.*;

import javax.swing.*;
import java.io.File;
// This class is used to organize PROV-O Import section of control panel tab
public class ProvoImportCore extends JPanel {
    // Variables
    private CySwingAppAdapter adapter;
    private CytoVisProject cytoVisProject;
    private MyControlPanel myControlPanel;
    private File visStyleFile;

    private String xmlPath;
    private String visStylePath;
    private String saxonPath;
    private String rest;
    private String wholePath;
    private String xmlFileName;
    private String visStyleFileName;
    private String saxonFileName;
    private File folderFile;
    private File outFile;

    private boolean fileControl;

    public ProvoImportCore(CytoVisProject cytoVisProject){
        // Initialization
        this.cytoVisProject = cytoVisProject;
        this.adapter = cytoVisProject.getAdapter();
        this.myControlPanel = cytoVisProject.getMyControlPanel();
        createFiles();
        // Organizing the windows console command
        this.rest = " -o:" + outFile.getAbsolutePath();
        wholePath = "java -jar ";
        fileControl = true;
    }
    // Create files under C: for extracting edges.csv end nodes.csv
    public void createFiles(){
        try{
            this.folderFile = new File("C:\\provoTransformerPlugin");
            folderFile.mkdir();
            this.outFile = new File("C:\\provoTransformerPlugin\\out.html");
            outFile.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // Method for choosing xml file
    public void chooseXmlFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose XML File");
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file.getName().contains(".xml") == false){
                setFileControl(false);
            }else{
                xmlPath = new String();
                setXmlPath(file.getAbsolutePath());
                setXmlFileName(file.getName());
                setFileControl(true);
            }
        }
    }
    // Method for choosing xml file
    public void chooseVisMapFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Visual Style File");
        if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if(file.getName().contains(".xsl") == false){
                setFileControl(false);;
            }else{
                visStylePath = new String();
                setVisStylePath(file.getAbsolutePath());
                setVisStyleFileName(file.getName());
                setFileControl(true);
            }
        }
    }
    // Method for choosing saxon file
    public void chooseSaxonFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Choose Saxon File");
        if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            if(file.getName().contains(".jar") == false){
                setFileControl(false);
            }else{
                saxonPath = new String();
                setSaxonPath(file.getAbsolutePath());
                setSaxonFileName(file.getName());
                setFileControl(true);
            }
        }
    }
    // Organizing the whole command which will be run at windows console
    public String getWholePath(){
        String path;
        path = wholePath + saxonPath + " -xsl:" + visStylePath  + " -s:" + xmlPath + rest;
        return path;
    }

    // Extracting edges and nodes files under C://provoTransformerPlugin which is initialized in constructor
    public void extractFiles(){
        folderFile.delete();
        createFiles();
        String path = getWholePath();
        TaskIterator taskIterator = new ExtractEdgesNodesTaskFactory(cytoVisProject,path).createTaskIterator();
        adapter.getTaskManager().execute(taskIterator);
    }

    public void importVisStyleTask(){
        TaskIterator taskIterator = new ImportVisualStyleTaskFactory(cytoVisProject).createTaskIterator();
        adapter.getTaskManager().execute(taskIterator);
    }

    // Getter and Setter Methods
    public boolean isFileControl() {
        return fileControl;
    }

    public void setFileControl(boolean fileControl) {
        this.fileControl = fileControl;
    }

    public String getSaxonFileName() {
        return saxonFileName;
    }

    public void setSaxonFileName(String saxonFileNAme) {
        this.saxonFileName = saxonFileNAme;
    }

    public String getVisStyleFileName() {
        return visStyleFileName;
    }

    public void setVisStyleFileName(String visStyleFileName) {
        this.visStyleFileName = visStyleFileName;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public String getVisStylePath() {
        return visStylePath;
    }

    public void setVisStylePath(String visStylePath) {
        this.visStylePath = visStylePath;
    }

    public String getSaxonPath() {
        return saxonPath;
    }

    public void setSaxonPath(String saxonPath) {
        this.saxonPath = saxonPath;
    }

}