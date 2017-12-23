package news.heu.run;

 
 

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import news.heu.entity.New;
import news.heu.entity.PushNewsJson;
import news.heu.entity.ResponseJson;
import news.heu.lupe.HomeActivity;
import news.heu.lupe.MainActivity;
import news.heu.lupe.R;
import news.heu.util.GetJosnbyHttp;
import news.heu.util.JsonTools;

import com.google.gson.Gson;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 


public class PullMessageRunnable implements Runnable {
	private Context ctx;
	private String pullMsgUrl;
	private String sid;
	 
	public static  int NOTIFY_ID = 0x00000001;
	
	public PullMessageRunnable(Context ctx,String userid) {
		this.ctx = ctx;
		this.sid = userid;
		
		 
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int icon = R.drawable.ic_launcher;        				
        long when = System.currentTimeMillis();
			String url = "http://"+HomeActivity.serverIP+":9090/NewsRelease/pushNews_getAllPushNews.action";
			 
			try {
				String josnAll = GetJosnbyHttp.request(url);
				Log.e("resposne",josnAll+"--");
				List<PushNewsJson> pusl = new ArrayList<PushNewsJson>();
				pusl = JsonTools.getJsonListPushNews(josnAll);
				for(PushNewsJson p:pusl){
					String u ="http://"+HomeActivity.serverIP+":9090/NewsRelease/pushMobileNews_checkAndroid.action?id="+sid+"&nid="+p.id;
					String resjson = GetJosnbyHttp.request(u);
					Gson g =new Gson();
					ResponseJson jsonObj = g.fromJson(resjson, ResponseJson.class);
					if(jsonObj.status==200)
					{   String uu = "http://"+HomeActivity.serverIP+":9090/NewsRelease/news_getMobileNewsById.action?id="+p.nid;
					    String newjson = GetJosnbyHttp.request(uu);
					    New n = g.fromJson(newjson, New.class);
					    
						Intent intent = new Intent(ctx,MainActivity.class);
						PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
						Notification notification = new Notification(icon, n.title, when);
				        notification.setLatestEventInfo(ctx, n.title, n.category, contentIntent);
				        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				        notification.defaults  = Notification.DEFAULT_ALL;
				        notificationManager.notify(NOTIFY_ID++, notification);
						
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	}
	 
}
