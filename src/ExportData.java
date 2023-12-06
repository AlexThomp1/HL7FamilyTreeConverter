import java.util.ArrayList;

public class ExportData {

    // Final Export Array
    ArrayList<String> exportArray = new ArrayList<String>();

    public ExportData()
    {
    }

    // The order in how the functions should be called are in the order of this file!
    
    public void staticStrings1(String creationTime)
    {
        exportArray.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        exportArray.add("<FamilyHistory classCode=\"OBS\" moodCode=\"EVN\">");
        exportArray.add("  <id root=\"2.16.840.1.113883.6.117\" extension=\"116\" assigningAuthorityName=\"HRA\" />");
        exportArray.add("  <code code=\"10157-6\" codeSystemName=\"LOINC\" displayName=\"HISTORY OF FAMILY MEMBER DISEASE\" />");
        exportArray.add("  <text>ClinicName; InstitutionName</text>");
        exportArray.add("  <effectiveTime value=\"" + creationTime + "\" />");
        exportArray.add("  <subject typeCode=\"SBJ\">");
        exportArray.add("    <patient classCode=\"PAT\">");
    }

    public void mrnLine(String mrn)
    {
        exportArray.add("      <id root=\"2.16.840.1.113883.6.117\" extension=\"" + mrn + "\" />");
    }

    public void mainPersonStart(Person mainPerson)
    {
        exportArray.add("      <patientPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">");
        exportArray.add("        <id extension=\"1\" />");
        exportArray.add("        <name>");
        exportArray.add("          <given>" + mainPerson.getFirstName() + "</given>");
        exportArray.add("          <family>" + mainPerson.getLastName() + "</family>");
        exportArray.add("        </name>");
        exportArray.add("        <administrativeGenderCode code=\"" + mainPerson.getGender() + "\" />");
        exportArray.add("        <birthTime value=\"" + mainPerson.getBirthTime() + "\" />");
        exportArray.add("        <deceasedInd value=\"" + mainPerson.getDeceased() + "\" />");
    }

    public void addEveryoneElse(ArrayList<Person> personArray, ArrayList<String> subjectOf2List, int ogPersonID)
    {
        for (int i = 1; i < personArray.size(); i++)
        {
             exportArray.add("        <relative classCode=\"PRS\">");
             exportArray.add("          <code code=\"" + personArray.get(i).getRCode() + "\" />");
             exportArray.add("          <relationshipHolder classCode=\"PSN\" determinerCode=\"INSTANCE\">");
             exportArray.add("            <id extension=\"" + personArray.get(i).getID() + "\" />");
             exportArray.add("            <name>");
             exportArray.add("              <given>" + personArray.get(i).getFirstName() + "</given>");
             exportArray.add("              <family>" + personArray.get(i).getLastName() + "</family>");
             exportArray.add("            </name>");
             exportArray.add("            <administrativeGenderCode code=\"" + personArray.get(i).getGender() + "\" />");
             exportArray.add("            <deceasedInd value=\"" + personArray.get(i).getDeceased() + "\" />");

             if (personArray.get(i).getRelativeArray().size() > 0)
             {
                for (int j = 0; j < personArray.get(i).getRelativeArray().size(); j++)
                {
                    exportArray.add("            <relative classCode=\"PRS\">");
                    exportArray.add("              <code code=\"" + personArray.get(i).getRelativeArray().get(j).getRCodeToPerson() + "\" />");
                    exportArray.add("              <relationshipHolder classCode=\"PSN\" determinerCode=\"INSTANCE\">");
                    exportArray.add("                <id extension=\"" + personArray.get(i).getRelativeArray().get(j).getID() + "\" />");
                    exportArray.add("              </relationshipHolder>");
                    exportArray.add("            </relative>");
                }
             }

             exportArray.add("          </relationshipHolder>");

             if (personArray.get(i).getSubjectOf1().isEmpty())
             {
                exportArray.add("          " + "<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
             }
             else
             {
                exportArray.add("          " + personArray.get(i).getSubjectOf1());
             }

             // Add subjectOf2
            if (personArray.get(i).getSubjectOf2().size() > 0)
            {
                exportArray.add("          " + personArray.get(i).getSubjectOf2().get(0));

                for (int j = 1; j < personArray.get(i).getSubjectOf2().size(); j++)
                {
                    exportArray.add("	      " + personArray.get(i).getSubjectOf2().get(j));
                }
            }

             /*if (personArray.get(i).getID() == ogPersonID)
             {
                if (subjectOf2List.isEmpty())
                {
                    exportArray.add("	      <subjectOf2 typeCode=\"SBJ\">");
                    exportArray.add("      </subjectOf2>");
                }
                else
                {
                    exportArray.add("          " + subjectOf2List.get(0));

                    for (int j = 1; j < subjectOf2List.size(); j++)
                    {
                        exportArray.add("	      " + subjectOf2List.get(j));
                    }
                }
             }*/

             exportArray.add("        </relative>");
        }

        exportArray.add("      </patientPerson>");
    }

    public void addFinalSubjectOf1(Person mainPerson)
    {
        if (mainPerson.getSubjectOf1().isEmpty())
        {
            exportArray.add("          " + "<subjectOf1 typeCode=\"SBJ\">\n          </subjectOf1>");
        }
        else
        {
            exportArray.add("      " + mainPerson.getSubjectOf1());
        }

        if (mainPerson.getSubjectOf2().size() < 1)
        {
            exportArray.add("          " + "<subjectOf2 typeCode=\"SBJ\">\n          </subjectOf2>");
        }
        else
        {
            for (int i = 0; i < mainPerson.getSubjectOf2().size(); i++)
            {
                exportArray.add("      " + mainPerson.getSubjectOf2().get(i));
            }
        }
    }

    public void addEndingLines()
    {
        exportArray.add("    </patient>");
        exportArray.add("  </subject>");
        exportArray.add("</FamilyHistory>");
    }

    public ArrayList<String> getExportArray()
    {
        return exportArray;
    }
}
    
