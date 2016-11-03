package search.engine;

import tool.search.Fusion;
import tool.search.InvertedIndex;
import tool.search.Similarity;
import tool.search.TfIdf;
import tools.FrenchTokenizer;
import tools.Normalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A simple Search Engine.
 */
public class SearchEngine {

    public static String[] keyWords;

    public static String keyWordR;

    public static HashMap<String, String[]> article = new HashMap<>();

    public static TreeMap<String, TreeSet<String>> keyDoc = new TreeMap<>();

    public static TreeSet<String> relativeFiles = new TreeSet<>();

    public static Set<File> relativeFilesWeight = new HashSet<>();

    public static TreeSet<String> relativeFilesId = new TreeSet<>();

    public static List<Map.Entry<String,Double>> scoreDoc = new ArrayList<Map.Entry<String, Double>>();

    /**
     * Le répertoire du corpus
     */
    public static String corpus = "/Users/xchen/Documents/AIC/TC3/project_search_engine/corpus";

    /**
     * Le fichier contenant les mots vides
     */
    private static String STOPWORDS_FILENAME = "/Users/xchen/Documents/AIC/TC3/project_search_engine/frenchST.txt";


    // the path to the weighted file
    public static String pathWeightedFile = "/Users/xchen/Documents/AIC/TC3/project_search_engine/weightedfile";

    // the path to the inverted file
    public static String pathInvertedFile = "/Users/xchen/Documents/AIC/TC3/project_search_engine/invertedfile";

    public static TreeMap<String, TreeSet<String>> invertedFile = new TreeMap<String, TreeSet<String>>();

    public static TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeight = new TreeMap<String, TreeMap<String, Integer>>();

    /**
     * Main, enter the key words to realize searching.
     */
    public static void main(String[] args) throws IOException {

        Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));


        System.out.println("This is a simple search engine." + "\n" +
                "The corpus are articles of 'Le Monde' published from January 2015 to April 2015." + "\n" +
                "Enter the key words separated by white space. " + "\n");
        System.out.print("Key word:");

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            keyWordR = br.readLine();
            keyWords = keyWordR.split(" ");
            for(String key:keyWords){
                System.out.println(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        article = tools.ReadXmlFile.getArticleInfor();

        File fileCorpus = new File(corpus);
        invertedFile = InvertedIndex.getInvertedFile(fileCorpus, tokenizerNoStopWords);
        //System.out.println(invertedFile.size());
        //InvertedIndex.saveInvertedFile(invertedFile,new File(pathInvertedFile));
        //invertedFileWithWeight = InvertedIndex.getInvertedFileWithWeights(fileCorpus,tokenizerNoStopWords);

        //TfIdf.getWeightFiles(fileCorpus,new File(pathWeightedFile),tokenizerNoStopWords);
        for (String keyWord : keyWords) {
            if (invertedFile.containsKey(keyWord)) {
                keyDoc.put(keyWord, invertedFile.get(keyWord));
                //System.out.println("word:" + keyWord + "  doc:" + invertedFile.get(keyWord).size());
            } else{
                System.out.print("Your search - "+keyWordR+" - did not match any documents");
                System.exit(0);
            }
        }
        if(keyDoc.size()==0){
            System.out.print("Your search - "+keyWordR+" - did not match any documents");
            System.exit(0);
        }
        relativeFiles = Fusion.fusionList(keyDoc);

        for(String reFile:relativeFiles ){
            relativeFilesWeight.add(new File(pathWeightedFile+"/"+reFile.replace("txt","poids")));
            //System.out.println(pathWeightedFile+"/"+reFile.replace("txt","poids"));
            relativeFilesId.add(reFile.substring(9).replace(".txt",""));
            //System.out.println(reFile);
        }

        System.out.println(relativeFilesWeight.size());

        scoreDoc = Similarity.getSimilarDocuments(keyWords,relativeFilesWeight);

        System.out.println(scoreDoc.size());

        for (int i = 0; i < scoreDoc.size(); i++) {

            System.out.println(
                    "title:"+article.get(((Map.Entry)scoreDoc.get(i)).getKey().toString().substring(9).replace(".poids",""))[0]+"\t"+"   url:"+
                            article.get(((Map.Entry)scoreDoc.get(i)).getKey().toString().substring(9).replace(".poids",""))[1]+"\t"+" score:"+((Map.Entry)scoreDoc.get(i)).getValue());

        }


    }


}
