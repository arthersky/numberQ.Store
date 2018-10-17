package langotec.numberq.store.dbConnect;

import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import langotec.numberq.store.login.Member;

public class ReadFile {
    private String myfile;
    private File dir;
    private Member storeAcnt;

    public ReadFile(File dir, String myfile, Member account){
        Log.e("readFile", "dir:" + dir + "/" + myfile + "   storeAcnt:"+account.toString());
        this.dir = dir;
        this.myfile = myfile;
        this.storeAcnt = account;
    }

    public Member read() {
        File file = new File(dir, myfile);
        if(file.exists()){
            char[] buffer = new char[10]; //一次讀取一個位元
            FileReader fr = null;
            StringBuilder sb = new StringBuilder();
            try{
                fr = new FileReader(file);
                while(fr.read(buffer) !=-1){
                    sb.append(buffer);
                }
            } catch(IOException e){

            } finally{
                try{
                    fr.close();  // 關閉檔案
                } catch(IOException e){  }
            }

            final String result = sb.toString().trim();
            Log.e("result", result);
            if(result.equals("no record")){
                return null;
            }else{
                return storeAcnt = new parseJSON(result, storeAcnt).parse();
            }
        } else {
            return null;
        }
    }
}
