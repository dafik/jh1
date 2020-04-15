package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AddressBookItem.
 */
@Entity
@Table(name = "address_book_item")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AddressBookItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "jhi_schema")
    private String schema;

    @Column(name = "label")
    private String label;

    @ManyToOne
    @JsonIgnoreProperties("addressBooks")
    private UserAccount userAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public AddressBookItem uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSchema() {
        return schema;
    }

    public AddressBookItem schema(String schema) {
        this.schema = schema;
        return this;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getLabel() {
        return label;
    }

    public AddressBookItem label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public AddressBookItem userAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
        return this;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressBookItem)) {
            return false;
        }
        return id != null && id.equals(((AddressBookItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AddressBookItem{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", schema='" + getSchema() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
