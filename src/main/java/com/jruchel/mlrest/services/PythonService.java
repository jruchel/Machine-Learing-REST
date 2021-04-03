package com.jruchel.mlrest.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class PythonService {

    private final String resourcesPath = "src/main/resources/scripts/%s.py";

    public String runScript(String name, String... args) throws IOException {

        Process p = Runtime.getRuntime().exec(createCommand(name, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

        String line = "";
        StringBuilder result = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }

        return result.toString();
    }

    private String createCommand(String name, String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("python ").append(String.format(resourcesPath, name)).append(" ");

        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        return sb.toString();
    }

}
