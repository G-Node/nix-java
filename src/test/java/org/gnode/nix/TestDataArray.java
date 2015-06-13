package org.gnode.nix;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class TestDataArray {

    private File file;
    private Block block;
    private DataArray array1, array2;

    private Date statup_time;

    @Before
    public void setUp() {
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        statup_time = new Date((System.currentTimeMillis() / 1000) * 1000);

        file = File.open("test_DataArray.h5", FileMode.Overwrite);

        block = file.createBlock("block_one", "dataset");
        array1 = block.createDataArray("array_one",
                "testdata",
                DataType.Double,
                new NDSize(new int[]{0, 0, 0}));
        array2 = block.createDataArray("random",
                "double",
                DataType.Double,
                new NDSize(new int[]{20, 20}));
    }

    @After
    public void tearDown() {
        file.close();
    }

    @Test
    public void testId() {
        assertEquals(array1.getId().length(), 36);
    }

    @Test
    public void testName() {
        assertEquals(array1.getName(), "array_one");
    }

    @Test
    public void testType() {
        assertEquals(array1.getType(), "testdata");
    }

    @Test
    public void testDefinition() {
        assertNull(array1.getDefinition());
    }

    @Test
    public void testLabel() {
        String testStr = "somestring";

        array1.setLabel(testStr);
        assertEquals(array1.getLabel(), testStr);
        array1.setLabel(null);
        assertNull(array1.getLabel());
    }

    @Test
    public void testUnit() {
        String testStr = "somestring";
        String validUnit = "mV^2";

        try {
            array1.setUnit(testStr);
            fail();
        } catch (RuntimeException re) {
        }

        try {
            array1.setUnit(validUnit);
        } catch (Exception e) {
            fail();
        }

        assertEquals(array1.getUnit(), validUnit);


        try {
            array1.setUnit(null);
        } catch (Exception e) {
            fail();
        }

        assertNull(array1.getUnit());
    }

    @Test
    public void testData() {

        NDArray A = new NDArray(new int[]{3, 4, 2}, DataType.Double);

        int values = 0;
        for (int i = 0; i != 3; ++i)
            for (int j = 0; j != 4; ++j)
                for (int k = 0; k != 2; ++k)
                    A.setDoubleData(new int[]{i, j, k}, values++);

        assertEquals(array1.getDataType(), DataType.Double);

        assertTrue(array1.getDataExtent().equals(new NDSize(new int[]{0, 0, 0})));
        assertNull(array1.getDimension(1));

        array1.setData(A);

        NDArray B = array1.getData();

        int verify = 0;
        int errors = 0;
        for (int i = 0; i != 3; ++i) {
            for (int j = 0; j != 4; ++j) {
                for (int k = 0; k != 2; ++k) {
                    int v = verify++;
                    if (B.getDoubleData(new int[]{i, j, k}) != v) {
                        errors += 1;
                    }
                }
            }
        }

        assertEquals(errors, 0);

        DataArray direct = block.createDataArray("da_direct", "double", A);
        NDArray Adirect = direct.getData();

        errors = 0;
        verify = 0;
        for (int i = 0; i != 3; ++i) {
            for (int j = 0; j != 4; ++j) {
                for (int k = 0; k != 2; ++k) {
                    int v = verify++;
                    if (Adirect.getDoubleData(new int[]{i, j, k}) != v) {
                        errors += 1;
                    }
                }
            }
        }

        assertEquals(errors, 0);

        DataArray directFloat = block.createDataArray("da_direct_int", "int", A, A.getDataType());
        NDArray AdirectFloat = directFloat.getData();

        errors = 0;
        verify = 0;
        for (int i = 0; i != 3; ++i) {
            for (int j = 0; j != 4; ++j) {
                for (int k = 0; k != 2; ++k) {
                    int v = verify++;
                    if (Adirect.getDoubleData(new int[]{i, j, k}) != v) {
                        errors += 1;
                    }
                }
            }
        }

        assertEquals(errors, 0);

        NDArray C = new NDArray(new int[]{5, 5}, DataType.Double);

        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                C.setDoubleData(new int[]{i, j}, 42.0);

        assertTrue(array2.getDataExtent().equals(new NDSize(new int[]{20, 20})));

        array2.setData(C);
        array2.setDataExtent(new NDSize(new int[]{40, 40}));
        assertTrue(array2.getDataExtent().equals(new NDSize(new int[]{40, 40})));

        NDArray D = new NDArray(new int[]{5, 5}, DataType.Double);
        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                D.setDoubleData(new int[]{i, j}, 42.0);

        array2.setData(D);

        NDArray E = array2.getData();

        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                assertTrue(D.getDoubleData(new int[]{i, j}) == E.getDoubleData(new int[]{i, j}));

        NDArray F = array2.getData();

        for (int i = 0; i != 5; ++i)
            for (int j = 0; j != 5; ++j)
                assertTrue(D.getDoubleData(new int[]{i, j}) == F.getDoubleData(new int[]{i, j}));

        DataArray da3 = block.createDataArray("direct-vector",
                "double",
                DataType.Double,
                new NDSize(new int[]{5}));

        assertTrue(da3.getDataExtent().equals(new NDSize(new int[]{5})));
        assertNull(da3.getDimension(1));

        NDArray dv = new NDArray(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new int[]{5});
        da3.setData(dv);

        NDArray dvin = da3.getData();

        for (int i = 0; i < 5; i++) {
            assertTrue(dv.getDoubleData(new int[]{i}) == dvin.getDoubleData(new int[]{i}));
        }
    }

    @Test
    public void testPolynomial() {
        double[] coefficients1 = new double[10];
        double[] coefficients2 = new double[10];

        for (int i = 0; i < 10; i++) {
            coefficients1[i] = i;
            coefficients2[i] = i;
        }

        array2.setPolynomCoefficients(coefficients2);
        List<Double> ret = array2.getPolynomCoefficients();
        for (int i = 0; i < ret.size(); i++) {
            assertTrue(ret.get(i) == coefficients2[i]);
        }

        array2.setPolynomCoefficients(null);
        assertTrue(array2.getPolynomCoefficients().size() == 0);

        array2.setExpansionOrigin(3);
        double retval = array2.getExpansionOrigin();
        assertTrue(retval == 3);
        array2.setExpansionOrigin(0.0);
        assertTrue(array2.getExpansionOrigin() == 0.0);

        //test IO with a polynomial set
        DataArray dap = block.createDataArray("polyio",
                "double",
                DataType.Double,
                new NDSize(new int[]{2, 3}));

        NDArray dv = new NDArray(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, new int[]{2, 3});
        dap.setData(dv);

        double[] poly = {1.0, 2.0, 3.0};
        dap.setPolynomCoefficients(poly);

        NDArray dvin_poly = dap.getData();

        double[] ref = {6, 17, 34, 57, 86, 121};
        double[] dvin_poly_arr = dvin_poly.getDoubleDataArray();

        assertEquals(ref.length, dvin_poly_arr.length);
    }

    @Test
    public void testDimension() {
        List<Dimension> dims = new ArrayList<Dimension>();
        double[] ticks = new double[5];
        double samplingInterval = Math.PI;

        for (int i = 0; i < 5; i++) {
            ticks[i] = Math.PI;
        }

        try {
            array2.appendRangeDimension(new double[]{});
            fail();
        } catch (RuntimeException re) {
        }

        try {
            array2.createRangeDimension(1, new double[]{});
            fail();
        } catch (RuntimeException re) {
        }

        dims.add(array2.createSampledDimension(1, samplingInterval));
        dims.add(array2.createSetDimension(2));
        dims.add(array2.createRangeDimension(3, ticks));
        dims.add(array2.appendSampledDimension(samplingInterval));
        dims.add(array2.appendSetDimension());
        dims.set(3, array2.createRangeDimension(4, ticks));

        // have some explicit dimension types
        RangeDimension dim_range = array1.appendRangeDimension(ticks);
        SampledDimension dim_sampled = array1.appendSampledDimension(samplingInterval);
        SetDimension dim_set = array1.appendSetDimension();

        assertTrue(array2.getDimension(dims.get(0).getIndex()).getDimensionType() == DimensionType.Sample);
        assertTrue(array2.getDimension(dims.get(1).getIndex()).getDimensionType() == DimensionType.Set);
        assertTrue(array2.getDimension(dims.get(2).getIndex()).getDimensionType() == DimensionType.Range);
        assertTrue(array2.getDimension(dims.get(3).getIndex()).getDimensionType() == DimensionType.Range);
        assertTrue(array2.getDimension(dims.get(4).getIndex()).getDimensionType() == DimensionType.Set);

        assertTrue(array2.getDimensionCount() == 5);

        // since deleteDimension renumbers indices to be continuous we test that too
        array2.deleteDimension(5);
        array2.deleteDimension(4);
        array2.deleteDimension(1);
        array2.deleteDimension(1);
        array2.deleteDimension(1);

        dims = array2.getDimensions();
        assertTrue(array2.getDimensionCount() == 0);
        assertTrue(dims.size() == 0);
    }

    @Test
    public void testCreatedAt() {
        assertTrue(array1.getCreatedAt().compareTo(statup_time) >= 0);
        assertTrue(array2.getCreatedAt().compareTo(statup_time) >= 0);

        long time = System.currentTimeMillis() - 10000000L * 1000;
        // precision of time_t is in seconds hence (millis / 1000) * 1000
        time = time / 1000 * 1000;

        Date past_time = new Date(time);
        array1.forceCreatedAt(past_time);
        array2.forceCreatedAt(past_time);
        assertTrue(array1.getCreatedAt().equals(past_time));
        assertTrue(array2.getCreatedAt().equals(past_time));
    }

    @Test
    public void testUpdatedAt() {
        assertTrue(array1.getUpdatedAt().compareTo(statup_time) >= 0);
        assertTrue(array2.getUpdatedAt().compareTo(statup_time) >= 0);
    }
}