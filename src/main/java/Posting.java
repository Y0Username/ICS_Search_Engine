import java.util.List;

/**
 * Created by Yathish on 3/2/17.
 */
public class Posting {
    private String docID;
    private Integer termFreq;
    private List<Integer> positions;

    public Posting(String docID, Integer termFreq, List<Integer> positions) {
        this.docID = docID;
        this.termFreq = termFreq;
        this.positions = positions;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
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
