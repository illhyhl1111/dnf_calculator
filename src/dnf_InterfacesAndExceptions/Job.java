package dnf_InterfacesAndExceptions;

public enum Job implements java.io.Serializable
{
	LAUNCHER_F(Character_type.GUNNER_F, "스톰트루퍼", "카시야스) 명속은거들뿐"),
	RANGER_F(Character_type.GUNNER_F, "크림슨 로제", "카인) 공변"),
	SPITFIRE_F(Character_type.GUNNER_F, "프레이야"),
	MECHANIC_F(Character_type.GUNNER_F, "옵티머스"),
	LAUNCHER_M(Character_type.GUNNER_M, "디스트로이어"), RANGER_M(Character_type.GUNNER_M, "레이븐"),
	SPITFIRE_M(Character_type.GUNNER_M, "커맨더"), MECHANIC_M(Character_type.GUNNER_M, "프라임"),
	CRUSADER(Character_type.PRIEST, "세인트"), INFIGHTER(Character_type.PRIEST, "저스티스"),
	AVENGER(Character_type.PRIEST, "이모탈"), EXORCIST(Character_type.PRIEST, "태을선인"),
	NENMASTER_F(Character_type.FIGHTER_F, "염제 폐월수화"), GRAPPLER_F(Character_type.FIGHTER_F, "얼티밋 디바"),
	STRIKER_F(Character_type.FIGHTER_F, "카이저"), STREETFIGHTER_F(Character_type.FIGHTER_F, "용독문주"),
	NENMASTER_M(Character_type.FIGHTER_M, "염황 광풍제월"), GRAPPLER_M(Character_type.FIGHTER_M, "그랜드 마스터"),
	STRIKER_M(Character_type.FIGHTER_M, "패황"), STREETFIGHTER_M(Character_type.FIGHTER_M, "명왕"),
	WEAPONMASTER(Character_type.SWORDMAN_M, "검신"), BUSERKER(Character_type.SWORDMAN_M, "블러드 이블"),
	ASURA(Character_type.SWORDMAN_M, "인다라천"), SOULMASTER(Character_type.SWORDMAN_M, "다크로드"),
	DARKTEMPELAR(Character_type.SWORDMAN_F, "네메시스"), SWORDMASTER(Character_type.SWORDMAN_F, "마제스티"),
	VEGABOND(Character_type.SWORDMAN_F, "검제"), DEMONSLAYER(Character_type.SWORDMAN_F, "디어사이드", "카인) 검魔"),
	ELEMENTALMASTER(Character_type.MAGE_F, "오버마인드"), SUMMONER(Character_type.MAGE_F, "이클립스"), 
	WITCH(Character_type.MAGE_F, "지니위즈"), BATTLEMAGE(Character_type.MAGE_F, "아슈타르테"),
	ELEMENTALBOMBER(Character_type.MAGE_M, "오블리비언"), GLACIALMASTER(Character_type.MAGE_M, "이터널"), 
	SWIFTMASTER(Character_type.MAGE_M, "아이올로스"), BOOLDMAGE(Character_type.MAGE_M, "뱀파이어 로드"),
	DIMENSIONWALKER(Character_type.MAGE_M, "어센션"), CREATOR(Character_type.MAGE_F, "크리에이터"),
	DARKKNIGHT(Character_type.SWORDMAN_M, "다크나이트"),
	VANGUARD(Character_type.DEMONICLANCER, "워로드"), DUALIST(Character_type.DEMONICLANCER, "듀란달"),
	ROUGE(Character_type.THIEF, "알키오네"), KUNOICH(Character_type.THIEF, "시라누이"),
	SHODOWDANCER(Character_type.THIEF, "그림리퍼"), NECROMENCER(Character_type.THIEF, "타나토스"),
	CHAOS(Character_type.KNIGHT, "마신"), ELVENKNIGHT(Character_type.THIEF, "가이아"),
	
	//UNIMPLEMENTED(Character_type.NONE, "미구현"),
	//UNIMPLEMENTED_SWORDMAN(Character_type.SWORDMAN_M, "미구현-귀검사"),
	//UNIMPLEMENTED_FIGHTER(Character_type.FIGHTER_F, "미구현-격투가"),
	//UNIMPLEMENTED_GUNNER(Character_type.GUNNER_M, "미구현-거너"),
	//UNIMPLEMENTED_MAGE(Character_type.MAGE_F, "미구현-마법사"),
	//UNIMPLEMENTED_PRIEST(Character_type.PRIEST, "미구현-프리스트"),
	//UNIMPLEMENTED_THIEF(Character_type.THIEF, "미구현-도적"),
	//UNIMPLEMENTED_DEMONICLANCER(Character_type.DEMONICLANCER, "미구현-마창사"),
	
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
				LAUNCHER_F.getName(), RANGER_F.getName(), DEMONSLAYER.getName()
		};
	}
}
