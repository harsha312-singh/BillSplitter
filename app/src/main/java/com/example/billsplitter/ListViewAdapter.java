package com.example.billsplitter;

import android.content.Context;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Model> modelList;
    ArrayList<Model> arrayList;
    ArrayList<String> member;
    groupName gp = new groupName();
    String s = contacts.st;
    String selected;
    String m;
    String gpmember;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference phoneNoReference = mRootReference.child("GroupName");

    public ListViewAdapter(Context context, List<Model> modelList) {
        mContext = context;
        this.modelList = modelList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modelList);
        this.member = new ArrayList<String>();
    }

    public class ViewHolder {

        TextView mName, mPhone;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row, null);

            holder.mName = convertView.findViewById(R.id.textView8);
            holder.mPhone = convertView.findViewById(R.id.textView9);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (modelList.get(position).isSelected)
            convertView.setBackgroundColor(Color.parseColor("#FF0000"));
        // convertView.setSelected(true);

        holder.mPhone.setText(modelList.get(position).getNumber());
        holder.mName.setText(modelList.get(position).getName());

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selected = modelList.get(position).getNumber();
                m = modelList.get(position).getName();
                Toast.makeText(mContext, "" + selected, Toast.LENGTH_SHORT).show();
                store_in_db();

                return false;
            }
        });
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if (charText.isEmpty()) {
            modelList.addAll(arrayList);
        } else {
            for (Model model : arrayList) {
                if (model.getName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void store_in_db() {
        phoneNoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int check = 1;
                for (DataSnapshot phSnapshot : dataSnapshot.getChildren()) {
                    Log.i("correction", phSnapshot.child("number").getValue().toString());
                    if (selected.equals(phSnapshot.child("number").getValue().toString()) && s.equals(phSnapshot.child("gpname").getValue().toString())) {
                        check = 0;
                        break;
                    }
                }
                if (check == 1) {
                    gp.setGpname(s);
                    gp.setNumber(selected);
                    phoneNoReference.push().setValue(gp);
                    gpmember = contacts.textView3.getText().toString();
                    if (gpmember.isEmpty())
                        gpmember = m;
                    else
                        gpmember = gpmember + ", " + m;
                    member.add(selected);
                    String s = " " + member.size();
                    Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
                    contacts.textView3.setText(gpmember);
                    contacts.textView3.setMovementMethod(new ScrollingMovementMethod());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void rem (){
        String s=" "+member.size();
        //Toast.makeText(mContext,"Removing all "+s+" members",Toast.LENGTH_SHORT).show();
        //for(int i=0;i<member.size();i++){
        //    remove_from_db(member.get(i));
        //}
        member.clear();
        Toast.makeText(mContext,"Removing all "+member.size()+" members",Toast.LENGTH_SHORT).show();
    }
    public void remove_from_db(String phoneNo) {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        Query query = dr.child("GroupName").orderByChild("number").equalTo(phoneNo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot phSnapshot : dataSnapshot.getChildren()) {
                    phSnapshot.getRef().removeValue();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}