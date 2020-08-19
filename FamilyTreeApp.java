/**
 *
 * @author Faiz Syed
 * Student ID: 33243485
 * Unit: ICT373 Assignment 2
 * Topic: Family Tree Application
 * Date: 31st-May-2020
 * 
 */
package familytree;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;
import javafx.application.Application;
import javafx.geometry.Insets;
//import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
//import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * For building GUI
 * @author Faiz
 */
public class FamilyTreeApp extends Application 
{
    private BorderPane border_pane;
    private Button AddRootBtn;
    private HBox editScene;
    private HBox viewScene;
    private FormHandler form;
    private TreeHandler familytree;
    private TreeView<Object> mytree;
    private String currentScene;
    private Text text;


    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) 
    {
        launch(args);
    }
    
        
    @Override
    /**
     * Overrides start method
     */
    public void start(Stage primaryStage) 
    {
        border_pane = BuildScene();
        border_pane.setStyle("-fx-background-color: #B2CFEA");
                
        primaryStage.setResizable(true);
        
        primaryStage.setScene(new Scene(border_pane));
        primaryStage.setTitle("Family Tree App");
        
        primaryStage.show();
        
        displayScene("DEFAULT");
    }
    
    
    @Override
    /**
     * Overrides init method
     */
    public void init()
    {
        familytree = new TreeHandler();
        form = new FormHandler();
        currentScene = null;
    }
    
    
    /**
     * Helps to smoothly switch between different scenes
     * @param scene 
     */
    private void displayScene(String scene)
    {
        currentScene = scene;
        switch(scene)
        {
            case "VIEW":  //when viewing member details
                invokeAddMember(familytree.getChosenItem());
                form.showViewableForm();
                viewScene.setVisible(true);
                editScene.setVisible(false); //disable edit scene
                break;  
            
            case "ADD": //to add a new member
                form.showEmptyForm();
                editScene.setVisible(true);
                AddRootBtn.setVisible(false);
                viewScene.setVisible(false);
                break;
                          
            case "EDIT":  //to edit an existing member
                form.showEditableForm();
                editScene.setVisible(true);
                viewScene.setVisible(false);
                break;
         
            case "DEFAULT": //default scene asking for root member
                default:
                    form.hideForm();
                    editScene.setVisible(false);
                    viewScene.setVisible(false);
                    mytree.getSelectionModel().clearSelection();
                    
                    if (!familytree.isNull()) //this is useful when we load a tree
                    {
                        AddRootBtn.setVisible(false);
                    } 
                    else 
                    {
                        AddRootBtn.setVisible(true);
                    }
                    break;
        }
    }
    
    
    /**
     * To construct the initial scene of the GUI
     * @return window- constructed BorderPane
     */
    private BorderPane BuildScene()
    {
        BorderPane window = new BorderPane();
        VBox rightPane = new VBox();
        VBox topPane = makeTopPane();
        mytree = familytree.getTree(); 
        
        mytree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mytree.setOnMouseClicked(e -> memberSelectionHandler()); //when a member is selected
        AddRootBtn = new Button("Add Root Person");
        AddRootBtn.setOnAction(e -> displayScene("ADD")); //when the add root button is selected
        AddRootBtn.managedProperty().bind(AddRootBtn.visibleProperty());
        AddRootBtn.setStyle("-fx-base: orange");
        editScene = EditSceneMaker();
        viewScene = ViewSceneMaker();   
        
        rightPane.getChildren().addAll(AddRootBtn, form.getRoot(), editScene, viewScene);
        BorderPane.setMargin(topPane, new Insets(8)); //Insets is four sides of the rectangle created for the GUI 
        BorderPane.setMargin(mytree, new Insets(10));
        BorderPane.setMargin(rightPane, new Insets(8)); 
        window.setPadding(new Insets(10));
        window.setTop(topPane);
        window.setLeft(mytree);
        window.setRight(rightPane);

        return window;
    }
    
    /**
     * Creates the structure for edit member scene of the form
     * @return 
     */
    private HBox EditSceneMaker() 
    {
        HBox components = new HBox(20);
        Button save = new Button("Save");
        save.setStyle("-fx-base: orange");
        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-base: orange");

        components.setPadding(new Insets(20, 0, 20, 0));
        components.getChildren().addAll(save, cancel);
        components.managedProperty().bind(components.visibleProperty());
        cancel.setOnAction(e -> cancelButtonHandler());
        save.setOnAction(e -> saveButtonHandler());

        return components;
    }
    
    /**
     * Method to hide or show option to add relative depending on the member type
     * @param chosenItem 
     */
    private void invokeAddMember(TreeItem<Object> chosenItem) 
    {
        if(chosenItem != null)
        {
            Button add = (Button) border_pane.lookup(String.format("#%s", "Add_Relative_ID"));
            add.setStyle("-fx-base: orange;");
            
            if(familytree.getMemberType(chosenItem).equals("Spouse"))
            {
                add.setDisable(true);
            }
            else
            {
                add.setDisable(false);
            }
        }
    }    
    
    /**
     * Creates structure for view member scene of the form
     * @return 
     */
    private HBox ViewSceneMaker()
    {
        HBox components = new HBox(20);
        Button add = new Button("Add Relative");
        add.setStyle("-fx-base: orange;");
        Button close = new Button("Close");
        close.setStyle("-fx-base: orange;");
        Button edit = new Button("Edit");
        edit.setStyle("-fx-base: orange;");
        components.setPadding(new Insets(20, 0, 20, 0));
        
        components.getChildren().addAll(edit, add, close);
        components.managedProperty().bind(components.visibleProperty());
        add.setId("Add_Relative_ID");
        //calling event handlers
        add.setOnAction(e -> displayScene("ADD"));
        close.setOnAction(e -> displayScene("DEFAULT"));
        edit.setOnAction(e -> displayScene("EDIT"));

        return components;
    }
    
    
    private VBox makeTopPane() //buildNavigation()
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        VBox topPane = new VBox();
        HBox group = new HBox(8);
        Button saveTreeFileBtn = new Button("Save Tree");
        saveTreeFileBtn.setStyle("-fx-base: indigo;");
        Button loadTreeBtn = new Button("Load Tree");
        loadTreeBtn.setStyle("-fx-base: indigo;");
        
        text = new Text();
        text.setText("Welcome to the Family Tree Application!\n");
        text.setFont(Font.font ("Times New Roman", 20));
        text.setFill(Color.BLACK);
        
        loadTreeBtn.setOnAction(e -> loadTreeFileButton());
        saveTreeFileBtn.setOnAction(e -> saveTreeFileButton());

        group.getChildren().addAll(loadTreeBtn,saveTreeFileBtn);
        topPane.getChildren().addAll(text, group);

        return topPane;
    
    }
    
    /** 
     * Event handler for save button
     */
    private void saveButtonHandler()
    {
        if(form.isNotEmpty())
        {
            int req;
            
            if(currentScene.equals("ADD"))
            {
                req = familytree.addItem(form.getFormData(), familytree.getChosenItem());
            }
            else
            {
                familytree.refreshTree(form.getFormData(), familytree.getChosenItem());
                req = 0;
            }
            
            switch(req)
            {
                case 0:
                    if(currentScene.equals("ADD"))
                    {
                        displayScene("DEFAULT");
                    }
                    else
                    {
                        displayScene("VIEW");
                    }
                    break;
                    
                case 2: //spouse exists error
                    promptAlert(
                                Alert.AlertType.ERROR,
                                "Cannot add this family member!",
                                "Spouse already exists"
                              );
                    break;
                    
                case 4: //Invalid memeber
                    promptAlert(
                            Alert.AlertType.ERROR,
                            "Cannot add this family member!",
                            "The member does not exist"
                          );
                    
                case 3:  //root member is spouse
                    promptAlert(
                      Alert.AlertType.ERROR,
                      "Cannot add this family member!",
                      "The root member cannot be a spouse"
                    );
                    break;
                default: 
                    System.out.println("Unknown error");
            }  
        }
        
        else //if form has any blank input fields
        {
            promptAlert(
                Alert.AlertType.ERROR,
                    "Form could not be saved!",
                    String.join("\n", form.getErrorPrompts())
            );
        }
    }
    
    /**
     * Event handler for cancel button
     */
    private void cancelButtonHandler()
    {
        if(currentScene.equals("EDIT"))
        {
            form.setFormData(familytree.getInfo(familytree.getChosenItem()));
            displayScene("VIEW");
        }
        else
        {
            displayScene("DEFAULT");
        }
    }
    
    /**
     * Event handler when a member is clicked from the tree list on the left
     */
    private void memberSelectionHandler()
    {
        TreeItem<Object> clickedItem = familytree.getChosenItem();
        if(clickedItem != null)
        {
            if(clickedItem.getValue() instanceof Person)
            {
                form.setFormData(familytree.getInfo(clickedItem));
                displayScene("VIEW"); //show View scene if clickedItem is a member
            }
            else
                displayScene("DEFAULT");
        }
    }

  
    /**
     * Saving a new tree locally in a .txt file
     */
    private void saveTreeFileButton()
    {    
        String filePath = createFileDialog("Save Family Tree");
        System.out.println("Saving tree....");
        if(!filePath.isEmpty())
        {
            try (ObjectOutputStream ostream = new ObjectOutputStream(new FileOutputStream(filePath)))
            {                        
                ostream.writeObject(familytree.getTreeRoot());
                promptAlert(Alert.AlertType.INFORMATION, "File saved sucessfully!", "The family tree was saved successfully" );
            }

            catch (IOException ex)
            {   
                promptAlert(Alert.AlertType.ERROR, "File saving failed!", "Unable to write to the file");
            }

            /*catch (FileNotFoundException ex2)
            {   
                promptAlert(Alert.AlertType.ERROR, "File saving failed!", "File doesn't exist");
            }*/
        }

    }
  
    /**
     * Loading an already built tree file
     */
    private void loadTreeFileButton()
    {
        String filePath = createFileDialog("Load Family Tree");

        if(!familytree.isNull())
        {
            int c = promptAlert(Alert.AlertType.CONFIRMATION, "Please confirm!", "Existing family tree will be overwritten");

            if(c==1) //if Cancel
                return;
        }  

        if(!filePath.isEmpty()) //if File path if not empty
        {
            try(ObjectInputStream istream = new ObjectInputStream(new FileInputStream(filePath)))
            {
                familytree.clearTree();
                familytree.addNewMember((Person) istream.readObject(), null);
                displayScene("DEFAULT");

            }
            catch(FileNotFoundException e)
            {
                promptAlert(Alert.AlertType.ERROR, "Unable to load file!", "The file is invalid");
            }

            catch(IOException e)
            {
                promptAlert(Alert.AlertType.ERROR, "Unable to load file!", "The selected file could not be loaded");          
            }

            catch(ClassNotFoundException e)
            {
                promptAlert(Alert.AlertType.ERROR, "Unable to load file!", e.toString());
            }
        }


    }
    
    /**
     * Prompts any alert messages
     * @param type
     * @param topic
     * @param message
     * @return 
     */
    private int promptAlert(Alert.AlertType type, String topic, String message) 
    {
        Alert alert = new Alert(type);
        alert.setHeaderText(topic);
        alert.setContentText(message);

        if (type.equals(Alert.AlertType.CONFIRMATION)) 
        {
            Optional<ButtonType> option = alert.showAndWait();
            
            if(option.get() == ButtonType.OK) 
            {
                return 0;
            } 
            else 
            {
                return 1;
            }
        } 
        else 
        {
            alert.show();
        }

        return 0;
    }
    
     
    /**
     * Presents a file chooser for loading or saving a family tree
     * @param req load/save request
     * @return file path
     */
    private String createFileDialog(String req) 
    {
        File file;
        FileChooser fileBox = new FileChooser();
        fileBox.setTitle(req);
        fileBox.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("Files", "*.txt")); //.txt file type
        
        file = req.equals("Save Family Tree")? fileBox.showSaveDialog(border_pane.getScene().getWindow())
      : fileBox.showOpenDialog(border_pane.getScene().getWindow());
        
        if(file!=null)
        {
            return file.getAbsoluteFile().toString();
        }
        else
        {
            return null;
        }
    }
    
}
