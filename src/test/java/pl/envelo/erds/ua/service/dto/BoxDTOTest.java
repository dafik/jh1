package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class BoxDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BoxDTO.class);
        BoxDTO boxDTO1 = new BoxDTO();
        boxDTO1.setId(1L);
        BoxDTO boxDTO2 = new BoxDTO();
        assertThat(boxDTO1).isNotEqualTo(boxDTO2);
        boxDTO2.setId(boxDTO1.getId());
        assertThat(boxDTO1).isEqualTo(boxDTO2);
        boxDTO2.setId(2L);
        assertThat(boxDTO1).isNotEqualTo(boxDTO2);
        boxDTO1.setId(null);
        assertThat(boxDTO1).isNotEqualTo(boxDTO2);
    }
}
