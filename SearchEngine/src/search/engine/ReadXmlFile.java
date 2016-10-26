package search.engine;

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
import java.util.Map;

public class ReadXmlFile {

    public static String pathDir1 = "/Users/xchen/Documents/AIC/TC3/project/subindex/2015/01/";

    public static String pathDir2 = "/Users/xchen/Documents/AIC/TC3/project/subindex/2015/02/";

    public static String pathDir3 = "/Users/xchen/Documents/AIC/TC3/project/subindex/2015/03/";

    public static String pathDir4 = "/Users/xchen/Documents/AIC/TC3/project/subindex/2015/04/";

    public static String pathOut = "/Users/xchen/Documents/AIC/TC3/project/corpus/";

    public static String pathText = "/Users/xchen/Documents/AIC/TC3/project/2015";

    public static String year = "2015";

    public static int numArticle = 0;

    public static HashMap<String, String[]> article = new HashMap<>();

    public HashMap<String, String[]> getArticle(){
        return article;
    }

    public ReadXmlFile() {

    }

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

                //System.out.println("Root  element: " + doc.getDocumentElement().getNodeName());

                //System.out.println("date: " + doc.getDocumentElement().getAttribute("date"));

                String date = doc.getDocumentElement().getAttribute("date");

                String month = date.substring(4,6);

                String day =  date.substring(6);

                //System.out.println(month+day);

                NodeList nList = doc.getElementsByTagName("doc");

                for (int i = 0; i < nList.getLength(); i++) {

                    Node node = nList.item(i);

                    //System.out.println("Node name: " + node.getNodeName());
                    Element ele = (Element) node;

                    //System.out.println("----------------------------");
                    if (node.getNodeType() == Element.ELEMENT_NODE) {

                        String id = ele.getAttribute("id");

                        String filename = date+"_"+ id+".txt";

                        String pathname = pathText+"/"+month+"/"+day+"/";

                        filename = pathname+filename;

                        //System.out.println(filename);

                        String[] attribute = new String[2];

                        attribute[0] = ele.getTextContent();

                        attribute[1] = ele.getAttribute("url");

                        article.put(id,attribute);

                        numArticle++;

                        /**
                        ProcessBuilder pb = new ProcessBuilder("cp",filename, pathOut);

                        Map<String, String> map = pb.environment();

                        Process p1 = pb.start();

                        System.out.println("doc id: " + ele.getAttribute("id"));

                        System.out.println("url: " + ele.getAttribute("url"));

                        System.out.println("dct: " + ele.getAttribute("dct"));

                        System.out.println("dd :" + ele.getAttribute("dd"));

                        System.out.println("title:" + ele.getTextContent());

                        System.out.println("-------------------------");
                        */

                    }


                }
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(numArticle);
        }


    /**
     * Main, enter the key words to realize searching.
     */
    public static HashMap<String,String[]> getArticleInfor() {

        File dir1 = new File(pathDir1);
        File dir2 = new File(pathDir2);
        File dir3 = new File(pathDir3);
        File dir4 = new File(pathDir4);

        File[] files1 = dir1.listFiles();
        File[] files2 = dir2.listFiles();
        File[] files3 = dir3.listFiles();
        File[] files4 = dir4.listFiles();

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

        //System.out.println(article.size());

        return article;

    }
}