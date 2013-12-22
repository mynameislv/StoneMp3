package ls.stonemp3;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import ls.download.HttpDownloader;
import ls.service.DownloadService;
import ls.start.Start_One;
import android.os.Bundle;
import android.os.StrictMode;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MP3List extends ListActivity {
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	private List<Mp3Info> mp3Infos = null;
	private String mp3url = AppConstant.URL.BASE_URL;
	public static final int NOTIFICATION_ID = 0;
	private NotificationManager mNotificationManager;
	private SharedPreferences sharedPreferences;

	// private String mp3url="http://192.168.22.1:8080/mp3/";
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mp3_list);
		// �˴���Ϊ�˽��һ��xml�ļ��²�������ʱ���õĶ���
		sharedPreferences = getSharedPreferences("count",MODE_WORLD_READABLE);

		int count = sharedPreferences.getInt("count", 0);
		if (count == 0) {

			Intent intent = new Intent();

			intent.setClass(getApplicationContext(),Start_One.class);

			startActivity(intent);

			finish();

			}

			 

			Editor editor = sharedPreferences.edit();

			//��������

			editor.putInt("count", ++count);

			//�ύ�޸�

			editor.commit();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		NotificationCompat.Builder b = new NotificationCompat.Builder(
				MP3List.this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("StoneMp3���ڲ���").setContentText("");
		Notification n = b.build(); // ����Notification����

		// ��װһ��Intent
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(
				MP3List.this, 0, intent, 0);
		
		// ����֪ͨ�������ͼ
		n.contentIntent=resultPendingIntent;
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		n.flags = Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(NOTIFICATION_ID, n);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mNotificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		menu.add(0, UPDATE, 1, R.string.mp3list_update);
		menu.add(0, ABOUT, 2, R.string.mp3list_about);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// System.out.println("item------->"+item.getItemId());
		if (item.getItemId() == UPDATE) {
			updateListView();
		} else if (item.getItemId() == ABOUT) {
			// ���˿��ҵĹ����ˣ�����
		}
		return super.onOptionsItemSelected(item);
	}

	private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Infos) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			// ����ֻ��һ����������
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3NameStr());
			map.put("mp3_size", mp3Info.getMp3sizeStr());
			list.add(map);

		}
		// ����һ��simpleadapter����
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, list,
				R.layout.mp3info_item, new String[] { "mp3_name", "mp3_size" },
				new int[] { R.id.mp3_name, R.id.mp3_size });

		return simpleAdapter;
	}

	private void updateListView() {
		// ���ذ�����MP3������Ϣ���ļ�
		// �û��������б�
		String xmlString = downloadXML(mp3url + "resources.xml");
		// String xmlString =
		// downloadXML("http://192.168.22.1:8080/mp3/resources.xml");
		// System.out.println("down------>"+xmlString);
		// ��xml�ļ����н��������Ұѽ����Ľ���ŵ�MP3info�����С����MP3info�ŵ�List��
		mp3Infos = parse(xmlString);
		System.out.println("mp3" + mp3Infos);
		// ����һ��list���󡣲�����simpleadapter�ı�׼����MP3infos���е�������ӵ�List���С�
		SimpleAdapter simpleAdapter = buildSimpleAdapter(mp3Infos);
		setListAdapter(simpleAdapter);
	}

	private String downloadXML(String url) {
		HttpDownloader httpDownloader = new HttpDownloader();
		// String
		// result=httpDownloader.download("http://192.168.1.136:8081/mp3/resouces.xml");

		String result = httpDownloader.download(url);
		return result;
	}

	private List<Mp3Info> parse(String xmlString) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();

			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(
					infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlString)));
			for (Iterator iterator = infos.iterator(); iterator.hasNext();) {
				Mp3Info mp3Info = (Mp3Info) iterator.next();
				// System.out.println(mp3Info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Mp3Info mp3Info = mp3Infos.get(position);
		// System.out.println("mp3----->"+mp3Info);
		// //�����û�������б��е�λ�����ĵ���Ӧ��MP3info��
		Intent intent = new Intent();
		intent.putExtra("mp3info", mp3Info);
		intent.setClass(this, DownloadService.class);

		// ����service��
		startService(intent);
	}
}
