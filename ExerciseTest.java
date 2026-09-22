import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;

/**
 * Tests the Exercise class.
 *
 * @author alex leon
 * @version 2026.09.21
 */
class ExerciseTest {

    // ~ Fields ................................................................
    private Exercise exercise;

    // ~ Constructors ..........................................................
    /**
     * set up JUnit testing
     */
    @Before
    public void setUp() {
        exercise = new Exercise("bench press");
        exercise.addMuscleGroup("chest");
    }

    // ~Public Methods ........................................................


    /**
     * Tests the getName and setName methods.
     */
    @Test
    public void testGetName() {
        assertEquals("bench press", exercise.getName());
        exercise.setName("leg press");
        assertEquals("leg press", exercise.getName());

    }


    /**
     * Tests the getMuscleGroup, addMuscleGroup and removeMuscleGroup methods.
     */
    @Test
    public void testGetMuscleGroup() {
        assertTrue(exercise.getMuscleGroup().isEmpty());
        exercise.addMuscleGroup("chest");
        assertEquals("chest", exercise.getMuscleGroup());
        exercise.removeMuscleGroup("chest");
        assertTrue(exercise.getMuscleGroup().isEmpty());
    }


    /**
     * Tests the getSets and addSet methods.
     */
    @Test
    public void testGetSets() {
        exercise = new Exercise("bench press");
        assertTrue(exercise.getSets().isEmpty());
        Set first = new Set(8, 20.00);
        exercise.addSet(first);
        assertEquals(first, exercise.getSets());
    }

}
