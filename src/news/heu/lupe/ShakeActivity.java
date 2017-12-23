package news.heu.lupe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeActivity extends Activity implements OnClickListener,IXListViewListener,SensorEventListener {
	TextView title;
	//ListView listView2;
	//List<New> gnewList;
	NewsAdaptor adapter2;
	String json;
    private XListView mListView2;
	
	public  List<New> gnewList;
	//public  List<New> gnewList2;
	//private ArrayList<String> items = new ArrayList<String>();
	private int start = 0;
	private static int refreshCnt = 0;
	private Handler mHandler;
	int bef,aft;
	RelativeLayout cate_container;
	int category_id;
	SensorManager sensorManager = null;  
    Vibrator vibrator = null;  

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shakeit);
		initView();
		//mListView2 = new XListView(this);
		mListView2 = (XListView) findViewById(R.id.shakeListview);
		mListView2.setPullLoadEnable(true);
		gnewList = new ArrayList<New>();

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); 
			
		mHandler = new Handler();
		mListView2.setXListViewListener(this);
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
		}
		else
		{
			Toast.makeText(ShakeActivity.this, "未连接到Internet网络！", Toast.LENGTH_SHORT).show();
		}
		mListView2.setOnItemClickListener(new ListView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(ShakeActivity.this, adapter2.getItem(position-1).title+"--"+position, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(ShakeActivity.this, WebSVeiwActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id",adapter2.getItem(position-1).id);
				bundle.putString("summary", adapter2.getItem(position-1).summary);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		findViewById(R.id.btn_shake_left_menu).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_shake_left_menu:
			if (MainActivity.slidingMenuView.getCurrentScreen() == 1) {
			MainActivity.slidingMenuView.snapToScreen(0);
			} 
			else {
				MainActivity.slidingMenuView.snapToScreen(1);
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

						
				adapter2 = new NewsAdaptor(ShakeActivity.this, R.layout.adapter_layout,gnewList);
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
		
		final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsTen.action";
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
									
									adapter2 = new NewsAdaptor(ShakeActivity.this, R.layout.adapter_layout,gnewList);
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();  
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
        float[] values = event.values;  
        if (sensorType == Sensor.TYPE_ACCELEROMETER)  
        {  
            if ((Math.abs(values[0]) > 15 || Math.abs(values[1]) > 15 || Math  
                    .abs(values[2]) > 15))  
            {  
                Log.d("sensor x ", "============ values[0] = " + values[0]);  
                Log.d("sensor y ", "============ values[1] = " + values[1]);  
                Log.d("sensor z ", "============ values[2] = " + values[2]);  
                //摇动手机后，再伴随震动提示~~  
                new Thread(new Runnable() {
    				
    				@Override
    				public void run() {
    					// TODO Auto-generated method stub
    					final String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsTen.action";
    					try {
    						json = GetJosnbyHttp.request(url);
    						gnewList = JsonTools.getJsonList(json);
    						mListView2.post(new Runnable() {
    							
    							@Override
    							public void run() {
    								// TODO Auto-generated method stub
    								//String url = "http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action";
    								
    								adapter2 = new NewsAdaptor(ShakeActivity.this, R.layout.adapter_layout,gnewList);
    								mListView2.setAdapter(adapter2);
    								vibrator.vibrate(500);  
    							}
    						});
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    					
    				}
    			}).start();
                
            }  
  
        }  
    }  

	@Override  
    protected void onPause()  
    {  
        super.onPause();  
        sensorManager.unregisterListener(this);  
    }  
  
    @Override  
    protected void onResume()  
    {  
        super.onResume();  
        sensorManager.registerListener(this,  
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
                SensorManager.SENSOR_DELAY_NORMAL); } 
	
}
