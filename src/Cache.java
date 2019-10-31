/**
 * Cache.java
 * Author: Maher Khan
 * (Translated code given in project description from C to Java)
 */


/**
 * The cache is represented by a 2-D array of blocks. 
 * The first dimension of the 2D array is "nsets" which is the number of sets (entries)
 * The second dimension is "assoc", which is the number of blocks in each set.
 */
public class Cache
{
    public int nsets;                       // number of sets
    public int blocksize;                   // block size 
    public int assoc;                       // associativity
    public int miss_penalty;                // the miss penalty
    public CacheBlock[][] blocks;   // a pointer tot he array of cache blocks

    public Cache(int size, int blocksize, int assoc)
    {
        int nblocks = size * 1024 / blocksize;

        this.blocksize = blocksize;         // number of blocks in the cache
        this.nsets = nblocks / assoc;       // number of sets (entries) in the cache
        this.assoc = assoc;


        // Create the cahce blocks
        this.blocks = new CacheBlock[this.nsets][this.assoc];
        for(int i=0; i<this.nsets; i++)
        {
            for(int j=0; j<this.assoc; j++)
            {
                this.blocks[i][j] = new CacheBlock();
            }

        }
    }

    /**
     * This is a simple cache block.
     * Note that no actual data will be stored in the cache
     */
    protected class CacheBlock
    {
        public long tag;
        public Boolean valid;
        public Boolean dirty;
        public int lru; // to be used to build the LRU stack for the blocks in a cache set

        public CacheBlock()
        {
            this.tag = 0;
            this.valid = false;
            this.dirty = false;
            this.lru = 0;
        }
    }
}