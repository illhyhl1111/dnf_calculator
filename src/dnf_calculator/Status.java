package dnf_calculator;
//import java.util.HashMap;

interface StatusList
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEMT_DARKNESS=3;
	int ATK_PHY=4; int  ATK_MAG=5; int  ATK_NODEF_PHY=6; int  ATK_NODEF_MAG=7; 
	int DEF_DEC_FIXED=8; int  DEF_DEC_PERCENT=9; 
	int DAM_INC=10; int  DAM_CRT=11; int  DAM_ADD=12;
	int STR=13; int INTELL=14; int STA=15; int WILL=16;
	
	public static final int STATNUM = 17;
	public static final int ELEMENTNUM = 4;
}

public class Status {
	
	private StatusInfo[] statInfo;
	
	public Status()
	{
		statInfo = new StatusInfo[StatusList.STATNUM];
		int i;
		for(i=0; i<StatusList.ELEMENTNUM; i++)
			statInfo[i] = new ElementInfo(false, 0);
		
		for(; i<StatusList.STATNUM; i++)
			statInfo[i] = new StatusInfo(0);
	}
	
	public void setStatus(Status_Public stat)
	{
		for(int i=0; i<StatusList.STATNUM; i++)
			statInfo[i]=stat.publicInfo[i].getClone();
	}
	
	class Status_Public
	{
		public StatusInfo[] publicInfo;  
		
		public Status_Public()
		{
			publicInfo = new StatusInfo[StatusList.STATNUM];
			for(int i=0; i<StatusList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
		
		public void setStat(int stat, int strength)
		{
			publicInfo[stat].str=strength;
		}
		
		public void setElementStat(int stat, int strength, boolean activated)
		{
			if(StatusList.ELEMENTNUM>stat){
				publicInfo[stat].str=strength;
				ElementInfo temp = (ElementInfo)publicInfo[stat];
				temp.hasElement=activated;
			}
			else; //Make Error
		}
		
		public void renewStat()
		{
			for(int i=0; i<StatusList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
	}

}

class StatusInfo
{
	int str;
	public StatusInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	
	public StatusInfo getClone() {return new StatusInfo(str);}
}

class ElementInfo extends StatusInfo
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
	
	public StatusInfo getClone() {return new ElementInfo(hasElement, str);}
}