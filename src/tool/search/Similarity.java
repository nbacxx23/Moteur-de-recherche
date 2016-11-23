package tool.search;

import tools.Normalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * functions to calculate the similarity score of documents.
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */

public class Similarity {

    /* the path to the HashId File */
    public static String pathHashId = "/Users/xchen/Documents/AIC/TC3/project_search_engine/hashId/Hash_ID";

    /* the total number of articles in the corpus*/
    public static Integer N = 9714;

    /**
     * Extracting the necessary information about a specific article
     * @param keywords
     * subindex file including all the articles information
     * @param file
     * the article to be dealt with
     * @param df
     * document frequency information for every word in the index
     * @param doc
     * Hashmap contains the information of the term frequency of every article for each word in the index
     * @param normalizer
     * The normalizer used to process the text
     * @return scoreSimilarity
     * Similarity score of this document compared to the requested words
     */
    public static double getSimilarity(String[] keywords, File file, HashMap<String, Integer> df, HashMap<String, TreeMap<String, Integer>> doc,
                                       Normalizer normalizer) throws IOException {

        double scoreSimilarity = 0;
        double sum_d_j_k = 0;
        double sum_d_j = 0;
        double sum_d_k = 0;


        HashMap<String, Double> tfidfKey = new HashMap<String, Double>();
        HashMap<String, Double> tfidfFile = new HashMap<String, Double>();
        HashMap<String, Double[]> tfidf = new HashMap<String, Double[]>();

        tfidfFile = TfIdf.getTfIdf(file, N, df, doc, normalizer);

        TreeSet<String> words = new TreeSet<>();

        words.addAll(tfidfFile.keySet());

        for (String key : keywords) {
            tfidfKey.put(key, Double.parseDouble("1"));
            if (!words.contains(key)) {
                words.add(key);
            }

        }

        for (String word : words) {
            //System.out.println(word);
            Double[] weight = new Double[2];
            if (tfidfKey.containsKey(word)) {
                weight[0] = tfidfKey.get(word);
            } else {
                weight[0] = (double) 0;
            }

            if (tfidfFile.containsKey(word)) {
                weight[1] = tfidfFile.get(word);
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

    return scoreSimilarity;
}


    /**
     * Extracting the necessary information about a specific article
     * @param keywords
     * subindex file including all the articles information
     * @param fileList
     * All the results file to be ranked
     * @param df
     * document frequency information for every word in the index
     * @param doc
     * Hashmap contains the information of the term frequency of every article for each word in the index
     * @param normalizer
     * The normalizer used to process the text
     * @return scoreList
     * Similarity score of every document compared to the requested words
     */
    public static List getSimilarDocuments(String[] keywords, Set<File> fileList, HashMap<String, Integer> df, HashMap<String, TreeMap<String, Integer>> doc,
                                           Normalizer normalizer) throws IOException {

        NavigableMap<Object, Object> scoreMap = new TreeMap<>().descendingMap();

        HashMap<String,String> scoreDocT = new HashMap<>();
        try{
            //through HashId to extract the real id of every article
            FileReader fr = new FileReader(new File(pathHashId));
            BufferedReader br = new BufferedReader(fr);
            String lineR;
            String[] line;
            while((lineR = br.readLine())!=null){
                line = lineR.split("\t");
                scoreDocT.put(line[0],line[1]);
            }
        }catch(IOException e){
            e.printStackTrace();
        }

        //get similarity score of every article
        for (File fileCompare : fileList) {

            double scoreCompare = getSimilarity(keywords, fileCompare, df, doc, normalizer);

            String nameId = scoreDocT.get(fileCompare.getName().replaceAll(".txt",""));

            scoreMap.put(nameId, scoreCompare);

        }

        //Transform the Treemap to list to rank according the value
        List scoreList = new ArrayList(scoreMap.entrySet());

        Collections.sort(scoreList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;

                return ((Double) obj2.getValue()).compareTo((Double) obj1
                        .getValue());
            }
        });


        return scoreList;
    }


}
