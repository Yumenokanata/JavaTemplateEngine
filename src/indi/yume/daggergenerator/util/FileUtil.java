package indi.yume.daggergenerator.util;

import java.io.*;

/**
 * Created by yume on 2015/9/27.
 */
public class FileUtil {
    public static File newFile(String... paths){
        StringBuilder p = new StringBuilder();
        if(paths.length > 0)
            p.append(paths[0]);

        if(paths.length > 1)
            for(int i = 1; i < paths.length; i++)
                p.append(File.separator)
                        .append(paths[i]);

        return new File(p.toString());
    }

    public static void writeToFile(String string, File targetFile){
        if(!targetFile.exists()){
            try {
                targetFile.getParentFile().mkdirs();
                targetFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fs = new FileOutputStream(targetFile);
            PrintStream p = new PrintStream(fs);
            p.print(string);
            p.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
