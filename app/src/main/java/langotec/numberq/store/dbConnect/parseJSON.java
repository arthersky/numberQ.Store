package langotec.numberq.store.dbConnect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import langotec.numberq.store.login.Member;

public class parseJSON {
    private String mycontent;
    private Member storeAcnt;
    private ArrayList<?> arrayList;

    public parseJSON(String mycontent, Member account){
        this.mycontent = mycontent;
        this.storeAcnt = account;
    }

    public parseJSON(String mycontent, ArrayList<?> arrayList){
        this.mycontent = mycontent;
        this.arrayList = arrayList;
    }

    public Member parse() {
        //Log.e("jsonArray","Enter parseJSON");
        try {
            JSONObject jsObj = new JSONObject(mycontent);
            int id = Integer.parseInt(jsObj.getString("id"));
            String userid = jsObj.getString("userId");
            String name = jsObj.getString("userName");
            String phone = jsObj.getString("userPhone");
            String email = jsObj.getString("email");
            String passwd = jsObj.getString("password");
            String headId = jsObj.getString("HeadId");
            String branchId = jsObj.getString("BranchId");
            String gmail = jsObj.getString("google_email");
            String lmail = jsObj.getString("line_email");
            String Femail = jsObj.getString("FB_email");
            storeAcnt.add(id, userid, name, phone, email, passwd, headId, branchId, gmail, lmail, Femail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return storeAcnt;
    }

//    private ArrayList<Menu> parse() {
//        Log.e("jsonArray","Enter parseJSON");
//        Log.e("jsonArray", mycontent);
//        try {
//            JSONArray jsArray = new JSONArray(mycontent);
//            Log.e("jsonArray", String.valueOf(jsArray.length()));
//            Log.e("jsonArray", String.valueOf(jsArray.get(0)));
//            for (int i=0; i<jsArray.length(); i++) {
//                JSONObject jsObj = jsArray.getJSONObject(i);
//                Log.e("jsobj", String.valueOf(jsObj));
//                String HeadName = jsObj.getString("HeadName");
//                String HeadId = jsObj.getString("HeadId");
//                String productId = jsObj.getString("productId");
//                String productType = jsObj.getString("productType");
//                String productName = jsObj.getString("productName");
//                String price = jsObj.getString("FAX");
//                String image = jsObj.getString("image");
//                int available = Integer.parseInt(jsObj.getString("available"));
//                String description = jsObj.getString("description");
//                Menu menu = new Menu();
//                menu.add(HeadName, HeadId, productId, productType, productName, price, image, available, description);
//                arrayList.add(menu);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return arrayList;
//    }
}
