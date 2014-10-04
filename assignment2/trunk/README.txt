Evolutionary Comptuation
COMP SCI 4095
Assignment Two
William Reid (a1215621) - william.reid@student.adelaide.edu.au
Alec Bellati (a1608934) - alec.bellati@student.adelaide.edu.au
Sami Peachey (a1192722) - samantha.peachey@student.adelaide.edu.au
Matthew Hart (a1193380) - matthew.hart@student.adelaide.edu.au


URL: https://github.com/AlecBellati/Evolutionary-Computation/
(Private Github repository, you should have access)


---- Details ----

Hi Kelly, Markus, whomever else will be marking this,

This README will explain how to run our code.

1. Driver.java is the program driver.

2. Line 176 in the TTPInstance.java file contains calls to four different algorithms.
	Alg 1: (Alec)
	Alg 2: (Matt)
	Alg 3: (Sami)
	Alg 4: Obsessive Packing (Will)
   Uncomment one of these at a time to test each algorithm

3. In addition, line 192 of the same file contains calls to each of the algorithms to get 
   the final solution (after 10 minutes). Uncomment the same algorithm name as done in step 2.

4. Once completed, compiled and run, the program should print out the solution after two minutes,
   five minutes and 10 minutes.


At command line, run:

java Driver -f <filename> [1-9]

The optional addition of the number on the end [1-9] is for us with the Obsessive Packing algorithm (Will)
and will choose the best parameters for the particular instance supplied. I.e. for instance 1 (a280_n279),
supply the number 1 at command line, where as for instance 9 (pla33810_n33809), supply the number 9.

Thanking you,
William Reid, Matthew Hart, Samantha Peachey, Alec Bellati.