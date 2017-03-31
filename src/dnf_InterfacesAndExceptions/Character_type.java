package dnf_InterfacesAndExceptions;

public enum Character_type implements java.io.Serializable
{
	SWORDMAN_M, SWORDMAN_F, FIGHTER_M, FIGHTER_F, GUNNER_M, GUNNER_F, MAGE_M, MAGE_F, PRIEST, THIEF, KNIGHT, DEMONICLANCER, ALL,
	NONE;
	
	public static String[] getCharacterTypeList()
	{
		return new String[] {"귀검사(남)", "귀검사(여)", "격투가(여)", "격투가(남)", "거너(남)", "거너(여)",
				"마법사(여)", "마법사(남)", "프리스트(남)", "도적", "나이트", "마창사"};
	}
	
	public static Character_type characterStringToEnum(String name)
	{
		switch(name)
		{
		case "귀검사(남)": return SWORDMAN_M;
		case "귀검사(여)": return SWORDMAN_F;
		case "격투가(여)": return FIGHTER_F;
		case "격투가(남)": return FIGHTER_M;
		case "거너(남)": return GUNNER_M;
		case "거너(여)": return GUNNER_F;
		case "마법사(여)": return MAGE_F;
		case "마법사(남)": return MAGE_M;
		case "프리스트(남)": return PRIEST;
		case "프리스트(여)": return PRIEST;
		case "도적": return THIEF;
		case "나이트": return KNIGHT;
		case "마창사": return DEMONICLANCER;
		default: return NONE;
		}
	}
}
