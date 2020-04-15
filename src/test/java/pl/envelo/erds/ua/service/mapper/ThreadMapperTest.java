package pl.envelo.erds.ua.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ThreadMapperTest {

    private ThreadMapper threadMapper;

    @BeforeEach
    public void setUp() {
        threadMapper = new ThreadMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(threadMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(threadMapper.fromId(null)).isNull();
    }
}
