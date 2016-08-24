package dnf_class;

import dnf_calculator.AbstractStatusInfo;
import dnf_calculator.BooleanInfo;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_calculator.Status;
import dnf_calculator.StatusInfo;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;

public class Monster {							// 피격자 class

	private AbstractStatusInfo[] monstInfo;
	public Item monsterFeature;
	
	public Monster(Status stat)								
	{
		monstInfo = new AbstractStatusInfo[Monster_StatList.STATNUM];
		int i;
		for(i=0; i<Monster_StatList.BOOLNUM; i++)
			monstInfo[i] = new BooleanInfo(false);
		
		for(; i<Monster_StatList.STATNUM; i++)
			monstInfo[i] = new StatusInfo(0);
		
		monstInfo[Monster_StatList.TYPE-Monster_StatList.STARTNUM] = new StatusInfo(MonsterType.NORMAL);
		monsterFeature = new Item();
	}
	
	public int getStat(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			return (int)((StatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).getStatToDouble();
		else throw new StatusTypeMismatch("Integer");
	}
	
	public boolean getBool(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof BooleanInfo)
			return ((BooleanInfo)monstInfo[stat-Monster_StatList.STARTNUM]).getBooleanStat();
		else throw new StatusTypeMismatch("Boolean");
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
}
