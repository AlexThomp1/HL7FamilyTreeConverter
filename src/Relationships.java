
import java.util.ArrayList;

public class Relationships {

    public Relationships()
    {

    }

    public String relationshipOfPat(Person person, Person patient)
    {
        String relationship = "NotAvailable";

        if (person.getRCode().equals("NMTH") || person.getRCode().equals("NFTH"))
        {
            if (patient.getGender().equals("M"))
            {
                return "SON";
            }
            else
            {
                return "DAU";
            }
        }
        else if (person.getRCode().equals("MGRMTH") || person.getRCode().equals("MGRFTH")
        || person.getRCode().equals("PGRMTH") || person.getRCode().equals("PGRFTH"))
        {
            if (patient.getGender().equals("M"))
            {
                return "GRNDSON";
            }
            else
            {
                return "GRNDDAU";
            }
        }
        else if (person.getRCode().equals("NSIS") || person.getRCode().equals("NBRO"))
        {
            if (patient.getGender().equals("M"))
            {
                return "NBRO";
            }
            else
            {
                return "NSIS";
            }
        }

        return relationship;
    }
}
    
