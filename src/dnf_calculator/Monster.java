package dnf_calculator;

public abstract class Monster {							// 피격자 class

	public Status monster_status;

	public AbstractStatusInfo[] monstInfo;
	
	public Monster(Status stat)								
	{
		monster_status=stat;
		monstInfo = new StatusInfo[Monster_StatList.STATNUM];
		int i;
		for(i=0; i<Monster_StatList.BOOLNUM; i++)
			monstInfo[i] = new BooleanInfo(false);
		
		for(; i<Monster_StatList.STATNUM; i++)
			monstInfo[i] = new StatusInfo(0);
		
		monstInfo[Monster_StatList.TYPE-Monster_StatList.STARTNUM] = new StatusInfo(MonsterType.NORMAL);
	}
	
	public int getStat(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			return ((StatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).str;
		else throw new StatusTypeMismatch("Integer");
	}
	
	public boolean getBool(int stat) throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof BooleanInfo)
			return ((BooleanInfo)monstInfo[stat-Monster_StatList.STARTNUM]).bool;
		else throw new StatusTypeMismatch("Boolean");
	}
	
	public void setStat(int stat, int strength)	throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			((StatusInfo)monstInfo[stat-Monster_StatList.STARTNUM]).str=strength;
		else throw new StatusTypeMismatch("Integer");
	}
	
	public void setBooleanStat(int stat, boolean bool)	throws StatusTypeMismatch
	{
		if(monstInfo[stat-Monster_StatList.STARTNUM] instanceof StatusInfo)
			((BooleanInfo)monstInfo[stat-Monster_StatList.STARTNUM]).bool=bool;
		else throw new StatusTypeMismatch("Boolean");
	}
}

interface DefenceIgnorePenalty
{
	int NORMAL=0; int EXPERT=10; int MASTER=20; int KING=40; int SLAYER=60; int ANTON_RAID=33; int ANTON_NOR=33;	// 난이도에 따른 방무패널티
}

interface MonsterType
{
	int NORMAL=100; int NAMED=75; int BOSS=50;											// 몹 등급에 따른 아이템퍼방깍 패널티 
}

interface Monster_StatList
{
	int COUNTER=1000; int BACKATK=101;																		//카운터여부, 백어택여부
	int FIRE_RESIST=1002; int WATER_RESIST=1003; int LIGHT_RESIST=1004; int DARKNESS_RESIST=1005;			// 화속저, 수속저, 명속저, 암속저
	int DIFFICULTY=1006; int HP=1007; 																		// 난이도, 체력, 
	int DEFENSIVE_PHY=1008; int DEFENSIVE_MAG=1009;															// 물방, 마방
	int LEVEL=1010;	int TYPE=1011;																			// 몹 레벨, 등급
	
	int STARTNUM=1000; int STATNUM=12;	int BOOLNUM=2;		//StatList와 겹치지 않는 시작번호, 스탯개수, bool형 스탯 개수
}

class BooleanInfo extends AbstractStatusInfo			// boolean형 스탯정보 저장 class
{
	boolean bool;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public BooleanInfo(boolean b)
	{
		bool=b;
	}
	
	public void setInfo(boolean b) { bool=b;}
	
	public BooleanInfo getClone() {return new BooleanInfo(bool);}	// 복제
}