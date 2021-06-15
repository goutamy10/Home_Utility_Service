package com.example.homeutilityservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.validation.Validator;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout signupOrLogin;
    ConstraintLayout signUpPage;
    ConstraintLayout loginpage;
    EditText firstName;
    EditText lastName;
    EditText phoneNumber;
    EditText email;
    EditText password;
    Button signup;
    private static  String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Sign up Textview
    public  void signupTextView(View view){
        ParseUser.logOut();
        firstName.setText(null);
        lastName.setText(null);
        phoneNumber.setText(null);
        password.setText(null);
        email.setText(null);
        signupOrLogin.setVisibility(View.INVISIBLE);
        loginpage.setVisibility(View.INVISIBLE);
        signUpPage.setVisibility(View.VISIBLE);
    }
    // already account
    public void already(View view){
        ParseUser.logOut();
        EditText username=findViewById(R.id.loginPageUserNameEditText);
        EditText password=findViewById(R.id.loginPasswordEditText);
        username.setText(null);
        username.setText(null);
        loginpage.setVisibility(View.VISIBLE);
        signUpPage.setVisibility(View.INVISIBLE);
    }
    // login textview
    public void loginTextView(View view){
        ParseUser.logOut();
        loginpage.setVisibility(View.VISIBLE);
        signUpPage.setVisibility(View.INVISIBLE);
        signupOrLogin.setVisibility(view.INVISIBLE);
    }
    public static Boolean emailValidator(String email){
            if (email == null) {
                return false;
            }
            Matcher matcher = EMAIL_PATTERN.matcher(email);
            return matcher.matches();

    }

    // SignUp Button
    public void signUp(View view){
        ParseUser.logOut();
       int phone=phoneNumber.getText().toString().length();
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Please enter first name");
            firstName.requestFocus();
        }else if (lastName.getText().toString().isEmpty()) {
            lastName.setError("Please enter your last name");
            lastName.requestFocus();
        }else if (phone!=10){
            phoneNumber.setError("phone number should not be less than or more than 10");
            phoneNumber.requestFocus();
        }
        else if (email.getText().toString().isEmpty() || emailValidator(email.getText().toString()) == false) {
            email.setError("please enter valid e-mail Id");
            email.requestFocus();
        }else if (password.getText().toString().length()<6){
            password.setError("Length should be at least 6");
            password.requestFocus();
        }
        else {
            ParseUser User = new ParseUser();
            User.setUsername(email.getText().toString());
            User.put("name",firstName.getText().toString() + " " + lastName.getText().toString());
            User.setEmail(email.getText().toString());
            User.put("phoneNumber", phoneNumber.getText().toString());
            User.setPassword(password.getText().toString());
            Log.i("password",password.getText().toString());
            User.put("occupation", "user");
            User.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(getApplicationContext(), "Success fully Registered ", Toast.LENGTH_LONG).show();
                        signUpPage.setVisibility(View.INVISIBLE);
                        loginpage.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        Log.i("sign up", "Failed " + e.getMessage());
                    }
                }
            });
        }



    }

    // Login page Login button
    public void login(View view){
        EditText username=findViewById(R.id.loginPageUserNameEditText);
        EditText password=findViewById(R.id.loginPasswordEditText);
        ParseUser.logOut();
        if (username.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            username.setError("enter your E-mail id or phone number");
            username.requestFocus();
            password.setError("enter your password ");
            password.requestFocus();
        }else if (username.getText().toString().isEmpty()){
            username.setError("enter your E-mail id or phone number");
            username.requestFocus();
        }else if (password.getText().toString().isEmpty()){
            password.setError("enter your password ");
            password.requestFocus();
        }else {
            Log.i("login","successful");
            Log.i("password",password.getText().toString());
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user!=null){
                        Intent intent=new Intent(getApplicationContext(),UserActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        signupOrLogin = findViewById(R.id.signUpOrLoginConstraintLayout);
        signUpPage = findViewById(R.id.signUpConstraintLayout);
        loginpage = findViewById(R.id.loginPage);
        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        phoneNumber = findViewById(R.id.phoneEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.PasswordEditText);
        signup = findViewById(R.id.signUpButton);
        // Parse server connecter
        // ip =http://3.12.83.230:80/parse
        //id =  user
        // password = NzSpGagSm662
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("23f0b09424fcfa66573be436b1a8bebe57d915db")
                .clientKey("f3fdc0e183d71394ccca320fb42168735f1d1590")
                .server("http://3.12.83.230:80/parse/")
                .build()
        );
        if (ParseUser.getCurrentUser() != null) {
            Intent intent=new Intent(getApplicationContext(),UserActivity.class);
            startActivity(intent);

        }
        loginpage.setVisibility(View.INVISIBLE);
        signUpPage.setVisibility(View.INVISIBLE);
        signupOrLogin.setVisibility(View.VISIBLE);





    }
}
