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

/**
 * Criteria class for the {@link pl.envelo.erds.ua.domain.Letter} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.LetterResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /letters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LetterCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ean;

    private StringFilter erdsId;

    private LongFilter senderId;

    private LongFilter receipientId;

    private LongFilter letterGroupId;

    public LetterCriteria() {
    }

    public LetterCriteria(LetterCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ean = other.ean == null ? null : other.ean.copy();
        this.erdsId = other.erdsId == null ? null : other.erdsId.copy();
        this.senderId = other.senderId == null ? null : other.senderId.copy();
        this.receipientId = other.receipientId == null ? null : other.receipientId.copy();
        this.letterGroupId = other.letterGroupId == null ? null : other.letterGroupId.copy();
    }

    @Override
    public LetterCriteria copy() {
        return new LetterCriteria(this);
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

    public LongFilter getSenderId() {
        return senderId;
    }

    public void setSenderId(LongFilter senderId) {
        this.senderId = senderId;
    }

    public LongFilter getReceipientId() {
        return receipientId;
    }

    public void setReceipientId(LongFilter receipientId) {
        this.receipientId = receipientId;
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
        final LetterCriteria that = (LetterCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ean, that.ean) &&
            Objects.equals(erdsId, that.erdsId) &&
            Objects.equals(senderId, that.senderId) &&
            Objects.equals(receipientId, that.receipientId) &&
            Objects.equals(letterGroupId, that.letterGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ean,
        erdsId,
        senderId,
        receipientId,
        letterGroupId
        );
    }

    @Override
    public String toString() {
        return "LetterCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ean != null ? "ean=" + ean + ", " : "") +
                (erdsId != null ? "erdsId=" + erdsId + ", " : "") +
                (senderId != null ? "senderId=" + senderId + ", " : "") +
                (receipientId != null ? "receipientId=" + receipientId + ", " : "") +
                (letterGroupId != null ? "letterGroupId=" + letterGroupId + ", " : "") +
            "}";
    }

}
