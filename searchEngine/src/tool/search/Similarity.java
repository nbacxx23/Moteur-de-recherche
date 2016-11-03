package tool.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * functions to calculate the similarity score of documents.
 */

public class Similarity {

    public static double getSimilarity(String[] keywords, File file) {

        double scoreSimilarity = 0;
        double sum_d_j_k = 0;
        double sum_d_j = 0;
        double sum_d_k = 0;

        HashMap<String, Double> tfidfKey = new HashMap<String, Double>();
        HashMap<String, Double> tfidfFile = new HashMap<String, Double>();
        HashMap<String, Double[]> tfidf = new HashMap<String, Double[]>();

        try {

            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_r;
            String[] lineFile;
            TreeSet<String> words = new TreeSet<>();

            while ((line_r = br.readLine()) != null) {
                lineFile = line_r.split("\t");
                tfidfFile.put(lineFile[0], Double.parseDouble(lineFile[1]));
                if (!words.contains(lineFile[0])) {
                    words.add(lineFile[0]);
                }
            }

            for(String key:keywords){
                tfidfKey.put(key,Double.parseDouble("1"));
                if(!words.contains(key)){
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

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return scoreSimilarity;
    }

    public static List getSimilarDocuments(String[] keywords, Set<File> fileList) {

        NavigableMap<Object,Object> scoreMap = new TreeMap<>().descendingMap();

        System.out.println(fileList.size());

        for(File fileCompare:fileList){

            double scoreCompare = getSimilarity(keywords,fileCompare);

            scoreMap.put(fileCompare.getName(),scoreCompare);

        }

        List scoreList = new ArrayList(scoreMap.entrySet());

        Collections.sort(scoreList, new Comparator() {
            public int compare(Object o1, Object o2) {
                Map.Entry obj1 = (Map.Entry) o1;
                Map.Entry obj2 = (Map.Entry) o2;

                return ((Double) obj2.getValue()).compareTo((Double) obj1
                        .getValue());
            }
        });

        /**
         for (int i = 0; i < scoreList.size(); i++) {
         System.out.println("(" + ((Map.Entry) scoreList.get(i)).getKey()
         + "," + ((Map.Entry) scoreList.get(i)).getValue() + ")");
         }
         */
        System.out.println(scoreMap.size());

        /*
        for(Map.Entry<Object,Object > score : scoreMap.entrySet()){
            System.out.println(score.getValue()+"\t"+score.getKey());
        }
        */

        return scoreList;
    }




}
