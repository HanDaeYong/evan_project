package kr.co.skmns.kakao.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * SendMailService
 * SMTP 메일전송 서비스

 * @author LimKyungTae
 * @since 2016.12.08
 */
@Service
public class SendMailService {

	@Value("#{system['server.env']}")
	private String serverEnv;
	
	@Value("#{smtp['smtp.skrentcar.host']}")
	private String SMTP_HOST;
	@Value("#{smtp['smtp.skrentcar.port']}")
	private String SMTP_PORT;
	@Value("#{smtp['smtp.skrentcar.id']}")
	private String SMTP_ID;
	@Value("#{smtp['smtp.skrentcar.pw']}")
	private String SMTP_PW;
	@Value("#{smtp['smtp.skrentcar.from']}")
	private String SMTP_FROM;
	
	/**
	 * SMTP 메일전송

	 * @author LimKyungTae
	 * @since 2016.12.08
	 * @param string 수신자
	 * @param string 참조자
	 * @param string 제목
	 * @param string 내용
	 */
	public void sendMailProc(String recipientTO, String recipientCC, String subject, String body){
		
		System.out.println("######## SERVER ::: " + serverEnv);
        
		if("local".equals(serverEnv)){
			
			try {
	        	// 메일 관련 정보
	            String host = SMTP_HOST;
	            int port = Integer.parseInt(SMTP_PORT);
	            final String username = SMTP_ID;
	            final String password = SMTP_PW;
	    		
	            // 메일 내용
	            Properties props = System.getProperties();
	    		
	            props.put("mail.smtp.host", host);
	            props.put("mail.smtp.port", port);
	            props.put("mail.smtp.auth", "true");
	            props.put("mail.smtp.ssl.enable", "true");
	            props.put("mail.smtp.ssl.trust", host);
	            
	            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	                String un=username;
	                String pw=password;
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(un, pw);
	                }
	            });
	            session.setDebug(true); //for debug
	              
	            Message mimeMessage = new MimeMessage(session);
	        	
				mimeMessage.setFrom(new InternetAddress(SMTP_FROM));
				
				mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientTO));
				mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(recipientCC));
				
				
				mimeMessage.setSubject(subject);
		        mimeMessage.setText(body);
		        Transport.send(mimeMessage);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			try {
	        	// 메일 관련 정보
	            String host = SMTP_HOST;
	            int port = Integer.parseInt(SMTP_PORT);
	    		
	            // 메일 내용
	            Properties props = System.getProperties();
	    		
	            props.put("mail.smtp.host", host);
	            props.put("mail.smtp.port", port);
	            props.put("mail.smtp.ssl.enable", "false");
//	            props.put("mail.smtp.ssl.trust", host);
	            
	            Session session = Session.getDefaultInstance(props);
	            session.setDebug(true); //for debug
	              
	            Message mimeMessage = new MimeMessage(session);
	        	
				mimeMessage.setFrom(new InternetAddress(SMTP_FROM));
				
				mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientTO));
				mimeMessage.addRecipients(Message.RecipientType.CC, InternetAddress.parse(recipientCC));
				
				
				mimeMessage.setSubject(subject);
		        mimeMessage.setText(body);
		        Transport.send(mimeMessage);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
