#!/bin/bash

for i in {1..10}
do
    java TSPProblem -f ../Data/eil51.xml >> eil51.csv &
    java TSPProblem -f ../Data/eil76.xml >> eil76.csv &
    java TSPProblem -f ../Data/st70.xml >> st70.csv &
    java TSPProblem -f ../Data/eil101.xml >> eil101.csv &
    java TSPProblem -f ../Data/kroA100.xml >> kroA100.csv &
    java TSPProblem -f ../Data/kroC100.xml >> kroC100.csv &
    java TSPProblem -f ../Data/kroD100.xml >> kroD100.csv &
    java TSPProblem -f ../Data/lin105.xml >> lin105.csv &
    java TSPProblem -f ../Data/pcb442.xml >> pcb442.csv &
    java TSPProblem -f ../Data/pr2392.xml >> pr2392.csv &
done