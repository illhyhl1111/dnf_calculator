package dnf_InterfacesAndExceptions;

public enum Job implements java.io.Serializable
{
	LAUNCHER_F(Character_type.GUNNER_F),
	
	
	UNIMPLEMENTED(Character_type.NONE),
	UNIMPLEMENTED_SWORDMAN(Character_type.SWORDMAN_M),
	UNIMPLEMENTED_FIGHTER(Character_type.FIGHTER_F),
	UNIMPLEMENTED_GUNNER(Character_type.GUNNER_M),
	UNIMPLEMENTED_MAGE(Character_type.MAGE_F),
	UNIMPLEMENTED_PRIEST(Character_type.PRIEST),
	UNIMPLEMENTED_THIEF(Character_type.THIEF),
	UNIMPLEMENTED_DEMONICLANCER(Character_type.DEMONICLANCER);
	
	Job(Character_type type)
	{
		charType=type;
	}
	public final Character_type charType;
}
