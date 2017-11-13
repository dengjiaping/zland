package com.beem.project.beem.service.pcklistener;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * filter friend invite
 * 
 * @author arthur
 *
 */
public class FriendInviteFilter implements PacketFilter {

	@Override
	public boolean accept(Packet packet) {
		if (packet instanceof Message) {
			Message pres = (Message) packet;
			if (pres.getType() == Message.Type.headline)
				return true;
		}
		return false;
	}

}
