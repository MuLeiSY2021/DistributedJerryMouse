package pers.jerrymouse.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Utils {
    public static void writeStringToFile(String content, File file) throws IOException {
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.getName().equals("null") && !parent.exists()) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }
        FileWriter writer = null;
        writer = new FileWriter(file, true);
        writer.write(content);
        writer.flush();
        writer.close();
    }
}
