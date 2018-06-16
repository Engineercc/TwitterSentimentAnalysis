package com.zemberek.util;

import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class wordAnalysis {

    private final Set<String> stopwords = new HashSet<>();
    private final Set<String> negativeWords = new HashSet<String>();
    private final Set<String> positiveWords = new HashSet<String>();

    public int countNegative;
    public int countPositive;

    public wordAnalysis(String word){

        countNegative = 0;
        countPositive = 0;

        TurkishMorphology morphology = null;
        try {
            morphology = TurkishMorphology.createWithDefaults();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String strWord = word.toLowerCase();
        StringBuilder sb = new StringBuilder();

        loadWord();

        for (String wrd : strWord.split("\\s+")) {
            if (!isStopWords(wrd)) {
                sb.append(wrd).append(" ");
            }
        }

        for (String s : sb.toString().trim().split("\\s+")) {
            List<WordAnalysis> result = morphology.analyze(s);
            boolean isNegative = isNegativeWords(s);
            boolean isPositive = isPositiveWords(s);
            for (WordAnalysis w : result) {
                if (w.formatOnlyIgs().contains("Pos") || w.formatOnlyIgs().contains("Neg")) {
                    if (!isNegative || !isPositive) {
                        emotion(w.formatOnlyIgs());
                    }
                    break;
                }
            }
        }
    }

    public wordAnalysis(){

    }

    public void emotion(String s) {
        if (s.contains("Neg")) {
            countNegative++;
        }
        if (s.contains("Pos")) {
            countPositive++;
        }
    }

    public boolean isNegativeWords(String s) {

        boolean bol = false;
        for (String str : negativeWords) {
            if (s.contains(str)) {
                bol = true;
                countNegative++;
            }
        }
        return bol;
    }

    public boolean isPositiveWords(String s) {

        boolean bol = false;
        for (String str : positiveWords) {
            if (s.contains(str)) {
                bol = true;
                countPositive++;
            }
        }
        return bol;
    }

    public boolean isStopWords(String s) {
        boolean bol = false;
        for (String str : stopwords) {
            if (s.equals(str)) {
                bol = true;
            }
        }
        return bol;
    }

    public void loadWord(){

        wordAnalysis m = new wordAnalysis();

        try {
            File stop_words_file = new File("C:\\Users\\Er-Ryan\\Desktop\\Senior Project\\stop_words.txt");
            InputStream stop_words_resource = new FileInputStream(stop_words_file);
            m.readWords(stop_words_resource,
                    Charset.forName("UTF-8"), stopwords);

            //Negative Words Loading
            File negative_words_file = new File("C:\\Users\\Er-Ryan\\Desktop\\Senior Project\\negative_words.txt");
            InputStream negative_words_resource = new FileInputStream(negative_words_file);
            m.readWords(negative_words_resource,
                    Charset.forName("UTF-8"), negativeWords);

            //Positive Words Loading
            File positive_words_file = new File("C:\\Users\\Er-Ryan\\Desktop\\Senior Project\\positive_words.txt");
            InputStream positive_words_resource = new FileInputStream(positive_words_file);
            m.readWords(positive_words_resource,
                    Charset.forName("UTF-8"), positiveWords);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void readWords(final InputStream inputStream, final Charset encoding, Set<String> set) {
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(inputStream,
                    encoding));
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.replaceAll("\\|.*", "").trim();
                    if (line.length() == 0) {
                        continue;
                    }
                    for (final String w : line.split("\\s+")) {

                        set.add(w.toLowerCase(Locale.ENGLISH));
                    }
                }
            } finally {
                in.close();
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
