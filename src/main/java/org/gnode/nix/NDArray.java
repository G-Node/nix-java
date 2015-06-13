package org.gnode.nix;

public class NDArray {

    private byte[] byteData;
    private short[] shortData;
    private int[] intData;
    private long[] longData;
    private float[] floatData;
    private double[] doubleData;

    private static final String strByte = "byte";
    private static final String strShort = "short";
    private static final String strInt = "int";
    private static final String strLong = "long";
    private static final String strFloat = "float";
    private static final String strDouble = "double";

    // number of dimensions
    private int rank;

    // effective number of elements in 1-D array
    private int size;

    // shape of ND-Array
    private int[] shape;

    // strides
    private int[] stride;

    // check if data array is initialized
    private boolean isDataInitialized;

    // offset
    private int offset = 0;

    //--------------------------------------------------
    // Base constructor.
    //--------------------------------------------------

    /**
     * Constructor. Set shape for ND-Array
     *
     * @param shape dimensions
     */
    public NDArray(int[] shape) {
        this.rank = shape.length;

        // copy dimensions
        this.shape = new int[rank];
        System.arraycopy(shape, 0, this.shape, 0, rank);

        // calculate strides
        this.stride = new int[rank];
        int st = 1;
        for (int j = rank - 1; j >= 0; j--) {
            stride[j] = st;
            st *= shape[j];
        }

        // effective size of 1-D array
        this.size = calcArrayProduct(shape);

        // set initialized to false
        this.isDataInitialized = false;
    }

    //--------------------------------------------------
    // Attribute getters
    //--------------------------------------------------

    /**
     * Return dimensions array.
     *
     * @return shape
     */
    public int[] getShape() {
        return shape;
    }

    /**
     * Number of dimensions.
     *
     * @return rank
     */
    public int getRank() {
        return rank;
    }
    //--------------------------------------------------
    // Constructors. Initialize with data.
    //--------------------------------------------------

    /**
     * Constructor. Initialize array with byte data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(byte[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.byteData = new byte[this.size];
        System.arraycopy(data, 0, this.byteData, 0, this.size);

        this.isDataInitialized = true;
    }

    /**
     * Constructor. Initialize array with short data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(short[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.shortData = new short[this.size];
        System.arraycopy(data, 0, this.shortData, 0, this.size);

        this.isDataInitialized = true;
    }

    /**
     * Constructor. Initialize array with int data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(int[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.intData = new int[this.size];
        System.arraycopy(data, 0, this.intData, 0, this.size);

        this.isDataInitialized = true;
    }

    /**
     * Constructor. Initialize array with long data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(long[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.longData = new long[this.size];
        System.arraycopy(data, 0, this.longData, 0, this.size);

        this.isDataInitialized = true;
    }

    /**
     * Constructor. Initialize array with float data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(float[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.floatData = new float[this.size];
        System.arraycopy(data, 0, this.floatData, 0, this.size);

        this.isDataInitialized = true;
    }

    /**
     * Constructor. Initialize array with double data.
     *
     * @param data  data
     * @param shape dimensions
     */
    public NDArray(double[] data, int[] shape) {
        this(shape);

        validateSize(data.length);

        this.doubleData = new double[this.size];
        System.arraycopy(data, 0, this.doubleData, 0, this.size);

        this.isDataInitialized = true;
    }

    //--------------------------------------------------
    // Getter functions
    //--------------------------------------------------

    /**
     * Set byte value at a position is initialized as a byte array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setByteData(int[] indexes, byte value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.byteData = new byte[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.byteData, strByte);
        }

        this.byteData[calcEffectiveIndex(indexes)] = value;
    }

    /**
     * Set short value at a position is initialized as a short array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setShortData(int[] indexes, short value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.shortData = new short[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.shortData, strShort);
        }

        this.shortData[calcEffectiveIndex(indexes)] = value;
    }

    /**
     * Set int value at a position is initialized as a int array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setIntData(int[] indexes, int value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.intData = new int[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.intData, strInt);
        }

        this.intData[calcEffectiveIndex(indexes)] = value;
    }

    /**
     * Set long value at a position is initialized as a long array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setLongData(int[] indexes, long value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.longData = new long[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.longData, strLong);
        }

        this.longData[calcEffectiveIndex(indexes)] = value;
    }

    /**
     * Set float value at a position is initialized as a float array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setFloatData(int[] indexes, float value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.floatData = new float[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.floatData, strFloat);
        }

        this.floatData[calcEffectiveIndex(indexes)] = value;
    }

    /**
     * Set double value at a position is initialized as a double array.
     *
     * @param indexes position
     * @param value   data
     */
    public void setDoubleData(int[] indexes, double value) {
        validateIndexes(indexes.length);

        if (!this.isDataInitialized) {
            this.doubleData = new double[this.size];
            this.isDataInitialized = true;
        } else {
            validateNotNull(this.doubleData, strDouble);
        }

        this.doubleData[calcEffectiveIndex(indexes)] = value;
    }

    //--------------------------------------------------
    // Getter functions
    //--------------------------------------------------

    /**
     * Get byte data at a position if initialized as byte array.
     *
     * @param indexes position
     * @return data
     */
    public byte getByteData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.byteData, strByte);

        return this.byteData[calcEffectiveIndex(indexes)];
    }

    /**
     * Get short data at a position if initialized as short array.
     *
     * @param indexes position
     * @return data
     */
    public short getShortData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.shortData, strShort);

        return this.shortData[calcEffectiveIndex(indexes)];
    }

    /**
     * Get int data at a position if initialized as int array.
     *
     * @param indexes position
     * @return data
     */
    public int getIntData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.intData, strInt);

        return this.intData[calcEffectiveIndex(indexes)];
    }

    /**
     * Get long data at a position if initialized as long array.
     *
     * @param indexes position
     * @return data
     */
    public long getLongData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.longData, strLong);

        return this.longData[calcEffectiveIndex(indexes)];
    }

    /**
     * Get float data at a position if initialized as float array.
     *
     * @param indexes position
     * @return data
     */
    public float getFloatData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.floatData, strFloat);

        return this.floatData[calcEffectiveIndex(indexes)];
    }

    /**
     * Get double data at a position if initialized as double array.
     *
     * @param indexes position
     * @return data
     */
    public double getDoubleData(int[] indexes) {
        validateIndexes(indexes.length);
        validateNotNull(this.doubleData, strDouble);

        return this.doubleData[calcEffectiveIndex(indexes)];
    }

    //--------------------------------------------------
    // Functions to retrieve underlying 1-D array
    //--------------------------------------------------

    /**
     * Get data as byte array if initialized as byte array.
     *
     * @return data array.
     */
    public byte[] getByteDataArray() {
        validateNotNull(this.byteData, strByte);
        return this.byteData;
    }

    /**
     * Get data as short array if initialized as short array.
     *
     * @return data array.
     */
    public short[] getShortDataArray() {
        validateNotNull(this.shortData, strShort);
        return this.shortData;
    }


    /**
     * Get data as int array if initialized as int array.
     *
     * @return data array.
     */
    public int[] getIntDataArray() {
        validateNotNull(this.intData, strInt);
        return this.intData;
    }

    /**
     * Get data as long array if initialized as long array.
     *
     * @return data array.
     */
    public long[] getLongDataArray() {
        validateNotNull(this.longData, strLong);
        return this.longData;
    }

    /**
     * Get data as float array if initialized as float array.
     *
     * @return data array.
     */
    public float[] getFloatDataArray() {
        validateNotNull(this.floatData, strFloat);
        return this.floatData;
    }

    /**
     * Get data as double array if initialized as double array.
     *
     * @return data array.
     */
    public double[] getDoubleDataArray() {
        validateNotNull(this.doubleData, strDouble);
        return this.doubleData;
    }


    //--------------------------------------------------
    // Utility functions
    //--------------------------------------------------

    /**
     * Calculates effective position in 1-D array
     *
     * @param indexes
     * @return
     */
    private int calcEffectiveIndex(int[] indexes) {
        int ind = offset;
        for (int i = 0; i < rank; i++) {
            ind += indexes[i] * stride[i];
        }
        return ind;
    }


    /**
     * Calculates array product. Used to determine total elements in 1-D array
     *
     * @param arr shape
     * @return product
     */
    private static int calcArrayProduct(int[] arr) {
        int product = 1;
        for (int x : arr) {
            product *= x;
        }
        return product;
    }

    /**
     * To validate size of data array
     *
     * @param size size
     */
    private void validateSize(int size) {
        if (this.size != size) {
            throw new IllegalArgumentException("Error : array size does not match dimensions");
        }
    }

    /**
     * To check indexes are valid
     *
     * @param rank rank
     */
    private void validateIndexes(int rank) {
        if (this.rank != rank) {
            throw new IllegalArgumentException("Error : invalid indexes");
        }
    }

    /**
     * To check if array is not null
     *
     * @param obj  int array
     * @param type string type
     */
    private static void validateNotNull(Object obj, String type) {
        if (obj == null) {
            throw new NullPointerException("Error : not initialized as " + type + " array");
        }
    }
}
