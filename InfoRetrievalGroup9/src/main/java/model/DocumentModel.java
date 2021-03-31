package model;

public class DocumentModel {

    private String playId;
    private String sceneId;
    private Long sceneNum;
    private String text;

    public String getPlayId() {
        return playId;
    }

    public void setPlayId(String playId) {
        this.playId = playId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public Long getSceneNum() {
        return sceneNum;
    }

    public void setSceneNum(Long sceneNum) {
        this.sceneNum = sceneNum;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
