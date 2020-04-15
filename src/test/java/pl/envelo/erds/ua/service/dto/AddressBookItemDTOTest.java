package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class AddressBookItemDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressBookItemDTO.class);
        AddressBookItemDTO addressBookItemDTO1 = new AddressBookItemDTO();
        addressBookItemDTO1.setId(1L);
        AddressBookItemDTO addressBookItemDTO2 = new AddressBookItemDTO();
        assertThat(addressBookItemDTO1).isNotEqualTo(addressBookItemDTO2);
        addressBookItemDTO2.setId(addressBookItemDTO1.getId());
        assertThat(addressBookItemDTO1).isEqualTo(addressBookItemDTO2);
        addressBookItemDTO2.setId(2L);
        assertThat(addressBookItemDTO1).isNotEqualTo(addressBookItemDTO2);
        addressBookItemDTO1.setId(null);
        assertThat(addressBookItemDTO1).isNotEqualTo(addressBookItemDTO2);
    }
}
