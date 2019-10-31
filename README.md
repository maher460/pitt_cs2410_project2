# Simple Cache Simulator

Author: Maher Khan

## Testing

Commandline argument format: cache_size block_size associtivity path_to_trace_file
cache_size: size of chace in MB
block_size: size of each block in Bytes
associtivity: Number of set associtivity

To run:

`
javac CacheSimulator.java 
java CacheSimulator 8 32 4 ../test/trace0
`