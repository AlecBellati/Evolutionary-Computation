#!/bin/bash

#store current working directory
cwd=$(pwd)

#remove in base
rm *.class
cd TTP

#remove in TTP
rm *.class

#remove in Optimisation
cd Optimisation
rm *.class
cd ..

#remove in Utils
cd Utils
rm *.class
cd ..

#remove in Thief
cd Thief
rm *.class

#remove in Travel
cd Travel
rm *.class

#return to parent
cd $cwd
