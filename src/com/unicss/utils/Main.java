package com.unicss.utils;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * 程序入口类
 * @author lenzhao
 * @email lenzhao@foxmail.com
 * @date2015-1-27 下午10:47:17
 */
public class Main {
	private static final Logger logger = Logger.getLogger(Main.class);
	private static final String DOWNLOAD_DIR = System.getProperty("user.dir")+"/download/";
	private static final String DONE_DIR = System.getProperty("user.dir")+"/done/";
	public static void main(String[] args) {
		File doneDir = new File(DONE_DIR);
		if(!doneDir.exists()){
			doneDir.mkdir();
		}
		File downloadDir = new File(DOWNLOAD_DIR);
		if(!downloadDir.exists()){
			downloadDir.mkdir();
		}
		Main main = new Main();
		String validateCode = main.getValidateCode(args[0]);
		logger.info(String.format("最终生成的验证码<%s>, 长度<%s>, ", validateCode,validateCode.length()));
	}
	/**
	 * 获取验证码
	 * @param url
	 * @return
	 */
	public String getValidateCode(String url){
		ImageProcess process = new ImageProcess(url);
		try {
			process.downloadImage();
		} catch (Exception e) {
			logger.error("下载验证码图像失败", e);
		}
		OCR ocr = new OCR();
		File fileDir = new File(DOWNLOAD_DIR);
		String result = "";
		 if(fileDir.isDirectory()){
			 File[] files = fileDir.listFiles();
			 for(File file : files){
				 try {
					 String temp =  ocr.recognizeText(file, "jpg");
						char[] chs = temp.trim().toCharArray();
						for(char ch : chs){
							result += String.valueOf(ch).trim();
						}
						File doneFile = new File(DONE_DIR,file.getName());
						process.copyFile(file.getAbsolutePath(),doneFile.getAbsolutePath());
						file.deleteOnExit();
					} catch (Exception e) {
						logger.error("识别图像时发生错误",e);
					}
			 }
		 }
		 return result;
	}
}
