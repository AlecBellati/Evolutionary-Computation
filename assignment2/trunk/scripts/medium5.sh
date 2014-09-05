#!/bin/bash

cd ..
javac *.java
java Driver -f ../instances/fnl4461_n22300_uncorr-similar-weights_05.ttp
cd scripts