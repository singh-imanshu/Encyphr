import java.awt.event.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.Desktop;

public class app implements ActionListener {
    private static JFrame appFrame;
    private static JPanel appPanel;
    private static JDialog fileClickedDialog, aboutDialog;
    private static JButton encryptionButton, decryptionButton, openButton, closeButton, openWD;
    private static JFileChooser fileChooser;
    private static JMenuItem appMenu;
    private static JMenuBar appMenuBar;
    private static JList<String> passList;
    private static JScrollPane scrollPane;
    static ArrayList<String> fileData;
    static ArrayList<String> pathData;
    static ArrayList<String> cryptData;
    static boolean result;
    static int selectedIndex;

    public static void mainApp() throws Exception {
        // basic outline of the JFrame
        appFrame = new JFrame("Encyphr");
        appPanel = new JPanel();
        appFrame.setSize(900, 900);
        encryptionButton = new JButton("Click here to encrypt a file ");
        encryptionButton.addActionListener(new app());

        // creating the menu for ABOUT
        appMenuBar = new JMenuBar();
        appMenu = new JMenuItem("ABOUT");
        appMenuBar.add(appMenu);
        appFrame.setJMenuBar(appMenuBar);
        appMenu.addActionListener(new app());

        // concluding JFrame outline

        try {
            appFrame.setIconImage(ImageIO.read(new File("encyphr_logo.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appPanel.add(encryptionButton);
        appFrame.setLayout(new BorderLayout());
        encryptionButton.setBounds(150, 25, 200, 20);
        appPanel.setLayout(null);
        appFrame.add(appPanel);
        appFrame.setResizable(false);
        appFrame.setVisible(true);
        arrayListManager();
        jListManager();
    }

    // function for populating the arrayLists
    public static void arrayListManager() throws Exception {
        fileData = new ArrayList<>();
        cryptData = new ArrayList<>();
        pathData = new ArrayList<>();
        String basePath = deviceInfo.basePath;
        BufferedReader reader = new BufferedReader(new FileReader(basePath + "\\encyphrlogs.eph"));
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
        reader.close();
    }

    // function for managing the list on the main app that shows the encrypted files
    public static void jListManager() throws Exception {
        // finalising list contents and position
        String[] dataArray = fileData.toArray(new String[0]);
        passList = new JList<>(dataArray);
        scrollPane = new JScrollPane(passList);
        appFrame.add(scrollPane, BorderLayout.WEST);
        // adding functionality to list elements
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

    // function for updating the list
    public static void updateList() throws Exception {
        fileData.clear();
        cryptData.clear();
        pathData.clear();

        // Reload data from file
        String basePath = deviceInfo.basePath;
        BufferedReader reader = new BufferedReader(new FileReader(basePath + "\\encyphrlogs.eph"));
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
        reader.close();

        // Update the JList
        String[] dataArray = fileData.toArray(new String[0]);
        passList.setListData(dataArray);
    }

    // function for handling the elements with an actionListener attached
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == encryptionButton) {
            fileChooser = new JFileChooser();
            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                String encryptedFilePath = file + ".encp";
                encrypt cry = new encrypt();
                cry.mainEncryption(fileChooser.getSelectedFile().getAbsolutePath(), encryptedFilePath);
                if (result) {
                    fileClickedDialog = new JDialog(appFrame, "Operation Successful!", true);
                    fileClickedDialog.setLayout(new FlowLayout());
                    closeButton = new JButton("Close");
                    fileClickedDialog.add(closeButton);
                    closeButton.addActionListener(new app());
                    fileClickedDialog.setSize(300, 300);
                    fileClickedDialog.setLocationRelativeTo(appFrame);
                    fileClickedDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    fileClickedDialog.setVisible(true);
                }
            }
        }
        if (e.getSource() == decryptionButton) {
            fileClickedDialog.dispose();
            decrypt dc = new decrypt();
            try {
                dc.mainDecryption(cryptData.get(selectedIndex), pathData.get(selectedIndex) + ".encp", pathData, cryptData);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            fileClickedDialog = new JDialog(appFrame, "File Decrypted Successfully!", true);
            fileClickedDialog.setLayout(new FlowLayout());
            openWD = new JButton("Open");
            openWD.addActionListener(new app());
            fileClickedDialog.add(openWD);
            fileClickedDialog.setSize(300, 300);
            fileClickedDialog.setLocationRelativeTo(appFrame);
            fileClickedDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            fileClickedDialog.setVisible(true);
        }
        if (e.getSource() == openButton) {
            String filePath = decrypt.decryptedFilePath;
            File file = new File(filePath);
            try {
                Desktop.getDesktop().open(file);
                fileClickedDialog.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource() == appMenu) {
            aboutDialog = new JDialog(appFrame, "ABOUT", true);
            aboutDialog.setLayout(new BorderLayout());
            JLabel aboutLabel = new JLabel("<html><center>Encyphr<br>Version 1.0<br><br>Developed by: Himanshu Kumar Singh<br>Email: himanshu.2508@outlook.com<br>github: github.com/singh-imanshu</center></html>", SwingConstants.CENTER);
            aboutLabel.setFont(new Font("Arial", Font.BOLD, 14));
            aboutDialog.add(aboutLabel, BorderLayout.CENTER);
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    aboutDialog.dispose();
                }
            });
            aboutDialog.add(closeButton, BorderLayout.SOUTH);
            aboutDialog.setSize(400, 200);
            aboutDialog.setLocationRelativeTo(appFrame);
            aboutDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            aboutDialog.setVisible(true);
        }
        if (e.getSource() == closeButton) {
            fileClickedDialog.dispose();
        }
        if (e.getSource() == openWD) {
            String filePath = decrypt.decryptedFilePath;
            File file = new File(filePath);
            try {
                Desktop.getDesktop().open(file);
                fileClickedDialog.dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
