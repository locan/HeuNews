package news.heu.lupe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import news.heu.Adaptor.CategoryTAdaptor;
import news.heu.Adaptor.NewsAdaptor;
import news.heu.entity.MCategory;
import news.heu.entity.New;
import news.heu.lupe.R;
import news.heu.util.GetJosnbyHttp;
import news.heu.util.JsonTools;
import news.heu.view.XListView;
import news.heu.view.XListView.IXListViewListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams; 
public class CategoryActivity extends Activity implements OnClickListener,IXListViewListener {
	TextView title;
	//ListView listView2;
	//List<New> gnewList;
	NewsAdaptor adapter2;
	CategoryTAdaptor categoryTAdaptor;
	String json;
    private XListView mListView2;
    PopupWindow popupWindow; 
	public  List<New> gnewList;
	//public  List<New> gnewList2;
	//private ArrayList<String> items = new ArrayList<String>();
	private int start = 0;
	private static int refreshCnt = 0;
	private Handler mHandler;
	int bef,aft;
	RelativeLayout cate_container;
	int category_id = 1;
	ListView gtlistview;
	int cate_id;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		initView();
		InitpopWindow();
		//mListView2 = new XListView(this);
		mListView2 = (XListView) findViewById(R.id.categoryListview);
		mListView2.setPullLoadEnable(true);
		//cate_container.addView(mListView2);
		gnewList = new ArrayList<New>();
		//gnewList2 = new ArrayList<New>();
		final String cate_name = getIntent().getExtras().getString("category");
		cate_id = getIntent().getExtras().getInt("cid", -1);
		Log.e("【跳转到的分类】", ""+cate_name);
		
		title.setText(cate_name);
		//geneItem();
		bef = gnewList.size();
		if(cate_name.equals("我的收藏")){
			SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			final int userid = pref.getInt("userid", 0);
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_getCollectByUserId.action?id="+userid;
					try {
						json = GetJosnbyHttp.request(url);
						gnewList = JsonTools.getJsonList(json);
						mListView2.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
								
								adapter2 = new NewsAdaptor(CategoryActivity.this, R.layout.adapter_layout,gnewList);
								mListView2.setAdapter(adapter2);
					
							}
						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}).start();
		}
		else{
		
		//Toast.makeText(CategoryActivity.this,"[CategoryActivity]"+cate_name, Toast.LENGTH_SHORT).show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsByCategoryIdSendToMobile.action?id="+cate_id;
				try {
					json = GetJosnbyHttp.request(url);
					gnewList = JsonTools.getJsonList(json);
					mListView2.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
							
							adapter2 = new NewsAdaptor(CategoryActivity.this, R.layout.adapter_layout,gnewList);
							mListView2.setAdapter(adapter2);
				
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		}



		mHandler = new Handler();
		mListView2.setXListViewListener(this);
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
		}
		else
		{
			Toast.makeText(CategoryActivity.this, "未连接到Internet网络！", Toast.LENGTH_SHORT).show();
		}
		mListView2.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(CategoryActivity.this, adapter2.getItem(position-1).title+"--"+position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(CategoryActivity.this, WebSVeiwActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id",adapter2.getItem(position-1).id);
				bundle.putString("summary", adapter2.getItem(position-1).summary);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		gtlistview.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(CategoryActivity.this,categoryTAdaptor.getItem(position).name,Toast.LENGTH_SHORT).show();
				title.setText(categoryTAdaptor.getItem(position).name);
				popupWindow.dismiss();
				
				final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsByCategoryIdSendToMobile.action?id="+categoryTAdaptor.getItem(position).id;
				cate_id = categoryTAdaptor.getItem(position).id;
				new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									json = GetJosnbyHttp.request(url);
									gnewList = JsonTools.getJsonList(json);
									mListView2.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
											
											adapter2 = new NewsAdaptor(CategoryActivity.this, R.layout.adapter_layout,gnewList);
											mListView2.setAdapter(adapter2);
										}
									});
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
			}
		});
	}

	private void initView() {
		findViewById(R.id.btn_category_left_menu).setOnClickListener(this);
	
		title = (TextView) findViewById(R.id.title_category_bar_name);
		title.setOnClickListener(this);
		cate_container = (RelativeLayout) findViewById(R.id.cate_container);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_category_left_menu:
			if (MainActivity.slidingMenuView.getCurrentScreen() == 1) {
			MainActivity.slidingMenuView.snapToScreen(0);
			} 
			else {
				MainActivity.slidingMenuView.snapToScreen(1);
			}
		
			break;
		case R.id.title_category_bar_name:
			if (!popupWindow.isShowing()) {  
                popupWindow.showAsDropDown(title,600, 0);  
            } 
			break;
		default:
			break;
		}
	}
	

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				gnewList.clear();
				geneItems();				
				// mAdapter.notifyDataSetChanged();
				//mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);
				//mListView.setAdapter(mAdapter);				

						
				adapter2 = new NewsAdaptor(CategoryActivity.this, R.layout.adapter_layout,gnewList);
				mListView2.setAdapter(adapter2);
			
					onLoad();

			}
		}, 1000);
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//geneItems();
				adapter2.notifyDataSetChanged();
				onLoad();
			}
		}, 1000);
	}
	private void geneItems() {
		/*for (int i = 0; i != 5; ++i) {
			newList.add("refresh cnt " + (++start));
		}*/
		
		final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsByCategoryIdSendToMobile.action?id="+cate_id;
new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					json = GetJosnbyHttp.request(url);
					gnewList = JsonTools.getJsonList(json);
					mListView2.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
							
							adapter2 = new NewsAdaptor(CategoryActivity.this, R.layout.adapter_layout,gnewList);
							mListView2.setAdapter(adapter2);
						}
					});
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
	

	private void onLoad() {
		mListView2.stopRefresh();
		mListView2.stopLoadMore();
		mListView2.setRefreshTime("刚刚");
	}
	public void InitpopWindow(){
		LayoutInflater inflater = LayoutInflater.from(this);  
        View view = inflater.inflate(R.layout.cate_popwindow, null); 
        gtlistview = (ListView) view.findViewById(R.id.cglistview);
        SQLiteDatabase db = openOrCreateDatabase("NewHLS.db", Context.MODE_PRIVATE, null); 
		Cursor c = db.rawQuery("SELECT * FROM category ",null); 
		 List<MCategory> mclist = new ArrayList<MCategory>();
			
	        while (c.moveToNext()) {  
	        	
	        	MCategory mcs = new MCategory();
	            int _id = c.getInt(c.getColumnIndex("_id"));  
	            String name = c.getString(c.getColumnIndex("name"));  
	            int cid = c.getInt(c.getColumnIndex("cid"));  
	           mcs.id = cid;
	           mcs.name = name;
	           mclist.add(mcs);
	            Log.e("db", "_id=>" + _id + ", name=>" + name + ", cid=>" + cid);  
	        } 
	        c.close();     
        categoryTAdaptor = new CategoryTAdaptor(getApplicationContext(), R.layout.adapter_menu_layout, mclist);
        gtlistview.setAdapter(categoryTAdaptor);
        popupWindow = new PopupWindow(view);//, LayoutParams.WRAP_CONTENT,  
              //  LayoutParams.WRAP_CONTENT); 
        popupWindow.setHeight(LayoutParams.WRAP_CONTENT); 
        popupWindow.setWidth(320);

        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.open_mine_messages_pressed));  
        popupWindow.setOutsideTouchable(true);  
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
        popupWindow.update();  
        popupWindow.setTouchable(true);  
        popupWindow.setFocusable(true); 
        
	}
	
}
