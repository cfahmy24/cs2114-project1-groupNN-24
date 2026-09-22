import java.util.ArrayList;
import java.util.Map;


public class WorkoutHistory
{
    /**
     * Used to check improvement, one of reach goals.
     *
     * @param workoutHistory
     *     completed workouts organized by week
     * @param currentWeek
     *     current week number
     */
    public static void checkImprovement(
        Map<Integer, ArrayList<Workout>> workoutHistory,
        int currentWeek)
    {
        ArrayList<Workout> previousWeek =
            workoutHistory.get(currentWeek - 1);

        ArrayList<Workout> twoWeeksAgo =
            workoutHistory.get(currentWeek - 2);

        if (previousWeek == null || twoWeeksAgo == null)
        {
            System.out.println();
            System.out.println(
                "Not enough workout history to generate a progress report.");
            System.out.println(
                "You need completed workouts from the previous two weeks.");
            return;
        }

        boolean foundExercise = false;

        for (Workout workout : previousWeek)
        {
            for (Exercise exercise : workout.getExercises())
            {
                String exerciseName = exercise.getName();

                Exercise oldExercise =
                    findExercise(twoWeeksAgo, exerciseName);

                if (oldExercise == null)
                {
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

                System.out.println(
                    "Previous week: " + oldWeight + " lbs, "
                        + oldReps + " reps");

                System.out.println(
                    "Most recent week: " + newWeight + " lbs, "
                        + newReps + " reps");

                boolean improved = false;

                if (newWeight > oldWeight)
                {
                    System.out.println(
                        "Weight improved by "
                            + (newWeight - oldWeight) + " lbs.");
                    improved = true;
                }

                if (newReps > oldReps)
                {
                    System.out.println(
                        "Reps improved by "
                            + (newReps - oldReps) + ".");
                    improved = true;
                }

                if (!improved)
                {
                    System.out.println("No improvement recorded.");
                }
            }
        }

        if (!foundExercise)
        {
            System.out.println();
            System.out.println(
                "No exercises were consistently logged across "
                    + "the previous two weeks.");
        }
    }


    private static Exercise findExercise(
        ArrayList<Workout> workouts,
        String exerciseName)
    {
        for (Workout workout : workouts)
        {
            for (Exercise exercise : workout.getExercises())
            {
                if (exercise.getName().equalsIgnoreCase(exerciseName))
                {
                    return exercise;
                }
            }
        }

        return null;
    }


    private static double getBestWeight(Exercise exercise)
    {
        double bestWeight = 0;

        for (Set set : exercise.getSets())
        {
            if (set.getWeight() > bestWeight)
            {
                bestWeight = set.getWeight();
            }
        }

        return bestWeight;
    }


    private static int getBestReps(Exercise exercise)
    {
        int bestReps = 0;

        for (Set set : exercise.getSets())
        {
            if (set.getReps() > bestReps)
            {
                bestReps = set.getReps();
            }
        }

        return bestReps;
    }


    /**
     * Displays completed workout history.
     *
     * @param workoutHistory
     *     completed workouts organized by week
     * @param currentWeek
     *     current week number
     */
    public static void viewHistory(
        Map<Integer, ArrayList<Workout>> workoutHistory,
        int currentWeek)
    {
        System.out.println("======================================");
        System.out.println("          WORKOUT HISTORY              ");
        System.out.println("======================================");

        if (workoutHistory.isEmpty())
        {
            System.out.println("No completed workouts yet.");
            return;
        }

        for (int week = 1; week <= currentWeek; week++)
        {
            ArrayList<Workout> workouts = workoutHistory.get(week);

            if (workouts == null || workouts.isEmpty())
            {
                continue;
            }

            System.out.println();
            System.out.println("Week " + week + ":");

            for (Workout workout : workouts)
            {
                System.out.println("  - " + workout.getName());

                for (Exercise exercise : workout.getExercises())
                {
                    System.out.println(
                        "      " + exercise.getName() + ": "
                            + exercise.getSets().size() + " sets");
                }
            }
        }
    }
}