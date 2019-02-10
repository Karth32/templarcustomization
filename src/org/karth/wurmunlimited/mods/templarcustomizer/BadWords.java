package org.karth.wurmunlimited.mods.templarcustomizer;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BadWords {
    static Map<String, String[]> words = new HashMap<>();
    static int largestWordLength = 0;
    private static Logger logger = Logger.getLogger("BadWords");

    public static void loadBadWordConfig() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1UMi1w_T593iuSi8q7QSTdlQkNJB6bEkeGKveOadS5sU/export?format=csv").openConnection().getInputStream()));
            String line = "";
            int counter = 0;
            while((line = reader.readLine()) != null) {
                counter++;
                String[] content;
                try {
                    content = line.split(",");
                    if(content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[]{};
                    if(content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if(word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
            logger.log(Level.INFO, "Filter loaded " + counter + " words");
        } catch (MalformedURLException e) {
            logger.log(Level.SEVERE, "Malformed URL in word filter. Word filtering will not work until fixed.");
            e.printStackTrace();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot reach filter URL, or some sort of disk error happened.");
            e.printStackTrace();
        }

    }


    public static boolean badWordCheck(String input) {
        if(input == null) {
            return false;
        }
        input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

        // iterate over each letter in the word
        for(int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for(int offset = 1; offset < (input.length()+1 - start) && offset < largestWordLength; offset++)  {
                String wordToCheck = input.substring(start, start + offset);
                if(words.containsKey(wordToCheck)) {
                    // for example, if you want to say the word bass, that should be possible.
                    String[] ignoreCheck = words.get(wordToCheck);
                    boolean ignore = false;
                    for(int s = 0; s < ignoreCheck.length; s++ ) {
                        if(input.contains(ignoreCheck[s])) {
                            ignore = true;
                            break;
                        }
                    }
                    if(!ignore) {
                        return true;
                    }
                }
            }
        }

        return false;

    }
}
