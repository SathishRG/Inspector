package com.folkus.ui.login.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.folkus.R;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.fragments.PendingInspectionsFragment;

import java.util.ArrayList;

public class AdapterPendingInspection extends RecyclerView.Adapter<AdapterPendingInspection.ViewHolder> {
    private ArrayList<CompletedInspection> localDataSet;
    private PendingInspectionsFragment mPendingInspectionsFragment;
    private InspectionRequestViewModel requestViewModel;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSellerNameValue;
        private TextView tvStatusValue;
        private TextView tvNoOfVehicleValue;
        private TextView tvSellerPhoneValue;
        private TextView tvInspectionLocation;
        private ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            super(view);
            tvSellerNameValue = (TextView) view.findViewById(R.id.tvSellerNameValue);
            tvStatusValue = (TextView) view.findViewById(R.id.tvStatusValue);
            tvSellerPhoneValue = (TextView) view.findViewById(R.id.tvSellerPhoneValue);
            tvInspectionLocation = (TextView) view.findViewById(R.id.tvInspectionLocation);
            tvNoOfVehicleValue = (TextView) view.findViewById(R.id.tvNoOfVehicleValue);
            constraintLayout = (ConstraintLayout) view.findViewById(R.id.inspectionRequestDetailsCL);
        }

    }

    public AdapterPendingInspection(ArrayList<CompletedInspection> dataSet, PendingInspectionsFragment pendingInspectionsFragment, InspectionRequestViewModel inspectionRequestViewModel) {
        localDataSet = dataSet;
        mPendingInspectionsFragment = pendingInspectionsFragment;
        requestViewModel = inspectionRequestViewModel;
    }

    @Override
    public AdapterPendingInspection.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view_inspection, viewGroup, false);

        return new AdapterPendingInspection.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPendingInspection.ViewHolder viewHolder, final int position) {
        CompletedInspection completedInspection = localDataSet.get(position);
        viewHolder.tvSellerNameValue.setText(completedInspection.getLocation());
        viewHolder.tvInspectionLocation.setText(completedInspection.getTime());
        viewHolder.tvNoOfVehicleValue.setText(completedInspection.getMake());
        viewHolder.tvSellerPhoneValue.setText(completedInspection.getDate());
        if (completedInspection.getStatus().equals("pending")) {
            viewHolder.tvStatusValue.setText(completedInspection.getStatus());
            viewHolder.tvStatusValue.setTextColor(Color.parseColor("#FF9900"));
        } else {
            viewHolder.tvStatusValue.setText(completedInspection.getStatus());
            viewHolder.tvStatusValue.setTextColor(Color.parseColor("#015B0A"));
        }
        viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestViewModel.onSelectInspectionDetail(completedInspection);
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
