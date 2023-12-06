import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Person 
{
    // All people have these
    private int ID;
    private String firstName;
    private String lastName;
    private String rCode;
    private String gender;
    private String deceased;
    private String subjectOf1;

    // Some people have these
    // Only for main person, all others "N/A"
    private String birthTime; // YEARMONTHDAY (20030222) = (Feb 22, 2003)

    // For everyone BUT main person
    private ArrayList<Relatives> relatives;
    private ArrayList<String> subjectOf2;
    
    //default constructor
    public Person() 
    {
        ID = -1;
        firstName = "";
        lastName = "";
        rCode = "";
        gender = "";
        deceased = "";
        birthTime = "";
        relatives = new ArrayList<Relatives>();
        subjectOf1 = "";
        subjectOf2 = new ArrayList<String>();
    }
    
    //paramaterized constructor
    public Person(int newID, String newFirstName, 
            String newLastName, String newRCode, String newGender,
            String newDeceased, String newBirthTime, ArrayList<String> newSubjectOf2) 
    {
        ID = newID;
        firstName = newFirstName;
        lastName = newLastName;
        rCode = newRCode;
        gender = newGender;
        deceased = newDeceased;
        birthTime = newBirthTime;
        relatives = new ArrayList<Relatives>();
        subjectOf1 = "";
        subjectOf2 = newSubjectOf2;
    }

    public int getID() 
    {
        return ID;
    }

    public String getFirstName() 
    {
        return firstName;
    }

    public String getLastName() 
    {
        return lastName;
    }

    public String getRCode() 
    {
        return rCode;
    }

    public String getGender() 
    {
        return gender;
    }

    public String getDeceased() 
    {
        return deceased;
    }

    public String getBirthTime()
    {
        return birthTime;
    }

    public ArrayList<Relatives> getRelativeArray() 
    {
        return relatives;
    }

    public String getSubjectOf1() 
    {
        return subjectOf1;
    }

    public ArrayList<String> getSubjectOf2() 
    {
        return subjectOf2;
    }

    public void setID(int ID) 
    {
        this.ID = ID;
    }

    public void setFirstName(String firstName) 
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) 
    {
        this.lastName = lastName;
    }

    public void setRCode(String rCode) 
    {
        this.rCode = rCode;
    }

    public void setGender(String gender) 
    {
        this.gender = gender;
    }

    public void setDeceased(String deceased) 
    {
        this.deceased = deceased;
    }

    public void setBirthTime(String birthTime) 
    {
        this.birthTime = birthTime;
    }

    public void setRelativeArray(ArrayList<Relatives> relatives) 
    {
        this.relatives = relatives;
    }

    public void setSubjectOf1(String subjectOf1) 
    {
        this.subjectOf1 = subjectOf1;
    }

    public void setSubjectOf2(ArrayList<String> subjectOf2)
    {
        this.subjectOf2 = subjectOf2;
    }

    public String configureBirthTime(String date)
    {
        String fixedDate = "";
        try
        {
            SimpleDateFormat dateFrom = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dateTo = new SimpleDateFormat("yyyyMMdd");  
            Date newDate = dateFrom.parse(date);
            fixedDate = dateTo.format(newDate);
        }
        catch (Exception e) 
        {
            //e.printStackTrace();
            return "BADFORMAT";
        }

        return fixedDate;
    }
}
