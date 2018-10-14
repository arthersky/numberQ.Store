package langotec.numberq.store.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import langotec.numberq.store.MainActivity;
import langotec.numberq.store.R;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ArrayList<MainOrder> orderList = MainActivity.orderList;
        MainOrder mainOrder = orderList.get(getIntent().getIntExtra("position",0));

        TextView orderDT = findViewById(R.id.order_orderDT);
//            TextView orderUserName = view.findViewById(R.id.order_user_name);
        TextView orderContactPhone = findViewById(R.id.order_contactPhone);
        TextView orderDeliveryType = findViewById(R.id.order_delivery_type);
        TextView orderTotalPrice = findViewById(R.id.order_totalPrice);
        TextView orderDetail = findViewById(R.id.order_order_list);

        orderDT.setText(mainOrder.getOrderDT().getTime().toString());
//            orderUserName.setText("訂購者：" + mainOrder.getUserName());
        orderDeliveryType.setText("訂購模式：" + mainOrder.getDeliveryType());
        orderTotalPrice.setText("總金額：" + mainOrder.getTotalPrice());
        orderContactPhone.setText("電話：" + mainOrder.getContactPhone());
        for (int i=1;i<=orderList.size();i++){
            orderDetail.setText(i+". "+mainOrder.getProductName()+"   數量:"+mainOrder.getQuantity());
        }
    }
}
