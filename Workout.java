import java.util.ArrayList;

/**
 * This class represents a Workout with exercises.
 *
 * @author Chehwan Hong
 * @version 2026.09.21
 */
public class Workout
{
    private String name;
    private ArrayList<Exercise> exercises;

    /**
     * Creates a Workout with a name.
     *
     * @param name
     *     name of the Workout
     * @throws IllegalArgumentException
     *     if name is null or empty
     */
    public Workout(String name)
    {
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        this.name = name;
        exercises = new ArrayList<Exercise>();
    }

    /**
     * Adds an exercise to the Workout.
     *
     * @param exercise
     *     exercise to add
     * @throws IllegalArgumentException
     *     if exercise is null
     */
    public void addExercise(Exercise exercise)
    {
        if (exercise == null)
        {
            throw new IllegalArgumentException();
        }

        exercises.add(exercise);
    }

    /**
     * Removes an exercise from the Workout.
     *
     * @param exercise
     *     exercise to remove
     */
    public void removeExercise(Exercise exercise)
    {
        exercises.remove(exercise);
    }

    /**
     * Gets all exercises in the Workout.
     *
     * @return list of exercises
     */
    public ArrayList<Exercise> getExercises()
    {
        return exercises;
    }

    /**
     * Gets the Workout name.
     *
     * @return name of the Workout
     */
    public String getName()
    {
        return name;
    }
}
