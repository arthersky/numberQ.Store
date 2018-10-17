package langotec.numberq.store.dbConnect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import langotec.numberq.store.login.Member;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDBConn extends Thread {

    //private static CustomerDBConn customer;
    private static final String Q_SERVER ="https://ivychiang0304.000webhostapp.com/numberq/";
    private static final String USERLOGIN_PHP = "storelogin.php";
    private static final String USERINSERT_PHP = "userinsert.php";
    private static final String USERUPDATE_PHP = "userupdate.php";
    public static final String STATUS_LOGIN = "LOGIN";
    public static final String STATUS_INSERT = "INSERT";
    public static final String STATUS_UPDATE = "UPDATE";
    private final MediaType FORM_CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private String uid, email, password, name, phone, status;
    private File dir;
    private Handler handler;
    private String qResult;
    private Member storeAcnt = Member.getInstance();

    //==  Constructor   =============================================================
    //public CustomerDBConn1(){}
    public UserDBConn(Handler handler, File dir, String uid, String sname, String phone, String email, String pwd, String status) {
        this.handler = handler;
        this.dir = dir;
        this.uid = uid;
        this.name = sname;
        this.phone = phone;
        this.email = email;
        this.password = pwd;
        this.status = status;
    }

    @Override
    public void run(){
        String param = null;
        String url = null;
        switch(status){
            case "LOGIN":
                param = "userid="+uid+"&pwd="+password;
                url = Q_SERVER+USERLOGIN_PHP;
                OKhttpConn(handler, param, url);
                break;
            case "INSERT":
                param = "sname="+name+"&phone="+phone+"&email="+email+"&pwd="+password;
                url = Q_SERVER+USERINSERT_PHP;
                OKhttpConn(handler, param, url);
                break;
            case "UPDATE":
                param = "uid=" + uid + "&sname=" + name + "&phone=" + phone + "&email=" + email; //+ "&pwd=" + password;
                url = Q_SERVER+USERUPDATE_PHP;
                OKhttpConn(handler, param, url);
                break;
        }

    }

    private void OKhttpConn(final Handler hd, String param, String url){
        // 使用okhttp3建立連線
        // 建立OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient();

        // FormBody放要傳的參數和值
        Log.e("param", param);
        RequestBody formBody = RequestBody.create(FORM_CONTENT_TYPE, param);
        //FormBody formBody = new FormBody.Builder()
        //        .add("email", email)
        //        .add("pwd", password)
        //        .build();
        // 建立Request，設置連線資訊
        Request request = new Request.Builder()
                .url(url)
                .post(formBody) // 使用post連線
                .build();
        // 建立Call
        Call call = okHttpClient.newCall(request);

        // 執行Call連線到網址
        // 使用okhttp異步方式送出request
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 連線成功
                Boolean isOk = false;
                if (response.code() == 200) {   // response.code() return the HTTP status
                    qResult = response.body().string().trim();
                    Log.e("query.qResult", qResult);

                    if(status == "LOGIN"){
                        if (qResult.equals("no record")) {
                            isOk = false;
                            Log.e("query.isOk", String.valueOf(isOk));
                        } else {
                            isOk = true;
                            Log.e("query.isOk", String.valueOf(isOk));
                            createFile(qResult);
                        }
                        response.close();
                        Message msg = new Message();
                        Bundle bd = new Bundle();
                        Boolean isConn = true;
                        bd.putBoolean("isConn", isConn);
                        bd.putBoolean("isOk", isOk);
                        msg.setData(bd);
                        hd.sendMessage(msg);
                    }

                    if(status == "INSERT" || status == "UPDATE"){
                        if (qResult.equals("1")) {
                            isOk = true;
                            Log.e("insertOk", String.valueOf(isOk));
                        } else {
                            isOk = false;
                            Log.e("insertOk", String.valueOf(isOk));
                        }
                        response.close();
                        Message msg = new Message();
                        Bundle bd = new Bundle();
                        Boolean isConn = true;
                        bd.putBoolean("isConn", isConn);
                        bd.putBoolean("isOk", isOk);
                        msg.setData(bd);
                        hd.sendMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 連線失敗,網路未開啟
                Log.e("failed", " no Data!");
                Message msg = new Message();
                Bundle bd = new Bundle();
                Boolean isOk = false;
                Boolean isConn = false;
                bd.putBoolean("isConn", isConn);
                bd.putBoolean("isOk", isOk);
                msg.setData(bd);
                hd.sendMessage(msg);
            }
        });
    }

    private void createFile(String result) {
        File file = new File(dir, Member.M_FILE);
        Log.e("outFile", String.valueOf(file));
        if (file.exists()) {
            Log.e("file.exists()", "yes");
            file.delete();
        }
        try {
            // 建立應用程式私有文件
            FileOutputStream fOut = new FileOutputStream(file, false);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);  // 若為文字檔,則需多宣告此物件
            // 寫入資料
            osw.write(result);
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Member getData(){
        return new parseJSON(qResult, storeAcnt).parse();
    }

//    private Member parseJSON(String s) {
//        Log.e("jsonArray","Enter parseJSON");
//        try {
//            JSONObject jsObj = new JSONObject(s);
//            int id = Integer.parseInt(jsObj.getString("id"));
//            String userid = jsObj.getString("customerUserId");
//            String name = jsObj.getString("userName");
//            String phone = jsObj.getString("userPhone");
//            String email = jsObj.getString("email");
//            String passwd = jsObj.getString("password");
//            String gmail = jsObj.getString("google_email");
//            String lmail = jsObj.getString("line_email");
//            String Femail = jsObj.getString("FB_email");
//            member.add(id, userid, name, phone, email, passwd, gmail, lmail, Femail);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return member;
//    }
}