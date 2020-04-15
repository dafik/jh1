package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.OutgoingMeta} entity.
 */
public class OutgoingMetaDTO implements Serializable {
    
    private Long id;

    private ZonedDateTime sendAt;

    private Integer expireAt;

    private ZonedDateTime lastEditAt;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getSendAt() {
        return sendAt;
    }

    public void setSendAt(ZonedDateTime sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Integer expireAt) {
        this.expireAt = expireAt;
    }

    public ZonedDateTime getLastEditAt() {
        return lastEditAt;
    }

    public void setLastEditAt(ZonedDateTime lastEditAt) {
        this.lastEditAt = lastEditAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OutgoingMetaDTO outgoingMetaDTO = (OutgoingMetaDTO) o;
        if (outgoingMetaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), outgoingMetaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OutgoingMetaDTO{" +
            "id=" + getId() +
            ", sendAt='" + getSendAt() + "'" +
            ", expireAt=" + getExpireAt() +
            ", lastEditAt='" + getLastEditAt() + "'" +
            "}";
    }
}
