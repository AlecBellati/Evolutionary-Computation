#!/bin/bash
time java Driver -f ../instances/fnl4461_n44600_uncorr_10.ttp 6 > fnl4461_44600_packingOnly.txt
time java -Xmx8192M -d64 Driver -f ../instances/pla33810_n33809_bounded-strongly-corr_01.ttp 7 > pla33810_33809_packingOnly.txt