package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;

@Description(name="per_record_ngrams",
    value="_FUNC_(n, text) - Return an array of all the n-gram phrases in the text.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(3, \"now is the time for all good men\") FROM src LIMIT 1;\n"
        + "  [[\"now is the\"], [\"is the time\"], ...]\n"
        + "Extra whitespace and punctuation are removed.\n")
public class PerRecordNGrams extends UDF {

    public ArrayList<String> evaluate(final int n, final String text) throws HiveException {
        if (n <= 0) {
            throw new HiveException(getClass().getSimpleName() + "n == " + n + ". Must be > 0");
        }
        ArrayList<ArrayList<String>> ngrams1 = new PerRecordNGramsAsArrays().evaluate(n, text);
        ArrayList<String> ngrams = new ArrayList<String>();
        for (ArrayList<String> ngram: ngrams1) {
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<n; i++) {
                if (i > 0) {
                    sb.append(" ");
                }
                sb.append(ngram.get(i));
            }
            ngrams.add(sb.toString());
        }
        return ngrams;
    }
}
