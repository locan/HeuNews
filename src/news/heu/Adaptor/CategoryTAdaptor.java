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

public class CategoryTAdaptor  extends ArrayAdapter {
	List<MCategory> list ;
	Context con;
	LayoutInflater inflater;
public CategoryTAdaptor(Context context, int resource, List list) {
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
			convertView = inflater.inflate(R.layout.adapter_menu_layout, null);
		} 
		
		TextView category_name = (TextView) convertView.findViewById(R.id.cateT);
		category_name.setText(getItem(position).name);
		Log.e("[分类name:]",getItem(position).name);
	/*	category_name.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getContext(), getItem(position).name, Toast.LENGTH_SHORT).show();
			}
		});*/
			
			
		return convertView;
	}

}
