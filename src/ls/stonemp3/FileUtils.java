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
		// �õ��ⲿ�洢��Ŀ¼
		SDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator;
		// System.out.println("SDCardRoot-->"+SDCardRoot);
		// ����ͣ�ˣ�����
	}

	// ��sd���ϴ����ļ�
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

	// ��sd���ϴ���Ŀ¼

	public File creatSDDir(String dir) {

		File dirFile = new File(SDCardRoot + dir);
		System.out.println(dirFile.mkdir());
		return dirFile;

	}

	// �ж�sd���ϵ��ļ����Ƿ����
	public boolean isFileExist(String fileName, String path) {
		File file = new File(SDCardRoot + path + File.separator + fileName);
		// System.out.println(" �ļ��д��� ");
		return file.exists();
	}

	// ��һ��Inputstream��������� д��sd����
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
