package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Attachment} entity.
 */
public class AttachmentDTO implements Serializable {
    
    private Long id;

    private String name;

    private Integer size;

    private String s3name;

    private String contentType;


    private Long letterGroupId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String gets3name() {
        return s3name;
    }

    public void sets3name(String s3name) {
        this.s3name = s3name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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

        AttachmentDTO attachmentDTO = (AttachmentDTO) o;
        if (attachmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attachmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttachmentDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", size=" + getSize() +
            ", s3name='" + gets3name() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", letterGroupId=" + getLetterGroupId() +
            "}";
    }
}
