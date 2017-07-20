package com.IR.core;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Math;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {
    private int document_num;
    private ArrayList<HashMap<String, Double>> docMap;
    private HashMap<String, String> titleToContent; //建立文章标题与内容的映射
    private ArrayList<String> docCollection;

    Document() throws IOException {
        docMap = new ArrayList<>();
        titleToContent = new HashMap<>();
        docCollection = new ArrayList<>();
        readFile("Docs/");
        parseDoc();
    }
    private void readFile(String path) throws IOException {
        ArrayList<String> res = new ArrayList<String>();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件路径名不存在！");
        }
        String[] docNameList = file.list();
        String content;
        document_num = docNameList.length;
        for (int i = 0; i < docNameList.length; i++) {
            content = readFileHelper(docNameList[i]);
            content = cleanDoc(content);
            docCollection.add(content);
            if (!titleToContent.containsKey(docNameList[i]))
                titleToContent.put(docNameList[i], docCollection.get(i));
        }
    }
    private String readFileHelper(String filePath) throws IOException {
        StringBuffer sb = new StringBuffer();
        readToBuffer(sb, filePath);
        return sb.toString();
    }
    private void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream("Docs/" + filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String line;
        line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            buffer.append('\n');
            line = reader.readLine();
        }
        reader.close();
        is.close();
    }
    private String cleanDoc(String str) {
        return str;
    }
    private void parseDoc() {
        ArrayList<String> wordsList = new ArrayList<>();
        for (int i = 0; i < document_num; i++) {
            wordsList.clear();
            HashMap<String, Double> tmpMap = new HashMap<>();
            List<Term> words = StandardTokenizer.segment(docCollection.get(i));
            for (int j = 0; j < words.size(); j++) {
                if (isSpecialChar(words.get(j))) continue;
                if (words.get(j).word == "(")
                    words.get(j).word = "\\(";
                wordsList.add(words.get(j).word);
            }
            for (String str : wordsList) {
                if (!tmpMap.containsKey(str)) {
                    tmpMap.put(str, cacculateWeight(str, i));
                }
            }
            docMap.add(tmpMap);
        }
    }
    private boolean isSpecialChar(Term str) {
        if (str == null) return true;
        if (str.nature == Nature.mg || str.nature == Nature.xx
                || str.nature == Nature.wkz || str.nature == Nature.wky
                || str.nature == Nature.x) return true;
        return false;
    }
    private double cacculateTF(String str, int docIdx) {
        String docText = docCollection.get(docIdx);
        System.out.println(str + " " + docIdx + " " + docText);
        double cnt = 0.0;
        double len = docText.length();
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(docText);
        while (m.find()) cnt++;
        if (cnt == 0.0) cnt = 1.0;
        return cnt / len;
    }
    private double cacculateIDF(String str) {
        String word = str;
        int df = 0;
        for (HashMap<String, Double> map : docMap) {
            if (map.containsKey(word))
                df++;
        }
        if (df == 0)
            df = 1;
        return Math.log(document_num / df);
    }
    private double cacculateWeight(String str, int docIdx) {
        return cacculateIDF(str) * cacculateTF(str, docIdx);
    }

    public int getDocNum() {
        return document_num;
    }
    public ArrayList<HashMap<String, Double>> getDocMap() {
        return docMap;
    }

    public ArrayList<String> getDocCollection() {
        return docCollection;
    }

    public static void main(String[]  args) throws IOException {
        Document doc = new Document();
        ArrayList<String> collection = doc.docCollection;
        ArrayList<HashMap<String, Double>> maps = doc.getDocMap();
        int i = 0;
//        for (HashMap<String, Double> map : maps) {
//            System.out.println("No." + i++ +" ");
//            for (Map.Entry<String, Double> entry : map.entrySet()) {
//                System.out.println("Key = " + entry.getKey() +",Weight = " + entry.getValue());
//            }
//        }
    }
}

