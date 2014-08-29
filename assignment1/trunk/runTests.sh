#!/bin/bash

FILES=../Data/*
for f in $FILES
do
    java TSPProblem -f "$f"
done