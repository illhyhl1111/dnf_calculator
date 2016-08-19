package dnf_calculator;

public abstract class Equipment 
{
	String org_name;
	//icon
	Equip_part part;
	String set;
	Dimension_stat dimStat;
	int reinforce;
	Status equip_stat;
	Rarity equip_rarity;
}

enum Dimension_stat
{
	STR, INT, STA, WILL, NONE;
}