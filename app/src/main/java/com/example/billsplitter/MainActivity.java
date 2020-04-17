package com.example.billsplitter;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= findViewById(R.id.editText);
    }

    public void OTPPage(View v)
    {
        String phoneNo="+91"+editText.getText().toString();
        if(phoneNo.length()<=12){
            Toast.makeText(MainActivity.this,"Enter correct number",Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(MainActivity.this, OTPActivity.class);
            i.putExtra("Details", phoneNo);
            startActivity(i);
        }
    }
}
