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
	String filePath="e:\\image";//文件存放位置
	String picList="pic.txt";
	int num;//当前抓取的图片数量
	PrintWriter pw;
	File lsit,file;
	OutputStream out;
	DataInputStream dis;
	BufferedReader br;
	
	FileReader in;
    LineNumberReader reader; 
	
	public ToCrawlPic(){
		try {	
			
			file=new File(filePath);
			if(!file.exists()){//如果不存在路径，则创建
				file.mkdirs();
			}			
			//创建清单
			lsit=new File(filePath+"/"+picList);
			lsit.createNewFile();
			
			pw =new PrintWriter(new FileWriter(filePath+"/"+picList));
			br=new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath+"/"+picList))));
			
			pw.println("图片清单(："+new Date()+")");
			pw.flush();
			
			in = new FileReader(new File(filePath+"/"+picList));
			reader = new LineNumberReader(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//关闭流
	public void closeStream(){
		
	}
	
	//读取已经存在的图片数量
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
	
	//保存抓取的图片列表
	public void savePicNameToFile(String name){
		try {
			//pw.write(name + "\n");
			pw.println(name);
			pw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//抓取图片
	public void catchPicByURL(String html_url){
		System.out.println("catchPicByURL:"+html_url);
		URL url;
		BufferedInputStream  bis;
		FileOutputStream fos;
		try {
			System.out.println("MSG:开始获取图片...");
			url=new URL(html_url);
			int index=getNum();
			//名字为序号+图片原本的名字
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
			//同时更新图片list
			savePicNameToFile(index+". "+picListName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//获取网页html
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
	
	//分析网页
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
		System.out.println("一共抓取了"+count+"张图片!!!");
		try {
			br.close();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
//		System.out.println("请输入抓去开始页码：");
//		Scanner s=new Scanner(System.in);
//		int start=s.nextInt();
//		System.out.println("请输入抓去结束页码：");
//		int ent=s.nextInt();
		String url="http://jandan.net/ooxx/page-806";
		ToCrawlPic tcp=new ToCrawlPic();
		tcp.analyzeUrl(url);
	}
	
}
