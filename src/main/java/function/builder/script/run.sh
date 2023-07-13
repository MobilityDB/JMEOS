#!/bin/bash
echo "Extraction des informations du fichier \"meos.h\"."
./run_extractor.sh

echo "Generation du fichier \"functions\"."
./run_builder.sh

read -p "Fin des scripts..."
