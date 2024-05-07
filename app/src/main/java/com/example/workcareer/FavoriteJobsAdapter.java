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

public class FavoriteJobsAdapter extends RecyclerView.Adapter<FavoriteJobsAdapter.ViewHolder> {
    private List<JobListing> favoriteJobs;
    private JobListingDao jobListingDao;
    private ExecutorService executorService;
    private Activity activity;

    public FavoriteJobsAdapter(Activity activity, JobListingDao jobListingDao, ExecutorService executorService) {
        this.activity = activity;
        this.jobListingDao = jobListingDao;
        this.executorService = executorService;
    }

    public void setFavoriteJobs ( List<JobListing> favoriteJobs ) {
        this.favoriteJobs = favoriteJobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder ( @NonNull ViewGroup parent, int viewType ) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_favorite_job, parent, false );
        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder ( @NonNull ViewHolder holder, int position ) {
        JobListing jobListing = favoriteJobs.get( position );
        holder.bind( jobListing );
    }

    @Override
    public int getItemCount () {
        return favoriteJobs != null ? favoriteJobs.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvCompany, tvLocation;
        private ImageView ivFavorite;

        public ViewHolder ( @NonNull View itemView ) {
            super( itemView );
            tvTitle = itemView.findViewById( R.id.tv_title );
            tvCompany = itemView.findViewById( R.id.tv_company );
            tvLocation = itemView.findViewById( R.id.tv_location );
            ivFavorite = itemView.findViewById( R.id.iv_favorite ); // Исправлено

            ivFavorite.setOnClickListener( v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    JobListing jobListing = favoriteJobs.get( position );
                    executorService.execute( () -> {
                        jobListingDao.delete( jobListing );
                        removeItem( position );
                    } );
                }
            } );
        }

        public void bind(JobListing jobListing) {
            tvTitle.setText(jobListing.getTitle());
            tvCompany.setText(jobListing.getCompany());
            tvLocation.setText(jobListing.getLocation());
            ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
        }

        private void removeItem(int position) {
            JobListing jobListing = favoriteJobs.get(position);
            executorService.execute(() -> {
                jobListingDao.delete(jobListing);
                activity.runOnUiThread(() -> {
                    favoriteJobs.remove(position);
                    notifyItemRemoved(position);
                });
            });
        }
    }
}
