package dnf_InterfacesAndExceptions;

public enum Weapon_detailType {
	SWORD_LONGSWORD(Weapon_type.SWORD, "대검"), SWORD_CLUB(Weapon_type.SWORD, "둔기"), SWORD_SHORTSWORD(Weapon_type.SWORD, "소검"),
	SWORD_KATANA(Weapon_type.SWORD, "도"), SWORD_LIGHTSWORD(Weapon_type.SWORD, new Job[]{Job.WEAPONMASTER, Job.VEGABOND, Job.DARKKNIGHT}, "광검"),
	
	FIGHTER_TONFA(Weapon_type.FIGHTER, "통파"), FIGHTER_CLAW(Weapon_type.FIGHTER, "클로"), FIGHTER_GAUNTLET(Weapon_type.FIGHTER, "건틀릿"),
	FITGHTER_BOXGLOVE(Weapon_type.FIGHTER, new Job[]{Job.STRIKER_F, Job.STRIKER_M}, "권투글러브"), FIGHTER_KNUCKLE(Weapon_type.FIGHTER, "너클"),
	
	GUN_HCANON(Weapon_type.GUN, "핸드캐넌"), GUN_MUSKET(Weapon_type.GUN, "머스켓"), GUN_REVOLVER(Weapon_type.GUN, "리볼버"),
	GUN_BOWGUN(Weapon_type.GUN, "보우건"), GUN_AUTOPISTOL(Weapon_type.GUN, "자동권총"),
	
	MAGE_STAFF(Weapon_type.MAGE, "스태프"), MAGE_ROD(Weapon_type.MAGE, "로드"), MAGE_POLE(Weapon_type.MAGE, "봉"),
	MAGE_SPEAR(Weapon_type.MAGE, "창"), MAGE_BROOM(Weapon_type.MAGE, new Job[]{Job.WITCH, Job.DIMENSIONWALKER, Job.CREATOR}, "빗자루"),
	
	PRIEST_CROSS(Weapon_type.PRIEST, "십자가"), PRIEST_BATTLEAXE(Weapon_type.PRIEST, "배틀엑스"), PRIEST_ROSARY(Weapon_type.PRIEST, "염주"),
	PRIEST_TOTEM(Weapon_type.PRIEST, "토템"), PRIEST_SCYTHE(Weapon_type.PRIEST, "낫"),
	
	THIEF_WAND(Weapon_type.THIEF, "완드"), THIEF_DAGGER(Weapon_type.THIEF, "단검"),
	THIEF_TWINSWORD(Weapon_type.THIEF, "쌍검"), THIEF_CHAKRAWEAPON(Weapon_type.THIEF, new Job[]{Job.KUNOICH}, "차크라웨펀"),
	
	LANCE_PIKE(Weapon_type.LANCE, "장창"), LANCE_HALBERD(Weapon_type.LANCE, "미늘창"),
	
	NONE(Weapon_type.NONE, "없음");
	
	Weapon_type bigType;
	Job[] required_job;
	String name;
	
	Weapon_detailType(Weapon_type type, String name)
	{
		bigType=type;
		required_job=new Job[0];
		this.name=name;
	}
	Weapon_detailType(Weapon_type type, Job[] required_job, String name)
	{
		bigType=type;
		this.required_job=required_job;
		this.name=name;
	}
	
	public String getName() {return name;}
	
	public static Weapon_detailType stringToType(String type)
	{
		for(Weapon_detailType types : Weapon_detailType.values())
			if(types.getName().equals(type)) return types;
		return NONE;
	}
	
	public boolean enabled(Job job)
	{
		if(required_job.length!=0){
			for(Job rJob : required_job){
				if(rJob==job) return true;
			}
			return false;
		}
		
		if(this==NONE) return true;
		
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
