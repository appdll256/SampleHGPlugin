package de.de.samplehungergamesplugin.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//Placeholder class
public final class GameStartEvent extends Event{

	private static final HandlerList HANDLER_LIST = new HandlerList();

	public GameStartEvent(){}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
