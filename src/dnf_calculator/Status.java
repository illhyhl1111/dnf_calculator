package dnf_calculator;
//import java.util.HashMap;

interface StatList									// 스탯 종류에 붙는 고유한 식별번호
													// 솔직히 번호붙은게 개판이라 다시 정리해야함
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEM_DARKNESS=3;											// 화, 수, 명, 암속성
	int WEP_PHY=4; int  WEP_MAG=5; int  WEP_NODEF_PHY=6; int  WEP_NODEF_MAG=7; int WEP_IND=19; int WEP_IND_REFORGE=20; 		// 무기물공, 무기마공, 방무물공, 방무마공, 독공, 재련독공
	int DEF_DEC_FIXED_PHY=8; int  DEF_DEC_PERCENT_PHY=9; int DEF_DEC_FIXED_MAG=36; int DEF_DEC_PERCENT_MAG=37;				// 고정물방깍, 퍼센트물방깍, 고정마방깍, 퍼센트마방깍
	int DAM_INC=10; int  DAM_CRT=11; int  DAM_ADD=12; 																		// 증뎀, 크증뎀, 추뎀합 
	int STR=13; int INT=14; int STA=15; int WILL=16;																// 힘, 지능, 체력, 정신력
	int MAST_PHY=17; int MAST_MAG=18; int MAST_IND=21; int MAST_PHY_2=24; int MAST_MAG_2=25;						// 물공마스터리, 마공마스터리, 독공%증가, 물공마스터리(종류 2), 마공마스터리(종류 2)
	int STR_INC=22; int INT_INC=23;																					// 힘%증가, 지능%증가
	int DAM_ADD_FIRE=26; int DAM_ADD_WATER=27; int DAM_ADD_LIGHT=28; int DAM_ADD_DARKNESS=29;						// 화속추뎀, 수속추뎀, 명속추뎀, 암속추뎀 (높은속성추뎀 필요)
	int DAM_BUF=30;																									//투함포 이그니스 공대버프
	int CRT_PHY=31; int CRT_MAG=32; int CRT_LOW=33; int CRT_BUF=35; int CRT_BACK_PHY=38; int CRT_BACK_MAG=39;		// 물크, 마크, 크리저항감소, 백물크, 백마크
	int DAM_INC_BACK=40; int DAM_CRT_BACK=41; int DAM_ADD_BACK=42;													//(조건부) 백어택 증뎀, 백어택 크증뎀, 백어택 추뎀 
	int BUF_INC=43; int BUF_CRT=44;																					//(스킬) 증뎀버프, 크증뎀버프
	int DAM_SKILL = 34;																								// 스증뎀합
	
	public static final int STATNUM = 45;							// 총 스탯 개수
	public static final int ELEMENTNUM = 4;							// 총 속성 개수
}

public class Status {
	
	private AbstractStatusInfo[] statInfo;
	
	public Status()									// 모든 스탯 0으로 초기화(디폴트 장비)
	{
		statInfo = new AbstractStatusInfo[StatList.STATNUM];
		int i;
		for(i=0; i<StatList.ELEMENTNUM; i++)
			statInfo[i] = new ElementInfo(false, 0);
		
		for(; i<StatList.STATNUM; i++)
			statInfo[i] = new StatusInfo(0);
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
			((StatusInfo)publicInfo[stat]).str=strength;
		}
		
		public void setElementStat(int stat, int strength, boolean activated)	// 특정 속성값+속성부여여부 변경
		{
			if(StatList.ELEMENTNUM>stat){
				((StatusInfo)publicInfo[stat]).str=strength;
				ElementInfo temp = (ElementInfo)publicInfo[stat];
				temp.hasElement=activated;
			}
			else; //Make Error
		}
		
		public int getStat(int stat)
		{
			return ((StatusInfo)publicInfo[stat]).str;
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

class ElementInfo extends StatusInfo				// 속성정보 저장 class
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