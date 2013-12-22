package ls.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.auth.MalformedChallengeException;

import ls.stonemp3.FileUtils;
import ls.stonemp3.MP3List;
import ls.stonemp3.PlayerActivity;

import android.R.integer;
import android.widget.Toast;

public class HttpDownloader {
	

public String download(String urlStr){
	StringBuffer sbBuffer= new StringBuffer();
	String line= null;
	BufferedReader buffer=null;
	try{
		URL url=new URL(urlStr);
		System.out.println("down"+urlStr);
		HttpURLConnection urlConn=(HttpURLConnection)url.openConnection();
		buffer =new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		//System.out.println("down"+urlStr);
		while ((line=buffer.readLine())!=null){
			sbBuffer.append(line);
		}
	}catch(Exception e){
		e.printStackTrace();
		
	}finally{
		try {
			buffer.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	return sbBuffer.toString();
}

public int dowFile (String urlStr, String path,String fileName){
	InputStream inputStream =null;
	try {FileUtils fileUtils=new FileUtils();
	if (fileUtils.isFileExist(fileName, path)) {
		return 1;
		
	}else {
		inputStream =getinInputStreamFromUrl(urlStr);
		File resultFlie= fileUtils.write2SDCardFromInput(path, fileName, inputStream);
		if(resultFlie==null){
			return -1;
		}
	}
		} catch (Exception e ){
			e.printStackTrace();
			return -1;
		}finally{
			try{
				inputStream.close();
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	return 0;
}
private URL url;
public InputStream getinInputStreamFromUrl (String 	urlStr) 
	throws MalformedURLException,IOException{
		 url=new URL(urlStr);
		HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
		InputStream inputStream=urlConnection.getInputStream();
//		urlConnection.setRequestMethod("GET");
//		urlConnection.connect();
		return inputStream;
	
	
}
}