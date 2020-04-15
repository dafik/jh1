package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import pl.envelo.erds.ua.domain.enumeration.LetterState;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.LetterGroup} entity.
 */
public class LetterGroupDTO implements Serializable {
    
    private Long id;

    private String msgId;

    private LetterState state;

    private String subject;

    private ZonedDateTime createdAt;

    private String replayTo;

    private String inReplayTo;


    private Long incomingMetaId;

    private Long outgoingMetaId;

    private Long boxId;

    private Long threadId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public LetterState getState() {
        return state;
    }

    public void setState(LetterState state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReplayTo() {
        return replayTo;
    }

    public void setReplayTo(String replayTo) {
        this.replayTo = replayTo;
    }

    public String getInReplayTo() {
        return inReplayTo;
    }

    public void setInReplayTo(String inReplayTo) {
        this.inReplayTo = inReplayTo;
    }

    public Long getIncomingMetaId() {
        return incomingMetaId;
    }

    public void setIncomingMetaId(Long incomingMetaId) {
        this.incomingMetaId = incomingMetaId;
    }

    public Long getOutgoingMetaId() {
        return outgoingMetaId;
    }

    public void setOutgoingMetaId(Long outgoingMetaId) {
        this.outgoingMetaId = outgoingMetaId;
    }

    public Long getBoxId() {
        return boxId;
    }

    public void setBoxId(Long boxId) {
        this.boxId = boxId;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LetterGroupDTO letterGroupDTO = (LetterGroupDTO) o;
        if (letterGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), letterGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LetterGroupDTO{" +
            "id=" + getId() +
            ", msgId='" + getMsgId() + "'" +
            ", state='" + getState() + "'" +
            ", subject='" + getSubject() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", replayTo='" + getReplayTo() + "'" +
            ", inReplayTo='" + getInReplayTo() + "'" +
            ", incomingMetaId=" + getIncomingMetaId() +
            ", outgoingMetaId=" + getOutgoingMetaId() +
            ", boxId=" + getBoxId() +
            ", threadId=" + getThreadId() +
            "}";
    }
}
