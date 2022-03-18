package listeners;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PrivateCodeListener extends ListenerAdapter {

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

		if (!VerifySlash.getCodeList().containsKey(event.getAuthor().getId()))
			return;

		if (event.getMessage().getContentRaw().equals(VerifySlash.getCodeList().get(event.getAuthor().getId()))) {

			Guild faGuild = event.getJDA().getGuildById("691590564315004959");
			Member member = faGuild.getMemberById(event.getAuthor().getId());

			List<Role> stRollen = new ArrayList<>();
			stRollen.add(faGuild.getRoleById("756494816749027411"));
			stRollen.add(faGuild.getRoleById("756494701389152337"));
			stRollen.add(faGuild.getRoleById("756494665691299978"));
			stRollen.add(faGuild.getRoleById("756494610720751726"));
			stRollen.add(faGuild.getRoleById("763449922351136798"));

			List<Role> mRoles = member.getRoles();

			List<Role> newRoleList = new ArrayList<>();

			newRoleList.add(faGuild.getRoleById("756495178180853811"));

			newRoleList.add(faGuild.getRoleById("951825918127661066"));

			newRoleList.addAll(mRoles);

			if (mRoles.size() != 0) {
				newRoleList.removeAll(stRollen);
			}

			faGuild.modifyMemberRoles(member, newRoleList).queue();

			event.getChannel().sendMessage("Du wurdest verifiziert!").queue();
			VerifySlash.getCodeList().remove(event.getAuthor().getId());
			
		} else if (!event.getMessage().getContentRaw().equals(VerifySlash.getCodeList().get(event.getAuthor().getId()))) {

			event.getChannel().sendMessage("Verifizierung fehlgeschlagen!").queue();
			VerifySlash.getCodeList().remove(event.getAuthor().getId());
			
		}

	}

}
