package de.de.samplehungergamesplugin;


import de.de.samplehungergamesplugin.events.GameEndEvent;
import de.de.samplehungergamesplugin.events.GameStartEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
final class Arena {
	private final boolean maintenance;

	private final Thread chestClock;
	private volatile boolean stopped = false;

	private final String name;
	private final Object[][] chests;

	private final Server server;
	private final World world;
	private final Database database;

	Arena(Server server, String arenaName, boolean maintenance) throws Error {
		this.maintenance = maintenance;
		this.server = server;
		name = arenaName;
		world = server.getWorld(name);
		database = new Database("arenas.db");
		database.execute("CREATE TABLE IF NOT EXISTS " + name + " (chestLocations TEXT NOT NULL);");

		if (!maintenance) {
			try {
				ResultSet allInformation = database.query("SELECT * FROM " + name + ";");

				//Allocate chest locations
				StringBuilder builder = new StringBuilder("");
				if (allInformation.first()) {
					do {
						builder.append(allInformation.getString("chestLocations")).append("/");
					} while (allInformation.next());
					builder.deleteCharAt(builder.length());
				}

				String[] chestLocationsRaw = builder.toString().split("/");

				chests = new Object[chestLocationsRaw.length][2];

				for (int i = 0; i < chestLocationsRaw.length; i++) {
					String[] coords = chestLocationsRaw[i].split(",", 3);
					chests[i][0] = world.getBlockAt(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]),
							Integer.parseInt(coords[2]));
				}

				//Generate chests
				for (Block chest : (Block[]) chests[0])
					genChest(chest);

				chestClock = new Thread(() -> {
					while (!stopped) {
						for (int i = 0; i < (chests.length / 2); i++)
							if (chests[i][1] != null) {
								Long lastChestLoot = (Long) chests[i][1];

								if ((lastChestLoot + 180000) <= System.currentTimeMillis())
									genChest((Block) chests[i][0]);
							}

					}

				});

				allInformation.close();

			}catch (SQLException sqlEx) {
				//TODO: Move to Database class
				throw new Error("Could not gather information about the area!", sqlEx);
			}
		}else{
			chestClock = null;
			chests = null;
		}
	}


	void addChestLocation(int x, int y, int z){
		database.update("INSERT INTO " + name + " (chestLocations) VALUES ('" + String.format("%1$d,%2$d,%3$d", x, y, z) + "');");
	}


	void deleteArena(){
		if(maintenance){
			database.execute("DROP TABLE " + name + ";");
		}
	}


	void removeChestLocation(int x, int y, int z){
		database.update("DELETE FROM " + name + " WHERE " + String.format("chestLocations='%1$d,%2$d,%3$d'", x, y, z) + " ;");
	}


	private void genChest(Block chest){
		chest.setType(Material.CHEST);
		Chest emptyChest = (Chest) chest.getState();
		Inventory chestInventory = emptyChest.getBlockInventory();
		chestInventory.clear();
		chestInventory.setItem(RandomGenerator.randomNumber(0, chestInventory.getSize()), RandomGenerator.randomUsableFightItemStack());
	}


	void startGame(Player[] players){
		if(!maintenance) {
			server.getPluginManager().callEvent(new GameStartEvent());

			//TODO: TP all players to arena and spread them

			chestClock.start();
		}
	}


	void endGame(){
		if(!maintenance) {
			stopped = true;
			server.getPluginManager().callEvent(new GameEndEvent());
		}
	}


	void maintenanceTP(Player player){
		player.teleport(world.getSpawnLocation());
	}

}
