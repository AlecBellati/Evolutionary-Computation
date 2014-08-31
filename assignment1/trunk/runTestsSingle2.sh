#!/bin/bash

java TSPProblem -f ../Data/kroA100.xml >> results/kroA100.csv
java TSPProblem -f ../Data/kroC100.xml >> results/kroC100.csv
java TSPProblem -f ../Data/kroD100.xml >> results/kroD100.csv
java TSPProblem -f ../Data/lin105.xml >> results/lin105.csv
java TSPProblem -f ../Data/pcb442.xml >> results/pcb442.csv