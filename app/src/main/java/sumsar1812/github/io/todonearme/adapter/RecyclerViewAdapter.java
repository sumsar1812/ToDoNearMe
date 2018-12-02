package sumsar1812.github.io.todonearme.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sumsar1812.github.io.todonearme.LocListener;
import sumsar1812.github.io.todonearme.R;
import sumsar1812.github.io.todonearme.Utils;
import sumsar1812.github.io.todonearme.model.ToDoItem;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<ToDoItem> mItems;
    private Context mContext;
    public RecyclerViewAdapter(Context context, List<ToDoItem> items) {
        this.mItems = items;
        this.mContext = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout, viewGroup, false);
        return new ViewHolder(view);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ToDoItem doItem = mItems.get(i);
        viewHolder.itemName.setText(doItem.getName());
        if (LocListener.getInstance().getLastLocation() == null) {
            viewHolder.itemDistance.setText(mContext.getString(R.string.NA));
        }
        float f = LocListener.getInstance().getLastLocation().distanceTo(doItem.getL());
        viewHolder.itemDistance.setText(Utils.getInstance().getDistanceString(f));
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView itemDistance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemDistance = itemView.findViewById(R.id.item_distance);
        }
    }
}
