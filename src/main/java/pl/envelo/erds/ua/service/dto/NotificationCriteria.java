package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pl.envelo.erds.ua.domain.enumeration.NotificationState;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link pl.envelo.erds.ua.domain.Notification} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NotificationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering NotificationState
     */
    public static class NotificationStateFilter extends Filter<NotificationState> {

        public NotificationStateFilter() {
        }

        public NotificationStateFilter(NotificationStateFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStateFilter copy() {
            return new NotificationStateFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ean;

    private StringFilter erdsId;

    private StringFilter processId;

    private StringFilter subject;

    private NotificationStateFilter state;

    private StringFilter senderId;

    private StringFilter senderLabel;

    private ZonedDateTimeFilter sendAt;

    private ZonedDateTimeFilter decisionAt;

    private ZonedDateTimeFilter expireAt;

    private LongFilter boxId;

    public NotificationCriteria() {
    }

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ean = other.ean == null ? null : other.ean.copy();
        this.erdsId = other.erdsId == null ? null : other.erdsId.copy();
        this.processId = other.processId == null ? null : other.processId.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.state = other.state == null ? null : other.state.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.senderLabel = other.senderLabel == null ? null : other.senderLabel.copy();
        this.sendAt = other.sendAt == null ? null : other.sendAt.copy();
        this.decisionAt = other.decisionAt == null ? null : other.decisionAt.copy();
        this.expireAt = other.expireAt == null ? null : other.expireAt.copy();
        this.boxId = other.boxId == null ? null : other.boxId.copy();
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEan() {
        return ean;
    }

    public void setEan(StringFilter ean) {
        this.ean = ean;
    }

    public StringFilter getErdsId() {
        return erdsId;
    }

    public void setErdsId(StringFilter erdsId) {
        this.erdsId = erdsId;
    }

    public StringFilter getProcessId() {
        return processId;
    }

    public void setProcessId(StringFilter processId) {
        this.processId = processId;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public NotificationStateFilter getState() {
        return state;
    }

    public void setState(NotificationStateFilter state) {
        this.state = state;
    }

    public StringFilter getSenderId() {
        return senderId;
    }

    public void setSenderId(StringFilter senderId) {
        this.senderId = senderId;
    }

    public StringFilter getSenderLabel() {
        return senderLabel;
    }

    public void setSenderLabel(StringFilter senderLabel) {
        this.senderLabel = senderLabel;
    }

    public ZonedDateTimeFilter getSendAt() {
        return sendAt;
    }

    public void setSendAt(ZonedDateTimeFilter sendAt) {
        this.sendAt = sendAt;
    }

    public ZonedDateTimeFilter getDecisionAt() {
        return decisionAt;
    }

    public void setDecisionAt(ZonedDateTimeFilter decisionAt) {
        this.decisionAt = decisionAt;
    }

    public ZonedDateTimeFilter getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(ZonedDateTimeFilter expireAt) {
        this.expireAt = expireAt;
    }

    public LongFilter getBoxId() {
        return boxId;
    }

    public void setBoxId(LongFilter boxId) {
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ean, that.ean) &&
            Objects.equals(erdsId, that.erdsId) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(state, that.state) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(senderLabel, that.senderLabel) &&
            Objects.equals(sendAt, that.sendAt) &&
            Objects.equals(decisionAt, that.decisionAt) &&
            Objects.equals(expireAt, that.expireAt) &&
            Objects.equals(boxId, that.boxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ean,
        erdsId,
        processId,
        subject,
        state,
        senderId,
        senderLabel,
        sendAt,
        decisionAt,
        expireAt,
        boxId
        );
    }

    @Override
    public String toString() {
        return "NotificationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ean != null ? "ean=" + ean + ", " : "") +
                (erdsId != null ? "erdsId=" + erdsId + ", " : "") +
                (processId != null ? "processId=" + processId + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (state != null ? "state=" + state + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (senderLabel != null ? "senderLabel=" + senderLabel + ", " : "") +
                (sendAt != null ? "sendAt=" + sendAt + ", " : "") +
                (decisionAt != null ? "decisionAt=" + decisionAt + ", " : "") +
                (expireAt != null ? "expireAt=" + expireAt + ", " : "") +
                (boxId != null ? "boxId=" + boxId + ", " : "") +
            "}";
    }

}
