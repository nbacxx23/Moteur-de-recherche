package tool.search;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by xchen on 2016/11/2.
 */
public class Fusion {

    public static TreeSet<String> fusionList(TreeMap<String, TreeSet<String>> keyDoc) {

        /**
         for (Map.Entry<String, TreeSet<String>> key : keyDoc.entrySet()) {

         System.out.println("word:" + key.getKey() + "\t" + "doc:" + key.getValue());

         }
         */

        int sizeKey = keyDoc.size();

        TreeSet<String> docFusion;

        String[] keys = new String[sizeKey];

        keys = keyDoc.keySet().toArray(keys);

        if (sizeKey == 1) {
            docFusion = keyDoc.get(keys[0]);
            return docFusion;
        } else {
            docFusion = keyDoc.get(keys[0]);
            for (int i = 1; i < sizeKey; i++) {
                docFusion.retainAll(keyDoc.get(keys[i]));
            }

            return docFusion;
        }
    }
}
