package com.example.workcareer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoriteFragment extends Fragment {

    private RecyclerView rvFavoriteJobs;
    private FavoriteJobsAdapter adapter;
    private JobListingDao jobListingDao;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        rvFavoriteJobs = view.findViewById(R.id.rv_favorite_jobs);
        AppDatabase db = Room.databaseBuilder(requireContext(), AppDatabase.class, "job-listings-db").build();
        jobListingDao = db.jobListingDao();
        executorService = Executors.newSingleThreadExecutor();
        adapter = new FavoriteJobsAdapter(requireActivity(), jobListingDao, executorService);
        rvFavoriteJobs.setAdapter(adapter);
        rvFavoriteJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoriteJobListings();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
    private void loadFavoriteJobListings() {
        executorService.execute(() -> {
            List<JobListing> favoriteJobs = jobListingDao.getFavoriteJobListings();
            requireActivity().runOnUiThread(() -> {
                adapter.setFavoriteJobs(favoriteJobs);
                if (favoriteJobs.isEmpty()) {
                    showEmptyView();
                } else {
                    hideEmptyView();
                }
            });
        });
    }

    private void showEmptyView() {
        rvFavoriteJobs.setVisibility(View.GONE);
        View emptyView = requireActivity().getLayoutInflater().inflate(R.layout.empty_view, null);
        TextView emptyText = emptyView.findViewById(R.id.empty_text);
        emptyText.setText(R.string.no_favorite_jobs);
        ((ViewGroup) rvFavoriteJobs.getParent()).addView(emptyView);
    }

    private void hideEmptyView() {
        rvFavoriteJobs.setVisibility(View.VISIBLE);
        ViewGroup parent = (ViewGroup) rvFavoriteJobs.getParent();
        View emptyView = parent.findViewWithTag("empty_view");
        if (emptyView != null) {
            parent.removeView(emptyView);
        }
    }
}
