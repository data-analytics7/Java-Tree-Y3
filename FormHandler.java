
package familytree;
import java.util.ArrayList;
import java.util.TreeMap;
import javafx.scene.Node;
//import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Faiz Syed
 * Student ID: 33243485
 */
public class FormHandler 
{
    private ArrayList<String> formPrompts;
    private GridPane grid;

    //creating file names
    public static final String fName = "First name", lName = "Last name", mName = "Maiden name",description = "Description",
    gender = "Gender", streetName = "Street name",streetNumber = "Street number", suburb= "Suburb",
    zip = "Zip code", memberType = "Member Type";
    private TreeMap<String, TextField> inputField;
    private VBox Components; //form components
    
    //creating an array out of the field labels
    public static final String[] InputFields = {fName,lName,mName,description, streetName,
        streetNumber,suburb,zip, gender, memberType};

    public static final String[] Genders = {"M", "F"};
    private HBox radioButtonGender;
    private HBox radioButtonMember;

    public static final String[] MemberTypes= {"Child", "Spouse"};


    /**
     * Constructor.
     */
    public FormHandler() 
    {
        inputField = new TreeMap<String, TextField>();
        Components = buildFormItems();
    }

  
    /**
     * Presents a fresh form with cleared data
     */
    public void showEmptyForm() 
    {   
        disableForm(false);
        resetForm();
        showForm();  
    }
  
    /**
     * Returns a VBox containing the form items
     */
    private VBox buildFormItems() 
    {
        VBox vbox = new VBox();
        grid = createForm();
        vbox.getChildren().add(grid);
        return vbox;
    }

    /**
     * Presents the form in an editable format
     */
    public void showEditableForm() 
    {
        disableForm(false);
        disableRadio(radioButtonMember, true); //the member type radio button will be kept disabled
        showForm();
    }
  
    /**
    * Creates the form and its items in a grid
    * @return Returns a GridPane containing the required form
    */
    private GridPane createForm() 
    {
        GridPane formGrid = new GridPane();
        int rowBuffer = 0;

        formGrid.setHgap(16); //setting the dimensions of the form grid 
        formGrid.setVgap(8);

        for (String field_label: InputFields) 
        {
            Label fieldLabel = new Label(field_label);
            fieldLabel.setStyle("-fx-font-size: 11pt");

            fieldLabel.setPrefWidth(130);
            formGrid.add(fieldLabel, 0, rowBuffer);

            if(field_label.equals(gender)) //for gender radio button
            {
                radioButtonGender = makeRadioButtons(Genders);
                formGrid.add(radioButtonGender, 1, rowBuffer);
            } 
            else 
                if (field_label.equals(memberType)) //for memberType radio button
                {
                    radioButtonMember = makeRadioButtons(MemberTypes);
                    formGrid.add(radioButtonMember, 1, rowBuffer);
                } 
                else //for all other text boxes
                {
                    TextField inputfield = new TextField();
                    inputfield.setStyle("-fx-background-color: #FFFFFF");
                    inputfield.setPrefWidth(256);
                    inputfield.setId(field_label);
                    formGrid.add(inputfield, 1, rowBuffer);
                    inputField.put(field_label, inputfield);
                }
            rowBuffer++;
        }

        return formGrid; //returns the complete form
    }

    /**
     * Presents the form in read only format
     * 
     */
    public void showViewableForm() 
    {
        disableForm(true);
        showForm();
    }

    /**
     * Method to build radio buttons
     * @param An array containing the list of radio buttons
     * @return HBox containing the required radio buttons
     */
    private HBox makeRadioButtons(String[] radioButtons) 
    {
        int counter = 0;
        HBox RadioButtonBox = new HBox(20);
        ToggleGroup radioOption = new ToggleGroup(); //to allow just a single toggle to be selected
        for (String i:radioButtons) 
        {
            RadioButton radioButton = new RadioButton(i);
            if (counter==0) 
            {
              radioButton.setSelected(true);
            }
            radioButton.setToggleGroup(radioOption);
            RadioButtonBox.getChildren().add(radioButton);
            counter++;
        }

        return RadioButtonBox;
    }

    /**
     * Pre-fills the form data
     * @param formData HashMap of form data
     */
    public void setFormData(TreeMap<String, String> formData) 
    {
        setRadioSelected(radioButtonGender, formData.get(gender));
        setRadioSelected(radioButtonMember, formData.get(memberType));

        formData.forEach((key, value) -> 
        {
            TextField field = inputField.get(key);
            if (field != null) 
            {
                field.setText(value);
            }
        });
    }

    /**
     * Disables the text fields and radio buttons inside the form
     * @param toDisable Text fields and radio buttons will be disabled if the boolean is true
     */
    private void disableForm(boolean toDisable) 
    {  
        disableRadio(radioButtonGender, toDisable);
        disableRadio(radioButtonMember, toDisable);
        inputField.forEach((key, textField) -> 
        {
            textField.setDisable(toDisable);
        });
    }

    /**
     * To disable the radio buttons present in the form
     * @param radioButtons contains the radio button
     * @param toDisable Boolean value indicating whether to disable the buttons.
     */
    private void disableRadio(HBox radioButtons, boolean toDisable) 
    {
        for (Node child : radioButtons.getChildren()) 
        {
            RadioButton radioButton = (RadioButton) child;
            radioButton.setDisable(toDisable);
        }
    }


    /**
     * Returns all form data.
     * @return HashMap containing string data.
     */
    public TreeMap<String, String> getFormData() 
    {
        TreeMap<String, String> data = new TreeMap<String, String>();
        data.put(gender, getRadioSelection(radioButtonGender));
        data.put(memberType, getRadioSelection(radioButtonMember));
        inputField.forEach((key, field) -> {
          data.put(key, field.getText().trim());
        });
        return data;
    }
  
    /**
    * For setting the radio button to selected 
    * @param radios contains the radio buttons
    * @param match string to match the button selection with
    */
    private void setRadioSelected(HBox radios, String match) 
    {
        for(Node child : radios.getChildren()) 
        {
            RadioButton radioButton = (RadioButton) child;
            if (radioButton.getText().equals(match)) 
            {
                radioButton.setSelected(true);
            }
        }
    }

    /**
     * Returns form node
     * @return
     */
    public VBox getRoot() 
    {
        return Components; 
    }

    /**
     * Returns radio button selection value
     * @param radioButton
     * @return
     */
    private String getRadioSelection(HBox radioButton) 
    {
        for (Node node : radioButton.getChildren()) 
        {
            RadioButton radio = (RadioButton) node;

            if (radio.isSelected()) 
            {
                return radio.getText();
            }
        }

        return "";
    }

    /**
     * Checks if the text field is empty
     * @param field the text field date
     * @return Returns true if the text field is empty
     */
    private boolean isfieldEmpty(String field) 
    {
        if (field.trim().length() > 0) 
        {
            return false;
        } 
        else 
        {
            return true;
        }
    }

    /**
     * To make sure the form has no empty fields
     * @return returns true if text field(s) are empty
     */
    public boolean isNotEmpty() 
    {
        formPrompts = new ArrayList<String>();

        inputField.forEach((key, value) -> 
        {  
            if (isfieldEmpty(value.getText())) 
            {
                formPrompts.add(String.format("\"%s\" cannot be submitted blank", value.getId()));
            }
        });

        return formPrompts.isEmpty();
    }
  
  
    /**
     * Get error messages generated from validation.
     * @return
     */
    public ArrayList<String> getErrorPrompts() 
    {
        return formPrompts;
    }

    /**
     * Make form visible.
     */
    public void showForm() 
    {
        Components.setVisible(true);
    }

      /**
     * Resets the form
     */
    private void resetForm() 
    {
        inputField.forEach((key, inputField) -> {inputField.clear();});

        RadioButton gender = (RadioButton) radioButtonGender.getChildren().get(0);
        RadioButton memberType   = (RadioButton) radioButtonMember.getChildren().get(0);

        gender.setSelected(true);
        memberType.setSelected(true);
    }

    /**
     * To hide the form
     */
    public void hideForm() 
    {
        resetForm();
        Components.setVisible(false);
    }
    
}
