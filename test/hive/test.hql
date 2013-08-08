-- A Hive local-mode test.
-- Executed by hive/test.sh, which should be run from the project's root directory!

set hive.metastore.warehouse.dir=${system:user.dir}/test/hive/tmp/warehouse;
set hive.metastore.warehouse.dir;

set hive.exec.scratchdir=${system:user.dir}/test/hive/tmp/scratchdir;
set hive.exec.scratchdir;

ADD JAR concurrentthought-hive-udfs-${VERSION}.jar;

CREATE TEMPORARY FUNCTION per_record_ngrams           AS 'com.concurrentthought.hive.udfs.PerRecordNGrams';
CREATE TEMPORARY FUNCTION per_record_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordNGramsAsArrays';

CREATE TEMPORARY FUNCTION per_record_context_ngrams           AS 'com.concurrentthought.hive.udfs.PerRecordContextNGrams';
CREATE TEMPORARY FUNCTION per_record_context_ngrams_as_arrays AS 'com.concurrentthought.hive.udfs.PerRecordContextNGramsAsArrays';

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

-- Test the Context NGram function that returns an array of string phrases:
INSERT OVERWRITE LOCAL DIRECTORY 'test/hive/output/context-ngrams'
SELECT per_record_context_ngrams("Time flies like an arrow. Fruit flies like a banana.", array(null, "flies", "like", null, null)) FROM src LIMIT 1;

-- Test the Context NGram function that returns an array of an array of word strings:
INSERT OVERWRITE LOCAL DIRECTORY 'test/hive/output/context-ngrams-arrays'
SELECT per_record_context_ngrams_as_arrays("Time flies like an arrow. Fruit flies like a banana.", array(null, "flies", "like", null, null)) FROM src LIMIT 1;

-- Clean up.
DROP TABLE IF EXISTS src;
