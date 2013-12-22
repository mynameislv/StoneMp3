package ls.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ls.lrc.LrcContent;
import ls.lrc.LrcProcessor;
import ls.stonemp3.AppConstant;
import ls.stonemp3.FileUtils;
import ls.stonemp3.Mp3Info;
import ls.stonemp3.PlayerActivity;

import android.R;
import android.R.integer;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class PlayService extends Service {
	MediaPlayer mediaPlayer = null;
	private LrcProcessor mLrcProcess; // 歌词处理
	private List<LrcContent> lrcList = new ArrayList<LrcContent>();
	private List<Mp3Info> mp3Infos;// 存放歌词列表对象
	private int index = 0;
	private int mode=2;
	private Mp3Info mp3Info = null;
	private Handler handler = new Handler();
	private Receiver receiver = new Receiver();
	private boolean isPlaying = true;
	private int location;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(receiver);
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// TODO Auto-generated method stub

		IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
		filter.addAction("ls.stone.localList");
		filter.addAction("ls.stone.Play");
		filter.addAction("ls.stone.Play_mode");
		registerReceiver(receiver, filter);

		FileUtils fileUtils = new FileUtils();
		// mp3Info = (Mp3Info) intent.getSerializableExtra("mp3info_service");
		mp3Infos = fileUtils.getName("mp3/");
		int MSG = intent.getIntExtra("MSG", 0);

		location = intent.getIntExtra("location", -1);

		// System.out.println("location----->" + location+mp3Infos);
		System.out.println("service");
		// System.out.println("----->"+destroy);

		if (mp3Infos != null) {

			if (MSG == AppConstant.PlayerMsg.PLAY_MSG) {
				play((String) mp3Infos.get(location).getMp3NameStr());
				isPlaying = true;
				lrcList=new ArrayList<LrcContent>();
				initLrc();

			} else {
				if (MSG == AppConstant.PlayerMsg.PAUSE_MSG) {
					pause();
					isPlaying = false;
				} else if (MSG == 12) {
					mediaPlayer.start();
				}
			}
		}
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				if(mode==1){
					mediaPlayer.start();
					initLrc();
				}
				else if(mode==2){
					location=location+1;
					if(location>mp3Infos.size()-1){
						location=0;
					}
					play((String) mp3Infos.get(location).getMp3NameStr());
					initLrc();
					PlayerActivity.mp3name.setText(mp3Infos.get(location).getMp3NameStr());
				}
			}
		} );
			  
		return super.onStartCommand(intent, flags, startId);
	}

	private void play(String mp3Info) {
		String pathString = getMp3path(mp3Info);
		mediaPlayer = MediaPlayer.create(PlayService.this,
				Uri.parse("file://" + pathString));
		isPlaying = true;
		mediaPlayer.start();
	}

	private void pause() {
		mediaPlayer.pause();
		isPlaying = false;

	}

	private String getMp3path(String mp3Info) {
		String sDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		String pathString = sDCardRoot + File.separator + "mp3/"
				+ File.separator + mp3Info;
		return pathString;
	}

	// 以下是歌词。。
	public void initLrc() {
		mLrcProcess = new LrcProcessor();
		// 读取歌词文件
		String lrc = mp3Infos.get(location).getLrcNameStr();
		mLrcProcess.readLRC(lrc);
		// System.out.println("lrc--->"+lrc);
		// 传回处理后的歌词文件

		lrcList = mLrcProcess.getLrcList();
		PlayerActivity.lrcView.setmLrcList(lrcList);
		// 切换带动画显示歌词
		// PlayerActivity.lrcView.setAnimation(AnimationUtils.loadAnimation(PlayService.this,R.anim.alpha_z));

		handler.post(mRunnable);
	}

	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			PlayerActivity.lrcView.setIndex(lrcIndex());
			PlayerActivity.lrcView.invalidate();
			handler.postDelayed(mRunnable, 100);
		}
	};

	public int lrcIndex() {
		int currentTime = 0;
		int duration = 0;
		if (isPlaying) {
			currentTime = mediaPlayer.getCurrentPosition();
			duration = mediaPlayer.getDuration();
		}
		if (currentTime < duration) {
			for (int i = 0; i < lrcList.size(); i++) {
				if (i < lrcList.size() - 1) {
					if (currentTime < lrcList.get(i).getLrcTime() && i == 0) {
						index = i;
					}
					if (currentTime > lrcList.get(i).getLrcTime()
							&& currentTime < lrcList.get(i + 1).getLrcTime()) {
						index = i;
					}
				}
				if (i == lrcList.size() - 1
						&& currentTime > lrcList.get(i).getLrcTime()) {
					index = i;
				}
			}
		}
		return index;
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			String destroy = intent.getStringExtra("destroy");
			//System.out.println("receiver");
			if (isPlaying&&!intent.getAction().equals("ls.stone.Play_mode")) {
				// System.out.println("isPlaying"+isPlaying);
				mediaPlayer.stop();
				mediaPlayer.release();
				isPlaying = false;

			}
			if (intent.getAction().equals("ls.stone.Play")) {
				location = intent.getIntExtra("location", 1);
				int past_last = intent.getIntExtra("past_last", -1);
				System.out.println("past"+past_last);
				if (past_last == 1) {
					

					
					mediaPlayer=new MediaPlayer();
					location = location - 1;
					play((String) mp3Infos.get(location).getMp3NameStr());
					initLrc();
					isPlaying=true;
				}

				
			}
			if (intent.getAction().equals("ls.stone.Play_mode")){
				mode=intent.getIntExtra("mode", -1);
				System.out.println("mode"+mode);
			}
		}

	}

}
