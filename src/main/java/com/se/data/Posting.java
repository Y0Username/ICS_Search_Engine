package com.se.data;
import java.util.List;

/**
 * Created by Yathish on 3/2/17.
 */

public class Posting {
    private Integer docID;
    private Integer termFreq;
    private List<Integer> positions;

    public Posting(Integer docID, Integer termFreq, List<Integer> positions) {
        this.docID = docID;
        this.termFreq = termFreq;
        this.positions = positions;
    }

    public Integer getDocID() {
        return docID;
    }

    public void setDocID(Integer docID) {
        this.docID = docID;
    }

    public Integer getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(Integer termFreq) {
        this.termFreq = termFreq;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

}
