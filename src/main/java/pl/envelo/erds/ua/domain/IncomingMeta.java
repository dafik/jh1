package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A IncomingMeta.
 */
@Entity
@Table(name = "incoming_meta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IncomingMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "received_at")
    private ZonedDateTime receivedAt;

    @Column(name = "read_at")
    private ZonedDateTime readAt;

    @OneToOne(mappedBy = "incomingMeta")
    @JsonIgnore
    private LetterGroup letterGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getReceivedAt() {
        return receivedAt;
    }

    public IncomingMeta receivedAt(ZonedDateTime receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public void setReceivedAt(ZonedDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public ZonedDateTime getReadAt() {
        return readAt;
    }

    public IncomingMeta readAt(ZonedDateTime readAt) {
        this.readAt = readAt;
        return this;
    }

    public void setReadAt(ZonedDateTime readAt) {
        this.readAt = readAt;
    }

    public LetterGroup getLetterGroup() {
        return letterGroup;
    }

    public IncomingMeta letterGroup(LetterGroup letterGroup) {
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
        if (!(o instanceof IncomingMeta)) {
            return false;
        }
        return id != null && id.equals(((IncomingMeta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "IncomingMeta{" +
            "id=" + getId() +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            "}";
    }
}
