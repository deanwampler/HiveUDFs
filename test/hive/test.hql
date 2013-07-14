-- A Hive local-mode test.
-- Executed by hive/test.sh, which should be run from the project's root directory!

set hive.metastore.warehouse.dir=${system:user.dir}/test/hive/tmp/warehouse;

ADD JAR concurrentthought-hive-udfs.jar;

CREATE TEMPORARY FUNCTION per_record_ngrams           AS 'com.concurrentthought.hive.udfs.PerRecordNGrams';
CREATE TEMPORARY FUNCTION per_record_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays';

-- A test table to use for a test query:
DROP TABLE IF EXISTS src;
CREATE TABLE src(toss STRING);
LOAD DATA LOCAL INPATH 'test/hive/test.hql' INTO TABLE src;

-- Test the NGram function that returns an array of string phrases:
INSERT OVERWRITE LOCAL DIRECTORY 'test/hive/output/ngrams'
SELECT per_record_ngrams(3, "Now is the time for all good men") FROM src LIMIT 1;

-- Test the NGram function that returns an array of an array of word strings:
INSERT OVERWRITE LOCAL DIRECTORY 'test/hive/output/ngrams-arrays'
SELECT per_record_ngrams_as_arrays(3, "Now is the time for all good men") FROM src LIMIT 1;

-- Clean up.
DROP TABLE IF EXISTS src;
