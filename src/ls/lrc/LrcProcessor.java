package ls.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ls.stonemp3.Mp3Info;

import android.R.integer;
import android.content.Intent;
import android.os.Environment;

public class LrcProcessor {
	private List<LrcContent> lrcList; // List集合存放歌词内容对象
	private LrcContent mLrcContent; // 声明一个歌词内容对象

	/**无参构造函数用来实例化对象
	 * 
	 */
	public LrcProcessor() {
		mLrcContent = new LrcContent();
		lrcList = new ArrayList<LrcContent>();
	}

	/**
	 * 读取歌词
	 * 
	 * @param path
	 * @return
	 */
	public String readLRC(String lrcName) {

		// 定义一个StringBuilder对象，用来存放歌词内容
		StringBuilder stringBuilder = new StringBuilder();

		try {
			// 创建一个文件输入流对象
			File file=new File(Environment
					.getExternalStorageDirectory().getAbsoluteFile()
					+ File.separator + "mp3/" + File.separator + lrcName);
			FileInputStream fileInputStream = new FileInputStream(file);
			System.out.println("fileis---->"+fileInputStream+lrcName);
			
			InputStreamReader inputStreamReader = new InputStreamReader(
					fileInputStream, "utf-8");
			BufferedReader br = new BufferedReader(inputStreamReader);
			String s = "";
			while ((s = br.readLine()) != null) {
				// 替换字符
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// 分离“@”字符
				String splitLrcData[] = s.split("@");
				if (splitLrcData.length > 1) {
					mLrcContent.setLrcStr(splitLrcData[1]);
					// 处理歌词取得歌曲的时间
					int lrcTime = time2Str(splitLrcData[0]);

					mLrcContent.setLrcTime(lrcTime);

					// 添加进列表数组
					lrcList.add(mLrcContent);

					// 新创建歌词内容对象
					mLrcContent = new LrcContent();
				}
			}
		}
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// stringBuilder.append("木有歌词文件，赶紧去下载！...");
		// }
		catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("木有读取到歌词哦！");
		}
		return stringBuilder.toString();
	}

	/**
	 * 解析歌词时间 歌词内容格式如下： [00:02.32]陈奕迅 [00:03.43]好久不见 [00:05.22]歌词制作 王涛
	 * 
	 * @param timeStr
	 * @return
	 */
	public int time2Str(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");

		String timeData[] = timeStr.split("@"); // 将时间分隔成字符串数组

		// 分离出分、秒并转换为整型
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// 计算上一行与下一行的时间转换为毫秒数
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List<LrcContent> getLrcList() {
		return lrcList;
	}
	// public ArrayList<Queue> process(InputStream inputStream){
	// //存放时间点
	// Queue<Long>timeMills=new LinkedList<Long>();
	// //存放时间点对应的歌词
	// Queue<String>messages=new LinkedList<String>();
	// ArrayList<Queue>queues=new ArrayList<Queue>();
	// try {
	// InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
	// BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
	// String temp=null;
	// int i=0;
	// Pattern p= Pattern.compile("\\[([^\\]]+)\\]");
	// String result =null;
	// boolean b=true;
	// while ((temp=bufferedReader.readLine())!=null){
	// i++;
	// Matcher matcher= p.matcher(temp);
	// if(matcher.find()){
	// if(result!=null){
	// messages.add(result);
	// }
	// String timeStr=matcher.group();
	// Long timeMill =time2Long(timeStr.substring(1,timeStr.length()-1));
	// if(b){
	// timeMills.offer(timeMill);
	//
	// }
	// String msg=temp.substring(10);
	// result=""+msg+"\n";
	// }else {
	// result=result+temp+"\n";
	// }
	// }
	// messages.add(result);
	// queues.add(timeMills);
	// queues.add(messages);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// return queues;
	//
	// }
	// //将分钟。秒全部转化成ms。
	//
	// public Long time2Long(String substring) {
	//
	// // TODO Auto-generated method stub
	// String s[]=substring.split(":");
	// int min=Integer.parseInt(s[0]);
	// String ss[]=s[1].split("\\.");
	// int sec=Integer.parseInt(ss[0]);
	// int mill=Integer.parseInt(ss[1]);
	// return min*60*1000+sec*1000+mill*10L;
	// }

}
