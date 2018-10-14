package langotec.numberq.store;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import langotec.numberq.store.login.LoginActivity;
import langotec.numberq.store.login.Member;
import langotec.numberq.store.map.PhpDB;
import langotec.numberq.store.menu.MainOrder;

public class WelcomeActivity extends AppCompatActivity {

    private CountDownTimer timer;
    private Context context;
    private PhpDB phpDB;
    private JSONArray ja;
    private ArrayList<MainOrder> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;

        getOrderData();
    }

    private void countDownTimer() {
        timer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (Member.getInstance().checkLogin(context)) {
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    intent.putExtra("orderList",orderList);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(context, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }

    private void getOrderData(){
//        phpDB = new PhpDB(context,hd);
//        phpDB.getPairSet().setPairFunction("orderList");
//        phpDB.getPairSet().setPairSearch(1,"o201810020003");
//        Log.e("等待資料回應:", new Date().toString());
//        new Thread(phpDB).start();
        phpDB = new PhpDB(context, hd);
        phpDB.getPairSet().setPairFunction("orderMSList");
        phpDB.getPairSet().setPairSearch(4, "2054");//第四個欄位,branchId
        phpDB.getPairSet().setPairJSON();
        Log.e("等待資料回應:", new Date().toString());
        new Thread(phpDB).start();

    }

    protected Handler hd = new hdsub();
    class hdsub extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e("", "Handler 發送過來的訊息：" + msg.obj);

            if (phpDB.getState()) {
                Log.e("資料回應時間", new Date().toString());
                Log.e("回應副程式", phpDB.getPairFunction());
                String tmp;
                if (!phpDB.isJSON()) {
                    for (int y = 0; y < phpDB.getRowSize(); y++) {
                        tmp = "";
                        for (Object key : ((PhpDB.ItemListRow) phpDB.getDataSet().get(y)).getAll().keySet()) {
                            tmp = tmp + key.toString() + "=" + ((PhpDB.ItemListRow) phpDB.getDataSet().get(y)).get(key.toString()).toString() + "  ";
                        }
                        Log.e("=========Debug=======", tmp);
                    }
                } else if (phpDB.isJSON()) {
                    ja = phpDB.getJSONData();
                    if (ja.length()>0){
                        for (int i = 0; i < ja.length(); i++) {
                            try {
                                JSONObject jsObj = ja.getJSONObject(i);
                                String orderId = jsObj.getString("orderId");
                                String userId = jsObj.getString("userId");
                                String HeadId = jsObj.getString("HeadId");
                                String BranchId = jsObj.getString("BranchId");
                                String deliveryType = jsObj.getString("deliveryType");
                                String contactPhone = jsObj.getString("contactPhone");
                                String deliveryAddress = jsObj.getString("deliveryAddress");
                                String taxId = jsObj.getString("taxId");
                                String payWay = jsObj.getString("payWay");
                                int payCheck = Integer.parseInt(jsObj.getString("payCheck"));
                                int totalPrice = Integer.parseInt(jsObj.getString("totalPrice"));
                                String comment = jsObj.getString("comment");
//                            String userName = jsObj.getString("userName");
                                String orderDT = jsObj.getString("orderDT");
                                Calendar orderDTc = parseDate(orderDT);
                                String orderGetDT = jsObj.getString("orderGetDT");
                                Calendar orderGetDTc = parseDate(orderGetDT);
                                String HeadName = jsObj.getString("HeadName");
                                String BranchName = jsObj.getString("BranchName");
                                int quantity = jsObj.optInt("quantity");
                                int sumprice = jsObj.optInt("sumprice");
                                String productType = jsObj.optString("productType");
                                String productName = jsObj.optString("productName");
                                String image = jsObj.optString("image");
                                int available = jsObj.optInt("available");
                                int waitingTime = jsObj.optInt("waitingTime");
                                String description = jsObj.optString("description");
                                MainOrder mainOrder = new MainOrder(
                                        orderId,
                                        userId,
                                        HeadId,
                                        BranchId,
                                        deliveryType,
                                        contactPhone,
                                        deliveryAddress,
                                        taxId,
                                        payWay,
                                        payCheck,
                                        totalPrice,
                                        comment,/*
                                    String userName,*/
                                        orderDTc,
                                        orderGetDTc,
                                        HeadName,
                                        BranchName,
                                        quantity,
                                        sumprice,
                                        productType,
                                        productName,
                                        image,
                                        available,
                                        waitingTime,
                                        description);
                                orderList.add(mainOrder);
                                Log.e("JSON DATA", ja.get(i).toString());
                            } catch (JSONException e) {
                                Log.e("JSON ERROR", e.toString());
                            }
                        }
                        Log.e("orderlist","order List size:"+orderList.size());
                    }
                }
            }
            countDownTimer();
        }
        private Calendar parseDate(String orderDT){
            //先分" "
            String[] split1 = orderDT.split(" ");
            for(String s : split1)
                Log.d("Calendar Parse",s);
            //再分"-"
            String[] split2 = split1[0].split("-");
            for(String s : split2)
                Log.d("Calendar Parse",s);
            int YY = Integer.parseInt(split2[0]);
            int MM = Integer.parseInt(split2[1]);
            int DD = Integer.parseInt(split2[2]);
            //再分":"
            String[] split3 = split1[1].split(":");
            for(String s : split3)
                Log.d("Calendar Parse",s);
            int hh = Integer.parseInt(split3[0]);
            int mm = Integer.parseInt(split3[1]);
            int ss = Integer.parseInt(split3[2]);
            Calendar c = Calendar.getInstance();
            c.set(YY,MM,DD,hh,mm,ss);
            return c;
        }

    }
}
