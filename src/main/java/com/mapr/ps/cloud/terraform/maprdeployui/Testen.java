package com.mapr.ps.cloud.terraform.maprdeployui;

import java.io.*;

public class Testen {
    public static void maain(String[] args) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // -- Linux --

        // Run a shell command
//        processBuilder.directory(new File("/etc"));
        processBuilder.command("dir");
        processBuilder.command("timeout 5");
        processBuilder.command("dir");
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
        String line;
        StringBuilder output = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            System.out.println("Success!");
            System.out.println(output);
            System.exit(0);
        } else {
            //abnormal...
        }

    }
}
