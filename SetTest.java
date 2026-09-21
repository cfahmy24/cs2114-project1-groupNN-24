import static org.junit.Assert.*;
import org.junit.Test;
/**
 * Tests the Set class.
 *
 * @author Colin Fahmy
 * @version 2026.09.21
 */
public class SetTest
{
    private Set set;

    /**
     * Sets up each test.
     */
    public void setUp()
    {
        set = new Set(10, 50.0);
    }

    /**
     * Tests getReps.
     */
    public void testGetReps()
    {
        assertEquals(10, set.getReps());
    }

    /**
     * Tests getWeight.
     */
    public void testGetWeight()
    {
        assertEquals(50.0, set.getWeight(), 0.01);
    }

    /**
     * Tests setting valid reps.
     */
    public void testSetReps()
    {
        set.setReps(20);
        assertEquals(20, set.getReps());
    }

    /**
     * Tests the lower reps boundary.
     */
    public void testSetRepsLowerBoundary()
    {
        set.setReps(1);
        assertEquals(1, set.getReps());
    }

    /**
     * Tests the upper reps boundary.
     */
    public void testSetRepsUpperBoundary()
    {
        set.setReps(99);
        assertEquals(99, set.getReps());
    }

    /**
     * Tests invalid reps below the allowed range.
     */
    public void testSetRepsTooLow()
    {
        Exception exception = null;

        try
        {
            set.setReps(0);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
    }

    /**
     * Tests invalid reps above the allowed range.
     */
    public void testSetRepsTooHigh()
    {
        Exception exception = null;

        try
        {
            set.setReps(100);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
    }

    /**
     * Tests setting a valid weight.
     */
    public void testSetWeight()
    {
        set.setWeight(75.5);
        assertEquals(75.5, set.getWeight(), 0.01);
    }

    /**
     * Tests zero weight for a bodyweight exercise.
     */
    public void testSetWeightZero()
    {
        set.setWeight(0.0);
        assertEquals(0.0, set.getWeight(), 0.01);
    }

    /**
     * Tests a negative weight.
     */
    public void testSetWeightNegative()
    {
        Exception exception = null;

        try
        {
            set.setWeight(-10.0);
        }
        catch (IllegalArgumentException e)
        {
            exception = e;
        }

        assertNotNull(exception);
    }
}
