#!/bin/bash
start=$(date +%s.%N)
for i in {1..20}
do
	java Driver -f ../instances/a280_n279_bounded-strongly-corr_01.ttp 1 >> a280_279_packingOnly.txt
done
>&2 echo "Finished Easy 1"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for j in {1..20}
do
	java Driver -f ../instances/a280_n1395_uncorr-similar-weights_05.ttp 2 >> a280_1395_packingOnly.txt
done
>&2 echo "Finished Easy 2"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
start=$(date +%s.%N)
for k in {1..20}
do
	java Driver -f ../instances/a280_n2790_uncorr_10.ttp 3 >> a280_2790_packingOnly.txt
done
>&2 echo "Finished Easy 3"
end=$(date +%s.%N)    
runtime=$(python -c "print(${end} - ${start})")
>&2 echo "Runtime was $runtime"
exit