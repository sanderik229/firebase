package com.example.firebasep7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {




    private Context context;
    private List<User> users;


    public UserAdapter(@NonNull Context context, List<User> userList) {
        super(context, R.layout.user_item);
        this.users=userList;
        this.context=context;
    }
    public View getView(int position, View convertView, ViewGroup parent){
                if(convertView == null){
                    convertView= LayoutInflater.from(context).inflate(R.layout.user_item,parent, false);

                }
                User user = users.get(position);
        TextView= userEmail= convertView.findViewById(R.id.userEmail);
        TextView= userRole= convertView.findViewById(R.id.userRole);

        userEmail.setText(user.getEmail());
        userRole.setText(user.userRole());
        return convertView;
    }

}
