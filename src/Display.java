
import java.util.ArrayList;

public class Display {

    public Display()
    {

    }

    public ArrayList<String> displayFamilyTree(ArrayList<Person> arrayList) {
        
        ArrayList<String> stringArray = new ArrayList<String>();

        for (Person person : arrayList) {
            int id = person.getID();
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String relationCode = person.getRCode();

            if (relationCode.equals("NotAvailable"))
            {
                stringArray.add(id + " | " + firstName + " " + lastName + " | No Relationship Code \n");
            }
            else
            {
                stringArray.add(id + " | " + firstName + " " + lastName + " | " + relationCode + "\n");
            }

          /*System.out.println("ID: " + id);
            System.out.print(" | First Name: " + firstName);
            System.out.print(" | Last Name: " + lastName);
            System.out.print(" | rCode: " + relationCode);*/
        }

        return stringArray;
    }

    public void printFamilyTreeToConsole(ArrayList<Person> arrayList) {

        for (Person person : arrayList) {
            int id = person.getID();
            String firstName = person.getFirstName();
            String lastName = person.getLastName();
            String relationCode = person.getRCode();
            String birthTime = "N/A";
            String gender = person.getGender();
            String deceased = person.getDeceased();
            if (person.getID() == 1)
            {
                birthTime = person.getBirthTime();
            }

            System.out.print("ID: " + id);
            System.out.print(" | First Name: " + firstName);
            System.out.print(" | Last Name: " + lastName);
            System.out.print(" | rCode: " + relationCode);
            System.out.print(" | Birth Time: " + birthTime);
            System.out.print(" | Gender: " + gender);
            System.out.print(" | Deceased?: " + deceased);
            System.out.println();
            for (int i = 0; i < person.getRelativeArray().size(); i++)
            {
                System.out.println("   Relative: " + person.getRelativeArray().get(i).getID() + " " +
                person.getRelativeArray().get(i).getRCodeToPerson());
            }
        }
    }
}
    
