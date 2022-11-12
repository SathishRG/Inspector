package com.folkus.ui.login.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.folkus.R;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.InspectionRequestData;

import java.util.ArrayList;

public class AdapterCompletedInspection extends RecyclerView.Adapter<AdapterCompletedInspection.ViewHolder> {
    private ArrayList<CompletedInspection> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getTvSellerNameValue() {
            return tvSellerNameValue;
        }

        public TextView getTvStatusValue() {
            return tvStatusValue;
        }

        public TextView getTvNoOfVehicleValue() {
            return tvNoOfVehicleValue;
        }

        public TextView getTvSellerPhoneValue() {
            return tvSellerPhoneValue;
        }

        public TextView getTvInspectionLocation() {
            return tvInspectionLocation;
        }

        private final TextView tvSellerNameValue;
        private final TextView tvStatusValue;
        private final TextView tvNoOfVehicleValue;
        private final TextView tvSellerPhoneValue;
        private final TextView tvInspectionLocation;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvSellerNameValue = (TextView) view.findViewById(R.id.tvSellerNameValue);
            tvStatusValue = (TextView) view.findViewById(R.id.tvStatusValue);
            tvNoOfVehicleValue = (TextView) view.findViewById(R.id.tvNoOfVehicleValue);
            tvSellerPhoneValue = (TextView) view.findViewById(R.id.tvSellerPhoneValue);
            tvInspectionLocation = (TextView) view.findViewById(R.id.tvInspectionLocation);
        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public AdapterCompletedInspection(ArrayList<CompletedInspection> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterCompletedInspection.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view_completed_inspection, viewGroup, false);

        return new AdapterCompletedInspection.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterCompletedInspection.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        String status = localDataSet.get(position).getStatus();

        viewHolder.getTvSellerNameValue().setText(localDataSet.get(position).getLocation());
        if (status.equals("pending")) {
            viewHolder.getTvStatusValue().setTextColor(Color.parseColor("#FF9900"));
        } else {
            viewHolder.getTvStatusValue().setTextColor(Color.parseColor("#015B0A"));
        }
        viewHolder.getTvNoOfVehicleValue().setText(localDataSet.get(position).getMake());
        viewHolder.getTvSellerPhoneValue().setText(localDataSet.get(position).getDate());
        viewHolder.getTvInspectionLocation().setText(localDataSet.get(position).getTime());
        viewHolder.getTvStatusValue().setText(localDataSet.get(position).getStatus());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
