import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class main implements ActionListener {
    private static JPanel signUpPanel;
    private static JPasswordField passField1, passField2;
    private static JLabel enterPass1, enterPass2;
    private static JFrame mainFrame;
    private static final String sysName=System.getProperty("user.name");
    private static final String mainPath = "C://users//"+sysName+"//Encyphr//";
        public static void main(String[] args) {
            System.out.println("Name of system: " + sysName);
            File file = new File(mainPath+"userPass.txt");
            if(file.exists() && !file.isDirectory()) {
                promptLogin();
            }
            else {
                promptSignUp();
            }
        }

        public static void promptLogin(){
            mainFrame = new JFrame("Login to Encyphr");
            mainFrame.setSize(450,400);
            mainFrame.setLayout(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
        }

        public static void promptSignUp(){
            /*File createFile = new File(mainPath+"userPass.txt");
            try{
                if(createFile.createNewFile()){
                    System.out.println("File created: "+createFile.getName());
                } else {
                    System.out.println("File already exists.");
                }
            } catch (IOException fileCreateException) {
                fileCreateException.printStackTrace();
            }*/
            mainFrame = new JFrame("SignUp to Encyphr");
            mainFrame.setSize(450,400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            signUpPanel = new JPanel();
            signUpPanel.setLayout(null);
            enterPass1 = new JLabel("Create a new password: ");
            enterPass2 = new JLabel("Confirm your password: ");
            enterPass1.setBounds(10,10,150,30);
            enterPass2.setBounds(10,40,150,30);
            signUpPanel.add(enterPass1);
            signUpPanel.add(enterPass2);
            passField1 = new JPasswordField();
            passField2 = new JPasswordField();
            passField1.setBounds(165,10,140,25);
            passField2.setBounds(165,40,140,25);
            signUpPanel.add(passField1);
            signUpPanel.add(passField2);
            mainFrame.add(signUpPanel);
            mainFrame.setResizable(false);
            mainFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }
}