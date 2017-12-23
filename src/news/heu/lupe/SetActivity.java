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

public class SetActivity extends Activity implements OnClickListener {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set);

		initView();
		String cate_name = getIntent().getExtras().getString("category");
		Toast.makeText(SetActivity.this,"[SetActivity]"+cate_name, Toast.LENGTH_SHORT).show();
	}

	private void initView() {
		findViewById(R.id.btn_main_left_menu).setOnClickListener(this);
		findViewById(R.id.btn_main_share).setOnClickListener(this);
		findViewById(R.id.setIP).setOnClickListener(this);
		findViewById(R.id.setCategory).setOnClickListener(this);
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
		case R.id.btn_main_share:

			break;
		case R.id.setIP:
			Intent intent = new Intent(SetActivity.this, SetIpActivity.class);
			startActivity(intent);
			break;
		case R.id.setCategory:
			Intent intent2 = new Intent(SetActivity.this, SetCategoryActivity.class);
			startActivity(intent2);
		default:
			break;
		}
	}
}
