package model;

public class PostingsModel {
    private String term;
    private Long docID;
    private Long position;

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Long getDocID() {
        return docID;
    }

    public void setDocID(Long docID) {
        this.docID = docID;
    }
}
