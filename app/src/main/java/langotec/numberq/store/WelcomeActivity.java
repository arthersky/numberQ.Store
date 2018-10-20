package langotec.numberq.store;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import langotec.numberq.store.dbConnect.parseJSON;
import langotec.numberq.store.fragment.OrderFinishFragment;
import langotec.numberq.store.fragment.OrderListFragment;
import langotec.numberq.store.login.LoginActivity;
import langotec.numberq.store.login.Member;
import langotec.numberq.store.map.PhpDB;
import langotec.numberq.store.menu.MainOrder;
import langotec.numberq.store.menu.OrderDetailActivity;

public class WelcomeActivity extends AppCompatActivity {

    private Context context;
    private static WeakReference<Context> weakReference;
    public static PhpDB phpDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        context = this;
        weakReference = new WeakReference<>(context);
        checkMember();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkMember();
    }

    private void checkMember(){
        if (!Member.getInstance().checkLogin(context)) {
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            ((Activity)context).startActivityForResult(intent, 1);
        }else getOrderData("WelcomeActivity");
    }

    public static void getOrderData(String from){
        MainActivity.orderList = new ArrayList<>();
        MainActivity.orderFinishList = new ArrayList<>();
        Member member = findMemberFile();
        phpDB = new PhpDB(weakReference, new OrderHandler(weakReference, from));
        phpDB.getPairSet().setPairFunction(phpDB.pairSet.phpSQLorderMSList);
        phpDB.getPairSet().setPairSearch(4, member.getBranchId());//第四個欄位,branchId
        phpDB.getPairSet().setPairOkHTTP();
        phpDB.getPairSet().setPairJSON();
        Log.e("等待資料回應:", new Date().toString());
        new Thread(phpDB).start();
    }

    public static class OrderHandler extends Handler {
        ArrayList<MainOrder> orderList = MainActivity.orderList;
        ArrayList<MainOrder> orderFinishList = MainActivity.orderFinishList;
        JSONArray ja;
        Context context;
        String from;
        int index = 0;
        public OrderHandler(WeakReference weakReference, String from) {
            context = (Context) weakReference.get();
            this.from = from;
        }
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
                            tmp = tmp + key.toString() + "=" +
                                    ((PhpDB.ItemListRow) phpDB.getDataSet().get(y)).get(key.toString()).toString() + "  ";
                        }
                        Log.e("=========Debug=======", tmp);
                    }
                } else if (phpDB.isJSON()) {
                    ja = phpDB.getJSONData();
                    for (int i = 0; i < ja.length(); i++) {
                        boolean flag = false;
                        try {
                            JSONObject jsObj = ja.getJSONObject(i);
                            Log.e("jsObj", jsObj.toString());
                            String orderId = jsObj.optString("orderId");
                            String productName = jsObj.optString("productName");
                            String quantity = jsObj.optString("quantity");
                            String sumprice = jsObj.optString("sumPrice");
                            String productType = jsObj.optString("productType");
                            String image = jsObj.optString("image");
                            for (int i2 = 0; i2 < orderList.size(); i2++){
                                MainOrder indexOrder = orderList.get(i2);
                                if (indexOrder.getOrderId().equals(orderId)){
                                    indexOrder.getProductName().add(productName);
                                    indexOrder.getQuantity().add(quantity);
                                    indexOrder.getSumprice().add(sumprice);
                                    flag = true;
                                    index++;
                                    if (index + orderList.size() +
                                            orderFinishList.size()  == ja.length()) {
                                        setActivity();
                                    }
                                    break;
                                }
                            }
                            for (int i2 = 0; i2 < orderFinishList.size(); i2++){
                                MainOrder indexOrder = orderFinishList.get(i2);
                                if (indexOrder.getOrderId().equals(orderId) &&
                                        indexOrder.getPayCheck() == 4){
                                    indexOrder.getProductName().add(productName);
                                    indexOrder.getQuantity().add(quantity);
                                    indexOrder.getSumprice().add(sumprice);
                                    flag = true;
                                    index++;
                                    if (index + orderList.size() +
                                        orderFinishList.size()  == ja.length()) {
                                       setActivity();
                                    }
                                    break;
                                }
                            }
                            if (flag)
                                continue;
                            String userId = jsObj.optString("userId");
                            String HeadId = jsObj.optString("HeadId");
                            String BranchId = jsObj.optString("BranchId");
                            String deliveryType = jsObj.optString("deliveryType");
                            String contactPhone = jsObj.optString("contactPhone");
                            String deliveryAddress = jsObj.optString("deliveryAddress");
                            String taxId = jsObj.optString("taxId");
                            String payWay = jsObj.optString("payWay");
                            int payCheck = Integer.parseInt(jsObj.optString("payCheck"));
                            int totalPrice = Integer.parseInt(jsObj.optString("totalPrice"));
                            String comment = jsObj.optString("comment");
                            String userName = jsObj.optString("userName");
                            String orderDT = jsObj.optString("orderDT");
                            Calendar orderDTc = parseDate(orderDT);
                            String orderGetDT = jsObj.optString("orderGetDT");
                            Calendar orderGetDTc = parseDate(orderGetDT);
                            String HeadName = jsObj.optString("HeadName");
                            String BranchName = jsObj.optString("BranchName");
                            int available = jsObj.optInt("available");
                            int waitingTime = jsObj.optInt("waitingTime");
                            String description = jsObj.optString("description");
                            MainOrder mainOrder = new MainOrder(orderId, userId, HeadId, BranchId,
                                    deliveryType, contactPhone, deliveryAddress, taxId, payWay,
                                    payCheck, totalPrice, comment, userName, orderDTc, orderGetDTc,
                                    HeadName, BranchName, productType, image, available, waitingTime,
                                    description);
                            mainOrder.getProductName().add(productName);
                            mainOrder.getQuantity().add(quantity);
                            mainOrder.getSumprice().add(sumprice);
                            if (mainOrder.getPayCheck() == 4)
                                orderFinishList.add(mainOrder);
                            else
                                orderList.add(mainOrder);
                        } catch (JSONException e) {
                            Log.e("JSON ERROR", e.toString());
                        }
                    }
                    if (index + orderList.size() +
                            orderFinishList.size()  == ja.length()) {
                        setActivity();
                    }
                }
            }
            Log.e("index", index + "," + orderList.size() + "," + orderFinishList.size());
            if (ja == null) {
                setActivity();
            }else if (from.equals("WelcomeActivity") && !phpDB.getState()){
                showDialog();
            }
        }
        private Calendar parseDate(String orderDT){
            //先分" "
            String[] split1 = orderDT.split(" ");
            //再分"-"
            String[] split2 = split1[0].split("-");
            int YY = Integer.parseInt(split2[0]);
            int MM = Integer.parseInt(split2[1]);
            int DD = Integer.parseInt(split2[2]);
            //再分":"
            String[] split3 = split1[1].split(":");
            int hh = Integer.parseInt(split3[0]);
            int mm = Integer.parseInt(split3[1]);
            int ss = Integer.parseInt(split3[2]);
            Calendar c = Calendar.getInstance();
            c.set(YY,MM,DD,hh,mm,ss);
            return c;
        }

        private void setActivity(){
            switch (from) {
                case "WelcomeActivity":
                    Intent intent = new Intent();
                    intent.setClass(context, MainActivity.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    break;
                case "OrderDetailActivity":
                    OrderDetailActivity.showDialog("createFinish");
                    break;
                case "OrderListFragment":
                    OrderListFragment.refreshOrder();
                    break;
                case "OrderFinishListFragment":
                    OrderFinishFragment.refreshOrder();
                    break;
                case "MainActivity":
                    OrderDetailActivity.loadingDialog.closeDialog();
                    OrderListFragment.refreshOrder();
                    OrderFinishFragment.refreshOrder();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //延遲100毫秒避免畫面更新異常
                            getOrderData("OrderListFragment");
                        }
                    }, 100);
                    break;
                default:
                    break;
            }
        }
    }

    public static Member findMemberFile() {
        File fileDir = new File(String.valueOf(weakReference.get().getFilesDir()) +
                "/storeUser.txt");
        String json = "";
        Member member = Member.getInstance();
        try {
            FileReader fileReader = new FileReader(fileDir);
            BufferedReader bReader = new BufferedReader(fileReader);
            json = bReader.readLine();
            bReader.close();
            Log.e("Member_json", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new parseJSON(json, member).parse();
    }

    private static void showDialog() {
        Context context = weakReference.get();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.connFail_noConn))
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage(context.getString(R.string.connFail_check))
                .setPositiveButton(context.getString(R.string.connFail_retry),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getOrderData("WelcomeActivity");
                            }
                        })
                .setNegativeButton("離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((Activity)weakReference.get()).finish();
                    }
                }).create().show();
    }
}
