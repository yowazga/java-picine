#!/bin/bash

find . -type f -name "*.class" -exec rm -f {} +

echo "All .class files have been deleted."