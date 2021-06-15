package com.example.homeutilityservice;

import android.content.Context;
import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class ElectricianFragment extends Fragment {
    final ArrayList<String> merchantNameArray = new ArrayList<String>();
    final ArrayList<String> descriptionArray = new ArrayList<String>();
    ListView listView;
    myAdapter adapter;
    ConstraintLayout failed;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String location = ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle().toString();
        View view = inflater.inflate(R.layout.fragment_electrician, container, false);
        listView = view.findViewById(R.id.listView);
        failed=view.findViewById(R.id.failedConstraint);
        adapter = new myAdapter(getActivity(), merchantNameArray, descriptionArray);
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("merchantDetails");
            query.whereEqualTo("location", location);
            query.whereEqualTo("occupation", "Electrician");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (objects.size() > 0 && e == null) {
                        for (ParseObject object : objects) {
                            merchantNameArray.add(object.getString("shopName"));
                            Log.i("name", object.getString("shopName"));
                            descriptionArray.add(object.getString("discription"));
                            Log.i("des", object.getString("discription"));
                            Log.i("location", object.getString("location"));
                        }
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), "merchants not found", Toast.LENGTH_LONG).show();
                        failed.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {

            Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ParseQuery<ParseObject>[] username = new ParseQuery[]{ParseQuery.getQuery("merchantDetails")};
                username[0].whereEqualTo("shopName", merchantNameArray.get(position));
                username[0].findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null && objects.size() > 0) {
                            for (ParseObject object : objects) {
                                String user = object.getString("username");
                                Intent intent = new Intent(getActivity(), OrderActivity.class);
                                intent.putExtra("username", user);
                                intent.putExtra("location", location);
                                intent.putExtra("shop", merchantNameArray.get(position));
                                startActivity(intent);
                            }
                        }
                    }
                });


            }
        });

        return view;
    }

    //adapter class
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> merchantNameArray = new ArrayList<String>();
        ArrayList<String> descriptionArray = new ArrayList<String>();

        myAdapter(Context c, ArrayList<String> merchantNameArray, ArrayList<String> descriptionArray) {
            super(c, R.layout.custom_listview, R.id.merchantNameTextView, merchantNameArray);
            this.context = c;
            this.merchantNameArray = merchantNameArray;
            this.descriptionArray = descriptionArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.custom_listview, parent, false);
            TextView merchantName = item.findViewById(R.id.merchantNameTextView);
            TextView description = item.findViewById(R.id.descriptionTextView);

            merchantName.setText(merchantNameArray.get(position));
            description.setText(descriptionArray.get(position));
            Log.i("adapter set", "success");


            return item;
        }
    }

}
