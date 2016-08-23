package dnf_calculator;

import org.eclipse.swt.graphics.Image;

public class Equipment implements Cloneable
{
	String name;										//이름
	Image icon;											//아이콘
	Equip_part part;									//부위
	Equip_Rarity rarity;								//희귀도
	Dimension_stat dimStat;								//차원작
	int reinforce;										//강화수치
	VillageStatusList vStat;							//마을스탯
	DungeonStatusList dStat;							//인던스탯
	StatusAndName supplement;							//보주
	SetName setName;									//셋옵이름
	
	public Equipment(String name, Image icon, Equip_part part, Equip_Rarity rarity, Dimension_stat dimStat,
			int reinforce, StatusAndName supplement, SetName setName)
	{
		this.name=name;
		this.icon=icon;
		this.part=part;
		this.rarity=rarity;
		this.dimStat=dimStat;
		this.reinforce=reinforce;
		this.supplement=supplement;
		this.setName=setName;
		
		vStat = new VillageStatusList();
		dStat = new DungeonStatusList();
	}
	
	public Equipment(String name, Image icon, Equip_part part, Equip_Rarity rarity)
	{
		this(name, icon, part, rarity, Dimension_stat.NONE, 0, new StatusAndName(StatList.STR, new StatusInfo(0)), SetName.NONE);
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}

enum SetName
{
	NONE,
	DARKGOTH, CHAMELEON, SUBMARINE, DARKHOLE, INFINITY,
	BURNINGSPELL, DROPPER, TACTICAL, ASSASSIN, NATURALGARDIAN,
	EYESHIELD, SPIDERQUEEN, FORBIDDENCONTRACT, MAELSTORM, FULLPLATE,
	SUPERSTAR, REFINEDSPELL, ICEQUEEN, HUGEFORM,
	GREATGLORY, GRACIA, DEVASTEDGRIEF, BURIEDSCREAM, ROOTOFDISEASE, ROMANTICE, CURSEOFSEAGOD,
	RELIC
}

enum Dimension_stat
{
	STR, INT, STA, WILL, NONE;
}

enum Equip_Rarity
{
	COMMON, UNCOMMON, RARE, CHRONICLE, UNIQUE, LEGENDARY, EPIC;
}

enum Equip_part
{
	ROBE, TROUSER, SHOULDER, BELT, SHOES, NECKLESS, BRACELET, RING, EARRING, AIDEQUIPMENT, MAGICSTONE;
}