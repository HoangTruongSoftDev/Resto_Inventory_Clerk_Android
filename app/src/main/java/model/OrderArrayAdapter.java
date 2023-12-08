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

public class OrderArrayAdapter  extends ArrayAdapter<Order> {
    private List<Order> orders;
    private Context context;
    public OrderArrayAdapter(Context context, int resource, List<Order> orders) {
        super(context, resource, orders);
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.single_order_layout, parent, false);
        }

        Order item = getItem(position);

        if (item != null) {
            TextView tvOrderId = convertView.findViewById(R.id.tvOrderId);
            TextView tvItemName = convertView.findViewById(R.id.tvItemName);
            TextView tvQuantity = convertView.findViewById(R.id.tvQuantity);
            TextView tvPrice = convertView.findViewById(R.id.tvPrice);
            TextView tvTotalPrice = convertView.findViewById(R.id.tvTotalPrice);

            tvOrderId.setText(item.getOrderId());
            tvItemName.setText(item.getItemName());
            tvQuantity.setText(item.getQuantities());
            tvPrice.setText(item.getUnitPrice().toString());
            tvTotalPrice.setText(""+item.getTotalPrice());
        }

        return convertView;
    }
}
