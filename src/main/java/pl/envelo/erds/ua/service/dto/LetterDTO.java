package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Letter} entity.
 */
public class LetterDTO implements Serializable {
    
    private Long id;

    private String ean;

    private String erdsId;


    private Long senderId;

    private Long receipientId;

    private Long letterGroupId;
    
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long actorId) {
        this.senderId = actorId;
    }

    public Long getReceipientId() {
        return receipientId;
    }

    public void setReceipientId(Long actorId) {
        this.receipientId = actorId;
    }

    public Long getLetterGroupId() {
        return letterGroupId;
    }

    public void setLetterGroupId(Long letterGroupId) {
        this.letterGroupId = letterGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LetterDTO letterDTO = (LetterDTO) o;
        if (letterDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), letterDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LetterDTO{" +
            "id=" + getId() +
            ", ean='" + getEan() + "'" +
            ", erdsId='" + getErdsId() + "'" +
            ", senderId=" + getSenderId() +
            ", receipientId=" + getReceipientId() +
            ", letterGroupId=" + getLetterGroupId() +
            "}";
    }
}
