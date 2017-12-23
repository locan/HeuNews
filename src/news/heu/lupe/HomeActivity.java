package news.heu.lupe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import news.heu.Adaptor.NewsAdaptor;
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
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener ,IWXAPIEventHandler,IXListViewListener{
	public static String serverIP = "192.168.1.104";
	
	//ListView listView;
	private XListView mListView;
	
	public static List<New> newList;
	public  List<New> newList2;
	//private ArrayList<String> items = new ArrayList<String>();
	private int start = 0;
	private static int refreshCnt = 0;
	private Handler mHandler;
	int bef,aft;
	NewsAdaptor adapter;
	
	String json;
	RelativeLayout container;
	
	
	public  static void setserverIP(String ip){
		serverIP=ip;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
		//String cate_name = getIntent().getExtras().getString("category");
		//Toast.makeText(HomeActivity.this,"[HomeActivity]"+cate_name, Toast.LENGTH_SHORT).show();
		//listView = new ListView(this);
		mListView =(XListView) findViewById(R.id.mianListview);
		mListView.setPullLoadEnable(true);
		//mListView.getLayoutParams().
		//container.addView(mListView);
		newList = new ArrayList<New>();
		newList2 = new ArrayList<New>();
		mHandler = new Handler();
		final String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					json = GetJosnbyHttp.request(url);
					newList = JsonTools.getJsonList(json);
					mListView.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
							
							adapter = new NewsAdaptor(HomeActivity.this, R.layout.adapter_layout,newList);
							mListView.setAdapter(adapter);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
				//geneItem();
				bef = newList.size();
				
			
		
		
	
		
		mListView.setXListViewListener(this);
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			Toast.makeText(HomeActivity.this, "Internet网络！", Toast.LENGTH_SHORT).show();
			//runListview();
			
		}
		else
		{
			Toast.makeText(HomeActivity.this, "未连接到Internet网络！", Toast.LENGTH_SHORT).show();
		}
	
		mListView.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(HomeActivity.this, adapter.getItem(position-1).title+"--"+position, Toast.LENGTH_SHORT).show();
				
						// TODO Auto-generated method stub
						  //  tts.speak("语音测试",TextToSpeech.QUEUE_FLUSH, null);
							Intent intent = new Intent();
							intent.setClass(HomeActivity.this, WebSVeiwActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("id",adapter.getItem(position-1).id);
							bundle.putString("summary",adapter.getItem(position-1).summary);
							intent.putExtras(bundle);
							startActivity(intent);
	
				
			}
		});
				
		//mListView.set
	}

	private void initView() {
		findViewById(R.id.btn_main_left_menu).setOnClickListener(this);
		
		container =  (RelativeLayout) findViewById(R.id.container);
		//tts = new TextToSpeech(this, this);
		
		//SwipeRefreshLayout	swipe_container = findViewById(R.id.swipe_container);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_main_left_menu:
			if (MainActivity.slidingMenuView.getCurrentScreen() == 1) {
				MainActivity.slidingMenuView.snapToScreen(0);
			} else {
				MainActivity.slidingMenuView.snapToScreen(1);
			}
			
			break;
		default:
			break;
		}
	}
	
			
	
	public  void  runListview(){
    	newList = new ArrayList<New>();
    	new Thread( new Runnable() {
    		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
				try {
					json = GetJosnbyHttp.request(url);
					SharedPreferences pref = getSharedPreferences("json_index", Context.MODE_PRIVATE);
					boolean isSuccess = pref.edit().putString("json", json).commit();
					Log.e("isSuccess",isSuccess+"--");
					initListView(json,true);
				} catch (IOException e) {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
                            Log.e("【url申请错误】",  "run");
							
							SharedPreferences pref = getSharedPreferences("json_index", Context.MODE_PRIVATE);
							//String index ="[{"+"id"+":1,"+"category"+":"+"政治"+","+"title"+":"+"李克强在博鳌论坛开幕式上主旨演讲(全文)"+","+"time"+":"+"Apr 11, 2014 10:04:21 PM"+","+"author"+":"+"LuPe"+","+"img"+","+"http://imgt3.bdstatic.com/it/u\u003d3039581168,2822584475\u0026fm\u003d90\u0026gp\u003d0.jpg"+","+"summary"+":"+"中华人民共和国国务院总理 李克强 4月10日,博鳌亚洲论坛2014年年会在海南省博鳌开幕,国务院总理李克强出席开幕式并发表题为《共同开创亚洲发展新未..."+","+"cnum"+":6}]";
							String json = pref.getString("json",null);
							Log.e("jsonCache","--"+json);
							//initListView(json,false);
						}
					});
					e.printStackTrace();
					Log.e("【url申请错误】",  "客户端与服务器连接异常，请检查服务器IP是否设置正确！");
				}				
			}
		}).start();
    	
    }
	
	private void initListView(String json,boolean inThread){
		Log.e("Json", json);
		newList =new ArrayList<New>();
		newList = JsonTools.getJsonList(json);
		if(inThread){
			mListView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
					
					adapter = new NewsAdaptor(HomeActivity.this, R.layout.adapter_layout,newList);
					mListView.setAdapter(adapter);
				}
			});
		}else{
			adapter = new NewsAdaptor(HomeActivity.this, R.layout.adapter_layout,newList);
			mListView.setAdapter(adapter);
		}		
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				newList.clear();
				geneItems();
				onLoad();
				// mAdapter.notifyDataSetChanged();
				//mAdapter = new ArrayAdapter<String>(XListViewActivity.this, R.layout.list_item, items);
				//mListView.setAdapter(mAdapter);				
			}
		}, 2000);
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				//geneItems();
				adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	private void geneItems() {
		/*for (int i = 0; i != 5; ++i) {
			newList.add("refresh cnt " + (++start));
		}*/
		
		final String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					json = GetJosnbyHttp.request(url);
					newList = JsonTools.getJsonList(json);
					mListView.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
							
							adapter = new NewsAdaptor(HomeActivity.this, R.layout.adapter_layout,newList);
							mListView.setAdapter(adapter);
						}
					});
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}
	/*private void geneItem() {
		for (int i = 0; i != 5; ++i) {
			newList.add("refresh cnt " + (++start));
		}
		
		final String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					json = GetJosnbyHttp.request(url);
					newList = JsonTools.getJsonList(json);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}*/

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}

	
	
	
}
