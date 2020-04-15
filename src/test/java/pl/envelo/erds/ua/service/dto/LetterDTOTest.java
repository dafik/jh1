package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class LetterDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LetterDTO.class);
        LetterDTO letterDTO1 = new LetterDTO();
        letterDTO1.setId(1L);
        LetterDTO letterDTO2 = new LetterDTO();
        assertThat(letterDTO1).isNotEqualTo(letterDTO2);
        letterDTO2.setId(letterDTO1.getId());
        assertThat(letterDTO1).isEqualTo(letterDTO2);
        letterDTO2.setId(2L);
        assertThat(letterDTO1).isNotEqualTo(letterDTO2);
        letterDTO1.setId(null);
        assertThat(letterDTO1).isNotEqualTo(letterDTO2);
    }
}
