package pl.envelo.erds.ua.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ncloak_id")
    private String ncloakId;

    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "userAccount")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AddressBookItem> addressBooks = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "user_account_box",
               joinColumns = @JoinColumn(name = "user_account_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "box_id", referencedColumnName = "id"))
    private Set<Box> boxes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNcloakId() {
        return ncloakId;
    }

    public UserAccount ncloakId(String ncloakId) {
        this.ncloakId = ncloakId;
        return this;
    }

    public void setNcloakId(String ncloakId) {
        this.ncloakId = ncloakId;
    }

    public String getEmail() {
        return email;
    }

    public UserAccount email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserAccount firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserAccount lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public UserAccount isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<AddressBookItem> getAddressBooks() {
        return addressBooks;
    }

    public UserAccount addressBooks(Set<AddressBookItem> addressBookItems) {
        this.addressBooks = addressBookItems;
        return this;
    }

    public UserAccount addAddressBook(AddressBookItem addressBookItem) {
        this.addressBooks.add(addressBookItem);
        addressBookItem.setUserAccount(this);
        return this;
    }

    public UserAccount removeAddressBook(AddressBookItem addressBookItem) {
        this.addressBooks.remove(addressBookItem);
        addressBookItem.setUserAccount(null);
        return this;
    }

    public void setAddressBooks(Set<AddressBookItem> addressBookItems) {
        this.addressBooks = addressBookItems;
    }

    public Set<Box> getBoxes() {
        return boxes;
    }

    public UserAccount boxes(Set<Box> boxes) {
        this.boxes = boxes;
        return this;
    }

    public UserAccount addBox(Box box) {
        this.boxes.add(box);
        box.getUserAccounts().add(this);
        return this;
    }

    public UserAccount removeBox(Box box) {
        this.boxes.remove(box);
        box.getUserAccounts().remove(this);
        return this;
    }

    public void setBoxes(Set<Box> boxes) {
        this.boxes = boxes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", ncloakId='" + getNcloakId() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
