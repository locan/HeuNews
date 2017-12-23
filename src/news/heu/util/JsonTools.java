package news.heu.util;

import java.util.List;

import news.heu.entity.MCategory;
import news.heu.entity.New;
import news.heu.entity.PushNewsJson;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonTools {
	public static List<New> getJsonList(String json){
		Gson gson = new Gson();
		//Log.e(tag, msg)
		TypeToken<List<New>> tt = new TypeToken<List<New>>(){};
		final List<New> list = gson.fromJson(json, tt.getType());
		
		return list;
	}
	public static List<MCategory> getJsonListMcategory(String json){
		Gson gson = new Gson();
		//Log.e(tag, msg)
		TypeToken<List<MCategory>> tt = new TypeToken<List<MCategory>>(){};
		final List<MCategory> list = gson.fromJson(json, tt.getType());
		
		return list;
	}
	public static List<PushNewsJson> getJsonListPushNews(String json){
		Gson gson = new Gson();
		//Log.e(tag, msg)
		TypeToken<List<PushNewsJson>> tt = new TypeToken<List<PushNewsJson>>(){};
		final List<PushNewsJson> list = gson.fromJson(json, tt.getType());
		
		return list;
	}


}
