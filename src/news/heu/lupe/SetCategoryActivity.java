package news.heu.lupe;

import java.util.List;

import news.heu.Adaptor.CategoryAdaptor;
import news.heu.Adaptor.NewsAdaptor;
import news.heu.entity.MCategory;
import news.heu.util.JsonTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SetCategoryActivity extends Activity implements OnClickListener {

	ImageView image_back;
	ImageView set_OK;
	ListView catelistView;
	List<MCategory> mlist;
	CategoryAdaptor categoryAdaptor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_category_activity);
		image_back = (ImageView) findViewById(R.id.btn_set_category_back);
		image_back.setOnClickListener(this);
		set_OK = (ImageView) findViewById(R.id.set_category_ok);
		set_OK.setOnClickListener(this);
		findViewById(R.id.setcate).setOnClickListener(this);
		catelistView = (ListView) findViewById(R.id.category_listview);
		SharedPreferences pref = getSharedPreferences("CategoryInfo", Context.MODE_PRIVATE);
		String cajson = pref.getString("json_category", null);
		Log.e("[][]",cajson);
		mlist = JsonTools.getJsonListMcategory(cajson);
		catelistView.setAdapter(categoryAdaptor);
		
		catelistView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
				
				categoryAdaptor = new CategoryAdaptor(SetCategoryActivity.this, R.layout.adapter_category_layout,mlist);
				catelistView.setAdapter(categoryAdaptor);
			}
		});
	
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_set_category_back:
			//Toast.makeText(SetCategoryActivity.this, "==已监听到来自评论页面的返回键==", Toast.LENGTH_SHORT).show();
			SetCategoryActivity.this.finish();
			break;
		case R.id.set_category_ok:
			Intent i = new Intent(SetCategoryActivity.this,MainActivity.class);
			startActivity(i);
			SetCategoryActivity.this.finish();
			//Toast.makeText(SetCategoryActivity.this, "设置服务IP为:" +input_ip.getText().toString() , Toast.LENGTH_SHORT).show();
			//input_ip.getText();
			break;
		case R.id.setcate:
			SQLiteDatabase db = openOrCreateDatabase("NewHLS.db", Context.MODE_PRIVATE, null); 
			db.execSQL("DROP TABLE IF EXISTS cateper");
			db.execSQL("CREATE TABLE cateper (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					 							" cid INTEGER, " +
										 		" name VARCHAR)");
			Intent i2 = new Intent(SetCategoryActivity.this,SetCategoryActivity.class);
			SetCategoryActivity.this.finish();
			startActivity(i2);
			
		}
		
		
	}
	
	

}
