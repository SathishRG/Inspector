package com.folkus.ui.login.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.folkus.R;
import com.folkus.data.remote.response.InspectionRequestData;
import com.folkus.ui.login.fragments.InspectionRequestFragment;

import java.util.ArrayList;

public class AdapterInspectionRequest extends RecyclerView.Adapter<AdapterInspectionRequest.ViewHolder> {
    private ArrayList<InspectionRequestData> localDataSet;
    public static InspectionRequestFragment inspectionRequestFragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvSellerNameValue;
        private final TextView tvSellerPhoneValue;
        private final TextView tvInspectionLocation;
        private final TextView tvNoOfVehicleValue;
        private final CardView cardView;


        public TextView getTvSellerNameValue() {
            return tvSellerNameValue;
        }

        public TextView getTvSellerPhoneValue() {
            return tvSellerPhoneValue;
        }

        public TextView getTvInspectionLocation() {
            return tvInspectionLocation;
        }

        public TextView getTvNoOfVehicleValue() {
            return tvNoOfVehicleValue;
        }


        public CardView getCardView() {
            return cardView;
        }

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            cardView = (CardView) view.findViewById(R.id.cardView);


            tvSellerNameValue = (TextView) view.findViewById(R.id.tvSellerNameValue);
            tvSellerPhoneValue = (TextView) view.findViewById(R.id.tvSellerPhoneValue);
            tvInspectionLocation = (TextView) view.findViewById(R.id.tvInspectionLocation);
            tvNoOfVehicleValue = (TextView) view.findViewById(R.id.tvNoOfVehicleValue);
        }


    }

    /**
     * Initialize the dataset of the Adapter.
     *  @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     * @param inspectionRequestFragment
     */
    public AdapterInspectionRequest(ArrayList<InspectionRequestData> dataSet, InspectionRequestFragment inspectionRequestFragment) {
        localDataSet = dataSet;
        this.inspectionRequestFragment = inspectionRequestFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_inspection_request, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        int no_cars = localDataSet.get(position).getNo_cars();
        viewHolder.getTvSellerNameValue().setText(localDataSet.get(position).getDealer_name());
        viewHolder.getTvInspectionLocation().setText(localDataSet.get(position).getAddress());
        viewHolder.getTvNoOfVehicleValue().setText(no_cars+"");
        viewHolder.getTvSellerPhoneValue().setText(localDataSet.get(position).getPhone_no());

        viewHolder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inspectionRequestFragment.navToSellerDetailsPage(localDataSet.get(position));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
