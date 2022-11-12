package com.folkus.ui.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.folkus.R;
import com.folkus.data.remote.response.SellerDealerShip;

import java.util.ArrayList;

public class AdapterCustomerNameAutoComplete extends ArrayAdapter<SellerDealerShip> implements Filterable {

    Context context;
    int resource;
    ArrayList<SellerDealerShip> mList, filteredPeople, mListAll;

    public AdapterCustomerNameAutoComplete(Context context,  int textViewResourceId, ArrayList<SellerDealerShip> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.resource = textViewResourceId;


        this.mList = items;
        mListAll = mList;
        filteredPeople = new ArrayList<SellerDealerShip>();
    }

    public SellerDealerShip getItem(int position) {

        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.lbl_name);
            textView.setText(mList.get(position).getDealer_name());
        }
        SellerDealerShip people = mList.get(position);
        if (people != null) {
            TextView textView = (TextView) view.findViewById(R.id.lbl_name);
            if (textView != null) {
                textView.setText(people.getDealer_name());
            }

        }
        return view;
    }

    @Override
    public Filter getFilter() {

        return nameFilter;
    }

    Filter nameFilter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<SellerDealerShip> filteredList = (ArrayList<SellerDealerShip>) results.values;

            if (results != null && results.count > 0) {
                clear();
                for (SellerDealerShip people : filteredList) {
                    add(people);
                }
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                filteredPeople.clear();
                for (SellerDealerShip people : mListAll) {
                    if (people.getDealer_name().contains(constraint)) {
                        filteredPeople.add(people);
                    }
                }
                filterResults.values = filteredPeople;
                filterResults.count = filteredPeople.size();
            }
            return filterResults;
        }
    };
}
