//package hl7.testing;

public class Relatives 
{
    private int ID;
    private String rCodeToPerson;
    
    //default constructor
    public Relatives() 
    {
        ID = -1;
        rCodeToPerson = "";
    }

    public Relatives(int newID, String newRCodeToPerson) 
    {
        ID = newID;
        rCodeToPerson = newRCodeToPerson;
    }

    public int getID() 
    {
        return ID;
    }

    public String getRCodeToPerson() 
    {
        return rCodeToPerson;
    }

    public void setID(int ID) 
    {
        this.ID = ID;
    }

    public void setRCode(String rCodeToPerson) 
    {
        this.rCodeToPerson = rCodeToPerson;
    }
}
