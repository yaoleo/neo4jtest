package utils.io;

import baike.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @Description:
 * @Author: J.Y.Zhang
 * @Date: 2018/1/13
 */
public class IOTool {

    private final static Logger log = LoggerFactory.getLogger(Application.class);

    public static ArrayList<String> readLinesFromFile(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
        String line = "";
        ArrayList<String> list = new ArrayList<>();
        while ((line=br.readLine())!=null){
            line = line.trim();
            if(line!=null && line!="")
                list.add(line);
        }
        br.close();

        return list;
    }
}
