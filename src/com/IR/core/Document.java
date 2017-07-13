package com.IR.core;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.lang.Math;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Document {
    private int document_num;
    private ArrayList<HashMap<String, Double>> docMap;
    private HashMap<String, ArrayList<String>> titleToContent; //建立文章标题与内容的映射
    private ArrayList<String> docCollection;

    Document(int doc_num) {
        document_num = doc_num;
        docMap = new ArrayList<>();
        titleToContent = new HashMap<>();
        docCollection = new ArrayList<>();
        readFile("Docs/");
        setTitleToContent();
        parseDoc();
    }
    private void readFile(String path) {
        ArrayList<String> res = new ArrayList<String>();
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("文件路径名不存在！");
        }
        String[] docNameList = file.list();
        for (int i = 0; i < docNameList.length; i++) {
            docNameList[i] = docNameList[i].split("\\.")[0];
            docCollection.add(docNameList[i]);
        }
    }
    private void setTitleToContent() {

    }
    private void parseDoc() {
        ArrayList<String> wordsList = new ArrayList<>();
        for (int i = 0; i < document_num; i++) {
            wordsList.clear();
            HashMap<String, Double> tmpMap = new HashMap<>();
            List<Term> words = IndexTokenizer.segment(docCollection.get(i));
            for (int j = 0; j < words.size(); j++) {
                wordsList.add(words.get(j).word);
            }
            for (String str : wordsList) {
                if (str == null) continue;
                if (!tmpMap.containsKey(str)) {
                    tmpMap.put(str, cacculateWeight(str, i));
                }
            }
            docMap.add(tmpMap);
        }
    }
    private double cacculateTF(String str, int docIdx) {
        String docText = docCollection.get(docIdx);
        double cnt = 0.0;
        double leng = docText.length();
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(docText);
        while (m.find()) cnt++;
        if (cnt == 0.0) cnt = 1.0;
        return cnt / leng;
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

    public static void main(String[]  args) {
        Document doc = new Document(26);
        ArrayList<String> collection = doc.docCollection;
        ArrayList<HashMap<String, Double>> maps = doc.getDocMap();
        int i = 0;
        for (HashMap<String, Double> map : maps) {
            System.out.println("No." + i++ +" ");
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                System.out.println("Key = " + entry.getKey() +",Weight = " + entry.getValue());
            }
        }
    }
}

