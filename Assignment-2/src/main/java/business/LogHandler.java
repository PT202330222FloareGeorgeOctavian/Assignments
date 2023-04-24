package business;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogHandler {
    private FileWriter fr;
    public LogHandler(File file){
        try {
            fr = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            overWrite();
        }
    }

    public void overWrite(){
        try {
            fr.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertLogs(String data){
        try{
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            if (fr != null) {
                fr.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
