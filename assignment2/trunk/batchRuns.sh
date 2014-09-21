#!/bin/bash
java DriverOld ../instances/ a280_n279_bounded-strongly-corr_01.ttp 2 10000 600000 > a280_hillClimber_opt2.txt
java DriverOld ../instances/ a280_n1395_uncorr-similar-weights_05.ttp 2 10000 600000 >> a280_hillClimber_opt2.txt
java DriverOld ../instances/ a280_n2790_uncorr_10.ttp 2 10000 600000 >> a280_hillClimber_opt2.txt
java DriverOld ../instances/ fnl4461_n4460_bounded-strongly-corr_01.ttp 2 10000 600000 > fnl4461_hillClimber_opt2.txt
java DriverOld ../instances/ fnl4461_n22300_uncorr-similar-weights_05.ttp 2 10000 600000 >> fnl4461_hillClimber_opt2.txt
java DriverOld ../instances/ fnl4461_n44600_uncorr_10.ttp 2 10000 600000 >> fnl4461_hillClimber_opt2.txt
java DriverOld ../instances/ pla33810_n33809_bounded-strongly-corr_01.ttp 2 10000 600000 > pla33810_hillClimber_opt2.txt
java DriverOld ../instances/ pla33810_n169045_uncorr-similar-weights_05.ttp 2 10000 600000 >> pla33810_hillClimber_opt2.txt
java DriverOld ../instances/ pla33810_n338090_uncorr_10.ttp 2 10000 600000 >> pla33810_hillClimber_opt2.txt

java DriverOld ../instances/ a280_n279_bounded-strongly-corr_01.ttp 1 10000 600000 > a280_hillClimber_opt1.txt
java DriverOld ../instances/ a280_n1395_uncorr-similar-weights_05.ttp 1 10000 600000 >> a280_hillClimber_opt1.txt
java DriverOld ../instances/ a280_n2790_uncorr_10.ttp 1 10000 600000 >> a280_hillClimber_opt1.txt
java DriverOld ../instances/ fnl4461_n4460_bounded-strongly-corr_01.ttp 1 10000 600000 > fnl4461_hillClimber_opt1.txt
java DriverOld ../instances/ fnl4461_n22300_uncorr-similar-weights_05.ttp 1 10000 600000 >> fnl4461_hillClimber_opt1.txt
java DriverOld ../instances/ fnl4461_n44600_uncorr_10.ttp 1 10000 600000 >> fnl4461_hillClimber_opt1.txt
java DriverOld ../instances/ pla33810_n33809_bounded-strongly-corr_01.ttp 1 10000 600000 > pla33810_hillClimber_opt1.txt
java DriverOld ../instances/ pla33810_n169045_uncorr-similar-weights_05.ttp 1 10000 600000 >> pla33810_hillClimber_opt1.txt
java DriverOld ../instances/ pla33810_n338090_uncorr_10.ttp 1 10000 600000 >> pla33810_hillClimber_opt1.txt