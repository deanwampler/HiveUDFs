#!/bin/bash
#----------------------------------------------
# test/hive/test.sh - Test using Hive running in local mode.

dir=$(dirname $0)
mkdir -p $dir/tmp/warehouse

hive -f $dir/test.hql
diff $dir/output/ngrams/000000_0 $dir/golden/ngrams/expected.txt
test $? -ne 0 && exit 1
diff $dir/output/ngrams-arrays/000000_0 $dir/golden/ngrams-arrays/expected.txt
test $? -ne 0 && exit 1

# Successful! Clean up...
rm -rf test/hive/tmp test/hive/output

exit 0