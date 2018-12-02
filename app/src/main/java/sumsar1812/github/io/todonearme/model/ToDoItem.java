package sumsar1812.github.io.todonearme.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class ToDoItem implements Parcelable {
    private String mName;
    private String mDescription;
    private Location mLocation;

    public ToDoItem(String name, String description, Location l) {
        this.mName = name;
        this.mDescription = description;
        this.mLocation = l;
    }
    public ToDoItem() { }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mName.hashCode();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mLocation.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToDoItem)) {
            return false;
        }
        ToDoItem tdi = (ToDoItem) o;
        return tdi.mName.equalsIgnoreCase(this.mName) && tdi.mDescription.equalsIgnoreCase(this.mDescription) && tdi.mLocation == mLocation;
    }

    public static final Creator<ToDoItem> CREATOR = new Creator<ToDoItem>() {
        @Override
        public ToDoItem createFromParcel(Parcel in) {
            return new ToDoItem(in);
        }

        @Override
        public ToDoItem[] newArray(int size) {
            return new ToDoItem[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Location getL() {
        return mLocation;
    }

    public void setL(Location l) {
        this.mLocation = l;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mDescription);
        dest.writeParcelable(mLocation, flags);
    }
    protected ToDoItem(Parcel in) {
        mName = in.readString();
        mDescription = in.readString();
        mLocation = in.readParcelable(Location.class.getClassLoader());
    }
}
