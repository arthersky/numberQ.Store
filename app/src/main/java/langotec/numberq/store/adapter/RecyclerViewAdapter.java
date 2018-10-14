package langotec.numberq.store.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import langotec.numberq.store.R;
import langotec.numberq.store.menu.MainOrder;
import langotec.numberq.store.menu.Order;
import langotec.numberq.store.menu.OrderDetailActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList data;
    private Context context;

    public RecyclerViewAdapter(ArrayList data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
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

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        View view = holder.itemView;
        context = view.getContext();

        if (data.get(0) instanceof MainOrder) {
            MainOrder mainOrder = (MainOrder) data.get(position);

            TextView orderDT = view.findViewById(R.id.order_orderDT);
//            TextView orderUserName = view.findViewById(R.id.order_user_name);
            TextView orderContactPhone = view.findViewById(R.id.order_contactPhone);
            TextView orderDeliveryType = view.findViewById(R.id.order_delivery_type);
            TextView orderTotalPrice = view.findViewById(R.id.order_totalPrice);

            orderDT.setText(mainOrder.getOrderDT().getTime().toString());
//            orderUserName.setText("訂購者：" + mainOrder.getUserName());
            orderDeliveryType.setText("訂購模式：" + mainOrder.getDeliveryType());
            orderTotalPrice.setText("總金額：" + mainOrder.getTotalPrice());
            orderContactPhone.setText("電話：" + mainOrder.getContactPhone());

            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    MainOrder mainOrder = (MainOrder) data.get(position);
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
