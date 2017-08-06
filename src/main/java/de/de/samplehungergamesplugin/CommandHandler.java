package de.de.samplehungergamesplugin;


import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 */
public final class CommandHandler implements CommandExecutor{

	private Server server;
	private boolean maintenance = false;
	private Arena maintenanceArena;

	CommandHandler(Server server){
		this.server = server;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
		Player player = null;
		ConsoleCommandSender console = null;

		if(commandSender instanceof Player)
			player = (Player) commandSender;

		if(commandSender instanceof ConsoleCommandSender)
			console = (ConsoleCommandSender) commandSender;

		//Player only callable commands
		if(player != null) {
			switch (label) {
				case "chest":
					if(maintenance && args != null && args.length == 2) {
						String[] coords = args[1].substring(1, args[0].length() - 1).split(",", 2);
						Location newChestLocation = new Location(player.getWorld(), Double.parseDouble(coords[0]),
								Double.parseDouble(coords[1]), Double.parseDouble(coords[2]));

						switch (args[0]) {
							case "add":
								maintenanceArena.addChestLocation(newChestLocation.getBlockX(), newChestLocation.getBlockY(),
									newChestLocation.getBlockZ());
								break;

							case "remove":
								maintenanceArena.removeChestLocation(newChestLocation.getBlockX(), newChestLocation.getBlockY(),
										newChestLocation.getBlockZ());
								break;
							default:
								return false;
						}
					}else{
						return false;
					}

					break;

				case "delete":
					if(maintenance)
						maintenanceArena.deleteArena();
					else
						return false;
					break;

				case "maintenance":
					if(args != null) {
						switch(args[0]){
							case "start":
								if(args.length == 2) {
									maintenanceArena = new Arena(server, args[1], true);
									maintenance = true;
									maintenanceArena.maintenanceTP(player);
								}else{
									return false;
								}

								break;

							case "stop":
								maintenance = false;
								maintenanceArena = null;

								//Shutdown for restart
								server.shutdown();

								break;

						}

					}

					break;

				case "type":

					break;

				default:
					return false;
			}
		}else{
			return false;
		}

		return true;
	}


	boolean isMaintenance(){
		return maintenance;
	}

}
