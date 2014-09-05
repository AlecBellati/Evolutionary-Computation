#!/bin/bash

cd ..
javac *.java
java Driver -f ../instances/a280_n279_bounded-strongly-corr_01.ttp
cd scripts