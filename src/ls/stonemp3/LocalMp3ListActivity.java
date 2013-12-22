package ls.stonemp3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ls.service.PlayService;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocalMp3ListActivity extends ListActivity {
	private List<Mp3Info> mp3Infos = null;
	private myReceiver myReceiver = new myReceiver();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_mp3);

	}

	protected void onResume() {
		FileUtils fileUtils = new FileUtils();
		// 这句得到文件的名字
		mp3Infos = fileUtils.getName("mp3/");
		// 定义一个Hashmap；
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3NameStr());
			list.add(map);

		}
		// 上面用迭代把数据驾到list变量里面；
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name" },
				new int[] { R.id.mp3_name });
		setListAdapter(simpleAdapter);
		super.onResume();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		
		 IntentFilter filter = new IntentFilter();//创建IntentFilter对象
//         filter.addAction("ls.stone.localList"); 
         registerReceiver(myReceiver, filter);
         Intent intent_r= new Intent();//创建Intent对象
         intent_r.setAction("ls.stone.localList");
         intent_r.putExtra("destroy","destroy");
        
        
         sendBroadcast(intent_r);//发送广播
         //System.out.println("sendbroadcast");
       
		if (mp3Infos != null) {
			
			Intent intent = new Intent();
			
			intent.putExtra("location", position);
			intent.setClass(this, PlayerActivity.class);
			
			startActivity(intent);
		}
        
		  
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		this.unregisterReceiver(receiver);
		super.onDestroy();
	}
	public  class myReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
 