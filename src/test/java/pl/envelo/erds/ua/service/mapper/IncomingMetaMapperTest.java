package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class IncomingMetaMapperTest {

    private IncomingMetaMapper incomingMetaMapper;

    @BeforeEach
    public void setUp() {
        incomingMetaMapper = new IncomingMetaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(incomingMetaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(incomingMetaMapper.fromId(null)).isNull();
    }
}
