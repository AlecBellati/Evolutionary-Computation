#!/bin/bash
time java Driver -f ../instances/fnl4461_n4460_bounded-strongly-corr_01.ttp 4 > fnl4461_4460_packingOnly.txt
time java Driver -f ../instances/fnl4461_n22300_uncorr-similar-weights_05.ttp 5 > fnl4461_22300_packingOnly.txt