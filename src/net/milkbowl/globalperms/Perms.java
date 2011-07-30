package net.milkbowl.globalperms;

public enum Perms {
	USE_CHEST("use.chest"),
	USE_DOOR("use.door"),
	USE_SIGN("use.sign"),
	USE_BED("use.bed"),
	USE_LAVA("restricted.lava.use"),
	USE_WATER("restricted.water.use"),
	USE_ALL("use.all"),
	BUILD("build"),
	FILL_WATER("restricted.water.fill"),
	USE_FIRE("restricted.fire"),
	FILL_LAVA("restricted.lava.fill"),
	CHAT("use.chat"),
	COMBAT_IMMUNE("immune.mobs"),
	PVP_IMMUNE("immune.pvp"),
	DENY_LOGIN("login.deny");

	String perm = null;

	Perms(String perm) {
		this.perm = perm;
	}

	public String getPerm() {
		return perm;
	}
}