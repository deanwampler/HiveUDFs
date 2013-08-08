package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.List;
import java.util.ArrayList;

@Description(name="per_record_context_ngrams",
    value="_FUNC_(text, array<string1, string2, ...>) - Return an array of all the n-gram phrases in the text that match the context pattern. Use nulls for blank placeholders in the pattern.",
    extended="Example:\n"
        + "  > SELECT _FUNC_(\"Time flies like an arrow. Fruit flies like a banana.\", array(null,\"flies\",\"like\",null,null)) FROM src LIMIT 1;\n"
        + "  [\"Time flies like an arrow\", \"Fruit flies like a banana\"]\n"
        + "Punctuation is treated as whitespace and extra whitespace is removed, including leading and trailing whitespace.\n")
public class PerRecordContextNGrams extends UDF {

    public ArrayList<String> evaluate(final String text, final List<String> ngramPattern) throws HiveException {
        ArrayList<ArrayList<String>> ngrams = new PerRecordContextNGramsAsArrays().evaluate(text, ngramPattern);
        return WordArrayArrayToStringArray.convert(ngrams);
    }
}
