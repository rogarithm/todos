package learning.jdk;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StreamTest {

    @Test
    public void makeIntegerListFromString() {

        String string = "123";
        List<Integer> integers = Arrays.stream(string.split(""))
                                       .map(Integer::parseInt)
                                       .toList();

        assertThat(integers).isEqualTo(List.of(1, 2, 3));
    }

}