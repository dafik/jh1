package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class ActorTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actor.class);
        Actor actor1 = new Actor();
        actor1.setId(1L);
        Actor actor2 = new Actor();
        actor2.setId(actor1.getId());
        assertThat(actor1).isEqualTo(actor2);
        actor2.setId(2L);
        assertThat(actor1).isNotEqualTo(actor2);
        actor1.setId(null);
        assertThat(actor1).isNotEqualTo(actor2);
    }
}
