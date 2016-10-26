package tp.tp3;

import tools.FrenchStemmer;
import tools.Normalizer;

import java.io.*;
import java.util.*;
import tp.tp1.TP1;

/**
 * TP 3
 * @author Xiaoxiao CHEN
 *
 */
public class TP3 {

    /**
     * Le répertoire du corpus
     */
    private static String DIRNAME = "/Users/xchen/Documents/AIC/TC3/tp3/lemonde-utf8";
    /**
     * Le fichier contenant les mots vides
     */
    private static String STOPWORDS_FILENAME = "/Users/xchen/Documents/AIC/TC3/tp2/frenchST.txt";


    public static TreeMap<String, TreeSet<String>> getInvertedFile(File dir, Normalizer normalizer) throws IOException{

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

                hits = TP1.getTermFrequencies(file,normalizer);

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


    /**
     * Main, appels de toutes les méthodes des exercices du TD1.
     */
    public static void main(String[] args) {
        try{
            String outFileName = "/Users/xchen/Documents/AIC/TC3/tp3/sorties";
            Normalizer stemmerAllWords = new FrenchStemmer();

            TreeMap<String, TreeMap<String,Integer>> index_iv_temp = getInvertedFileWithWeights(new File(DIRNAME),stemmerAllWords);
            //saveInvertedFile(index_iv_temp,new File(outFileName));

            TreeMap<String,Integer> tf;
            String word;
            for (Map.Entry<String, TreeMap<String,Integer>> hit : index_iv_temp.entrySet()) {
                word = hit.getKey();
                tf = hit.getValue();
                System.out.println("word:" + word + "\t"+"doc:" + tf);
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}