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
	private List<LrcContent> lrcList; // List���ϴ�Ÿ�����ݶ���
	private LrcContent mLrcContent; // ����һ��������ݶ���

	/**�޲ι��캯������ʵ��������
	 * 
	 */
	public LrcProcessor() {
		mLrcContent = new LrcContent();
		lrcList = new ArrayList<LrcContent>();
	}

	/**
	 * ��ȡ���
	 * 
	 * @param path
	 * @return
	 */
	public String readLRC(String lrcName) {

		// ����һ��StringBuilder����������Ÿ������
		StringBuilder stringBuilder = new StringBuilder();

		try {
			// ����һ���ļ�����������
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
				// �滻�ַ�
				s = s.replace("[", "");
				s = s.replace("]", "@");
				// ���롰@���ַ�
				String splitLrcData[] = s.split("@");
				if (splitLrcData.length > 1) {
					mLrcContent.setLrcStr(splitLrcData[1]);
					// ������ȡ�ø�����ʱ��
					int lrcTime = time2Str(splitLrcData[0]);

					mLrcContent.setLrcTime(lrcTime);

					// ��ӽ��б�����
					lrcList.add(mLrcContent);

					// �´���������ݶ���
					mLrcContent = new LrcContent();
				}
			}
		}
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// stringBuilder.append("ľ�и���ļ����Ͻ�ȥ���أ�...");
		// }
		catch (IOException e) {
			e.printStackTrace();
			stringBuilder.append("ľ�ж�ȡ�����Ŷ��");
		}
		return stringBuilder.toString();
	}

	/**
	 * �������ʱ�� ������ݸ�ʽ���£� [00:02.32]����Ѹ [00:03.43]�þò��� [00:05.22]������� ����
	 * 
	 * @param timeStr
	 * @return
	 */
	public int time2Str(String timeStr) {
		timeStr = timeStr.replace(":", ".");
		timeStr = timeStr.replace(".", "@");

		String timeData[] = timeStr.split("@"); // ��ʱ��ָ����ַ�������

		// ������֡��벢ת��Ϊ����
		int minute = Integer.parseInt(timeData[0]);
		int second = Integer.parseInt(timeData[1]);
		int millisecond = Integer.parseInt(timeData[2]);

		// ������һ������һ�е�ʱ��ת��Ϊ������
		int currentTime = (minute * 60 + second) * 1000 + millisecond * 10;
		return currentTime;
	}

	public List<LrcContent> getLrcList() {
		return lrcList;
	}
	// public ArrayList<Queue> process(InputStream inputStream){
	// //���ʱ���
	// Queue<Long>timeMills=new LinkedList<Long>();
	// //���ʱ����Ӧ�ĸ��
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
	// //�����ӡ���ȫ��ת����ms��
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
