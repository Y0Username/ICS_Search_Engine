package com.se.data;

import java.util.Map;

/**
 * Created by Yathish on 3/8/17.
 */
public class parsedDocument {
    private Integer docLength;
    private Map<String, Posting> postingMap;

    public parsedDocument(Integer docLength, Map<String, Posting> postingMap) {
        this.docLength = docLength;
        this.postingMap = postingMap;
    }

    public Integer getDocLength() {
        return docLength;
    }

    public void setDocLength(Integer docLength) {
        this.docLength = docLength;
    }

    public Map<String, Posting> getPostingMap() {
        return postingMap;
    }

    public void setPostingMap(Map<String, Posting> postingMap) {
        this.postingMap = postingMap;
    }
}
