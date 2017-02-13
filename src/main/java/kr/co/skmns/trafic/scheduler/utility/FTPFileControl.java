package kr.co.skmns.trafic.scheduler.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FTPFileControl {	
	
	private static final Logger logger = LoggerFactory.getLogger("trafic");
	
	FTPClient ftpClient;
	
	public FTPFileControl() {
		ftpClient = new FTPClient();
	}
	// 서버로 연결
	public void connect(String ftpUrl, int ftpPort) {
		
		try {			
			logger.info(ftpUrl);
			ftpClient.connect(ftpUrl, ftpPort);
			int reply;
			// 연결 시도후, 성공했는지 응답 코드 확인
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();				
				logger.info("FTP Server connection refuse");				
			} else {				
				logger.info("FTP Server connection success");				
			}	
		} catch (IOException e) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException f) {
				
				}
			}			
			logger.error("[ERROR] FTP Server connection fail : [{}]\n",e.getMessage());
		}
	}
	// 계정과 패스워드로 로그인
	public boolean login(String ftpUrl, int ftpPort, String ftpId, String ftpPw) {
		try {			
			this.connect(ftpUrl, ftpPort);			
			logger.info("FTP Server login");			
			return ftpClient.login(ftpId, ftpPw);
		} catch (IOException e) {			
			logger.error("[ERROR] FTP Server login fail : [{}]\n",e.getMessage());			
			
		}
		return false;
	}
	
	// 서버로부터 로그아웃
	public boolean logout() {
	
		try {			
			logger.info("FTP Server logout");			
			return ftpClient.logout();
		} catch (IOException e) {			
			logger.error("[ERROR] FTP Server logout fail : [{}]\n",e.getMessage());			
		}
		return false;
	}
	
	// FTP의 ls 명령, 모든 파일 리스트를 가져온다
	public FTPFile[] list() {
	
		FTPFile[] files = null;
		try {
			files = this.ftpClient.listFiles();			
			logger.info("Load file list");			
			return files;

		} catch (IOException e) {
			logger.error("[ERROR] Load file list fail : [{}]\n",e.getMessage());
			
		}
		return null;
	}
	
	// 파일을 전송 받는다
	public boolean get(String sourcePath, String targetPath, String targetName) {
	
		boolean flag = false;
	
		OutputStream output = null;
		try {
			// 받는 파일 생성 path 위치에 name 이름으로 파일 생성된다
			File local = new File(targetPath, targetName);
			output = new FileOutputStream(local);			
			logger.info("Check directory for download");			
		} catch (FileNotFoundException e) {
			logger.error("[ERROR] No directory for download : [{}]\n",e.getMessage());
			return flag;
		}
	
		try {
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (ftpClient.retrieveFile(sourcePath, output)) {
				flag = true;				
				logger.info("Completed file download");				
			}
			output.close();			
		} catch (IOException e) {			
			logger.error("[ERROR] Fail file download : [{}]\n",e.getMessage());			
		}
		return flag;
	}
	
	// 파일을 전송 받는다 위의 method 와 return 값이 달라서 하나 더 만들었다
	public File getFile(String sourcePath, String targetPath, String targetName) {
		
		OutputStream output = null;
		File local = null;
		try {
			// 받는 파일 생성
			local = new File(targetPath, targetName);
			output = new FileOutputStream(local);
		} catch (FileNotFoundException e) {			
			logger.error("[ERROR] No directory for download : [{}]\n",e.getMessage());
		}
		
		File file = new File(sourcePath);
		try {
			if (ftpClient.retrieveFile(sourcePath, output)) {
				//
			}
		} catch (IOException e) {
			logger.error("[ERROR] Fail file download : [{}]\n",e.getMessage());			
		}
		return local;
	}
	
	// 파일을 전송 한다
	public boolean put(String fileName, String targetSource, String targetName) {
	
		boolean flag = false;
		InputStream input = null;
		File local = null;
		
		try {
			local = new File(targetSource, fileName);
			input = new FileInputStream(local);
		} catch (FileNotFoundException e) {
			return flag;
		}
		
		try {
		
			// targetName 으로 파일이 올라간다
			if (ftpClient.storeFile(targetName, input)) {
				flag = true;
			}
		} catch (IOException e) {
			logger.error("[ERROR] Could not send file : [{}]\n",e.getMessage());			
			return flag;
		}
		return flag;
		
	}
	
	// 서버 디렉토리 이동
	protected void cd(String path) {
	
		try {
			ftpClient.changeWorkingDirectory(path);
		} catch (IOException e) {
			logger.error("[ERROR] Could not move directory : [{}]\n",e.getMessage());			
		}
	
	}
	
	// 서버로부터 연결을 닫는다
	public void disconnect() {
	
		try {
			ftpClient.disconnect();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public void setFileType(int iFileType) {
		try {
			ftpClient.setFileType(iFileType);
		} catch (Exception e) {
			logger.error("[ERROR] Could not set file type : [{}]\n",e.getMessage());			
		}
	}
	
	public boolean delete(String targetSource, String targetName) {
		
		boolean flag = false;
		
		try {
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(targetSource);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			boolean isSuccess = ftpClient.deleteFile(targetName);
			
			if(isSuccess) {
				flag = true;
			}else {
				logger.info("Could not delete file.");
			}
		}catch(IOException e) {
			logger.error("[ERROR] Could not delete file. : [{}]\n",e.getMessage());			
		}
			
		
		return flag;
		
	}
}
