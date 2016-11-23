package search.engine;

import tool.search.Fusion;
import tool.search.Similarity;
import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * A simple Search Engine with stemming.
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */
public class SearchEngineStemming {

    public static String[] keyWords;

    public static String keyWordR;

    /**
     * The information of every article including title and url
     */
    public static HashMap<String, String[]> article = new HashMap<>();

    public static TreeSet<String> relativeFiles = new TreeSet<>();

    public static Set<File> relativeFilesSet = new TreeSet<>();

    public static List<Map.Entry<String, Double>> scoreDoc = new ArrayList<Map.Entry<String, Double>>();

    /**
     * Le fichier contenant les mots vides
     */
    private static String STOPWORDS_FILENAME = "/Users/xchen/Documents/AIC/TC3/project_search_engine/frenchST.txt";

    /**
     * Le répertoire du corpus
     */
    public static String corpus = "/Users/xchen/Documents/AIC/TC3/project_search_engine/corpus";

    public static HashMap<String, Integer> df = new HashMap<>();

    public static HashMap<String, TreeMap<String, Integer>> doc = new HashMap<>();

    public static HashMap<String, TreeMap<String, Integer>> keyDoc = new HashMap<>();

    // the path to the inverted file
    public static String pathInvertedFile = "/Users/xchen/Documents/AIC/TC3/project_search_engine/invertedindex_stemming/";

    /**
     * Extracting the weight information of every word
     * @param index
     * inverted index
     */
    public static void getAllwordTfDf(File index) {

        TreeMap<String, Integer> tfKeyword;

        String wordIndexLine;
        String[] wordIndex;

        try {
            FileReader fr = new FileReader(index);
            BufferedReader br = new BufferedReader(fr);


            while ((wordIndexLine = br.readLine()) != null) {

                //System.out.println(wordIndexLine);
                wordIndex = wordIndexLine.split("\t");
                df.put(wordIndex[0], Integer.parseInt(wordIndex[1]));
                tfKeyword = readTf(wordIndex[2]);
                doc.put(wordIndex[0], tfKeyword);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracting the weight information of requested words
     * @param keyWord
     * Requested words
     */
    public static void getKeywordTfDf(String[] keyWord) {

        for (String key : keyWord) {
            if (doc.containsKey(key)) {
                keyDoc.put(key, doc.get(key));
            }

        }
    }

    /**
     * Extracting Term Frequency corresponding to the article
     * @param tfInfo
     * Term Frequency information
     * @return tfMap
     * Mapping relationship between article and word's term frequency
     */
    public static TreeMap<String, Integer> readTf(String tfInfo) {

        TreeMap<String, Integer> tfMap = new TreeMap<String, Integer>();

        String regEx = "[, ]+";
        Pattern pattern = Pattern.compile(regEx);
        String[] tf = pattern.split(tfInfo);
        tf[0] = tf[0].replaceAll("\\{", "");
        tf[tf.length - 1] = tf[tf.length - 1].replaceAll("\\}", "");

        for (int i = 0; i < tf.length; i++) {
            String[] temp = tf[i].split("=");
            tfMap.put(temp[0], Integer.parseInt(temp[1]));
        }

        return tfMap;
    }


    /**
     * Main, enter the key words to run the searching.
     */
    public static void main(String[] args) throws IOException {

        Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));

        System.out.println("This is a simple search engine." + "\n" +
                "The corpus are articles of 'Le Monde' published from January 2015 to April 2015." + "\n" +
                "Enter the key words separated by white space. " + "\n");
        System.out.print("Key word:");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            keyWordR = br.readLine();
            ArrayList<String> keyList = stemmerNoStopWords.normalize(keyWordR);
            keyWords = new String[keyList.size()];
            keyWords = keyList.toArray(keyWords);
            /*
            for (String key : keyWords) {
                System.out.println(key);
            }
            */
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get article meta date
        article = tools.ReadXmlFile.getArticleInfo();

        File invertedindex = new File(pathInvertedFile, "Inverted_Index");

        getAllwordTfDf(invertedindex);

        getKeywordTfDf(keyWords);

        if (keyDoc.size() == 0) {

            System.out.print("Your search - " + keyWordR + " - did not match any documents");

            System.exit(0);
        }

        // fusion of the articles
        relativeFiles = Fusion.fusionList(keyDoc);

        for (String reFile : relativeFiles) {

            relativeFilesSet.add(new File(corpus + "/" + reFile + ".txt"));

        }

        // return all the articles ranked by their similarity score to the requested words
        scoreDoc = Similarity.getSimilarDocuments(keyWords, relativeFilesSet, df, doc, stemmerNoStopWords);

        for (int i = 0; i < scoreDoc.size(); i++) {

            System.out.println(
                    "title:" + article.get(((Map.Entry) scoreDoc.get(i)).getKey().toString().substring(9).replace(".txt", ""))[0] + "\t" + "   url:" +
                            article.get(((Map.Entry) scoreDoc.get(i)).getKey().toString().substring(9).replace(".txt", ""))[1]+"\n");
                            //+ "\t" + " score:" + ((Map.Entry) scoreDoc.get(i)).getValue());

        }


    }

}