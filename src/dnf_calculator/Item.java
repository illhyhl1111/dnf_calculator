package dnf_calculator;

import org.eclipse.swt.graphics.Image;

public abstract class Item implements Cloneable
{
	String name;										//이름
	Image icon;											//아이콘
	Item_Rarity rarity;								//희귀도
	VillageStatusList vStat;							//마을스탯
	DungeonStatusList dStat;							//인던스탯
	
	public Item(String name, Image icon, Item_Rarity rarity)
	{
		this.name=name;
		this.icon=icon;
		this.rarity=rarity;
		vStat = new VillageStatusList();
		dStat = new DungeonStatusList();
	}
	public Item()
	{
		name="이름없음";
		icon=null; 			//TODO
		rarity=Item_Rarity.NONE;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
}

class Equipment extends Item
{
	Equip_part part;									//부위
	Dimension_stat dimStat;								//차원작
	int reinforce;										//강화수치
	Card card;											//보주
	SetName setName;									//셋옵이름
	
	public Equipment(String name, Image icon,Item_Rarity rarity, Equip_part part, Dimension_stat dimStat,
			int reinforce, Card card, SetName setName)
	{	
		super(name, icon, rarity);
		this.part=part;
		this.dimStat=dimStat;
		this.reinforce=reinforce;
		this.card=card;
		this.setName=setName;
	}
	public Equipment(String name, Image icon, Item_Rarity rarity, Equip_part part)
	{
		this(name, icon, rarity, part, Dimension_stat.NONE, 0, new Card("없음", null, Item_Rarity.NONE, AddOn.NONE), SetName.NONE);
	}
	public Equipment(Equip_part part) {
		super();
		this.part=part;
	}
}

class Avatar extends Item
{
	Avatar_part part;
	Emblem emblem1;
	Emblem emblem2;
	
	public Avatar(String name, Image icon, Item_Rarity rarity, Avatar_part part, Emblem emblem1, Emblem emblem2)
	{
		super(name, icon, rarity);
		this.part=part;
		this.emblem1=emblem1;
		this.emblem2=emblem2;
	}
	
	public Avatar(String name, Image icon, Item_Rarity rarity, Avatar_part part)
	{
		super(name, icon, rarity);
		this.part=part;
	}
	public Avatar(Avatar_part part) {
		super();
		this.part=part;
	}
}

class Creature extends Item
{
	public Creature(String name, Image icon, Item_Rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Creature(){ super();}
}

class Title extends Item
{
	public Title(String name, Image icon, Item_Rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Title(){ super();}
}

class Consumeable extends Item
{
	public Consumeable(String name, Image icon, Item_Rarity rarity)
	{
		super(name, icon, rarity);
	}
	public Consumeable(){ super();}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof Consumeable)
			if(this.name.equals(((Consumeable) o).name)) return true;
		return false;
	}
}

class Card extends Item
{
	AddOn type;
	public Card(String name, Image icon, Item_Rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
}

class Emblem extends Item
{
	AddOn type;
	public Emblem(String name, Image icon, Item_Rarity rarity, AddOn type)
	{
		super(name, icon, rarity);
		this.type=type;
	}
}

enum AddOn
{
	NONE, CRITICAL, STAT, ATK, ACCESSORY, AIDEQUIPMENT, MAGICSTONE, RED, GREEN, YELLOW, BLUE,
	ROBE, WEAPON, TITLE, RED_GREEN, YELLOW_BLUE, MULTIPLE_COLOR
}

enum Avatar_part implements Equipable
{
	CAP, HAIR, EARRING, SHOULDER, COAT, BELT, PANTS, SHOES, SKIN, AURA
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

enum Item_Rarity
{
	NONE, COMMON, UNCOMMON, RARE, CHRONICLE, UNIQUE, LEGENDARY, EPIC;
}

enum Equip_part implements Equipable
{
	ROBE, TROUSER, SHOULDER, BELT, SHOES, NECKLESS, BRACELET, RING, EARRING, AIDEQUIPMENT, MAGICSTONE;
}

interface Equipable
{
}