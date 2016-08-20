package dnf_calculator;
//import java.util.HashMap;

interface StatList									// 스탯 종류에 붙는 고유한 식별번호
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEM_DARKNESS=3;											// 화, 수, 명, 암속성
	public static final int ELEMENTNUM = 4;							// 총 속성 개수
	public static final int ELEMENTNUM_START = 0;							// 총 속성 개수
	///////////////////////////////////////
	
	int WEP_PHY=10; int  WEP_MAG=11; int  WEP_NODEF_PHY=12; int  WEP_NODEF_MAG=13; int WEP_IND=14; int WEP_IND_REFORGE=15; 		// 무기물공, 무기마공, 방무물공, 방무마공, 독공, 재련독공
	int DEF_DEC_FIXED_PHY=16; int  DEF_DEC_PERCENT_PHY_ITEM=17; int DEF_DEC_FIXED_MAG=18; int DEF_DEC_PERCENT_MAG_ITEM=19;		// 고정물방깍, (아이템)퍼센트물방깍, 고정마방깍, (아이템)퍼센트마방깍
	int STR=20; int INT=21; int STA=22; int WILL=23;																// 힘, 지능, 체력, 정신력
	int STR_INC=24; int INT_INC=25;																					// 힘%증가, 지능%증가
	int DAM_INC=26; int  DAM_CRT=27; int  DAM_ADD=28; 																// 증뎀, 크증뎀, 추뎀합
	int BUF_INC=29; int BUF_CRT=30;																					//(스킬) 증뎀버프, 크증뎀버프
	int DAM_SKILL = 31;																								// 스증뎀합
	int DAM_INC_BACK=32; int DAM_CRT_BACK=33; int DAM_ADD_BACK=34;													//(조건부) 백어택 증뎀, 백어택 크증뎀, 백어택 추뎀
	int DAM_ADD_FIRE=35; int DAM_ADD_WATER=36; int DAM_ADD_LIGHT=37; int DAM_ADD_DARKNESS=38;						// 화속추뎀, 수속추뎀, 명속추뎀, 암속추뎀 (높은속성추뎀 필요)
	int ELEM_FIRE_DEC=39; int ELEM_WATER_DEC=40; int ELEM_LIGHT_DEC=41; int ELEM_DARKNESS_DEC=42;					// 화속저, 수속저, 명속저, 암속저
	int DAM_BUF=43;																									// 투함포 이그니스 공대버프
	public static final int INTNUM = 34;						// 총 int형 스탯 개수
	public static final int INTNUM_START = 10;
	///////////////////////////////////////
	
	int DEF_DEC_PERCENT_PHY_SKILL=100; int DEF_DEC_PERCENT_MAG_SKILL=101;												//(스킬) 퍼센트물방깍, 퍼센트마방깍
	int CRT_PHY=102; int CRT_MAG=103; int CRT_LOW=104; int CRT_BUF=105; int CRT_BACK_PHY=106; int CRT_BACK_MAG=107;		// 물크, 마크, 크리저항감소, 백물크, 백마크
	int MAST_PHY=108; int MAST_MAG=109; int MAST_IND=110; int MAST_PHY_2=111; int MAST_MAG_2=112;						// 물공마스터리, 마공마스터리, 독공%증가, 물공마스터리(종류 2), 마공마스터리(종류 2)
	public static final int DOUBLENUM = 13;						// 총 double형 스탯 개수
	public static final int DOUBLENUM_START = 100;
	///////////////////////////////////////
	
	public static final int STATNUM = ELEMENTNUM+INTNUM+DOUBLENUM;;							// 총 스탯 개수
	
}

public class Status {
	
	private AbstractStatusInfo[] statInfo;
	
	public Status()									// 모든 스탯 0으로 초기화(디폴트 장비)
	{
		statInfo = new AbstractStatusInfo[StatList.STATNUM];
		int i;
		int j=0;									// 배열 index 숫자
		for(i=StatList.ELEMENTNUM_START; i<StatList.ELEMENTNUM; i++, j++)
			statInfo[j] = new ElementInfo(false, 0);
		
		for(i=StatList.INTNUM_START; i<StatList.INTNUM; i++, j++)
			statInfo[j] = new StatusInfo(0);
		
		for(i=StatList.DOUBLENUM_START; i<StatList.DOUBLENUM; i++, j++)
			statInfo[j] = new DoubleStatusInfo(0);
		
	}
	
	public void setStatus(PublicStatus stat)		// 스탯을 inner class값으로 변경
	{
		for(int i=0; i<StatList.STATNUM; i++)
			statInfo[i]=stat.publicInfo[i].getClone();
	}
	
	class PublicStatus								// private 참조를 위한 inner class. 모든 스탯 참조/변경 가능 
	{
		public AbstractStatusInfo[] publicInfo;  
		
		public PublicStatus()						// 모든 스탯 장비값으로 초기화(outer class 장비)
		{
			publicInfo = new StatusInfo[StatList.STATNUM];
			for(int i=0; i<StatList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
		
		public void setStat(int stat, int strength)	// 특정 스탯값 변경
		{
			if(publicInfo[stat] instanceof StatusInfo)
				((StatusInfo)publicInfo[stat]).str=strength;
			else return; //print error
		}
		
		public void setDoubleStat(int stat, double strength)	// 특정 스탯값 변경
		{
			if(publicInfo[stat] instanceof DoubleStatusInfo)
				((DoubleStatusInfo)publicInfo[stat]).str=strength;
			else return; //print error
		}
		
		public void setElementStat(int stat, int strength, boolean activated)	// 특정 속성값+속성부여여부 변경
		{
			if(publicInfo[stat] instanceof ElementInfo){
				((StatusInfo)publicInfo[stat]).str=strength;
				ElementInfo temp = (ElementInfo)publicInfo[stat];
				temp.hasElement=activated;
			}
			else; //Make Error
		}
		
		public double getStat(int stat)
		{
			return (double)(((StatusInfo)publicInfo[stat]).str);
		}
		
		public void renewStat()						// outer class값과 동기화 
		{
			for(int i=0; i<StatList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
	}

}

abstract class AbstractStatusInfo					// 스탯정보 저장 class
{
	abstract public AbstractStatusInfo getClone();
}

class StatusInfo extends AbstractStatusInfo			// int형 스탯정보 저장 class
{
	int str;										// private으로 바꿔야하지만 몰라 귀찮다 그냥 조심해야지
	public StatusInfo(int strength)
	{
		str=strength;
	}
	
	public void setInfo(int strength) { str=strength;}
	
	public StatusInfo getClone() {return new StatusInfo(str);}	// 복제
}

class DoubleStatusInfo extends AbstractStatusInfo
{
	double str;
	public DoubleStatusInfo(double strength)
	{
		str=strength;
	}
	
	public void setDoubleInfo(double strength) {str=strength;}
	
	public DoubleStatusInfo getClone() {return new DoubleStatusInfo(str);}
}

class ElementInfo extends StatusInfo				// 속성정보 저장 class
{
	boolean hasElement;
	
	public ElementInfo(boolean activated, int strength)
	{
		super(strength);
		hasElement=activated;
	}
	
	public void setElementInfo(boolean activated, int strength)
	{
		super.setInfo(strength);
		hasElement=activated;
	}
	
	public void setElementInfo(int strength)
	{
		super.setInfo(strength);
	}
	
	public StatusInfo getClone() {return new ElementInfo(hasElement, str);}
}