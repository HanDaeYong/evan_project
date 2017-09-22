package kr.co.skmns.kakao.controller;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.co.skmns.kakao.service.KakaoAPIService;

@Controller
@RequestMapping("/kakao")
public class KaKaoAPIController {
	@Inject KakaoAPIService kakaoAPIService;
	
	private static final Logger logger = LoggerFactory.getLogger(KaKaoAPIController.class);

	@Value("#{system['server.env']}")
	private String serverEnv;
	
	/**
	 * kakao keyboard
	 * 
	 * @author Han Daeyong [17.09.20]
	 * @return Json
	 */
	@RequestMapping("/keyboard")
	public JSONObject keyboard() {
		//JSON 객체 생성
        JSONObject jsObj = new JSONObject();        
        
		logger.info("################ KakaoAPIController start ################");
        jsObj = kakaoAPIService.keyboard();
        System.out.println(jsObj);
		logger.info("################ KakaoAPIController end ################\n\n");
		return jsObj;
	}
	
}