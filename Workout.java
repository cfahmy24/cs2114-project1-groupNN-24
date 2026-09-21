import java.util.ArrayList;

public class Workout
{
    private String name;
    private ArrayList<Exercise> exercises;
    //~ Fields ................................................................

    //~ Constructors ..........................................................

    //~Public  Methods ........................................................
    public Workout(String name)
    {
        if (name == null || name.isEmpty())
        {
            throw new IllegalArgumentException();
        }

        this.name = name;
        exercises = new ArrayList<Exercise>();
    }
    public void addExercise(Exercise exercise)
    {
        if (exercise == null)
        {
            throw new IllegalArgumentException();
        }

        exercises.add(exercise);
    }
    public void removeExercise(Exercise exercise)
    {
        exercises.remove(exercise);
    }
    public ArrayList<Exercise> getExercises()
    {
        return exercises;
    }
    public String getName()
    {
        return name;
    }

}
