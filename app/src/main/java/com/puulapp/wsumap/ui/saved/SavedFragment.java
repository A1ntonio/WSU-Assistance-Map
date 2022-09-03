package com.puulapp.wsumap.ui.saved;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.puulapp.wsumap.R;
import com.puulapp.wsumap.database.DBAdapter;
import com.puulapp.wsumap.database.SaveModel;
import com.puulapp.wsumap.databinding.FragmentSavedBinding;
import com.puulapp.wsumap.ui.home.HomeListAdapter;
import com.puulapp.wsumap.ui.home.ItemListModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SavedFragment extends Fragment {


    private List<SaveModel> itemLists;
    private SavedListAdapter savedListAdapter;
    private int position;
    RecyclerView save_rv;

    private FragmentSavedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSavedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initiate(root);
        return root;
    }

    private void initiate(View root) {
        save_rv = root.findViewById(R.id.save_rv);
        save_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        itemLists = new DBAdapter(getActivity()).retrieveSaved();

        savedListAdapter = new SavedListAdapter(itemLists, getContext());
        save_rv.setAdapter(savedListAdapter);
        savedListAdapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                Toast.makeText(getContext(), "Deleting", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                SaveModel saveModel = itemLists.get(viewHolder.getAdapterPosition());
                position = viewHolder.getAdapterPosition();
                boolean result = new DBAdapter(getActivity()).deleteSingleRow(saveModel.getId());
                if (result){
                    Snackbar.make(getContext(), save_rv, "Deleted", 2000).show();
                } else {
                    Snackbar.make(getContext(), save_rv, "Not Deleted", 2000).show();
                }
                itemLists.remove(position);
                savedListAdapter.notifyDataSetChanged();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(save_rv);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}