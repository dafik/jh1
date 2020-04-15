package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

/**
 * A OutgoingMeta.
 */
@Entity
@Table(name = "outgoing_meta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OutgoingMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "send_at")
    private ZonedDateTime sendAt;

    @Column(name = "expire_at")
    private Integer expireAt;

    @Column(name = "last_edit_at")
    private ZonedDateTime lastEditAt;

    @OneToOne(mappedBy = "outgoingMeta")
    @JsonIgnore
    private LetterGroup letterGroup;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSendAt() {
        return sendAt;
    }

    public OutgoingMeta sendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
        return this;
    }

    public void setSendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getExpireAt() {
        return expireAt;
    }

    public OutgoingMeta expireAt(Integer expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public void setExpireAt(Integer expireAt) {
        this.expireAt = expireAt;
    }

    public ZonedDateTime getLastEditAt() {
        return lastEditAt;
    }

    public OutgoingMeta lastEditAt(ZonedDateTime lastEditAt) {
        this.lastEditAt = lastEditAt;
        return this;
    }

    public void setLastEditAt(ZonedDateTime lastEditAt) {
        this.lastEditAt = lastEditAt;
    }

    public LetterGroup getLetterGroup() {
        return letterGroup;
    }

    public OutgoingMeta letterGroup(LetterGroup letterGroup) {
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
        if (!(o instanceof OutgoingMeta)) {
            return false;
        }
        return id != null && id.equals(((OutgoingMeta) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OutgoingMeta{" +
            "id=" + getId() +
            ", sendAt='" + getSendAt() + "'" +
            ", expireAt=" + getExpireAt() +
            ", lastEditAt='" + getLastEditAt() + "'" +
            "}";
    }
}
