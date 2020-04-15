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
 * Criteria class for the {@link pl.envelo.erds.ua.domain.IncomingMeta} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.IncomingMetaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /incoming-metas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IncomingMetaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter receivedAt;

    private ZonedDateTimeFilter readAt;

    private LongFilter letterGroupId;

    public IncomingMetaCriteria() {
    }

    public IncomingMetaCriteria(IncomingMetaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.receivedAt = other.receivedAt == null ? null : other.receivedAt.copy();
        this.readAt = other.readAt == null ? null : other.readAt.copy();
        this.letterGroupId = other.letterGroupId == null ? null : other.letterGroupId.copy();
    }

    @Override
    public IncomingMetaCriteria copy() {
        return new IncomingMetaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(ZonedDateTimeFilter receivedAt) {
        this.receivedAt = receivedAt;
    }

    public ZonedDateTimeFilter getReadAt() {
        return readAt;
    }

    public void setReadAt(ZonedDateTimeFilter readAt) {
        this.readAt = readAt;
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
        final IncomingMetaCriteria that = (IncomingMetaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(receivedAt, that.receivedAt) &&
            Objects.equals(readAt, that.readAt) &&
            Objects.equals(letterGroupId, that.letterGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        receivedAt,
        readAt,
        letterGroupId
        );
    }

    @Override
    public String toString() {
        return "IncomingMetaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (receivedAt != null ? "receivedAt=" + receivedAt + ", " : "") +
                (readAt != null ? "readAt=" + readAt + ", " : "") +
                (letterGroupId != null ? "letterGroupId=" + letterGroupId + ", " : "") +
            "}";
    }

}
