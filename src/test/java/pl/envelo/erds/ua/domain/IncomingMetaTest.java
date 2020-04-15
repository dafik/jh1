package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class IncomingMetaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomingMeta.class);
        IncomingMeta incomingMeta1 = new IncomingMeta();
        incomingMeta1.setId(1L);
        IncomingMeta incomingMeta2 = new IncomingMeta();
        incomingMeta2.setId(incomingMeta1.getId());
        assertThat(incomingMeta1).isEqualTo(incomingMeta2);
        incomingMeta2.setId(2L);
        assertThat(incomingMeta1).isNotEqualTo(incomingMeta2);
        incomingMeta1.setId(null);
        assertThat(incomingMeta1).isNotEqualTo(incomingMeta2);
    }
}
