package com.concurrentthought.hive.udfs;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import java.util.List;
import java.util.ArrayList;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PerRecordContextNGramsTest {

    private PerRecordContextNGrams func = new PerRecordContextNGrams();

    private ArrayList<String> toArray(String s) {
        ArrayList<String> a = new ArrayList<String>();
        for (String s2: s.split(" ")) {
            a.add(s2);
        }
        return a;
    }

    private static ArrayList<String> empty = new ArrayList<String>();
    private static List<String> emptyPattern = new ArrayList<String>();
    private static List<String> fliesPattern = new ArrayList<String>();

    @BeforeClass
    public static void setup() {
        fliesPattern.add(null);
        fliesPattern.add("flies");
        fliesPattern.add("like");
        fliesPattern.add(null);
        fliesPattern.add(null);
    }

    @Test
    public void emptyListReturnedForNullText() throws HiveException {
        assertEquals(empty, func.evaluate(null, fliesPattern));
    }

    @Test
    public void emptyListReturnedForEmptyText() throws HiveException {
        assertEquals(empty, func.evaluate("", fliesPattern));
    }

    @Test(expected = HiveException.class)
    public void throwsIfPatternIsNull() throws HiveException {
        func.evaluate("foo", null);
    }

    @Test(expected = HiveException.class)
    public void throwsIfPatternIsEmpty() throws HiveException {
        func.evaluate("foo", empty);
    }

    @Test
    public void emptyListReturnedForTextWithFewerThanNWords() throws HiveException {
        assertEquals(empty, func.evaluate("Time flies like an", fliesPattern));
    }

    @Test
    public void oneElementListReturnedForMatchingTextWithNWords() throws HiveException {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Time flies like an arrow");
        assertEquals(expected, func.evaluate("Time flies like an arrow", fliesPattern));
    }

    @Test
    public void emptyListReturnedForNonmatchingTextWithNWords() throws HiveException {
        assertEquals(empty, func.evaluate("Time flies with an arrow", fliesPattern));
    }

    @Test
    public void manyElementListReturnedForMatchingTextWithMoreThanNWords() throws HiveException {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Time flies like an arrow");
        expected.add("Fruit flies like a banana");
        assertEquals(expected, func.evaluate("Time flies like an arrow. Fruit flies like a banana.", fliesPattern));
    }

    @Test
    public void leadingAndTrailingWhitespaceIsIgnored() throws HiveException {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Time flies like an arrow");
        assertEquals(expected, func.evaluate(" Time\tflies  like \t an    arrow\t", fliesPattern));
    }

    @Test
    public void punctuationIsTreatedAsWhitespace() throws HiveException {
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Time flies like an arrow");
        assertEquals(expected, func.evaluate("!Time-flies.like,an#arrow", fliesPattern));
    }
}
