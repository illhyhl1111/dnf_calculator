package dnf_InterfacesAndExceptions;

public enum Weapon_detailType {
	SWORD_LONGSWORD(Weapon_type.SWORD), SWORD_CLUB(Weapon_type.SWORD), SWORD_SHORTSWORD(Weapon_type.SWORD),
	SWORD_KATANA(Weapon_type.SWORD), SWORD_LIGHTSWORD(Weapon_type.SWORD),
	
	FIGHTER_TONFA(Weapon_type.FIGHTER), FIGHTER_CLAW(Weapon_type.FIGHTER), FIGHTER_GAUNTLET(Weapon_type.FIGHTER),
	FITGHTER_BOXGLOVE(Weapon_type.FIGHTER), FIGHTER_KNUCKLE(Weapon_type.FIGHTER),
	
	GUN_HCANON(Weapon_type.GUN), GUN_MUSKET(Weapon_type.GUN), GUN_REVOLVER(Weapon_type.GUN),
	GUN_BOWGUN(Weapon_type.GUN), GUN_AUTOPISTOL(Weapon_type.GUN),
	
	MAGE_STAFF(Weapon_type.MAGE), MAGE_ROD(Weapon_type.MAGE), MAGE_POLE(Weapon_type.MAGE),
	MAGE_SPEAR(Weapon_type.MAGE), MAGE_BROOM(Weapon_type.MAGE),
	
	PRIEST_CROSS(Weapon_type.PRIEST), PRIEST_BATTLEAXE(Weapon_type.PRIEST), PRIEST_ROSARY(Weapon_type.PRIEST),
	PRIEST_TOTEM(Weapon_type.PRIEST), PRIEST_SCYTHE(Weapon_type.PRIEST),
	
	THIEF_WAND(Weapon_type.THIEF), THIEF_DAGGER(Weapon_type.THIEF), THIEF_TWINSWORD(Weapon_type.THIEF), THIEF_CHAKRAWEAPON(Weapon_type.THIEF),
	
	LANCE_PIKE(Weapon_type.LANCE), LANCE_HALBERD(Weapon_type.LANCE);
	
	Weapon_detailType(Weapon_type type)
	{
		bigType=type;
	}
	Weapon_type bigType;
}
