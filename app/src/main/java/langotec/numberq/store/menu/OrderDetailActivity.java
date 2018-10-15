package langotec.numberq.store.menu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
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
        TextView orderUserName = findViewById(R.id.order_user_name);
        TextView orderContactPhone = findViewById(R.id.order_contactPhone);
        TextView orderDeliveryType = findViewById(R.id.order_delivery_type);
        TextView orderTotalPrice = findViewById(R.id.order_totalPrice);
        LinearLayout ll1 = findViewById(R.id.ll1);
        LinearLayout ll2 = findViewById(R.id.ll2);

        orderDT.setText(mainOrder.getOrderDT().getTime().toString());
        orderUserName.setText("訂購者：" + mainOrder.getUserName());
        orderDeliveryType.setText("訂購模式：" + mainOrder.getDeliveryType());
        orderTotalPrice.setText("總金額：" + mainOrder.getTotalPrice());
        orderContactPhone.setText("電話：" + mainOrder.getContactPhone());

        for (int i=0;i<mainOrder.getProductName().size();i++){
            TextView tv1 = new TextView(this);
            tv1.setText(i+1+". "+mainOrder.getProductName().get(i));
            tv1.setTextSize(20);
            tv1.setId(i);
            ll1.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText("   數量:"+mainOrder.getQuantity().get(i));
            tv2.setTextSize(20);
            tv2.setId(i);
            ll2.addView(tv2);
        }
    }
}
