package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pl.envelo.erds.ua.domain.enumeration.LetterState;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link pl.envelo.erds.ua.domain.LetterGroup} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.LetterGroupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /letter-groups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LetterGroupCriteria implements Serializable, Criteria {
    /**
     * Class for filtering LetterState
     */
    public static class LetterStateFilter extends Filter<LetterState> {

        public LetterStateFilter() {
        }

        public LetterStateFilter(LetterStateFilter filter) {
            super(filter);
        }

        @Override
        public LetterStateFilter copy() {
            return new LetterStateFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter msgId;

    private LetterStateFilter state;

    private StringFilter subject;

    private ZonedDateTimeFilter createdAt;

    private StringFilter replayTo;

    private StringFilter inReplayTo;

    private LongFilter incomingMetaId;

    private LongFilter outgoingMetaId;

    private LongFilter attachmentId;

    private LongFilter letterId;

    private LongFilter boxId;

    private LongFilter threadId;

    public LetterGroupCriteria() {
    }

    public LetterGroupCriteria(LetterGroupCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.msgId = other.msgId == null ? null : other.msgId.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.replayTo = other.replayTo == null ? null : other.replayTo.copy();
        this.inReplayTo = other.inReplayTo == null ? null : other.inReplayTo.copy();
        this.incomingMetaId = other.incomingMetaId == null ? null : other.incomingMetaId.copy();
        this.outgoingMetaId = other.outgoingMetaId == null ? null : other.outgoingMetaId.copy();
        this.attachmentId = other.attachmentId == null ? null : other.attachmentId.copy();
        this.letterId = other.letterId == null ? null : other.letterId.copy();
        this.boxId = other.boxId == null ? null : other.boxId.copy();
        this.threadId = other.threadId == null ? null : other.threadId.copy();
    }

    @Override
    public LetterGroupCriteria copy() {
        return new LetterGroupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMsgId() {
        return msgId;
    }

    public void setMsgId(StringFilter msgId) {
        this.msgId = msgId;
    }

    public LetterStateFilter getState() {
        return state;
    }

    public void setState(LetterStateFilter state) {
        this.state = state;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getReplayTo() {
        return replayTo;
    }

    public void setReplayTo(StringFilter replayTo) {
        this.replayTo = replayTo;
    }

    public StringFilter getInReplayTo() {
        return inReplayTo;
    }

    public void setInReplayTo(StringFilter inReplayTo) {
        this.inReplayTo = inReplayTo;
    }

    public LongFilter getIncomingMetaId() {
        return incomingMetaId;
    }

    public void setIncomingMetaId(LongFilter incomingMetaId) {
        this.incomingMetaId = incomingMetaId;
    }

    public LongFilter getOutgoingMetaId() {
        return outgoingMetaId;
    }

    public void setOutgoingMetaId(LongFilter outgoingMetaId) {
        this.outgoingMetaId = outgoingMetaId;
    }

    public LongFilter getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(LongFilter attachmentId) {
        this.attachmentId = attachmentId;
    }

    public LongFilter getLetterId() {
        return letterId;
    }

    public void setLetterId(LongFilter letterId) {
        this.letterId = letterId;
    }

    public LongFilter getBoxId() {
        return boxId;
    }

    public void setBoxId(LongFilter boxId) {
        this.boxId = boxId;
    }

    public LongFilter getThreadId() {
        return threadId;
    }

    public void setThreadId(LongFilter threadId) {
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
        final LetterGroupCriteria that = (LetterGroupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(msgId, that.msgId) &&
            Objects.equals(state, that.state) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(replayTo, that.replayTo) &&
            Objects.equals(inReplayTo, that.inReplayTo) &&
            Objects.equals(incomingMetaId, that.incomingMetaId) &&
            Objects.equals(outgoingMetaId, that.outgoingMetaId) &&
            Objects.equals(attachmentId, that.attachmentId) &&
            Objects.equals(letterId, that.letterId) &&
            Objects.equals(boxId, that.boxId) &&
            Objects.equals(threadId, that.threadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        msgId,
        state,
        subject,
        createdAt,
        replayTo,
        inReplayTo,
        incomingMetaId,
        outgoingMetaId,
        attachmentId,
        letterId,
        boxId,
        threadId
        );
    }

    @Override
    public String toString() {
        return "LetterGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (msgId != null ? "msgId=" + msgId + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (replayTo != null ? "replayTo=" + replayTo + ", " : "") +
                (inReplayTo != null ? "inReplayTo=" + inReplayTo + ", " : "") +
                (incomingMetaId != null ? "incomingMetaId=" + incomingMetaId + ", " : "") +
                (outgoingMetaId != null ? "outgoingMetaId=" + outgoingMetaId + ", " : "") +
                (attachmentId != null ? "attachmentId=" + attachmentId + ", " : "") +
                (letterId != null ? "letterId=" + letterId + ", " : "") +
                (boxId != null ? "boxId=" + boxId + ", " : "") +
                (threadId != null ? "threadId=" + threadId + ", " : "") +
            "}";
    }

}
