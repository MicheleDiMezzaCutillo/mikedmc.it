package it.mikedmc.model.dto;

public class ContentDto {
    private Long id;
    private String type;
    private String data;
    private String option1;
    private String option2;
    private String option3;
    private Long position;
    private Long documentationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public Long getDocumentationId() {
        return documentationId;
    }

    public void setDocumentationId(Long documentationId) {
        this.documentationId = documentationId;
    }
}
