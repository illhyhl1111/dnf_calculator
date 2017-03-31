package dnf_class;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import dnf_calculator.AbstractStatusInfo;
import dnf_calculator.BooleanInfo;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.LongStatusInfo;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.StatusInfo;
import dnf_calculator.StatusList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class Monster extends IconObject implements java.io.Serializable{							/**
	 * 
	 */
	private static final long serialVersionUID = -207946696464873997L;

	private AbstractStatusInfo[] monstInfo;
	public final HashMap<MonsterOption, StatusList> monsterFeature;
	private StatusList additionalStatList;
	private static HashMap<String, Integer> statHash = new HashMap<String, Integer>(); 
	private static boolean statHashSetted = false;
	private MonsterOption monsterOption;
	public LinkedList<String> explanation;						//설명
	
	public Monster(String name)								
	{
		super();
		this.setName(name);
		this.setIcon("image\\Monster\\"+name+".png");
		monstInfo = new AbstractStatusInfo[Monster_StatList.STATNUM];
		int i;
		for(i=0; i<Monster_StatList.BOOLNUM; i++)
			monstInfo[i] = new BooleanInfo(false);
		
		for(; i<Monster_StatList.STATNUM-Monster_StatList.DOUBLENUM-Monster_StatList.LONGNUM; i++)
			monstInfo[i] = new StatusInfo(0);
		
		for(; i<Monster_StatList.STATNUM-Monster_StatList.LONGNUM; i++)
			monstInfo[i] = new DoubleStatusInfo(0.0);
		
		for(; i<Monster_StatList.STATNUM; i++)
			monstInfo[i] = new LongStatusInfo(0);
		
		monstInfo[Monster_StatList.TYPE-Monster_StatList.STARTNUM] = new StatusInfo(MonsterType.NORMAL);
		monsterFeature = new HashMap<MonsterOption, StatusList>();
		additionalStatList = new StatusList();
		explanation = new LinkedList<String>();
		monsterOption = new MonsterOption(this);
	}
	public Monster()								
	{
		super();
		monstInfo = new AbstractStatusInfo[Monster_StatList.STATNUM];
		int i;
		for(i=0; i<Monster_StatList.BOOLNUM; i++)
			monstInfo[i] = new BooleanInfo(false);
		
		for(; i<Monster_StatList.STATNUM-Monster_StatList.DOUBLENUM-Monster_StatList.LONGNUM; i++)
			monstInfo[i] = new StatusInfo(0);
		
		for(; i<Monster_StatList.STATNUM-Monster_StatList.LONGNUM; i++)
			monstInfo[i] = new DoubleStatusInfo(0.0);
		
		for(; i<Monster_StatList.STATNUM; i++)
			monstInfo[i] = new LongStatusInfo(0);
		
		monstInfo[Monster_StatList.TYPE-Monster_StatList.STARTNUM] = new StatusInfo(MonsterType.NORMAL);
		monsterFeature = new HashMap<MonsterOption, StatusList>();
		additionalStatList = new StatusList();
		explanation = new LinkedList<String>();
		monsterOption = new MonsterOption(this);
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
		statHash.put("방깍제한", Monster_StatList.DEFENCE_LIMIT);
	}
	
	public void setSubMonster(MonsterOption monsterOption)
	{
		additionalStatList = monsterFeature.get(monsterOption);
		if(additionalStatList==null){
			additionalStatList=new StatusList();
			this.monsterOption = new MonsterOption(this);
		}
		else this.monsterOption = monsterOption; 
	}
	public void setSubMonster(String name)
	{
		MonsterOption temp=null;
		for(Entry<MonsterOption, StatusList> entry : monsterFeature.entrySet()){
			if(entry.getKey().getName().equals(name)){
				temp=entry.getKey();
				additionalStatList = entry.getValue();
			}
			break;
		}
		
		if(temp==null){
			additionalStatList=new StatusList();
			this.monsterOption = new MonsterOption(this);
			return;
		}
		if(additionalStatList==null) additionalStatList=new StatusList();
		this.monsterOption = temp; 
	}
	
	public StatusList getAdditionalStatList() {
		if(additionalStatList==null) return new StatusList();
		return additionalStatList;
	}
	
	public MonsterOption getMonsterOption() {return monsterOption;}
	
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
	
	public double getDoubleStat(int stat) throws StatusTypeMismatch
	{
		return monstInfo[stat-Monster_StatList.STARTNUM].getStatToDouble();
	}
	public double getDoubleStat(String stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat))
			return monstInfo[getStatHash().get(stat)-Monster_StatList.STARTNUM].getStatToDouble();
		else throw new UndefinedStatusKey(stat);
	}
	
	public long getLongStat(int stat) throws StatusTypeMismatch
	{
		return (long) monstInfo[stat-Monster_StatList.STARTNUM].getStatToDouble();
	}
	public long getLongStat(String stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat))
			return (long) monstInfo[getStatHash().get(stat)-Monster_StatList.STARTNUM].getStatToDouble();
		else throw new UndefinedStatusKey(stat);
	}
	
	public boolean getCounter(boolean isHoldingSkill) throws StatusTypeMismatch
	{
		if(isHoldingSkill) return false;
		return ((BooleanInfo)monstInfo[Monster_StatList.COUNTER-Monster_StatList.STARTNUM]).getBooleanStat();
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
	
	public void setDoubleStat(int stat, double strength) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof DoubleStatusInfo)
			((DoubleStatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).setInfo(strength);
		else throw new StatusTypeMismatch("Double");
	}
	
	public void setLongStat(int stat, long strength) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof LongStatusInfo)
			((LongStatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).setInfo(strength);
		else throw new StatusTypeMismatch("Long");
	}
	public void setLongStat(int stat, int strength) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof LongStatusInfo)
			((LongStatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).setInfo(strength);
		else throw new StatusTypeMismatch("Long");
	}
	
	public void setStat(String stat, Object strength) throws UndefinedStatusKey, StatusTypeMismatch
	{
		if(getStatHash().containsKey(stat)){
			int statNum = getStatHash().get(stat);
			if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof StatusInfo && strength instanceof Integer)
				setStat(getStatHash().get(stat), (int)strength);
			else if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof BooleanInfo && strength instanceof Boolean)
				setBooleanStat(getStatHash().get(stat), (boolean)strength);
			else if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof DoubleStatusInfo && strength instanceof Double)
				setDoubleStat(getStatHash().get(stat), (double)strength);
			else if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof LongStatusInfo && strength instanceof Long)
				setLongStat(getStatHash().get(stat), (long)strength);
			else if(monstInfo[statNum-Monster_StatList.STARTNUM] instanceof LongStatusInfo && strength instanceof Integer)
				setLongStat(getStatHash().get(stat), (int)strength);
			else throw new StatusTypeMismatch(strength.getClass().getName());
		}
		else throw new UndefinedStatusKey(stat);
	}
}
