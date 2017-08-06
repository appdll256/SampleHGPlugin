package de.de.samplehungergamesplugin.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public final class VotingEndEvent extends Event{

	private static final HandlerList HANDLER_LIST = new HandlerList();

	private final String votedMap;

	public VotingEndEvent(String votedMap){
		this.votedMap = votedMap;
	}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}

	public String getVotedMap() {
		return votedMap;
	}
}
