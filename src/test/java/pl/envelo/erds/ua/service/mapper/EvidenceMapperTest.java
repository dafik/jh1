package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EvidenceMapperTest {

    private EvidenceMapper evidenceMapper;

    @BeforeEach
    public void setUp() {
        evidenceMapper = new EvidenceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(evidenceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(evidenceMapper.fromId(null)).isNull();
    }
}
