package com.example.homeutilityservice;

import android.content.Context;
import android.content.LocusId;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    final ArrayList<String> objectIdArrayList = new ArrayList<>();
    final ArrayList<String> merchantNameArrayList = new ArrayList<>();
    final ArrayList<String> statusArray = new ArrayList<>();
    final ArrayList<String> priceArray = new ArrayList<>();
    final ArrayList<String> dateArray = new ArrayList<>();
    ListView historyListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        historyListView = view.findViewById(R.id.historyList);
        final myAdapter adapter = new myAdapter(getActivity(), merchantNameArrayList, objectIdArrayList, statusArray,dateArray,priceArray);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("order");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
        query.whereEqualTo("status", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Log.i("object id", object.getObjectId());
                        objectIdArrayList.add(object.getObjectId());
                        merchantNameArrayList.add(object.getString("shopName"));
                        dateArray.add(String.valueOf(object.getCreatedAt()));
                        priceArray.add(String.valueOf(object.get("total")));
                        Log.i("shopNme", object.getString("shopName"));

                        statusArray.add("Pending");

                    }
                    historyListView.setAdapter(adapter);
                }
            }
        });
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("order");
        query1.whereEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
        query1.whereEqualTo("status", true);
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Log.i("object id", object.getObjectId());
                        objectIdArrayList.add(object.getObjectId());
                        merchantNameArrayList.add(object.getString("shopName"));
                        dateArray.add(String.valueOf(object.getCreatedAt()));
                        priceArray.add(String.valueOf(object.get("total")));
                        statusArray.add("Completed");

                    }
                    historyListView.setAdapter(adapter);
                }
            }
        });


        return view;
    }

    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> merchantNameArray = new ArrayList<String>();
        ArrayList<String> descriptionArray = new ArrayList<String>();
        ArrayList<String> statusArray = new ArrayList<>();
        ArrayList<String> dateArray = new ArrayList<>();
        ArrayList<String> priceArray = new ArrayList<>();

        myAdapter(Context c, ArrayList<String> merchantNameArray, ArrayList<String> descriptionArray, ArrayList<String> statusArray, ArrayList<String> dateArray, ArrayList<String> priceArray) {
            super(c, R.layout.custom_listview, R.id.merchantNameTextView, merchantNameArray);
            this.context = c;
            this.merchantNameArray = merchantNameArray;
            this.descriptionArray = descriptionArray;
            this.statusArray = statusArray;
            this.dateArray = dateArray;
            this.priceArray = priceArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.history_layout, parent, false);
            TextView merchantName = item.findViewById(R.id.objectIdTextView);
            TextView description = item.findViewById(R.id.merchantNameTextView);
            TextView status = item.findViewById(R.id.statusTextView);
            TextView date = item.findViewById(R.id.dateTextView);
            TextView price = item.findViewById(R.id.priceTextView);
            merchantName.setText(merchantNameArray.get(position));
            description.setText(descriptionArray.get(position));
            status.setText(statusArray.get(position));
            price.setText("₹ "+priceArray.get(position));
            date.setText(dateArray.get(position));
            Log.i("adapter set", "success");


            return item;
        }
    }
}
