package tp.tp5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * TP 5
 *
 * @author Xiaoxiao CHEN
 */

public class TP5 {

    /**
     * Le répertoire du corpus
     */
    private static String DIRNAME = "/Users/xchen/Documents/AIC/TC3/tp2/sorties/tools.FrenchStemmer_noSW/";

    public static double getSimilarity(File file1, File file2) {

        HashMap<String, Double> tfidf1 = new HashMap<String, Double>();
        HashMap<String, Double> tfidf2 = new HashMap<String, Double>();
        HashMap<String, Double[]> tfidf = new HashMap<String, Double[]>();

        double scoreSimilarity = 0;
        double sum_d_j_k = 0;
        double sum_d_j = 0;
        double sum_d_k = 0;

        try {
            FileReader fr1 = new FileReader(file1);
            BufferedReader br1 = new BufferedReader(fr1);

            FileReader fr2 = new FileReader(file2);
            BufferedReader br2 = new BufferedReader(fr2);

            String line1_r;
            String line2_r;
            String[] line1;
            String[] line2;
            TreeSet<String> words = new TreeSet<>();

            while ((line1_r = br1.readLine()) != null) {
                line1 = line1_r.split("\t");
                tfidf1.put(line1[0], Double.parseDouble(line1[1]));
                if (!words.contains(line1[0])) {
                    words.add(line1[0]);
                }
            }

            while ((line2_r = br2.readLine()) != null) {
                line2 = line2_r.split("\t");
                tfidf2.put(line2[0], Double.parseDouble(line2[1]));
                if (!words.contains(line2[0])) {
                    words.add(line2[0]);
                }
            }

            for (String word : words) {
                //System.out.println(word);
                Double[] weight = new Double[2];
                if (tfidf1.containsKey(word)) {
                    weight[0] = tfidf1.get(word);
                } else {
                    weight[0] = (double) 0;
                }

                if (tfidf2.containsKey(word)) {
                    weight[1] = tfidf2.get(word);
                } else {
                    weight[1] = (double) 0;
                }
                //System.out.println(word + "\t" + weight[0] + "\t" + weight[1]);
                tfidf.put(word, weight);
            }

            for (Map.Entry<String, Double[]> allTfIdf : tfidf.entrySet()) {
                sum_d_j_k += allTfIdf.getValue()[0] * allTfIdf.getValue()[1];
                sum_d_j += Math.pow(allTfIdf.getValue()[0], 2);
                sum_d_k += Math.pow(allTfIdf.getValue()[1], 2);
            }

            scoreSimilarity = sum_d_j_k / (Math.sqrt(sum_d_j) * Math.sqrt(sum_d_k));

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return scoreSimilarity;
    }

    public static void getSimilarDocuments(File file, Set<File> fileList) {
        NavigableMap<Object,Object> scoreMap = new TreeMap<>().descendingMap();
            for(File fileCompare:fileList){
                double scoreCompare = getSimilarity(file,fileCompare);
                scoreMap.put(scoreCompare,fileCompare.getName());
            }

            for(Map.Entry<Object,Object > score : scoreMap.entrySet()){
                System.out.println(score.getValue()+"\t"+score.getKey());
            }
    }

    /**
     * Main, appels de toutes les méthodes des exercices du TD1.
     */
    public static void main(String[] args) {

        String file1 = DIRNAME + "/texte.95-1.poids";
        String file2 = DIRNAME + "/texte.95-1.poids";

        File dir = new File(DIRNAME);
        File[] files = dir.listFiles();
        TreeSet<File> files_s = new TreeSet<File>();
        for (File file : files) {
            files_s.add(file);
        }
        getSimilarDocuments(new File(file1), files_s);

        double score = getSimilarity(new File(file1), new File(file2));

        System.out.println("score of similarity: " + score);
    }
}
