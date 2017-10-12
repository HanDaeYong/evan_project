package kr.co.skmns.kakao.controller;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
	 * @return String
	 */
	@RequestMapping(value = "/keyboard", method = RequestMethod.GET)
	public @ResponseBody String keyboard() {
		//JSON 객체 생성
        JSONObject jsObj = new JSONObject();        
        
		logger.info("################ KakaoAPIController keyboard start ################");
		System.out.println("KakaoAPIController keyboard start");
        jsObj = kakaoAPIService.keyboard();
        logger.info(jsObj.toJSONString());
		logger.info("################ KakaoAPIController keyboard end ################\n\n");		
		return jsObj.toJSONString();
	}
	
	/**
	 * kakao message
	 * 
	 * @author Han Daeyong [17.10.12]
	 * @return String
	 */
	@RequestMapping(value = "/message", method = RequestMethod.POST, headers = "Accept=application/json;charset=utf8")	
	public @ResponseBody String message(@RequestBody JSONObject resObj) {
		//JSON 객체 생성
        JSONObject jsObj = new JSONObject();        
        
		logger.info("################ KakaoAPIController message start ################");
		logger.info(resObj.toJSONString());
		
		jsObj = kakaoAPIService.message(resObj);
		

        logger.info(jsObj.toJSONString());        
		logger.info("################ KakaoAPIController message end ################\n\n");
		
		return jsObj.toJSONString();
	}
	
}