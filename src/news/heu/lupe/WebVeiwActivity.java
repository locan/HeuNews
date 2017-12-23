package news.heu.lupe;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class WebVeiwActivity extends Activity {
	RelativeLayout web_container;
	RelativeLayout web_top;
	RelativeLayout web_bottom;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.web_activity);
		web_top = (RelativeLayout) findViewById(R.id.webtop);
		web_container = (RelativeLayout) findViewById(R.id.webcontainer);
		//web_bottom = (RelativeLayout) findViewById(R.id.webbottom);
	}
	public void addSubWebView(View view){
		web_container.addView(view);
	}
	public RelativeLayout getTop(){
		return web_top;
	}
	
	public RelativeLayout getContainer(){
		return web_container;
	}
	public RelativeLayout getBottom(){
		return web_bottom;
	}
	
}
