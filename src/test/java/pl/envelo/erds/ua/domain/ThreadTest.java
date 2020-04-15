package pl.envelo.erds.ua.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pl.envelo.erds.ua.web.rest.TestUtil;

public class ThreadTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Thread.class);
        Thread thread1 = new Thread();
        thread1.setId(1L);
        Thread thread2 = new Thread();
        thread2.setId(thread1.getId());
        assertThat(thread1).isEqualTo(thread2);
        thread2.setId(2L);
        assertThat(thread1).isNotEqualTo(thread2);
        thread1.setId(null);
        assertThat(thread1).isNotEqualTo(thread2);
    }
}
