package riddlewave;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class QuestionManager {

    public static List<Question> loadQuestions(String province) {
        List<Question> questions = new ArrayList<>();
        String filePath = "resources/data/" + province + "/soal.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirst = true;

            while ((line = br.readLine()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue; // Skip header
                }
                String[] parts = line.split(",", -1);
                if (parts.length >= 6) {
                    String text = parts[0];
                    String[] options = { parts[1], parts[2], parts[3], parts[4] };
                    String correct = parts[5].trim().toUpperCase();
                    questions.add(new Question(text, options, correct));
                }
            }

        } catch (IOException e) {
            System.out.println("Gagal membaca soal untuk provinsi " + province + ": " + e.getMessage());
        }

        return questions;
    }

    public static String loadNarration(String province) {
        String filePath = "resources/data/" + province + "/narasi.txt";
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println("Gagal membaca narasi untuk provinsi " + province + ": " + e.getMessage());
            return "";
        }
    }
}
