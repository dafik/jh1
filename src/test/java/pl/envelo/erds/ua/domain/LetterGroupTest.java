package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class LetterGroupTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LetterGroup.class);
        LetterGroup letterGroup1 = new LetterGroup();
        letterGroup1.setId(1L);
        LetterGroup letterGroup2 = new LetterGroup();
        letterGroup2.setId(letterGroup1.getId());
        assertThat(letterGroup1).isEqualTo(letterGroup2);
        letterGroup2.setId(2L);
        assertThat(letterGroup1).isNotEqualTo(letterGroup2);
        letterGroup1.setId(null);
        assertThat(letterGroup1).isNotEqualTo(letterGroup2);
    }
}
