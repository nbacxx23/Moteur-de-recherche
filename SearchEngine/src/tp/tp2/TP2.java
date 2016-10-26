package tp.tp2;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;
import tp.tp1.TP1;

/**
 * TP 2
 * @author Xiaoxiao CHEN
 *
 */
public class TP2 {

    /**
     * Le répertoire du corpus
     */
    // TODO CHANGER LE CHEMIN
    protected static String DIRNAME = "/Users/xchen/Documents/AIC/TC3/tp2/lemonde-utf8";
    /**
     * Le fichier contenant les mots vides
     */
    private static String STOPWORDS_FILENAME = "/Users/xchen/Documents/AIC/TC3/tp2/frenchST.txt";



    /**
     * exo 2.1 : Calcule le df, c'est-à-dire le nombre de documents
     * pour chaque mot apparaissant dans la collection. Le mot
     * "à" doit ainsi apparaître dans 88 documents, le mot
     * "ministère" dans 4 documents.
     */
    public static HashMap<String, Integer> getDocumentFrequency(File dir, Normalizer normalizer) throws IOException {
        HashMap<String, Integer> hits = new HashMap<String, Integer>();
        // TODO
        if (dir.isDirectory()) {
            // Liste des fichiers du répertoire
            // ajouter un filtre (FileNameFilter) sur les noms
            // des fichiers si nécessaire
            File[] files = dir.listFiles();

            // Parcours des fichiers et remplissage de la table
            int nb_file = 0;
            for (File file : files) {
                //System.err.println("Analyse du fichier " + file.getAbsolutePath());
                // TODO
                ArrayList<String> words_c = normalizer.normalize(file);
                Set<String> word_found = new HashSet();
                Integer number_c;
                for (String word : words_c) {
                    word = word.toLowerCase();
                    if (hits.get(word) == null) {
                        hits.put(word, 1);
                        word_found.add(word);
                    } else if(!word_found.contains(word)){
                        number_c = hits.get(word) + 1;
                        hits.put(word, number_c);
                        word_found.add(word);
                    }
                }
            }
        }
        return hits;
    }

    /**
     * exo 2.4 : Calcule le tf.idf des mots d'un fichier en fonction
     * des df déjà calculés, du nombre de documents et de
     * la méthode de normalisation.
     */
    public static HashMap<String, Double> getTfIdf(File file, HashMap<String, Integer> dfs, int documentNumber, Normalizer normalizer) throws IOException {
        HashMap<String, Double> tfIdfs = new HashMap<String, Double>();
        // TODO

        HashMap<String, Integer> tf_f = TP1.getTermFrequencies(file,normalizer);

        ArrayList<String> words = normalizer.normalize(file);

        for(String word: words){
            Double idf = Math.log((double)documentNumber/(double)dfs.get(word));
            Double tf_idf = (double)tf_f.get(word)*idf;
            tfIdfs.put(word,tf_idf);
        }
        return tfIdfs;
    }

    /**
     * exo 2.5 : Crée, pour chaque fichier d'un répertoire, un nouveau
     * fichier contenant les poids de chaque mot. Ce fichier prendra
     * la forme de deux colonnes (mot et poids) séparées par une tabulation.
     * Les mots devront être placés par ordre alphabétique.
     * Les nouveaux fichiers auront pour extension .poids
     * et seront écrits dans le répertoire outDirName.
     */
    public static void getWeightFiles(File inDir, File outDir, Normalizer normalizer) throws IOException {
        // TODO
        if (inDir.isDirectory()) {
            // Liste des fichiers du répertoire
            // ajouter un filtre (FileNameFilter) sur les noms
            // des fichiers si nécessaire
            File[] files = inDir.listFiles();

            HashMap<String, Integer> dfs = getDocumentFrequency(inDir, normalizer);

            if (!outDir.exists()) {
                outDir.mkdirs();
            }

            for (File file : files) {
                String filename = file.getName();
                HashMap<String, Double> tf_idf = getTfIdf(file, dfs, files.length, normalizer);
                TreeSet<String> words = new TreeSet<String>(tf_idf.keySet());
                try {
                    PrintWriter writer = new PrintWriter(outDir + "/" + filename.replace("txt", "poids"));
                    for (String word : words) {
                        writer.println(word + "\t" + tf_idf.get(word));
                    }
                    writer.close();
                }catch (Exception e){
                    System.out.println(e.toString());
                }
            }
        }
    }

    /**
     * Main, appels de toutes les méthodes des exercices du TP2.
     */
    public static void main(String[] args) {
        try {
            String outDirName = "/Users/xchen/Documents/AIC/TC3/tp2/sorties";
            Normalizer stemmerAllWords = new FrenchStemmer();
            Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));
            Normalizer tokenizerAllWords = new FrenchTokenizer();
            Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));
            Normalizer[] normalizers = {stemmerAllWords, stemmerNoStopWords,
                    tokenizerAllWords, tokenizerNoStopWords};
            for (Normalizer normalizer : normalizers) {
                String name = normalizer.getClass().getName();
                if (!normalizer.getStopWords().isEmpty()) {
                    name += "_noSW";
                }
                System.out.println("Normalisation avec " + name);
                System.out.println(getDocumentFrequency(new File(DIRNAME), normalizer).size());
                System.out.println("GetWeightFiles avec " + name);
                getWeightFiles(new File(DIRNAME), new File(new File(outDirName), name), normalizer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}