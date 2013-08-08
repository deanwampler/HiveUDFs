package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;

@Description(name="per_record_ngrams",
    value="_FUNC_(n, text) - Return an array of all the n-gram phrases in the text.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(3, \"Now is the time for all good men\") FROM src LIMIT 1;\n"
        + "  [\"Now is the\", \"is the time\", ...]\n"
        + "Punctuation is treated as whitespace and extra whitespace is removed, including leading and trailing whitespace.\n")
public class PerRecordNGrams extends UDF {

    public ArrayList<String> evaluate(final int n, final String text) throws HiveException {
        if (n <= 0) {
            throw new HiveException(getClass().getSimpleName() + ": n == " + n + ". Must be > 0");
        }
        ArrayList<ArrayList<String>> ngrams = new PerRecordNGramsAsArrays().evaluate(n, text);
        return WordArrayArrayToStringArray.convert(ngrams);
    }
}
