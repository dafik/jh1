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
 * Criteria class for the {@link pl.envelo.erds.ua.domain.AddressBookItem} entity. This class is used
 * in {@link pl.envelo.erds.ua.web.rest.AddressBookItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /address-book-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AddressBookItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter uid;

    private StringFilter schema;

    private StringFilter label;

    private LongFilter userAccountId;

    public AddressBookItemCriteria() {
    }

    public AddressBookItemCriteria(AddressBookItemCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.schema = other.schema == null ? null : other.schema.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.userAccountId = other.userAccountId == null ? null : other.userAccountId.copy();
    }

    @Override
    public AddressBookItemCriteria copy() {
        return new AddressBookItemCriteria(this);
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

    public LongFilter getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(LongFilter userAccountId) {
        this.userAccountId = userAccountId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AddressBookItemCriteria that = (AddressBookItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(schema, that.schema) &&
            Objects.equals(label, that.label) &&
            Objects.equals(userAccountId, that.userAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        schema,
        label,
        userAccountId
        );
    }

    @Override
    public String toString() {
        return "AddressBookItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (schema != null ? "schema=" + schema + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (userAccountId != null ? "userAccountId=" + userAccountId + ", " : "") +
            "}";
    }

}
