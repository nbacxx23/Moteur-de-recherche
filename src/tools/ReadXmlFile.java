package tools;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Methods used to getting the information of article
 * @author Xiaoxiao CHEN
 * @since JDK1.8
 */

public class ReadXmlFile {
    // All paths should be changed according to the real locations
    /* path to different subindex(from Jaunary to April)*/
    public static String pathDir1 = "/Users/xchen/Documents/AIC/TC3/project_search_engine/subindex/2015/01/";

    public static String pathDir2 = "/Users/xchen/Documents/AIC/TC3/project_search_engine/subindex/2015/02/";

    public static String pathDir3 = "/Users/xchen/Documents/AIC/TC3/project_search_engine/subindex/2015/03/";

    public static String pathDir4 = "/Users/xchen/Documents/AIC/TC3/project_search_engine/subindex/2015/04/";

    /* path to generate the target corpus*/
    public static String pathOut = "/Users/xchen/Documents/AIC/TC3/project_search_engine/corpus/";

    /* path to the global corpus*/
    public static String pathText = "/Users/xchen/Documents/AIC/TC3/project_search_engine/2015";

    public static int numArticle = 0;

    public static HashMap<String, String[]> article = new HashMap<>();

    public ReadXmlFile() {

    }

    /**
     * Extracting the necessary information about a specific article
     * @param xmlFile
     * subindex file including all the articles information
     */
    public static void readFile(File xmlFile) {

        File outDir = new File(pathOut);

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {

            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            String date = doc.getDocumentElement().getAttribute("date");

            String month = date.substring(4, 6);

            String day = date.substring(6);

            NodeList nList = doc.getElementsByTagName("doc");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);

                Element ele = (Element) node;

                if (node.getNodeType() == Element.ELEMENT_NODE) {

                    String id = ele.getAttribute("id");

                    String filename = date + "_" + id + ".txt";

                    String pathname = pathText + "/" + month + "/" + day + "/";

                    filename = pathname + filename;
                    /* this function original used to copy the file from the global corpus to our own corpus

                    System.out.println(filename);

                    ProcessBuilder pb = new ProcessBuilder("cp",filename, pathOut);

                    Map<String, String> map = pb.environment();

                    Process p1 = pb.start();
                    */
                    String[] attribute = new String[2];

                    attribute[0] = ele.getTextContent();

                    attribute[1] = ele.getAttribute("url");

                    article.put(id, attribute);

                    numArticle++;


                }


            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Extracting the necessary information about the corpus's articles
     * @return article
     * article information including id, title and URL link
     */
    public static HashMap<String, String[]> getArticleInfo() {

        File dir1 = new File(pathDir1);
        File dir2 = new File(pathDir2);
        File dir3 = new File(pathDir3);
        File dir4 = new File(pathDir4);

        File[] files1 = dir1.listFiles();
        File[] files2 = dir2.listFiles();
        File[] files3 = dir3.listFiles();
        File[] files4 = dir4.listFiles();

        for (File file : files1) {
            readFile(file);
        }

        for (File file : files2) {
            readFile(file);
        }

        for (File file : files3) {
            readFile(file);
        }

        for (File file : files4) {
            readFile(file);
        }

        //System.out.println(article.size());

        return article;

    }
    /**
     public static void main(String[] args) throws IOException {

     File[] files1 = new File(pathDir1).listFiles();
     File[] files2 = new File(pathDir2).listFiles();
     File[] files3 = new File(pathDir3).listFiles();
     File[] files4 = new File(pathDir4).listFiles();

     for(File file:files1){
     readFile(file);
     }

     for(File file:files2){
     readFile(file);
     }

     for(File file:files3){
     readFile(file);
     }

     for(File file:files4){
     readFile(file);
     }

     }
     */
}
