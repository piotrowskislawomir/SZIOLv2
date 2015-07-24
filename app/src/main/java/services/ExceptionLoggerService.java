package services;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Slawek on 2015-06-06.
 */
public class ExceptionLoggerService {

    public void writefile(String fileName, String content) {
        File externalStorageDir = Environment.getExternalStorageDirectory();
        File myFile = new File(externalStorageDir, fileName);

        try {
            if (!myFile.exists()) {
                myFile.createNewFile();
            }

            FileOutputStream fostream = new FileOutputStream(myFile);
            OutputStreamWriter oswriter = new OutputStreamWriter(fostream);
            BufferedWriter bwriter = new BufferedWriter(oswriter);
            bwriter.write(content);
            bwriter.newLine();
            bwriter.close();
            oswriter.close();
            fostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

