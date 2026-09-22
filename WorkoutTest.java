import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Workout class.
 *
 * @author Chehwan Hong
 * @version 2026.09.21
 */
public class WorkoutTest
{
    /**
     * Tests getting the Workout name.
     */
    @Test
    public void testGetName()
    {
        Workout workout = new Workout("Chest Day");

        assertEquals("Chest Day", workout.getName());
    }

    /**
     * Tests an empty Workout name.
     */
    @Test
    public void testInvalidName()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Workout(""));
    }

    /**
     * Tests a null Workout name.
     */
    @Test
    public void testNullName()
    {
        assertThrows(
            IllegalArgumentException.class,
            () -> new Workout(null));
    }

    /**
     * Tests adding an Exercise.
     */
    @Test
    public void testAddExercise()
    {
        Workout workout = new Workout("Chest Day");
        Exercise exercise = new Exercise("Bench Press");

        workout.addExercise(exercise);

        assertTrue(workout.getExercises().contains(exercise));
    }

    /**
     * Tests adding a null Exercise.
     */
    @Test
    public void testAddNullExercise()
    {
        Workout workout = new Workout("Chest Day");

        assertThrows(
            IllegalArgumentException.class,
            () -> workout.addExercise(null));
    }

    /**
     * Tests removing an Exercise.
     */
    @Test
    public void testRemoveExercise()
    {
        Workout workout = new Workout("Chest Day");
        Exercise exercise = new Exercise("Bench Press");

        workout.addExercise(exercise);
        workout.removeExercise(exercise);

        assertFalse(workout.getExercises().contains(exercise));
    }

    /**
     * Tests removing an Exercise that is not in the Workout.
     */
    @Test
    public void testRemoveExerciseNotInWorkout()
    {
        Workout workout = new Workout("Chest Day");
        Exercise exercise = new Exercise("Bench Press");

        workout.removeExercise(exercise);

        assertTrue(workout.getExercises().isEmpty());
    }

    /**
     * Tests getting the Exercises in the Workout.
     */
    @Test
    public void testGetExercises()
    {
        Workout workout = new Workout("Chest Day");
        Exercise exercise = new Exercise("Bench Press");

        workout.addExercise(exercise);

        assertEquals(1, workout.getExercises().size());
        assertTrue(workout.getExercises().contains(exercise));
    }
}