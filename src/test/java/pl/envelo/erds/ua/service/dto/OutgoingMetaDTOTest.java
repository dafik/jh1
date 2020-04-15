package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class OutgoingMetaDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OutgoingMetaDTO.class);
        OutgoingMetaDTO outgoingMetaDTO1 = new OutgoingMetaDTO();
        outgoingMetaDTO1.setId(1L);
        OutgoingMetaDTO outgoingMetaDTO2 = new OutgoingMetaDTO();
        assertThat(outgoingMetaDTO1).isNotEqualTo(outgoingMetaDTO2);
        outgoingMetaDTO2.setId(outgoingMetaDTO1.getId());
        assertThat(outgoingMetaDTO1).isEqualTo(outgoingMetaDTO2);
        outgoingMetaDTO2.setId(2L);
        assertThat(outgoingMetaDTO1).isNotEqualTo(outgoingMetaDTO2);
        outgoingMetaDTO1.setId(null);
        assertThat(outgoingMetaDTO1).isNotEqualTo(outgoingMetaDTO2);
    }
}
