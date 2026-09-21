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
public class Exercise{
    private String name;
    private ArrayList<String> muscles;
    private ArrayList<Set> sets;

    public Exercise(String name) {
        
        this.name = name;
        
        muscles = new ArrayList<String>();
        
        sets = new ArrayList<Set>();

    }


    public ArrayList<String> getMuscleGroup() {
        return muscles;
    }


    public String getName() {
        return name;
    }


    public ArrayList<Set> getSets() {
        return sets;
    }


    public ArrayList<String> addMuscleGroup(String muscle) {
        if(muscle == null) {
            throw new IllegalArgumentException();
        }
        muscles.add(muscle);
        return muscles;
    }


    public ArrayList<String> removeMuscleGroup(String muscle) {
        if(!muscles.contains(muscle)) {
            throw new NoSuchElementException();
        }
        muscles.remove(muscle);
        return muscles;
    }


    public void setName(String name) {
        if(name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }


    public void addSet(Set set) {
        
        this.sets.add(set);
    }


    public void removeSets(Set set) {
        if(!sets.contains(set)) {
            throw new NoSuchElementException();
        }
        sets.remove(set);
    }
}
