# README for Concurrent Thought Hive UDFs

[Dean Wampler](dean@concurrentthought.com)

V0.1.X - First release

This project is a collection of Hive UDFs. 

Documentation is defined in the functions, so `DESCRIBE FUNCTION ...` works as it should.

## Per-record NGram Functions

The following functions are analogous to Hive's built-in `ngrams` and `context_ngrams` functions, but they operate on single text fields. Hence, they are per-record UDFs, not UDAFs.

For all these functions, if the `text` is empty or `null` an empty array of resulting NGrams is returned. Punctuation in the input string is treated as whitespace and extra whitespace is removed, including leading and trailing whitespace. Case is **not** ignored, so call `lower(text)` first, for example, first.

### `per_record_ngrams(n, text)`

(Java class: `com.concurrentthought.hive.udfs.PerRecordNGrams`)

Returns an array containing the `n` NGram phrases in `text`. The value of `n` must be a positive integer or an exception is thrown. 

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs-X.Y.Z.jar;

    CREATE TEMPORARY FUNCTION per_record_ngrams AS 'com.concurrentthought.hive.udfs.PerRecordNGrams';

    SELECT per_record_ngrams(3, "Now is the time for all good men") FROM src LIMIT 1;
    > ["Now is the","is the time","the time for","time for all","for all good","all good men"]

### `per_record_ngrams_as_arrays(n, text)` 

(Java class: `com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays`)

Returns an array containing the `n` NGrams in `text` as nested arrays of words. The value of `n` must be a positive integer or an exception is thrown.

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs-X.Y.Z.jar;

    CREATE TEMPORARY FUNCTION per_record_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays';

    SELECT per_record_ngrams_as_arrays(3, "Now is the time for all good men") FROM src LIMIT 1;
    > [["Now","is","the"],["is","the","time"],["the","time","for"],["time","for","all"],["for","all","good"],["all","good","men"]]

### `per_record_context_ngrams(text, array(word1, word2, ...))`

(Java class: `com.concurrentthought.hive.udfs.PerRecordContextNGrams`)

Returns an array containing the context NGram phrases in `text` that match the context pattern given by the second array argument. The array of words to match must not be empty or an exception is thrown. Any word in the array equal to `null` will match any word.

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs-X.Y.Z.jar;

    CREATE TEMPORARY FUNCTION per_record_context_ngrams AS 'com.concurrentthought.hive.udfs.PerRecordContextNGrams';

    SELECT per_record_context_ngrams("Time flies like an arrow. Fruit flies like a banana.", array(null, "flies", "like", null, null)) FROM src LIMIT 1;
    > ["Time flies like an arrow","Fruit flies like a banana"]

## `per_record_ngrams_as_arrays(n, text)` 

(Java class: `com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays`)

Returns an array containing the context NGram phrases, as nested arrays of words, in `text` that match the context pattern given by the second array argument. The array of words to match must not be empty or an exception is thrown. Any word in the array equal to `null` will match any word.

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs-X.Y.Z.jar;

    CREATE TEMPORARY FUNCTION per_record_context_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordContextNGramsAsArrays';

    SELECT per_record_context_ngrams_as_arrays("Now is the time for all good men", array(null, "flies", "like", null, null)) FROM src LIMIT 1;
    > [["Time","flies","like","an","arrow"],["Fruit","flies","like","a","banana"]]


## Building the Code

An `ant` build file is included. It looks for the required Hive and Hadoop jars using the two environment variables, with default values:

| Name          | Default Value     |
| :------------ | :---------------- |
| `HADOOP_HOME` | `/usr/lib/hadoop` |
| `HIVE_HOME`   | `/usr/lib/hadoop` |

Otherwise, you can override them on the commands line, e.g., 

    HADOOP_HOME=/path/to/lib/hadoop HIVE_HOME=/path/to/lib/hive ant

If you're building on a development workstation, just download the appropriate Hive and Hadoop distributions and put them somewhere convenient.

If `ant` is invoked without a specific target, `clean`, `compile`, `jar`, and `test` are built. The jar file is named `concurrentthought-hive-udfs-X.Y.Z.jar` (for a specified `X.Y.Z` version in the `build.xml` file) and the `test` target runs unit tests.

There is also a `test-hive` target that is not executed by default. It runs a simple Hive script to test the functions. It *assumes* you have Hadoop and Hive configured for *local mode* on your build machine, as it temporarily defines the `hive.metastore.warehouse.dir` setting to `${system:user.dir}/test/hive/tmp/warehouse`, where `${system:user.dir}` is this working directory. If you remove this line in the script `test/hive/test.hql`, the script should work for non-local mode installations. Note that the `test-hive` target actually invokes a driver shell script, `test/hive/test.sh`. Also, it will only run on Hive v0.10.0 or later, because it embeds comments and it uses embedded variable expansions that don't work with early versions of Hive.

## Supported Hive Versions

The code builds and the unit tests pass for Hive v0.7.1, v0.8.0, v0.9.0, v0.10.0, and v0.11.0. However, it has only been tested with Hive v0.11.0. Please submit patches if it doesn't work with earlier releases!
