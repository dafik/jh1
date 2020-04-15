package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import pl.envelo.erds.ua.domain.enumeration.NotificationState;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Notification} entity.
 */
public class NotificationDTO implements Serializable {
    
    private Long id;

    private String ean;

    private String erdsId;

    private String processId;

    private String subject;

    private NotificationState state;

    private String senderId;

    private String senderLabel;

    private ZonedDateTime sendAt;

    private ZonedDateTime decisionAt;

    private ZonedDateTime expireAt;


    private Long boxId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getErdsId() {
        return erdsId;
    }

    public void setErdsId(String erdsId) {
        this.erdsId = erdsId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public NotificationState getState() {
        return state;
    }

    public void setState(NotificationState state) {
        this.state = state;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderLabel() {
        return senderLabel;
    }

    public void setSenderLabel(String senderLabel) {
        this.senderLabel = senderLabel;
    }

    public ZonedDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public ZonedDateTime getDecisionAt() {
        return decisionAt;
    }

    public void setDecisionAt(ZonedDateTime decisionAt) {
        this.decisionAt = decisionAt;
    }

    public ZonedDateTime getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public Long getBoxId() {
        return boxId;
    }

    public void setBoxId(Long boxId) {
        this.boxId = boxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (notificationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notificationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
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
            ", boxId=" + getBoxId() +
            "}";
    }
}
