package pt.isel.ls;

import org.junit.Test;
import static org.junit.Assert.*;

public class IntsTests {

    @Test
    public void max_returns_greatest(){
        assertEquals(1, Ints.max(1, -2));
        assertEquals(1, Ints.max(-2,1));
        assertEquals(-1, Ints.max(-1,-2));
        assertEquals(-1, Ints.max(-2,-1));
    }

    @Test
    public void explicitNonFailure() {
        assertEquals(6, 1+5);
    }

    @Test
    public void indexOfBinary_returns_negative_if_not_found(){
        // Arrange
        int[] v = {1,2,3};

        // Act
        int ix = Ints.indexOfBinary(v,0,3,4);

        // Assert
        assertTrue(ix < 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void indexOfBinary_throws_IllegalArgumentException_if_indexes_are_not_valid(){
        // Arrange
        int[] v = {1,2,3};

        // Act
        int ix = Ints.indexOfBinary(v, 2, 1, 4);

        // Assert
        assertTrue(ix < 0);
    }

    @Test
    public void indexOfBinary_right_bound_parameter_is_exclusive(){
        int[] v = {2,2,2};
        int ix = Ints.indexOfBinary(v, 1, 1, 2);
        assertTrue(ix < 0);
    }

    @Test
    public void indexOfBinary_and_max_test(){
        int v[] = {1,3,5,7,8,9};
        assertEquals(1, Ints.max(-1, v[0]));
        assertEquals(3, Ints.max(-1,v[1]));
        assertEquals(5, Ints.max(-1,v[2]));
        assertEquals(7, Ints.max(-1,v[3]));
        assertEquals(8, Ints.max(-1,v[4]));
        assertEquals(9, Ints.max(-1,v[5]));
        int ix = Ints.indexOfBinary(v, 1,v.length-1,7);
        assertTrue(3 == ix);
    }
}
