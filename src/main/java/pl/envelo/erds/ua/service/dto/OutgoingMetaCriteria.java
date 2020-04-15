package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link pl.envelo.erds.ua.domain.OutgoingMeta} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.OutgoingMetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /outgoing-metas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OutgoingMetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter sendAt;

    private IntegerFilter expireAt;

    private ZonedDateTimeFilter lastEditAt;

    private LongFilter letterGroupId;

    public OutgoingMetaCriteria() {
    }

    public OutgoingMetaCriteria(OutgoingMetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sendAt = other.sendAt == null ? null : other.sendAt.copy();
        this.expireAt = other.expireAt == null ? null : other.expireAt.copy();
        this.lastEditAt = other.lastEditAt == null ? null : other.lastEditAt.copy();
        this.letterGroupId = other.letterGroupId == null ? null : other.letterGroupId.copy();
    }

    @Override
    public OutgoingMetaCriteria copy() {
        return new OutgoingMetaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getSendAt() {
        return sendAt;
    }

    public void setSendAt(ZonedDateTimeFilter sendAt) {
        this.sendAt = sendAt;
    }

    public IntegerFilter getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(IntegerFilter expireAt) {
        this.expireAt = expireAt;
    }

    public ZonedDateTimeFilter getLastEditAt() {
        return lastEditAt;
    }

    public void setLastEditAt(ZonedDateTimeFilter lastEditAt) {
        this.lastEditAt = lastEditAt;
    }

    public LongFilter getLetterGroupId() {
        return letterGroupId;
    }

    public void setLetterGroupId(LongFilter letterGroupId) {
        this.letterGroupId = letterGroupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OutgoingMetaCriteria that = (OutgoingMetaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(sendAt, that.sendAt) &&
            Objects.equals(expireAt, that.expireAt) &&
            Objects.equals(lastEditAt, that.lastEditAt) &&
            Objects.equals(letterGroupId, that.letterGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        sendAt,
        expireAt,
        lastEditAt,
        letterGroupId
        );
    }

    @Override
    public String toString() {
        return "OutgoingMetaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (sendAt != null ? "sendAt=" + sendAt + ", " : "") +
                (expireAt != null ? "expireAt=" + expireAt + ", " : "") +
                (lastEditAt != null ? "lastEditAt=" + lastEditAt + ", " : "") +
                (letterGroupId != null ? "letterGroupId=" + letterGroupId + ", " : "") +
            "}";
    }

}
