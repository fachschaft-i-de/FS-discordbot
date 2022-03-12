package slash;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

import listeners.HelpSlash;
import listeners.SelfroleSlash;
import listeners.VerifySlash;


public class SlashManager {

	private final List<ISlash> commands = new ArrayList<>();

	public SlashManager() {
		addCommand(new HelpSlash(this));
		addCommand(new SelfroleSlash());
		addCommand(new VerifySlash());
	}

	private void addCommand(ISlash cmd) {
		boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

		if (nameFound) {
			throw new IllegalArgumentException("Command existiert schon");
		}

		commands.add(cmd);
	}

	public List<ISlash> getCommands() {
		return commands;
	}

	@Nullable
	private ISlash getCommand(String search) {
		String searchLower = search.toLowerCase();

		for (ISlash cmd : this.commands) {
			if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}

		return null;
	}

}
