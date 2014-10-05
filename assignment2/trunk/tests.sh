#!/bin/bash


echo 'fnl4461_n4460_bounded-strongly-corr_01.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/fnl4461_n4460_bounded-strongly-corr_01.ttp >> fnl4461_n4460_bounded-strongly-corr_01.txt
done

echo 'fnl4461_n22300_uncorr-similar-weights_05.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/fnl4461_n22300_uncorr-similar-weights_05.ttp >> fnl4461_n22300_uncorr-similar-weights_05.txt
done

echo 'fnl4461_n44600_uncorr_10.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/fnl4461_n44600_uncorr_10.ttp >> fnl4461_n44600_uncorr_10.txt
done




echo 'pla33810_n33809_bounded-strongly-corr_01.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/pla33810_n33809_bounded-strongly-corr_01.ttp >> pla33810_n33809_bounded-strongly-corr_01.txt
done

echo 'pla33810_n169045_uncorr-similar-weights_05.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/pla33810_n169045_uncorr-similar-weights_05.ttp >> pla33810_n169045_uncorr-similar-weights_05.txt
done

echo 'pla33810_n338090_uncorr_10.ttp'
for i in {1..10}
do
    time java Driver -f ../instances/pla33810_n338090_uncorr_10.ttp >> pla33810_n338090_uncorr_10.txt
done