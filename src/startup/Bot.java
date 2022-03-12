package startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.login.LoginException;

import config.Config;
import listeners.HelpSlash;
import listeners.PrivateCodeListener;
import listeners.SelfroleListener;
import listeners.SelfroleSlash;
import listeners.VcListener;
import listeners.VerifySlash;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import slash.SlashManager;

public class Bot {
	static public JDA jda;
	public boolean shutdown = false;

	private Bot() throws LoginException, InterruptedException {
		
		JDABuilder jdabuilder;
		jdabuilder = JDABuilder.createDefault(Config.get("TOKEN"));
		jdabuilder.setStatus(OnlineStatus.ONLINE);
		jdabuilder.setMemberCachePolicy(MemberCachePolicy.ALL);
		jdabuilder.enableIntents(GatewayIntent.GUILD_MEMBERS);
		jdabuilder.enableIntents(GatewayIntent.GUILD_MESSAGES);
		jdabuilder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);

		SlashManager manager = new SlashManager();
		// add listeners here
		jdabuilder.addEventListeners(new SelfroleListener());
		jdabuilder.addEventListeners(new HelpSlash(manager));
		jdabuilder.addEventListeners(new SelfroleSlash());
		jdabuilder.addEventListeners(new VcListener());
		jdabuilder.addEventListeners(new VerifySlash());
		jdabuilder.addEventListeners(new PrivateCodeListener());

		// build bot
		jda = jdabuilder.build();
		jda.awaitReady();

		// exit bot
		austaste();

		Guild mytestguild = jda.getGuildById("691590564315004959");

		mytestguild.updateCommands().addCommands(new CommandData("help", "Hilft dir bei etwas, was denn sonst?!?!"),
				new CommandData("selfrole", "Selfroles erstellen!").addOptions(
						new OptionData(OptionType.CHANNEL, "channel",
								"Gebe hier ein Channel ein, wo die Nachricht gesendet werden soll!")
										.setChannelTypes(ChannelType.TEXT).setRequired(true),
						new OptionData(OptionType.STRING, "titel", "Setzt dein Selfrole-Titel fest").setRequired(true),
						new OptionData(OptionType.STRING, "beschreibung",
								"Gebe hier eine Beschreibung zu deiner Nachricht an!").setRequired(true)),
				new CommandData("verify", "Verifiziere dich!").addOptions(new OptionData(OptionType.INTEGER, "matrikelnummer", "Was du hier schreiben musst ist hoffentlich offensichtlich!").setRequired(true))).queue();

		System.out.println("Bot ohne Fehler online");

	}

	public static void main(String[] args) throws LoginException, InterruptedException {
		new Bot();
	}

	public void austaste() {

		new Thread(() -> {

			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while ((line = reader.readLine()) != null) {

					if (line.equalsIgnoreCase("exit")) {
						shutdown = true;
						if (jda != null) {
							jda.getPresence().setStatus(OnlineStatus.OFFLINE);
							jda.shutdown();
							System.out.println("Bot offline.");
						}

						reader.close();
						break;
					} else {
						System.out.println("Use 'exit' to shutdown.");
					}
				}
			} catch (IOException e) {
			}

		}).start();
	}

}
