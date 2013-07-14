package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;

@Description(name="per_record_ngram_as_arrays",
    value="_FUNC_(n, text) - Return an array of all the n-grams in the text as arrays of words.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(3, \"now is the time for all good men\") FROM src LIMIT 1;\n"
        + "  [[\"now\", \"is\", \"the\"], [\"is\", \"the\", \"time\"], ...]\n"
        + "Extra whitespace and punctuation are removed.\n")
public class PerRecordNGramsAsArrays extends UDF {

    public ArrayList<ArrayList<String>> evaluate(final int n, final String text) throws HiveException {
        if (n <= 0) {
            throw new HiveException(getClass().getSimpleName() + "n == " + n + ". Must be > 0");
        }
        ArrayList<ArrayList<String>> ngrams = new ArrayList<ArrayList<String>>();
        if (text == null)
            return ngrams;
        String[] words = text.toString().trim().toLowerCase().split("[\\p{Space}\\p{Punct}]+");
        for (int i=0; i <= words.length - n; i++) {
            ArrayList<String> array = new ArrayList<String>();
            ngrams.add(array);   
            for (int j=i; j<i+n; j++) {
                array.add(words[j]);
            }
        }
        return ngrams;
    }
}