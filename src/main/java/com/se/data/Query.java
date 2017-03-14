package com.se.data;

/**
 * Created by Yathish on 3/13/17.
 */


//TODO: Class not used

public class Query {
    private String term;
    private Integer termFreq;

    public Query(String term, Integer termFreq) {
        this.term = term;
        this.termFreq = termFreq;
    }

    public String getTerm() { return term; }

    public void setTerm(String term) { this.term = term; }

    public Integer getTermFreq() { return termFreq; }

    public void setTermFreq(Integer termFreq) { this.termFreq = termFreq; }

}
