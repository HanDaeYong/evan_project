package kr.co.skmns.kakao.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TestClass {
	
	static Scanner sc = new Scanner(System.in);
	
	public static void makeTuk(){

		System.out.println("전화번호 입력(-제외) : ");
		
		String mdn = sc.nextLine();
		String tuk = AEScrypto.aesEncrypt(mdn);
	
		System.out.println("result tuk ::: "+tuk + " : "+mdn);
		System.out.println("");
	}
	
	public static void deTuk(){
		
		System.out.println("tuk 입력 : ");
		
		String enTuk = sc.nextLine();
		String tuk = AEScrypto.aesDecrypt(enTuk);
		
		System.out.println("result ::: "+tuk);
		System.out.println("");
	}
	

	public static void main(String[] args) {
		
		boolean exitflag = true;
		System.out.println("\n**********************************************************");
		System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()));
		
		while(exitflag){
			System.out.println("[10 ::: TUK 생성]");
			System.out.println("[11 ::: TUK 복호화]");
			
			System.out.println("[99 ::: 나가기]");
			System.out.println("*입력 = ");
			
			String select = sc.nextLine();
			int selectInt = Integer.parseInt(select);
			
			switch (selectInt) {
			
				case 10: makeTuk(); break;
				case 11: deTuk(); break;
					
				case 99: exitflag = false; break;
				default: break;
			}
		}
		System.out.println("**********************************************************\n");
	}

}
