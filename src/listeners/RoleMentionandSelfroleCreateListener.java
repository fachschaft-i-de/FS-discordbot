package listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;

public class RoleMentionandSelfroleCreateListener extends ListenerAdapter {

	private final long channelId, authorId; // id because keeping the entity would risk cache to become outdated
	private String prozID;
	private InteractionHook hookMessage;
	private String hookMessageID;
	boolean messageReceived = false;

	public RoleMentionandSelfroleCreateListener(MessageChannel channel, User author, String prozID,
			InteractionHook hookMessage, String hookMessageID, ButtonClickEvent event) {
		this.channelId = channel.getIdLong();
		this.authorId = author.getIdLong();
		this.prozID = prozID;
		this.hookMessage = hookMessage;
		this.hookMessageID = hookMessageID;

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {

				if (messageReceived) {

				} else {

					event.getJDA().removeEventListener(RoleMentionandSelfroleCreateListener.this);
					EmbedBuilder builder = new EmbedBuilder();
					builder.setDescription("Zeit abgelaufen...");
					builder.setColor(0xFF8C00);
					hookMessage.editMessageEmbedsById(hookMessageID, builder.build()).queue();

				}

			}
		},5 * 60 * 1000);

	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		if (event.getAuthor().isBot())
			return; // don't respond to other bots
		if (event.getChannel().getIdLong() != channelId)
			return; // ignore other channels
		if (event.getAuthor().getIdLong() != authorId)
			return;

		if (event.getMessage().getMentionedRoles().size() != 0) {

			messageReceived = true;

			List<Role> roles = new ArrayList<>();
			roles = event.getMessage().getMentionedRoles();

			for (int i = 0; i < roles.size(); i++) {

				SelfroleSlash.getProzIDs().get(prozID).add(roles.get(i).getId());

			}

			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("Selfrole wurde erstellt!");
			builder.setColor(0xFF8C00);

			hookMessage.editMessageEmbedsById(hookMessageID, builder.build()).queue();
			event.getMessage().delete().queueAfter(3, TimeUnit.SECONDS);

			// NICHT VERGESSEN LISTENER SCHLIEßEN
			event.getJDA().removeEventListener(this);

			selfroleCreater(event);

		}

	}

	public void selfroleCreater(GuildMessageReceivedEvent event) {

		List<SelectOption> rolelist = new ArrayList<>();

		if (SelfroleSlash.getProzIDs().get(prozID).get(3).equals("mehrere")) {

			for (int i = 4; i < SelfroleSlash.getProzIDs().get(prozID).size(); i++) {

				rolelist.add(SelectOption.of(
						event.getGuild().getRoleById(SelfroleSlash.getProzIDs().get(prozID).get(i)).getName(),
						SelfroleSlash.getProzIDs().get(prozID).get(i)));

			}

			SelectionMenu menu = SelectionMenu.create("selfrole").setPlaceholder("Wähle aus...")
					.setRequiredRange(0, rolelist.size()).addOptions(rolelist).build();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle(SelfroleSlash.getProzIDs().get(prozID).get(0));
			builder.setDescription(SelfroleSlash.getProzIDs().get(prozID).get(1));
			builder.setColor(0xffffff);

			event.getGuild().getTextChannelById(SelfroleSlash.getProzIDs().get(prozID).get(2))
					.sendMessageEmbeds(builder.build()).setActionRow(menu).queue();

		} else {

			for (int i = 4; i < SelfroleSlash.getProzIDs().get(prozID).size(); i++) {

				rolelist.add(SelectOption.of(
						event.getGuild().getRoleById(SelfroleSlash.getProzIDs().get(prozID).get(i)).getName(),
						SelfroleSlash.getProzIDs().get(prozID).get(i)));

			}

			SelectionMenu menu = SelectionMenu.create("selfrole").setPlaceholder("Wähle aus...").setRequiredRange(0, 1)
					.addOptions(rolelist).build();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setTitle(SelfroleSlash.getProzIDs().get(prozID).get(0));
			builder.setDescription(SelfroleSlash.getProzIDs().get(prozID).get(1));
			builder.setColor(0xffffff);

			event.getGuild().getTextChannelById(SelfroleSlash.getProzIDs().get(prozID).get(2))
					.sendMessageEmbeds(builder.build()).setActionRow(menu).queue();

		}

	}

}
