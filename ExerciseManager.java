import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles exercise browsing and custom exercise creation.
 */
public class ExerciseManager
{
    private Scanner scanner;
    private ExerciseDatabase exerciseDatabase;


    /**
     * Creates an ExerciseManager.
     *
     * @param scanner
     *     scanner used for user input
     * @param exerciseDatabase
     *     database of available exercises
     */
    public ExerciseManager(
        Scanner scanner,
        ExerciseDatabase exerciseDatabase)
    {
        this.scanner = scanner;
        this.exerciseDatabase = exerciseDatabase;
    }


    /**
     * Allows the user to browse the exercise database.
     */
    public void browseExercises()
    {
        ArrayList<Exercise> exercises =
            exerciseDatabase.getExercises();

        if (exercises.isEmpty())
        {
            System.out.println(
                "There are no exercises in the database.");
            pause();
            return;
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
            System.out.println("          EXERCISE DATABASE            ");
            System.out.println(
                "          Page " + (page + 1)
                    + " of " + totalPages);
            System.out.println("======================================");

            for (int i = start; i < end; i++)
            {
                Exercise exercise = exercises.get(i);

                System.out.println(
                    (i - start + 1) + ". "
                        + exercise.getName());

                System.out.println(
                    "   Muscles: "
                        + formatMuscleGroups(
                            exercise.getMuscleGroup()));
            }

            System.out.println();
            System.out.println("n. Next page");
            System.out.println("p. Previous page");
            System.out.println("q. Return to main menu");

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
                return;
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
                        Exercise selected =
                            exercises.get(
                                start + selection - 1);

                        displayExercise(selected);
                    }
                    else
                    {
                        System.out.println(
                            "Invalid selection.");
                        pause();
                    }
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
     * Displays information about one exercise.
     *
     * @param exercise
     *     exercise to display
     */
    private void displayExercise(Exercise exercise)
    {
        clearScreen();

        System.out.println("======================================");
        System.out.println(exercise.getName());
        System.out.println("======================================");

        System.out.println(
            "Muscle groups: "
                + formatMuscleGroups(
                    exercise.getMuscleGroup()));

        System.out.println(
            "Sets currently stored: "
                + exercise.getSets().size());

        pause();
    }


    /**
     * Creates a custom exercise.
     *
     * @param name
     *     name of the exercise
     * @param muscles
     *     muscle groups for the exercise
     * @param set
     *     first set for the exercise
     * @return the created exercise
     */
    private Exercise createAnExercise(
        String name,
        ArrayList<String> muscles,
        Set set)
    {
        Exercise newExercise = new Exercise(name);

        newExercise.addSet(set);

        for (int i = 0; i < muscles.size(); i++)
        {
            newExercise.addMuscleGroup(
                muscles.get(i));
        }

        return newExercise;
    }


    /**
     * Allows the user to create a custom exercise.
     */
    public void addCustomExercise()
    {
        clearScreen();

        System.out.println("======================================");
        System.out.println("          ADD CUSTOM EXERCISE");
        System.out.println("======================================");

        System.out.print("Enter exercise name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty())
        {
            System.out.println(
                "Exercise name cannot be empty.");
            pause();
            return;
        }

        ArrayList<String> muscles = new ArrayList<>();

        boolean addingMuscles = true;

        while (addingMuscles)
        {
            System.out.println();

            System.out.println(
                "Current muscle groups: "
                    + formatMuscleGroups(muscles));

            System.out.println("1. Add muscle group");
            System.out.println("2. Finish");

            int choice = readInt("Select an option: ");

            switch (choice)
            {
                case 1:
                    System.out.print(
                        "Enter muscle group: ");

                    String muscle =
                        scanner.nextLine().trim();

                    if (muscle.isEmpty())
                    {
                        System.out.println(
                            "Muscle group cannot be empty.");
                    }
                    else
                    {
                        muscles.add(muscle);

                        System.out.println(
                            "Muscle group added.");
                    }
                    break;

                case 2:
                    if (muscles.isEmpty())
                    {
                        System.out.println(
                            "An exercise must have at least "
                                + "one muscle group.");
                    }
                    else
                    {
                        addingMuscles = false;
                    }
                    break;

                default:
                    System.out.println(
                        "Invalid option.");
            }
        }

        int reps =
            readInt(
                "Enter repetitions for the first set (1-99): ");

        double weight =
            readDouble(
                "Enter weight for the first set: ");

        try
        {
            Set set = new Set(reps, weight);

            Exercise newExercise =
                createAnExercise(
                    name,
                    muscles,
                    set);

            exerciseDatabase.addExercise(newExercise);

            System.out.println();
            System.out.println(
                "Custom exercise created!");
            System.out.println(
                "Name: " + newExercise.getName());

            System.out.println(
                "Muscles: "
                    + formatMuscleGroups(
                        newExercise.getMuscleGroup()));

            System.out.println();
            System.out.println(
                "It has been added to your exercise database.");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(
                "Could not create exercise: "
                    + e.getMessage());
        }

        pause();
    }


    private int readInt(String message)
    {
        while (true)
        {
            System.out.print(message);

            String input =
                scanner.nextLine().trim();

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

            String input =
                scanner.nextLine().trim();

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
        System.out.println(
            "Press Enter to continue...");
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

        StringBuilder result =
            new StringBuilder();

        for (int i = 0;
            i < muscleGroups.size();
            i++)
        {
            result.append(
                muscleGroups.get(i));

            if (i < muscleGroups.size() - 1)
            {
                result.append(", ");
            }
        }

        return result.toString();
    }
}