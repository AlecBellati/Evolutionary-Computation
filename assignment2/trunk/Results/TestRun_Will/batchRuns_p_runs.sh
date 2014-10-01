#!/bin/bash
start=$(date +%s.%N)
for i in {1..20}
do
	java -Xmx8192M -d64 Driver -f ../instances/pla33810_n33809_bounded-strongly-corr_01.ttp 7 >> pla33810_33809_packingOnly.txt
done
>&2 echo "Finished Hard 1"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for j in {1..20}
do
	java -Xmx8192M -d64 Driver -f ../instances/pla33810_n169045_uncorr-similar-weights_05.ttp 8 >> pla33810_169045_packingOnly.txt
done
>&2 echo "Finished Hard 2"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for k in {1..20}
do
	java -Xmx8192M -d64 Driver -f ../instances/pla33810_n338090_uncorr_10.ttp 9 >> pla33810_338090_packingOnly.txt
done
>&2 echo "Finished Hard 3"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
exit