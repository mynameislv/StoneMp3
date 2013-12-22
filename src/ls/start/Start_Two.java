package ls.start;

import ls.start.Start_One.Next;
import ls.stonemp3.MP3List;
import ls.stonemp3.MainActivity;
import ls.stonemp3.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class Start_Two extends Activity{
	private Button nextButton;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_two);
		nextButton=(Button)findViewById(R.id.next);
		nextButton.setOnClickListener(new Next());
	
	}
class Next implements OnClickListener{

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		intent.setClass(Start_Two.this, MainActivity.class);
		startActivity(intent);
		
		
	}}

	
}
