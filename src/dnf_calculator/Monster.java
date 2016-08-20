package dnf_calculator;

public abstract class Monster {
	public int defensive;
	public int fire_resist;
	public int water_resist;
	public int light_resist;
	public int darkness_resist;
	public int HP;
	public int difficulty;
	public Status mosnter_status;
	public boolean counter;
	public boolean backattack;
	
	public StatusInfo[] monstInfo;
	
	public Monster()
	{
		monstInfo = new StatusInfo[StatList.STATNUM];
		for(int i=Monster_StatList.STARTNUM; i<Monster_StatList.ELEMENTNUM; i++)
			monstInfo[i] = new StatusInfo(0);
	}
	
	public int getStat(int stat)
	{
		return monstInfo[stat].str;
	}
}

interface DefenceIgnorePenalty
{
	int NORMAL=0; int EXPERT=10; int MASTER=20; int KING=40; int SLAYER=60; int ANTON_RAID=33; int ANTON_NOR=33;
}

interface Monster_StatList
{
	int DEFENSIVE=10000; int FIRE_RESIST=10001; int WATER_RESIST=10002; int LIGHT_RESIST=10003; int DARKNESS_RESIST=10004;
	int DIFFICULTY=10005; int HP=10006; int COUNTER=10007;
	
	int STARTNUM=10000; int ELEMENTNUM=8;
}