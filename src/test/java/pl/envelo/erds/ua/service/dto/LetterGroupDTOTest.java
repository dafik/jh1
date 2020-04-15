package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class LetterGroupDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LetterGroupDTO.class);
        LetterGroupDTO letterGroupDTO1 = new LetterGroupDTO();
        letterGroupDTO1.setId(1L);
        LetterGroupDTO letterGroupDTO2 = new LetterGroupDTO();
        assertThat(letterGroupDTO1).isNotEqualTo(letterGroupDTO2);
        letterGroupDTO2.setId(letterGroupDTO1.getId());
        assertThat(letterGroupDTO1).isEqualTo(letterGroupDTO2);
        letterGroupDTO2.setId(2L);
        assertThat(letterGroupDTO1).isNotEqualTo(letterGroupDTO2);
        letterGroupDTO1.setId(null);
        assertThat(letterGroupDTO1).isNotEqualTo(letterGroupDTO2);
    }
}
