/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package familytree;
import java.util.ArrayList;

/**
 * A sub class of Person.java
 * @author Faiz
 */
public class Children extends Person
{
    private ArrayList<Children> childrenList;
   
    /**
     * Constructor
     */
    public Children()
    {
        childrenList = new ArrayList<Children>();
        setMemberType("Child");
    }
        
    /**
     * Returning an array containing spouse and children objects of the given member
     * @return 
     */
    public ArrayList<Person> getPersonRelatives()
    {
        ArrayList<Person> relativesList = new ArrayList<>();      
        if(getSpouse() == null && childrenList.size() == 0) //if no spouse or children return empty array
            return new ArrayList<>();
        else //if spouse or children exist
        {
            relativesList.add(getSpouse());
            for(int i=0; i<childrenList.size(); i++)
                relativesList.add(childrenList.get(i));
            return relativesList;
        }
    }
    
    /**
     * To add the person as a child type
     * @param member instance
     */
    public void addChild(Children member)
    {
        childrenList.add(member);
    }
    
}

