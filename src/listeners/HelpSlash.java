package listeners;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import slash.ISlash;
import slash.SlashManager;

public class HelpSlash extends ListenerAdapter implements ISlash {

	private SlashManager manager;

	public HelpSlash() {
	}

	public HelpSlash(SlashManager manager) {
		this.manager = manager;
	}

	@Override
	public void onSlashCommand(SlashCommandEvent event) {

		if (event.getName().equals("help")) {

			List<SelectOption> commandslist = new ArrayList<>();

			for (int i = 0; i < manager.getCommands().size(); i++) {
				if (!manager.getCommands().get(i).getName().contains("m_")) {
					commandslist.add(SelectOption.of(manager.getCommands().get(i).getName(),
							manager.getCommands().get(i).getName()));
				}

			}

			SelectionMenu menu = SelectionMenu.create("helpMenu").setPlaceholder("Wähle dein Command aus " + "")
					.setRequiredRange(1, 1).addOptions(commandslist).build();

			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("Hier findest du alle Commands");
			builder.setColor(0x2c2f33);

			event.replyEmbeds(builder.build()).addActionRow(menu).setEphemeral(true).queue();
		}

	}

	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {

		if (event.getSelectionMenu().getId().equals("helpMenu")) {

			String manuCommandName = event.getSelectedOptions().get(0).getValue();

			for (int i = 0; i < manager.getCommands().size(); i++) {
				if (manager.getCommands().get(i).getName().equals(manuCommandName)) {
					EmbedBuilder builder = new EmbedBuilder();
					builder.setDescription(manager.getCommands().get(i).getHelp());
					builder.setColor(0x2c2f33);
					event.replyEmbeds(builder.build()).setEphemeral(true).queue();
				}
			}

		}

	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getHelp() {
		return "Ich helfe dir offensichtlich?!?!";
	}

}
