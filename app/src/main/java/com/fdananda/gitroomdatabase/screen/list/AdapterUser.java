package com.fdananda.gitroomdatabase.screen.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.fdananda.gitroomdatabase.R;
import com.fdananda.gitroomdatabase.model.User;

import java.util.ArrayList;

public class AdapterUser extends ArrayAdapter<User> {

    private Context context;
    private final ArrayList<User> users;

    public AdapterUser(Context context, ArrayList<User> users){
        super(context, R.layout.list_users_adapter, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_users_adapter, parent, false);

        TextView textViewPosition   = view.findViewById(R.id.textViewPosition);
        TextView textViewName       = view.findViewById(R.id.textViewName);
        TextView textViewSurname    = view.findViewById(R.id.textViewSurname);
        TextView textViewAge        = view.findViewById(R.id.textViewAge);

        textViewPosition.setText(String.valueOf(users.get(position).getId()));
        textViewName.setText(users.get(position).getName());
        textViewSurname.setText(users.get(position).getSurname());
        textViewAge.setText("(" + String.valueOf(users.get(position).getAge()) + " anos)");

        return view;
    }
}
