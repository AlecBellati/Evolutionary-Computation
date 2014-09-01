#!/bin/bash

cd ../../
javac *.java
java Driver -f ../instances/fnl4461_n4460_bounded-strongly-corr_01.ttp
cd scripts/unix