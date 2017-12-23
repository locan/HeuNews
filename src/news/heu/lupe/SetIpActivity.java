package news.heu.lupe;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SetIpActivity extends Activity implements OnClickListener {

	ImageView image_back;
	ImageView set_OK;
	EditText input_ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_ip_activity);
		image_back = (ImageView) findViewById(R.id.btn_set_back);
		image_back.setOnClickListener(this);
		input_ip = (EditText)findViewById(R.id.setIp_text);
		input_ip.setHint(HomeActivity.serverIP);
		set_OK = (ImageView) findViewById(R.id.set_ok);
		set_OK.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_set_back:
			Toast.makeText(SetIpActivity.this, "==已监听到来自评论页面的返回键==", Toast.LENGTH_SHORT).show();
			SetIpActivity.this.finish();
			break;
		case R.id.set_ok:
			HomeActivity.setserverIP(input_ip.getText().toString());
			Toast.makeText(SetIpActivity.this, "设置服务IP为:" +input_ip.getText().toString() , Toast.LENGTH_SHORT).show();
			//input_ip.getText();
			break;
		}
		
		
	}
	
	

}
