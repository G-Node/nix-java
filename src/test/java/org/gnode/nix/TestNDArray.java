package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestNDArray {

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testBasic() {

        NDArray A = new NDArray(new int[]{5, 5}, DataType.Int32);

        int values = 0;
        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                A.setIntData(new int[]{i, j}, values++);

        values = 0;
        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                assertTrue(A.getIntData(new int[]{i, j}) == values++);

        assertTrue(A.getIntData(new int[]{1, 0}) == 5);
        assertTrue(A.getIntData(new int[]{3, 3}) == 18);
        assertTrue(A.getIntData(new int[]{4, 4}) == 24);

        int[] dims = {3, 4, 5};
        NDArray B = new NDArray(dims, DataType.Double);
        values = 0;
        for (int i = 0; i != dims[0]; ++i)
            for (int j = 0; j != dims[1]; ++j)
                for (int k = 0; k != dims[2]; ++k)
                    B.setDoubleData(new int[]{i, j, k}, values++);

        assertTrue(B.getDoubleData(new int[]{1, 0, 3}) == 23);
        assertTrue(B.getDoubleData(new int[]{2, 0, 2}) == 42);
        assertTrue(B.getDoubleData(new int[]{1, 1, 1}) == 26);
    }
}