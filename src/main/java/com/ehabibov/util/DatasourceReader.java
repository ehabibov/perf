package com.ehabibov.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasourceReader {

    private static final Logger log = LoggerFactory.getLogger(DatasourceReader.class);

    public static Map<String, String> toMap(String datasource){
        File file = new File(datasource);
        Map<String, String> map = new HashMap<>();

        try {
            Scanner sc = new Scanner(file);
            if (file.canRead()){
                while (sc.hasNextLine()){
                    String[] pair = sc.nextLine().split("=");
                    map.put(pair[0], pair[1]);
                }
            } else {
                log.error(String.format("Datasource file can not be read: %s/%s)", datasource, file.getName()));
            }
        } catch (FileNotFoundException e) {
            log.error(String.format("Datasource file not found in the following path: %s/%s", datasource, file.getName()));
            e.printStackTrace();
        }
        return map;
    }
}
