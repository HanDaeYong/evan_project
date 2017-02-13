package kr.co.skmns.trafic.scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/batch")
public class TraficBatchController {

	private static final Logger logger = LoggerFactory.getLogger(TraficBatchController.class);

	@Value("#{system['server.env']}")
	private String serverEnv;
	
	/**
	 * test
	 * 
	 * @author Han Daeyong [17.02.09]
	 * @return String
	 */
	@RequestMapping("/test.do")
	public String test() {
		logger.info("################ CustomerServiceController test start ################");

		logger.info("################ CustomerServiceController test end ################\n\n");
		return "test";
	}
	
}