package hw4.hash;

import java.util.HashMap;
import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO: Write a utility function that returns true if the given oomages 
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */

        HashMap<Integer, Integer> h = new HashMap<>();
        //int count = 0;

        for (Oomage o : oomages) {
            int count;
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;

            if (h == null || (h.get(bucketNum) == null)) {
                count = 0;
            } else {
                count = h.get(bucketNum);
            }

            h.put(bucketNum, count + 1);
        }

        for (Integer bucketNum : h.keySet()) {
            if ((h.get(bucketNum) < ((oomages.size()) / 50)) || (h.get(bucketNum) > ((oomages.size()) / 2.5))) {
                return false;
            }
        }

        return true;
    }
}
