package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;
import pl.envelo.erds.ua.domain.enumeration.BoxType;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Box} entity.
 */
public class BoxDTO implements Serializable {
    
    private Long id;

    private String uid;

    private String namespace;

    private String label;

    private BoxType boxType;

    private Boolean isActive;

    private ZonedDateTime rulesAcceptanceDate;


    private Long rulesAcceptanceUserId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public ZonedDateTime getRulesAcceptanceDate() {
        return rulesAcceptanceDate;
    }

    public void setRulesAcceptanceDate(ZonedDateTime rulesAcceptanceDate) {
        this.rulesAcceptanceDate = rulesAcceptanceDate;
    }

    public Long getRulesAcceptanceUserId() {
        return rulesAcceptanceUserId;
    }

    public void setRulesAcceptanceUserId(Long userAccountId) {
        this.rulesAcceptanceUserId = userAccountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BoxDTO boxDTO = (BoxDTO) o;
        if (boxDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), boxDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BoxDTO{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", namespace='" + getNamespace() + "'" +
            ", label='" + getLabel() + "'" +
            ", boxType='" + getBoxType() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", rulesAcceptanceDate='" + getRulesAcceptanceDate() + "'" +
            ", rulesAcceptanceUserId=" + getRulesAcceptanceUserId() +
            "}";
    }
}
