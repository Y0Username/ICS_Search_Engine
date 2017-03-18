package com.se.util;

import com.se.data.SearchResult;
import com.se.file.FileHandler;
import com.se.query.QueryRunner;

import java.util.*;

/**
 * Created by Yathish on 3/17/17.
 */
public class NDCG {
    public static void main(String[] args) {
        String query = "mondego";
        Map<String, Double> googleMap = FileHandler.relevancyReader(query);
        List<Double> googleList = new ArrayList<>(googleMap.values());

        QueryRunner queryRunner = new QueryRunner();
        List<SearchResult> SearchResults = queryRunner.search(query);
        List<Double> chotheList = mapNDCG(query, SearchResults, googleMap);

        List<Double> ndcg = calculateNDCG(googleList, chotheList);
        System.out.println(ndcg);
    }

    public static List<Double> mapNDCG(String query, List<SearchResult> SearchResults, Map<String, Double> googleMap){
        Map<String, Double> chotheMap = new LinkedHashMap<>();

        if(query.equals("information retrieval")){
            SearchResults = SearchResults.subList(0, 5);
        }
        for (SearchResult result : SearchResults) {
            if(googleMap.containsKey(result.getUrl())){
                chotheMap.put(result.getUrl(), googleMap.get(result.getUrl()));
            }
            else{
                chotheMap.put(result.getUrl(), 0.0);
            }
        }
        List<Double> chotheList = new ArrayList<>(chotheMap.values());
        return chotheList;
    }

    public static List<Double> calculateNDCG(List<Double> googleList, List<Double> chotheList){
        for(int i = 1; i < googleList.size(); i++){
            googleList.set(i, googleList.get(i) / (Math.log10(i+1) / Math.log10(2)));
            googleList.set(i, (googleList.get(i) + googleList.get(i-1)));
            chotheList.set(i, chotheList.get(i) / (Math.log10(i+1) / Math.log10(2)));
            chotheList.set(i, (chotheList.get(i) + chotheList.get(i-1)));
        }
        List<Double> ndcg = new ArrayList<>();
        for(int i = 0; i < googleList.size(); i++){
            ndcg.add(chotheList.get(i) / googleList.get(i));
        }
        return ndcg;
    }

}
