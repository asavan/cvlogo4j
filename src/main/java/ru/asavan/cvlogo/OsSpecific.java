package ru.asavan.cvlogo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by asavan on 27.02.2022.
 */
public class OsSpecific {
    private OsSpecific() {}
    public static OsName chooseOs() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return OsName.WIN;
        }
        if (isUnix(OS)) {
            return OsName.LINUX;
        }
        return OsName.OTHER;
    }

    public static void writeOnDisk(String repo, String output, OsName osName) throws IOException {
        String scriptName = getFileName(repo, osName);
        save(output, scriptName);
        System.out.println(scriptName + " saved.");
    }

    private static boolean isUnix(String OS) {
        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);
    }
    private static void save(String output, String scriptName) throws IOException {
        Files.writeString(Paths.get(scriptName), output);
    }

    private static String getExtension(OsName os) {
        if (os == OsName.WIN) {
            return ".bat";
        }
        return ".sh";
    }

    private static String getFileName(String name, OsName os) {
        return name + getExtension(os);
    }
}
