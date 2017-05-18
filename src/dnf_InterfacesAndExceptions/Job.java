package dnf_InterfacesAndExceptions;

import java.util.LinkedList;

public enum Job implements java.io.Serializable
{
	LAUNCHER_F(Character_type.GUNNER_F, "스톰트루퍼", "카시야스) 명속은거들뿐"),
	RANGER_F(Character_type.GUNNER_F, "크림슨 로제", "던조닉) 프사암릿내놔"),
	SPITFIRE_F(Character_type.GUNNER_F, "프레이야", "프레이) 미녀총잡이, 바칼) 트롤프레기야"),
	MECHANIC_F(Character_type.GUNNER_F, "옵티머스"),
	LAUNCHER_M(Character_type.GUNNER_M, "디스트로이어", "카시야스) Nodata"), RANGER_M(Character_type.GUNNER_M, "레이븐", "안톤) 속사포탄환"),
	SPITFIRE_M(Character_type.GUNNER_M, "커맨더", "바칼) 혈누"), MECHANIC_M(Character_type.GUNNER_M, "프라임"),
	CRUSADER(Character_type.PRIEST, "세인트"), INFIGHTER(Character_type.PRIEST, "저스티스"),
	AVENGER(Character_type.PRIEST, "이모탈"), EXORCIST(Character_type.PRIEST, "태을선인", "??) 종교, 디레지에) 성불왕"),
	NENMASTER_F(Character_type.FIGHTER_F, "염제 폐월수화", "카인)카이123, 던조닉) 프사암릿내놔"), GRAPPLER_F(Character_type.FIGHTER_F, "얼티밋 디바"),
	STRIKER_F(Character_type.FIGHTER_F, "카이저"), STREETFIGHTER_F(Character_type.FIGHTER_F, "용독문주", "구현 예정"),
	NENMASTER_M(Character_type.FIGHTER_M, "염황 광풍제월"), GRAPPLER_M(Character_type.FIGHTER_M, "그랜드 마스터", "구현 예정"),
	STRIKER_M(Character_type.FIGHTER_M, "패황", "카시야스) 발차기팡팡"), STREETFIGHTER_M(Character_type.FIGHTER_M, "명왕"),
	WEAPONMASTER(Character_type.SWORDMAN_M, "검신", "구현 예정"), BUSERKER(Character_type.SWORDMAN_M, "블러드 이블", "던조닉) RoDem"),
	ASURA(Character_type.SWORDMAN_M, "인다라천"), SOULMASTER(Character_type.SWORDMAN_M, "다크로드"),
	DARKTEMPELAR(Character_type.SWORDMAN_F, "네메시스"), SWORDMASTER(Character_type.SWORDMAN_F, "마제스티"),
	VEGABOND(Character_type.SWORDMAN_F, "검제", "구현 예정"), DEMONSLAYER(Character_type.SWORDMAN_F, "디어사이드", "던조닉) 프사암릿내놔"),
	ELEMENTALMASTER(Character_type.MAGE_F, "오버마인드", "프레이) 별마리, 카인) 롤이짱"), SUMMONER(Character_type.MAGE_F, "이클립스"), 
	WITCH(Character_type.MAGE_F, "지니위즈"), BATTLEMAGE(Character_type.MAGE_F, "아슈타르테", "프레이) 무가차"),
	ELEMENTALBOMBER(Character_type.MAGE_M, "오블리비언", "카시야스) 피디대정령"), GLACIALMASTER(Character_type.MAGE_M, "이터널"), 
	SWIFTMASTER(Character_type.MAGE_M, "아이올로스"), BOOLDMAGE(Character_type.MAGE_M, "뱀파이어 로드"),
	DIMENSIONWALKER(Character_type.MAGE_M, "어센션"), CREATOR(Character_type.MAGE_F, "크리에이터", "카인) 중력맨"),
	DARKKNIGHT(Character_type.SWORDMAN_M, "다크나이트"),
	VANGUARD(Character_type.DEMONICLANCER, "워로드", "바칼) 찔례"), DUALIST(Character_type.DEMONICLANCER, "듀란달", "바칼)맛있는듀얼"),
	ROUGE(Character_type.THIEF, "알키오네", "카인) END, 카시야스) 깜냥♬"), KUNOICH(Character_type.THIEF, "시라누이", "구현 예정"),
	SHADOWDANCER(Character_type.THIEF, "그림리퍼", "디레지에) Quist"), NECROMENCER(Character_type.THIEF, "타나토스", "구현 예정"),
	CHAOS(Character_type.KNIGHT, "마신"), ELVENKNIGHT(Character_type.KNIGHT, "가이아", "던조닉) 유려한사령"),
	
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
	public String getContributor() {
		if(contributor.equals("미구현") || contributor.equals("구현 예정")) return contributor;
		return "직업 정보 제공 - "+contributor;
	}
	
	public static Job getJob(String name)
	{
		for(Job job : Job.values()){
			if(job.getName().equals(name)) return job;
			else if(job.getName().equals(name.replace("미구현", ""))) return job;
			else if(job.getName().equals(name.replace("구현 예정", ""))) return job;
		}
		return NONE;
	}
	
	public static String[] getImplementedList()
	{
		return new String[] {
				LAUNCHER_F.getName(), RANGER_F.getName(), DEMONSLAYER.getName(), SPITFIRE_M.getName(), ELEMENTALBOMBER.getName(),
				LAUNCHER_M.getName(), RANGER_M.getName(), SPITFIRE_F.getName(), EXORCIST.getName(), BATTLEMAGE.getName(), ELEMENTALMASTER.getName(),
				STRIKER_M.getName(), VANGUARD.getName(), ELVENKNIGHT.getName(),
		};
	}
	
	public static String[] getImplementedList(Character_type type)
	{
		LinkedList<String> list = new LinkedList<String>();
		for(Job job : Job.values())
			if(job.charType==type){
				if(job.getContributor().equals("미구현") || job.getContributor().equals("구현 예정"))
					list.add(job.name+"("+job.getContributor()+")");
				else list.add(job.name);
			}
		return list.toArray(new String[0]);
	}
}		
