package com.folkus.ui.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.folkus.R;
import com.folkus.data.remote.response.NotificationData;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.fragments.NotificationFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    private ArrayList<NotificationData> localDataSet;
    private UserViewModel userViewModel;

    public void setValue(ArrayList<NotificationData> data) {
        localDataSet = data;
        notifyDataSetChanged();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getTvMessageValue() {
            return tvMessageValue;
        }

        public TextView getTvTimeAgo() {
            return tvTimeAgo;
        }

        private final TextView tvMessageValue;
        private final TextView tvTimeAgo;
        private final ImageView imageButton;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvMessageValue = (TextView) view.findViewById(R.id.tvMessageValue);
            tvTimeAgo = (TextView) view.findViewById(R.id.tvTimeAgo);
            imageButton = (ImageView) view.findViewById(R.id.imageButton);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NotificationFragment.position = getAdapterPosition();
                    String notificationId = localDataSet.get(getAdapterPosition()).getNotification_id() + "";
                    userViewModel.deleteNotification(notificationId);
                }
            });

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet          String[] containing the data to populate views to be used
     *                         by RecyclerView.
     * @param userViewModel
     */
    public AdapterNotification(ArrayList<NotificationData> dataSet, UserViewModel userViewModel) {
        localDataSet = dataSet;
        this.userViewModel = userViewModel;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterNotification.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view_notification, viewGroup, false);

        return new AdapterNotification.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterNotification.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        long Timestamp = localDataSet.get(position).getTime();
        Date timeD = new Date(Timestamp * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String Time = sdf.format(timeD);

        viewHolder.getTvMessageValue().setText(localDataSet.get(position).getMessage());
        viewHolder.getTvTimeAgo().setText(Time);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
