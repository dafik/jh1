package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Actor} entity.
 */
public class ActorDTO implements Serializable {
    
    private Long id;

    private String uid;

    private String schema;

    private String label;

    
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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
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

        ActorDTO actorDTO = (ActorDTO) o;
        if (actorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActorDTO{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", schema='" + getSchema() + "'" +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
