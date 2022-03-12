package listeners;

import java.util.EnumSet;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VcListener extends ListenerAdapter {

	private final String ERSTELLE_LERNRAUM = "765599701533327396";
	List<String> notAffectedVc = List.of("765599701533327396", "755373837436321824", "755374121646555136", "824405905214144523");
	
	
	@Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
        Member member = event.getMember();
        if (member.getUser().isBot())
            return;

        VoiceChannel channelJoined = event.getChannelJoined();
        if (channelJoined.getId().equals(ERSTELLE_LERNRAUM)) {
            Category parent = channelJoined.getParent();
            assert parent != null;
            VoiceChannel complete = parent.createVoiceChannel("Lernraum von " + member.getEffectiveName())
                    .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.MANAGE_CHANNEL), null)
                    .complete();
            Guild guild = event.getGuild();
            guild.moveVoiceMember(member, complete).queue();

            VoiceChannel channelLeft = event.getChannelLeft();
            if (notAffectedVc.contains(channelLeft.getId()))
                return;

            if (channelLeft.getMembers().size() == 0) {
                channelLeft.delete().queue();
            }
        } else {
            VoiceChannel channelLeft = event.getChannelLeft();
            if (notAffectedVc.contains(channelLeft.getId()))
                return;

            if (channelLeft.getMembers().size() == 0) {
                channelLeft.delete().queue();
            }
        }
    }

    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        Member member = event.getMember();
        if (member.getUser().isBot()) {
            return;
        }

        Guild guild = event.getGuild();
        VoiceChannel channelJoined = event.getChannelJoined();

        if (channelJoined.getId().equals(ERSTELLE_LERNRAUM)) {
            Category parent = channelJoined.getParent();
            assert parent != null;
            VoiceChannel complete = parent.createVoiceChannel("Lernraum von " + member.getEffectiveName())
                    .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.MANAGE_CHANNEL), null)
                    .complete();
            guild.moveVoiceMember(member, complete).queue();
        }
    }

    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        Member member = event.getMember();
        if (member.getUser().isBot()) {
            return;
        }


        VoiceChannel channelLeft = event.getChannelLeft();
        if (notAffectedVc.contains(channelLeft.getId()))
            return;

        if (channelLeft.getMembers().size() == 0) {
            channelLeft.delete().queue();
        }
    }

}
