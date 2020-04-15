package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class OutgoingMetaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutgoingMeta.class);
        OutgoingMeta outgoingMeta1 = new OutgoingMeta();
        outgoingMeta1.setId(1L);
        OutgoingMeta outgoingMeta2 = new OutgoingMeta();
        outgoingMeta2.setId(outgoingMeta1.getId());
        assertThat(outgoingMeta1).isEqualTo(outgoingMeta2);
        outgoingMeta2.setId(2L);
        assertThat(outgoingMeta1).isNotEqualTo(outgoingMeta2);
        outgoingMeta1.setId(null);
        assertThat(outgoingMeta1).isNotEqualTo(outgoingMeta2);
    }
}
