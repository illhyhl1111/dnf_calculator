package dnf_calculator;
import java.util.HashMap;

interface StatList									// 스탯 종류에 붙는 고유한 식별번호
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEM_DARKNESS=3;											// 화, 수, 명, 암속성
	public static final int ELEMENTNUM = 4;							// 총 속성 개수
	public static final int ELEMENTNUM_START = 1;					
	///////////////////////////////////////
	
	int WEP_PHY=4; int  WEP_MAG=5; int  WEP_NODEF_PHY=6; int  WEP_NODEF_MAG=7; int WEP_IND=8; int WEP_IND_REFORGE=9; 		// 무기물공, 무기마공, 방무물공, 방무마공, 독공, 재련독공
	int DEF_DEC_FIXED_PHY=10; int  DEF_DEC_PERCENT_PHY_ITEM=11; int DEF_DEC_FIXED_MAG=12; int DEF_DEC_PERCENT_MAG_ITEM=13;		// 고정물방깍, (아이템)퍼센트물방깍, 고정마방깍, (아이템)퍼센트마방깍
	int STR=14; int INT=15; int STA=16; int WILL=17;																// 힘, 지능, 체력, 정신력
	int STR_INC=18; int INT_INC=19;																					// 힘%증가, 지능%증가
	int DAM_INC=20; int  DAM_CRT=21; int  DAM_ADD=22; 																// 증뎀, 크증뎀, 추뎀합
	int DAM_INC_BACK=23; int DAM_CRT_BACK=24; int DAM_ADD_BACK=25;													//(조건부) 백어택 증뎀, 백어택 크증뎀, 백어택 추뎀
	int DAM_ADD_FIRE=26; int DAM_ADD_WATER=27; int DAM_ADD_LIGHT=28; int DAM_ADD_DARKNESS=29;						// 화속추뎀, 수속추뎀, 명속추뎀, 암속추뎀 (높은속성추뎀 필요)
	int ELEM_FIRE_DEC=30; int ELEM_WATER_DEC=31; int ELEM_LIGHT_DEC=32; int ELEM_DARKNESS_DEC=33;					// 화속저, 수속저, 명속저, 암속저
	int DAM_BUF=34;																									// 투함포 이그니스 공대버프
	public static final int INTNUM = 31;						// 총 int형 스탯 개수
	public static final int INTNUM_START = 10;
	///////////////////////////////////////
	
	int DAM_SKILL = 35;																								// 스증뎀합
	int DEF_DEC_PERCENT_PHY_SKILL=36; int DEF_DEC_PERCENT_MAG_SKILL=37;												//(스킬) 퍼센트물방깍, 퍼센트마방깍
	int CRT_PHY=38; int CRT_MAG=39; int CRT_LOW=40; int CRT_BACK_PHY=41; int CRT_BACK_MAG=42;		// 물크, 마크, 크리저항감소, 백물크, 백마크
	int MAST_IND=43; int MAST_PHY=44; int MAST_MAG=45; int MAST_PHY_2=46; int MAST_MAG_2=47;						// 물공마스터리, 마공마스터리, 독공%증가, 물공마스터리(종류 2), 마공마스터리(종류 2)
	int BUF_INC=48; int BUF_CRT=49;																					//(스킬) 증뎀버프, 크증뎀버프
	public static final int DOUBLENUM = 15;						// 총 double형 스탯 개수
	public static final int DOUBLENUM_START = 100;
	///////////////////////////////////////
	
	public static final int STATNUM = ELEMENTNUM+INTNUM+DOUBLENUM;;							// 총 스탯 개수
	
}

public class Status {
	
	private AbstractStatusInfo[] statInfo;
	public static HashMap<String, Integer> statHash = new HashMap<String, Integer>();
	
	public Status()									// 모든 스탯 0으로 초기화(디폴트 장비)
	{
		statInfo = new AbstractStatusInfo[StatList.STATNUM];
		int i;
		for(i=0; i<StatList.ELEMENTNUM; i++)
			statInfo[i] = new ElementInfo(false, 0);
		
		for(; i<StatList.ELEMENTNUM+StatList.INTNUM; i++)
			statInfo[i] = new StatusInfo(0);
		
		for(; i<StatList.STATNUM; i++)
			statInfo[i] = new DoubleStatusInfo(0);
		setStatHash();
	}
	
	public static void setStatHash()
	{
		statHash.put("화속성", StatList.ELEM_FIRE); statHash.put("화속", StatList.ELEM_FIRE); statHash.put("화", StatList.ELEM_FIRE);
		statHash.put("수속성", StatList.ELEM_WATER); statHash.put("수속", StatList.ELEM_WATER); statHash.put("수", StatList.ELEM_WATER);
		statHash.put("명속성", StatList.ELEM_LIGHT); statHash.put("명속", StatList.ELEM_LIGHT); statHash.put("명", StatList.ELEM_LIGHT);
		statHash.put("암속성", StatList.ELEM_DARKNESS); statHash.put("암속", StatList.ELEM_DARKNESS); statHash.put("암", StatList.ELEM_DARKNESS);
		
		statHash.put("물공", StatList.WEP_PHY); statHash.put("무기물공", StatList.WEP_PHY);
		statHash.put("마공", StatList.WEP_MAG); statHash.put("무기마공", StatList.WEP_MAG);
		statHash.put("물리방무뎀", StatList.WEP_NODEF_PHY); statHash.put("마법방무뎀", StatList.WEP_NODEF_MAG);
		statHash.put("물리방무", StatList.WEP_NODEF_PHY); statHash.put("마법방무", StatList.WEP_NODEF_MAG);
		statHash.put("독공", StatList.WEP_IND); statHash.put("재련독공", StatList.WEP_IND_REFORGE);
	
		statHash.put("고정물방깍", StatList.DEF_DEC_FIXED_PHY); statHash.put("고정마방깍", StatList.DEF_DEC_FIXED_MAG);
		statHash.put("퍼물방깍_템", StatList.DEF_DEC_PERCENT_PHY_ITEM); statHash.put("퍼마방깍_템", StatList.DEF_DEC_PERCENT_MAG_ITEM);
		
		statHash.put("힘", StatList.STR); statHash.put("지능", StatList.INT);
		statHash.put("체력", StatList.STA); statHash.put("정신력", StatList.WILL);
		statHash.put("힘뻥", StatList.STR_INC); statHash.put("지능뻥", StatList.INT_INC);
		
		statHash.put("증뎀", StatList.DAM_INC); statHash.put("크증뎀", StatList.DAM_CRT); statHash.put("추뎀", StatList.DAM_ADD);
		statHash.put("증뎀버프", StatList.BUF_INC); statHash.put("크증버프", StatList.BUF_CRT); statHash.put("크증뎀버프", StatList.BUF_CRT);
		statHash.put("스증", StatList.DAM_SKILL); statHash.put("스증뎀", StatList.DAM_SKILL);
		
		statHash.put("백증뎀", StatList.DAM_INC_BACK); statHash.put("백크증", StatList.DAM_CRT_BACK); statHash.put("백추뎀", StatList.DAM_ADD_BACK);
		statHash.put("화속추", StatList.DAM_ADD_FIRE); statHash.put("수속추", StatList.DAM_ADD_WATER);
		statHash.put("명속추", StatList.DAM_ADD_LIGHT); statHash.put("암속추", StatList.DAM_ADD_DARKNESS);
		statHash.put("화추뎀", StatList.DAM_ADD_FIRE); statHash.put("수추뎀", StatList.DAM_ADD_WATER);
		statHash.put("명추뎀", StatList.DAM_ADD_LIGHT); statHash.put("암추뎀", StatList.DAM_ADD_DARKNESS);
		
		statHash.put("화속깍", StatList.ELEM_FIRE_DEC); statHash.put("수속깍", StatList.ELEM_WATER_DEC);
		statHash.put("명속깍", StatList.ELEM_LIGHT_DEC); statHash.put("암속깍", StatList.ELEM_DARKNESS_DEC);
		statHash.put("버프식증뎀", StatList.DAM_BUF); statHash.put("투함포항", StatList.DAM_BUF);
		
		statHash.put("퍼물방깍_스킬", StatList.DEF_DEC_PERCENT_PHY_SKILL); statHash.put("퍼마방깍_스킬", StatList.DEF_DEC_PERCENT_MAG_SKILL);
		statHash.put("물크", StatList.CRT_PHY); statHash.put("마크", StatList.CRT_MAG); statHash.put("크리저항", StatList.CRT_LOW);
		statHash.put("백물크", StatList.CRT_BACK_PHY); statHash.put("백마크", StatList.CRT_BACK_MAG);
		statHash.put("물공마스터리", StatList.MAST_PHY); statHash.put("마공마스터리", StatList.MAST_MAG); statHash.put("독공뻥", StatList.MAST_IND);
		statHash.put("물공마스터리2", StatList.MAST_PHY_2); statHash.put("마공마스터리2", StatList.MAST_MAG_2);
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
			publicInfo = new AbstractStatusInfo[StatList.STATNUM];
			for(int i=0; i<StatList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
		
		public void setStat(int stat, int strength)	throws StatusTypeMismatch
		{
			if(publicInfo[stat] instanceof StatusInfo || publicInfo[stat] instanceof ElementInfo)
				((StatusInfo)publicInfo[stat]).str=strength;
			else throw new StatusTypeMismatch("Integer");
		}
		public void setStat(String stat, int strength) throws StatusTypeMismatch, UndefinedStatusKey
		{
			if(statHash.containsKey(stat)) setStat(statHash.get(stat), strength);
			else throw new UndefinedStatusKey(stat);
		}
		
		
		public void setDoubleStat(int stat, double strength) throws StatusTypeMismatch
		{
			if(publicInfo[stat] instanceof DoubleStatusInfo)
				((DoubleStatusInfo)publicInfo[stat]).str=strength;
			else throw new StatusTypeMismatch("Double");
		}
		public void setDoubleStat(String stat, double strength) throws StatusTypeMismatch, UndefinedStatusKey
		{
			if(statHash.containsKey(stat)) setDoubleStat(statHash.get(stat), strength);
			else throw new UndefinedStatusKey(stat);
		}
		
		
		public void setElementStat(int stat, int strength, boolean activated) throws StatusTypeMismatch	// 특정 속성값+속성부여여부 변경
		{
			if(publicInfo[stat] instanceof ElementInfo){
				((StatusInfo)publicInfo[stat]).str=strength;
				ElementInfo temp = (ElementInfo)publicInfo[stat];
				temp.hasElement=activated;
			}
			else throw new StatusTypeMismatch("Element");
		}
		public void setElementStat(String stat, int strength, boolean activated) throws StatusTypeMismatch, UndefinedStatusKey
		{
			if(statHash.containsKey(stat)) setElementStat(statHash.get(stat), strength, activated);
			else throw new UndefinedStatusKey(stat);
		}
		
		
		public double getStat(int stat) throws StatusTypeMismatch
		{
			return publicInfo[stat].getStatToDouble();
		}
		
		public void renewStat()						// outer class값과 동기화 
		{
			for(int i=0; i<StatList.STATNUM; i++)
				publicInfo[i] = statInfo[i].getClone();
		}
	}
}

@SuppressWarnings("serial")
class StatusTypeMismatch extends Exception
{
	public StatusTypeMismatch(String targetType)
	{
		super("ERROR : Status Type Mismatch - Target Type : "+targetType);
	}
}

@SuppressWarnings("serial")
class UndefinedStatusKey extends Exception
{
	public UndefinedStatusKey(String key)
	{
		super("ERROR : Undefined Input for Status Key - "+key);
	}
}


abstract class AbstractStatusInfo 				// 스탯정보 저장 class
{
	abstract public AbstractStatusInfo getClone();
	abstract public double getStatToDouble() throws StatusTypeMismatch;
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
	public double getStatToDouble() {return (double)str;}
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
	public double getStatToDouble() {return str;}
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
	public double getStatToDouble() {return super.getStatToDouble();}
}