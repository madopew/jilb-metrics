package main;

import jilbMetrics.JilbMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        final String fileName = "./res/Test.kt";
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(fileName)))) {
            String s = br.readLine();
            while(s != null) {
                sb.append(s).append("\n");
                s = br.readLine();
            }
        } catch (Exception e) {
            System.err.println("File error");
        }
        //Lexer l = new Lexer(sb.toString());
        //System.out.println(l);
        JilbMetrics jm = new JilbMetrics(sb.toString());
        System.out.printf("Absolute: %d\nRelative: %.3f (Total amount: %d)\nNesting: %d\n",
                jm.getAbsoluteDifficulty(),
                jm.getRelativeDifficulty(),
                jm.getTotalOperatorAmount(),
                jm.getMaxNestingLevel());
    }
}
