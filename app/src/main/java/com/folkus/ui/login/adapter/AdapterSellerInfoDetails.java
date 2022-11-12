package com.folkus.ui.login.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.folkus.R;
import com.folkus.data.remote.response.SellerDetails;

import java.util.ArrayList;

public class AdapterSellerInfoDetails extends RecyclerView.Adapter<AdapterSellerInfoDetails.ViewHolder> {
    private ArrayList<SellerDetails> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSellerNameValue;
        private final TextView tvSellerPhoneValue;
        private final TextView tvInspectionLocation;

        public TextView getVinId() {
            return vinId;
        }

        private final TextView vinId;


        public TextView getTvSellerNameValue() {
            return tvSellerNameValue;
        }

        public TextView getTvSellerPhoneValue() {
            return tvSellerPhoneValue;
        }

        public TextView getTvInspectionLocation() {
            return tvInspectionLocation;
        }


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tvSellerNameValue = (TextView) view.findViewById(R.id.tvSellerNameValue);
            tvSellerPhoneValue = (TextView) view.findViewById(R.id.tvSellerPhoneValue);
            tvInspectionLocation = (TextView) view.findViewById(R.id.tvInspectionLocation);
            vinId = (TextView) view.findViewById(R.id.vinId);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public AdapterSellerInfoDetails(ArrayList<SellerDetails> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterSellerInfoDetails.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_seller_info, viewGroup, false);

        return new AdapterSellerInfoDetails.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AdapterSellerInfoDetails.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Log.e("TAG", "onBindViewHolder: "+ localDataSet.get(position).getMake());
        viewHolder.getTvSellerNameValue().setText(localDataSet.get(position).getYear() + "");
        viewHolder.getTvInspectionLocation().setText(localDataSet.get(position).getMake());
        viewHolder.getTvSellerPhoneValue().setText(localDataSet.get(position).getModel());
        viewHolder.getVinId().setText(localDataSet.get(position).getVin_no());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
