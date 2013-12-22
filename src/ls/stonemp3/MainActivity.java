package ls.stonemp3;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity{
@Override
protected void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.main);
	TabHost tabHost=getTabHost();
	Intent remoteIntent=new Intent();
	remoteIntent.setClass(this, MP3List.class);
	TabHost.TabSpec remotesSpec= tabHost.newTabSpec("���ظ���");
	remotesSpec.setIndicator("���ظ���");
	remotesSpec.setContent(remoteIntent);
	tabHost.addTab(remotesSpec);
	Intent localintent=new Intent();
	localintent.setClass(this, LocalMp3ListActivity.class);
	TabHost.TabSpec locSpec=tabHost.newTabSpec("�����б�");
	locSpec.setIndicator("�����б�");
	locSpec.setContent(localintent);
	tabHost.addTab(locSpec);
	
}
}
