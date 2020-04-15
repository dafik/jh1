package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressBookItemMapperTest {

    private AddressBookItemMapper addressBookItemMapper;

    @BeforeEach
    public void setUp() {
        addressBookItemMapper = new AddressBookItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(addressBookItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(addressBookItemMapper.fromId(null)).isNull();
    }
}
