package dnf_class;

import java.util.HashMap;

import dnf_calculator.AbstractStatusInfo;
import dnf_calculator.BooleanInfo;
import dnf_calculator.StatusList;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.StatusInfo;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class Monster extends IconObject implements java.io.Serializable{							/**
	 * 
	 */
	private static final long serialVersionUID = -207946696464873997L;

	public final String name;
	private AbstractStatusInfo[] monstInfo;
	public StatusList monsterFeature;
	private static HashMap<String, Integer> statHash = new HashMap<String, Integer>(); 
	private static boolean statHashSetted = false;
	
	public Monster(String name)								
	{
		this.name=name;
		monstInfo = new AbstractStatusInfo[Monster_StatList.STATNUM];
		int i;
		for(i=0; i<Monster_StatList.BOOLNUM; i++)
			monstInfo[i] = new BooleanInfo(false);
		
		for(; i<Monster_StatList.STATNUM; i++)
			monstInfo[i] = new StatusInfo(0);
		
		monstInfo[Monster_StatList.TYPE-Monster_StatList.STARTNUM] = new StatusInfo(MonsterType.NORMAL);
		monsterFeature = new StatusList();
	}
	
	public static HashMap<String, Integer> getStatHash()
	{
		if(!statHashSetted) setStatHash();
		return statHash;
	}
	
	public static void setStatHash()
	{
		statHashSetted = true;
		statHash.put("카운터", Monster_StatList.COUNTER); statHash.put("백어택", Monster_StatList.BACKATK);
		
		statHash.put("화속저", Monster_StatList.FIRE_RESIST); statHash.put("수속저", Monster_StatList.WATER_RESIST);
		statHash.put("명속저", Monster_StatList.LIGHT_RESIST); statHash.put("암속저", Monster_StatList.DARKNESS_RESIST);
		statHash.put("난이도", Monster_StatList.DIFFICULTY); statHash.put("체력", Monster_StatList.HP);
		statHash.put("물방", Monster_StatList.DEFENSIVE_PHY); statHash.put("물리방어력", Monster_StatList.DEFENSIVE_PHY);
		statHash.put("마방", Monster_StatList.DEFENSIVE_MAG); statHash.put("마법방어력", Monster_StatList.DEFENSIVE_MAG);
		statHash.put("레벨", Monster_StatList.LEVEL); statHash.put("타입", Monster_StatList.TYPE); statHash.put("방깍패널티", Monster_StatList.TYPE);
		statHash.put("레벨", Monster_StatList.DEFENCE_LIMIT);
	}
	
	public int getStat(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			return (int)((StatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).getStatToDouble();
		else throw new StatusTypeMismatch("Integer");
	}
	public int getStat(String stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat)) return getStat(getStatHash().get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public boolean getBool(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof BooleanInfo)
			return ((BooleanInfo)monstInfo[stat-Monster_StatList.STARTNUM]).getBooleanStat();
		else throw new StatusTypeMismatch("Boolean");
	}
	public boolean getBool(String stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat)) return getBool(getStatHash().get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public void setStat(int stat, int strength)	throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			((StatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).setInfo(strength);
		else throw new StatusTypeMismatch("Integer");
	}
	
	public void setBooleanStat(int stat, boolean bool)	throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof BooleanInfo)
			((BooleanInfo)monstInfo[stat-Monster_StatList.STARTNUM]).setBooleanStat(bool);
		else throw new StatusTypeMismatch("Boolean");
	}
	public void setStat(String stat, Object strength) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat)){
			int statNum = getStatHash().get(stat);
			if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof StatusInfo && strength instanceof Integer)
				setStat(getStatHash().get(stat), (int)strength);
			else if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof BooleanInfo && strength instanceof Boolean)
				setBooleanStat(getStatHash().get(stat), (boolean)strength);
			else throw new StatusTypeMismatch(strength.getClass().getName());
		}
		else throw new UndefinedStatusKey(stat);
	}
}
