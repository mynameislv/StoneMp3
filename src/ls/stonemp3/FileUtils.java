package ls.stonemp3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileUtils {
	private String SDCardRoot;

	public FileUtils() {
		// 得到外部存储的目录
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		// System.out.println("SDCardRoot-->"+SDCardRoot);
		// 到这停了？？？
	}

	// 在sd卡上创建文件
	public File creatFileInSDCardFile(String fileName, String path)
			throws IOException {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		file.createNewFile();
		System.out.println("file-->" + file);

		return file;

	}

	// public File creatSDFile(String fileName) throws IOException {
	// File file = new File(SDCardRoot + fileName);
	// file.createNewFile();
	// return file;
	// }

	// 在sd卡上创建目录

	public File creatSDDir(String dir) {

		File dirFile = new File(SDCardRoot + dir);
		System.out.println(dirFile.mkdir());
		return dirFile;

	}

	// 判断sd卡上的文件夹是否存在
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		// System.out.println(" 文件夹存在 ");
		return file.exists();
	}

	// 讲一个Inputstream里面的数据 写到sd卡中
	public File write2SDCardFromInput(String path, String fileName,
			InputStream input) {

		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			// file=creatSDDir(path+fileName);
			file = creatFileInSDCardFile(fileName, path);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int temp;
			while ((temp = input.read(buffer)) != -1) {
				output.write(buffer, 0, temp);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;

	}

	public List<Mp3Info> getName(String path) {
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
		File file = new File(SDCardRoot + File.separator + path);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith("mp3")) {
				Mp3Info mp3Info = new Mp3Info();
				mp3Info.setMp3NameStr(files[i].getName());
				String lrc = files[i].getName().replaceAll(".mp3", ".lrc");
				mp3Info.setLrcNameStr(lrc);
				mp3Infos.add(mp3Info);
			}

		}
		return mp3Infos;

	}
}
