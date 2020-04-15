package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import pl.envelo.erds.ua.domain.enumeration.BoxType;

/**
 * A Box.
 */
@Entity
@Table(name = "box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Box implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "namespace")
    private String namespace;

    @Column(name = "label")
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(name = "box_type")
    private BoxType boxType;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "rules_acceptance_date")
    private ZonedDateTime rulesAcceptanceDate;

    @OneToMany(mappedBy = "box")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Evidence> evidences = new HashSet<>();

    @OneToMany(mappedBy = "box")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "box")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<LetterGroup> letterGroups = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("boxes")
    private UserAccount rulesAcceptanceUser;

    @ManyToMany(mappedBy = "boxes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<UserAccount> userAccounts = new HashSet<>();

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

    public Box uid(String uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamespace() {
        return namespace;
    }

    public Box namespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLabel() {
        return label;
    }

    public Box label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public Box boxType(BoxType boxType) {
        this.boxType = boxType;
        return this;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Box isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getRulesAcceptanceDate() {
        return rulesAcceptanceDate;
    }

    public Box rulesAcceptanceDate(ZonedDateTime rulesAcceptanceDate) {
        this.rulesAcceptanceDate = rulesAcceptanceDate;
        return this;
    }

    public void setRulesAcceptanceDate(ZonedDateTime rulesAcceptanceDate) {
        this.rulesAcceptanceDate = rulesAcceptanceDate;
    }

    public Set<Evidence> getEvidences() {
        return evidences;
    }

    public Box evidences(Set<Evidence> evidences) {
        this.evidences = evidences;
        return this;
    }

    public Box addEvidence(Evidence evidence) {
        this.evidences.add(evidence);
        evidence.setBox(this);
        return this;
    }

    public Box removeEvidence(Evidence evidence) {
        this.evidences.remove(evidence);
        evidence.setBox(null);
        return this;
    }

    public void setEvidences(Set<Evidence> evidences) {
        this.evidences = evidences;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Box notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Box addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setBox(this);
        return this;
    }

    public Box removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setBox(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<LetterGroup> getLetterGroups() {
        return letterGroups;
    }

    public Box letterGroups(Set<LetterGroup> letterGroups) {
        this.letterGroups = letterGroups;
        return this;
    }

    public Box addLetterGroup(LetterGroup letterGroup) {
        this.letterGroups.add(letterGroup);
        letterGroup.setBox(this);
        return this;
    }

    public Box removeLetterGroup(LetterGroup letterGroup) {
        this.letterGroups.remove(letterGroup);
        letterGroup.setBox(null);
        return this;
    }

    public void setLetterGroups(Set<LetterGroup> letterGroups) {
        this.letterGroups = letterGroups;
    }

    public UserAccount getRulesAcceptanceUser() {
        return rulesAcceptanceUser;
    }

    public Box rulesAcceptanceUser(UserAccount userAccount) {
        this.rulesAcceptanceUser = userAccount;
        return this;
    }

    public void setRulesAcceptanceUser(UserAccount userAccount) {
        this.rulesAcceptanceUser = userAccount;
    }

    public Set<UserAccount> getUserAccounts() {
        return userAccounts;
    }

    public Box userAccounts(Set<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
        return this;
    }

    public Box addUserAccount(UserAccount userAccount) {
        this.userAccounts.add(userAccount);
        userAccount.getBoxes().add(this);
        return this;
    }

    public Box removeUserAccount(UserAccount userAccount) {
        this.userAccounts.remove(userAccount);
        userAccount.getBoxes().remove(this);
        return this;
    }

    public void setUserAccounts(Set<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Box)) {
            return false;
        }
        return id != null && id.equals(((Box) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Box{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", label='" + getLabel() + "'" +
            ", boxType='" + getBoxType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", rulesAcceptanceDate='" + getRulesAcceptanceDate() + "'" +
            "}";
    }
}
