#!/bin/bash

for i in {1..10}
do
    java TSPProblem -f ../Data/eil51.xml >> results/eil51.csv
    java TSPProblem -f ../Data/eil76.xml >> results/eil76.csv
    java TSPProblem -f ../Data/st70.xml >> results/st70.csv
    java TSPProblem -f ../Data/eil101.xml >> results/eil101.csv
    java TSPProblem -f ../Data/kroA100.xml >> results/kroA100.csv
    java TSPProblem -f ../Data/kroC100.xml >> results/kroC100.csv
    java TSPProblem -f ../Data/kroD100.xml >> results/kroD100.csv
    java TSPProblem -f ../Data/lin105.xml >> results/lin105.csv
    java TSPProblem -f ../Data/pcb442.xml >> results/pcb442.csv
done