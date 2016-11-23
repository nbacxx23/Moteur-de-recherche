package tool.search;

import tools.Normalizer;

import java.io.*;
import java.util.*;

/**
 * This class includes all the methods used to construct inverted index
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */
public class InvertedIndex {

    /**
     * Storing the inverted index to the appointed path
     * @param invertedFile
     * The inverted index constructed by the system
     * @param outFile
     * path to store the inverted index
     */
    public static void saveInvertedFile(TreeMap<String, TreeMap<String,Integer>> invertedFile, File outFile) throws IOException{

        String word;
        TreeMap<String,Integer> doc;
        Integer frequence;
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        try {
            FileWriter fw = new FileWriter(new File(outFile, "Inverted_Index"));
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            for (Map.Entry<String, TreeMap<String,Integer>> inverted_f : invertedFile.entrySet()) {
                word = inverted_f.getKey();
                doc = inverted_f.getValue();
                frequence = doc.size();

                out.println(word + "\t" + frequence + "\t" + doc);

            }
            out.close();
        }catch (Exception e) {
            System.out.println(e.toString());
        }

    }


    /**
     * Establishing the inverted index with weight information
     * @param dir
     * subindex file including all the articles information
     * @param normalizer
     * The normalizer used to process the text
     * @return index_iv_w
     * inverted index with articles and term frequency
     */
    public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(File dir, Normalizer normalizer) throws IOException{

        TreeMap<String,TreeMap<String,Integer>> index_iv_w = new TreeMap<String,TreeMap<String,Integer>>();


        if (dir.isDirectory()) {

            File[] files = dir.listFiles();

            // Parcours des fichiers et remplissage de la table

            for (File file : files) {

                HashMap<String,Integer> hits = new HashMap<String, Integer>();

                hits = TfIdf.getTermFrequencies(file,normalizer);

                for(String word:hits.keySet()){
                    if(!index_iv_w.containsKey(word)){
                        TreeMap<String,Integer> tf = new TreeMap<String, Integer>();
                        tf.put(file.getName().replaceAll(".txt",""),hits.get(word));
                        index_iv_w.put(word,tf);
                    }

                    TreeMap<String,Integer> tf2 = index_iv_w.get(word);
                    tf2.put(file.getName().replaceAll(".txt",""),hits.get(word));
                    index_iv_w.put(word,tf2);

                }

            }
        }
        return index_iv_w;
    }
}
