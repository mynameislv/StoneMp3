package ls.stonemp3;

import java.util.List;
import java.util.Queue;

import ls.lrc.LrcContent;
import ls.lrc.LrcProcessor;
import ls.lrc.LrcView;
import ls.service.PlayService;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PlayerActivity extends Activity {
	private ImageButton begin = null;
	private ImageButton pastButton = null;
	private ImageButton nextButton = null;
	private ImageButton modeButton = null;

	private Handler handler = new Handler();
	// private UpdateTimeCallback updateTimeCallback = null;
	MediaPlayer mediaPlayer = null;
	private boolean isPlaying = true;
	private boolean isPause = false;
	private boolean isReleased = false;
	private boolean isOne = false;
	public static TextView mp3name;
	private TextView lrc;
	private Mp3Info mp3Info = null;
	private long beginLong = 0;
	private long nextTime = 0;
	private long currentTime = 0;
	private long pauseTime = 0;
	private int location = 0;

	public static LrcView lrcView;
	private List<LrcContent> lrcList = null;
	private List<Mp3Info> mp3Infos = null;
	private Receiver receiver;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play);
		isOne = false;
		Intent intent = getIntent();
		location = intent.getIntExtra("location", -1);
		System.out.println("location" + location);
		FileUtils fileUtils = new FileUtils();
		// 这句得到文件的名字
		mp3Infos = fileUtils.getName("mp3/");
		// 注册Broadcast；
		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		receiver = new Receiver();
		filter.addAction("ls.stone.localList");
		registerReceiver(receiver, filter);

		// System.out.println("register");

		mp3name = (TextView) findViewById(R.id.mp3name);
		// mp3Info = (Mp3Info) intent.getSerializableExtra("mp3info");
		mp3name.setText(mp3Infos.get(location).getMp3NameStr());
		// System.out.println("mp3Info___----->" + mp3Info);
		lrcView = (LrcView) findViewById(R.id.lrcShowView);
		begin = (ImageButton) findViewById(R.id.play);
		pastButton = (ImageButton) findViewById(R.id.past);
		nextButton = (ImageButton) findViewById(R.id.next);
		modeButton = (ImageButton) findViewById(R.id.mode);
		// System.out.println("mp3infoplayer---->"+mp3Info);
		begin.setOnClickListener(new BeginButton());
		begin.setBackgroundResource(R.drawable.play);
		modeButton.setBackgroundResource(R.drawable.all);
		pastButton.setOnClickListener(new PastButton());
		nextButton.setOnClickListener(new NextButton());
		modeButton.setOnClickListener(new ModeButton());
		firs_start();

	}

	class ModeButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (!isOne) {
				Intent intent_r = new Intent();
				intent_r.setAction("ls.stone.Play_mode");
				intent_r.putExtra("mode", 1);

				sendBroadcast(intent_r);

				modeButton.setBackgroundResource(R.drawable.one);
				isOne = true;
			}
			else if (isOne) {
				Intent intent_r = new Intent();
				intent_r.setAction("ls.stone.Play_mode");
				intent_r.putExtra("mode", 2);

				sendBroadcast(intent_r);
	
				modeButton.setBackgroundResource(R.drawable.all);
				isOne = false;

			}

		}
	}

	class PastButton implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent_r = new Intent();// 创建Intent对象
			if (location != 0) {
				intent_r.setAction("ls.stone.Play");
				intent_r.putExtra("past_last", 1);

				sendBroadcast(intent_r);
				mp3name.setText(mp3Infos.get(location - 1).getMp3NameStr());
				location = location - 1;// 发送广播
			} else {

				Toast toast = Toast.makeText(PlayerActivity.this, "",
						Toast.LENGTH_SHORT);
				toast.setText("没有上一首歌了");
				toast.show();
			}

		}
	}

	class NextButton implements OnClickListener {

		@SuppressLint("ShowToast")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
if(location<=mp3Infos.size()-1){
			Intent intent_r = new Intent();// 创建Intent对象
			intent_r.setAction("ls.stone.Play");
			intent_r.putExtra("past_last", 2);

			sendBroadcast(intent_r);
			mp3name.setText(mp3Infos.get(location + 1).getMp3NameStr());}
else {
	Toast toast = Toast.makeText(PlayerActivity.this, "",
			Toast.LENGTH_SHORT);
	toast.setText("没有下一首歌了");
	toast.show();
}// 发送广播

		}
	}

	class BeginButton implements OnClickListener {
		@SuppressLint("NewApi")
		public void onClick(View v) {
			if (!isPlaying) {
				start();

			} else if (!isPause) {
				isPause = true;
				isPlaying = false;
				begin.setBackgroundResource(R.drawable.play);
				Intent intent = new Intent();
				intent.setClass(PlayerActivity.this, PlayService.class);
				// intent.putExtra("mp3info_service", mp3Info);
				intent.putExtra("location", location);
				intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
				startService(intent);
				// handler.postDelayed(updateTimeCallback, 5);
				// pauseTime = System.currentTimeMillis();

			}

		}

	}

	@SuppressLint("NewApi")
	private void firs_start() {
		begin.setBackgroundResource(R.drawable.pause);

		isPlaying = true;
		isPause = false;
		Intent intent = new Intent();
		intent.setClass(PlayerActivity.this, PlayService.class);
		// intent.putExtra("mp3info_service", mp3Info);

		intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
		intent.putExtra("location", location);

		// prepareLrc(mp3Info.getLrcNameStr());
		startService(intent);

		// System.out.println("send");
		// beginLong = System.currentTimeMillis() - pauseTime + beginLong;
		// handler.postDelayed(updateTimeCallback, 5);
	}

	private void start() {
		begin.setBackgroundResource(R.drawable.pause);
		isPlaying = true;
		isPause = false;
		Intent intent = new Intent();
		intent.setClass(PlayerActivity.this, PlayService.class);
		// intent.putExtra("mp3info_service", mp3Info);
		intent.putExtra("MSG", 12);
		intent.putExtra("location", location);
		// prepareLrc(mp3Info.getLrcNameStr());

		// System.out.println("lrc.=-------->" + mp3Info.getLrcNameStr());
		startService(intent);
		// beginLong = System.currentTimeMillis() - pauseTime + beginLong;
		// handler.postDelayed(updateTimeCallback, 5);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	public class Receiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// System.out.println("bjhjuhjhj" + intent.getAction());
			// if (intent.getAction().equals("ls.stone.localList")) {
			// location = intent.getIntExtra("location", -1);
			// System.out.println("location" + location);
			// }
			//
		}

	}
}
