package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LetterGroupMapperTest {

    private LetterGroupMapper letterGroupMapper;

    @BeforeEach
    public void setUp() {
        letterGroupMapper = new LetterGroupMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(letterGroupMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(letterGroupMapper.fromId(null)).isNull();
    }
}
