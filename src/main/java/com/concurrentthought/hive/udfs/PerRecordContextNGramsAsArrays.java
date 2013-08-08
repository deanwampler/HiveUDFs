package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.List;
import java.util.ArrayList;

@Description(name="per_record_context_ngrams_as_arrays",
    value="_FUNC_(text, array<string1, string2, ...>) - Return an array of all the n-gram phrases in the text that match the context pattern as arrays of words. Use nulls for blank placeholders in the pattern.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(\"Time flies like an arrow. Fruit flies like a banana.\", array(null,\"flies\",\"like\",null,null)) FROM src LIMIT 1;\n"
        + "  [[\"Time\", \"flies\", \"like\", \"an\", \"arrow\"], [\"Fruit\", \"flies\", \"like\", \"a\", \"banana\"]]\n"
        + "Extra whitespace is removed. Punctuation is treated as extra whitespace.\n")
public class PerRecordContextNGramsAsArrays extends UDF {

    public ArrayList<ArrayList<String>> evaluate(final String text, final List<String> ngramPattern) throws HiveException {
        if (ngramPattern == null || ngramPattern.size() <= 0) {
            throw new HiveException(getClass().getSimpleName() + ": Input n-gram pattern is empty.");
        }
        if (text == null) {
            return new ArrayList<ArrayList<String>>();
        }

        ArrayList<ArrayList<String>> allNgrams = new PerRecordNGramsAsArrays().evaluate(ngramPattern.size(), text);
        return filter(allNgrams, ngramPattern);
    }

    private ArrayList<ArrayList<String>> filter(ArrayList<ArrayList<String>> allNgrams, List<String> ngramPattern) {
        ArrayList<ArrayList<String>> filtered = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> ngram: allNgrams) {
            // System.out.printf("\n  <");
            // for (String word: ngram) {
            //     System.out.printf("%s ",word);
            // }
            // System.out.printf(">");
            if (keepNGram(ngram, ngramPattern)) {
                filtered.add(ngram);
                // System.out.printf(" - keep (pattern: %s)\n", ngramPattern);
            // } else {
            //     System.out.printf(" - discard (pattern: %s)\n", ngramPattern);

            }

        }
        return filtered;
    }

    private boolean keepNGram(ArrayList<String> ngram, final List<String> ngramPattern) {
        for (int i=0; i < ngramPattern.size(); i++) {
            String word = ngramPattern.get(i);
            // System.out.printf("word = <%s>, pattern = <%s>\n", ngram.get(i), word);
            if (word != null && ! word.equals(ngram.get(i))) {
                return false;
            }
        }
        return true;
    }
}
