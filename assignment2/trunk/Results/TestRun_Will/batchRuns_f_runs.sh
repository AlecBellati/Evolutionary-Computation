#!/bin/bash
start=$(date +%s.%N)
for i in {1..20}
do
	java Driver -f ../instances/fnl4461_n4460_bounded-strongly-corr_01.ttp 4 >> fnl4461_4460_packingOnly.txt
done
>&2 echo "Finished Medium 1"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for j in {1..20}
do
	java Driver -f ../instances/fnl4461_n22300_uncorr-similar-weights_05.ttp 5 >> fnl4461_22300_packingOnly.txt
done
>&2 echo "Finished Medium 2"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for k in {1..20}
do
	java Driver -f ../instances/fnl4461_n44600_uncorr_10.ttp 6 >> fnl4461_44600_packingOnly.txt
done
>&2 echo "Finished Medium 3"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
exit