package ls.stonemp3;

import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Mp3ListContentHandler extends DefaultHandler{
	private List<Mp3Info> infos=null;
	private Mp3Info mp3Info=null;
	private String tagname=null;

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String tempString=new String(ch,start,length);
		// TODO Auto-generated method stub
		if(tagname.equals("id"))
		{
			mp3Info.setIdString(tempString);
		}else if(tagname.equals("mp3.name")){  
			mp3Info.setMp3NameStr(tempString);
			 
		}
		else if(tagname.equals("mp3.size")){
			mp3Info.setMp3sizeStr(tempString);
			
		}
		else if(tagname.equals("lrc.name")){
			mp3Info.setLrcNameStr(tempString);
			
		}else if(tagname.equals("lrc.size")){
			mp3Info.setLrcSizeStr(tempString);
			
		}
	}

	public Mp3ListContentHandler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

	public List<Mp3Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		if(qName.equals("resource")){
		infos.add(mp3Info);
		
		
	}
	tagname="";
		
	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		this.tagname=localName;
		if(tagname.equals("resource"))
			mp3Info=new Mp3Info();
		
	}

}
