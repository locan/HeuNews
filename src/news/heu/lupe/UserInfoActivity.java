package news.heu.lupe;

import news.heu.lupe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class UserInfoActivity extends Activity implements OnClickListener {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userinfo);

		initView();
		
	}

	private void initView() {
		findViewById(R.id.btn_user_left_menu).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_user_left_menu:
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
}
