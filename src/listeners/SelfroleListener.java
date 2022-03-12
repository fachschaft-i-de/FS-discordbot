package listeners;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SelfroleListener extends ListenerAdapter {

	@Override
	public void onSelectionMenu(SelectionMenuEvent event) {


		if (event.getSelectionMenu().getId().equals("selfrole")) {

			if (event.getSelectionMenu().getMaxValues() == 1) {

				List<Role> allMemberRoles = new ArrayList<>(event.getMember().getRoles());

				List<Role> optionRoleListforRemove = new ArrayList<>();
				
				Role selectedrole = null;
				if (event.getSelectedOptions().size() != 0) {
					selectedrole = event.getGuild().getRoleById(event.getSelectedOptions().get(0).getValue());
				}
			
				for (int i = 0; i < event.getSelectionMenu().getOptions().size(); i++) {
					if (!(selectedrole == event.getGuild()
							.getRoleById(event.getSelectionMenu().getOptions().get(i).getValue()))) {
						optionRoleListforRemove.add(
								event.getGuild().getRoleById(event.getSelectionMenu().getOptions().get(i).getValue()));
					}
				}

				if (!optionRoleListforRemove.isEmpty()) {
					allMemberRoles.removeAll(optionRoleListforRemove);
				}
				
				if(event.getSelectedOptions().size() != 0) {
					allMemberRoles.add(selectedrole);
				}
				

				event.getGuild().modifyMemberRoles(event.getMember(), allMemberRoles).queue();
				event.deferEdit().queue();

			} else {

				List<Role> memberRoles = new ArrayList<>(event.getMember().getRoles());
				List<Role> menuRoles = new ArrayList<>();
				
				for(int i = 0; i < event.getSelectionMenu().getOptions().size(); i++) {
					menuRoles.add(event.getGuild().getRoleById(event.getSelectionMenu().getOptions().get(i).getValue()));
				}
				
				for(int i = 0; i < menuRoles.size(); i++) {
					if(memberRoles.contains(menuRoles.get(i))) {
						memberRoles.remove(menuRoles.get(i));
					}
				}
				
				List<Role> selectedRoles = new ArrayList<>();
				
				for(int i = 0; i < event.getSelectedOptions().size(); i++) {
					selectedRoles.add(event.getGuild().getRoleById(event.getSelectedOptions().get(i).getValue()));
				}
				
				memberRoles.addAll(selectedRoles);
				event.getGuild().modifyMemberRoles(event.getMember(), memberRoles).queue();
				event.deferEdit().queue();

			}

		}

	}

}
