package pl.envelo.erds.ua.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.Thread} entity.
 */
public class ThreadDTO implements Serializable {
    
    private Long id;

    private String subject;

    private ZonedDateTime lastLetterTime;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ZonedDateTime getLastLetterTime() {
        return lastLetterTime;
    }

    public void setLastLetterTime(ZonedDateTime lastLetterTime) {
        this.lastLetterTime = lastLetterTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThreadDTO threadDTO = (ThreadDTO) o;
        if (threadDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), threadDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ThreadDTO{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", lastLetterTime='" + getLastLetterTime() + "'" +
            "}";
    }
}
