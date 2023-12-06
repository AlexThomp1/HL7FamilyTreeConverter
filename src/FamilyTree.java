/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
//package hl7.testing;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.InputSource;
  
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
  
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class FamilyTree
{

    private ArrayList<Person> mainPersonArray;
    private ArrayList<Person> backupPersonArray;
    private ArrayList<String> subjectOf2MainArray;
    private int originalMainPersonID;

    // Creation Date of File
    String creationTime = "";

    /**
     * @param args the command line arguments
     */

    public FamilyTree()
    {
        subjectOf2MainArray = new ArrayList<String>();
        originalMainPersonID = -1;
    }
    
    //public static void main(String[] args)
    public int parseFile(String fileName)
    {
        // Variables
        ArrayList<Person> personArray = new ArrayList<Person>();
        ArrayList<Relatives> relativeArray;
        ArrayList<String> subjectOf1Array = new ArrayList<String>();
        ArrayList<String> subjectOf2Array = new ArrayList<String>();

        try 
        {
            // Parse the XML content
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            document.getDocumentElement().normalize();

            // Create XPath
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // Checks to see if the file is the correct format before parsing
            if (evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/id/@extension").equals(""))
            {
                //System.out.println("CANNOT PARSE FILE DUE TO NO INFORMATION GATHERED FROM FILE!");
                return 1;
            }

            // Extract information using XPath expressions
            // The path is like folders for XML, and the @NAME is if there is a value in the tag
            creationTime = evaluateXPath(xpath, document, "/FamilyHistory/effectiveTime/@value");
            String personID = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/id/@extension");
            String personRCode = "PAT";
            String personFirstName = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/name/given");
            String personLastName = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/name/family");
            String personGender = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/administrativeGenderCode/@code");
            String personBirthTime = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/birthTime/@value");
            String personDeceased = evaluateXPath(xpath, document, "/FamilyHistory/subject/patient/patientPerson/deceasedInd/@value");

            originalMainPersonID = Integer.parseInt(personID);

            // Parse all SubjectOf2 for Main Person
            NodeList subjectOf2List = (NodeList) xpath.evaluate("FamilyHistory/subject/patient/subjectOf2", document, XPathConstants.NODESET);
            for (int j = 0; j < subjectOf2List.getLength(); j++) {
                Element subjectOf2 = (Element) subjectOf2List.item(j);
                String subjectOf2Xml = elementToStringForSubjectOfTag(subjectOf2);
                //System.out.println("SubjectOf2 XML: " + subjectOf2Xml);
                subjectOf2Array.add(subjectOf2Xml);
            }

            subjectOf2MainArray = subjectOf2Array;

            // Parse SubjectOf1 for Main Person
            NodeList subjectOf1List = (NodeList) xpath.evaluate("FamilyHistory/subject/patient/subjectOf1", document, XPathConstants.NODESET);
            for (int j = 0; j < subjectOf1List.getLength(); j++) {
                Element subjectOf1 = (Element) subjectOf1List.item(j);
                String subjectOf1Xml = elementToStringForSubjectOfTag(subjectOf1);
                //System.out.println("SubjectOf1 XML: " + subjectOf1Xml);
                subjectOf1Array.add(subjectOf1Xml);
            }

            // Output for testing for main person
            //System.out.println("ID: " + personID);
            //System.out.println("Relationship Code: " + personRCode);
            //System.out.println("First Name: " + personFirstName);
            //System.out.println("Last Name: " + personLastName);
            //System.out.println("Gender: " + personGender);

            // Add main person to PersonArray
            personArray.add(new Person(Integer.parseInt(personID), personFirstName, personLastName, personRCode, personGender, personDeceased, personBirthTime, subjectOf2Array));

            // Parses the SubjectOf1 for each Relative
            NodeList peopleForSubjectOf1 = (NodeList) xpath.evaluate("//patientPerson", document, XPathConstants.NODESET);
            for (int i = 0; i < peopleForSubjectOf1.getLength(); i++)
            {
                Element person = (Element) peopleForSubjectOf1.item(i);
                
                subjectOf1List = (NodeList) xpath.evaluate("relative/subjectOf1", person, XPathConstants.NODESET);
                for (int j = 0; j < subjectOf1List.getLength(); j++) {
                    Element subjectOf1 = (Element) subjectOf1List.item(j);
                    String subjectOf1Xml = elementToStringForSubjectOfTag(subjectOf1);
                    //System.out.println("SubjectOf1 XML: " + subjectOf1Xml);
                    subjectOf1Array.add(subjectOf1Xml);
                }
            }

            /*  // Parses the SubjectOf2 for each Relative
            NodeList peopleForSubjectOf2 = (NodeList) xpath.evaluate("//patientPerson", document, XPathConstants.NODESET);
            System.out.println(peopleForSubjectOf2.getLength());
            for (int i = 0; i < peopleForSubjectOf2.getLength(); i++)
            {
                Element person = (Element) peopleForSubjectOf2.item(i);
                
                subjectOf2List = (NodeList) xpath.evaluate("relative/subjectOf2", person, XPathConstants.NODESET);
                System.out.println(subjectOf2List.getLength());
                for (int j = 0; j < subjectOf2List.getLength(); j++) {
                    Element subjectOf2 = (Element) subjectOf2List.item(j);
                    String subjectOf2Xml = elementToStringForSubjectOfTag(subjectOf2);
                    //System.out.println("SubjectOf1 XML: " + subjectOf1Xml);
                    subjectOf2ArrayRelative.add(subjectOf2Xml);
                }
            }*/


            // Extract relatives information
            XPathExpression relativesExpression = xpath.compile("/FamilyHistory/subject/patient/patientPerson/relative");
            NodeList relatives = (NodeList) relativesExpression.evaluate(document, XPathConstants.NODESET);

            // Rel array of parents for main person
            ArrayList<Relatives> relArray = new ArrayList<Relatives>();

            for (int i = 0; i < relatives.getLength(); i++) 
            {
                String relativeId = evaluateXPath(xpath, relatives.item(i), "relationshipHolder/id/@extension");
                String relativeRCode = evaluateXPath(xpath, relatives.item(i), "code/@code");
                String relativeFirstName = evaluateXPath(xpath, relatives.item(i), "relationshipHolder/name/given");
                String relativeLastName = evaluateXPath(xpath, relatives.item(i), "relationshipHolder/name/family");
                String relativeGender = evaluateXPath(xpath, relatives.item(i), "relationshipHolder/administrativeGenderCode/@code");
                String relativeDeceased = evaluateXPath(xpath, relatives.item(i), "relationshipHolder/deceasedInd/@value");
                ArrayList<String> subjectOf2ArrayRelative = new ArrayList<String>();

                NodeList relativeSubjectOf2 = (NodeList) xpath.evaluate("subjectOf2", relatives.item(i), XPathConstants.NODESET);
                for (int j = 0; j < relativeSubjectOf2.getLength(); j++)
                {
                    Element subOf2Item = (Element) relativeSubjectOf2.item(j);

                    String subjectOf2Xml = elementToStringForSubjectOfTag(subOf2Item);
                    //System.out.println("SubjectOf1 XML: " + subjectOf1Xml);
                    subjectOf2ArrayRelative.add(subjectOf2Xml);
                }

                //System.out.println(subjectOf2ArrayRelative.size());

                // Add parents of main person to a relative array
                if (relativeId.equals("2"))
                {
                    Relatives mother = new Relatives(2, "NMTH");
                    relArray.add(mother);
                }
                else if (relativeId.equals("3"))
                {
                    Relatives father = new Relatives(3, "NFTH");
                    relArray.add(father);
                }

                //System.out.println("Relative ID: " + relativeId);
                //System.out.println("Relative Relationship Code: " + relativeRCode);
                //System.out.println("Relative First Name: " + relativeFirstName);
                //System.out.println("Relative Last Name: " + relativeLastName);

                // Extract information about relatives of the current relative
                XPathExpression relativesOfRelativeExpression = xpath.compile("relationshipHolder/relative");
                NodeList relativesOfRelative = (NodeList) relativesOfRelativeExpression.evaluate(relatives.item(i), XPathConstants.NODESET);

                // Clear relative array for next one
                relativeArray = new ArrayList<Relatives>();

                for (int j = 0; j < relativesOfRelative.getLength(); j++) 
                {
                    String relativeOfRelativeId = evaluateXPath(xpath, relativesOfRelative.item(j), "relationshipHolder/id/@extension");
                    String relativeOfRelativeRCode = evaluateXPath(xpath, relativesOfRelative.item(j), "code/@code");

                    //System.out.println("  Relative of Relative ID: " + relativeOfRelativeId);
                    //System.out.println("  Relative of Relative Relationship Code: " + relativeOfRelativeRCode);

                    relativeArray.add(new Relatives(Integer.parseInt(relativeOfRelativeId), relativeOfRelativeRCode));
                }

                if (relativesOfRelative.getLength() == 0)
                {
                    //System.out.println(relativeId + " HAS NO RELATIVES!\n");
                }

                //System.out.println(); // Add a newline for better readability

                // Add relatives to PersonArray
                personArray.add(new Person(Integer.parseInt(relativeId), relativeFirstName, relativeLastName, relativeRCode, relativeGender, relativeDeceased, "", subjectOf2ArrayRelative));
                
                if (relativeArray.size() > 0)
                {
                    personArray.get(i+1).setRelativeArray(relativeArray);
                }
            }

            // Add subjectOf1 to each person
            for (int j = 0; j < subjectOf1Array.size(); j++)
            {
                personArray.get(j).setSubjectOf1(subjectOf1Array.get(j));
            }

            personArray.get(0).setRelativeArray(relArray);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        mainPersonArray = personArray;
        backupPersonArray = mainPersonArray;

        return 0;
    }

    public void reOrientPatient(Person person)
    {
        mainPersonArray = backupPersonArray;
        ArrayList<Person> newPersonList = new ArrayList<Person>();
        ArrayList<PairMaker> pastIDArray = new ArrayList<PairMaker>();
        Relationships rships = new Relationships();
        Person mother = new Person();
        Person father = new Person();

        // Add the first 1-7 in order of the new patient to the new array
        newPersonList.add(person); // New Patient
        PairMaker patientPair = new PairMaker(person.getID(), 1);
        pastIDArray.add(patientPair);

        // PARENTS
        // If the person has relatives atm, check them.
        //System.out.println("RELATIVE SIZE: " + person.getRelativeArray().size());
        if (person.getRelativeArray().size() > 0)
        {
            // Go through the relative array of the person to find mother (since needs to be 2nd)
            for (int i = 0; i < person.getRelativeArray().size(); i++)
            {
                // If the person has a NMTH tag, set as mother and add to list as 2nd.
                if (person.getRelativeArray().get(i).getRCodeToPerson().equals("NMTH"))
                {
                    mother = findPersonByID(person.getRelativeArray().get(i).getID());
                    newPersonList.add(mother);
                    PairMaker motherPair = new PairMaker(mother.getID(), 2);
                    pastIDArray.add(motherPair);
                }
            }

            // Go through the relative array of the person to find father (since needs to be 3rd)
            for (int i = 0; i < person.getRelativeArray().size(); i++)
            {
                // If the person has a NFTH tag, set as father and add to list as 3rd.
                if (person.getRelativeArray().get(i).getRCodeToPerson().equals("NFTH"))
                {
                    father = findPersonByID(person.getRelativeArray().get(i).getID());
                    newPersonList.add(father);
                    PairMaker fatherPair = new PairMaker(father.getID(), 3);
                    pastIDArray.add(fatherPair);
                }
            }
        }
        // If the person doesn't 
        else
        {
            ArrayList<Relatives> parents = new ArrayList<Relatives>();
            // Default Mother
            Person emptyPerson = new Person();
            emptyPerson.setID(1002); // Did 1002 b/c will override later
            emptyPerson.setRCode("NMTH");
            emptyPerson.setGender("F");
            emptyPerson.setFirstName("Mother");
            emptyPerson.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            mother = emptyPerson;
            newPersonList.add(mother);

            // Default Father
            Person emptyPerson2 = new Person();
            emptyPerson2.setID(1003); // Did 1003 b/c will override later
            emptyPerson2.setRCode("NFTH");
            emptyPerson2.setGender("M");
            emptyPerson2.setFirstName("Father");
            emptyPerson2.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            father = emptyPerson2;
            newPersonList.add(father);

            Relatives rMother = new Relatives(mother.getID(),"NMTH");
            parents.add(rMother);

            Relatives rFather = new Relatives(father.getID(), "NFTH");
            parents.add(rFather);

            // Set new relatives to person
            person.setRelativeArray(parents);
        }

        // MATERNAL GRANDPARENTS
        Person mGrandMother = new Person();
        Person mGrandFather = new Person();

        // If the person has relatives atm, check them.
        if (mother.getRelativeArray().size() > 0)
        {

            // Go through the relative array of the person to find mother (since needs to be 2nd)
            for (int i = 0; i < mother.getRelativeArray().size(); i++)
            {
                // If the person has a NMTH tag, set as mother and add to list as 2nd.
                if (mother.getRelativeArray().get(i).getRCodeToPerson().equals("NMTH"))
                {
                    mGrandMother = findPersonByID(mother.getRelativeArray().get(i).getID());
                    newPersonList.add(mGrandMother);
                    PairMaker mGrandMotherPair = new PairMaker(mGrandMother.getID(), 4);
                    pastIDArray.add(mGrandMotherPair);
                }
            }

            // Go through the relative array of the person to find father (since needs to be 3rd)
            for (int i = 0; i < mother.getRelativeArray().size(); i++)
            {
                // If the person has a NFTH tag, set as father and add to list as 3rd.
                if (mother.getRelativeArray().get(i).getRCodeToPerson().equals("NFTH"))
                {
                    mGrandFather = findPersonByID(mother.getRelativeArray().get(i).getID());
                    newPersonList.add(mGrandFather);
                    PairMaker mGrandFatherPair = new PairMaker(mGrandFather.getID(), 5);
                    pastIDArray.add(mGrandFatherPair);
                }
            }
        }
        // If the person doesn't 
        else
        {
            ArrayList<Relatives> parents = new ArrayList<Relatives>();
            // Default MMother
            Person emptyPerson1 = new Person();
            emptyPerson1.setID(1004); // Did 1004 b/c will override later
            emptyPerson1.setRCode("MGRMTH");
            emptyPerson1.setGender("M");
            emptyPerson1.setFirstName("MGrandMother");
            emptyPerson1.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            mGrandMother = emptyPerson1;
            newPersonList.add(mGrandMother);

            // Default MFather
            Person emptyPerson2 = new Person();
            emptyPerson2.setID(1005); // Did 1003 b/c will override later
            emptyPerson2.setRCode("MGRFTH");
            emptyPerson2.setGender("M");
            emptyPerson2.setFirstName("MGrandFather");
            emptyPerson2.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            mGrandFather = emptyPerson2;
            newPersonList.add(mGrandFather);

            Relatives rMother = new Relatives(mGrandMother.getID(), "NMTH");
            parents.add(rMother);

            Relatives rFather = new Relatives(mGrandFather.getID(), "NFTH");
            parents.add(rFather);

            // Set new relatives to person
            mother.setRelativeArray(parents);
        }

        // PATERNAL GRANDPARENTS
        Person pGrandMother = new Person();
        Person pGrandFather = new Person();

        // If the person has relatives atm, check them.
        if (father.getRelativeArray().size() > 0)
        {

            // Go through the relative array of the person to find mother (since needs to be 2nd)
            for (int i = 0; i < father.getRelativeArray().size(); i++)
            {
                // If the person has a NMTH tag, set as mother and add to list as 2nd.
                if (father.getRelativeArray().get(i).getRCodeToPerson().equals("NMTH"))
                {
                    pGrandMother = findPersonByID(father.getRelativeArray().get(i).getID());
                    newPersonList.add(pGrandMother);
                    PairMaker pGrandMotherPair = new PairMaker(pGrandMother.getID(), 6);
                    pastIDArray.add(pGrandMotherPair);
                }
            }

            // Go through the relative array of the person to find father (since needs to be 3rd)
            for (int i = 0; i < father.getRelativeArray().size(); i++)
            {
                // If the person has a NFTH tag, set as father and add to list as 3rd.
                if (father.getRelativeArray().get(i).getRCodeToPerson().equals("NFTH"))
                {
                    pGrandFather = findPersonByID(father.getRelativeArray().get(i).getID());
                    newPersonList.add(pGrandFather);
                    PairMaker pGrandFatherPair = new PairMaker(pGrandFather.getID(), 7);
                    pastIDArray.add(pGrandFatherPair);
                }
            }
        }
        // If the person doesn't 
        else
        {
            ArrayList<Relatives> parents = new ArrayList<Relatives>();
            // Default PMother
            Person emptyPerson1 = new Person();
            emptyPerson1.setID(1006); // Did 1006 b/c will override later
            emptyPerson1.setRCode("PGRMTH");
            emptyPerson1.setGender("F");
            emptyPerson1.setFirstName("PGrandMother");
            emptyPerson1.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            pGrandMother = emptyPerson1;
            newPersonList.add(pGrandMother);

            // Default PFather
            Person emptyPerson2 = new Person();
            emptyPerson2.setID(1007); // Did 1007 b/c will override later
            emptyPerson2.setRCode("PGRFTH");
            emptyPerson2.setGender("M");
            emptyPerson2.setFirstName("PGrandFather");
            emptyPerson2.setSubjectOf1("<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
            pGrandFather = emptyPerson2;
            newPersonList.add(pGrandFather);

            Relatives rMother = new Relatives(pGrandMother.getID(), "NMTH");
            parents.add(rMother);

            Relatives rFather = new Relatives(pGrandFather.getID(), "NFTH");
            parents.add(rFather);

            // Set new relatives to person
            father.setRelativeArray(parents);
        }

        // Once the 7 people are arranged, add the rest of the tree to the back of the array.
        for (int i = 0; i < mainPersonArray.size(); i++)
        {
            boolean dontAdd = false;

            for (int j = 0; j < pastIDArray.size(); j++)
            {
                if (mainPersonArray.get(i).getID() == pastIDArray.get(j).getItem1())
                {
                    dontAdd = true;
                }
            }

            if (!dontAdd)
            {
                newPersonList.add(mainPersonArray.get(i));
            }
        }

        // Add every persons' current ID to the pastID list
        pastIDArray.clear();

        for (int i = 0; i < newPersonList.size(); i++)
        {
            PairMaker newPair = new PairMaker();
            newPair.setItem1(newPersonList.get(i).getID());
            newPair.setItem2(i + 1);
            pastIDArray.add(newPair);

            //System.out.println("1: " + pastIDArray.get(i).getItem1() + " 2: " + pastIDArray.get(i).getItem2());
        }

        // Keep track of original person's new ID for exporting purposes of subjectOf2
        for (int i = 0; i < pastIDArray.size(); i++)
        {
            if (pastIDArray.get(i).getItem1() == 1)
            {
                originalMainPersonID = pastIDArray.get(i).getItem2();
            }
        }

        // Set class to new in-order IDs
        for (int i = 0; i < newPersonList.size(); i++)
        {
            newPersonList.get(i).setID(i + 1);
        }

        // Search through the new list, and find any RELATIVES w/ past IDs, and switch to new ones.
        for (int i = 0; i < newPersonList.size(); i++)
        {
            for (int j = 0; j < newPersonList.get(i).getRelativeArray().size(); j++)
            {
                for (int k = 0; k < pastIDArray.size(); k++)
                {
                    if (newPersonList.get(i).getRelativeArray().get(j).getID() == pastIDArray.get(k).getItem1())
                    {
                        //System.out.println(pastIDArray.get(k).getItem2());
                        newPersonList.get(i).getRelativeArray().get(j).setID(pastIDArray.get(k).getItem2());
                        break;
                    }
                }
            }
        }

        // Change everyone's relationship codes
        newPersonList.get(7).setRCode(rships.relationshipOfPat(newPersonList.get(0), newPersonList.get(7)));

        // Change the first 1-7 relationship codes
        newPersonList.get(0).setRCode("PAT");
        newPersonList.get(1).setRCode("NMTH");
        newPersonList.get(2).setRCode("NFTH");
        newPersonList.get(3).setRCode("MGRMTH");
        newPersonList.get(4).setRCode("MGRFTH");
        newPersonList.get(5).setRCode("PGRMTH");
        newPersonList.get(6).setRCode("PGRFTH");

        // For now, just set rest of RCodes to "NotAvailable" until solution later
        for (int i = 8; i < newPersonList.size(); i++)
        {
            newPersonList.get(i).setRCode("NotAvailable");
        }

        // Set the changed array to the new one
        mainPersonArray = newPersonList;

        // Display
        /*for (int i = 0; i < newPersonList.size(); i++)
        {
            System.out.println("Person: " + newPersonList.get(i).getID() + " " + newPersonList.get(i).getFirstName());
            
            for (int j = 0; j < newPersonList.get(i).getRelativeArray().size(); j++)
            {
                System.out.println("  Relative: " + newPersonList.get(i).getRelativeArray().get(j).getID() + " " + newPersonList.get(i).getRelativeArray().get(j).getRCodeToPerson());
            }
        }*/
    }

    // Pass in ID, returns Person w/ ID, if not found, empty person w/ ID = -1
    public Person findPersonByID(int ID)
    {
        Person foundPerson = new Person();

        for (int i = 0; i < mainPersonArray.size(); i++)
        {
            if (mainPersonArray.get(i).getID() == ID)
            {
                foundPerson = mainPersonArray.get(i);
            }
        }

        return foundPerson;
    }

    public int getPersonLayerNumber(String personRCode)
    {
        /*
         * Layers look like:
         * GREAT GRAND PARENT LAYER | 6
         * GRAND PARENT LAYER | 5
         * PARENT LAYER | 4
         * CURRENT PERSON LAYER | 3
         * CHILDREN LAYER | 2
         * GRAND CHILD LAYER | 1
         */

        // Do order bottom to top (1 - 6)
        if (personRCode.equals("GRNDCHILD") || personRCode.equals("GRNDDAU") || personRCode.equals("GRNDSON"))
        {
            return 1;
        }
        else if (personRCode.equals("SON") || personRCode.equals("DAU") || personRCode.equals("NCHILD") ||
                 personRCode.equals("NEPHEW") || personRCode.equals("NIECE") || personRCode.equals("NIENEPH") ||
                 personRCode.equals("SONINLAW") || personRCode.equals("DAUINLAW") || personRCode.equals("CHLDINLAW"))
        {
            return 2;
        }
        else if (personRCode.equals("PAT") || personRCode.equals("NBRO") || personRCode.equals("NSIS") || personRCode.equals("SIB") ||
                 personRCode.equals("COUSIN") || personRCode.equals("MCOUSN") || personRCode.equals("PCOUSN") ||
                 personRCode.equals("HUSB") || personRCode.equals("WIFE"))
        {
            return 3;
        }
        else if (personRCode.equals("NFTH") || personRCode.equals("NMTH") ||
                 personRCode.equals("MUNCLE") || personRCode.equals("MAUNT") ||
                 personRCode.equals("PUNCLE") || personRCode.equals("PAUNT"))
        {
            return 4;
        }
        else if (personRCode.equals("MGRMTH") || personRCode.equals("MGRFTH") || 
                 personRCode.equals("PGRMTH") || personRCode.equals("PGRFTH"))
        {
            return 5;
        }
        else if (personRCode.equals("MGGRMTH") || personRCode.equals("MGGRFTH") || 
                 personRCode.equals("PGGRMTH") || personRCode.equals("PGGRFTH"))
        {
            return 6;
        }

        return 0;
    }


    public ArrayList<Person> getPersonArray()
    {
        return mainPersonArray;
    }

    public int getOriginalPersonID()
    {
        return originalMainPersonID;
    }

    public ArrayList<String> getSubjectOf2()
    {
        return subjectOf2MainArray;
    }


    private static String evaluateXPath(XPath xpath, Object item, String expression) throws Exception {
        XPathExpression xPathExpression = xpath.compile(expression);
        return (String) xPathExpression.evaluate(item, XPathConstants.STRING);
    }

    private static String elementToStringForSubjectOfTag(Element element) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(element), new StreamResult(writer));
        return writer.toString();
    }
}
