package model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.resto_inventory_clerk_android.R;

import java.util.List;

public class ItemArrayAdapter extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> items;

    public ItemArrayAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_item_layout, parent, false);
        }

        Item item = getItem(position);

        if (item != null) {
            TextView tvItemId = convertView.findViewById(R.id.tvItemId);
            TextView tvItemName = convertView.findViewById(R.id.tvItemName);
            TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);

            tvItemId.setText(String.valueOf(item.getItemId()));
            tvItemName.setText(item.getName());
            tvQuantity.setText(String.valueOf(item.getQuantity()));
        }

        return convertView;
    }
}

