import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recommendation
{
    /**
     * Recommends exercises based on muscle groups that have
     * received less work during the current week.
     *
     * @param exerciseDatabase
     *     list of available exercises
     * @param workoutHistory
     *     completed workouts organized by week
     * @param currentWeek
     *     current week number
     */
    public static void recommendExercises(
        ArrayList<Exercise> exerciseDatabase,
        Map<Integer, ArrayList<Workout>> workoutHistory,
        int currentWeek)
    {
        System.out.println("======================================");
        System.out.println("        EXERCISE RECOMMENDATIONS       ");
        System.out.println("======================================");

        ArrayList<String> allMuscles =
            getAllMuscleGroups(exerciseDatabase);

        if (allMuscles.isEmpty())
        {
            System.out.println("No muscle-group data available.");
            return;
        }

        Map<String, Integer> muscleCounts = new HashMap<>();

        for (String muscle : allMuscles)
        {
            muscleCounts.put(muscle, 0);
        }

        ArrayList<Workout> completed =
            workoutHistory.get(currentWeek);

        if (completed != null)
        {
            for (Workout workout : completed)
            {
                for (Exercise exercise : workout.getExercises())
                {
                    for (String muscle : exercise.getMuscleGroup())
                    {
                        String normalized = muscle.toLowerCase();

                        muscleCounts.put(
                            normalized,
                            muscleCounts.getOrDefault(normalized, 0) + 1);
                    }
                }
            }
        }

        int lowestCount = Integer.MAX_VALUE;

        for (int count : muscleCounts.values())
        {
            if (count < lowestCount)
            {
                lowestCount = count;
            }
        }

        System.out.println(
            "Muscle groups with the least recorded work:");

        ArrayList<String> underusedMuscles = new ArrayList<>();

        for (String muscle : muscleCounts.keySet())
        {
            if (muscleCounts.get(muscle) == lowestCount)
            {
                underusedMuscles.add(muscle);
            }
        }

        for (String muscle : underusedMuscles)
        {
            System.out.println(
                "- " + muscle + " ("
                    + muscleCounts.get(muscle)
                    + " exercises)");
        }

        System.out.println();
        System.out.println("Recommended exercises:");

        int recommendationCount = 0;

        for (Exercise exercise : exerciseDatabase)
        {
            boolean matches = false;

            for (String muscle : exercise.getMuscleGroup())
            {
                if (underusedMuscles.contains(muscle.toLowerCase()))
                {
                    matches = true;
                    break;
                }
            }

            if (matches)
            {
                System.out.println(
                    "- " + exercise.getName()
                        + " | "
                        + formatMuscleGroups(
                            exercise.getMuscleGroup()));

                recommendationCount++;

                if (recommendationCount >= 10)
                {
                    break;
                }
            }
        }

        if (recommendationCount == 0)
        {
            System.out.println("No recommendations found.");
        }
    }


    /**
     * Gets every muscle group appearing in the database.
     *
     * @param exerciseDatabase
     *     list of available exercises
     * @return list of muscle groups
     */
    private static ArrayList<String> getAllMuscleGroups(
        ArrayList<Exercise> exerciseDatabase)
    {
        ArrayList<String> muscles = new ArrayList<>();

        for (Exercise exercise : exerciseDatabase)
        {
            for (String muscle : exercise.getMuscleGroup())
            {
                String normalized = muscle.toLowerCase();

                if (!muscles.contains(normalized))
                {
                    muscles.add(normalized);
                }
            }
        }

        return muscles;
    }


    /**
     * Formats muscle groups into a readable String.
     *
     * @param muscleGroups
     *     muscle groups to format
     * @return formatted muscle groups
     */
    private static String formatMuscleGroups(
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