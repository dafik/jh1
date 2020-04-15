package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class OutgoingMetaMapperTest {

    private OutgoingMetaMapper outgoingMetaMapper;

    @BeforeEach
    public void setUp() {
        outgoingMetaMapper = new OutgoingMetaMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(outgoingMetaMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(outgoingMetaMapper.fromId(null)).isNull();
    }
}
