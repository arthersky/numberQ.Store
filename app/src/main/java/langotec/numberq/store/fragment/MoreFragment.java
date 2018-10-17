package langotec.numberq.store.fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import langotec.numberq.store.R;
import langotec.numberq.store.dbConnect.UserDBConn;
import langotec.numberq.store.login.AccInfoActivity;
import langotec.numberq.store.login.LoginActivity;
import langotec.numberq.store.login.Member;

public class MoreFragment extends Fragment {

    public Context context;
    private Button btnCheck;
    private TextView status;
    private View view;
    private Member member;
    private UserDBConn user;
    private MyHandler handler = new MyHandler();
    private ProgressDialog loading;

    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_more, container, false);
        setLayout();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        member = Member.getInstance();
        Boolean ismember = member.checkLogin(context);
        if (!ismember) {
            status.setText(R.string.not_login);
            btnCheck.setText(R.string.login);
        } else {
            status.setText(member.getUserId());
            btnCheck.setText(R.string.my_file);
        }

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnCheck:
                        Log.e("btnCheck","btnCheck Click");
                        if (btnCheck.getText().toString().trim().equals(getResources().getString(R.string.login))) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                        } else if (btnCheck.getText().toString().trim().equals(getResources().getString(R.string.my_file))) {
                            loading = ProgressDialog.show(getActivity(),"載入會員資料","Loading...", false);
                            user = new UserDBConn(handler, context.getFilesDir(), member.getUserId(), null, null, null, member.getPassword(), UserDBConn.STATUS_LOGIN);
                            user.start();
                        }
                        break;
                }
            }
        });
    }

    private void setLayout() {
        status = view.findViewById(R.id.mystatus);
        btnCheck = view.findViewById(R.id.btnCheck);
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            Bundle bd = msg.getData();
            Boolean isOk = bd.getBoolean("isOk");
            Boolean isConn = bd.getBoolean("isConn");
            Log.e("Login.isOk",String.valueOf(isOk));
            Log.e("Login.isConn",String.valueOf(isConn));
            loading.dismiss();
            // 連線成功
            if (isConn && isOk){
                // 使用者已註冊
                member = user.getData();
                Intent intent = new Intent(context, AccInfoActivity.class);
                intent.putExtra("myfile", member);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.login)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMessage("網路未連線！\n請確認您的連線狀態。")
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create().show();
            }
        }
    }
}
