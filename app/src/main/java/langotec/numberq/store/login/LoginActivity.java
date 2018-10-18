package langotec.numberq.store.login;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import langotec.numberq.store.MainActivity;
import langotec.numberq.store.R;
import langotec.numberq.store.WelcomeActivity;
import langotec.numberq.store.dbConnect.UserDBConn;


public class LoginActivity extends AppCompatActivity {

    private EditText etUid;
    private EditText etPass;
    private Button btnLogin;

    private MyHandler handler;
    private Context context;
    private UserDBConn user;
    private String uid, pwd;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        setLayout();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uid = etUid.getText().toString();
                pwd = etPass.getText().toString();
                if(uid.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(context,"未輸入帳號或密碼", Toast.LENGTH_LONG).show();
                }else{
                    loading = ProgressDialog.show(context, "登入中","Loading...", false);
                    handler = new MyHandler();
                    user = new UserDBConn(handler, getFilesDir(), uid, null, null, null, pwd, UserDBConn.STATUS_LOGIN );
                    user.start();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        this.finishAffinity();
    }

    private void setLayout(){
        etUid = findViewById(R.id.etUid);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private boolean isVaildEmailFormat(String email)
    {
        if (email == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            Bundle bd = msg.getData();
            Boolean isOk = bd.getBoolean("isOk");
            Boolean isConn = bd.getBoolean("isConn");
            Log.e("login.isOk", String.valueOf(isOk));
            Log.e("login.isConn", String.valueOf(isConn));
            loading.dismiss();
            String tmpmsg=null;
            if (isConn) { //連線成功
                if (isOk) { // 使用者已註冊
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("登入")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setMessage("登入成功!")
                            .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent();
                                    intent.setClass(context, WelcomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .create().show();
                } else {  // 使用者未註冊
                    tmpmsg="登入失敗!\n請確認帳號或密碼是否正確。";
                    showdialog(tmpmsg);
                }
            } else { // 連線失敗,未開啟網路
                tmpmsg="網路未連線!\n請確認您的網路連線狀態。";
                showdialog(tmpmsg);
            }
        }
    }

    private void showdialog(String src){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("登入")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setMessage("網路未連線!\n請確認您的網路連線狀態。")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }
/*
    private void loginSuccessGoTo(){
        Intent intent = new Intent();
        if (startFrom != null && startFrom.equals("fromCartFragment")) {
            intent.putExtra("User", user.getData());
            intent.setClass(context, CheckOutActivity.class);
        }
        else{
            intent.putExtra("startFrom", "fromLoginActivity");
            intent.setClass(context, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
    */
}
