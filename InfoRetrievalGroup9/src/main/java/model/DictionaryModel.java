package model;

import java.util.LinkedHashSet;

public class DictionaryModel {

    private String term;
    private Long docFreq;
    private LinkedHashSet<Long> postSet;
    private LinkedHashSet<IndexPositionModel> indexPositionSet;

    public LinkedHashSet<IndexPositionModel> getIndexPositionSet() {
        return indexPositionSet;
    }

    public void setIndexPositionSet(LinkedHashSet<IndexPositionModel> indexPositionSet) {
        this.indexPositionSet = indexPositionSet;
    }

    public LinkedHashSet<Long> getPostSet() {
        return postSet;
    }

    public void setPostSet(LinkedHashSet<Long> postSet) {
        this.postSet = postSet;
    }

    public String getTerm() {

        return term;
    }

    public void setTerm(String term) {

        this.term = term;
    }

    public Long getDocFreq() {

        return docFreq;
    }

    public void setDocFreq(Long docFreq) {

        this.docFreq = docFreq;
    }

}
