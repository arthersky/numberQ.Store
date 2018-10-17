package langotec.numberq.store.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import langotec.numberq.store.R;

public class UserAdapter extends BaseAdapter {
	
	// 定義 LayoutInflater
	private LayoutInflater myinflater;
	// 定義 Adapter 內藴藏的資料容器
	private ArrayList<HashMap<String, Object>> list;

	//初始化
	public UserAdapter(Context context, ArrayList<HashMap<String, Object>> list){
		//預先取得 LayoutInflater 物件實體
		myinflater = LayoutInflater.from(context);
		this.list = list;
    }

	@Override
	public int getCount() { // 公定寫法(取得List資料筆數)
		return list.size();
	}

	@Override
	public Object getItem(int position) { // 公定寫法(取得該筆資料)
		return list.get(position);
	}

	@Override
	public long getItemId(int position) { // 公定寫法(取得該筆資料的position)
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		//當ListView被拖拉時會不斷觸發getView，為了避免重複加載必須加上這個判斷
		if(convertView == null){
			// 1:將 R.layout.row 實例化
			convertView = myinflater.inflate(R.layout.row, parent, false);

			// 2:建立 UI 標籤結構並存放到 holder
			holder = new ViewHolder();
			holder.title = (TextView)convertView.findViewById(R.id.t1);
			holder.subTitle = (TextView)convertView.findViewById(R.id.t2);

			// 3:注入 UI 標籤結構 --> convertView
			convertView.setTag(holder);
		}else{
			// 取得  UI 標籤結構
			holder = (ViewHolder)convertView.getTag();
		}

		// 4:設定顯示資料
		holder.title.setText(list.get(position).get("Title").toString());
		holder.subTitle.setText(list.get(position).get("subTitle").toString());
		return convertView;
	}
	
	// UI 標籤結構
	//優化Listview 避免重新加載
	//這邊宣告你會動到的Item元件
	static class ViewHolder {
		TextView title;
		TextView subTitle;
	}
}