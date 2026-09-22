import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles workout creation and management.
 */
public class WorkoutManager
{
    private Scanner scanner;
    private ExerciseDatabase exerciseDatabase;


    /**
     * Creates a WorkoutManager.
     *
     * @param scanner
     *     scanner used for user input
     * @param exerciseDatabase
     *     database of available exercises
     */
    public WorkoutManager(
        Scanner scanner,
        ExerciseDatabase exerciseDatabase)
    {
        this.scanner = scanner;
        this.exerciseDatabase = exerciseDatabase;
    }


    /**
     * Creates a new workout.
     *
     * @return the created workout, or null if cancelled
     */
    public Workout createWorkout()
    {
        clearScreen();

        System.out.println("======================================");
        System.out.println("           CREATE WORKOUT              ");
        System.out.println("======================================");

        String workoutName;

        while (true)
        {
            System.out.print("Enter workout name: ");
            workoutName = scanner.nextLine().trim();

            if (!workoutName.isEmpty())
            {
                break;
            }

            System.out.println("Workout name cannot be empty.");
        }

        Workout workout;

        try
        {
            workout = new Workout(workoutName);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(
                "Could not create workout: " + e.getMessage());
            pause();
            return null;
        }

        boolean addingExercises = true;

        while (addingExercises)
        {
            clearScreen();

            System.out.println("======================================");
            System.out.println(workout.getName());
            System.out.println("======================================");

            System.out.println("Exercises currently added:");

            if (workout.getExercises().isEmpty())
            {
                System.out.println("  None");
            }
            else
            {
                for (int i = 0;
                    i < workout.getExercises().size();
                    i++)
                {
                    Exercise exercise =
                        workout.getExercises().get(i);

                    System.out.println(
                        "  " + (i + 1) + ". "
                            + exercise.getName());
                }
            }

            System.out.println();
            System.out.println("1. Add exercise");
            System.out.println("2. Remove exercise");
            System.out.println("3. Finish workout");
            System.out.println("4. Cancel workout");

            int choice = readInt("Select an option: ");

            switch (choice)
            {
                case 1:
                    addExerciseToWorkout(workout);
                    break;

                case 2:
                    removeExerciseFromWorkout(workout);
                    break;

                case 3:
                    if (workout.getExercises().isEmpty())
                    {
                        System.out.println(
                            "A workout must contain at least one exercise.");
                        pause();
                    }
                    else
                    {
                        return workout;
                    }
                    break;

                case 4:
                    System.out.println("Workout cancelled.");
                    pause();
                    return null;

                default:
                    System.out.println("Invalid option.");
                    pause();
            }
        }

        return null;
    }


    /**
     * Adds an exercise from the database to a workout.
     *
     * @param workout
     *     workout to add the exercise to
     */
    private void addExerciseToWorkout(Workout workout)
    {
        Exercise selected = selectExercise();

        if (selected == null)
        {
            return;
        }

        /*
         * Create a new Exercise instead of putting the database object
         * directly into the workout.
         *
         * This is important because the database represents exercise
         * templates, while the workout contains the user's actual sets.
         */
        Exercise workoutExercise =
            new Exercise(selected.getName());

        for (String muscleGroup : selected.getMuscleGroup())
        {
            workoutExercise.addMuscleGroup(muscleGroup);
        }

        addSetsToExercise(workoutExercise);

        try
        {
            workout.addExercise(workoutExercise);

            System.out.println(
                workoutExercise.getName()
                    + " added to workout.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(
                "Could not add exercise: " + e.getMessage());
        }

        pause();
    }


    /**
     * Allows the user to select an exercise from the database.
     *
     * @return selected exercise, or null if cancelled
     */
    private Exercise selectExercise()
    {
        ArrayList<Exercise> exercises =
            exerciseDatabase.getExercises();

        if (exercises.isEmpty())
        {
            System.out.println("No exercises available.");
            pause();
            return null;
        }

        int page = 0;
        final int pageSize = 9;

        while (true)
        {
            clearScreen();

            int start = page * pageSize;
            int end = Math.min(
                start + pageSize,
                exercises.size());

            int totalPages =
                (exercises.size() + pageSize - 1) / pageSize;

            System.out.println("======================================");
            System.out.println("          SELECT EXERCISE              ");
            System.out.println(
                "Page " + (page + 1) + " of " + totalPages);
            System.out.println("======================================");

            for (int i = start; i < end; i++)
            {
                Exercise exercise = exercises.get(i);

                System.out.println(
                    (i - start + 1) + ". "
                        + exercise.getName());

                System.out.println(
                    "   "
                        + formatMuscleGroups(
                            exercise.getMuscleGroup()));
            }

            System.out.println();
            System.out.println("n. Next page");
            System.out.println("p. Previous page");
            System.out.println("q. Cancel");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("n"))
            {
                if (page < totalPages - 1)
                {
                    page++;
                }
            }
            else if (input.equalsIgnoreCase("p"))
            {
                if (page > 0)
                {
                    page--;
                }
            }
            else if (input.equalsIgnoreCase("q"))
            {
                return null;
            }
            else
            {
                try
                {
                    int selection =
                        Integer.parseInt(input);

                    if (selection >= 1
                        && selection <= end - start)
                    {
                        return exercises.get(
                            start + selection - 1);
                    }

                    System.out.println("Invalid selection.");
                    pause();
                }
                catch (NumberFormatException e)
                {
                    System.out.println("Invalid input.");
                    pause();
                }
            }
        }
    }


    /**
     * Adds sets to an exercise.
     *
     * @param exercise
     *     exercise receiving the sets
     */
    private void addSetsToExercise(Exercise exercise)
    {
        boolean addingSets = true;

        while (addingSets)
        {
            clearScreen();

            System.out.println("======================================");
            System.out.println(
                "Adding sets to: " + exercise.getName());
            System.out.println("======================================");

            System.out.println(
                "Current sets: " + exercise.getSets().size());

            System.out.println();
            System.out.println("1. Add set");
            System.out.println("2. Finish adding sets");

            int choice = readInt("Select an option: ");

            switch (choice)
            {
                case 1:
                    int reps =
                        readInt("Enter repetitions (1-99): ");

                    double weight =
                        readDouble("Enter weight: ");

                    try
                    {
                        Set set = new Set(reps, weight);
                        exercise.addSet(set);

                        System.out.println(
                            "Set added: " + reps
                                + " reps @ " + weight);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.out.println(
                            "Invalid set: " + e.getMessage());
                    }

                    pause();
                    break;

                case 2:
                    if (exercise.getSets().isEmpty())
                    {
                        System.out.println(
                            "An exercise must have at least one set.");
                        pause();
                    }
                    else
                    {
                        addingSets = false;
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
                    pause();
            }
        }
    }


    /**
     * Removes an exercise from a workout.
     *
     * @param workout
     *     workout to remove the exercise from
     */
    private void removeExerciseFromWorkout(Workout workout)
    {
        if (workout.getExercises().isEmpty())
        {
            System.out.println(
                "There are no exercises to remove.");
            pause();
            return;
        }

        System.out.println("Select an exercise to remove:");

        for (int i = 0;
            i < workout.getExercises().size();
            i++)
        {
            System.out.println(
                (i + 1) + ". "
                    + workout.getExercises().get(i).getName());
        }

        int choice = readInt("Selection: ");

        if (choice >= 1
            && choice <= workout.getExercises().size())
        {
            Exercise exercise =
                workout.getExercises().get(choice - 1);

            workout.removeExercise(exercise);

            System.out.println(
                exercise.getName() + " removed.");
        }
        else
        {
            System.out.println("Invalid selection.");
        }

        pause();
    }


    private int readInt(String message)
    {
        while (true)
        {
            System.out.print(message);

            String input = scanner.nextLine().trim();

            try
            {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Please enter a valid integer.");
            }
        }
    }


    private double readDouble(String message)
    {
        while (true)
        {
            System.out.print(message);

            String input = scanner.nextLine().trim();

            try
            {
                return Double.parseDouble(input);
            }
            catch (NumberFormatException e)
            {
                System.out.println(
                    "Please enter a valid number.");
            }
        }
    }


    private void pause()
    {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }


    private void clearScreen()
    {
        for (int i = 0; i < 30; i++)
        {
            System.out.println();
        }
    }


    private String formatMuscleGroups(
        ArrayList<String> muscleGroups)
    {
        if (muscleGroups.isEmpty())
        {
            return "None";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < muscleGroups.size(); i++)
        {
            result.append(muscleGroups.get(i));

            if (i < muscleGroups.size() - 1)
            {
                result.append(", ");
            }
        }

        return result.toString();
    }
}