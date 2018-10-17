package langotec.numberq.store.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;

import langotec.numberq.store.MainActivity;
import langotec.numberq.store.R;
import langotec.numberq.store.WelcomeActivity;
import langotec.numberq.store.map.PhpDB;

public class OrderDetailActivity extends AppCompatActivity {

    private static WeakReference<Context> weakReference;
    private static MainOrder mainOrder;
    private static LoadingDialog loadingDialog;
    private static AlertDialog alertDialog;
    public static boolean orderCreated, allowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        allowBack = true;
        weakReference = new WeakReference<>(context);
        setContentView(R.layout.activity_order_detail);
        setLayout();

    }

    @Override
    public void onBackPressed() {
        if (allowBack)
            super.onBackPressed();
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public static void setLayout(){
        Activity activity = ((Activity)weakReference.get());
        ArrayList<MainOrder> orderList = MainActivity.orderList;
        mainOrder = orderList.get(activity.getIntent().getIntExtra("position",0));

        TextView text_orderDT = activity.findViewById(R.id.order_orderDT);
        TextView text_orderUserName = activity.findViewById(R.id.order_user_name);
        TextView text_orderContactPhone = activity.findViewById(R.id.order_contactPhone);
        TextView text_orderDeliveryType = activity.findViewById(R.id.order_delivery_type);
        TextView text_orderTotalPrice = activity.findViewById(R.id.order_totalPrice);
        Button button_changeStatic = activity.findViewById(R.id.button_changeStatic);
        button_changeStatic.setOnClickListener(new ButtonClickListener());
        LinearLayout ll1 = activity.findViewById(R.id.ll1);
        LinearLayout ll2 = activity.findViewById(R.id.ll2);

        Date orderDT = mainOrder.getOrderDT().getTime();
        Date orderGetDT = mainOrder.getOrderGetDT().getTime();
        text_orderDT.setText(activity.getString(R.string.order_createTime) +
                String.format("\n%tF", orderDT) +
                String.format(" %tT\n\n", orderDT) +
                activity.getString(R.string.order_finishTime) +
                String.format("\n%tF", orderGetDT) +
                String.format(" %tT\n", orderGetDT));
        text_orderUserName.setText("訂購者：" + mainOrder.getUserName());
        text_orderDeliveryType.setText("訂購模式：" + mainOrder.getDeliveryType());
        text_orderTotalPrice.setText("總金額：" + mainOrder.getTotalPrice());
        text_orderContactPhone.setText("電話：" + mainOrder.getContactPhone());
        if (mainOrder.getPayCheck() == 2) {
            button_changeStatic.setBackgroundColor(activity.getResources().getColor(R.color.QSecondary300));
            button_changeStatic.setText(activity.getText(R.string.order_done));
        }else if (mainOrder.getPayCheck() != 1 && mainOrder.getPayCheck() != 2){
            button_changeStatic.setVisibility(View.GONE);
        }
        ll1.removeAllViews();
        ll2.removeAllViews();
        for (int i=0;i<mainOrder.getProductName().size();i++){
            TextView tv1 = new TextView(activity);
            tv1.setText(i+1+". "+mainOrder.getProductName().get(i));
            tv1.setTextSize(20);
            tv1.setId(i);
            ll1.addView(tv1);
            TextView tv2 = new TextView(activity);
            tv2.setText("   數量:"+mainOrder.getQuantity().get(i));
            tv2.setTextSize(20);
            tv2.setId(i);
            ll2.addView(tv2);
        }
    }

    public static void orderChangeState(MainOrder mainOrder,
                                        String from, WeakReference<Context> weakReference){
        allowBack = false;
        loadingDialog = new LoadingDialog(weakReference);
        loadingDialog.setMessage((weakReference.get().getString(R.string.checkOut_creatingOrders)));
        PhpDB phpDB = new PhpDB(weakReference, new OrderHandler(weakReference, from));
        phpDB.getPairSet().setPairFunction(phpDB.pairSet.phpSQLsetOrderUpdate);
        phpDB.getPairSet().setPairSearch(1, mainOrder.getOrderId());
        Log.e("orderId", mainOrder.getOrderId());
        //PayCheck設定
        if (from.equals("OrderDetailActivity"))
            phpDB.getPairSet().setPairSearch(10, String.valueOf(mainOrder.getPayCheck() + 1));
        else if (from.equals("MainActivity"))
            phpDB.getPairSet().setPairSearch(10, "4");
        new Thread(phpDB).start();
    }

    private static class ButtonClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            orderChangeState(mainOrder, "OrderDetailActivity", weakReference);
        }
    }
    private static class OrderHandler extends Handler{
        WeakReference<Context> handlerWeakReference;
        String from;
        OrderHandler(WeakReference<Context> handlerWeakReference, String from){
            this.handlerWeakReference = handlerWeakReference;
            this.from = from;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PhpDB tmpDB = ((PhpDB)(msg.obj));
            if(tmpDB.getState()) {
                String tmp = "";
                for (int y = 0; y < tmpDB.getRowSize(); y++) {
                    for (Object key : ((PhpDB.ItemListRow) tmpDB.getDataSet().get(y)).getAll().keySet()) {
                        tmp += ((PhpDB.ItemListRow) tmpDB.getDataSet().get(y)).get(key.toString()).toString();
                    }
                    if (tmpDB.getPairFunction().equals(tmpDB.getPairSet().phpSQLsetOrderUpdate)) {
                        if (tmp.equals("true")) {
                            WelcomeActivity.getOrderData(from);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showDialog("createFinish", handlerWeakReference);
                                }
                            }, 1000);

                        } else {
                            showDialog("createFailure", handlerWeakReference);
                        }
                    }
                }
            }else {
                showDialog("createFailure", handlerWeakReference);
            }
        }
    }

    public static void showDialog(String type, final WeakReference<Context> weakReference) {
        final Context dialogContext = weakReference.get();
        final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false);
        if (type.equals("createFinish")) {//順利更新訂單後的顯示
            builder.setTitle(dialogContext.getString(R.string.checkOut_createOrderSuccess))
                    .setPositiveButton(dialogContext.getString(R.string.menu_confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    allowBack = true;
                                    orderCreated = false;
                                    alertDialog.dismiss();
                                    loadingDialog.closeDialog();
                                }
                            });
        }else if (type.equals("createFailure")) {//更新訂單失敗的顯示
            builder.setTitle(dialogContext.getString(R.string.checkOut_createOrderFailure))
                    .setMessage(dialogContext.getString(R.string.checkOut_createOrderRetry))
                    .setPositiveButton(dialogContext.getString(R.string.menu_cancel),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    allowBack = true;
                                    orderCreated = false;
                                    alertDialog.dismiss();
                                    loadingDialog.closeDialog();
                                }
                            });
        }
        alertDialog = builder.create();
        if (!alertDialog.isShowing())
            alertDialog.show();
    }
}
