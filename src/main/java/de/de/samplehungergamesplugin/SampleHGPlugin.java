package de.de.samplehungergamesplugin;


import de.de.samplehungergamesplugin.events.GameEndEvent;
import de.de.samplehungergamesplugin.events.GameStartEvent;
import de.de.samplehungergamesplugin.events.VotingEndEvent;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class SampleHGPlugin extends JavaPlugin{

	private static Server server;
	private static CommandHandler commandHandler;

	@Override
	public void onDisable() {
		super.onDisable();
	}


	@Override
	public void onEnable() {
		server = getServer();

		//Register commands
		commandHandler = new CommandHandler(server);

		getCommand("chest").setExecutor(commandHandler);
		getCommand("delete").setExecutor(commandHandler);
		getCommand("maintenance").setExecutor(commandHandler);
		getCommand("type").setExecutor(commandHandler);

		//Register events
		server.getPluginManager().registerEvents(new SampleHGPlugin.EventListener(), this);
	}


	private class EventListener implements Listener {

		private Arena arena;
		private boolean hasGameStarted = false;

		private Gamer[] involvedGamer;

		private void checkFinishConditions(){
			int aliveGamerCounter = 0;
			for (Gamer gamer : involvedGamer) {
				if(gamer != null && !gamer.hasDied())
					aliveGamerCounter++;
			}

			if(aliveGamerCounter <= 1){
				server.getPluginManager().callEvent(new GameEndEvent());
			}

		}

		//TODO: Migrate with server's voting plugin
		//Voting system event
		@EventHandler(priority = EventPriority.MONITOR)
		public void onEndVoting(VotingEndEvent votingEndEvent) {
			//Load arena
			arena = new Arena(server, votingEndEvent.getVotedMap(), false);

			//Call event from async thread -> no blocking cause of wait action
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					server.getPluginManager().callEvent(new GameStartEvent());
				}
			}, 10000);
		}


		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPlayerLogin(PlayerLoginEvent playerLoginEvent){
			if(commandHandler.isMaintenance()) {
				playerLoginEvent.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Server is in maintenance! Sorry!");
			}else{
				playerLoginEvent.allow();
			}
		}


		@EventHandler(priority = EventPriority.LOW)
		public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
			if(hasGameStarted)
				playerJoinEvent.getPlayer().setGameMode(GameMode.SPECTATOR);
		}


		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerLeave(PlayerQuitEvent playerLeaveEvent){
			if(hasGameStarted) {
				Player leavingPlayer = playerLeaveEvent.getPlayer();

				for (int i = 0; i < involvedGamer.length; i++) {
					if (leavingPlayer == involvedGamer[i].getPlayer())
						involvedGamer[i] = null;
				}

				checkFinishConditions();
			}
		}


		@EventHandler(priority = EventPriority.MONITOR)
		public void onPlayerDied(PlayerDeathEvent playerDeathEvent){
			//Set the player to "spectator" mode
			Player diedPlayer = playerDeathEvent.getEntity();
			diedPlayer.setGameMode(GameMode.SPECTATOR);

			for (Gamer gamer : involvedGamer) {
				if(gamer.getPlayer() == diedPlayer){
					gamer.setDeathTime(System.currentTimeMillis());
					gamer.setDied(true);
				}
			}
		}


		//Plugin intern
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onStartGame(GameStartEvent gameStartEvent) {
			//Get all joined player
			Collection collectedPlayer = server.getOnlinePlayers();

			Player[] currentlyLoggedPlayer = new Player[collectedPlayer.size()];
			collectedPlayer.toArray(currentlyLoggedPlayer);

			involvedGamer = new Gamer[currentlyLoggedPlayer.length];

			for (int i = 0; i < involvedGamer.length; i++) {
				involvedGamer[i] = new Gamer(currentlyLoggedPlayer[i]);
			}

			//Start game
			hasGameStarted = true;
			arena.startGame(currentlyLoggedPlayer);
		}


		@EventHandler(priority = EventPriority.HIGHEST)
		public void onEndGame(GameEndEvent gameEndEvent) {
			arena.endGame();

			//Kill null
			int notNullPropCounter = 0;
			for (Gamer anInvolvedGamer : involvedGamer) {
				if (anInvolvedGamer != null)
					notNullPropCounter++;
			}

			Gamer[] gamers = new Gamer[notNullPropCounter];
			int overhead = 0;
			for (int i = 0; i < gamers.length; i++) {
				if(involvedGamer[i + overhead] != null){
					gamers[i] = involvedGamer[i + overhead];
				}else{
					i--;
					overhead++;
				}
			}

			//Let the helper function do the duty
			Arrays.sort(gamers);

			//Display ranking
			server.broadcastMessage("Ranking:");
			for (int i = 1; (i - 1) < gamers.length; i++) {
				server.broadcastMessage(String.format("#%1$d >>> %2$s", i, gamers[i - 1].getPlayer().getName()));
			}

			//Calling any server-global plugin stubs here

			//Call a shutdown timer
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					server.shutdown();
				}
			}, 10000);
		}
	}
}
