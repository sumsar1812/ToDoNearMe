package sumsar1812.github.io.todonearme;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sumsar1812.github.io.todonearme.adapter.RecyclerViewAdapter;


public class CurrentToDoFragment extends Fragment {
    RecyclerView recyclerView;
    private CurrentToDoPresenter mPresenter;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPresenter = new CurrentToDoPresenter();
        initRecyclerView();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_to_do, container, false);
    }

    private void initRecyclerView() {

        if (getView() != null) {
            mRecyclerViewAdapter = new RecyclerViewAdapter(getView().getContext(),mPresenter.loadItems());
            recyclerView = getView().findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getView().getContext()));
            addAdapter();
        }

    }
    private void addAdapter() {
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }

}
