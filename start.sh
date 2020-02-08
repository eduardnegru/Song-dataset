#!/bin/bash

spark-submit --driver-memory 4g --executor-memory 4g --class songdataset.SongDataset --master spark://adrian:7077 song.jar ./data/data1.txt

