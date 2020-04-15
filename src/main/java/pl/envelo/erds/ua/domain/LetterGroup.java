package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import pl.envelo.erds.ua.domain.enumeration.LetterState;

/**
 * A LetterGroup.
 */
@Entity
@Table(name = "letter_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class LetterGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "msg_id")
    private String msgId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private LetterState state;

    @Column(name = "subject")
    private String subject;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "replay_to")
    private String replayTo;

    @Column(name = "in_replay_to")
    private String inReplayTo;

    @OneToOne
    @JoinColumn(unique = true)
    private IncomingMeta incomingMeta;

    @OneToOne
    @JoinColumn(unique = true)
    private OutgoingMeta outgoingMeta;

    @OneToMany(mappedBy = "letterGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Attachment> attachments = new HashSet<>();

    @OneToMany(mappedBy = "letterGroup")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Letter> letters = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("letterGroups")
    private Box box;

    @ManyToOne
    @JsonIgnoreProperties("letterGroups")
    private Thread thread;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public LetterGroup msgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public LetterState getState() {
        return state;
    }

    public LetterGroup state(LetterState state) {
        this.state = state;
        return this;
    }

    public void setState(LetterState state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public LetterGroup subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public LetterGroup createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReplayTo() {
        return replayTo;
    }

    public LetterGroup replayTo(String replayTo) {
        this.replayTo = replayTo;
        return this;
    }

    public void setReplayTo(String replayTo) {
        this.replayTo = replayTo;
    }

    public String getInReplayTo() {
        return inReplayTo;
    }

    public LetterGroup inReplayTo(String inReplayTo) {
        this.inReplayTo = inReplayTo;
        return this;
    }

    public void setInReplayTo(String inReplayTo) {
        this.inReplayTo = inReplayTo;
    }

    public IncomingMeta getIncomingMeta() {
        return incomingMeta;
    }

    public LetterGroup incomingMeta(IncomingMeta incomingMeta) {
        this.incomingMeta = incomingMeta;
        return this;
    }

    public void setIncomingMeta(IncomingMeta incomingMeta) {
        this.incomingMeta = incomingMeta;
    }

    public OutgoingMeta getOutgoingMeta() {
        return outgoingMeta;
    }

    public LetterGroup outgoingMeta(OutgoingMeta outgoingMeta) {
        this.outgoingMeta = outgoingMeta;
        return this;
    }

    public void setOutgoingMeta(OutgoingMeta outgoingMeta) {
        this.outgoingMeta = outgoingMeta;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public LetterGroup attachments(Set<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public LetterGroup addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setLetterGroup(this);
        return this;
    }

    public LetterGroup removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setLetterGroup(null);
        return this;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Set<Letter> getLetters() {
        return letters;
    }

    public LetterGroup letters(Set<Letter> letters) {
        this.letters = letters;
        return this;
    }

    public LetterGroup addLetter(Letter letter) {
        this.letters.add(letter);
        letter.setLetterGroup(this);
        return this;
    }

    public LetterGroup removeLetter(Letter letter) {
        this.letters.remove(letter);
        letter.setLetterGroup(null);
        return this;
    }

    public void setLetters(Set<Letter> letters) {
        this.letters = letters;
    }

    public Box getBox() {
        return box;
    }

    public LetterGroup box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Thread getThread() {
        return thread;
    }

    public LetterGroup thread(Thread thread) {
        this.thread = thread;
        return this;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LetterGroup)) {
            return false;
        }
        return id != null && id.equals(((LetterGroup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "LetterGroup{" +
            "id=" + getId() +
            ", msgId='" + getMsgId() + "'" +
            ", state='" + getState() + "'" +
            ", subject='" + getSubject() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", replayTo='" + getReplayTo() + "'" +
            ", inReplayTo='" + getInReplayTo() + "'" +
            "}";
    }
}
