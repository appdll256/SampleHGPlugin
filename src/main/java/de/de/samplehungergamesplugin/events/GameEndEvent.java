package de.de.samplehungergamesplugin.events;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

//PlaceHolder class
public final class GameEndEvent extends Event{

	private static final HandlerList HANDLER_LIST = new HandlerList();

	public GameEndEvent(){}

	public static HandlerList getHandlerList(){
		return HANDLER_LIST;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
