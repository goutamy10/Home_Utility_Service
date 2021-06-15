package com.example.homeutilityservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

public class AccountFragment extends Fragment {
    TextView nameTextView;
    TextView phoneNumber;
    TextView emailTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_account, container, false);
        nameTextView=view.findViewById(R.id.nameTextView);
        phoneNumber=view.findViewById(R.id.phoneNumberTextView);
        emailTextView=view.findViewById(R.id.emialTex);
        phoneNumber.setText(ParseUser.getCurrentUser().getString("phoneNumber"));

        nameTextView.setText(ParseUser.getCurrentUser().getString("name"));

        emailTextView.setText(ParseUser.getCurrentUser().getUsername());

        return view;
    }
}
