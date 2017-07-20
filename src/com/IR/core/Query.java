package com.IR.core;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.oracle.tools.packager.mac.MacAppBundler;

import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.io.*;
import java.util.List;
import java.util.Map;

public class Query {
    private int word_num;
    private ArrayList<String> queryWords;
    private HashMap<String, Double> queryMap;
    private double[] simmMatrix;

    Query(Document doc) throws IOException{
        ArrayList<String> parseResult = parseQuery();
        word_num = parseResult.size();
        queryWords = parseResult;
        queryMap = new HashMap<>();
        simmMatrix = new double[doc.getDocNum()];
        cacculateTermWeight(doc);
        cacculateSimm(doc);
    }
    private ArrayList<String> parseQuery() throws IOException {
        String query = Input();
        ArrayList<String> queryWords = new ArrayList<>();
        List<Term> splitWords = IndexTokenizer.segment(query);
        for (int i = 0; i < splitWords.size(); i++)
            queryWords.add(splitWords.get(i).word);
        return queryWords;
    }
    private String Input() throws IOException {
        String query ;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入搜索关键字： ");
        query = br.readLine();
        return query;
    }
    private double cacculateIDF(String str, Document doc) {
        int df = 0;
        ArrayList<HashMap<String, Double>> maps = doc.getDocMap();
        int  n = doc.getDocNum();
        for (int i = 0; i < n; i++) {
            if (maps.get(i).containsKey(str))
                df++;
        }
        if (df == 0)
            df = 1;
        //System.out.println(Math.log(n / df));
        return Math.log(n / df);
    }
    private void cacculateTermWeight(Document doc) {
        for (String str : queryWords) {
            if (!queryMap.containsKey(str))
                queryMap.put(str, cacculateIDF(str, doc));
        }
    }
    //计算doc和query的相似度
    private void cacculateSimm(Document doc) {
        int n = doc.getDocNum();
        int m = word_num;
        double tmp = 0.0;
        int i, j;
        for (i = 0; i < n; i++) {
            System.out.println();
            for (j = 0; j < m; j++) {
                HashMap<String, Double> docmap = doc.getDocMap().get(i);
                for (Map.Entry<String, Double> entry : docmap.entrySet()) {
                    //System.out.print("Key= " + entry.getKey() + " ");
                }
                if (docmap.containsKey(queryWords.get(j))) {
                    tmp += docmap.get(queryWords.get(j)) * queryMap.get(queryWords.get(j));
                    //System.out.println(docmap.get(queryWords.get(j)) + " " + queryMap.get(queryWords.get(j)));
                }
            }
            simmMatrix[i] = tmp;
            tmp = 0.0;
        }
    }

    public int getWordNum() {
        return  word_num;
    }
    public ArrayList<String> getQueryWords() {
        return queryWords;
    }
    public HashMap<String, Double> getQueryMap() {
        return queryMap;
    }

    public void showQueryResult(Document doc) {
        int maxSimmIdx = 0;
        double maxSimm = 0.0;
        for (int i = 0; i < doc.getDocNum(); i++) {
            if (simmMatrix[i] > maxSimm) {
                maxSimm = simmMatrix[i];
                maxSimmIdx = i;
            }
        }
        System.out.println("检索结果： " + doc.getDocCollection().get(maxSimmIdx));
    }

    public double[] getSimmMatrix() {
        return simmMatrix;
    }

    public static void main(String[] args) throws IOException {
        Document doc = new Document();
        Query query = null;
        try {
            query = new Query(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        for (int i = 0; i < doc.getDocNum(); i++)
//            System.out.println(query.getSimmMatrix()[i]);
        query.showQueryResult(doc);
    }
}

