package learning.jdk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class StringTest {

    @Test
    public void removeFirstNCharactersUsingSubstring() {
        assertThat("Bearer access-token".substring("Bearer ".length())).isEqualTo("access-token");
    }
}