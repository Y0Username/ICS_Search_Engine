import java.util.List;

/**
 * Created by Yathish on 3/3/17.
 */
public class WordFreq {
    private String term;
    private Integer frequency;
    private List<Integer> positions;

    public WordFreq(String term, Integer frequency, List<Integer> positions) {
        this.term = term;
        this.frequency = frequency;
        this.positions = positions;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }
}
