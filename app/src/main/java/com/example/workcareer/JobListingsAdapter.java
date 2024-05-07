package com.example.workcareer;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobListingsAdapter extends RecyclerView.Adapter<JobListingsAdapter.ViewHolder> {

    private List<JobListing> jobListings;
    private JobListingDao jobListingDao;
    private OnJobClickListener listener;
    private ExecutorService executorService;
    private Activity activity;

    public interface OnJobClickListener {
        void onJobClicked(JobListing jobListing, boolean isLiked);
    }

    public JobListingsAdapter(JobListingDao jobListingDao, Activity activity) {
        this.jobListingDao = jobListingDao;
        this.activity = activity;
        executorService = Executors.newSingleThreadExecutor();
    }

    public void setJobListings(List<JobListing> jobListings) {
        this.jobListings = jobListings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobListing jobListing = jobListings.get(position);
        holder.bind(jobListing);
        holder.ivFavorite.setImageResource(jobListing.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
    }

    @Override
    public int getItemCount() {
        return jobListings != null ? jobListings.size() : 0;
    }

    public void setOnJobClickListener(OnJobClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvCompany, tvLocation;
        private ImageView ivFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvCompany = itemView.findViewById(R.id.tv_company);
            tvLocation = itemView.findViewById(R.id.tv_location);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);

            ivFavorite.setOnClickListener(v -> {
                JobListing jobListing = jobListings.get(getAdapterPosition());
                boolean isLiked = !jobListing.isFavorite();
                executorService.execute(() -> {
                    jobListing.setFavorite(isLiked);
                    if (isLiked) {
                        jobListingDao.insert(jobListing);
                    } else {
                        jobListingDao.delete(jobListing);
                    }
                    if (listener != null) {
                        listener.onJobClicked(jobListing, isLiked);
                    }
                });
                notifyItemChanged(getAdapterPosition());
            });
        }

        public void bind(JobListing jobListing) {
            tvTitle.setText(jobListing.getTitle());
            tvCompany.setText(jobListing.getCompany());
            tvLocation.setText(jobListing.getLocation());
            ivFavorite.setImageResource(jobListing.isFavorite() ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_outline);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        executorService.shutdown();
    }
}
