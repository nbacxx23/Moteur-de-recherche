package tp.tp4;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.nio.*;

/**
 * TP 3
 * @author Xiaoxiao CHEN
 *
 */

public class TP4 {

    /**
     * Le répertoire du corpus
     */
    private static String DIRNAME = "/Users/xchen/Documents/AIC/TC3/tp4/";

    public static void mergeInvertedFiles(File invertedFile1, File invertedFile2, File mergedInvertedFile) throws IOException{


        String ligne1 ;
        String ligne2 ;

        String[] ligne1_mot ;
        String[] ligne2_mot ;

        String mot_ligne1 ;
        String mot_ligne2 ;
     try {
         Reader fr1 = new InputStreamReader(new FileInputStream(invertedFile1), "ISO-8859-1");
         BufferedReader br1 = new BufferedReader(fr1);

         Reader fr2 = new InputStreamReader(new FileInputStream(invertedFile2), "ISO-8859-1");
         BufferedReader br2 = new BufferedReader(fr2);

         FileWriter fw = new FileWriter(new File(mergedInvertedFile,"index.ind.cxx.txt"));
         BufferedWriter ow = new BufferedWriter(fw);
         PrintWriter out = new PrintWriter(ow);

         ligne1 = br1.readLine();
         ligne2 = br2.readLine();

         while ((ligne1 != null) && (ligne2 != null)) {
             ligne1_mot = ligne1.split("\t");
             ligne2_mot = ligne2.split("\t");
             mot_ligne1 = ligne1_mot[0];
             mot_ligne2 = ligne2_mot[0];
             if (mot_ligne1.compareTo(mot_ligne2) == 0) {
                 Integer addition = Integer.parseInt(ligne1_mot[1]) + Integer.parseInt(ligne2_mot[1]);
                 String concate = ligne1_mot[2] + "," + ligne2_mot[2];
                 out.println(mot_ligne1 + "\t" + addition + "\t" + concate);
                 out.flush();
                 System.out.println(mot_ligne1 + "\t" + addition + "\t" + concate);
                 ligne1 = br1.readLine();
                 ligne2 = br2.readLine();
             } else if (mot_ligne1.compareTo(mot_ligne2) < 0) {
                 out.println(ligne1);
                 System.out.println(ligne1);
                 out.flush();
                 ligne1 = br1.readLine();
             } else  {
                 out.println(ligne2);
                 out.flush();
                 System.out.println(ligne2);
                 ligne2 = br2.readLine();
             }
         }

         if (ligne1 == null) {
             while (ligne2 != null) {
                 out.println(ligne2);
                 out.flush();
                 System.out.println("oaoa");
                 ligne2 = br2.readLine();
             }
         } else {
             while (ligne1 != null) {
                 out.println(ligne1);
                 out.flush();
                 System.out.println("oaoa");
                 ligne1 = br1.readLine();
             }
         }
         out.close();
     }catch (Exception e){
         System.out.println(e.toString());
     }
    }



    /**
     * Main, appels de toutes les méthodes des exercices du TD1.
     */
    public static void main(String[] args) {
        try{
            String outDirName = "/Users/xchen/Documents/AIC/TC3/tp4/";

            File invertedFile1 = new File(DIRNAME+"index1.ind.txt");

            File invertedFile2 = new File(DIRNAME+"index2.ind.txt");

            File mergedInvertedFile = new File(outDirName);

            mergeInvertedFiles(invertedFile1,invertedFile2,mergedInvertedFile);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
