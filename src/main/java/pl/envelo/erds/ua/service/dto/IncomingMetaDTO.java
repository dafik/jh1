package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.IncomingMeta} entity.
 */
public class IncomingMetaDTO implements Serializable {
    
    private Long id;

    private ZonedDateTime receivedAt;

    private ZonedDateTime readAt;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(ZonedDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public ZonedDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(ZonedDateTime readAt) {
        this.readAt = readAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IncomingMetaDTO incomingMetaDTO = (IncomingMetaDTO) o;
        if (incomingMetaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), incomingMetaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IncomingMetaDTO{" +
            "id=" + getId() +
            ", receivedAt='" + getReceivedAt() + "'" +
            ", readAt='" + getReadAt() + "'" +
            "}";
    }
}
