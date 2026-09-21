/**
 * Represents one set of an exercise.
 * Stores the number of repetitions and the weight used.
 *
 * @author Colin Fahmy
 * @version 2026.09.21
 */
public class Set
{
    private int reps;
    private double weight;

    /**
     * Creates a new Set object.
     *
     * @param reps
     *   the number of repetitions
     * @param weight
     *    the weight used
     */
    public Set(int reps, double weight)
    {
        setReps(reps);
        setWeight(weight);
    }
    /**
     * Returns the number of repetitions.
     *
     * @return the number of reps
     */
    public int getReps()
    {
        return reps;
    }
    /**
     * Returns the weight used.
     *
     * @return the weight
     */
    public double getWeight()
    {
        return weight;
    }
    /**
     * Sets the number of repetitions.
     *
     * @param reps
     *    the number of repetitions
     */
    public void setReps(int reps)
    {
        if (reps < 1 || reps > 99)
        {
            throw new IllegalArgumentException();
        }
        this.reps = reps;
    }
    /**
     * Sets the weight used.
     *
     * @param weight
     *   the weight used
     */
    public void setWeight(double weight)
    {
        if (weight < 0)
        {
            throw new IllegalArgumentException();
        }

        this.weight = weight;
    }
}