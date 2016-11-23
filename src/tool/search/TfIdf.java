package tool.search;

import tools.Normalizer;

import java.io.*;
import java.util.*;

/**
 * Methods used to getting the information of article
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */

public class TfIdf {

    /**
     * Term Frequency of each word existing in the article
     * @param file
     * the article to be dealt with
     * @param normalizer
     * The normalizer used to process the text
     * @return hits
     * Term Frequency of each word in the article
     */
    public static HashMap<String, Integer>  getTermFrequencies(File file, Normalizer normalizer) throws IOException {
        // Création de la table des mots
        HashMap<String, Integer> hits = new HashMap<String, Integer>();

        // Appel de la méthode de normalisation
        ArrayList<String> words = normalizer.normalize(file);

        Integer number;
        // Pour chaque mot de la liste, on remplit un dictionnaire
        // du nombre d'occurrences pour ce mot

        for (String word : words) {

            word = word.toLowerCase();
            // Sinon, on incrémente le nombre d'occurrence
            if(hits.get(word)==null){
                hits.put(word,1);
            }else{
                number = hits.get(word)+1;
                hits.put(word,number);
            }

        }
        return hits;
    }


    /**
     * Term Frequency of each word existing in the article
     * @param file
     * the article to be dealt with
     * @param documentNumber
     * total number of article in this corpus
     * @param df
     * The normalizer used to process the text
     * @param doc
     * Hashmap contains the information of the term frequency of every article for each word in the index
     * @param normalizer
     * The normalizer used to process the text
     * @return hits
     * Term Frequency of each word in the article
     */
    public static HashMap<String, Double> getTfIdf(File file, int documentNumber,HashMap<String,Integer> df, HashMap<String,TreeMap<String,Integer>> doc,Normalizer normalizer) throws IOException {

        HashMap<String, Double> tfIdfs = new HashMap<String, Double>();

        ArrayList<String> words = normalizer.normalize(file);

        for(String word: words){
            Double idf = Math.log((double)documentNumber/(double)df.get(word));
            Double tf_idf = (double)doc.get(word).get(file.getName().replaceAll(".txt",""))*idf;
            tfIdfs.put(word,tf_idf);
        }
        return tfIdfs;
    }


}
