package com.hujiang.blue.service;

import com.hujiang.blue.vo.SpellCheckVo;
import org.assertj.core.util.Lists;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class SpellCheckService {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private Map<String, Double> langModel;
    private Set<String> dictionary;

    private BritishEnglish britishEnglish;

    @PostConstruct
    public void init() throws IOException {
        langModel = buildLanguageModel("/Users/xujia/big.txt");
        dictionary = langModel.keySet();
        britishEnglish = new BritishEnglish();
    }

    private Map<String, Double> buildLanguageModel(String sample)
            throws IOException {
        Map<String, Double> langModel = new HashMap<String, Double>();
        BufferedReader reader = new BufferedReader(new FileReader(sample));
        Pattern pattern = Pattern.compile("[a-zA-Z]+");
        String line;
        int totalCnt = 0;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(" ");
            for (String word : words) {
                if (pattern.matcher(word).matches()) {
                    word = word.toLowerCase();
                    langModel.merge(word, 1D, (a, b) -> a + b);
                    totalCnt++;
                }
            }
        }
        reader.close();

        for (Map.Entry<String, Double> entry : langModel.entrySet())
            entry.setValue(entry.getValue() / totalCnt);

        return langModel;
    }


    public List<String> wordSpellCheck(String words) {
        words = words.trim().toLowerCase();
        if ("bye".equals(words))
            return null;
        if (dictionary.contains(words))
            return null;

        // 3.Build set for word in edit distance and remove inexistent in dictionary
        Set<String> wordsInEditDistance = buildEditDistance1Set(langModel, words);
        wordsInEditDistance.retainAll(dictionary);
        if (wordsInEditDistance.isEmpty()) {
            wordsInEditDistance = buildEditDistance2Set(langModel, words);
            wordsInEditDistance.retainAll(dictionary);
            if (wordsInEditDistance.isEmpty()) {
                System.out.println("Failed to check this spell");
                return null;
            }
        }

        // 4.Calculate Bayes's probability
        // c - correct word we guess, w - wrong word user input in reality
        // argmax P(c|w) = argmax P(w|c) * P(c) / P(w)
        // we ignore P(w) here, because it's the same for all words
        return guessCorrectWord(langModel, wordsInEditDistance);
    }

    private Set<String> buildEditDistance1Set(
            Map<String, Double> langModel,
            String input) {
        Set<String> wordsInEditDistance = new HashSet<String>();
        char[] characters = input.toCharArray();

        // Deletion: delete letter[i]
        for (int i = 0; i < input.length(); i++)
            wordsInEditDistance.add(input.substring(0,i) + input.substring(i+1));

        // Transposition: swap letter[i] and letter[i+1]
        for (int i = 0; i < input.length()-1; i++)
            wordsInEditDistance.add(input.substring(0,i) + characters[i+1] +
                    characters[i] + input.substring(i+2));

        // Alteration: change letter[i] to a-z
        for (int i = 0; i < input.length(); i++)
            for (char c : ALPHABET)
                wordsInEditDistance.add(input.substring(0,i) + c + input.substring(i+1));

        // Insertion: insert new letter a-z
        for (int i = 0; i < input.length()+1; i++)
            for (char c : ALPHABET)
                wordsInEditDistance.add(input.substring(0,i) + c + input.substring(i));

        return wordsInEditDistance;
    }

    private Set<String> buildEditDistance2Set(
            Map<String, Double> langModel,
            String input) {
        Set<String> wordsInEditDistance1 = buildEditDistance1Set(langModel, input);
        Set<String> wordsInEditDistance2 = new HashSet<String>();
        for (String editDistance1 : wordsInEditDistance1)
            wordsInEditDistance2.addAll(buildEditDistance1Set(langModel, editDistance1));
        wordsInEditDistance2.addAll(wordsInEditDistance1);
        return wordsInEditDistance2;
    }

    private List<String> guessCorrectWord(
            final Map<String, Double> langModel,
            Set<String> wordsInEditDistance) {
        List<String> words = new LinkedList<String>(wordsInEditDistance);
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String word1, String word2) {
                return langModel.get(word2).compareTo(langModel.get(word1));
            }
        });
        return words.size() > 5 ? words.subList(0, 5) : words;
    }


    public List<SpellCheckVo> getSpellError(String sentence) {

        JLanguageTool langTool = new JLanguageTool(britishEnglish);
        // comment in to use statistical ngram data:
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        try {
            List<RuleMatch> ruleMatches = langTool.check(sentence);
            if(ruleMatches.size() > 0) {
                List<SpellCheckVo> spellCheckVos = Lists.newArrayList();
                ruleMatches.stream().forEach(
                        r -> {
                            SpellCheckVo spellCheckVo = new SpellCheckVo(r.getFromPos(), r.getToPos(), r.getShortMessage(), r.getSuggestedReplacements(), r.getMessage());
                            spellCheckVos.add(spellCheckVo);
                        }
                );
                return spellCheckVos;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
