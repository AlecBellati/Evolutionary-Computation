#!/bin/bash

cd ../../
javac *.java
java Driver -f ../instances/fnl4461_n44600_uncorr_10.ttp
cd scripts/unix