package news.heu.Adaptor;
import java.util.ArrayList;
import java.util.List;
import news.heu.entity.MCategory;
import news.heu.lupe.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryAdaptor  extends ArrayAdapter {
	List<MCategory> list ;
	Context con;
	LayoutInflater inflater;
public CategoryAdaptor(Context context, int resource, List list) {
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
	public MCategory getItem(int i) {
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
			convertView = inflater.inflate(R.layout.adapter_category_layout, null);
		} 
		final Button butin = (Button) convertView.findViewById(R.id.but_insert);
		TextView catecory_name = (TextView) convertView.findViewById(R.id.set_category_name);
		catecory_name.setText(getItem(position).name);
		Log.e("[分类name:]",getItem(position).name);
		final SQLiteDatabase db = getContext().openOrCreateDatabase("NewHLS.db", Context.MODE_PRIVATE, null); 
		 Cursor cc = db.rawQuery("SELECT * FROM cateper WHERE cid="+getItem(position).id,null);  
		 int oi = cc.getCount();
		if(oi==1){
			butin.setText("已选择");
		}
		butin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				// TODO Auto-generated method stub
				//Toast.makeText(getContext(), getItem(position).name, Toast.LENGTH_SHORT).show();
				
				Cursor c = db.rawQuery("SELECT * FROM cateper ",null);  
				Cursor cc = db.rawQuery("SELECT * FROM cateper WHERE cid="+getItem(position).id,null);  
				int  oi = cc.getCount();
				Log.e("[测试存在]", ""+oi);
				 int ii= c.getCount();
				 
				 if(ii<4&&oi<1){
					 
				db.execSQL("INSERT INTO cateper VALUES (NULL, ?, ?)", new Object[]{getItem(position).id,getItem(position).name});
				butin.setText("已选择");
				//butin.setEnabled(false);
				 }
				 else
				 {
					Toast.makeText(getContext(), "您已选择4个分类！", Toast.LENGTH_SHORT).show(); 
				 }
				 if(oi==1)
				 {
					 db.execSQL("DELETE FROM cateper WHERE cid = "+getItem(position).id);
					 butin.setText("选择");
				 }
				 Log.e("[(]喜欢的分类[)]",""+ii);
				 c = db.rawQuery("SELECT * FROM cateper ",null);  
				 List<MCategory> mclist = new ArrayList<MCategory>();
				
			        while (c.moveToNext()) {  
			        	
			        	
			            int _id = c.getInt(c.getColumnIndex("_id"));  
			            String name = c.getString(c.getColumnIndex("name"));  
			            int cid = c.getInt(c.getColumnIndex("cid"));  
			           
			            Log.e("db", "_id=>" + _id + ", name=>" + name + ", cid=>" + cid);  
			        } 
			        c.close();      
			}
		});
		return convertView;
	}

}
