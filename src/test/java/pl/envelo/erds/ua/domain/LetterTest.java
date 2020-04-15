package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class LetterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Letter.class);
        Letter letter1 = new Letter();
        letter1.setId(1L);
        Letter letter2 = new Letter();
        letter2.setId(letter1.getId());
        assertThat(letter1).isEqualTo(letter2);
        letter2.setId(2L);
        assertThat(letter1).isNotEqualTo(letter2);
        letter1.setId(null);
        assertThat(letter1).isNotEqualTo(letter2);
    }
}
