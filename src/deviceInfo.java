public class deviceInfo {
    static String systemDrive;
    static String deviceNameInfo, osInfo, fileSeperator,basePath;
    public static void main(){
        deviceNameInfo = System.getProperty("user.name");
        osInfo = System.getProperty("os.name");
        fileSeperator = System.getProperty("file.seperator");
        if(osInfo.contains("Windows")){
            systemDrive = System.getenv("SystemDrive");
        }
        basePath=systemDrive+"\\users\\"+deviceNameInfo+"\\EncyphrVault";
    }
}
