package kr.co.skmns.trafic.scheduler.cron;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.co.skmns.trafic.scheduler.service.TraficBatchService;


/**
 * LBS Trafic Info Batch
 * LBS 경찰청 교통정보 수집 배치 스케줄러

 * @author Han Daeyong
 * @since 2017.02.09
 */
@Component
public class TraficBatch {

	@Inject TraficBatchService traficBatchService;
	
	private static final Logger logger = LoggerFactory.getLogger("batch");
	
//	/**
//	 * TEST - 10초마다 호출이 되는 스케쥴러 
//	 */
//	@Scheduled(cron = "0/10 * * * * *")
//	public void cronTest1(){
//		System.out.println("10초 마다~~~~~~~~~~~~~");
//	}
//	
//	/**
//	 * TEST - 매 초마다 호출이 되는 스케쥴러 
//	 * 
//	 */
//	@Scheduled(cron = "* * * * * *")
//	public void cronTest2(){
//		System.out.println("1초 마다~~~~~~~~~~~~~");
	
	
	
//	}
	
	/**
	 * 운전습관 SK렌트카 배치 - 오전 9시 마다 실행
	 * 
	 * @author LimKyungTae
	 * @since 2016.12.08 
	 */
//	@Scheduled(cron = "0/10 * * * * *")
/*	@Scheduled(cron = "0 0 9 * * *")
	public void skrentcarSendMailBatch(){
		
		logger.info("");
		logger.info("=================================================");
		logger.info("======= SK렌터카 신청자 메일 발송 배치 START ======");
		logger.info("=================================================");
		
		traficBatchService.sendSKrentcarList();

		logger.info("=================================================");
		logger.info("======= SK렌터카 신청자 메일 발송 배치 END ========");
		logger.info("=================================================\n");
	}*/
	
	/**
	 * 경찰청 교통 정보 수집 배치 - 매 5분마다 실행
	 * 
	 * @author Han Daeyong
	 * @since 2017.02.09 
	 */
	@Scheduled(cron = "0 0/5 * * * *")
	public void traficInfoBatch(){
		logger.info("traficInfoBatch start");
		traficBatchService.getTraficInfo();
		logger.info("traficInfoBatch end");
	}
	
	/**
	 * 경찰청 교통 정보 파일관리 배치 - 매일 새볔 1시에 실행
	 * 
	 * @author Han Daeyong
	 * @since 2017.02.13
	 */
	@Scheduled(cron = "0 0 1 * * *")
	public void traficInfoFileMgmtBatch(){
		logger.info("traficInfoFileMgmtBatch start");
		traficBatchService.deleteTraficInfoFiles();
		logger.info("traficInfoFileMgmtBatch end");
	}
	
}