package de.de.samplehungergamesplugin;


import org.bukkit.entity.Player;


final class Gamer implements Comparable<Gamer>{

	private final Player player;

	private boolean died = false;
	private long deathTime = 0;


	Gamer(Player player){
		this.player = player;
	}


	boolean hasDied() {
		return died;
	}

	long getDeathTime() {
		return deathTime;
	}

	Player getPlayer() {
		return player;
	}

	void setDeathTime(long deathTime) {
		this.deathTime = deathTime;
	}

	void setDied(boolean died) {
		this.died = died;
	}

	@Override
	public int compareTo(Gamer o) {
		long customDeathTime = o.deathTime;

		if(deathTime < customDeathTime)
			return 1;

		if(deathTime == customDeathTime)
			return 0;

		if(deathTime > customDeathTime)
			return -1;

		//Case, which never happens
		return -2;
	}
}
