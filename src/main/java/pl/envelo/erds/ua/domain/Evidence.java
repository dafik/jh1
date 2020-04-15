package pl.envelo.erds.ua.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.ZonedDateTime;

import pl.envelo.erds.ua.domain.enumeration.EvidenceType;

/**
 * A Evidence.
 */
@Entity
@Table(name = "evidence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Evidence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "ean")
    private String ean;

    @Column(name = "erds_id")
    private String erdsId;

    @Column(name = "process_id")
    private String processId;

    @Column(name = "date")
    private ZonedDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private EvidenceType type;

    @Column(name = "location")
    private String location;

    @ManyToOne
    @JsonIgnoreProperties("evidences")
    private Box box;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public Evidence ean(String ean) {
        this.ean = ean;
        return this;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getErdsId() {
        return erdsId;
    }

    public Evidence erdsId(String erdsId) {
        this.erdsId = erdsId;
        return this;
    }

    public void setErdsId(String erdsId) {
        this.erdsId = erdsId;
    }

    public String getProcessId() {
        return processId;
    }

    public Evidence processId(String processId) {
        this.processId = processId;
        return this;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Evidence date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public EvidenceType getType() {
        return type;
    }

    public Evidence type(EvidenceType type) {
        this.type = type;
        return this;
    }

    public void setType(EvidenceType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public Evidence location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Box getBox() {
        return box;
    }

    public Evidence box(Box box) {
        this.box = box;
        return this;
    }

    public void setBox(Box box) {
        this.box = box;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evidence)) {
            return false;
        }
        return id != null && id.equals(((Evidence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Evidence{" +
            "id=" + getId() +
            ", ean='" + getEan() + "'" +
            ", erdsId='" + getErdsId() + "'" +
            ", processId='" + getProcessId() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
