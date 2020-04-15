package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

import pl.envelo.erds.ua.domain.enumeration.NotificationState;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ean")
    private String ean;

    @Column(name = "erds_id")
    private String erdsId;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "subject")
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private NotificationState state;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "sender_label")
    private String senderLabel;

    @Column(name = "send_at")
    private ZonedDateTime sendAt;

    @Column(name = "decision_at")
    private ZonedDateTime decisionAt;

    @Column(name = "expire_at")
    private ZonedDateTime expireAt;

    @ManyToOne
    @JsonIgnoreProperties("notifications")
    private Box box;

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

    public Notification ean(String ean) {
        this.ean = ean;
        return this;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getErdsId() {
        return erdsId;
    }

    public Notification erdsId(String erdsId) {
        this.erdsId = erdsId;
        return this;
    }

    public void setErdsId(String erdsId) {
        this.erdsId = erdsId;
    }

    public String getProcessId() {
        return processId;
    }

    public Notification processId(String processId) {
        this.processId = processId;
        return this;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSubject() {
        return subject;
    }

    public Notification subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NotificationState getState() {
        return state;
    }

    public Notification state(NotificationState state) {
        this.state = state;
        return this;
    }

    public void setState(NotificationState state) {
        this.state = state;
    }

    public String getSenderId() {
        return senderId;
    }

    public Notification senderId(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderLabel() {
        return senderLabel;
    }

    public Notification senderLabel(String senderLabel) {
        this.senderLabel = senderLabel;
        return this;
    }

    public void setSenderLabel(String senderLabel) {
        this.senderLabel = senderLabel;
    }

    public ZonedDateTime getSendAt() {
        return sendAt;
    }

    public Notification sendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
        return this;
    }

    public void setSendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public ZonedDateTime getDecisionAt() {
        return decisionAt;
    }

    public Notification decisionAt(ZonedDateTime decisionAt) {
        this.decisionAt = decisionAt;
        return this;
    }

    public void setDecisionAt(ZonedDateTime decisionAt) {
        this.decisionAt = decisionAt;
    }

    public ZonedDateTime getExpireAt() {
        return expireAt;
    }

    public Notification expireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Box getBox() {
        return box;
    }

    public Notification box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return id != null && id.equals(((Notification) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", ean='" + getEan() + "'" +
            ", erdsId='" + getErdsId() + "'" +
            ", processId='" + getProcessId() + "'" +
            ", subject='" + getSubject() + "'" +
            ", state='" + getState() + "'" +
            ", senderId='" + getSenderId() + "'" +
            ", senderLabel='" + getSenderLabel() + "'" +
            ", sendAt='" + getSendAt() + "'" +
            ", decisionAt='" + getDecisionAt() + "'" +
            ", expireAt='" + getExpireAt() + "'" +
            "}";
    }
}
