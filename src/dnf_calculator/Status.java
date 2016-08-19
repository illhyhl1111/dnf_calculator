package dnf_calculator;
//import java.util.HashMap;

class StatusArray
{
	public static final int STATNUM = 17;
	public static final int ELEMENTNUM = 4;
	public static final String[] statusList =
		{
			"str", "intell", "sta", "will",
			"atk_phy", "atk_mag", "atk_noDef_phy", "atk_noDef_mag",
			"def_dec_fixed", "def_dec_percent",
			"dam_inc", "dam_crt", "dam_add",
			"element_fire","element_water", "element_light","element_darkenss"
		};
	
	//public HashMap<StatusList, Integer> hMap = new HashMap<StatusList, Integer>();
	/*for(int i=0; i<STATNUM, i++)
	{
		hMap.put(i, statusList[i]);
	}*/
	
}

interface StatusList
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEMT_DARKNESS=3;
	int ATK_PHY=4; int  ATK_MAG=5; int  ATK_NODEF_PHY=6; int  ATK_NODEF_MAG=7; 
	int DEF_DEC_FIXED=8; int  DEF_DEC_PERCENT=9; 
	int DAM_INC=10; int  DAM_CRT=11; int  DAM_ADD=12;
	int STR=13; int INTELL=14; int STA=15; int WILL=16;
}

public class Status {
	
	private StatusInfo<?>[] statInfo; 
	
	public Status()
	{
		statInfo = new StatusInfo<?>[StatusArray.STATNUM];
		int i;
		for(i=0; i<StatusArray.ELEMENTNUM; i++)
			statInfo[i] = new StatusInfo<StatInfo>(StatusArray.statusList[i], new StatInfo(0));
		
		for(; i<StatusArray.STATNUM; i++)
			statInfo[i] = new StatusInfo<ElementInfo>(StatusArray.statusList[i], new ElementInfo(false, 0));
	}
	
	public void setStatus(Status_Public stat)
	{
		
	}
}

class Status_Public
{
	public StatusInfo<?>[] statInfo; 
	
	public Status_Public()
	{
		statInfo = new StatusInfo<?>[StatusArray.STATNUM];
		int i;
		for(i=0; i<StatusArray.ELEMENTNUM; i++)
			statInfo[i] = new StatusInfo<StatInfo>(StatusArray.statusList[i], new StatInfo(0));
		
		for(; i<StatusArray.STATNUM; i++)
			statInfo[i] = new StatusInfo<ElementInfo>(StatusArray.statusList[i], new ElementInfo(false, 0));
	}
	
	public void setStat(int stat, int strength)
	{
		statInfo[stat].setStatus(strength);
	}
	
	public void setElementStat(int stat, int strength, boolean activated)
	{
		if(StatusArray.ELEMENTNUM>stat) statInfo[stat].setElementStatus(strength, activated);
		else; //Make Error
	}
}

class StatInfo
{
	int str;
	public StatInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
}

class ElementInfo extends StatInfo
{
	boolean hasElement;
	
	public ElementInfo(boolean activated, int strength)
	{
		super(strength);
		hasElement=activated;
	}
	
	public void setInfo(boolean activated, int strength)
	{
		super.setInfo(strength);
		hasElement=activated;
	}
	
	public void setInfo(int strength)
	{
		super.setInfo(strength);
	}
}

class StatusInfo<T extends StatInfo>
{
	private String statName;
	private T str;
	
	public StatusInfo(String name, T strength){
		statName=name;
		str=strength;
	}
	
	public void setStatus(int strength){
		str.setInfo(strength);
	}
	
	public void setElementStatus(int strength, boolean activated)
	{
		ElementInfo temp = (ElementInfo)str;
		temp.setInfo(activated, strength);
	}
}