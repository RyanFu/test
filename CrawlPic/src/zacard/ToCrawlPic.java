package zacard;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToCrawlPic {
	String filePath="e:\\image";//�ļ�����λ��
	String picList="pic.txt";
	int num;//��ǰץȡ��ͼƬ����
	PrintWriter pw;
	File lsit,file;
	OutputStream out;
	DataInputStream dis;
	BufferedReader br;
	
	FileReader in;
    	LineNumberReader reader; //line nums
	
	public ToCrawlPic(){
		try {	
			
			file=new File(filePath);
			if(!file.exists()){//����������·�����򴴽�
				file.mkdirs();
			}			
			//�����嵥
			lsit=new File(filePath+"/"+picList);
			lsit.createNewFile();
			
			pw =new PrintWriter(new FileWriter(filePath+"/"+picList));
			br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath+"/"+picList))));
			
			pw.println("ͼƬ�嵥(��"+new Date()+")");
			pw.flush();
			
			in = new FileReader(new File(filePath+"/"+picList));
			reader = new LineNumberReader(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�ر���
	public void closeStream(){
		
	}
	
	//��ȡ�Ѿ����ڵ�ͼƬ����
	public int getNum(){
		int index=0;	
		try {
			while(reader.readLine()!=null){
				index++;
			}
			
//			while(br.readLine()!=null){
//				index++;
//			}
			System.out.println("index:"+index);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return index;
	}
	
	//����ץȡ��ͼƬ�б�
	public void savePicNameToFile(String name){
		try {
			//pw.write(name + "\n");
			pw.println(name);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//ץȡͼƬ
	public void catchPicByURL(String html_url){
		System.out.println("catchPicByURL:"+html_url);
		URL url;
		BufferedInputStream  bis;
		FileOutputStream fos;
		try {
			System.out.println("MSG:��ʼ��ȡͼƬ...");
			url=new URL(html_url);
			int index=getNum();
			//����Ϊ����+ͼƬԭ��������
			String picListName=html_url.substring(html_url.lastIndexOf("/")+1);
			System.out.println("picListName:"+picListName);
			bis=new BufferedInputStream(url.openStream());
			fos=new FileOutputStream(new File(filePath+"/"+index+picListName.trim()));
			int image;
			while((image=bis.read())!=-1){
				fos.write(image);
			}
			bis.close();
			fos.close();
			//ͬʱ����ͼƬlist
			savePicNameToFile(index+". "+picListName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//��ȡ��ҳhtml
	public String getHtmlCode(String _url){
		StringBuffer res=new StringBuffer("");
		URL url;
		BufferedReader br;
		try {
			url=new URL(_url);
			br=new BufferedReader (new InputStreamReader(url.openStream()));
			String content;
			while((content=br.readLine())!=null){
				res.append(content);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res.toString();
	}
	
	//������ҳ
	public void analyzeUrl(String _url){
		String searchImgReg = "(?x)(src|SRC)=('|\")(http://([\\w-]+\\.)+[\\w-]+(:[0-9]+)*(/[\\w-]+)*(/[\\w-]+\\.(jpg|JPG|png|PNG|gif|GIF)))('|\")";
		String htmlCode=getHtmlCode(_url);
		Pattern pattern=Pattern.compile(searchImgReg);
		Matcher matcher=pattern.matcher(htmlCode);
		int count=0;
		while(matcher.find()){
			catchPicByURL(matcher.group(3));
			count++;
		}
		System.out.println("һ��ץȡ��"+count+"��ͼƬ!!!");
		try {
			br.close();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		System.out.println("������ץȥ��ʼҳ�룺");
//		Scanner s=new Scanner(System.in);
//		int start=s.nextInt();
//		System.out.println("������ץȥ����ҳ�룺");
//		int ent=s.nextInt();
		String url="http://jandan.net/ooxx/page-806";
		ToCrawlPic tcp=new ToCrawlPic();
		tcp.analyzeUrl(url);
	}
	
}
