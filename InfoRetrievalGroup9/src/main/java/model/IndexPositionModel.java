package model;

import java.util.LinkedHashSet;

public class IndexPositionModel {

    private Long docId;
    private LinkedHashSet<Long> positions;

    public Long getDocId() {
        return docId;
    }

    public void setDocId(Long docId) {
        this.docId = docId;
    }

    public LinkedHashSet<Long> getPositions() {
        return positions;
    }

    public void setPositions(LinkedHashSet<Long> positions) {
        this.positions = positions;
    }
}
