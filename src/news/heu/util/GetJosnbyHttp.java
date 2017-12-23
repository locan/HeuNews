package news.heu.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class GetJosnbyHttp {
	
	
	public static String request(String url) throws IOException
	{
		String json = null;
		try {
			URL u =  new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(6000);
			conn.setRequestMethod("POST");
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while(true){
				int len = in.read(buffer);
				if(len == -1){break;}
				out.write(buffer,0,len);
			}
			
			byte[] result = out.toByteArray();
			
			json = new String(result,"GBK");
			Log.e("json",json);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return json;
	}

}
