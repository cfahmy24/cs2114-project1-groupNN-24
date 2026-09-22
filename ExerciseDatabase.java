import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles the exercise database.
 */
public class ExerciseDatabase
{
    private ArrayList<Exercise> exercises;


    /**
     * Creates an empty exercise database.
     */
    public ExerciseDatabase()
    {
        exercises = new ArrayList<Exercise>();
    }


    /**
     * Loads exercises from a text file.
     *
     * Expected format:
     *
     * Bench Press|Chest,Triceps,Shoulders
     * Squat|Quadriceps,Glutes,Hamstrings
     * Barbell Curl|Biceps,Forearms
     *
     * @param filename
     *     name of the exercise file
     */
    public void loadExerciseDatabase(String filename)
    {
        try (BufferedReader reader =
            new BufferedReader(new FileReader(filename)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                line = line.trim();

                // Ignore blank lines
                if (line.isEmpty())
                {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length != 2)
                {
                    System.out.println(
                        "Skipping malformed database line: " + line);
                    continue;
                }

                String exerciseName = parts[0].trim();
                String muscleGroupString = parts[1].trim();

                try
                {
                    Exercise exercise = new Exercise(exerciseName);

                    String[] muscleGroups =
                        muscleGroupString.split(",");

                    for (String muscleGroup : muscleGroups)
                    {
                        String trimmedGroup = muscleGroup.trim();

                        if (!trimmedGroup.isEmpty())
                        {
                            exercise.addMuscleGroup(trimmedGroup);
                        }
                    }

                    exercises.add(exercise);
                }
                catch (IllegalArgumentException e)
                {
                    System.out.println(
                        "Skipping invalid exercise: "
                            + exerciseName);
                }
            }
        }
        catch (IOException e)
        {
            System.out.println(
                "Could not load exercise database: "
                    + filename);
        }
    }


    /**
     * Gets all exercises in the database.
     *
     * @return list of exercises
     */
    public ArrayList<Exercise> getExercises()
    {
        return exercises;
    }


    /**
     * Adds an exercise to the database.
     *
     * @param exercise
     *     exercise to add
     */
    public void addExercise(Exercise exercise)
    {
        exercises.add(exercise);
    }
}
