package kr.rogarithm.todos.domain.todo.fixture;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import kr.rogarithm.todos.domain.todo.dto.UpdateTodoRequest;
import org.jeasy.random.FieldPredicates;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.range.LongRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;

public class UpdateTodoParam {

    public static final Predicate<Field> ID = FieldPredicates.named("id")
                                                      .and(FieldPredicates.ofType(Long.class))
                                                      .and(FieldPredicates.inClass(UpdateTodoRequest.class));
    public static final Predicate<Field> STATE = FieldPredicates.named("state")
                                                         .and(FieldPredicates.ofType(String.class))
                                                         .and(FieldPredicates.inClass(UpdateTodoRequest.class));
    public static final Predicate<Field> NAME = FieldPredicates.named("name")
                                                        .and(FieldPredicates.ofType(String.class))
                                                        .and(FieldPredicates.inClass(UpdateTodoRequest.class));

    public static final LongRangeRandomizer IS_VALID_ID = new LongRangeRandomizer(1L, 100L);
    public static final LongRangeRandomizer IS_INVALID_ID = new LongRangeRandomizer(-100L, 0L);
    public static final Randomizer<String> IS_VALID_STATE = () -> List.of("INCOMPLETE", "COMPLETE").get(new Random().nextInt(1));
    public static final Randomizer<String> IS_INVALID_STATE = new StringRandomizer();
    public static final Randomizer<String> IS_INVALID_NAME = () -> "";
}