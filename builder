#!/bin/bash

rm -rf src/main/resources/lib/
mkdir -p src/main/resources/lib/

echo "building hello lib"
gcc -fPIC -shared -o src/main/resources/lib/libhello.so src/main/c/hello.c

echo 'building sort lib'
gcc -fPIC -shared -o src/main/resources/lib/libsort.so src/main/c/sort.c

echo 'building test lib'
gcc -fPIC -shared -o src/main/resources/lib/libcounter.so src/main/c/counter.c

echo "building gocalc lib"
go build -o src/main/resources/lib/libgocalc.so -buildmode=c-shared src/main/go/calculator.go
