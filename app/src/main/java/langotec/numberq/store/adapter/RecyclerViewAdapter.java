package langotec.numberq.store.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import langotec.numberq.store.R;
import langotec.numberq.store.menu.MainOrder;
import langotec.numberq.store.menu.OrderDetailActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList data;
    private Context context;

    public RecyclerViewAdapter(ArrayList data) {
        this.data = data;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
        TextView text_orderDT;
        TextView text_orderUserName;
        TextView text_orderContactPhone;
        TextView text_orderDeliveryType;
        TextView text_orderTotalPrice;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (data.get(0) instanceof MainOrder) {
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cardview_mainorder_row, parent, false
            );
        }
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View view = holder.itemView;
        context = view.getContext();

        if (data.get(0) instanceof MainOrder) {
            MainOrder mainOrder = (MainOrder) data.get(position);

            holder.text_orderDT = view.findViewById(R.id.order_orderDT);
            holder.text_orderUserName = view.findViewById(R.id.order_user_name);
            holder.text_orderContactPhone = view.findViewById(R.id.order_contactPhone);
            holder.text_orderDeliveryType = view.findViewById(R.id.order_delivery_type);
            holder.text_orderTotalPrice = view.findViewById(R.id.order_totalPrice);

            Date orderDT = mainOrder.getOrderDT().getTime();
            holder.text_orderDT.setText(
                    context.getString(R.string.order_createTime) +
                    String.format("\n%tF", orderDT) +
                    String.format(" %tT\n", orderDT));
            holder.text_orderUserName.setText(context.getString(R.string.order_subscriber) +
                    mainOrder.getUserName());
            holder.text_orderContactPhone.setText(context.getString(R.string.order_phone) +
                    mainOrder.getContactPhone());
            holder.text_orderDeliveryType.setText(context.getString(R.string.order_pattern) +
                    mainOrder.getDeliveryType());
            holder.text_orderTotalPrice.setText(context.getString(R.string.order_total) +
                    mainOrder.getTotalPrice());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainOrder mainOrder = (MainOrder) data.get(position);
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("position",position);
                    ((Activity)context).startActivityForResult(intent, 1);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
