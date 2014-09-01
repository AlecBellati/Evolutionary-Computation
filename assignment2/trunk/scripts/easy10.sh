#!/bin/bash

cd ../../
javac *.java
java Driver -f ../instances/a280_n2790_uncorr_10.ttp
cd scripts/unix