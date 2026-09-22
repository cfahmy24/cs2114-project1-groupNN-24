import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Exercise represents an exercise object with an associated muscle group.
 * Exercises can be added to workouts.
 *
 * @author alex leon
 * @version 2026.09.21
 */
public class Exercise {
    private String name;
    private ArrayList<String> muscles;
    private ArrayList<Set> sets;

    /**
     * Create an exercise object
     * 
     * @param name
     */
    public Exercise(String name) {

        this.name = name;

        muscles = new ArrayList<String>();

        sets = new ArrayList<Set>();

    }


    /**
     * Return the muscle group(s) associated with the exercise.
     * 
     * @return ArrayList muscles
     */
    public ArrayList<String> getMuscleGroup() {
        return muscles;
    }


    /**
     * Return the name of the exercise
     * 
     * @return String name
     */
    public String getName() {
        return name;
    }


    /**
     * Return the list of sets of an exercise.
     * 
     * @return ArrayList sets
     */
    public ArrayList<Set> getSets() {
        return sets;
    }


    /**
     * Adds a muscle worked to an exercise.
     * 
     * @param muscle
     *            to add
     * @return updated list of muscles
     */
    public ArrayList<String> addMuscleGroup(String muscle) {
        if (muscle == null) {
            throw new IllegalArgumentException();
        }
        muscles.add(muscle);
        return muscles;
    }


    /**
     * Removes a muscle from an exercise.
     * 
     * @param muscle
     *            to remove
     * @return updated list of muscles
     */
    public ArrayList<String> removeMuscleGroup(String muscle) {
        if (!muscles.contains(muscle)) {
            throw new NoSuchElementException();
        }
        muscles.remove(muscle);
        return muscles;
    }


    /**
     * Sets the name of an exercise
     * 
     * @param name
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }


    /**
     * Adds a set object to an exercise
     * 
     * @param set
     *            object to be added
     */
    public void addSet(Set set) {

        this.sets.add(set);
    }


    /**
     * Removes a set object from an exercise
     * 
     * @param set
     *            object to be removed
     */
    public void removeSets(Set set) {
        if (!sets.contains(set)) {
            throw new NoSuchElementException();
        }
        sets.remove(set);
    }
}
