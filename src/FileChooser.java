// Java program to create open or
// save dialog using JFileChooser
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
//import java.awt.GridLayout;
import java.awt.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;

import javax.swing.filechooser.*;

class FileChooser extends JFrame implements ActionListener {
 
    // Jlabel to show the files user selects
    static JFrame iconFrame;
    static JLabel outputLabel;
    static JLabel pathLabel;
    static JLabel importLabel;
    static JLabel patientLabel;
    static JLabel exportLabel;
    static JLabel noFileLabel;
    static JLabel labelDOB;
    static JLabel labelMRN;
    static JLabel labelFirstName;
    static JLabel labelLastName;
    static JLabel labelRCode;
    static JLabel labelFirstNameEdit;
    static JLabel labelLastNameEdit;
    static JScrollPane displayAreaScroll;
    static JTextArea displayArea;
    static DefaultListModel<String> defaultModel;
    static JList<String> listDisplay;
    static String fName;
    static String lName;
    static String medRN;
    static String dateOB;
    static String fNameEdit;
    static String lNameEdit;
    static String rEdit;

    private JComboBox<String> patientBox;
    private JComboBox<String> rCodeBox;
    private JButton editButton;
    private JCheckBox customText;

    // Variables
    private FamilyTree familyTree;
    private Display display;
    private String fileImportPath = "";

    // Fonts
    Font tahoma;
    Font tahomaSmall;
 
    // a default constructor
    FileChooser()
    {
        familyTree = new FamilyTree();
        display = new Display();

        // Fonts
        tahoma = new Font("Tahoma", tahoma.BOLD, 12);
    }
 
    //public static void main(String args[])
    public void createFrame()
    {
        // frame to contain GUI elements
        JFrame frame = new JFrame("file chooser");

        // changes background color to purple
        //frame.getContentPane().setBackground(new Color(123, 50, 250));

        // sets Title
        frame.setTitle("HL7 Family Tree Importer/Converter");

        // allows user to close application by clicking x
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // doesn't allow user to resize application
        frame.setResizable(false);

        // sets application to 750 x 500
        frame.setSize(550, 460);

        frame.setLocationRelativeTo(null);

        // makes application visible
        frame.setVisible(true);

        // Pop up for Re-orient Patient
        JFrame popUpFrame = new JFrame("Basic Information");

        popUpFrame.setTitle("Enter Patient's Data");

        popUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        popUpFrame.setResizable(false);

        popUpFrame.setSize(550, 300);

        popUpFrame.setVisible(false);

        // Pop up for edit patient
        JFrame editPopUpFrame = new JFrame("Edit Selected Person");

        editPopUpFrame.setTitle("Edit Selected Person's Information");

        editPopUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        editPopUpFrame.setResizable(false);

        editPopUpFrame.setSize(550, 300);

        editPopUpFrame.setVisible(false);

        // creates image object
        //ImageIcon image = new ImageIcon();

        // pulls image from given path
        //this.setIconImage(image.getImage());

        // make a panel to add the buttons and labels
        JPanel panel = new JPanel();
        JPanel popUpPanel = new JPanel();
        JPanel editPopUpPanel = new JPanel();

        //panel.setBackground(new Color(123, 50, 250));
        panel.setLayout(null);
        popUpPanel.setLayout(null);
        editPopUpPanel.setLayout(null);

        // creates a box to display output text
        outputLabel = new JLabel("Output of Family Tree:");
        Dimension size0 = outputLabel.getPreferredSize();
        outputLabel.setBounds(135, 25, 200, size0.height);

        // set the label to its initial value
        importLabel = new JLabel("1) Import Your File:");
        Dimension size1 = importLabel.getPreferredSize();
        importLabel.setBounds(380, 50, size1.width, size1.height);

        // set the label to its initial value
        noFileLabel = new JLabel("No File Selected");
        Dimension size2 = noFileLabel.getPreferredSize();
        noFileLabel.setBounds(380, 75, 250, size2.height);

        // set the label to its initial value
        //** SEE ABOUT IMPLEMENTING A HOVER ACTION TO SHOW ENTIRE FILE PATH **/
        pathLabel = new JLabel("No File Selected");
        Dimension pathSize = pathLabel.getMaximumSize();
        pathLabel.setBounds(380, 75, pathSize.width, pathSize.height);
 
        // button to open save dialog
        JButton importBtn = new JButton("Select File");
        Dimension size3 = importBtn.getPreferredSize();
        importBtn.setBounds(380, 95, 125, size3.height);
        importBtn.setBackground(new Color(255, 255, 255));
        importBtn.setFont(tahoma);

        // set the label to its initial value
        patientLabel = new JLabel("2) Select the New Patient:");
        Dimension size4 = patientLabel.getPreferredSize();
        patientLabel.setBounds(380, 145, size4.width, size4.height);

        // Drop-down box for people
        patientBox = new JComboBox<>();
        Dimension patientBoxSize = patientBox.getPreferredSize();
        patientBox.setBounds(380, 170, 125, patientBoxSize.height);
        patientBox.setBackground(new Color(255, 255, 255));
        patientBox.setFont(tahoma);
        patientBox.setMaximumRowCount(12);

        // Drop-down box for RCodes
        rCodeBox = new JComboBox<>();
        Dimension rCodeBoxSize = rCodeBox.getPreferredSize();
        rCodeBox.setBounds(215, 105, 100, rCodeBoxSize.height);
        rCodeBox.setBackground(new Color(255, 255, 255));
        rCodeBox.setFont(tahoma);
        rCodeBox.setMaximumRowCount(12);
 
        // set the label to its initial value
        exportLabel = new JLabel("3) Export Your File:");
        Dimension size6 = exportLabel.getPreferredSize();
        exportLabel.setBounds(380, 210, 120, size6.height);
        
        // button to open open dialog
        JButton exportBtn = new JButton("Export File");
        Dimension size7 = exportBtn.getPreferredSize();
        exportBtn.setBounds(380, 235, 125, size7.height);
        exportBtn.setBackground(new Color(255, 255, 255));
        exportBtn.setFont(tahoma);

        // Button for editing selected person
        editButton = new JButton("Edit Person's Information");
        Dimension size20 = editButton.getPreferredSize();
        editButton.setBounds(35, 376, 320, size20.height);
        editButton.setBackground(new Color(255, 255, 255));
        editButton.setFont(tahoma);

        // Area for Output display
        //displayArea = new JTextArea();
        //displayArea.setBounds(100, 50, 200, 300);
        //displayArea.setEditable(false);

        // List for Output display
        defaultModel = new DefaultListModel<String>();
        listDisplay = new JList<String>();
        listDisplay.setBounds(35, 50, 320, 325);
        listDisplay.setModel(defaultModel);

        // To make Output display scrollable if bigger than box
        displayAreaScroll = new JScrollPane(listDisplay);
        displayAreaScroll.setBounds(35, 50, 320, 325);

        labelFirstName = new JLabel("First Name:");
        Dimension size10 = labelFirstName.getMaximumSize();
        labelFirstName.setBounds(147, 35, size10.width, size10.height);

        labelLastName = new JLabel("Last Name:");
        Dimension size11 = labelLastName.getMaximumSize();
        labelLastName.setBounds(147, 70, 200, size11.height);

        labelMRN = new JLabel("Medical Record Number:");
        Dimension size12 = labelMRN.getMaximumSize();
        labelMRN.setBounds(72, 105, 200, size12.height);

        labelDOB = new JLabel("Date of Birth(MM/DD/YYYY):");
        Dimension size13 = labelDOB.getMaximumSize();
        labelDOB.setBounds(59, 140, 200, size13.height);

        labelFirstNameEdit = new JLabel("First Name:");
        Dimension size15 = labelFirstNameEdit.getMaximumSize();
        labelFirstNameEdit.setBounds(147, 35, size10.width, size15.height);

        labelLastNameEdit = new JLabel("Last Name:");
        Dimension size16 = labelLastNameEdit.getMaximumSize();
        labelLastNameEdit.setBounds(147, 70, 200, size16.height);

        labelRCode = new JLabel("Relationship Code:");
        Dimension size14 = labelRCode.getMaximumSize();
        labelRCode.setBounds(107, 105, 200, size14.height);

        JTextField firstName = new JTextField(20);
        //Dimension size14 = firstName.getMaximumSize();
        firstName.setBounds(215, 35, 100, 20);

        JTextField lastName = new JTextField(20);
        lastName.setBounds(215, 70, 100, 20);
        JTextField DOB = new JTextField(10);
        DOB.setBounds(215, 140, 100, 20);
        JTextField MRN = new JTextField(20);
        MRN.setBounds(215, 105, 100, 20);

        JButton doneBtn = new JButton("Done");
        doneBtn.setBounds(225, 175, 75, 25);
        doneBtn.setBackground(new Color(255, 255, 255));
        doneBtn.setFont(tahoma);

        JTextField firstNameEdit = new JTextField(20);
        firstNameEdit.setBounds(215, 35, 100, 20);

        JTextField lastNameEdit = new JTextField(20);
        lastNameEdit.setBounds(215, 70, 100, 20);
        JTextField rCodeEdit = new JTextField(10);
        rCodeEdit.setBounds(215, 105, 100, 20);

        JButton doneBtnEdit = new JButton("Done");
        doneBtnEdit.setBounds(225, 160, 75, 25);
        doneBtnEdit.setBackground(new Color(255, 255, 255));
        doneBtnEdit.setFont(tahoma);

        JButton helpButton = new JButton("?");
        helpButton.setBounds(10, 10, 20, 20);
        helpButton.setBackground(new Color(255, 255, 255));
        helpButton.setFont(tahoma);
        helpButton.setMargin(new Insets(0, 0, 0, 0));

        customText = new JCheckBox("Unique Code?");
        customText.setBounds(320, 103, 150, 25);
        customText.setFont(tahoma);

        JLabel scrollHeader = new JLabel("ID | Name | Relationship Code");
        displayAreaScroll.setColumnHeaderView(scrollHeader);

        ImageIcon img = new ImageIcon(this.getClass().getResource("img/familyTreeIconTransparent.png"));
        frame.setIconImage(img.getImage());
        popUpFrame.setIconImage(img.getImage());
        editPopUpFrame.setIconImage(img.getImage());

        popUpFrame.setLocationRelativeTo(null);
        editPopUpFrame.setLocationRelativeTo(null);
 
        // make an object of the class filechooser
        FileChooser fChoose = new FileChooser();
 
        // add action listener to the button to capture user
        // response on buttons
        importBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Calls importFile function when file is choosen
                importFile();
            }
        });
        exportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    familyTree.getPersonArray().size();
                }
                catch (Exception e2)
                {
                    JOptionPane.showMessageDialog(null, "No data to export!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                // Calls importFile function when file is choosen
                exportFile();
            }

        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (listDisplay.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(null, "Please select a person in the output box to edit!", "Error!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                // Set the first & last fields to current ones
                firstNameEdit.setText(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getFirstName());
                lastNameEdit.setText(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getLastName());
                rCodeEdit.setText(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getRCode());
                rCodeBox.setSelectedItem(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getRCode());
                
                editPopUpFrame.setVisible(true);
            }
        });
        doneBtnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make this data useable by other classes
                fNameEdit = firstNameEdit.getText();
                lNameEdit = lastNameEdit.getText();
                rEdit = rCodeEdit.getText();

                familyTree.getPersonArray().get(listDisplay.getSelectedIndex()).setFirstName(fNameEdit);
                familyTree.getPersonArray().get(listDisplay.getSelectedIndex()).setLastName(lNameEdit);

                if (customText.isSelected())
                {
                    familyTree.getPersonArray().get(listDisplay.getSelectedIndex()).setRCode(rEdit);
                }
                else
                {
                    familyTree.getPersonArray().get(listDisplay.getSelectedIndex()).setRCode(rCodeBox.getSelectedItem().toString());
                }

                editPopUpFrame.dispose();

                populateDropdown();
            }
        });
        patientBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED && patientBox.getSelectedIndex() != -1) {
                    // Set the first & last fields to current ones
                    firstName.setText(familyTree.findPersonByID(patientBox.getSelectedIndex() + 1).getFirstName());
                    lastName.setText(familyTree.findPersonByID(patientBox.getSelectedIndex() + 1).getLastName());
                    //System.out.println("POPPED UP!");
                    popUpFrame.setVisible(true);
                }
            }
        });
        customText.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    rCodeEdit.setVisible(true);
                    rCodeBox.setVisible(false);
                }
                else
                {
                    rCodeEdit.setVisible(false);
                    rCodeBox.setVisible(true);
                    rCodeBox.setSelectedItem(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getRCode());
                }
            }
        });
        doneBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Make this data useable by other classes
                fName = firstName.getText();
                lName = lastName.getText();
                medRN = MRN.getText();
                dateOB = DOB.getText();
                // String for birthTime
                String birthTime = "";

                // Checks for MRN
                if (medRN.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Please enter the MRN!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Sets the birthtime & configures the entered birthtime to correct format
                birthTime = familyTree.getPersonArray().get(0).configureBirthTime(dateOB);

                // Catches if a wrong date format is inputted
                if (birthTime.equals("BADFORMAT"))
                {
                    JOptionPane.showMessageDialog(null, "Wrong date format!", "Error", JOptionPane.INFORMATION_MESSAGE);
                    DOB.setText("");
                    return;
                }

                // Get index + 1 of selected box, and find the person by ID, and pass to reorient
                familyTree.reOrientPatient(familyTree.findPersonByID(patientBox.getSelectedIndex() + 1));

                // Set birthtime to 1st person & new First and Last names
                familyTree.getPersonArray().get(0).setBirthTime(birthTime);
                familyTree.getPersonArray().get(0).setFirstName(fName);
                familyTree.getPersonArray().get(0).setLastName(lName);

                //System.out.println(familyTree.getPersonArray().get(0).getBirthTime());
                populateDropdown();
                patientBox.setSelectedIndex(0);
                popUpFrame.dispose();
                exportBtn.setVisible(true);
                exportLabel.setVisible(true);

                firstName.setText("");
                lastName.setText("");
                MRN.setText("");
                DOB.setText("");
            }
        });
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane optionPane = new JOptionPane(new JLabel("<html><center><h3>HL7 Family Tree Importer/Converter</h3> <br> Version: 1.0<br><br> Developers: Alex Thompson, Nate Mixon, and Josh Stradford <br><br> Managers: Evan Hack and Caleb Gillispie", JLabel.CENTER));
                JDialog dialog = optionPane.createDialog("Information");
                dialog.setIconImage(img.getImage());
                dialog.setModal(true);
                dialog.setVisible(true);
            }
        });
        // Checks if window is closed for new patient
        WindowListener listener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt)
            {
                Frame frame = (Frame) evt.getSource();
                patientBox.setSelectedIndex(0);
            }
        };
        // Checks if window is closed for edit patient
        WindowListener listener2 = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evt)
            {
                Frame frame = (Frame) evt.getSource();
                rCodeBox.setSelectedItem(familyTree.findPersonByID(listDisplay.getSelectedIndex() + 1).getRCode());
            }
        };
 
        // add buttons and labels to the frame
        panel.add(importBtn);
        panel.add(exportBtn);
        panel.add(helpButton);
        panel.add(editButton);
        exportBtn.setVisible(false);
        editButton.setVisible(false);
        panel.add(outputLabel);
        panel.add(importLabel);
        panel.add(noFileLabel);
        noFileLabel.setVisible(true);
        panel.add(pathLabel);
        pathLabel.setVisible(false);
        panel.add(patientLabel);
        panel.add(patientBox);
        patientLabel.setVisible(false);
        patientBox.setVisible(false);
        panel.add(exportLabel);
        exportLabel.setVisible(false);
        panel.add(displayAreaScroll);

        popUpPanel.add(labelFirstName);
        popUpPanel.add(firstName);
        popUpPanel.add(labelLastName);
        popUpPanel.add(lastName);
        popUpPanel.add(labelMRN);
        popUpPanel.add(MRN);
        popUpPanel.add(labelDOB);
        popUpPanel.add(DOB);
        popUpPanel.add(doneBtn);

        editPopUpPanel.add(labelFirstNameEdit);
        editPopUpPanel.add(firstNameEdit);
        editPopUpPanel.add(labelLastNameEdit);
        editPopUpPanel.add(lastNameEdit);
        editPopUpPanel.add(labelRCode);
        editPopUpPanel.add(rCodeEdit);
        editPopUpPanel.add(doneBtnEdit);
        editPopUpPanel.add(customText);
        editPopUpPanel.add(rCodeBox);
 
        // add panel to the frame
        frame.add(panel);
        popUpFrame.add(popUpPanel);
        editPopUpFrame.add(editPopUpPanel);
 
        frame.setVisible(true);
        popUpFrame.setVisible(false);
        editPopUpFrame.setVisible(false);
        rCodeEdit.setVisible(false);
        rCodeBox.setVisible(true);
        popUpFrame.addWindowListener(listener);
        editPopUpFrame.addWindowListener(listener2);
        iconFrame = frame;
    }

    /*
     * I am keeping this function for now, but it's currently not used for anything atm
     */
    public void actionPerformed(ActionEvent evt)
    {
    }

    // Once the open file button is pressed, this function is called
    private void importFile()
    {
        // Finds location of current .jar file
        File loc = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        // create an object of JFileChooser class
        JFileChooser path = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(loc));

        // restrict user to selecting files with .xml extension
        path.setAcceptAllFileFilterUsed(false);

        path.setDialogTitle("Select a HL7 File to Import");

        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .xml files", "xml");
        path.addChoosableFileFilter(restrict);

        // invoke the showsOpenDialog function to show the save dialog
        int check = path.showOpenDialog(iconFrame);

        // if the user selects a file
        if (check == JFileChooser.APPROVE_OPTION)
        {
            noFileLabel.setVisible(false);
            // set the label to the path of the selected file
            //pathLabel.setText(path.getSelectedFile().getAbsolutePath());
            pathLabel.setText(path.getSelectedFile().getAbsoluteFile().getName());
            pathLabel.setVisible(true);

            // Sets the path of the import file
            fileImportPath = path.getSelectedFile().getAbsolutePath();

            // Checks to see if the file can be parsed correctly & parses it
            int parseErrorCode = familyTree.parseFile(fileImportPath);

            if(parseErrorCode == 1)
            {
                // If can't parse due to not correct file format, show error & quit function
                JOptionPane.showMessageDialog(null, "Could not parse file! Not correct file format!", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            else if(parseErrorCode == 2)
            {
                // If can't parse due to not matching attribute tag sizes (id # != firstname #), show error & quit function
                JOptionPane.showMessageDialog(null, "Could not parse file! Missing information on people!", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Adds the parsed file to the drop-down
            populateDropdown();
            populateRCodeBox();
            patientBox.setVisible(true);
            patientLabel.setVisible(true);
            editButton.setVisible(true);
        }
        // if the user cancelled the operation
        else
        {
            //pathLabel.setText("the user cancelled the operation");
            //System.out.println("The User cancelled the operation.");
        }
    }

    private void exportFile()
    {
        // Finds location of current .jar file
        File loc = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        // create an object of JFileChooser class
        JFileChooser path = new JFileChooser(FileSystemView.getFileSystemView().getParentDirectory(loc));

        // restrict user to selecting files with .xml extension
        path.setAcceptAllFileFilterUsed(false);

        path.setDialogTitle("Select a .xml File");

        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only export .xml files", "xml");
        path.addChoosableFileFilter(restrict);

        String newDOB = dateOB.replaceAll("/", "-");

        String defaultFileName = familyTree.getPersonArray().get(0).getFirstName() + " " +
        familyTree.getPersonArray().get(0).getLastName()+ " " + medRN + " HL7 " + newDOB;
        //System.out.println(defaultFileName);
        path.setSelectedFile(new File(defaultFileName));

        // invoke the showsOpenDialog function to show the save dialog
        int check = path.showSaveDialog(iconFrame);

        // if the user selects a file
        if (check == JFileChooser.APPROVE_OPTION)
        {
            // Create new XML file
            try 
            {
                File file = new File(path.getSelectedFile() + ".xml");
                if (file.createNewFile()) 
                {
                    //System.out.println("File created: " + file.getName());
                } 
                else 
                {
                    //System.out.println("File already exists.");
                }
            }
            catch (IOException e) 
            {
                //System.out.println("An error occurred.");
                JOptionPane.showMessageDialog(null, "An error has occured while creating the export file!", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Write in new XML file
            try
            {
                FileWriter writer = new FileWriter(path.getSelectedFile() + ".xml");
                ExportData exportData = new ExportData();

                // Every function to gather the correct information to export
                exportData.staticStrings1(familyTree.creationTime);
                exportData.mrnLine(medRN);
                exportData.mainPersonStart(familyTree.getPersonArray().get(0));
                exportData.addEveryoneElse(familyTree.getPersonArray(), familyTree.getSubjectOf2(), familyTree.getOriginalPersonID());
                exportData.addFinalSubjectOf1(familyTree.getPersonArray().get(0));
                exportData.addEndingLines();

                ArrayList<String> displayOutput = exportData.getExportArray();

                for (int i = 0; i < displayOutput.size(); i++)
                {
                    writer.write(displayOutput.get(i) + "\n");
                }

                writer.close();
                //System.out.println("Added to file successfully!");

                JOptionPane.showMessageDialog(null, "Export completed successfully!", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null, "An error has occured while writing to the export file!", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        // if the user cancelled the operation
        else
        {
            //System.out.println("The User cancelled the operation.");
        }
    }

    private void populateDropdown()
    {
        // Remove old HL7 patients if there are any in the drop-down box
        patientBox.removeAllItems();
        //displayArea.selectAll();
        //displayArea.replaceSelection("");
        defaultModel.clear();

        // Put the output of what to display in String Array
        ArrayList<String> stringArray = new ArrayList<String>();
        stringArray = display.displayFamilyTree(familyTree.getPersonArray());

        //displayArea.append("ID | Name | Relationship Code\n\n");

        // Adds each person from the personArray to the drop-down box
        for (int i = 0; i < familyTree.getPersonArray().size(); i++)
        {
            // Add person display to output box
            defaultModel.addElement(stringArray.get(i));
            //displayArea.append(stringArray.get(i));
            
            // Add person to the drop-down box
            patientBox.addItem(Integer.toString(familyTree.getPersonArray().get(i).getID()) + ": " + familyTree.getPersonArray().get(i).getFirstName() + " "
            + familyTree.getPersonArray().get(i).getLastName());
        }

        listDisplay.setModel(defaultModel);
    }

    public void populateRCodeBox()
    {
        rCodeBox.removeAllItems();

        ArrayList<String> rCodeArray = new ArrayList<String>();
        rCodeArray.addAll(Arrays.asList("PAT", "NMTH", "NFTH", "MGRMTH", "MGRFTH", "PGRMTH", "PGRFTH", 
        "DAU", "SON", "MCOUSN", "PCOUSN", "COUSN", "MAUNT", "PAUNT", "MGGRMTH", "MGGRFTH", "PGGRMTH", "PGGRFTH", 
        "GRNDSON", "GRNDDAU", "DAUINLAW", "SONINLAW", "FTHINLAW", "MTHINLAW", "BROINLAW", "SISINLAW", "NEPHEW", 
        "NIECE", "STPFTH", "STPMTH", "NBRO", "NSIS", "HBRO", "HSIS", "STPSIS", "STPBRO", "HUSB", "WIFE"));

        Collections.sort(rCodeArray);

        for (int i = 0; i < rCodeArray.size(); i++)
        {
            rCodeBox.addItem(rCodeArray.get(i));
        }
    }

    public String getImportPath()
    {
        return fileImportPath;
    }
}