/**
 * CacheSimulator.java
 * Author: Maher Khan
 * (Translated code given in project description from C to Java)
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.IllegalArgumentException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CacheSimulator
{
    private Cache cache;

    public CacheSimulator(int size, int blocksize, int assoc)
    {
        this.cache = new Cache(size, blocksize, assoc);
    }

    public void updateLRU(int index, int way)
    {
        int k;
        for (k=0; k < this.cache.assoc; k++)
        {
            if(this.cache.blocks[index][k].lru < this.cache.blocks[index][way].lru)
            {
                this.cache.blocks[index][k].lru = this.cache.blocks[index][k].lru + 1;
            }
        }
        this.cache.blocks[index][way].lru = 0;
    }

    /**
     * This function is used to simulate a single cache access
     * @param  address     [memory address]
     * @param  access_type [0 for read, 1 for write]
     * @return             [returns 0 (if a hit), 
     *                              1 (if a miss but no dirty block is writen back), or
     *                              2 (if a miss and a dirty block is writen back)]
     */ 
    public int cache_access(long address, int access_type)
    {
        int block_address = (int) (address / (long) this.cache.blocksize);
        int tag = block_address / this.cache.nsets;
        int index = block_address - (tag * this.cache.nsets);
        int i, way, max;

        /**
         * Check for cache hit
         */
        for(i=0; i < this.cache.assoc; i++)
        {
            if(this.cache.blocks[index][i].tag == tag 
                && this.cache.blocks[index][i].valid)
            {
                // HIT!
                this.updateLRU(index, i);
                if(access_type == 1)
                {
                    this.cache.blocks[index][i].dirty = true;
                }
                return 0;
            }
        }

        /**
         * If not a hit, then it is a miss :(
         * Now, we are going to look for an invalid entry
         */
        for(way=0; way<this.cache.assoc; way++)
        {
            if(!this.cache.blocks[index][way].valid)
            {
                // Found an invalid entry
                this.cache.blocks[index][way].valid = true;
                this.cache.blocks[index][way].tag = tag;
                this.cache.blocks[index][way].lru = way;
                this.updateLRU(index, way);
                if(access_type==0)
                {
                   this.cache.blocks[index][way].dirty = false; 
                }
                else
                {
                    this.cache.blocks[index][way].dirty = true;
                }
                return 1;
                
            }
        }

        /**
         * No dirty block found :(
         * Now, we are going to find the least recently used block
         */
        max = this.cache.blocks[index][0].lru;
        way = 0;
        for(i=0; i<this.cache.assoc; i++)
        {
            if(this.cache.blocks[index][i].lru > max)
            {
                max = this.cache.blocks[index][i].lru;
                way = i;
            }
        }

        /**
         * We found the least recently used block
         * which is this.blocks[index][way]
         */
        this.cache.blocks[index][way].tag = tag;
        this.updateLRU(index, way);

        // Check the condition of the evicted block
        if(!this.cache.blocks[index][way].dirty)
        {
            // The evicted block is clean!
            this.cache.blocks[index][way].dirty = (access_type==0 ? false : true) ;
            return 1;
        }
        else
        {
            // The evicted block is dirty
            this.cache.blocks[index][way].dirty = (access_type==0 ? false : true);
            return 2;
        }
    }

    public static void main(String [] args) throws FileNotFoundException, IOException
    {
        if(args.length != 4)
        {
            throw new IllegalArgumentException("Expecting: size blocksize assoc path_to_trace_file");
        }

        int size = Integer.parseInt(args[0]);
        int blocksize = Integer.parseInt(args[1]);
        int assoc = Integer.parseInt(args[2]);
        String file_path = args[3];

        CacheSimulator cache_sim = new CacheSimulator(size, blocksize, assoc);

        int hits = 0;
        int misses = 0;
        int evicts = 0;
        String[] splitted;
        long address;
        int access_type;
        int res;

        try(BufferedReader br = new BufferedReader(new FileReader(file_path))) 
        {
            for(String line; (line = br.readLine()) != null; ) {
                splitted = line.split(" ", 4);
                address = Long.decode(splitted[3]);
                access_type = Integer.parseInt(splitted[2]);
                res = cache_sim.cache_access(address, access_type);
                if(res == 0)
                {
                    hits = hits + 1;
                }
                else if(res == 1)
                {
                    misses = misses + 1;
                }
                else
                {
                    misses = misses + 1;
                    evicts = evicts + 1;
                }
            }
        }

        System.out.println("[CacheSimulator] Done running the simulation!");
        System.out.println("[CacheSimulator] Hits: " + Integer.toString(hits));
        System.out.println("[CacheSimulator] Misses: " + Integer.toString(misses));
        System.out.println("[CacheSimulator] Evicts: " + Integer.toString(evicts));
    }

}