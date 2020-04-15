package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class ThreadDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ThreadDTO.class);
        ThreadDTO threadDTO1 = new ThreadDTO();
        threadDTO1.setId(1L);
        ThreadDTO threadDTO2 = new ThreadDTO();
        assertThat(threadDTO1).isNotEqualTo(threadDTO2);
        threadDTO2.setId(threadDTO1.getId());
        assertThat(threadDTO1).isEqualTo(threadDTO2);
        threadDTO2.setId(2L);
        assertThat(threadDTO1).isNotEqualTo(threadDTO2);
        threadDTO1.setId(null);
        assertThat(threadDTO1).isNotEqualTo(threadDTO2);
    }
}
