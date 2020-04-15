package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class IncomingMetaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IncomingMetaDTO.class);
        IncomingMetaDTO incomingMetaDTO1 = new IncomingMetaDTO();
        incomingMetaDTO1.setId(1L);
        IncomingMetaDTO incomingMetaDTO2 = new IncomingMetaDTO();
        assertThat(incomingMetaDTO1).isNotEqualTo(incomingMetaDTO2);
        incomingMetaDTO2.setId(incomingMetaDTO1.getId());
        assertThat(incomingMetaDTO1).isEqualTo(incomingMetaDTO2);
        incomingMetaDTO2.setId(2L);
        assertThat(incomingMetaDTO1).isNotEqualTo(incomingMetaDTO2);
        incomingMetaDTO1.setId(null);
        assertThat(incomingMetaDTO1).isNotEqualTo(incomingMetaDTO2);
    }
}
