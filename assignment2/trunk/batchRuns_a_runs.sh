#!/bin/bash
time java Driver -f ../instances/a280_n279_bounded-strongly-corr_01.ttp 1 > a280_279_packingOnly.txt
time java Driver -f ../instances/a280_n1395_uncorr-similar-weights_05.ttp 2 > a280_1395_packingOnly.txt
time java Driver -f ../instances/a280_n2790_uncorr_10.ttp 3 > a280_2790_packingOnly.txt