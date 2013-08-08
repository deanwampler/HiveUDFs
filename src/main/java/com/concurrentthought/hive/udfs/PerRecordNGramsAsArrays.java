package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;

@Description(name="per_record_ngram_as_arrays",
    value="_FUNC_(n, text) - Return an array of all the n-grams in the text as arrays of words.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(3, \"Now is the time for all good men\") FROM src LIMIT 1;\n"
        + "  [[\"Now\", \"is\", \"the\"], [\"is\", \"the\", \"time\"], ...]\n"
        + "Extra whitespace is removed. Punctuation is treated as extra whitespace.\n")
public class PerRecordNGramsAsArrays extends UDF {

    public ArrayList<ArrayList<String>> evaluate(final int n, final String text) throws HiveException {
        if (n <= 0) {
            throw new HiveException(getClass().getSimpleName() + ": n == " + n + ". Must be > 0");
        }
        ArrayList<ArrayList<String>> ngrams = new ArrayList<ArrayList<String>>();
        if (text == null) {
            return ngrams;
        }
        String[] wordsArray = text.trim().split("[\\p{Space}\\p{Punct}]+");
        ArrayList<String> words = new ArrayList<String>();
        for (String word: wordsArray) {
            if (word.length() > 0)
                words.add(word);
        }
        for (int i = 0; i <= words.size() - n; i++) {
            ArrayList<String> array = new ArrayList<String>();
            for (int j = 0; j < n; j++) {
                array.add(words.get(i+j));
            }
            ngrams.add(array);   
        }
        return ngrams;
    }
}