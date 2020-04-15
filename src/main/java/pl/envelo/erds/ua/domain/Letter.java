package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Letter.
 */
@Entity
@Table(name = "letter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Letter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ean")
    private String ean;

    @Column(name = "erds_id")
    private String erdsId;

    @ManyToOne
    @JsonIgnoreProperties("letters")
    private Actor sender;

    @ManyToOne
    @JsonIgnoreProperties("letters")
    private Actor receipient;

    @ManyToOne
    @JsonIgnoreProperties("letters")
    private LetterGroup letterGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public Letter ean(String ean) {
        this.ean = ean;
        return this;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getErdsId() {
        return erdsId;
    }

    public Letter erdsId(String erdsId) {
        this.erdsId = erdsId;
        return this;
    }

    public void setErdsId(String erdsId) {
        this.erdsId = erdsId;
    }

    public Actor getSender() {
        return sender;
    }

    public Letter sender(Actor actor) {
        this.sender = actor;
        return this;
    }

    public void setSender(Actor actor) {
        this.sender = actor;
    }

    public Actor getReceipient() {
        return receipient;
    }

    public Letter receipient(Actor actor) {
        this.receipient = actor;
        return this;
    }

    public void setReceipient(Actor actor) {
        this.receipient = actor;
    }

    public LetterGroup getLetterGroup() {
        return letterGroup;
    }

    public Letter letterGroup(LetterGroup letterGroup) {
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
        if (!(o instanceof Letter)) {
            return false;
        }
        return id != null && id.equals(((Letter) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Letter{" +
            "id=" + getId() +
            ", ean='" + getEan() + "'" +
            ", erdsId='" + getErdsId() + "'" +
            "}";
    }
}
