package ls.service;

import ls.download.HttpDownloader;
import ls.stonemp3.AppConstant;
import ls.stonemp3.Mp3Info;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class DownloadService extends Service {
	 private String mp3url=AppConstant.URL.BASE_URL;
	//private String mp3url = "http://192.168.22.1:8080/mp3/";

	// 每次点击一个条目时就会调用方法
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// 从intent中取出MP3info对象
		Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3info");
		// System.out.println("service---->"+mp3Info);
		// 生成一个下载线程。并将MP3info对象作为参数传递到线程中
		DownLoadTread downLoadTread = new DownLoadTread(mp3Info);
		Thread thread = new Thread(downLoadTread);
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class DownLoadTread implements Runnable {
		private Mp3Info mp3Info = null;

		public DownLoadTread(Mp3Info mp3Info) {
			// TODO Auto-generated constructor stub
			this.mp3Info = mp3Info;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String mp3UrlString = mp3url + mp3Info.getMp3NameStr();
			String lrcUrlString = mp3url + mp3Info.getLrcNameStr();
			// 根据MP3文件的名字生成下载地址
			HttpDownloader httpDownloader = new HttpDownloader();
			// System.out.println("mp3name------>" + mp3Info.getMp3NameStr());
			// 将文件下载下来，并存储到SD卡中。
			int result = httpDownloader.dowFile(mp3UrlString, "mp3/",
					mp3Info.getMp3NameStr());
			int result_lrc = httpDownloader.dowFile(lrcUrlString, "mp3/",
					mp3Info.getLrcNameStr());
			String resultMessage = null;
			if (result == -1) {
				resultMessage = "下载失败";

			} else if (result == 0) {
				resultMessage = "文件已经存在不需要再次下载";

			} else if (result == 1) {
				resultMessage = "下载成功";
			}
			// NotificationManager mNotificationManager = (NotificationManager)
			// getSystemService(Context.NOTIFICATION_SERVICE);
			// Notification mNotification = new Notification();
			// mNotification.icon = R.drawable.ic_launcher;
			// mNotification.tickerText = resultMessage;
			// mNotification.defaults = Notification.DEFAULT_SOUND;
			// mNotificationManager.notify(0, mNotification);

		}
	}

}
