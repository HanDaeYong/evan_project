package kr.co.skmns.kakao.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.skmns.kakao.dao.TraficBatchDao;
import kr.co.skmns.kakao.model.UBISKrentcarBean;
import kr.co.skmns.kakao.utility.AEScrypto;
import kr.co.skmns.kakao.utility.FTPFileControl;

/**
 * Kakao
 * @author Ham Daeyong
 * @since 2017.09.20
 */

@Service
public class KakaoAPIService {
	
	public JSONObject keyboard() {		
		//JSON 객체 생성
        JSONObject jsObj = new JSONObject();
        JSONArray jsArr = new JSONArray();
        
        //버튼에 들어갈 텍스트
        jsArr.add("say hello");
        jsArr.add("say bye");        
        
        //home keyboard 설정
        jsObj.put("type", "buttons");
        jsObj.put("buttons", jsArr);
        return jsObj;
	}
}
