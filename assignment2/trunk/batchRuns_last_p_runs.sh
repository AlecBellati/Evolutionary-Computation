#!/bin/bash
time java -Xmx8192M -d64 Driver -f ../instances/pla33810_n169045_uncorr-similar-weights_05.ttp 8 > pla33810_169045_packingOnly.txt
time java -Xmx8192M -d64 Driver -f ../instances/pla33810_n338090_uncorr_10.ttp 9 > pla33810_338090_packingOnly.txt