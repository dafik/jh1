package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LetterMapperTest {

    private LetterMapper letterMapper;

    @BeforeEach
    public void setUp() {
        letterMapper = new LetterMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(letterMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(letterMapper.fromId(null)).isNull();
    }
}
