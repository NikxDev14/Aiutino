package org.example.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static String PATH_ENV = ".env";
    private static Map<String,String> env = new HashMap<>();

    public static void load() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(PATH_ENV));
        String line;

        while ((line = br.readLine()) != null){
            if (line.startsWith("#") || line.trim().isEmpty()) continue;

            String[] split = line.split("=",2);
            env.put(split[0].trim(),split[1].trim());
        }

        br.close();
    }

    public static String getEnv(String key){
        return env.get(key);
    }
}
