/**
 * Created by Yathish on 3/2/17.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;
public class Tokenizer {

    public static void walker(String path, List<File> allFiles) {
        File root = new File(path);
        File[] list = root.listFiles();
        if (list == null) return;
        for (File f: list) {
            if (f.isDirectory()) {
                walker(f.getAbsolutePath(), allFiles);
            }
            else {
                allFiles.add(f.getAbsoluteFile());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        List<File> allFiles = new ArrayList<File>();
        walker("/Users/Yathish/Desktop/School/Winter 2017/CS221/Project3/walks", allFiles);
        Map<String, List<Posting>> postingListMap = new HashMap<String, List<Posting>>();
        for(File files: allFiles) {
            Map<String, Posting> postingMap = new HashMap<String, Posting>();
            String docID = files.toString().replaceAll("/Users/Yathish/Desktop/School/Winter 2017/CS221/Project3/walks/","");
            String bText;
            try {
                Document doc = Jsoup.parse(files, "UTF-8");
                bText = doc.body().text();
            } catch (Exception e) {
                System.out.println(files);
                System.out.println("Error while parsing: " + e);
                continue;
            }
            bText = bText.toLowerCase();
            Matcher m = Pattern.compile("[^\\W_]+").matcher(bText);
            //TODO: Stemming and stop word removal
            int pos = 1;
            while (m.find()) {
                if(postingMap.containsKey(m.group(0))){
                    Posting seenTerm = postingMap.get(m.group(0));
                    seenTerm.setTermFreq((seenTerm.getTermFreq() + 1));
                    List<Integer> posList = seenTerm.getPositions();
                    posList.add(pos);
                    seenTerm.setPositions(posList);
                    postingMap.put(m.group(0), seenTerm);
                }
                else{
                    List<Integer> posList = new ArrayList<Integer>();
                    posList.add(pos);
                    Posting newTerm = new Posting(docID, 1, posList);
                    postingMap.put(m.group(0), newTerm);
                }
                pos++;
            }
            for (String term : postingMap.keySet()) {
                if (postingListMap.containsKey(term)){
                    List<Posting> postingList = postingListMap.get(term);
                    postingList.add(postingMap.get(term));
                    postingListMap.put(term, postingList);
                }
                else{
                    List<Posting> postingList = new ArrayList<Posting>();
                    postingList.add(postingMap.get(term));
                    postingListMap.put(term, postingList);
                }
            }
        }
        //Printing the final Postings List for each term
        for(String terms : postingListMap.keySet()){
            System.out.println("Term: " + terms);
            List<Posting> postingList = postingListMap.get(terms);
            for(Posting pList : postingList){
                System.out.println("DocID: " + pList.getDocID().toString());
                System.out.println("Term Frequency: " + pList.getTermFreq());
                System.out.print("Positions:");
                for(Integer posi : pList.getPositions()){
                    System.out.print(" " + posi);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}