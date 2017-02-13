package com.llamacorp.filterdialogexample;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom adapter class that implements the Filterable interface, allowing it
 * to filter items within it based on a filter string.
 */
public class FilterAdapter extends BaseAdapter implements Filterable {
   List<String> arrayList; // current values post filtering
   private List<String> mOriginalValues; // values pre filtering
   private LayoutInflater inflater; 

   public FilterAdapter(Context context, List<String> arrayList) {
      this.arrayList = arrayList;
      inflater = LayoutInflater.from(context);
   }

   @Override
   public int getCount() {
      return arrayList.size();
   }

   @Override
   public Object getItem(int position) {
      return position;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   private class ViewHolder {
      TextView textView;
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;

      if (convertView == null){

         holder = new ViewHolder();
         convertView = inflater.inflate(R.layout.alertlistrow, null);
         holder.textView = (TextView) convertView.findViewById(R.id.country_name_textView);
         convertView.setTag(holder);
      } else {
         holder = (ViewHolder) convertView.getTag();
      }
      holder.textView.setText(arrayList.get(position));
      return convertView;
   }

   @Override
   public Filter getFilter() {
      return new Filter() {

         @SuppressWarnings("unchecked")
         @Override
         protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (List<String>) results.values; // has the filtered values
            notifyDataSetChanged();  // notifies the data with new filtered values
         }

         @Override
         protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
            List<String> FilteredArrList = new ArrayList<>();

            if (mOriginalValues == null){
               mOriginalValues = new ArrayList<>(arrayList); // saves the original data in mOriginalValues
            }

            // if constraint is too short:
            if (constraint == null || constraint.length() == 0){
               // set the Original result to return
               results.count = mOriginalValues.size();
               results.values = mOriginalValues;
            // constraint is long enough, proceed:
            } else {
               constraint = constraint.toString().toLowerCase();
               int mTextLength = constraint.length();

               for (String data : mOriginalValues) {
                  if (mTextLength > data.length())
                     continue;
                  if (data.toLowerCase().contains(constraint)){
                     FilteredArrList.add(data);
                  }
               }

//               // slow down search for testing
//               for (int j = 0; j < 1000000000; j++) {
//                  results.count = j;
//               }

               // set the Filtered result to return
               results.count = FilteredArrList.size();
               results.values = FilteredArrList;
            }
            return results;
         }
      };
   }
}