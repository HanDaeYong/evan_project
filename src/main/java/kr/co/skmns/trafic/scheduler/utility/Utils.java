package kr.co.skmns.trafic.scheduler.utility;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	
	/**
	 * tuk 복호화
	 * 
	 * @author LimKyungTae - 2016.06.16
	 * @return Map<String, String>
	 * 
	 * */
	public static Map<String, String> decryptTuk(String tuk){
		
		Map<String, String> map = new HashMap<String, String>();
		
		String[] splitStr;
		
		logger.info("Tuk Param ::: {}", tuk);
		
		try {
			String decryptStr = AEScrypto.aesDecrypt(tuk);
			
			if("TUK_ERROR".equals(decryptStr)){
				map.put("mdn", "TUK_ERROR");
			}else{
				//mdn, timestamp split
				splitStr = decryptStr.split(",");
				
				map.put("mdn", splitStr[0]);
				map.put("timestamp", splitStr[1]);
			}
		} catch (Exception e) {
			logger.info(e.toString());
			map.put("mdn", "TUK_ERROR");
		}
		
		return map;
	}
	
}
