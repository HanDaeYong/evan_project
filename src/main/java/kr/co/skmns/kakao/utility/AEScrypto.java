package kr.co.skmns.kakao.utility;

import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


//고급 암호화 표준(AES, Advanced Encryption Standard)
//암호화와 복호화 과정에서 동일한 키를 사용하는 대칭 키 알고리즘
public class AEScrypto {
	/**
	 * hexa 코드로 변환할 때 사용
	 */
	private static final byte TO_HEXA = (byte)0xff;
	static String key;
	
	static {
		key =ResourceBundle.getBundle("crypto").getString("tuk.key");
	}

	/**
	 * AES 암호화
	 * 
	 * @param str 암호화할 문자열
	 * @return AES 암호화된 문자열
	 * @throws TOPSException 예외처리
	 */
    public static String aesEncrypt(String str) {
        try {        	
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(),"AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(str.getBytes());

            return byteArrayToHex(encrypted);
        } catch (Exception e) {
            System.out.println(e.toString());
            return str;
        }
    }

	 /**
	  * AES 복호화
	  * 
	  * @param str 복호화할 문자열
	  * @return AES 복호화된 문자열
	  */
    public static String aesDecrypt(String str) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(),"AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] original = cipher.doFinal(hexToByteArray(str));

            return new String(original);
        } catch (Exception e) {
        	System.out.println(e.toString());
            return "TUK_ERROR";
        }
    }
    
	 /**
	  * byte[] to hex : unsigned byte(바이트) 배열을 16진수 문자열로 변환하여 반환
	  * 
	  * @param ba unsigned byte(바이트) 배열
	  * @return 16진수 문자열
	  */
    public static String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }

        StringBuffer sb = new StringBuffer(ba.length * 2);
        String hexNumber;
        for (int x = 0; x < ba.length; x++) {
            hexNumber = "0" + Integer.toHexString(TO_HEXA & ba[x]);
            sb.append(hexNumber.substring(hexNumber.length() - 2));
        }

        return sb.toString();
    }

	 /**
	  * hex to byte[] : 16진수 문자열을 바이트 배열로 변환
	  * 
	  * @param hex 16진수 문자열
	  * @return 바이트 배열
	  */
    public static byte[] hexToByteArray(String hex) {
        if (hex == null || hex.length() == 0) {
            return null;
        }

        byte[] ba = new byte[hex.length() / 2];
        for (int i = 0; i < ba.length; i++) {
            ba[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }

        return ba;
    }


} 