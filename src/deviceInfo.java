import java.io.*;
import java.util.*;
public class deviceInfo {
    static String systemDrive;
    static String deviceNameInfo, osInfo, fileSeperator,basePath;
    static int algodata;
    public static void main() throws Exception{
        deviceNameInfo = System.getProperty("user.name");
        osInfo = System.getProperty("os.name");
        fileSeperator = System.getProperty("file.seperator");
        if(osInfo.contains("Windows")){
            systemDrive = System.getenv("SystemDrive");
        }
        basePath=systemDrive+"\\users\\"+deviceNameInfo+"\\EncyphrVault";
        File file = new File(basePath+"\\cryptdata.eph");
        ArrayList<String> md = new ArrayList<>();
        if(file.exists()){
            BufferedReader reader = new BufferedReader(new FileReader(file.getAbsolutePath()));
            String currentLine;
            while((currentLine = reader.readLine()) != null){
                md.add(currentLine);
            }
            algodata = Integer.parseInt(md.get(0));
        }
    }
}
