package familytree;
import java.util.ArrayList;
import java.util.TreeMap; //using TreeMap
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author Faiz Syed 
 * Student ID: 33243485
 */
public class TreeHandler 
{
    private TreeView<Object> mytree;
    
    /**
     * Constructor
     */
    public TreeHandler()
    {
        mytree = new TreeView<Object>(); //creating a new tree
    }
    
    /**
     * To Add a new member
     * @param memberInfo
     * @param itemSelected
     * @return 
     */
    public int addItem(TreeMap<String, String> memberInfo, TreeItem<Object> itemSelected )
    {
        String memberType = memberInfo.get("Member Type");
        Children chosenItem = (itemSelected == null ? null : (Children) itemSelected.getValue()); //get object of the selectedItem value
        
        if(memberType.equals("Child"))
        {
            if(chosenItem == null) //no parent selected to add this child
            {
                if(mytree.getRoot() == null) //if there is no root member
                    addAsRoot(memberInfo);
                else
                    return 1; //need parent
            }  
            else //if parent member is specified ,ie. chosenItem is not null           
                addAsChild(memberInfo, itemSelected, chosenItem);    
        }     
       else //memberType is Spouse
           if(memberType.equals("Spouse"))
           {
               if(chosenItem == null)
                    return 3; //there has to be a member for assigning spouse
               else //if a chosenMember for this spouse exists then
                {
                    if(chosenItem.hasSpouse())
                        return 2; //if spouse already exists
                    else              
                        addAsSpouse(memberInfo, itemSelected, chosenItem);                                     
                }               
           }
        return 0;       
    }
 
    /**
     * To load family tree from a tree
     * @param newItem
     * @param parent 
     */
    public void addNewMember(Person newItem, TreeItem<Object> parent)
    {
        TreeItem<Object> newTreeItem = new TreeItem<Object>(newItem);
        newTreeItem.setExpanded(true);
        
        if(parent == null) //if no parent
            mytree.setRoot(newTreeItem);
        else //if parent exists
            assignParent(newTreeItem, parent, newItem.getMemberType()); //either as child or spouse
        
        //Checking for relatives
        if(newItem.getMemberType().equals("Child")) //if the member type is child
            loadRelatives(newItem, newTreeItem);
    }
    
    /**
     * To assign a parent
     * @param newTreeItem
     * @param parent
     * @param memberType 
     */
    private void assignParent(TreeItem<Object> newTreeItem, TreeItem<Object> parent, String memberType)
    {

        String itemType = memberType.equals("Spouse") ? "Spouse" : "Children"; //get member type
        TreeItem<Object> theItem = getItem(itemType, parent); //gets the item from the existing items

        if (theItem == null) 
        {
            theItem = new TreeItem<Object>(itemType); //sets the label Spouse/Children
            theItem.getChildren().add(newTreeItem); //creating a new theItem and adding newTreeItem to it
            theItem.setExpanded(true);
            if (itemType.equals("Spouse")) //if item is a spouse
                parent.getChildren().add(0, theItem);//spouse will always be added above children in the tree
            else //if item is child
                parent.getChildren().add(theItem); //append children to parent
        } 
        else 
            theItem.getChildren().add(newTreeItem);  
       
    }
    
        /**
     * Adds as root to the class and to the tree
     * @param memberInfo 
     */
    private void addAsRoot(TreeMap<String, String> memberInfo)
    {
        Person newItem;
        newItem = new Children();
        newItem.setInfo(memberInfo);
        addNewMember(newItem, null); //making this member the root
    }
    
    
    /**
     * Adds as child to the class and to the tree
     * @param memberInfo
     * @param itemSelected
     * @param chosenItem 
     */
    private void addAsChild(TreeMap<String, String> memberInfo, TreeItem<Object> itemSelected, Children chosenItem)
    {
        Person newItem;
        newItem = new Children();
        newItem.setInfo(memberInfo);
        addNewMember(newItem, itemSelected); //adds child to parent
        chosenItem.addChild((Children)newItem); 
    }
    
    /**
     * Adds as spouse info to class and to the tree
     * @param memberInfo
     * @param itemSelected
     * @param chosenItem 
     */
    private void addAsSpouse(TreeMap<String, String> memberInfo, TreeItem<Object> itemSelected, Children chosenItem)
    {
        Person newItem;
        newItem = new Spouse();
        newItem.setInfo(memberInfo);
        newItem.setSpouse((Person) chosenItem); //setting as spouse for one another
        chosenItem.setSpouse((Person) newItem);
        addNewMember(newItem, itemSelected);      
    }
    
    /**
     * Search tree for an item
     * @param label
     * @param newTreeItem
     * @return TreeItem object
     */
    private TreeItem<Object> getItem(String label, TreeItem<Object> newTreeItem)
    {
        for(TreeItem<Object> i : newTreeItem.getChildren())
        {
            if(i.getValue().toString().equals(label))
            {
                return (TreeItem<Object>) i;
            }
        }
        return null;
    }
    
     /**
     * To load relatives of a child item when saved file is loaded
     * @param newItem
     * @param newTreeItem 
     */
    private void loadRelatives(Person newItem, TreeItem<Object> newTreeItem)
    {
        ArrayList<Person> relatives = new ArrayList<>();
        relatives = ((Children)newItem).getPersonRelatives(); //look for relatives

        if(relatives != null && relatives.size() > 0) //if relatives exist
        {
            for(int i = 0; i<relatives.size(); i++)
            {
                if(i==0)
                {
                    //add spouse
                    if(relatives.get(i) != null)
                    {
                        TreeItem<Object> newRelative = new TreeItem<Object>(relatives.get(0));
                        assignParent(newRelative, newTreeItem, relatives.get(0).getMemberType());
                    }
                }
                else
                {
                    addNewMember(relatives.get(i), newTreeItem);
                }
            }
        }
        
    }
    
    /**
     * Get member info
     * @param item
     * @return 
     */
    public TreeMap<String, String> getInfo(TreeItem<Object> item) 
    {
        if (item != null) 
            return ((Person) item.getValue()).getInfo();
        else //if the item is null
            return new TreeMap<String, String>();
    }
    
    
    /**
    * Returns instance of selected item in tree.
    */
    public TreeItem<Object> getChosenItem() 
    {
        return (TreeItem<Object>) mytree.getSelectionModel().getSelectedItem();
    }
    
    /**
     * To get the member type
     * @param item
     * @return 
     */
    public String getMemberType(TreeItem<Object> item) 
    {
        if (item != null) 
          return ((Person) item.getValue()).getMemberType();
        else 
          return null;
    }

    /**
     * Get tree node
     * @return tree node
     */
    public TreeView<Object> getTree() 
    {
        return mytree;
    }
  
   
   /**
    * To refresh the tree after member info is updated
    * @param treeInfo
    * @param treeItem 
    */
   public void refreshTree(TreeMap<String, String> treeInfo, TreeItem<Object> treeItem)    
   {  
       if(treeItem != null)
       {
            Person member = (Person)treeItem.getValue();
            member.setInfo(treeInfo);
            mytree.refresh();
       }
   }
   
    /**
    * Returns boolean indicating whether the tree has a root item.
    */
    public boolean isNull() 
    {
        return mytree.getRoot() == null;
    }
  
    /**
     * To clear the whole tree
     */
    public void clearTree() 
    {
        if (mytree.getRoot() != null) 
            mytree.getRoot().getChildren().clear();
        mytree.setRoot(null);
    }
    
    /**
    * Get root of a given family member
    */
    public Children getTreeRoot() //get root of a given family member
    {
        if (mytree.getRoot() == null) 
            return null;
        else 
            return (Children) mytree.getRoot().getValue();
    }

    
} //END FamilyTree.java
