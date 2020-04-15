package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class BoxTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Box.class);
        Box box1 = new Box();
        box1.setId(1L);
        Box box2 = new Box();
        box2.setId(box1.getId());
        assertThat(box1).isEqualTo(box2);
        box2.setId(2L);
        assertThat(box1).isNotEqualTo(box2);
        box1.setId(null);
        assertThat(box1).isNotEqualTo(box2);
    }
}
