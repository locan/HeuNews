package news.heu.lupe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.zip.Inflater;

import com.google.gson.Gson;

import news.heu.entity.ResponseJson;
import news.heu.lupe.R;
import news.heu.util.GetJosnbyHttp;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UserLoginActivity extends Activity implements OnClickListener {
	EditText username;
	EditText password;
	String json;
	String user; 
	String psw ;
	String url;
	MainActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initView();
		
	}

	private void initView() {
		findViewById(R.id.btn_login_left_menu).setOnClickListener(this);
		findViewById(R.id.login).setOnClickListener(this);
		findViewById(R.id.registers).setOnClickListener(this);
		username = (EditText) findViewById(R.id.accounts);
		password = (EditText) findViewById(R.id.password);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login_left_menu:
			if (MainActivity.slidingMenuView.getCurrentScreen() == 1) {
				MainActivity.slidingMenuView.snapToScreen(0);
			} else {
				MainActivity.slidingMenuView.snapToScreen(1);
			}
			break;
		case R.id.login:
			 user = username.getText().toString();
			 psw = password.getText().toString();
			url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileUser_loginUsersInMobile.action?user.username="+user+"&user.password="+psw;
			if(TextUtils.isEmpty(user)||TextUtils.isEmpty(psw)){
				Toast.makeText(UserLoginActivity.this, "请输入完整的信息", Toast.LENGTH_LONG).show();
				return;
			}
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					try {
						json = GetJosnbyHttp.request(url);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Gson g =new Gson();
								ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
								if(jsonObj.status==200){
									int userid = jsonObj.userid;
									SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
									pref.edit().putString("username", user).commit();
									pref.edit().putInt("userid", userid).commit();
									Toast.makeText(UserLoginActivity.this,"成功登陆", Toast.LENGTH_LONG).show();
									Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
									UserLoginActivity.this.finish();
									startActivity(intent);
									
								}else{
									Toast.makeText(UserLoginActivity.this,jsonObj.msg, Toast.LENGTH_LONG).show();
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
			
			
			break;
		case R.id.registers:
			
			 user = username.getText().toString();
			 psw = password.getText().toString();
			 try {
				final String myName = URLEncoder.encode(user, "UTF-8");
				String sex = URLEncoder.encode("保密", "UTF-8");
				 url =new String("http://"+HomeActivity.serverIP+":9090/NewsRelease/mobileUser_addUserInMobile.action?user.username="+myName+"&user.password="+psw+"&user.sex="+sex);
					if(TextUtils.isEmpty(user)||TextUtils.isEmpty(psw)){
						Toast.makeText(UserLoginActivity.this, "请输入完整的信息", Toast.LENGTH_LONG).show();
						return;
					}
					new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							try {
								Log.e("【注册】", "jjjjjj");
								json = GetJosnbyHttp.request(url);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Gson g =new Gson();
										ResponseJson jsonObj = g.fromJson(json, ResponseJson.class);
										if(jsonObj.status==200){
											int userid = jsonObj.userid;
											
											SharedPreferences pref = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
											pref.edit().putString("username", myName).commit();
											pref.edit().putInt("userid", userid).commit();
											Toast.makeText(UserLoginActivity.this,"注册成功！", Toast.LENGTH_LONG).show();
											
										}else{
											Toast.makeText(UserLoginActivity.this,jsonObj.msg, Toast.LENGTH_LONG).show();
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
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
		default:
			break;
		}
	}
}
