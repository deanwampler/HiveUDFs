# README for Concurrent Thought Hive UDFs

[Dean Wampler](dean@concurrentthought.com)

This project is a collection of Hive UDFs. 

Documentation is defined in the functions, so `DESCRIBE FUNCTION ...` works as it should.

## `per_record_ngrams(n, text)`

(Java class: `com.concurrentthought.hive.udfs.PerRecordNGrams`)

Returns an array containing the `n` NGram phrases in `text`. The value of `n` must be a positive integer. The `text` can be empty or `null`, in which case an empty array is returned.

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs.jar;

    CREATE TEMPORARY FUNCTION per_record_ngrams AS 'com.concurrentthought.hive.udfs.PerRecordNGrams';

    SELECT per_record_ngrams(3, "Now is the time for all good men") FROM src LIMIT 1;
    > ["now is the","is the time","the time for","time for all","for all good","all good men"]

## `per_record_ngrams_as_arrays(n, text)` 

(Java class: `com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays`)

Returns an array containing the `n` NGrams in `text` as nested arrays of words. The value of `n` must be a positive integer. The `text` can be empty or `null`, in which case an empty array is returned.

Here is an example (see also `test/hive/test.hql`):

    ADD JAR /path/to/concurrentthought-hive-udfs.jar;

    CREATE TEMPORARY FUNCTION per_record_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays';

    SELECT per_record_ngrams_as_arrays(3, "Now is the time for all good men") FROM src LIMIT 1;
    > [["now","is","the"],["is","the","time"],["the","time","for"],["time","for","all"],["for","all","good"],["all","good","men"]]


## Building the Code

An `ant` build file is included. It looks for the required Hive and Hadoop jars using the two environment variables, with default values:

| Name          | Default Value     |
| :------------ | :---------------- |
| `HADOOP_HOME` | `/usr/lib/hadoop` |
| `HIVE_HOME`   | `/usr/lib/hadoop` |

Otherwise, you can override them on the commands line, e.g., 

    HADOOP_HOME=/path/to/lib/hadoop HIVE_HOME=/path/to/lib/hive ant

If you're building on a development workstation, just download the appropriate Hive and Hadoop distributions and put them somewhere convenient.

If `ant` is invoked without a specific target, `clean`, `compile`, `jar`, and `test` are built. The jar file is named `concurrentthought-hive-udfs.jar` and the `test` target runs unit tests.

There is also a `test-hive` target that is not executed by default. It runs a simple Hive script to test the functions. It *assumes* you have Hadoop and Hive configured for *local mode* on your build machine, as it temporarily defines the `hive.metastore.warehouse.dir` setting to `${system:user.dir}/test/hive/tmp/warehouse`, where `${system:user.dir}` is this working directory. If you remove this line in the script `test/hive/test.hql`, the script should work for non-local mode installations. Note that the `test-hive` target actually invokes a driver shell script, `test/hive/test.sh`.

