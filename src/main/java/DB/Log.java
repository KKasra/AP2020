package DB;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;

public class Log {
    private File source;
    private FileWriter writer;

    public Log(String path, boolean append) {
        source = new File(path);
        try {
            source.createNewFile();
            writer = new FileWriter(source, append);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String string) {
        System.out.println(string);
        try {
            writer.append(string + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        flushWriter();
    }

    public void writeEvent(String event, String description){
        write(event + " " + new Timestamp(System.currentTimeMillis()) + " " + description);
    }

    public void addDeleteHeader(){
        ArrayList<String> res = new ArrayList<>();
        try {
            Scanner read = new Scanner(source);
            while (read.hasNextLine())
                res.add(read.nextLine());
            res.add(2, "DELETED_AT: " + new Timestamp(System.currentTimeMillis()));
            writer = new FileWriter(source, false);
            for (String line : res)
                write(line);
            flushWriter();

        } catch (Exception e) {
            System.err.println("log file not found,");
            e.printStackTrace();
        }

    }
    public void flushWriter() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
