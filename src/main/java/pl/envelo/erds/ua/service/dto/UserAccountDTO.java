package pl.envelo.erds.ua.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link pl.envelo.erds.ua.domain.UserAccount} entity.
 */
public class UserAccountDTO implements Serializable {
    
    private Long id;

    private String ncloakId;

    private String email;

    private String firstName;

    private String lastName;

    private Boolean isActive;

    private Set<BoxDTO> boxes = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNcloakId() {
        return ncloakId;
    }

    public void setNcloakId(String ncloakId) {
        this.ncloakId = ncloakId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<BoxDTO> getBoxes() {
        return boxes;
    }

    public void setBoxes(Set<BoxDTO> boxes) {
        this.boxes = boxes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserAccountDTO userAccountDTO = (UserAccountDTO) o;
        if (userAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserAccountDTO{" +
            "id=" + getId() +
            ", ncloakId='" + getNcloakId() + "'" +
            ", email='" + getEmail() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", isActive='" + isIsActive() + "'" +
            ", boxes='" + getBoxes() + "'" +
            "}";
    }
}
