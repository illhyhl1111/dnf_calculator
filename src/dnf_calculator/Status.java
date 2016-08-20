package dnf_calculator;
//import java.util.HashMap;

interface StatList
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEMT_DARKNESS=3;
	int WEP_PHY=4; int  WEP_MAG=5; int  WEP_NODEF_PHY=6; int  WEP_NODEF_MAG=7; int WEP_IND=19; int WEP_IND_REFORGE=20; 
	int DEF_DEC_FIXED_PHY=8; int  DEF_DEC_PERCENT_PHY=9; int DEF_DEC_FIXED_MAG=36; int DEF_DEC_PERCENT_MAG=37;
	int DAM_INC=10; int  DAM_CRT=11; int  DAM_ADD=12; 
	int STR=13; int INTELL=14; int STA=15; int WILL=16;
	int MAST_PHY=17; int MAST_MAG=18; int MAST_IND=21; int MAST_PHY_2=24; int MAST_MAG_2=25;
	int STR_INC=22; int MAG_INC=23;
	int DAM_ADD_FIRE=26; int DAM_ADD_WATER=27; int DAM_ADD_LIGHT=28; int DAM_ADD_DARKNESS=29;
	int DAM_BUF=30; 
	int CRT_PHY=31; int CRT_MAG=32; int CRT_LOW_PHY=33; int CRT_LOW_MAG=34; int CRT_BUF=35; int CRT_BACK_PHY=38; int CRT_BACK_MAG=39;
	int DAM_INC_BACK=40; int DAM_CRT_BACK=41; int DAM_ADD_BACK=42;
	int BUF_INC=43; int BUF_CRT=44;
	
	public static final int STATNUM = 45;
	public static final int ELEMENTNUM = 4;
}

public class Status {
	
	private StatusInfo[] statInfo;
	
	public Status()
	{
		statInfo = new StatusInfo[StatList.STATNUM];
		int i;
		for(i=0; i<StatList.ELEMENTNUM; i++)
			statInfo[i] = new ElementInfo(false, 0);
		
		for(; i<StatList.STATNUM; i++)
			statInfo[i] = new StatusInfo(0);
	}
	
	public void setStatus(PublicStatus stat)
	{
		for(int i=0; i<StatList.STATNUM; i++)
			statInfo[i]=stat.publicInfo[i].getClone();
	}
	
	class PublicStatus
	{
		public StatusInfo[] publicInfo;  
		
		public PublicStatus()
		{
			publicInfo = new StatusInfo[StatList.STATNUM];
			for(int i=0; i<StatList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
		
		public void setStat(int stat, int strength)
		{
			publicInfo[stat].str=strength;
		}
		
		public void setElementStat(int stat, int strength, boolean activated)
		{
			if(StatList.ELEMENTNUM>stat){
				publicInfo[stat].str=strength;
				ElementInfo temp = (ElementInfo)publicInfo[stat];
				temp.hasElement=activated;
			}
			else; //Make Error
		}
		
		public int getStat(int stat)
		{
			return publicInfo[stat].str;
		}
		
		public void renewStat()
		{
			for(int i=0; i<StatList.STATNUM; i++)
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