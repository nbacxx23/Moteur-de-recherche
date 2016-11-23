package tool.search;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class includes the method used to do the fusion among all the articles lists correspond to the
 * requested words and find the articles including all of them
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */
public class Fusion {

    /**
     * Finding out the articles include all the requested words
     * @param keyDoc
     * the index information of every requested words
     * @return docFusion
     * the ralated articles including all the requested words
     */
    public static TreeSet<String> fusionList(HashMap<String, TreeMap<String,Integer>> keyDoc) {

        int sizeKey = keyDoc.size();

        TreeSet<String> docFusion;

        String[] keys = new String[sizeKey];

        keys = keyDoc.keySet().toArray(keys);

        if (sizeKey == 1) {

            Set<String> temp = keyDoc.get(keys[0]).keySet();
            docFusion = new TreeSet<>(temp);
            return docFusion;

        } else {

            Set<String> temp = keyDoc.get(keys[0]).keySet();
            docFusion = new TreeSet<>(temp);
            for (int i = 1; i < sizeKey; i++) {
                docFusion.retainAll(keyDoc.get(keys[i]).keySet());
            }

            return docFusion;
        }
    }
}
