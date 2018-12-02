package sumsar1812.github.io.todonearme.presenter;

import android.content.Context;
import android.location.Location;

import sumsar1812.github.io.todonearme.model.ToDoItem;

public class CreatePresenter {
    Context context;
    ToDoItem toDoItem = new ToDoItem();
    public CreatePresenter(Context context) {
        this.context = context;
    }
    public Location getLocation() {
        return toDoItem.getL();
    }
    public void setName(String name) {
        toDoItem.setName(name);
    }
    public void setDescription(String desc) {
        toDoItem.setDescription(desc);
    }
    public void setLocation(Location location) {
        toDoItem.setL(location);
    }
    public ToDoItem getToDoItem() {
        return toDoItem;
    }
}
