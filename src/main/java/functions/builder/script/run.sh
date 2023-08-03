#!/bin/bash

echo "Extracting information from the \"meos.h\" file..."
./run_extractor.sh

echo "Generating the \"functions\" file..."
./run_builder.sh

read -p "End of scripts."