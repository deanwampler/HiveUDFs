#!/bin/bash
#----------------------------------------------
# test/hive/test.sh - Test using Hive running in local mode.

version=$1
if [[ -z $version ]]
then
    echo "Must pass the version string to this script, e.g., 'hive.sh 0.1.0'"
    exit 1
fi

# Does metastore_db already exist in this directory? It shouldn't, but if it does, don't delete it later!
test -d metastore_db || delete_metastore_db="yes"

dir=$(dirname $0)
mkdir -p $dir/tmp/warehouse

hive -d VERSION=$version -f $dir/test.hql
for golden in $dir/golden/*
do
    output=${golden/golden/output}
    echo "Checking $golden vs $output:"
    diff $output/000000_0 $golden/expected.txt
    test $? -ne 0 && exit 1
done

# Successful! Clean up...
rm -rf test/hive/tmp test/hive/output 
test -n "$delete_metastore_db" && rm -rf metastore_db

exit 0