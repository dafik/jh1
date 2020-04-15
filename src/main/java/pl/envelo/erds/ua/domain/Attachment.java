package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "size")
    private Integer size;

    @Column(name = "s_3_name")
    private String s3name;

    @Column(name = "content_type")
    private String contentType;

    @ManyToOne
    @JsonIgnoreProperties("attachments")
    private LetterGroup letterGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Attachment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public Attachment size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String gets3name() {
        return s3name;
    }

    public Attachment s3name(String s3name) {
        this.s3name = s3name;
        return this;
    }

    public void sets3name(String s3name) {
        this.s3name = s3name;
    }

    public String getContentType() {
        return contentType;
    }

    public Attachment contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LetterGroup getLetterGroup() {
        return letterGroup;
    }

    public Attachment letterGroup(LetterGroup letterGroup) {
        this.letterGroup = letterGroup;
        return this;
    }

    public void setLetterGroup(LetterGroup letterGroup) {
        this.letterGroup = letterGroup;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            ", s3name='" + gets3name() + "'" +
            ", contentType='" + getContentType() + "'" +
            "}";
    }
}
