package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import pl.envelo.erds.ua.domain.enumeration.EvidenceType;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Evidence} entity.
 */
public class EvidenceDTO implements Serializable {
    
    private Long id;

    private String ean;

    private String erdsId;

    private String processId;

    private ZonedDateTime date;

    private EvidenceType type;

    private String location;


    private Long boxId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public String getErdsId() {
        return erdsId;
    }

    public void setErdsId(String erdsId) {
        this.erdsId = erdsId;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public EvidenceType getType() {
        return type;
    }

    public void setType(EvidenceType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getBoxId() {
        return boxId;
    }

    public void setBoxId(Long boxId) {
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

        EvidenceDTO evidenceDTO = (EvidenceDTO) o;
        if (evidenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), evidenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EvidenceDTO{" +
            "id=" + getId() +
            ", ean='" + getEan() + "'" +
            ", erdsId='" + getErdsId() + "'" +
            ", processId='" + getProcessId() + "'" +
            ", date='" + getDate() + "'" +
            ", type='" + getType() + "'" +
            ", location='" + getLocation() + "'" +
            ", boxId=" + getBoxId() +
            "}";
    }
}
