package search.engine;

import tool.search.InvertedIndex;
import tools.FrenchStemmer;
import tools.FrenchTokenizer;
import tools.Normalizer;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Creating index that stores all the necessary information.
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */
public class Index {

    /**
     * Le fichier contenant les mots vides
     */
    private static String STOPWORDS_FILENAME = "/Users/xchen/Documents/AIC/TC3/project_search_engine/frenchST.txt";

    /**
     * Le répertoire du corpus
     */
    public static String corpus = "/Users/xchen/Documents/AIC/TC3/project_search_engine/corpus";

    // the path to the inverted index
    public static String pathInvertedFile = "/Users/xchen/Documents/AIC/TC3/project_search_engine/invertedindex";

    // the path to the inverted index stemming
    public static String pathInvertedFileStemming = "/Users/xchen/Documents/AIC/TC3/project_search_engine/invertedindex_stemming";

    public static String hashIdFile = "/Users/xchen/Documents/AIC/TC3/project_search_engine/hashId";

    /**
     * InvertedFile with words associated to the document it appears and the frequency in each document
     */
    public static TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeight = new TreeMap<String, TreeMap<String, Integer>>();

    public static TreeMap<String, TreeMap<String, Integer>> invertedFileWithWeightStemming = new TreeMap<String, TreeMap<String, Integer>>();


    /**
     * Mapping the article's real id to its hashID
     * @param corpusFiles
     * The corpus
     * @return associatedId
     * Mapping relationship between the article's real id and its hashID
     */
    public static HashMap<String,String> hashInvertedIndex(File corpusFiles){

        HashMap<String,String> associatedId = new HashMap<>();

        if (corpusFiles.isDirectory()) {

            File[] corpusFile = corpusFiles.listFiles();

            Integer i = 1;

            for(File file:corpusFile){

                file.renameTo(new File(file.getParent()+"/"+i+".txt"));
                associatedId.put(i.toString(),file.getName());
                //System.out.println(i.toString()+"\t"+file.getName());
                i++;
            }
        }
        return  associatedId;
    }

    /**
     * Saving the hashID file
     * @param associatedId
     * Mapping relationship between the article's real id and its hashID
     * @param outFile
     * Path to store the hashID
     */
    public static void writeHashDocId(HashMap<String,String> associatedId,File outFile){

        if (!outFile.exists()) {
            outFile.mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(new File(outFile, "Hash_ID"));
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);

            String name;
            String id;
            for (Map.Entry<String, String> hashId : associatedId.entrySet()) {
                name = hashId.getValue();
                id = hashId.getKey();

                out.println(id + "\t" + name );
            }
            out.close();
        }catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    /**
     * Main, procedure of establishing the inverted index.
     */
    public static void main(String[] args) throws IOException, InterruptedException {

        TimeUnit.SECONDS.sleep(20);

        HashMap<String,String> associtedId = new HashMap<>();

        Normalizer tokenizerNoStopWords = new FrenchTokenizer(new File(STOPWORDS_FILENAME));

        Normalizer stemmerNoStopWords = new FrenchStemmer(new File(STOPWORDS_FILENAME));

        File fileCorpus = new File(corpus);

        associtedId = hashInvertedIndex(fileCorpus);

        writeHashDocId(associtedId,new File(hashIdFile));

        invertedFileWithWeight = InvertedIndex.getInvertedFileWithWeights(fileCorpus,tokenizerNoStopWords);

        invertedFileWithWeightStemming = InvertedIndex.getInvertedFileWithWeights(fileCorpus,stemmerNoStopWords);

        InvertedIndex.saveInvertedFile(invertedFileWithWeight,new File(pathInvertedFile));

        InvertedIndex.saveInvertedFile(invertedFileWithWeightStemming,new File(pathInvertedFileStemming));

    }
}
