import java.io.*;
import java.util.*;

public class deviceInfo {
    static String systemDrive;
    static String deviceNameInfo, osInfo, fileSeparator, basePath;
    static int algodata;

    public static void relInfo() throws Exception {
        deviceNameInfo = System.getProperty("user.name");
        osInfo = System.getProperty("os.name").toLowerCase();
        fileSeparator = System.getProperty("file.separator");

        if (osInfo.contains("windows")) {
            systemDrive = System.getenv("SystemDrive");
            basePath = systemDrive + fileSeparator + "Users" + fileSeparator + deviceNameInfo + fileSeparator + "EncyphrVault";
        } else if (osInfo.contains("linux") || osInfo.contains("unix") || osInfo.contains("mac")) { // Added support for Linux, Unix, and Mac
            systemDrive = System.getProperty("user.home"); // Use user's home directory as base
            basePath = systemDrive + fileSeparator + ".EncyphrVault"; // Use a hidden directory (prefix with '.')x
        } else {
            throw new UnsupportedOperationException("Unsupported Operating System: " + osInfo);
        }

        File file = new File(basePath + fileSeparator + "cryptdata.eph");
        ArrayList<String> md = new ArrayList<>();
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                String currentLine;
                while ((currentLine = reader.readLine()) != null) {
                    md.add(currentLine);
                }
                algodata = Integer.parseInt(md.get(0));
            }
        }
    }
}
