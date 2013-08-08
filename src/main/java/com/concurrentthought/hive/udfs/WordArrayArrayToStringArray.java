package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.exec.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;

/**
 * Helper class used to convert the results of the *AsArrays classes to
 * results returned by the "non-array" classes.
 */ 
class WordArrayArrayToStringArray {
    protected static ArrayList<String> convert(ArrayList<ArrayList<String>> ngramsArraysArray) {
        ArrayList<String> ngrams = new ArrayList<String>();
        if (ngramsArraysArray == null) 
            return ngrams;

        for (ArrayList<String> ngram: ngramsArraysArray) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ngram.size(); i++) {
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