package com.rainbowtooth.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A simple map of known size from String to String.
 * 
 * @author Khalil Fazal
 */
public class SimpleMap implements Parcelable {

    /**
     * A public CREATOR field that generates instances of {@link SimpleMap} from a {@link Parcel}.
     */
    public static final Parcelable.Creator<SimpleMap> CREATOR = new Parcelable.Creator<SimpleMap>() {

        @Override
        public SimpleMap createFromParcel(final Parcel source) {
            return new SimpleMap(source);
        }

        @Override
        public SimpleMap[] newArray(final int size) {
            return new SimpleMap[size];
        }

    };

    /**
     * Backing array of keys
     */
    private final String[] keys;

    /**
     * Backing array of values
     */
    private final String[] values;

    /**
     * Position where new items can be inserted.
     * Must be <= n.
     */
    private int mark;

    /**
     * Construct the map with a known size
     * 
     * @param n the size of the map
     */
    public SimpleMap(final int n) {
        this.keys = new String[n];
        this.values = new String[n];
        this.mark = 0;
    }

    /**
     * Creates a {@link SimpleMap} from a {@link Parcel}
     * 
     * @param in the parcel
     */
    protected SimpleMap(final Parcel in) {
        this.keys = (String[]) in.readArray(String.class.getClassLoader());
        this.values = (String[]) in.readArray(String.class.getClassLoader());
        this.mark = in.readInt();
    }

    /**
     * Insert (k, v) into the map.
     * 
     * @param k the key
     * @param v the value
     */
    public void put(final String k, final String v) {
        this.keys[this.mark] = k;
        this.values[this.mark++] = v;
    }

    /**
     * @return the keys as a primitive array
     */
    public String[] getKeys() {
        return this.keys;
    }

    /**
     * Get the value at an index
     * 
     * @param i the index
     * @return the value
     */
    public String valueAt(final int i) {
        return this.values[i];
    }

    /**
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeArray(this.keys);
        dest.writeArray(this.values);
        dest.writeInt(this.mark);
    }
}
