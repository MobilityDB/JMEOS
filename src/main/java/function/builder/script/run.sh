#!/bin/bash
echo "Extracting information from \"meos.h\" file..."
./run_extractor.sh

echo "Generating file \"functions\"..."
./run_builder.sh

read -p "End of scripts."
