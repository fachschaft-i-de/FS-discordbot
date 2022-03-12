package listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import slash.ISlash;

public class SelfroleSlash extends ListenerAdapter implements ISlash{
	static HashMap<String, ArrayList<String>> mappedProz = new HashMap<>();
	@Override
	public void onSlashCommand(SlashCommandEvent event) {

		if(event.getName().equals("selfrole") && (event.getMember().hasPermission(Permission.BAN_MEMBERS) || event.getMember().hasPermission(Permission.KICK_MEMBERS))) {
			
			ArrayList<String> infos = new ArrayList<>();
			infos.add(event.getOption("titel").getAsString());//0
			infos.add(event.getOption("beschreibung").getAsString());//1
			infos.add(event.getOption("channel").getAsString());//2
			
			mappedProz.put(event.getId(), infos);
		
			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("__**Selfrole Einstellungen**__");
			builder.setDescription("**Titel:** " + "\n" 
									+ "`" + event.getOption("titel").getAsString() + "`" + "\n" + "\n"
									+ "**Beschreibung:** " + "\n"
									+ "`" + event.getOption("beschreibung").getAsString() + "`" + "\n"
									+ "\n"
									+ "\n"
									+ "__**Sollen mehrere Rollen auswählbar sein? Oder nur höchstens eine?**__" + "\n"
									+ "**Beispiel:** " + "\n"
									+ "> `" + "Bei der Kategorie _Alter_ eine Rolle`" + "\n"
									+ "> `" + "Bei der Kategorie _Spiele_ mehrere Rollen`");
			builder.setColor(0xFF8C00);
			
			event.replyEmbeds(builder.build()).setEphemeral(true).addActionRow(Button.primary(event.getId()+ "mehrere", "Mehrere Rollen"),
															Button.primary(event.getId() + "eine", "Eine Rolle")).queue();
			
			
		}else if(event.getName().equals("selfrole")){
			event.reply("Du bist nicht berechtigt!").setEphemeral(true).queue();
		}
	
	}
	
	@Override
	public void onButtonClick(ButtonClickEvent event) {
	
		if(event.getComponentId().contains("mehrere")) {
			String hashid = event.getComponentId().replace("mehrere", "");
			mappedProz.get(hashid).add("mehrere");//3

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("**__Selfrole Einstellungen__**");
			builder.setDescription("Erwähne nun bitte alle Rollen im Chat, die in dieser Selfrole Nachricht zur Verfügung gestellt werden sollen." + "\n"
									+ "Im Moment kannst du bis zu 25 Rollen hinzufügen." + "\n"
									+ "\n"
									+ "\n"
									+ "**Beispiel:** " + "\n"
									+ "> " + "Sagen wir du möchtest folgende Rollen verwenden: Legende, Meister und Idiot." + "\n"
									+ "> " + "Dann schreibst du im Chat:" + "\n"
									+ "> `" + "@Legende @Meister @Idiot" + "`");
			builder.setFooter("Du hast 5 Minuten Zeit");
			builder.setColor(0xFF8C00);
			
			//WIR GEHEN NUN IN DIE KLASSE RoleMentionListeners
			event.getJDA().addEventListener(new RoleMentionandSelfroleCreateListener(event.getChannel(), event.getUser(), hashid, event.getHook(), event.getMessageId(), event));
			
			event.editMessageEmbeds(builder.build()).queue();
			event.getHook().editMessageComponentsById(event.getMessageId(), Collections.emptyList()).queue();
			
			
		}else if (event.getComponentId().contains("eine")) {
			
			String hashid = event.getComponentId().replace("eine", "");
			mappedProz.get(hashid).add("eine");//3

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle("**__Selfrole Einstellungen__**");
			builder.setDescription("Erwähne nun bitte alle Rollen im Chat, die in dieser Selfrole Nachricht zur Verfügung gestellt werden sollen." + "\n"
									+ "Im Moment kannst du bis zu 25 Rollen hinzufügen." + "\n"
									+ "\n"
									+ "\n"
									+ "**Beispiel:** " + "\n"
									+ "> " + "Sagen wir du möchtest folgende Rollen verwenden: Legende, Meister und Idiot." + "\n"
									+ "> " + "Dann schreibst du im Chat:" + "\n"
									+ "> `" + "@Legende @Meister @Idiot" + "`");
			builder.setFooter("Du hast 5 Minuten Zeit");
			builder.setColor(0xFF8C00);
			
			//WIR GEHEN NUN IN DIE KLASSE RoleMentionListener
			event.getJDA().addEventListener(new RoleMentionandSelfroleCreateListener(event.getChannel(), event.getUser(), hashid, event.getHook(), event.getMessageId(), event));
			
			event.editMessageEmbeds(builder.build()).queue();
			event.getHook().editMessageComponentsById(event.getMessageId(), Collections.emptyList()).queue();

			
		}

		
	}
	

	@Override
	public String getName() {
		return "selfrole";
	}

	@Override
	public String getHelp() {
		
		String selfrolemessage = 
				"Mein Selfrole-Command erstellt ein Selfrole-Menü für dich!" + "\n" 
				+ "Dabei musst du folgende Werte setzen wenn du mein Command ausführst:"+ "\n"
				+ "\n"
				+ "\n"
				+ "> `Channel: Hier gibst du an, wo dein Menü auftauchen soll.`"+ "\n"
				+ "> `Titel: Dein/e Titel/Kategorie soll hier rein.`"+ "\n"
				+ "> `Beschreibung: Beschreibe hier deine Kategorie.`"+ "\n";
		
		return selfrolemessage;
	}
	
	static public HashMap<String, ArrayList<String>> getProzIDs(){
		return mappedProz;
	}

}
