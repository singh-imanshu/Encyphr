import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.Desktop;
public class app implements ActionListener {
    private static JFrame appFrame;
    private static JPanel appPanel;
    private static JDialog fileClickedDialog,aboutDialog;
    private static JButton encryptionButton, decryptionButton, openButton;
    private static JFileChooser fileChooser;
    private static JMenu appMenu;
    private static JMenuBar appMenuBar;
    private static JList<String> passList;
    private static JScrollPane scrollPane;
    static ArrayList<String> fileData;
    static ArrayList<String> pathData;
    static ArrayList<String> cryptData;
    static int selectedIndex;

    public static void main() throws Exception {
        //basic outline of the JFrame
        appFrame = new JFrame("Encyphr- home");
        appPanel = new JPanel();
        appFrame.setSize(800, 800);
        encryptionButton = new JButton("Click here to encrypt a file ");
        encryptionButton.addActionListener(new app());

        //creating the menu for ABOUT
        appMenuBar = new JMenuBar();
        appMenu = new JMenu("ABOUT");
        appMenuBar.add(appMenu);
        appFrame.setJMenuBar(appMenuBar);
        appMenu.addActionListener(new app());

        //concluding JFrame outline
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appPanel.add(encryptionButton);
        appFrame.setLayout(new BorderLayout());
        encryptionButton.setBounds(375, 400, 50, 20);
        appFrame.add(appPanel);
        appFrame.setResizable(false);
        appFrame.setVisible(true);

        label = new JLabel("To decrypt a file, click on it from the left menu.");
        label.setBounds(400,400,50,25);
        label1 = new JLabel("Please note that the list does not change in real time. I am still trying to figure it out.");
        label1.setBounds(400,430,50,25);
        appPanel.add(label);
        appPanel.add(label1);

        jListManager();
    }

    public static void jListManager() throws Exception {
        System.out.println("jListManager invoked");

        fileData = new ArrayList<>();
        cryptData = new ArrayList<>();
        pathData = new ArrayList<>();
        fileData.clear();
        cryptData.clear();
        pathData.clear();
        String basePath = deviceInfo.basePath;
        BufferedReader reader = new BufferedReader(new FileReader(basePath + "\\encyphrlogs.encp"));
        String currentLine;
        int x = 0;
        while ((currentLine = reader.readLine()) != null) {
            if (x % 2 == 0) {
                File file = new File(currentLine);
                fileData.add(file.getName());
                pathData.add(currentLine);
                x++;
            } else {
                cryptData.add(currentLine);
                x++;
            }
        }
        //finalising list contents and position
        String[] dataArray = fileData.toArray(new String[0]);
        passList = new JList<>(dataArray);
        scrollPane = new JScrollPane(passList);
        appFrame.add(scrollPane, BorderLayout.WEST);

        //adding functionality to list elements
        passList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedIndex = passList.getSelectedIndex();
                String selectedValue = passList.getSelectedValue();
                if (selectedValue != null) {
                    fileClickedDialog = new JDialog(appFrame, "File Options", true);
                    fileClickedDialog.setLayout(new FlowLayout());

                    openButton = new JButton("Open");
                    decryptionButton = new JButton("Decrypt");
                    fileClickedDialog.add(openButton);
                    fileClickedDialog.add(decryptionButton);
                    openButton.addActionListener(new app());
                    decryptionButton.addActionListener(new app());
                    fileClickedDialog.setSize(300, 300);
                    fileClickedDialog.setLocationRelativeTo(appFrame);
                    fileClickedDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    fileClickedDialog.setVisible(true);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encryptionButton) {
            fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                System.out.println(file);
                String encryptedFilePath = file + ".encp";
                encrypt cry = new encrypt();
                cry.main(fileChooser.getSelectedFile().getAbsolutePath(), encryptedFilePath);
            }
        }
        if (e.getSource() == decryptionButton) {
            fileClickedDialog.dispose();
            decrypt dc = new decrypt();
            System.out.println(cryptData.get(selectedIndex));
            try {
                dc.main(cryptData.get(selectedIndex), pathData.get(selectedIndex) + ".encp", pathData, cryptData);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            fileClickedDialog = new JDialog(appFrame, "File Decrypted Successfully!", true);
            fileClickedDialog.setLayout(new FlowLayout());
            openButton = new JButton("Open");
            openButton.addActionListener(new app());
            fileClickedDialog.add(openButton);
            fileClickedDialog.setSize(300, 300);
            fileClickedDialog.setLocationRelativeTo(appFrame);
            fileClickedDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            fileClickedDialog.setVisible(true);

        }
        if (e.getSource() == openButton) {
            System.out.println("The user wishes to open the file after decryption.");
            String filePath = decrypt.decryptedFilePath;
            File file = new File(filePath);
            try {
                Desktop.getDesktop().open(file);
                fileClickedDialog.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource()==appMenu){
            System.out.println("check");
            aboutDialog = new JDialog(appFrame,"ABOUT",true);
            aboutDialog.setLayout(new FlowLayout());
            aboutDialog.setSize(300,300);
            aboutDialog.setLocationRelativeTo(appFrame);
            aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            aboutDialog.setVisible(true);
        }
    }
}
