package listeners;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import config.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import slash.ISlash;

public class VerifySlash extends ListenerAdapter implements ISlash{
	
	private static HashMap<String, String> codeList = new HashMap<>();
	private HashMap<String, Integer> cooldownMapMin = new HashMap<>();
	private HashMap<String, Integer> cooldownMapHour = new HashMap<>();
	private HashMap<String, Integer> cooldownMapDay = new HashMap<>();

	@Override
	public void onSlashCommand(SlashCommandEvent event) {
		

		if (event.getName().equals("verify") && event.getChannel().getId().equals("952260026494500955")) {
			
			Member member = event.getMember();
			if (!cooldownMapMin.containsKey(member.getId())) {
				cooldownMapMin.put(member.getId(), event.getTimeCreated().getMinute());
				cooldownMapHour.put(member.getId(), event.getTimeCreated().getHour());
				cooldownMapDay.put(member.getId(), event.getTimeCreated().getDayOfMonth());

				sendMail(event);

			}else {
				int savedTime = cooldownMapMin.get(member.getId());
				if (savedTime - event.getTimeCreated().getMinute() <= -1 || cooldownMapHour.get(member.getId()) != event.getTimeCreated().getHour() || cooldownMapDay.get(member.getId()) != event.getTimeCreated().getDayOfMonth()) {

					sendMail(event);
					cooldownMapMin.put(member.getId(), event.getTimeCreated().getMinute());
					cooldownMapHour.put(member.getId(), event.getTimeCreated().getHour());
					cooldownMapDay.put(member.getId(), event.getTimeCreated().getDayOfMonth());

				} else {
					event.reply("- " + "Dein Cooldown von einer Minute ist noch aktiv, " + member.getEffectiveName() + " -").setEphemeral(true).queue();
				}
				
			}
			
		}
		
		

	}
	
	public void sendMail(SlashCommandEvent event) {
		String mNummer = event.getOption("matrikelnummer").getAsString();

		if (Pattern.matches("[0-9]{7}", mNummer)) {

			event.deferReply(true).queue();

			String to = mNummer + "@stud.hs-mannheim.de";
			String from = Config.get("MAIL");
			String host = "smtp.gmail.com";


			Properties properties = System.getProperties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", "465");
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.auth", "true");

			Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

				protected PasswordAuthentication getPasswordAuthentication() {

					return new PasswordAuthentication(Config.get("MAIL"), Config.get("PASS"));

				}

			});

			session.setDebug(true);

			try {
				

				String randomNumber = generateRandomString(10);
			    
			    if(codeList.containsKey(event.getUser().getId())) {
			    	codeList.replace(event.getUser().getId(), randomNumber);
			    }else {
					codeList.put(event.getUser().getId(), randomNumber);
				}
			    

				MimeMessage message = new MimeMessage(session);
				message.setFrom(new InternetAddress(from));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setSubject("VERIFY-CODE");


				String msg = "Hey, "
						 	+ "\n"
							+ "\n"	
							+ "hier ist dein Code: " + randomNumber
							+ "\n"
							+ "Nachdem du dem Fachschafts-Bot per Direktnachricht dein Code gesendet hast, wirst du automatisch verifiziert" 
							+ "\n"
							+ "Wenn es Schwierigkeiten geben sollte schreibe einen Admin oder jemanden von der Fachschaft an!"
							+ "\n"
							+ "\n"
							+ "Mit freundlichen Grüßen"
							+ "\n"
							+ "Deine Fachschaft";
				message.setText(msg);

				System.out.println("sending...");

				Transport.send(message);
				System.out.println("Sent message successfully....");
				
				
				
			} catch (MessagingException mex) {
				mex.printStackTrace();
			}

			event.getHook().editOriginal(
					"Check deine Hochschul-Mail ab und schick mir per Direktnachricht dein Code, damit ich dich verifizieren kann! Du hast 5 Minuten, also mach schnell!")
					.queue();
			
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					
					codeList.remove(event.getUser().getId());

				}
			},5 * 60 * 1000);

		} else {
			event.reply("Das ist keine richtige Matrikelnummer!").setEphemeral(true).queue();
		}

	}
	
	public static HashMap<String, String> getCodeList(){
		return codeList;
	}

	@Override
	public String getName() {
		return "verify";
	}

	@Override
	public String getHelp() {
		String verifyMessage = 
				"Mein Verify-Command verifiziert dich per Mail!" + "\n" 
				+ "Dabei musst du folgenden Wert setzen: "+ "\n"
				+ "\n"
				+ "\n"
				+ "> `Matrikelnummer: Gebe hier deine eigene Matrikelnummer ein.`"+ "\n";
		
		return verifyMessage;
	}
	
	public static String generateRandomString(int length) {

	    String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
	    String CHAR_UPPER = CHAR_LOWER.toUpperCase();
	    String NUMBER = "0123456789";

	    String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
	    SecureRandom scr = new SecureRandom();

	    if (length < 1) throw new IllegalArgumentException();

	    StringBuilder sb = new StringBuilder(length);
	    
	    for (int i = 0; i < length; i++) {
	        int rndCharAt = scr.nextInt(DATA_FOR_RANDOM_STRING.length());
	        char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);

	        sb.append(rndChar);
	    }

	    return sb.toString();
	}

}
