package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class EvidenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Evidence.class);
        Evidence evidence1 = new Evidence();
        evidence1.setId(1L);
        Evidence evidence2 = new Evidence();
        evidence2.setId(evidence1.getId());
        assertThat(evidence1).isEqualTo(evidence2);
        evidence2.setId(2L);
        assertThat(evidence1).isNotEqualTo(evidence2);
        evidence1.setId(null);
        assertThat(evidence1).isNotEqualTo(evidence2);
    }
}
