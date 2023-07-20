package learning.easyrandom;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class EasyRandomTest {

    EasyRandom generator;

    List<FakeTodo> todos;

    @BeforeEach
    public void setUp() {

        Predicate<Field> todoId = FieldPredicates.named("id")
                                                 .and(FieldPredicates.ofType(Long.class))
                                                 .and(FieldPredicates.inClass(FakeTodo.class));
        EasyRandomParameters todoParameters = new EasyRandomParameters()
                .randomize(todoId, new LongRangeRandomizer(1L, 100L));
        generator = new EasyRandom(todoParameters);
    }

    @DisplayName("id는 양수이기만 하면 된다")
    @Test
    public void idIsPositive() {

        todos = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            todos.add(generator.nextObject(FakeTodo.class));
        }

        for (int i = 0; i < 100; i++) {
            Assertions.assertThat(todos.get(i).getId()).isGreaterThan(-1L);
        }
    }

    private static class FakeTodo {

        private Long id;

        public Long getId() {
            return id;
        }
    }
}