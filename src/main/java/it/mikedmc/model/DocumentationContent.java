package it.mikedmc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.mikedmc.enums.DocumentationContentType;
import jakarta.persistence.*;

@Entity
public class DocumentationContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentationContentType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documentation_id", nullable = false)
    @JsonIgnore
    private Documentation documentation;

    private String option1;
    private String option2;
    private String option3;

    private Long position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentationContentType getType() {
        return type;
    }

    public void setType(DocumentationContentType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Documentation getDocumentation() {
        return documentation;
    }

    public void setDocumentation(Documentation documentation) {
        this.documentation = documentation;
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
}
