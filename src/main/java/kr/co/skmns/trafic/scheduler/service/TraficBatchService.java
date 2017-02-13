package kr.co.skmns.trafic.scheduler.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.skmns.trafic.scheduler.dao.TraficBatchDao;
import kr.co.skmns.trafic.scheduler.model.UBISKrentcarBean;
import kr.co.skmns.trafic.scheduler.utility.AEScrypto;
import kr.co.skmns.trafic.scheduler.utility.FTPFileControl;

/**
 * TmapBatch
 * T map 모바일웹용 배치 스케줄러

 * @author LimKyungTae
 * @since 2016.12.08
 */
@Service
public class TraficBatchService {
	
	@Inject TraficBatchDao tmapBatchDao;
	@Inject SendMailService sendMailService;
	
	private static final Logger logger = LoggerFactory.getLogger("batch");
	
	@Value("#{smtp['smtp.skrentcar.to']}")
	private String SMTP_TO;
	@Value("#{smtp['smtp.skrentcar.cc']}")
	private String SMTP_CC;
	
	/**
	 * 운전습관 SK렌트카 배치 서비스 로직
	 * 
	 * @author LimKyungTae
	 * @since 2016.12.08 
	 */
	public void sendSKrentcarList(){
		
		logger.info("---------------------------");
		logger.info("-     DB SELECT START     -");
		logger.info("---------------------------");
		
		List<UBISKrentcarBean> list = tmapBatchDao.selectRegYesterdayList();

		logger.info("-> 전날 신청 건수 :::: "+list.size()+ " 건");

		logger.info("---------------------------");
		logger.info("-      DB SELECT END      -");
		logger.info("---------------------------");
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy년 MM월 dd일");
		Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);

		String subject = "[T map 운전습관] SK렌터카 신청리스트 "+ date.format(cal.getTime());
		String bodyTitle = "";
		String body = "";
		
		if(list.size() <= 0){
			//전날 신청 건 수가 없을 때
			body = body + date.format(cal.getTime())+"에는 신청자가 없습니다.";	
			
		}else{
			//전날 신청 건 수가 1건 이상일 때
			
			bodyTitle = date.format(cal.getTime())+" 총 신청자 수는 " + list.size() +"명 입니다.\n\n";
			
			for(UBISKrentcarBean bean: list){
				
				body = body + bean.getMdn() + "," + bean.getName() + ","+ bean.getReg_date() + "|";
			}
			//암호화
			body = AEScrypto.aesEncrypt(body);
		}
		
		logger.info("---------------------------");
		logger.info("-     SEND MAIL START     -");
		logger.info("---------------------------");
		
		logger.info("-> 수신자 ::: {}",SMTP_TO);
		logger.info("-> 참조자 ::: {}",SMTP_CC);
		logger.info("-> 제목 ::: {}",subject);
		logger.info("-> 내용 ::: {}",bodyTitle+body);
		
//		SendMailService.sendMailProc(SMTP_TO, SMTP_CC, subject, bodyTitle+body);
		sendMailService.sendMailProc(SMTP_TO, SMTP_CC, subject, bodyTitle+body);
		
		logger.info("---------------------------");
		logger.info("-      SEND MAIL END      -");
		logger.info("---------------------------");
		
	}
	
	/**
	 * LBS 교통정보 수집 배치 서비스 로직
	 * 
	 * @author Han Daeyong
	 * @since 2017.02.09 
	 */
	@Value("#{system['ftp.server.url']}")
	private String FTP_URL;
	@Value("#{system['ftp.server.port']}")
	private int FTP_PORT;
	@Value("#{system['ftp.server.id']}")
	private String FTP_ID;
	@Value("#{system['ftp.server.password']}")
	private String FTP_PW;
	
	@Value("#{system['ftp.trafic.file.down.path']}")
	private String FTP_DOWN_PATH;
	@Value("#{system['ftp.trafic.file.up.path']}")
	private String FTP_UP_PATH;

	@Value("#{system['ftp.trafic.file.source.path']}")
	private String FTP_SOURCE_PATH;
	@Value("#{system['ftp.trafic.file.name.traf']}")
	private String FTP_NAME_TRAF;
	@Value("#{system['ftp.trafic.file.name.inci']}")
	private String FTP_NAME_INCI;
	
	public void getTraficInfo(){
		//System.out.println( tmapBatchDao.testDbCon());
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmm");
		Calendar cal = Calendar.getInstance();	    

		String trafDateNm = FTP_NAME_TRAF + "_" + date.format(cal.getTime());
		String inciDateNm = FTP_NAME_INCI +  "_" + date.format(cal.getTime());
		
		try {    		
    		FTPFileControl ftp = new FTPFileControl();
    		//FTP연결
    		ftp.connect(FTP_URL, FTP_PORT);;
    		//FTP로그인
    		ftp.login(FTP_URL, FTP_PORT, FTP_ID, FTP_PW);
    		//FTP 리스트 load
    		ftp.list();
    		//FTP 다운로드
    		ftp.get(FTP_SOURCE_PATH + FTP_NAME_TRAF, FTP_DOWN_PATH, trafDateNm);    		
    		ftp.get(FTP_SOURCE_PATH + FTP_NAME_INCI, FTP_DOWN_PATH, inciDateNm);
    		
    		//다운로드 후 원격지 FTP 파일 삭제
    		ftp.delete(FTP_SOURCE_PATH, FTP_NAME_TRAF);
    		ftp.delete(FTP_SOURCE_PATH, FTP_NAME_INCI);
    		//FTP 로그아웃
    		ftp.logout();
    		
    	} catch (Exception e) {
    		logger.error("[ERROR] FTPTraficInfoDown!!! : [{}]\n", e.getMessage());
    		return;
    	}
		
	}

	public void deleteTraficInfoFiles() {
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmm");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		
		File local = new File(FTP_DOWN_PATH);					
		try {
			if (local.exists()){
				File[] files = local.listFiles();
				Date fileDate = new Date();
				for (File file : files) {
					if(file.getName().indexOf(FTP_NAME_TRAF) > -1 || file.getName().indexOf(FTP_NAME_INCI) > -1){
						//문자를 데이터형으로 변경
						fileDate = date.parse(file.getName().split("_")[2]);
						//파일이 현재 날짜보다 한달전 파일이면 삭제
						if(fileDate.compareTo(cal.getTime()) > 0){
							file.delete();
				        }						
					}																										 
				}			
			} else {
				logger.info("Files are not exist.");
			} 
		} catch (ParseException e) {
			logger.error("[ERROR] deleteTraficInfoFiles!!! : [{}]\n", e.getMessage());
		}
	}
}
