package pl.envelo.erds.ua.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class ActorDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActorDTO.class);
        ActorDTO actorDTO1 = new ActorDTO();
        actorDTO1.setId(1L);
        ActorDTO actorDTO2 = new ActorDTO();
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO2.setId(actorDTO1.getId());
        assertThat(actorDTO1).isEqualTo(actorDTO2);
        actorDTO2.setId(2L);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO1.setId(null);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
    }
}
