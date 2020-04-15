package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class AddressBookItemTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressBookItem.class);
        AddressBookItem addressBookItem1 = new AddressBookItem();
        addressBookItem1.setId(1L);
        AddressBookItem addressBookItem2 = new AddressBookItem();
        addressBookItem2.setId(addressBookItem1.getId());
        assertThat(addressBookItem1).isEqualTo(addressBookItem2);
        addressBookItem2.setId(2L);
        assertThat(addressBookItem1).isNotEqualTo(addressBookItem2);
        addressBookItem1.setId(null);
        assertThat(addressBookItem1).isNotEqualTo(addressBookItem2);
    }
}
