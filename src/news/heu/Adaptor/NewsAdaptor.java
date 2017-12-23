package news.heu.Adaptor;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import news.heu.entity.New;
import news.heu.lupe.HomeActivity;
import news.heu.lupe.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class NewsAdaptor  extends ArrayAdapter  {
	
	List<New> list ;
	Context con;
	LayoutInflater inflater;


public NewsAdaptor(Context context, int resource, List list) {
		super(context, resource, list);
		this.con = context;
		this.list = list;
		
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//View view  = inflater.inflate(R.layout.sub, null);
	}

	
	
	 
 

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public New getItem(int i) {
		// TODO Auto-generated method stub
		return list.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(convertView ==null ){
			convertView = inflater.inflate(R.layout.adapter_layout, null);
		} 
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView summary = (TextView) convertView.findViewById(R.id.summary);
		ImageView image  = (ImageView) convertView.findViewById(R.id.image);
		TextView nresoure = (TextView) convertView.findViewById(R.id.nsourse);
		TextView ncomment = (TextView) convertView.findViewById(R.id.ncomment);

		title.setText(getItem(position).title);
		summary.setText(getItem(position).summary);
		nresoure.setText(getItem(position).nsource);
		ncomment.setText(""+getItem(position).cnum);
		//title.setVisibility(View.GONE);
		//title.setVisibility(View.VISIBLE);
		//"http://"+serverIP+":9090/NewsRelease/news_getAllNewsSendToMobile.action"
		String url ="http://"+HomeActivity.serverIP+":9090/NewsRelease/ImagesUploaded/press/"+ getItem(position).imgsuo;
		if(!TextUtils.isEmpty(url)){
			queryImage(image,url);
			
		}

		//addListener(convertView,position);
		return convertView;
	}
	/*public void addListener(View convertView,final int position) {  
			
	       ((Button)convertView.findViewById(R.id.domore)).setOnClickListener(    
	               new View.OnClickListener() {    
	                   @Override    
	                   public void onClick(View v) {    
	                	   	 
	                         Toast.makeText(getContext(), getItem(position).title, Toast.LENGTH_LONG).show();
	                         tts.speak(getItem(position).title,TextToSpeech.QUEUE_FLUSH, null);
	                   }    
	               });    
	    }*/
   void queryImage(final ImageView image,final String url){
	   
	   new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
		try {
			URL u = new URL(url);
			InputStream in = u.openStream();
			final Bitmap bmp = BitmapFactory.decodeStream(in);
			if(bmp!=null){
				image.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						image.setImageBitmap(bmp);
						image.setScaleType(ScaleType.CENTER_CROP);
					
					}
				});
				
			}
			
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








}
