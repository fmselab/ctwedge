#!/bin/bash

# This is the path containing the feature model evolutions of the
MODELS_PATH="../evolutionModels/*";
# Output file name
now=$(date +"%m_%d_%Y_%I_%M")
OUTPUT_FILE="output_mutations_$now.csv";
# Number of repetitions
REPETITIONS=10;
# Number of threads
NTHREADS=(1 2 4 6 8);
# Evolution filename
EVOLUTION_FILE="ModelsCorrespondance.csv";

for d in $MODELS_PATH; do
  	if [ -d "$d" ]; then
  		echo "Fetching path $d";
  		# If the directory contains xml files
  		if [ "$(find $d -type f -name '*.xml' | wc -l)" -gt "0" ]; then
  			# Check if fhe files are those in the ModelsCorrespondance.csv
  			model_found=false;
  			for m in [ $(cat $EVOLUTION_FILE | cut -d ',' -f 1) ]; do
  				if [ "$(find $d -type f -name "$m.xml" | wc -l)" -gt "0" ]; then
  					model_found=true;
  					modelname=$m;
  				fi
  			done
  			if $model_found ; then
  				echo " -- Found models in the directory";
  				# Now, since the models have been found, extract from the ModelsCorrespondance.csv file the models to be tested
  				row=$(cat $EVOLUTION_FILE | grep $modelname);
				echo " --- The following files will be considered: $row";
				array=(${row//,/ })
				for i in "${!array[@]}"
				do
					# The pair of files has been found. Run the experiments with different threads and repetitions
					for j in "${NTHREADS[@]}"; do
						for k in $(seq 1 $REPETITIONS); do
							echo " ---- Running experiments with $j threads and repetition $k";
							echo "      Model being mutated ${array[i]}";
							java -jar SPLC_Experiments_With_Mutations.jar $j $d/${array[i]} $OUTPUT_FILE
						done
					done
				done
  			else
  				echo " -- No models found in the directory";
  			fi
  		fi
  	fi
done
