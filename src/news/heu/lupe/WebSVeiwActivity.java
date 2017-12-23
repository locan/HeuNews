package news.heu.lupe;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import news.heu.entity.New;
import news.heu.entity.ResponseJson;
import news.heu.util.GetJosnbyHttp;

import com.google.gson.Gson;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WebSVeiwActivity extends WebVeiwActivity implements OnClickListener,IWXAPIEventHandler,OnInitListener{
	public static final String APP_ID = "wx94b91889417b87d1";
	private TextToSpeech tts;
	private IWXAPI api;
	int news_id;
	String new_summary;
	ImageView image_back;
	ImageView pr_share;
	EditText website;
	WebView web;
	TextView toComment;
	boolean tts_index;
	boolean collect_index;
	Button reader;
	Button collectbut;
	EditText commenttext;
	New g_new;
	List<Map<String,Object>> listitem;
	int []imageid =new int[]{R.drawable.weixin_popover,R.drawable.weixinpengyou_popover};
	final String []wxtext = new String[]{"分享给微信好友","分享到朋友圈"};
	SimpleAdapter adapter ;
	int collect_id;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/*
		 * private SimpleDateFormat sdf = new SimpleDateFormate("yyyy-MM-dd HH:mm:ss");
		 * */
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view  = inflater.inflate(R.layout.webviewactivity, null);
		addSubWebView(view);
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		api.registerApp(APP_ID);
		image_back = (ImageView) findViewById(R.id.btn_web_back);
		pr_share = (ImageView) findViewById(R.id.btn_web_share);
		collectbut = (Button) findViewById(R.id.collectss);
		commenttext = (EditText) findViewById(R.id.writecomment);
		findViewById(R.id.readss).setOnClickListener(this);
		findViewById(R.id.tocomment).setOnClickListener(this);
		collectbut.setOnClickListener(this);
		image_back.setOnClickListener(this);
		pr_share.setOnClickListener(this);
		tts_index = false;
		tts = new TextToSpeech(this, this);
		listitem = new ArrayList<Map<String,Object>>();
		for(int i=0; i<imageid.length; ++i){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("wximage", imageid[i]);
			map.put("wxtext", wxtext[i]);
			listitem.add(map);
		}
		adapter = new SimpleAdapter(this, listitem, R.layout.wxselect, 
				new String[]{"wximage","wxtext"}, new int[]{R.id.wximage,R.id.wxtext});
		//bottom_comment
		//toComment = (TextView) findViewById(R.id.bottom_comment);
		//toComment.setOnClickListener(this);
		web =(WebView) findViewById(R.id.webview);
		WebSettings settings = web.getSettings();
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS); 
		//settings.setUseWideViewPort(true);  
        //settings.setLoadWithOverviewMode(true);
		website = (EditText) findViewById(R.id.web_input);
        web.setVisibility(View.VISIBLE);
        web.getSettings().setUseWideViewPort(true);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setAllowFileAccess(true);
		web.getSettings().setPluginState(PluginState.ON);
		//web.setWebChromeClient(new WebChromeClient());
		web.getSettings().setAllowFileAccess(true);
		web.setWebViewClient(new WebViewClient(){  
	            @Override  
	            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
	                // TODO Auto-generated method stub  
	                view.loadUrl(url);// 使用当前WebView处理跳转  
	                return true;//true表示此事件在此处被处理，不需要再广播  
	            }  
	            @Override   //转向错误时的处理  
	            public void onReceivedError(WebView view, int errorCode,  
	                    String description, String failingUrl) {  
	                // TODO Auto-generated method stub  
	                Toast.makeText(WebSVeiwActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
	            }  
	        });
		
		try {
			web.getClass().getMethod("onPause").invoke(web,(Object[])null);
			web.getClass().getMethod("onResume").invoke(web,(Object[])null);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//reader = (Button) findViewById(R.id.readss);
		//reader.setBackground(getResources().getDrawable(R.drawable.collect_yes));
		news_id = getIntent().getExtras().getInt("id");
		new_summary = getIntent().getExtras().getString("summary");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				g_new = findnewInList(news_id);
				String check =""+ news_id;
				Log.e("[--id]",check);
				String contentURL = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsById.action?id="+news_id;
				//String contentURL = "http://news.163.com/14/0510/12/9RSQ7GOM0001124J.html";
				web.loadUrl(contentURL);
				website.setText(contentURL);
				
			}
		} ).start();
		/*
		 * 
		 */
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
				int userid = pref.getInt("userid", 0);
				try {
					String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_checkCollect.action?uid="+userid+"&nid="+news_id ;
					final String json = GetJosnbyHttp.request(url);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Gson g =new Gson();
							ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
							if(jsonObj.status==200){
								collect_index = true;
								collect_id = jsonObj.userid;
								collectbut.setBackground(getResources().getDrawable(R.drawable.collect_yes));
							}else{
								collect_index = false;
								collectbut.setBackground(getResources().getDrawable(R.drawable.collect_no));
							}
							
							// TODO Auto-generated method stub
							
							
						}
					});
					Log.e("json",json);
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}}).start();
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_web_back:// 首页
			//Toast.makeText(WebSVeiwActivity.this, "==已监听到来自新闻查看页面返回键==", Toast.LENGTH_SHORT).show();
			WebSVeiwActivity.this.finish();
			tts.shutdown();
			tts_index = false;
			break;
		case R.id.btn_web_share:
			Toast.makeText(WebSVeiwActivity.this, "【点击了分享】"+g_new.title, Toast.LENGTH_SHORT).show();
			
		    Builder builder = new AlertDialog.Builder(WebSVeiwActivity.this);
			builder.setIcon(R.drawable.abc_ic_menu_share_holo_light);
			builder.setTitle("分享到微信");
			builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(WebSVeiwActivity.this, wxtext[which] , Toast.LENGTH_SHORT).show();
					if(isSharefriend(which))
					WXshare(true);
					else WXshare(false);
				}

			});
			builder.create().show();
			break;
		case R.id.readss:
			if(tts_index)
			{
				tts.stop();
				
				tts_index = false;
			}
			else
			{	
			tts.speak(new_summary,
					TextToSpeech.QUEUE_FLUSH, null);
			tts_index = true; 
			}
			
			break;
		case R.id.collectss:
			if(collect_index)
			{new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
					int userid = pref.getInt("userid", 0);
					try {
						String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_checkCollect.action?uid="+userid+"&nid="+news_id ;
						final String json = GetJosnbyHttp.request(url);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Gson g =new Gson();
								ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
								if(jsonObj.status==200){
									
									collect_id = jsonObj.userid;
									
								}
								
								// TODO Auto-generated method stub
								new Thread(new Runnable(){

									@Override
									public void run() {
										// TODO Auto-generated method stub
										SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
										int userid = pref.getInt("userid", 0);
										try {
											Log.e("[url前]", "---------");
											String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_delCollect.action?id="+collect_id;
											Log.e("[url后]", url);
											final String json = GetJosnbyHttp.request(url);
											runOnUiThread(new Runnable() {
												@Override
												public void run() {
													Gson g =new Gson();
													ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
													if(jsonObj.status==200){
														collect_index = false;
														collectbut.setBackground(getResources().getDrawable(R.drawable.collect_no));
														Toast.makeText(WebSVeiwActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
													}
												}
											});
											Log.e("json",json);
											
										} catch (MalformedURLException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}}).start();
								
							}
						});
						Log.e("json",json);
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}}).start();
				
			
			}
			else{
				new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
						int userid = pref.getInt("userid", 0);
						try {
							String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_checkCollect.action?uid="+userid+"&nid="+news_id ;
							final String json = GetJosnbyHttp.request(url);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Gson g =new Gson();
									ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
									if(jsonObj.status==200){
										
										collect_id = jsonObj.userid;
									
									}
									// TODO Auto-generated method stub
									SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
									final int userid = pref.getInt("userid", 0);
									if(userid==0){
										Toast.makeText(WebSVeiwActivity.this, "请登录后进行收藏", Toast.LENGTH_SHORT).show();
										return;
									}
									new Thread(new Runnable(){

										@Override
										public void run() {
											// TODO Auto-generated method stub
											
											try {
												String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileCollect_addCollect.action?collect.user.id="+userid+"&collect.news.id="+news_id;
												final String json = GetJosnbyHttp.request(url);
												runOnUiThread(new Runnable() {
													@Override
													public void run() {
														Gson g =new Gson();
														ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
														if(jsonObj.status==200){
															collect_index = true;
															collectbut.setBackground(getResources().getDrawable(R.drawable.collect_yes));
															Toast.makeText(WebSVeiwActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
														}
													}
												});
												Log.e("json",json);
												
											} catch (MalformedURLException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}}).start();
									
								}
							});
							Log.e("json",json);
							
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}}).start();
				
				
			}
			
			break;
		case R.id.tocomment:
			SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
			final int userid = pref.getInt("userid", 0);
			if(userid==0){
				Toast.makeText(WebSVeiwActivity.this, "请登录后进行评论", Toast.LENGTH_SHORT).show();
				return;
			}
			final String comment = new String(commenttext.getText().toString());
			if(comment.equals("")){
				Toast.makeText(WebSVeiwActivity.this, "请填写评论", Toast.LENGTH_SHORT).show();
				return;
			}
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					try {
						String comm = URLEncoder.encode(comment, "UTF-8");
						String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileComment_addCommentInMobile.action?comment.user.id="+userid+"&comment.news.id="+news_id+"&comment.content="+comm;
						final String json = GetJosnbyHttp.request(url);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Gson g =new Gson();
								ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
								if(jsonObj.status==200){
								
									Toast.makeText(WebSVeiwActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
									web.reload();
									commenttext.setText("");
								}
							}
						});
						Log.e("json",json);
						
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}}).start();
			break;
		/*case R.id.bottom_comment:
			Toast.makeText(WebSVeiwActivity.this, "==点击评论==", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(WebSVeiwActivity.this, CommentActivity.class);
			Bundle bundle =new Bundle();
			bundle.putInt("id",news_id);
			intent.putExtras(bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);*/
	   default:  break;
	}
	
	}
	
	public New findnewInList(int id){
		
		for(New ne:HomeActivity.newList){
			if(ne.id.equals(id))
				return ne;
		}
		
		return null; 
	}
	public void WXshare(final Boolean is){
		//1、创建一个相应媒体资源类型的Object WXTextObject，给对象的相应属性赋值
		//Bitmap bmp =  BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				   String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getNewsById.action?id="+news_id;
				   String url2 = "http://"+HomeActivity.serverIP+":9090/NewsRelease/ImagesUploaded/press/"+g_new.imgsuo;//1400241754502.jpg";//+g_new.imgsuo;
				   
				   URL u;
					try {
						u = new URL(url2);
						InputStream in = u.openStream();
						Bitmap bmp =  BitmapFactory.decodeStream(in);
						//WXImageObject imageObject = new WXImageObject(bmp);
					    WXWebpageObject webObject = new WXWebpageObject(url);
					    webObject.webpageUrl = url;  
						WXMediaMessage msg = new WXMediaMessage(webObject);  
					    msg.title = g_new.title;  
					    msg.description = g_new.summary;
					    msg.setThumbImage(bmp);
						//2、使用WXMediaMessage对多媒体资源对象进行包装，对其mediaObject属性进行赋值
						//WXMediaMessage msg = new WXMediaMessage();
						// WXWebpage
						//msg.mediaObject = imageObject;
						//msg.description="我是一个描述";
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						bmp.compress(Bitmap.CompressFormat.JPEG, 100, out); 
						msg.thumbData = out.toByteArray();
						bmp.recycle();
						 
						//3、创建SendMessageToWX.Req的对象实例，
						//对transaction--唯一标识（String类型）通常用一个时间随机数
						//对message属性赋值，其值为WXMediaMessage
						//对scene属性赋值 WXSceneTimeline--朋友圈  ,WXSceneSession--好友会话
						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = String.valueOf(System.currentTimeMillis());
						req.message = msg;
						if(is){
							req.scene = SendMessageToWX.Req.WXSceneSession;
						}
						else
							req.scene = SendMessageToWX.Req.WXSceneTimeline;
							//isSharedtoFriends.isChecked()?SendMessageToWX.Req.WXSceneTimeline:SendMessageToWX.Req.WXSceneSession;
						
						api.sendReq(req);
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
			}
		}).start();
		
		
	}
	public boolean isSharefriend(int i){
		if(i==0)
		return true;
		else return false;
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
	public void onInit(int status) {
		// TODO Auto-generated method stub
		if (status == TextToSpeech.SUCCESS)
		{
			
			int result = tts.setLanguage(Locale.CHINA);
			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED)
			{
				Toast.makeText(WebSVeiwActivity.this, "1111",
						Toast.LENGTH_LONG).show();
			}
		}
		
	}
  
}
