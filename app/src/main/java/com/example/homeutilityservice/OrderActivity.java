package com.example.homeutilityservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.AsyncListUtil;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.internal.Util;

public class OrderActivity extends AppCompatActivity {
    ArrayList<String> selectedItems = new ArrayList<>();

    ConstraintLayout orderConstraint;
    Button bookButton;
    ListView itemListView;
    EditText item;
    EditText price;
    String merchantuser;
    String shop;
    String location;
    ConstraintLayout failedConstraint;
    Integer total = 0;
    myAdapter adapter;
    myAdapter orderadapter;
    ListView orderConstraintListView;
    TextView totalTextView;
    ListView orderedListView;
    TextView orderconstrainntTotal;
    ConstraintLayout itemConstraintLayout;
    final ArrayList<String> itemNameArray = new ArrayList<String>();
    final ArrayList<String> itemPriceArray = new ArrayList<String>();
    final ArrayList<String> orderItemNameArray = new ArrayList<String>();
    final ArrayList<Integer> totalArray = new ArrayList<>();
    final ArrayList<String> orderItemPriceArray = new ArrayList<String>();
    EditText addressEditText;
    ConstraintLayout successConstraint;

// book button

    public void bookButton(View view) {
        itemConstraintLayout.setVisibility(View.INVISIBLE);
        orderConstraint.setVisibility(View.VISIBLE);
        successConstraint.setVisibility(View.INVISIBLE);
        orderConstraintListView.setAdapter(orderadapter);
        orderConstraintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(OrderActivity.this, "long press to delete item", Toast.LENGTH_SHORT).show();
            }
        });
        orderconstrainntTotal.setText(String.valueOf(total + 300));
        orderConstraintListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int r = Integer.parseInt(orderconstrainntTotal.getText().toString());
                int sum = r - Integer.parseInt(orderItemPriceArray.get(position));
                orderItemNameArray.remove(position);
                totalArray.remove(position);
                orderItemPriceArray.remove(position);


                orderedListView.setAdapter(orderadapter);

                orderconstrainntTotal.setText(String.valueOf(sum));

                return true;
            }
        });
    }

    // order button
    public void orderButton(View view) {
        Random rand = new Random();
        int i = rand.nextInt(10000);
        Boolean complete = true;
        Boolean incomplete = false;
        if (addressEditText.getText().toString().isEmpty() || addressEditText.getText().toString().length()<=20) {
            addressEditText.setError("enter  your full Address in 50 words");
            addressEditText.requestFocus();
        } else {
            ParseObject orderObject = new ParseObject("order");
            orderObject.put("orderId", shop + i);
            orderObject.put("username", ParseUser.getCurrentUser().getUsername().toString());
            orderObject.put("address", addressEditText.getText().toString());
            orderObject.put("status", incomplete);
            orderObject.put("shopName", shop);
            orderObject.put("location",location);
            orderObject.put("merchantId", merchantuser);
            orderObject.put("itemName", orderItemNameArray);
            orderObject.put("itemPrice", orderItemPriceArray);
            orderObject.put("name",ParseUser.getCurrentUser().getString("name"));
            orderObject.put("total",total+300);
            orderObject.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(OrderActivity.this, "ordered successfully", Toast.LENGTH_SHORT).show();
                        successConstraint.setVisibility(View.VISIBLE);
                        itemConstraintLayout.setVisibility(View.INVISIBLE);
                        orderConstraint.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        itemConstraintLayout = findViewById(R.id.itemConstraint);
        itemConstraintLayout.setVisibility(View.VISIBLE);
        orderConstraint = findViewById(R.id.orderConstraintLayout);
        orderConstraint.setVisibility(View.INVISIBLE);
        bookButton = findViewById(R.id.bookButton);
        addressEditText = findViewById(R.id.addressEditText);
        orderconstrainntTotal = findViewById(R.id.totalOrderConstraintTextView);
        orderConstraintListView = findViewById(R.id.orderConstraintListView);
        successConstraint = findViewById(R.id.successConstraint);
        successConstraint.setVisibility(View.INVISIBLE);

        final Intent i = getIntent();
        failedConstraint=findViewById(R.id.failedConstraint);
        failedConstraint.setVisibility(View.INVISIBLE);
        merchantuser = i.getStringExtra("username");
        shop = i.getStringExtra("shop");
        location=i.getStringExtra("location");
        Log.i("username", merchantuser);
        this.setTitle(shop);

        itemListView = findViewById(R.id.itemListView);
        orderedListView = findViewById(R.id.orderListView);
        totalTextView = findViewById(R.id.totalPriceTextView);
        adapter = new myAdapter(getApplication(), itemNameArray, itemPriceArray);
        orderadapter = new myAdapter(getApplication(), orderItemNameArray, orderItemPriceArray);
        ParseQuery<ParseObject> query = ParseQuery.getQuery(merchantuser + "Item");
        Utility.setListViewHeightBasedOnChildren(orderedListView);
        Utility.setListViewHeightBasedOnChildren(itemListView);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseObject object : objects) {
                        Log.i("itemName = ", object.getString("itemName"));
                        Log.i("itemPrice = ", object.getString("itemPrice"));
                        itemNameArray.add(object.getString("itemName"));
                        itemPriceArray.add(object.getString("itemPrice"));
                    }
                    failedConstraint.setVisibility(View.INVISIBLE);
                    itemListView.setAdapter(adapter);
                    // adding item to slect list
                    itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(OrderActivity.this, "item added", Toast.LENGTH_SHORT).show();
                            orderItemNameArray.add(itemNameArray.get(position));
                            orderItemPriceArray.add(itemPriceArray.get(position));
                            totalArray.add(Integer.parseInt(itemPriceArray.get(position)));

                           total=total+Integer.parseInt(itemPriceArray.get(position));
                            totalTextView.setText(String.valueOf(total + 300));

                            orderedListView.setAdapter(orderadapter);

                        }
                    });
                    orderedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(OrderActivity.this, "long press to delete item", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //removing item from slect listView
                    orderedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            int r = Integer.parseInt(totalTextView.getText().toString());
                            int sum = r - Integer.parseInt(orderItemPriceArray.get(position));
                            orderItemNameArray.remove(position);
                            totalArray.remove(position);
                            orderItemPriceArray.remove(position);


                            orderedListView.setAdapter(orderadapter);

                            totalTextView.setText(String.valueOf(sum));

                            return true;
                        }
                    });

                    Log.i("adapter", "set success");
                } else {
                 failedConstraint.setVisibility(View.VISIBLE);
                 itemConstraintLayout.setVisibility(View.GONE);
                }
            }
        });


    }

    // adapter
    class myAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> itemNameArray = new ArrayList<String>();
        ArrayList<String> itemPriceArray = new ArrayList<String>();
        ImageView imageView;

        myAdapter(Context c, ArrayList<String> itemNameArray, ArrayList<String> itemPriceArray) {
            super(c, R.layout.order_custom, R.id.itemNameTextView, itemNameArray);
            this.context = c;
            this.itemNameArray = itemNameArray;
            this.itemPriceArray = itemPriceArray;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplication().getSystemService(context.LAYOUT_INFLATER_SERVICE);
            View item = layoutInflater.inflate(R.layout.order_custom, parent, false);
            TextView mitem = item.findViewById(R.id.itemNameTextView);
            TextView mprice = item.findViewById(R.id.itemPriceTextView);

            mitem.setText(itemNameArray.get(position));
            mprice.setText(itemPriceArray.get(position));
            Log.i("adapter set", "success");


            return item;
        }
    }

    // avoid over lapping of ListView
    public static class Utility {

        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
}
