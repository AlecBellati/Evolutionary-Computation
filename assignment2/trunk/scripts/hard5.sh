#!/bin/bash

cd ../../
javac *.java
java Driver -f ../instances/pla33810_n169045_uncorr-similar-weights_05.ttp
cd scripts/unix