# Flow cytometry data set analysis based on Flink

## Sample data format

sample, FSC-A, SSC-A, CD48, Ly6G, CD117, SCA1, CD11b, CD150, CD11c, B220, Ly6C,...

sample, date, experiment, day, subject, kind, instrument, researchers

## Task
- Number of (valid) measurements conducted per researcher.
- k-means clustering of the measurements.
- Outlier removal and reclustering.

## Estimated Duration
< 5 mins

## Restriction
Apache Flink only.

School Flink Cluster only.

## Performance
Three tasks complete in 2 min

## Related Assignment
USYD 2017S1 COMP 5349 Assignment 1

