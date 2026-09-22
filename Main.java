import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
    private static ArrayList<Exercise> exerciseDatabase = new ArrayList<>();

    // Workouts planned for the current week
    private static ArrayList<Workout> currentWeekWorkouts = new ArrayList<>();

    // Completed workouts organized by week number
    private static Map<Integer, ArrayList<Workout>> workoutHistory =
        new HashMap<>();

    // Start at week 1
    private static int currentWeek = 1;

    public static void main(String[] args) {

        loadExerciseDatabase("exercises.txt");

        System.out.println("======================================");
        System.out.println("           WELCOME TO GYM BUDDY       ");
        System.out.println("======================================");

        if (exerciseDatabase.isEmpty()) {
            System.out.println("WARNING: No exercises were loaded.");
            System.out.println("Make sure exercises.txt exists.");
        }
        else {
            System.out.println("Loaded " + exerciseDatabase.size()
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
                    createWorkout();
                    break;

                case 2:
                    viewCurrentWorkouts();
                    break;

                case 3:
                    completeWorkout();
                    break;

                case 4:
                    browseExercises();
                    break;

                case 5:
                    recommendExercises();
                    break;

                case 6:
                    viewHistory();
                    break;

                case 7:
                    addCustomExercise();
                    break;

                case 8:
                    checkImprovement();
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
    // EXERCISE DATABASE
    // ============================================================


    /**
     * Loads exercises from a text file.
     *
     * Expected format:
     *
     * Bench Press|Chest,Triceps,Shoulders
     * Squat|Quadriceps,Glutes,Hamstrings
     * Barbell Curl|Biceps,Forearms
     */
    private static void loadExerciseDatabase(String filename) {

        try (BufferedReader reader = new BufferedReader(new FileReader(
            filename))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                // Ignore blank lines
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\|");

                if (parts.length != 2) {
                    System.out.println("Skipping malformed database line: "
                        + line);
                    continue;
                }

                String exerciseName = parts[0].trim();
                String muscleGroupString = parts[1].trim();

                try {
                    Exercise exercise = new Exercise(exerciseName);

                    String[] muscleGroups = muscleGroupString.split(",");

                    for (String muscleGroup : muscleGroups) {
                        String trimmedGroup = muscleGroup.trim();

                        if (!trimmedGroup.isEmpty()) {
                            exercise.addMuscleGroup(trimmedGroup);
                        }
                    }

                    exerciseDatabase.add(exercise);

                }
                catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid exercise: "
                        + exerciseName);
                }
            }

        }
        catch (IOException e) {
            System.out.println("Could not load exercise database: " + filename);
        }
    }


    /**
     * Allows the user to browse the exercise database.
     */
    private static void browseExercises() {

        if (exerciseDatabase.isEmpty()) {
            System.out.println("There are no exercises in the database.");
            pause();
            return;
        }

        int page = 0;
        final int pageSize = 9;

        while (true) {

            clearScreen();

            int start = page * pageSize;
            int end = Math.min(start + pageSize, exerciseDatabase.size());

            int totalPages = (exerciseDatabase.size() + pageSize - 1)
                / pageSize;

            System.out.println("======================================");
            System.out.println("          EXERCISE DATABASE            ");
            System.out.println("          Page " + (page + 1) + " of "
                + totalPages);
            System.out.println("======================================");

            for (int i = start; i < end; i++) {

                Exercise exercise = exerciseDatabase.get(i);

                System.out.println((i - start + 1) + ". " + exercise.getName());

                System.out.println("   Muscles: " + formatMuscleGroups(exercise
                    .getMuscleGroup()));
            }

            System.out.println();
            System.out.println("n. Next page");
            System.out.println("p. Previous page");
            System.out.println("q. Return to main menu");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("n")) {

                if (page < totalPages - 1) {
                    page++;
                }

            }
            else if (input.equalsIgnoreCase("p")) {

                if (page > 0) {
                    page--;
                }

            }
            else if (input.equalsIgnoreCase("q")) {
                return;

            }
            else {
                try {

                    int selection = Integer.parseInt(input);

                    if (selection >= 1 && selection <= end - start) {

                        Exercise selected = exerciseDatabase.get(start
                            + selection - 1);

                        displayExercise(selected);

                    }
                    else {
                        System.out.println("Invalid selection.");
                        pause();
                    }

                }
                catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                    pause();
                }
            }
        }
    }


    /**
     * Displays information about one exercise.
     */
    private static void displayExercise(Exercise exercise) {

        clearScreen();

        System.out.println("======================================");
        System.out.println(exercise.getName());
        System.out.println("======================================");

        System.out.println("Muscle groups: " + formatMuscleGroups(exercise
            .getMuscleGroup()));

        System.out.println("Sets currently stored: " + exercise.getSets()
            .size());

        pause();
    }

    // ============================================================
    // WORKOUT CREATION
    // ============================================================


    /**
     * Creates a new workout for the current week.
     */
    private static void createWorkout() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("           CREATE WORKOUT              ");
        System.out.println("======================================");

        String workoutName;

        while (true) {

            System.out.print("Enter workout name: ");
            workoutName = scanner.nextLine().trim();

            if (!workoutName.isEmpty()) {
                break;
            }

            System.out.println("Workout name cannot be empty.");
        }

        Workout workout;

        try {
            workout = new Workout(workoutName);
        }
        catch (IllegalArgumentException e) {
            System.out.println("Could not create workout: " + e.getMessage());
            pause();
            return;
        }

        boolean addingExercises = true;

        while (addingExercises) {

            clearScreen();

            System.out.println("======================================");
            System.out.println(workout.getName());
            System.out.println("======================================");

            System.out.println("Exercises currently added:");

            if (workout.getExercises().isEmpty()) {
                System.out.println("  None");
            }
            else {
                for (int i = 0; i < workout.getExercises().size(); i++) {

                    Exercise exercise = workout.getExercises().get(i);

                    System.out.println("  " + (i + 1) + ". " + exercise
                        .getName());
                }
            }

            System.out.println();
            System.out.println("1. Add exercise");
            System.out.println("2. Remove exercise");
            System.out.println("3. Finish workout");
            System.out.println("4. Cancel workout");

            int choice = readInt("Select an option: ");

            switch (choice) {

                case 1:
                    addExerciseToWorkout(workout);
                    break;

                case 2:
                    removeExerciseFromWorkout(workout);
                    break;

                case 3:

                    if (workout.getExercises().isEmpty()) {
                        System.out.println("A workout must contain at least "
                            + "one exercise.");
                        pause();
                    }
                    else {
                        currentWeekWorkouts.add(workout);

                        System.out.println("Workout added to Week "
                            + currentWeek + ".");
                        pause();

                        addingExercises = false;
                    }

                    break;

                case 4:
                    System.out.println("Workout cancelled.");
                    pause();
                    addingExercises = false;
                    break;

                default:
                    System.out.println("Invalid option.");
                    pause();
            }
        }
    }


    /**
     * Adds an exercise from the database to a workout.
     */
    private static void addExerciseToWorkout(Workout workout) {

        Exercise selected = selectExercise();

        if (selected == null) {
            return;
        }

        /*
         * Create a new Exercise instead of putting the database object
         * directly into the workout.
         *
         * This is important because the database represents exercise
         * templates, while the workout contains the user's actual sets.
         */
        Exercise workoutExercise = new Exercise(selected.getName());

        for (String muscleGroup : selected.getMuscleGroup()) {

            workoutExercise.addMuscleGroup(muscleGroup);
        }

        addSetsToExercise(workoutExercise);

        try {
            workout.addExercise(workoutExercise);

            System.out.println(workoutExercise.getName()
                + " added to workout.");

        }
        catch (IllegalArgumentException e) {
            System.out.println("Could not add exercise: " + e.getMessage());
        }

        pause();
    }


    /**
     * Allows the user to select an exercise from the database.
     */
    private static Exercise selectExercise() {

        if (exerciseDatabase.isEmpty()) {
            System.out.println("No exercises available.");
            pause();
            return null;
        }

        int page = 0;
        final int pageSize = 9;

        while (true) {

            clearScreen();

            int start = page * pageSize;
            int end = Math.min(start + pageSize, exerciseDatabase.size());

            int totalPages = (exerciseDatabase.size() + pageSize - 1)
                / pageSize;

            System.out.println("======================================");
            System.out.println("          SELECT EXERCISE              ");
            System.out.println("Page " + (page + 1) + " of " + totalPages);
            System.out.println("======================================");

            for (int i = start; i < end; i++) {

                Exercise exercise = exerciseDatabase.get(i);

                System.out.println((i - start + 1) + ". " + exercise.getName());

                System.out.println("   " + formatMuscleGroups(exercise
                    .getMuscleGroup()));
            }

            System.out.println();
            System.out.println("n. Next page");
            System.out.println("p. Previous page");
            System.out.println("q. Cancel");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("n")) {

                if (page < totalPages - 1) {
                    page++;
                }

            }
            else if (input.equalsIgnoreCase("p")) {

                if (page > 0) {
                    page--;
                }

            }
            else if (input.equalsIgnoreCase("q")) {

                return null;

            }
            else {

                try {

                    int selection = Integer.parseInt(input);

                    if (selection >= 1 && selection <= end - start) {

                        return exerciseDatabase.get(start + selection - 1);

                    }
                    else {
                        System.out.println("Invalid selection.");
                        pause();
                    }

                }
                catch (NumberFormatException e) {

                    System.out.println("Invalid input.");
                    pause();
                }
            }
        }
    }


/*
 * // ----------------------------------------------------------
 * /**
 * Allows the user to create their own exercises they can later add
 * 
 * @param name
 * 
 * @param muscles
 * 
 * @param sets
 */
    private static Exercise createAnExercise(
        String name,
        ArrayList<String> muscles,
        Set sets) {
        Exercise newE = new Exercise(name);
        newE.addSet(sets);
        for (int i = 0; i < muscles.size(); i++) {
            newE.addMuscleGroup(muscles.get(i));
        }
        return newE;
    }


    private static void addCustomExercise() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("          ADD CUSTOM EXERCISE");
        System.out.println("======================================");

        System.out.print("Enter exercise name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Exercise name cannot be empty.");
            pause();
            return;
        }

        ArrayList<String> muscles = new ArrayList<>();

        boolean addingMuscles = true;

        while (addingMuscles) {

            System.out.println();
            System.out.println("Current muscle groups: " + formatMuscleGroups(
                muscles));

            System.out.println("1. Add muscle group");
            System.out.println("2. Finish");

            int choice = readInt("Select an option: ");

            switch (choice) {

                case 1:
                    System.out.print("Enter muscle group: ");
                    String muscle = scanner.nextLine().trim();

                    if (muscle.isEmpty()) {
                        System.out.println("Muscle group cannot be empty.");
                    }
                    else {
                        muscles.add(muscle);
                        System.out.println("Muscle group added.");
                    }
                    break;

                case 2:
                    if (muscles.isEmpty()) {
                        System.out.println(
                            "An exercise must have at least one muscle group.");
                    }
                    else {
                        addingMuscles = false;
                    }
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        int reps = readInt("Enter repetitions for the first set (1-99): ");
        double weight = readDouble("Enter weight for the first set: ");

        try {

            Set set = new Set(reps, weight);

            Exercise newExercise = createAnExercise(name, muscles, set);

            exerciseDatabase.add(newExercise);

            System.out.println();
            System.out.println("Custom exercise created!");
            System.out.println("Name: " + newExercise.getName());
            System.out.println("Muscles: " + formatMuscleGroups(newExercise
                .getMuscleGroup()));

            System.out.println();
            System.out.println("It has been added to your exercise database.");

        }
        catch (IllegalArgumentException e) {
            System.out.println("Could not create exercise: " + e.getMessage());
        }

        pause();
    }
/*
 * // ----------------------------------------------------------
 * /**
 * Used to check improvement, one of reach goals
 */


    private static void checkImprovement() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("          WORKOUT PROGRESS REPORT");
        System.out.println("======================================");

        ArrayList<Workout> previousWeek = workoutHistory.get(currentWeek - 1);

        ArrayList<Workout> twoWeeksAgo = workoutHistory.get(currentWeek - 2);

        if (previousWeek == null || twoWeeksAgo == null) {
            System.out.println();
            System.out.println(
                "Not enough workout history to generate a progress report.");
            System.out.println(
                "You need completed workouts from the previous two weeks.");
            return;
        }

        boolean foundExercise = false;

        for (Workout workout : previousWeek) {

            for (Exercise exercise : workout.getExercises()) {

                String exerciseName = exercise.getName();

                Exercise oldExercise = findExercise(twoWeeksAgo, exerciseName);

                if (oldExercise == null) {
                    continue;
                }

                foundExercise = true;

                double oldWeight = getBestWeight(oldExercise);
                double newWeight = getBestWeight(exercise);

                int oldReps = getBestReps(oldExercise);
                int newReps = getBestReps(exercise);

                System.out.println();
                System.out.println("--------------------------------------");
                System.out.println(exerciseName);
                System.out.println("--------------------------------------");

                System.out.println("Previous week: " + oldWeight + " lbs, "
                    + oldReps + " reps");

                System.out.println("Most recent week: " + newWeight + " lbs, "
                    + newReps + " reps");

                boolean improved = false;

                if (newWeight > oldWeight) {
                    System.out.println("Weight improved by " + (newWeight
                        - oldWeight) + " lbs.");
                    improved = true;
                }

                if (newReps > oldReps) {
                    System.out.println("Reps improved by " + (newReps - oldReps)
                        + ".");
                    improved = true;
                }

                if (!improved) {
                    System.out.println("No improvement recorded.");
                }
            }
        }

        if (!foundExercise) {
            System.out.println();
            System.out.println("No exercises were consistently logged across "
                + "the previous two weeks.");
        }
    }


    private static Exercise findExercise(
        ArrayList<Workout> workouts,
        String exerciseName) {

        for (Workout workout : workouts) {

            for (Exercise exercise : workout.getExercises()) {

                if (exercise.getName().equalsIgnoreCase(exerciseName)) {
                    return exercise;
                }
            }
        }

        return null;
    }


    private static double getBestWeight(Exercise exercise) {

        double bestWeight = 0;

        for (Set set : exercise.getSets()) {

            if (set.getWeight() > bestWeight) {
                bestWeight = set.getWeight();
            }
        }
        return bestWeight;
    }


    private static int getBestReps(Exercise exercise) {

        int bestReps = 0;

        for (Set set : exercise.getSets()) {

            if (set.getReps() > bestReps) {
                bestReps = set.getReps();
            }
        }

        return bestReps;
    }


    /**
     * Adds sets to an exercise.
     */
    private static void addSetsToExercise(Exercise exercise) {

        boolean addingSets = true;

        while (addingSets) {

            clearScreen();

            System.out.println("======================================");
            System.out.println("Adding sets to: " + exercise.getName());
            System.out.println("======================================");

            System.out.println("Current sets: " + exercise.getSets().size());

            System.out.println();
            System.out.println("1. Add set");
            System.out.println("2. Finish adding sets");

            int choice = readInt("Select an option: ");

            switch (choice) {

                case 1:

                    int reps = readInt("Enter repetitions (1-99): ");

                    double weight = readDouble("Enter weight: ");

                    try {

                        Set set = new Set(reps, weight);

                        exercise.addSet(set);

                        System.out.println("Set added: " + reps + " reps @ "
                            + weight);

                    }
                    catch (IllegalArgumentException e) {

                        System.out.println("Invalid set: " + e.getMessage());
                    }

                    pause();
                    break;

                case 2:

                    if (exercise.getSets().isEmpty()) {

                        System.out.println("An exercise must have at least "
                            + "one set.");

                        pause();

                    }
                    else {
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
     */
    private static void removeExerciseFromWorkout(Workout workout) {

        if (workout.getExercises().isEmpty()) {

            System.out.println("There are no exercises to remove.");
            pause();
            return;
        }

        System.out.println("Select an exercise to remove:");

        for (int i = 0; i < workout.getExercises().size(); i++) {

            System.out.println((i + 1) + ". " + workout.getExercises().get(i)
                .getName());
        }

        int choice = readInt("Selection: ");

        if (choice >= 1 && choice <= workout.getExercises().size()) {

            Exercise exercise = workout.getExercises().get(choice - 1);

            workout.removeExercise(exercise);

            System.out.println(exercise.getName() + " removed.");

        }
        else {
            System.out.println("Invalid selection.");
        }

        pause();
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
    // RECOMMENDATIONS
    // ============================================================


    /**
     * Recommends exercises based on muscle groups that have
     * received less work during the current week.
     */
    private static void recommendExercises() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("        EXERCISE RECOMMENDATIONS       ");
        System.out.println("======================================");

        ArrayList<String> allMuscles = getAllMuscleGroups();

        if (allMuscles.isEmpty()) {

            System.out.println("No muscle-group data available.");
            pause();
            return;
        }

        Map<String, Integer> muscleCounts = new HashMap<>();

        for (String muscle : allMuscles) {
            muscleCounts.put(muscle, 0);
        }

        /*
         * Count muscle-group occurrences in completed workouts
         * for the current week.
         */
        ArrayList<Workout> completed = workoutHistory.get(currentWeek);

        if (completed != null) {

            for (Workout workout : completed) {

                for (Exercise exercise : workout.getExercises()) {

                    for (String muscle : exercise.getMuscleGroup()) {

                        String normalized = muscle.toLowerCase();

                        muscleCounts.put(normalized, muscleCounts.getOrDefault(
                            normalized, 0) + 1);
                    }
                }
            }
        }

        int lowestCount = Integer.MAX_VALUE;

        for (int count : muscleCounts.values()) {

            if (count < lowestCount) {
                lowestCount = count;
            }
        }

        System.out.println("Muscle groups with the least recorded work:");

        ArrayList<String> underusedMuscles = new ArrayList<>();

        for (String muscle : muscleCounts.keySet()) {

            if (muscleCounts.get(muscle) == lowestCount) {

                underusedMuscles.add(muscle);
            }
        }

        for (String muscle : underusedMuscles) {

            System.out.println("- " + muscle + " (" + muscleCounts.get(muscle)
                + " exercises)");

        }

        System.out.println();
        System.out.println("Recommended exercises:");

        int recommendationCount = 0;

        for (Exercise exercise : exerciseDatabase) {

            boolean matches = false;

            for (String muscle : exercise.getMuscleGroup()) {

                if (underusedMuscles.contains(muscle.toLowerCase())) {

                    matches = true;
                    break;
                }
            }

            if (matches) {

                System.out.println("- " + exercise.getName() + " | "
                    + formatMuscleGroups(exercise.getMuscleGroup()));

                recommendationCount++;

                if (recommendationCount >= 10) {
                    break;
                }
            }
        }

        if (recommendationCount == 0) {

            System.out.println("No recommendations found.");
        }

        pause();
    }


    /**
     * Gets every muscle group appearing in the database.
     */
    private static ArrayList<String> getAllMuscleGroups() {

        ArrayList<String> muscles = new ArrayList<>();

        for (Exercise exercise : exerciseDatabase) {

            for (String muscle : exercise.getMuscleGroup()) {

                String normalized = muscle.toLowerCase();

                if (!muscles.contains(normalized)) {
                    muscles.add(normalized);
                }
            }
        }

        return muscles;
    }

    // ============================================================
    // HISTORY
    // ============================================================


    private static void viewHistory() {

        clearScreen();

        System.out.println("======================================");
        System.out.println("          WORKOUT HISTORY              ");
        System.out.println("======================================");

        if (workoutHistory.isEmpty()) {

            System.out.println("No completed workouts yet.");
            pause();
            return;
        }

        for (int week = 1; week <= currentWeek; week++) {

            ArrayList<Workout> workouts = workoutHistory.get(week);

            if (workouts == null || workouts.isEmpty()) {
                continue;
            }

            System.out.println();
            System.out.println("Week " + week + ":");

            for (Workout workout : workouts) {

                System.out.println("  - " + workout.getName());

                for (Exercise exercise : workout.getExercises()) {

                    System.out.println("      " + exercise.getName() + ": "
                        + exercise.getSets().size() + " sets");
                }
            }
        }

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
