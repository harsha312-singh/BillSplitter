package com.example.billsplitter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class contacts extends AppCompatActivity {

    ListView listView;
    TextView textView2;
    static TextView textView3;
    SearchView searchView;
    ListViewAdapter adapter;
    String[] namecollection;
    String[] phonecollection;
    groupName gp = new groupName();
    static String st;
    ArrayList<Model> arrayList = new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        st = getIntent().getStringExtra("gp");
        Toast.makeText(contacts.this, st, Toast.LENGTH_SHORT).show();
        listView = findViewById(R.id.listView);
        textView2 = findViewById(R.id.textView7);
        textView3 = findViewById(R.id.textView10);
        searchView = findViewById(R.id.search_bar);
        textView2.setText(st);
        namecollection = new String[1000];
        phonecollection = new String[1000];

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int j = 0;
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{id}, null);
            //Log.i("MY INFO", id + " = " +name);
            namecollection[j] = name;
            while (phoneCursor.moveToNext()) {
                String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Log.i("MY INFO",phoneNumber);
                phonecollection[j] = phoneNumber;
                break;
            }
            j++;
        }

        for (int i = 0; i < j; i++) {
            Model model = new Model(namecollection[i], phonecollection[i]);
            arrayList.add(model);
        }

        adapter = new ListViewAdapter(this, arrayList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                if (TextUtils.isEmpty(newText)) {
                    adapter.filter("");
                    listView.clearTextFilter();
                } else {
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }
    public void remove(View view){
        adapter.rem();
        textView3.setText("");
    }
}

