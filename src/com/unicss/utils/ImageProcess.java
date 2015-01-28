package com.unicss.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.log4j.Logger;

/**
 * 验证码下载工具类
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-1-27 下午10:45:57
 */
public class ImageProcess {
	private static final Logger logger = Logger.getLogger(ImageProcess.class);
	private static final String DOWNLOAD_DIR = System.getProperty("user.dir")+"/download/";
	private String url;
	public ImageProcess(String url){
		this.url = url;
	}
	public ImageProcess(){
		
	}
	/**
	 * 下载验证码
	 * @throws Exception
	 */
    public void downloadImage() throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getMethod = new HttpGet(url);
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            HttpResponse response = httpClient.execute(getMethod, new BasicHttpContext());
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent(); 
            OutputStream outstream = new FileOutputStream(new File(DOWNLOAD_DIR, df.format(new Date())+ ".jpg"));
            int l = -1;
            byte[] tmp = new byte[2048]; 
            while ((l = instream.read(tmp)) != -1) {
                outstream.write(tmp);
            } 
            outstream.close();
        } finally {
            getMethod.releaseConnection();
        }
        logger.info("下载验证码完毕！");
    }
    
    /** 
     * 复制单个文件 
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean 
     */ 
   public void copyFile(String oldPath, String newPath) { 
       try { 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[2048]; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           logger.error("复制单个文件操作出错",e); 
       } 
   } 
   /**
    * 抓取网页信息
    */
   public void captureHtml(String strURL) throws Exception {
		//String strURL = "http://ip.chinaz.com/?IP=" + ip;
		URL url = new URL(strURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		InputStreamReader input = new InputStreamReader(httpConn
				.getInputStream(), "utf-8");
		BufferedReader bufReader = new BufferedReader(input);
		String line = "";
		StringBuilder contentBuf = new StringBuilder();
		while ((line = bufReader.readLine()) != null) {
			contentBuf.append(line);
		}
		String buf = contentBuf.toString();
		System.out.println(buf.toString());
		/*int beginIx = buf.indexOf("查询结果[");
		int endIx = buf.indexOf("上面三项依次显示的是");
		String result = buf.substring(beginIx, endIx);
		System.out.println("captureHtml()的结果：\n" + result);*/
	}
   
   public static void main(String[] args) {
	   ImageProcess process = new ImageProcess();
	   try {
		process.captureHtml("https://passport.csdn.net/account/register");
	} catch (Exception e) {
		e.printStackTrace();
	}
   }
}
