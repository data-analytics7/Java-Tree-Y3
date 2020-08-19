
package familytree;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Class containing setters and getters for a person - also the super class to Spouse.java and Children.java
 * Will store person info in a TreeMap
 * @author Faiz Syed
 * Student ID: 33243485
 */
abstract class Person implements Serializable
{
    //A TreeMap to store person info in key value pairs
    private TreeMap<String, String> thePerson;
    private Person thespouse;
    //basic variables to store values in
    public static final String fName = "Fname", lName = "Lname", mName = "Mname", 
            description = "description", gender = "gender", streetName = "street name", suburb = "suburb",
            zip = "zip", memberType = "memberType";
     //array of input field values
    public static final String[] fields = {fName, lName, mName, description, gender, 
        streetName, suburb, zip, memberType};
    
   
    /**
     * Constructor
     */
    public Person()
    {
        thePerson = new TreeMap<String, String>();
        for(String theField : fields)
            thePerson.put(theField, ""); //initialising TreeMap to store values later
    }
   
    /**
     * Set information of the person
     * @param info 
     */
    public void setInfo(TreeMap<String, String> info)
    {   //setting values into person TreeMap
        info.forEach((key, value) -> {this.thePerson.put(key, value);});  //deep copy
    }
    
    /**
     * To get person info
     * @return thePerson
     */
    public TreeMap<String, String> getInfo()
    {
        return thePerson;
    }
    
    /**
     * Set person's spouse information
     * @param newmember 
     */
    public void setSpouse(Person newmember)
    {
        thespouse = newmember;
    }
    
    /**
     * To get spouse info
     * @return thespouse
     */
    public Person getSpouse()
    {
        return thespouse;
    }
    
    /**
     * Set type of member
     * @param theType 
     */
    public void setMemberType(String theType)
    {
        thePerson.put(memberType, theType);
    }

    /**
     * To get member type
     * @return memberType
     */
    public String getMemberType()
    {
        return thePerson.get(memberType);
    }
    
    /**
     * To check whether spouse exists
     * @return flag
     */
    public boolean hasSpouse() 
    {
        boolean flag;
        flag = thespouse !=null;    
        return flag;
    }
    
    /**
     * This is an overridden method to display First and Last name of the member on the Tree
     * @return String
     */
    public String toString() 
    {
        return String.format("%s %s", thePerson.get("First name"), thePerson.get("Last name"));
    }
    
}
