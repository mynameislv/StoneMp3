package ls.stonemp3;

import java.io.Serializable;

public class Mp3Info  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idString;
	private String mp3NameStr;
	private String mp3sizeStr;
	private String lrcNameStr;
	private String lrcSizeStr;
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	public Mp3Info() {
		super();
	}
	public Mp3Info(String idString, String mp3NameStr, String mp3sizeStr,
			String lrcNameStr, String lrcSizeStr) {
		super();
		this.idString = idString;
		this.mp3NameStr = mp3NameStr;
		this.mp3sizeStr = mp3sizeStr;
		this.lrcNameStr = lrcNameStr;
		this.lrcSizeStr = lrcSizeStr;
	}
	@Override
	public String toString() {
		return "Mp3Info [idString=" + idString + ", mp3NameStr=" + mp3NameStr
				+ ", mp3sizeStr=" + mp3sizeStr + ", lrcNameStr=" + lrcNameStr
				+ ", lrcSizeStr=" + lrcSizeStr + "]";
	}
	public String getMp3NameStr() {
		return mp3NameStr;
	}
	public void setMp3NameStr(String mp3NameStr) {
		this.mp3NameStr = mp3NameStr;
	}
	public String getMp3sizeStr() {
		return mp3sizeStr;
	}
	public void setMp3sizeStr(String mp3sizeStr) {
		this.mp3sizeStr = mp3sizeStr;
	}
	public String getLrcNameStr() {
		return lrcNameStr;
	}
	public void setLrcNameStr(String lrcNameStr) {
		this.lrcNameStr = lrcNameStr;
	}
	public String getLrcSizeStr() {
		return lrcSizeStr;
	}
	public void setLrcSizeStr(String lrcSizeStr) {
		this.lrcSizeStr = lrcSizeStr;
	}
	public String replaceAll(String string, String string2) {
		// TODO Auto-generated method stub
		return null;
	}

}
