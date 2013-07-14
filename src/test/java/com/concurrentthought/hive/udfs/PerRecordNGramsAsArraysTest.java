package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PerRecordNGramsAsArraysTest {

    private PerRecordNGramsAsArrays func = new PerRecordNGramsAsArrays();

    private ArrayList<String> toArray(String s) {
        ArrayList<String> a = new ArrayList<String>();
        for (String s2: s.split(" ")) {
            a.add(s2);
        }
        return a;
    }

    @Test
    public void emptyListReturnedForNullText() throws HiveException {
        assertEquals(new ArrayList<ArrayList<String>>(), func.evaluate(3, null));
    }

    @Test
    public void emptyListReturnedForEmptyText() throws HiveException {
        assertEquals(new ArrayList<ArrayList<String>>(), func.evaluate(3, ""));
    }

    @Test(expected = HiveException.class)
    public void throwsIfNIsNegative() throws HiveException {
        assertEquals(new ArrayList<String>(), func.evaluate(-1, null));
    }

    @Test(expected = HiveException.class)
    public void throwsIfNEqualsZero() throws HiveException {
        assertEquals(new ArrayList<String>(), func.evaluate(0, null));
    }

    @Test
    public void emptyListReturnedForTextWithFewerThanNWords() throws HiveException {
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        assertEquals(expected, func.evaluate(3, "Now is"));
    }

    @Test
    public void oneElementListReturnedForTextWithNWords() throws HiveException {
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        expected.add(toArray("now is the"));
        assertEquals(expected, func.evaluate(3, "Now is the"));
    }

    @Test
    public void manyElementListReturnedForTextWithMoreThanNWords() throws HiveException {
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        expected.add(toArray("now is the"));
        expected.add(toArray("is the time"));
        assertEquals(expected, func.evaluate(3, "Now is the time"));

        expected = new ArrayList<ArrayList<String>>();
        expected.add(toArray("now is the"));
        expected.add(toArray("is the time"));
        expected.add(toArray("the time for"));
        expected.add(toArray("time for all"));
        expected.add(toArray("for all good"));
        expected.add(toArray("all good men"));
        assertEquals(expected, func.evaluate(3, "Now is the time for all good men"));
    }

    @Test
    public void leadingAndTrailingWhitespaceIsIgnored() throws HiveException {
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        expected.add(toArray("now is the"));
        expected.add(toArray("is the time"));
        assertEquals(expected, func.evaluate(3, " \tNow is the time \t"));
    }

    @Test
    public void punctuationIsIgnored() throws HiveException {
        ArrayList<ArrayList<String>> expected = new ArrayList<ArrayList<String>>();
        expected.add(toArray("now is the"));
        expected.add(toArray("is the time"));
        assertEquals(expected, func.evaluate(3, "Now-is.the ! time ;"));
    }
}
