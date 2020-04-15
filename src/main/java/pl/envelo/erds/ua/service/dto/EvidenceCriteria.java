package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pl.envelo.erds.ua.domain.enumeration.EvidenceType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link pl.envelo.erds.ua.domain.Evidence} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.EvidenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /evidences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EvidenceCriteria implements Serializable, Criteria {
    /**
     * Class for filtering EvidenceType
     */
    public static class EvidenceTypeFilter extends Filter<EvidenceType> {

        public EvidenceTypeFilter() {
        }

        public EvidenceTypeFilter(EvidenceTypeFilter filter) {
            super(filter);
        }

        @Override
        public EvidenceTypeFilter copy() {
            return new EvidenceTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter ean;

    private StringFilter erdsId;

    private StringFilter processId;

    private ZonedDateTimeFilter date;

    private EvidenceTypeFilter type;

    private StringFilter location;

    private LongFilter boxId;

    public EvidenceCriteria() {
    }

    public EvidenceCriteria(EvidenceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.ean = other.ean == null ? null : other.ean.copy();
        this.erdsId = other.erdsId == null ? null : other.erdsId.copy();
        this.processId = other.processId == null ? null : other.processId.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.boxId = other.boxId == null ? null : other.boxId.copy();
    }

    @Override
    public EvidenceCriteria copy() {
        return new EvidenceCriteria(this);
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

    public ZonedDateTimeFilter getDate() {
        return date;
    }

    public void setDate(ZonedDateTimeFilter date) {
        this.date = date;
    }

    public EvidenceTypeFilter getType() {
        return type;
    }

    public void setType(EvidenceTypeFilter type) {
        this.type = type;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
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
        final EvidenceCriteria that = (EvidenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(ean, that.ean) &&
            Objects.equals(erdsId, that.erdsId) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(date, that.date) &&
            Objects.equals(type, that.type) &&
            Objects.equals(location, that.location) &&
            Objects.equals(boxId, that.boxId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        ean,
        erdsId,
        processId,
        date,
        type,
        location,
        boxId
        );
    }

    @Override
    public String toString() {
        return "EvidenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (ean != null ? "ean=" + ean + ", " : "") +
                (erdsId != null ? "erdsId=" + erdsId + ", " : "") +
                (processId != null ? "processId=" + processId + ", " : "") +
                (date != null ? "date=" + date + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (boxId != null ? "boxId=" + boxId + ", " : "") +
            "}";
    }

}
