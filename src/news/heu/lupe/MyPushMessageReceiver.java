package news.heu.lupe;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;

public class MyPushMessageReceiver extends FrontiaPushMessageReceiver{

	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		
	}

	@Override
	public void onDelTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListTags(Context arg0, int arg1, List<String> arg2,
			String arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context context, String message, String customContentString) {
		// TODO Auto-generated method stub
		String messageString = "透传消息 message=" + message + " customContentString="
				+ customContentString;
				//Log.d(TAG, messageString);
				// 自定义内容获取方式，mykey和myvalue对应透传消息推送时自定义内容中设置的键和值
				if (customContentString != null & customContentString != "") {
				JSONObject customJson = null;
				try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
				myvalue = customJson.getString("mykey");
				}
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				}
		
	}

	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		// TODO Auto-generated method stub
		String notifyString = "通知点击 title=" + title + " description="
				+ description + " customContent=" + customContentString;
				//Log.d(TAG, notifyString);
				// 自定义内容获取方式，mykey和myvalue对应通知推送时自定义内容中设置的键和值
				if (customContentString != null & customContentString != "") {
				JSONObject customJson = null;
				try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
				myvalue = customJson.getString("mykey");
				}
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
				}
		
	}

	@Override
	public void onSetTags(Context arg0, int arg1, List<String> arg2,
			List<String> arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnbind(Context arg0, int arg1, String arg2) {
		// TODO Auto-generated method stub
		
	}

}
