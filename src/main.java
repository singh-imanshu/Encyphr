import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class main implements ActionListener {
    private static JPanel signUpPanel, loginPanel;
    private static JPasswordField passField1, passField2;
    private static JButton confirmButton1, confirmButton2;
    private static JLabel enterPass1, enterPass2, algorithmSelection;
    private static JFrame mainFrame;
    private static JComboBox<String> algorithmComboBox;
    static String mainPath;
        public static void main(String[] args) {

            //getting basic device info and specifying folderPath
            deviceInfo di = new deviceInfo();
            try {
                di.relInfo();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            mainPath = deviceInfo.basePath;

            //creation of EncyphrVault folder
            File folder = new File(mainPath);
            if(folder.exists()&&folder.isDirectory()){
                System.out.println("Vault existence: positive");
            } else {
                folder.mkdirs();
            }

            //creation of metadata.eph
            File file = new File(mainPath+"\\metadata.eph");
            if(file.exists() && !file.isDirectory()) {
                promptLogin();
            }
            else {
                promptSignUp();
            }

        }

        public static void promptLogin() {
            //login page GUI
            mainFrame = new JFrame("Login to Encyphr");
            mainFrame.setSize(450,400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginPanel = new JPanel();
            loginPanel.setLayout(null);
            enterPass1 = new JLabel("Enter your password: ");
            enterPass1.setBounds(10,10,150,30);
            passField1 = new JPasswordField();
            passField1.setBounds(165,10,140,25);
            confirmButton2 = new JButton("SUBMIT");
            confirmButton2.setBounds(10,50,100,25);
            loginPanel.add(confirmButton2);
            loginPanel.add(enterPass1);
            loginPanel.add(passField1);
            mainFrame.add(loginPanel);
            confirmButton2.addActionListener(new main());
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
        }

    public static void promptSignUp() {
        File createFile = new File(mainPath + "\\metadata.eph");
        File logFile = new File(mainPath + "\\encyphrlogs.eph");

        // Creating logFile
        try {
            if (createFile.createNewFile() && logFile.createNewFile()) {
                System.out.println("Files created: " + createFile.getName() + " and " + logFile.getName());
            } else {
                System.out.println("Files already exist.");
            }
        } catch (IOException fileCreateException) {
            fileCreateException.printStackTrace();
        }

        // Sign up page GUI
        mainFrame = new JFrame("Register to Encyphr");
        mainFrame.setSize(450, 450);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpPanel = new JPanel();
        signUpPanel.setLayout(null);

        enterPass1 = new JLabel("Create a new password: ");
        enterPass2 = new JLabel("Confirm your password: ");
        enterPass1.setBounds(10, 10, 150, 30);
        enterPass2.setBounds(10, 40, 150, 30);
        signUpPanel.add(enterPass1);
        signUpPanel.add(enterPass2);

        passField1 = new JPasswordField();
        passField2 = new JPasswordField();
        passField1.setBounds(165, 10, 140, 25);
        passField2.setBounds(165, 40, 140, 25);
        signUpPanel.add(passField1);
        signUpPanel.add(passField2);

        // Create a label for the algorithm selection
        algorithmSelection = new JLabel("Select encryption algorithm:");
        algorithmSelection.setBounds(10, 75, 175, 30);
        signUpPanel.add(algorithmSelection);

        // Create a JComboBox for selecting the encryption algorithm
        String[] algorithms = {"AES-192", "AES-128", "AES-256"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setBounds(185, 75, 140, 25);
        signUpPanel.add(algorithmComboBox);

        confirmButton1 = new JButton("SUBMIT");
        confirmButton1.setBounds(10, 110, 100, 25);
        confirmButton1.addActionListener(new main());
        signUpPanel.add(confirmButton1);

        mainFrame.add(signUpPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

        //handling the functionality of the buttons
        @Override
        public void actionPerformed(ActionEvent e) {

            if(e.getSource()==confirmButton2){
                try {
                    checkPasswordValidity();
                }catch(Exception exc1){
                    exc1.printStackTrace();
                }
            }

            if (e.getSource() == confirmButton1) {
                String temp = String.valueOf(passField1.getPassword());
                if (temp.equals(String.valueOf(passField2.getPassword()))) {
                    try {
                        temp = hash(temp);
                        FileWriter fileWriter = new FileWriter(mainPath + "\\metadata.eph");
                        fileWriter.write(temp);
                        fileWriter.close();

                        // Get the selected encryption algorithm
                        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                        File file = new File(mainPath+"\\cryptdata.eph");
                        file.createNewFile();
                        System.out.println("cryptdata.eph created");
                        FileWriter fw = new FileWriter(mainPath+"\\cryptdata.eph",true);
                        fw.write(selectedAlgorithm.substring(4,7)+"\n");
                        fw.close();
                        deviceInfo dc = new deviceInfo();
                        dc.relInfo();
                        mainFrame.dispose();
                        promptLogin();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        //method for hashing the password upon login and storage for the first time
        public static String hash(String st) throws Exception {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(st.getBytes());
            byte[] stringHashArray = messageDigest.digest();
            StringBuilder sdbs = new StringBuilder();
            for(byte variable : stringHashArray)
            {
                sdbs.append(String.format("%02x",variable));
            }
            String resultHash = sdbs.toString();
            return resultHash;
        }

        //verifying the password
        public static void checkPasswordValidity() throws Exception{
            char [] passChar = passField1.getPassword();
            String passText = new String(passChar);
            String hashedPass = hash(passText);
            ArrayList<String> data =new ArrayList<>();
            Scanner scanner = new Scanner(new File(mainPath+"\\metadata.eph"));
            while(scanner.hasNext()) {
                data.add(scanner.next());
            }
            String val = data.get(0);
            if(val.equals(hashedPass)){
                app mainApp = new app();
                mainFrame.dispose();
                mainApp.mainApp();
            }
        }
}
