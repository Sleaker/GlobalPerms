package net.milkbowl.globalperms;

public enum Perms {
	USE_CHEST("use.chest"),
	USE_DOOR("use.door"),
	USE_SIGN("use.sign"),
	USE_BED("use.bed"),
	USE_LAVA("use.lava"),
	USE_WATER("use.water"),
	USE_ALL("use.all"),
	BUILD("build"),
	FILL_WATER("restricted.water"),
	USE_FIRE("restricted.fire"),
	FILL_LAVA("restricted.lava"),
	CHAT("use.chat"),
	COMBAT_IMMUNE("damage.mobs"),
	PVP_IMMUNE("damage.pvp");

	String perm = null;

	Perms(String perm) {
		this.perm = perm;
	}

	public String getPerm() {
		return perm;
	}
}