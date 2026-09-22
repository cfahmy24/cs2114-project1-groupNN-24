import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Main class for Gym Buddy.
 *
 * This class handles:
 * - Terminal user interface
 * - Exercise database loading
 * - Workout creation
 * - Workout logging
 * - Weekly progression
 * - Workout history
 * - Muscle-group tracking
 * - Exercise recommendations
 *
 * The Exercise, Workout, and Set classes are responsible for their
 * own data and validation.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);

 // Exercise database loaded from exercises.txt
    private static ExerciseDatabase exerciseDatabase =
        new ExerciseDatabase();

    // Workouts planned for the current week
    private static ArrayList<Workout> currentWeekWorkouts = new ArrayList<>();

    // Completed workouts organized by week number
    private static Map<Integer, ArrayList<Workout>> workoutHistory =
        new HashMap<>();

    // Start at week 1
    private static int currentWeek = 1;

    public static void main(String[] args) {

        exerciseDatabase.loadExerciseDatabase("exercises.txt");

        System.out.println("======================================");
        System.out.println("           WELCOME TO GYM BUDDY       ");
        System.out.println("======================================");

        if (exerciseDatabase.getExercises().isEmpty()) {
            System.out.println("WARNING: No exercises were loaded.");
            System.out.println("Make sure exercises.txt exists.");
        }
        else {
            System.out.println(
                "Loaded " + exerciseDatabase.getExercises().size()
                    + " exercises.");
        }

        pause();

        boolean running = true;

        while (running) {
            clearScreen();

            System.out.println("======================================");
            System.out.println("              GYM BUDDY                ");
            System.out.println("======================================");
            System.out.println("Current Week: " + currentWeek);
            System.out.println();
            System.out.println("1. Create a workout");
            System.out.println("2. View current week's workouts");
            System.out.println("3. Complete/log a workout");
            System.out.println("4. Browse exercise database");
            System.out.println("5. Get exercise recommendations");
            System.out.println("6. View workout history");
            System.out.println("7. Add a custom exercise");
            System.out.println("8. Check workout improvement");
            System.out.println("9. Advance to next week");
            System.out.println("10. Exit");
            System.out.println("--------------------------------------");

            int choice = readInt("Select an option: ");

            switch (choice) {
                case 1:
                    WorkoutManager workoutManager =
                        new WorkoutManager(scanner, exerciseDatabase);

                    Workout newWorkout = workoutManager.createWorkout();

                    if (newWorkout != null)
                    {
                        currentWeekWorkouts.add(newWorkout);

                        System.out.println(
                            "Workout added to Week " + currentWeek + ".");
                        pause();
                    }

                    break;

                case 2:
                    viewCurrentWorkouts();
                    break;

                case 3:
                    completeWorkout();
                    break;

                case 4:
                    ExerciseManager exerciseManager =
                        new ExerciseManager(scanner, exerciseDatabase);

                    exerciseManager.browseExercises();
                    break;

                case 5:
                    clearScreen();

                    Recommendation.recommendExercises(
                        exerciseDatabase.getExercises(),
                        workoutHistory,
                        currentWeek);

                    pause();
                    break;

                case 6:
                    clearScreen();

                    WorkoutHistory.viewHistory(
                        workoutHistory,
                        currentWeek);

                    pause();
                    break;

                case 7:
                    ExerciseManager customExerciseManager =
                        new ExerciseManager(scanner, exerciseDatabase);

                    customExerciseManager.addCustomExercise();
                    break;

                case 8:
                    clearScreen();

                    WorkoutHistory.checkImprovement(
                        workoutHistory,
                        currentWeek);

                    pause();
                    break;

                case 9:
                    advanceWeek();
                    break;

                case 10:
                    running = false;
                    System.out.println("Thanks for using Gym Buddy!");
                    break;

                default:
                    System.out.println("Invalid option.");
                    pause();
            }
        }

        scanner.close();
    }

    // ============================================================
    // CURRENT WORKOUTS
    // ============================================================


    private static void viewCurrentWorkouts() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("       WEEK " + currentWeek + " WORKOUTS");
        System.out.println("======================================");

        if (currentWeekWorkouts.isEmpty()) {

            System.out.println("No workouts have been planned.");
            pause();
            return;
        }

        for (int i = 0; i < currentWeekWorkouts.size(); i++) {

            Workout workout = currentWeekWorkouts.get(i);

            System.out.println((i + 1) + ". " + workout.getName());

            for (Exercise exercise : workout.getExercises()) {

                System.out.println("   - " + exercise.getName() + " | "
                    + formatMuscleGroups(exercise.getMuscleGroup()));

                for (Set set : exercise.getSets()) {

                    System.out.println("       " + set.getReps() + " reps @ "
                        + set.getWeight());
                }
            }

            System.out.println();
        }

        pause();
    }

    // ============================================================
    // COMPLETING WORKOUTS
    // ============================================================


    /**
     * Marks one of the planned workouts as completed.
     *
     * Since the MVP does not have a separate WorkoutHistory class,
     * completed workouts are stored in workoutHistory by week.
     */
    private static void completeWorkout() {

        clearScreen();

        if (currentWeekWorkouts.isEmpty()) {

            System.out.println("There are no workouts planned "
                + "for this week.");
            pause();
            return;
        }

        System.out.println("======================================");
        System.out.println("       COMPLETE WORKOUT - WEEK " + currentWeek);
        System.out.println("======================================");

        for (int i = 0; i < currentWeekWorkouts.size(); i++) {

            System.out.println((i + 1) + ". " + currentWeekWorkouts.get(i)
                .getName());
        }

        System.out.println("0. Cancel");

        int choice = readInt("Select workout: ");

        if (choice == 0) {
            return;
        }

        if (choice < 1 || choice > currentWeekWorkouts.size()) {

            System.out.println("Invalid selection.");
            pause();
            return;
        }

        Workout workout = currentWeekWorkouts.get(choice - 1);

        if (!workoutHistory.containsKey(currentWeek)) {

            workoutHistory.put(currentWeek, new ArrayList<Workout>());
        }

        workoutHistory.get(currentWeek).add(workout);

        currentWeekWorkouts.remove(choice - 1);

        System.out.println(workout.getName() + " marked as completed.");

        pause();
    }

    // ============================================================
    // WEEK MANAGEMENT
    // ============================================================


    private static void advanceWeek() {

        clearScreen();

        System.out.println("You are currently on Week " + currentWeek + ".");

        if (!currentWeekWorkouts.isEmpty()) {

            System.out.println();
            System.out.println("WARNING: You still have " + currentWeekWorkouts
                .size() + " planned workout(s) "
                + "that have not been completed.");

            System.out.println();
            System.out.println("1. Advance anyway");
            System.out.println("2. Cancel");

            int choice = readInt("Select an option: ");

            if (choice != 1) {
                return;
            }
        }

        currentWeek++;

        currentWeekWorkouts.clear();

        System.out.println();
        System.out.println("Advanced to Week " + currentWeek + "!");

        pause();
    }

    // ============================================================
    // INPUT HELPERS
    // ============================================================


    /**
     * Safely reads an integer from the terminal.
     */
    private static int readInt(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                return Integer.parseInt(input);

            }
            catch (NumberFormatException e) {

                System.out.println("Please enter a valid integer.");
            }
        }
    }


    /**
     * Safely reads a double from the terminal.
     */
    private static double readDouble(String message) {

        while (true) {

            System.out.print(message);

            String input = scanner.nextLine().trim();

            try {

                return Double.parseDouble(input);

            }
            catch (NumberFormatException e) {

                System.out.println("Please enter a valid number.");
            }
        }
    }


    /**
     * Pauses the terminal until the user presses Enter.
     */
    private static void pause() {

        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }


    /**
     * Attempts to make the terminal easier to read.
     *
     * This is intentionally simple because ANSI clear-screen
     * commands do not work identically in every terminal.
     */
    private static void clearScreen() {

        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }


    /**
     * Formats an ArrayList of muscle groups into a readable String.
     */
    private static String formatMuscleGroups(ArrayList<String> muscleGroups) {

        if (muscleGroups.isEmpty()) {
            return "None";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < muscleGroups.size(); i++) {

            result.append(muscleGroups.get(i));

            if (i < muscleGroups.size() - 1) {
                result.append(", ");
            }
        }

        return result.toString();
    }
}
