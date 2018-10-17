package langotec.numberq.store.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import langotec.numberq.store.R;
import langotec.numberq.store.dbConnect.ReadFile;


public class AccInfoActivity extends AppCompatActivity {

    private Context context;
    private UserAdapter adapter;
    private ArrayList<HashMap<String, Object>> itemList;
    private Member account = Member.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_info);
        context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("使用者資訊");

        account = new ReadFile(getFilesDir(), "storeUser.txt", account).read();

        if(account != null){
            String[] field = new String[]{"userId", "userName", "userPhone", "email", "HeadId", "BranchId"};
            String[] value = new String[]{account.getUserId(), account.getUserName(), account.getUserPhone(), account.getEmail(), account.getHeadId(), account.getBranchId()};
            itemList = new ArrayList<>();
            for(int i=0; i<field.length; i++){
                HashMap<String, Object> item = new HashMap<>();
                item.put("Title", field[i]);
                item.put("subTitle", value[i]);
                itemList.add(item);
            }

            adapter = new UserAdapter(context, itemList);
            ListView lv = (ListView)findViewById(R.id.listview);
            lv.setAdapter(adapter);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onLogoutClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("確定登出?")
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        File file = new File(context.getFilesDir(), Member.M_FILE);
                        file.delete();
                        account.delete();
                        dialogInterface.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }
}
