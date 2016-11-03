package tool.search;

import tools.Normalizer;

import java.io.*;
import java.util.*;

/**
 * Created by xchen on 2016/11/2.
 */
public class InvertedIndex {

    public static TreeMap<String, TreeSet<String>> getInvertedFile(File dir, Normalizer normalizer) throws IOException {

        TreeMap<String, TreeSet<String>> index_iv = new TreeMap<String, TreeSet<String>>();
        ArrayList<String> wordsInFile;
        ArrayList<String> words;
        String wordLC;
        if (dir.isDirectory()) {
            // Liste des fichiers du répertoire
            // ajouter un filtre (FileNameFilter) sur les noms
            // des fichiers si nécessaire
            File[] files = dir.listFiles();

            // Parcours des fichiers et remplissage de la table

            for (File file : files) {
                //System.err.println("Analyse du fichier " + file.getAbsolutePath());
                // TODO
                wordsInFile = new ArrayList<String>();
                ArrayList<String> words_file = normalizer.normalize(file);
                Integer number;
                for (String word : words_file) {
                    wordLC = word;
                    wordLC = wordLC.toLowerCase();
                    // si le mot n'a pas déjà été trouvé dans ce document :
                    if (!index_iv.containsKey(wordLC)) {
                        TreeSet<String> doc = new TreeSet<String>();
                        doc.add(file.getName());
                        index_iv.put(wordLC, doc);
                    }
                    TreeSet<String> doc_temp = index_iv.get(wordLC);
                    doc_temp.add(file.getName());
                    index_iv.put(wordLC, doc_temp);
                }
            }
        }
        return index_iv;
    }

    public static void saveInvertedFile(TreeMap<String, TreeSet<String>> invertedFile, File outFile) throws IOException{

        String word;
        TreeSet<String> doc;
        Integer frequence;
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        try {
            FileWriter fw = new FileWriter(new File(outFile, "Inverted_File"));
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            for (Map.Entry<String, TreeSet<String>> inverted_f : invertedFile.entrySet()) {
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



    public static TreeMap<String, TreeMap<String, Integer>> getInvertedFileWithWeights(File dir, Normalizer normalizer) throws IOException{

        TreeMap<String,TreeMap<String,Integer>> index_iv_w = new TreeMap<String,TreeMap<String,Integer>>();


        if (dir.isDirectory()) {
            // Liste des fichiers du répertoire
            // ajouter un filtre (FileNameFilter) sur les noms
            // des fichiers si nécessaire
            File[] files = dir.listFiles();

            // Parcours des fichiers et remplissage de la table

            for (File file : files) {
                //System.err.println("Analyse du fichier " + file.getAbsolutePath());
                // TODO

                HashMap<String,Integer> hits = new HashMap<String, Integer>();

                hits = TfIdf.getTermFrequencies(file,normalizer);

                for(String word:hits.keySet()){
                    if(!index_iv_w.containsKey(word)){
                        TreeMap<String,Integer> tf = new TreeMap<String, Integer>();
                        tf.put(file.getName(),hits.get(word));
                        index_iv_w.put(word,tf);
                    }

                    TreeMap<String,Integer> tf2 = index_iv_w.get(word);
                    tf2.put(file.getName(),hits.get(word));
                    index_iv_w.put(word,tf2);

                }

            }
        }
        return index_iv_w;
    }
}
