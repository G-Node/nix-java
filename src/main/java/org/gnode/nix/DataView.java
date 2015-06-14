package org.gnode.nix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(value = "linux",
        include = {"<nix/DataView.hpp>"},
        link = {"nix"})
@Namespace("nix")
public class DataView extends Pointer {

    static {
        Loader.load();
    }

    public DataView(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset) {
        allocate(da, count, offset);
    }

    private native void allocate(@ByVal DataArray da, @ByVal NDSize count, @ByVal NDSize offset);

    // the DataIO interface implementation

    /**
     * Set data extent.
     *
     * @param extent extent
     */
    public native
    @Name("dataExtent")
    void setDataExtent(@Const @ByRef NDSize extent);

    /**
     * Data extent.
     *
     * @return extent
     */
    public native
    @Name("dataExtent")
    @ByVal
    NDSize getDataExtent();

    /**
     * Get type of data.
     *
     * @return type
     */
    public native
    @Name("dataType")
    @ByVal
    @Cast("nix::DataType")
    int getDataType();

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") byte[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") short[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") int[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") long[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") float[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void getData(@Cast("nix::DataType") int dtype,
                                @Cast("void*") double[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") byte[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") short[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") int[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") long[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") float[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    private native void setData(@Cast("nix::DataType") int dtype,
                                @Cast("const void*") double[] data,
                                @Const @ByRef NDSize count,
                                @Const @ByRef NDSize offset);

    /**
     * Write ND-Array data to data array.
     *
     * @param dataType type
     * @param ndArray  data
     * @param count    shape
     * @param offset   offset
     */
    public void setData(int dataType, NDArray ndArray, NDSize count, NDSize offset) {

        switch (dataType) {
            case DataType.Int8:
                setData(dataType, ndArray.getByteDataArray(), count, offset);
                break;

            case DataType.Int16:
                setData(dataType, ndArray.getShortDataArray(), count, offset);
                break;

            case DataType.Int32:
                setData(dataType, ndArray.getIntDataArray(), count, offset);
                break;

            case DataType.Int64:
                setData(dataType, ndArray.getLongDataArray(), count, offset);
                break;

            case DataType.Float:
                setData(dataType, ndArray.getFloatDataArray(), count, offset);
                break;

            case DataType.Double:
                setData(dataType, ndArray.getDoubleDataArray(), count, offset);
                break;
        }
    }

    /**
     * Write ND-Array data to data array.
     *
     * @param ndArray nd array
     */
    public void setData(NDArray ndArray) {
        NDSize dims = new NDSize(ndArray.getShape());
        setDataExtent(dims);
        setData(ndArray.getDataType(), ndArray, dims, new NDSize());
    }

    /**
     * Write ND-Array data to data array.
     *
     * @param ndArray nd array
     * @param offset  offset
     */
    public void setData(NDArray ndArray, NDSize offset) {
        NDSize dims = new NDSize(ndArray.getShape());
        setDataExtent(dims);
        setData(ndArray.getDataType(), ndArray, dims, offset);
    }

    /**
     * Get written ND-Array from data array.
     *
     * @param dataType type
     * @param count    shape
     * @param offset   offset
     * @return ndarray
     */
    public NDArray getData(int dataType, NDSize count, NDSize offset) {

        int size = (int) count.getElementsProduct();

        switch (dataType) {
            case DataType.Int8:
                byte[] byteData = new byte[size];
                getData(dataType, byteData, count, offset);
                return new NDArray(byteData, count.getData());

            case DataType.Int16:
                short[] shortData = new short[size];
                getData(dataType, shortData, count, offset);
                return new NDArray(shortData, count.getData());

            case DataType.Int32:
                int[] intData = new int[size];
                getData(dataType, intData, count, offset);
                return new NDArray(intData, count.getData());

            case DataType.Int64:
                long[] longData = new long[size];
                getData(dataType, longData, count, offset);
                return new NDArray(longData, count.getData());

            case DataType.Float:
                float[] floatData = new float[size];
                getData(dataType, floatData, count, offset);
                return new NDArray(floatData, count.getData());

            case DataType.Double:
                double[] doubleData = new double[size];
                getData(dataType, doubleData, count, offset);
                return new NDArray(doubleData, count.getData());

            default:
                return null;
        }
    }

    /**
     * Get written ND-Array from data array.
     *
     * @return nd array
     */
    public NDArray getData() {
        return getData(getDataType(), getDataExtent(), new NDSize());
    }

    /**
     * Get written ND-Array from data array.
     *
     * @param offset offset
     * @return nd array
     */
    public NDArray getData(NDSize offset) {
        return getData(getDataType(), getDataExtent(), offset);
    }


}
