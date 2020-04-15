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
 * Criteria class for the {@link pl.envelo.erds.ua.domain.Actor} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.ActorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ActorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uid;

    private StringFilter schema;

    private StringFilter label;

    public ActorCriteria() {
    }

    public ActorCriteria(ActorCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.schema = other.schema == null ? null : other.schema.copy();
        this.label = other.label == null ? null : other.label.copy();
    }

    @Override
    public ActorCriteria copy() {
        return new ActorCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUid() {
        return uid;
    }

    public void setUid(StringFilter uid) {
        this.uid = uid;
    }

    public StringFilter getSchema() {
        return schema;
    }

    public void setSchema(StringFilter schema) {
        this.schema = schema;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ActorCriteria that = (ActorCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(schema, that.schema) &&
            Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        schema,
        label
        );
    }

    @Override
    public String toString() {
        return "ActorCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (schema != null ? "schema=" + schema + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
            "}";
    }

}
