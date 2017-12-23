package news.heu.lupe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import news.heu.run.PullMessageRunnable;
import news.heu.util.GetJosnbyHttp;
import news.heu.util.JsonTools;
import news.heu.view.SlidingMenuView;
import news.heu.view.SlidingMenuView.CloseAnimation;
import news.heu.Adaptor.CategoryAdaptor;
import news.heu.entity.MCategory;
import news.heu.lupe.R;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.ActivityGroup;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActivityGroup implements OnClickListener {
	public static SlidingMenuView slidingMenuView;
	ScheduledThreadPoolExecutor executor;
	public static String scode;
	private ViewGroup tabcontent;
    LinearLayout menu;
    ListView listViewMenu;
    CategoryAdaptor adapter;
	String json;
	List<MCategory> menuList;
	String push_key = "SDtKkQZcP4I3qB7uGb7XCyMB";
	Button but ;
	Button collectbut;
	Button category1;
	Button category2;
	Button category3;
	Button category4;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_side);

		slidingMenuView = (SlidingMenuView) findViewById(R.id.main_menu_view);
		tabcontent = (ViewGroup) slidingMenuView.findViewById(R.id.main_body);
		scode = Secure.getString(getBaseContext().getContentResolver(), Secure.ANDROID_ID);
		Log.e("[machine code]", scode);
		initView();
		CreateCategoryDB();
		// 以apikey的方式登录，一般放在主Activity的onCreate中
				PushManager.startWork(getApplicationContext(),
						PushConstants.LOGIN_TYPE_API_KEY, 
						push_key);
			executor = new  ScheduledThreadPoolExecutor(1);
				PullMessageRunnable pullMessageRunnable = new PullMessageRunnable(getApplicationContext(), scode);
				executor.scheduleAtFixedRate(pullMessageRunnable, 5, 10, TimeUnit.SECONDS);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 加载主页
		
		but = (Button) findViewById(R.id.personinfor);
		but.setOnClickListener(this);
		collectbut = (Button) findViewById(R.id.personcollect);
		collectbut.setOnClickListener(this);
		SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
		String username = pref.getString("username", null);
		if(username!=null)
			but.setText(username+"欢迎登陆！（点击注销）");
		Intent i = new Intent(this, HomeActivity.class);
		View v = getLocalActivityManager().startActivity(HomeActivity.class.getName(), i).getDecorView();
				tabcontent.removeAllViews();
				tabcontent.addView(v);
		menu = (LinearLayout) findViewById(R.id.main_left_menu);
		
		findViewById(R.id.btn_homepage).setOnClickListener(this);
		findViewById(R.id.btn_set).setOnClickListener(this);
		findViewById(R.id.btn_exit).setOnClickListener(this);
		findViewById(R.id.shake).setOnClickListener(this);
		category1 = (Button) findViewById(R.id.category1);
		category1 .setOnClickListener(this);
		category2 = (Button) findViewById(R.id.category2);
		category2 .setOnClickListener(this);
		category3 = (Button) findViewById(R.id.category3);
		category3.setOnClickListener(this);
		category4 = (Button) findViewById(R.id.category4);
		category4.setOnClickListener(this);
		category1.setVisibility(View.INVISIBLE) ;
		category2.setVisibility(View.INVISIBLE) ;
		category3.setVisibility(View.INVISIBLE) ;
		category4.setVisibility(View.INVISIBLE) ;
		findViewById(R.id.tocategory).setOnClickListener(this);
		tabcontent.setOnClickListener(this);
	}
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id .personinfor:
			SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			String username = pref.getString("username", null);
			//Toast.makeText(getApplicationContext(), "点击了用户信息", Toast.LENGTH_SHORT).show();
			if(username==null)
			{
			showActivity(UserLoginActivity.class,null,0);
			}
			else
			{ 	
				pref.edit().putInt("userid", 0).commit();
				pref.edit().putString("username", null).commit();
				but.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						but.setText("用户（点击登录）");
					}
				});
			}
		
			break;
		case R.id.btn_homepage:// 首页
			
			showActivity(HomeActivity.class,null,0);
			break;
		case R.id.btn_set:// 设置
			showActivity(SetActivity.class,"设置",0);
			break;
		case R.id.btn_exit:// 退出
			//this.finish();
			Toast.makeText(MainActivity.this, "点击了退出系统", Toast.LENGTH_SHORT).show();
			finish();
			System.exit(0);
			break;
		case R.id.personcollect:
			Toast.makeText(getApplicationContext(), "[我的收藏]", Toast.LENGTH_SHORT).show();
			showActivity(CategoryActivity.class,"我的收藏",0);
			break;
		case R.id.main_body:
			slidingMenuView.snapToScreen(1);
			break;
		case R.id.shake:
			showActivity(ShakeActivity.class,null,0);
			break;
		case R.id.tocategory:
			showActivity(CategoryActivity.class,"政治[点击切换频道]",1);
			break;
		default:
			break;
		}
	}

	/**
	 * 切换Activity的方法
	 * 
	 * @param c
	 *            参数为Activity
	 */
	
	private void showActivity(Class<?> c,String cate,int cid) {
		Intent intent = new Intent(MainActivity.this, c);
		Bundle bundle = new Bundle();
		bundle.putString("category",cate);
		bundle.putInt("cid", cid);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		View view = getLocalActivityManager().startActivity(c.getName(), intent)
				.getDecorView();
		tabcontent.removeAllViews();
		tabcontent.addView(view);

		slidingMenuView.setCloseAnimation(new CloseAnimation() {

			@Override
			public void closeMenuAnimation() {
				// TODO Auto-generated method stub
				if (-slidingMenuView.getScrollX() == getWindowManager()
						.getDefaultDisplay().getWidth()
						- (slidingMenuView.totalWidth - getWindowManager()
								.getDefaultDisplay().getWidth())) {
					slidingMenuView.closeMenu_2(1);
				}
			}
		});

		slidingMenuView.closeMenu_1(1);
	}

	public ViewGroup getTabcontent() {
		return tabcontent;
	}

	public void setTabcontent(ViewGroup tabcontent) {
		this.tabcontent = tabcontent;
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (slidingMenuView.getCurrentScreen() == 1) {

				if ((System.currentTimeMillis() - exitTime) > 2000) {
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
			} else {
				slidingMenuView.snapToScreen(1);
			}

			return true;

		} else if (keyCode == KeyEvent.KEYCODE_MENU
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (slidingMenuView.getCurrentScreen() == 1) {
				slidingMenuView.snapToScreen(0);
			} else {
				slidingMenuView.snapToScreen(1);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void CreateCategoryDB(){
		 int index=0;
		 final SQLiteDatabase db = openOrCreateDatabase("NewHLS.db", Context.MODE_PRIVATE, null); 
		// db.execSQL("DROP TABLE IF EXISTS cateper");"create table if not exists
		 db.execSQL("CREATE TABLE IF NOT EXISTS cateper (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				 							" cid INTEGER, " +
									 		" name VARCHAR)");
		 Cursor c = db.rawQuery("SELECT * FROM cateper ",null);
		 List<MCategory> mclist = new ArrayList<MCategory>();
			
	        while (c.moveToNext()) {  
	        	
	        	MCategory mcg = new MCategory();
	            int _id = c.getInt(c.getColumnIndex("_id"));  
	            String name = c.getString(c.getColumnIndex("name"));  
	            int cid = c.getInt(c.getColumnIndex("cid"));  
	            mcg.id = cid;
	            mcg.name = name;
	            mclist.add(mcg);
	            Log.e("db", "_id=>" + _id + ", name=>" + name + ", cid=>" + cid);  
	        } 
	        c.close();
	        int li=1;
	        for(final MCategory vv:mclist)
	        {
	        	if(li==1) 
	        	{
		        	category1.setText(vv.name);
		        	category1.setVisibility(View.VISIBLE);
		        	category1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showActivity(CategoryActivity.class,vv.name,vv.id);
						}
					});
	        	}
	        	if(li==2) 
	        	{
	        		category2.setText(vv.name);
	        		category2.setVisibility(View.VISIBLE);
	        		category2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showActivity(CategoryActivity.class,vv.name,vv.id);
						}
					});
        		}
	        	if(li==3) 
	        	{
	        		category3.setText(vv.name);
	        		category3.setVisibility(View.VISIBLE);
	        		category3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showActivity(CategoryActivity.class,vv.name,vv.id);
						}
					});
        		}
	        	if(li==4) 
	        	{
	        		category4.setText(vv.name);
	        		category4.setVisibility(View.VISIBLE);
	        		category4.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							showActivity(CategoryActivity.class,vv.name,vv.id);
						}
					});
        		}
	        	li++;
	        }
		 db.execSQL("DROP TABLE IF EXISTS category");
		 db.execSQL("CREATE TABLE category (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				 							" cid INTEGER, " +
									 		" name VARCHAR)");
		 menuList = new ArrayList<MCategory>();
			final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/category_getAllCategoryToMobile.action";
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String json_category = GetJosnbyHttp.request(url);
						SharedPreferences pref = getSharedPreferences("CategoryInfo", Context.MODE_PRIVATE);
						pref.edit().putString("json_category", json_category).commit();
						menuList = JsonTools.getJsonListMcategory(json_category);
						for(MCategory mc:menuList){
							MCategory ca = new MCategory();  
							ca.name = mc.name;  
							ca.id = mc.id;  
					        //插入数据  
					        db.execSQL("INSERT INTO category VALUES (NULL, ?, ?)", new Object[]{ca.id,ca.name}); 
							
						}
						/*Cursor c = db.rawQuery("SELECT * FROM category ",null);  
				        while (c.moveToNext()) {  
				            int _id = c.getInt(c.getColumnIndex("_id"));  
				            String name = c.getString(c.getColumnIndex("name"));  
				            int cid = c.getInt(c.getColumnIndex("cid"));  
				            Log.e("db", "_id=>" + _id + ", name=>" + name + ", cid=>" + cid);  
				        } 
				        c.close();   */
						db.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		 
	}

}
