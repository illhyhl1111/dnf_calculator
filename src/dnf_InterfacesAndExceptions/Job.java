package dnf_InterfacesAndExceptions;

public enum Job implements java.io.Serializable
{
	LAUNCHER_F(Character_type.GUNNER_F, "스톰투르퍼", "카시야스) 명속은거들뿐"),
	RANGER_F(Character_type.GUNNER_F, "크림슨 로제", "카인) 공변"),
	CRUSADER(Character_type.PRIEST, "세인트"),
	NENMASTER_F(Character_type.FIGHTER_F, "염제 폐월수화"), GRAPPLER_F(Character_type.FIGHTER_F, "얼티밋 디바"),
	WEAPONMASTER(Character_type.SWORDMAN_M, "검신"),
	DARKTEMPELER(Character_type.SWORDMAN_F, "네메시스"),
	
	
	UNIMPLEMENTED(Character_type.NONE, "미구현"),
	UNIMPLEMENTED_SWORDMAN(Character_type.SWORDMAN_M, "미구현-귀검사"),
	UNIMPLEMENTED_FIGHTER(Character_type.FIGHTER_F, "미구현-격투가"),
	UNIMPLEMENTED_GUNNER(Character_type.GUNNER_M, "미구현-거너"),
	UNIMPLEMENTED_MAGE(Character_type.MAGE_F, "미구현-마법사"),
	UNIMPLEMENTED_PRIEST(Character_type.PRIEST, "미구현-프리스트"),
	UNIMPLEMENTED_THIEF(Character_type.THIEF, "미구현-도적"),
	UNIMPLEMENTED_DEMONICLANCER(Character_type.DEMONICLANCER, "미구현-마창사"),
	
	NONE(Character_type.NONE, "없음");
	
	String name;
	String contributor;
	
	Job(Character_type type, String name)
	{
		charType=type;
		this.name=name;
		contributor="미구현";
	}
	Job(Character_type type, String name, String contributor)
	{
		charType=type;
		this.name=name;
		this.contributor=contributor;
	}
	
	public String getName() {return name;}
	public final Character_type charType;
	public String getContributor() {return "직업 정보 제공 - "+contributor;}
	
	public static Job getJob(String name)
	{
		for(Job job : Job.values())
			if(job.getName().equals(name)) return job;
		return NONE;
	}
	
	public static String[] getImplementedList()
	{
		return new String[] {
				LAUNCHER_F.getName(), RANGER_F.getName(),
		};
	}
}
