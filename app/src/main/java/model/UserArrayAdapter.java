package model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.resto_inventory_clerk_android.R;

import java.util.ArrayList;

public class UserArrayAdapter extends BaseAdapter {
    Context context;
    int layoutId;
    ArrayList<User> listOfUsers;
    ArrayList<Employee> listOfEmployees;
    LayoutInflater inflater;


    public UserArrayAdapter(Context context, int layoutId,  ArrayList<User> listOfUsers, ArrayList<Employee> listOfEmployees) {
        this.context = context;
        this.layoutId = layoutId;
        this.listOfUsers = listOfUsers;
        this.listOfEmployees = listOfEmployees;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return listOfEmployees.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView = inflater.inflate(layoutId, null);

            Employee employee = listOfEmployees.get(position);
           User user = listOfUsers.stream().filter(u -> u.getUserId() == employee.getEmployeeId()).findFirst().orElse(null);
            TextView tvEmployeeId = convertView.findViewById(R.id.tvEmployeeId),
                    tvFullName = convertView.findViewById(R.id.tvFullName),
                    tvPosition = convertView.findViewById(R.id.tvPosition);
            tvEmployeeId.setText(String.valueOf(user.getUserId()));
        tvFullName.setText(employee.getFirstName() + " " + employee.getLastName());
            tvPosition.setText(user.getPosition());
            return convertView;


    }
}
