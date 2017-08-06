package de.de.samplehungergamesplugin;

import de.de.samplehungergamesplugin.annotations.NotNull;
import de.de.samplehungergamesplugin.annotations.Nullable;
import org.bukkit.Location;
import org.bukkit.World;

/**
 *
 * @since 0.1
 */
@Deprecated
public final class ArgumentValidator {

	/**
	 * Parses the given argument to an {@link Integer}
	 *
	 * @param arg The argument
	 * @return The parsed {@link Integer} or null, if the value could not be parsed
	 */
	@Nullable
	public static Integer parseInt(String arg){
		try{
			//Calling as test method and return
			return Integer.valueOf(arg);
		}catch(NumberFormatException numFormEx){
			return null;
		}
	}


	/**
	 * Parses the given argument to a {@link Float}
	 *
	 * @param arg The argument
	 * @return The parsed {@link Float} or {@link Float#NaN}, if the value could not be parsed
	 */
	public static Float parseFloat(String arg){
		try{
			//Calling as test method and return
			return Float.valueOf(arg);
		}catch(NumberFormatException numFormEx){
			return Float.NaN;
		}
	}


	/**
	 * Parses the given argument to a {@link Double}
	 *
	 * @param arg The argument
	 * @return The parsed {@link Double} or {@link Double#NaN}, if the value could not be parsed
	 */
	public static Double parseDouble(String arg){
		try{
			//Calling as test method and return
			return Double.valueOf(arg);
		}catch(NumberFormatException numFormEx){
			return Double.NaN;
		}
	}


	/**
	 * <p>
	 *     Parses the argument to a {@link Location} object
	 * </p>
	 * <p>
	 *     Note that this follows the "(x,y,z)" schema
	 * </p>
	 *
	 * @param arg The argument
	 * @return The parsed {@link Location} object or null, if the value could not be parsed
	 */
	@Nullable
	public static Location parseLocation(@NotNull World world, String arg){
		String locationCoordsConcat = arg.substring(1, arg.length() - 1);

		String[] locationCoords = locationCoordsConcat.split(",", 3);

		try{
			return new Location(world, Double.parseDouble(locationCoords[0]), Double.parseDouble(locationCoords[1]),
					Double.parseDouble(locationCoords[2]));
		}catch(NumberFormatException numFormEx){
			return null;
		}

	}

}
