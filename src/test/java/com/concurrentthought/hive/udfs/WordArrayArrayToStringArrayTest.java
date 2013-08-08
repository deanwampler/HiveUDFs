package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WordArrayArrayToStringArrayTest {

    private ArrayList<String> toArray(String s) {
        ArrayList<String> a = new ArrayList<String>();
        for (String s2: s.split(" ")) {
            a.add(s2);
        }
        return a;
    }

    private ArrayList<ArrayList<String>> empty = new ArrayList<ArrayList<String>>();

    @Test
    public void emptyListReturnedForNullArray() {
        assertEquals(new ArrayList<String>(), WordArrayArrayToStringArray.convert(null));
    }

    @Test
    public void emptyListReturnedForEmptyArray() {
        assertEquals(new ArrayList<String>(), WordArrayArrayToStringArray.convert(empty));
    }

    @Test
    public void arrayOfOneArrayOfWordsConvertedToArrayOfOneString() throws HiveException {
        ArrayList<ArrayList<String>> input = new ArrayList<ArrayList<String>>();
        input.add(toArray("now is the time"));
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("now is the time");
        assertEquals(expected, WordArrayArrayToStringArray.convert(input));
    }

    @Test
    public void arrayOfSeveralArraysOfWordsConvertedToArrayOfSeveralStrings() throws HiveException {
        ArrayList<ArrayList<String>> input = new ArrayList<ArrayList<String>>();
        input.add(toArray("now is the time"));
        input.add(toArray("for all good men"));
        input.add(toArray("to come to the aid of their country"));
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("now is the time");
        expected.add("for all good men");
        expected.add("to come to the aid of their country");
        assertEquals(expected, WordArrayArrayToStringArray.convert(input));
    }
}