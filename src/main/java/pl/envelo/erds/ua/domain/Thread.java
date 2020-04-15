package pl.envelo.erds.ua.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Thread.
 */
@Entity
@Table(name = "thread")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Thread implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "last_letter_time")
    private ZonedDateTime lastLetterTime;

    @OneToMany(mappedBy = "thread")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LetterGroup> letterGroups = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Thread subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ZonedDateTime getLastLetterTime() {
        return lastLetterTime;
    }

    public Thread lastLetterTime(ZonedDateTime lastLetterTime) {
        this.lastLetterTime = lastLetterTime;
        return this;
    }

    public void setLastLetterTime(ZonedDateTime lastLetterTime) {
        this.lastLetterTime = lastLetterTime;
    }

    public Set<LetterGroup> getLetterGroups() {
        return letterGroups;
    }

    public Thread letterGroups(Set<LetterGroup> letterGroups) {
        this.letterGroups = letterGroups;
        return this;
    }

    public Thread addLetterGroup(LetterGroup letterGroup) {
        this.letterGroups.add(letterGroup);
        letterGroup.setThread(this);
        return this;
    }

    public Thread removeLetterGroup(LetterGroup letterGroup) {
        this.letterGroups.remove(letterGroup);
        letterGroup.setThread(null);
        return this;
    }

    public void setLetterGroups(Set<LetterGroup> letterGroups) {
        this.letterGroups = letterGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Thread)) {
            return false;
        }
        return id != null && id.equals(((Thread) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Thread{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", lastLetterTime='" + getLastLetterTime() + "'" +
            "}";
    }
}
