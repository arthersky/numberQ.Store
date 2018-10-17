package langotec.numberq.store.login;

import android.app.Application;
import android.content.Context;

import java.io.Serializable;

import langotec.numberq.store.dbConnect.ReadFile;

public class Member extends Application implements Serializable {
    private static Member storeAcnt;
    private int id;
    private String userId;
    private String userName;
    private String userPhone;
    private String email;
    private String password;
    private String HeadId;
    private String BranchId;
    private String google_email;
    private String line_email;
    private String FB_email;
    private static Context context;
    public static final String M_FILE="storeUser.txt";

    private Member(){
        //context = getApplicationContext();
    }

    public static Member getInstance(){
        if(storeAcnt == null){
            //new ReadFile( context.getFilesDir(),"storeUser.txt", storeAcnt);
            synchronized(Member.class){
                if(storeAcnt == null){
                    storeAcnt = new Member();
                }
            }
        }
        return storeAcnt;
    }

    public void add(int id, String userId, String userName, String userPhone, String email, String password, String HeadId, String BranchId, String google_email, String line_email, String FB_email){
        setId(id);
        setUserId(userId);
        setUserName(userName);
        setUserPhone(userPhone);
        setEmail(email);
        setPassword(password);
        setHeadId(HeadId);
        setBranchId(BranchId);
        setGoogle_email(google_email);
        setLine_email(line_email);
        setFB_email(FB_email);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadId() {
        return HeadId;
    }

    public void setHeadId(String headId) {
        HeadId = headId;
    }

    public String getBranchId() {
        return BranchId;
    }

    public void setBranchId(String branchId) {
        BranchId = branchId;
    }

    public String getGoogle_email() {
        return google_email;
    }

    public void setGoogle_email(String google_email) {
        this.google_email = google_email;
    }

    public String getLine_email() {
        return line_email;
    }

    public void setLine_email(String line_email) {
        this.line_email = line_email;
    }

    public String getFB_email() {
        return FB_email;
    }

    public void setFB_email(String FB_email) {
        this.FB_email = FB_email;
    }

    public Boolean checkLogin(Context context){
        if(null != new ReadFile(context.getFilesDir(), Member.M_FILE, storeAcnt).read())
            return true;
        else{
            return false;
        }
    }

    public void delete(){
        storeAcnt = null;
    }
}
