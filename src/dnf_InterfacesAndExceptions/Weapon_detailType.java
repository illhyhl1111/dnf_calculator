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
	
	LANCE_PIKE(Weapon_type.LANCE), LANCE_HALBERD(Weapon_type.LANCE),
	
	NONE(Weapon_type.NONE);
	
	Weapon_type bigType;
	JobList required_job;
	
	Weapon_detailType(Weapon_type type)
	{
		bigType=type;
		required_job=JobList.UNIMPLEMENTED;
	}
	Weapon_detailType(Weapon_type type, JobList required_job)
	{
		bigType=type;
		this.required_job=required_job;
	}
	
	public boolean enabled(JobList job)
	{
		if(required_job!=JobList.UNIMPLEMENTED){
			if(this.required_job==job) return true;
			else return false;
		}
		
		switch(job.charType)
		{
		case SWORDMAN_M: case SWORDMAN_F: case KNIGHT:
			if(bigType==Weapon_type.SWORD) return true;
			else return false;
		
		case FIGHTER_M: case FIGHTER_F:
			if(bigType==Weapon_type.FIGHTER) return true;
			else return false;
			
		case GUNNER_M: case GUNNER_F:
			if(bigType==Weapon_type.GUN) return true;
			else return false;
			
		case MAGE_M: case MAGE_F:
			if(bigType==Weapon_type.MAGE) return true;
			else return false;
			
		case PRIEST:
			if(bigType==Weapon_type.PRIEST) return true;
			else return false;		
			
		case THIEF:
			if(bigType==Weapon_type.THIEF) return true;
			else return false;
		
		case DEMONICLANCER:
			if(bigType==Weapon_type.LANCE) return true;
			else return false;
			
		default:
			return true;
		}
	}
}
