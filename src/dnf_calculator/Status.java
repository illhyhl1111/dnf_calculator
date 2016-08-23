package dnf_calculator;
import java.util.HashMap;

interface StatList									// 스탯 종류에 붙는 고유한 식별번호
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEM_DARKNESS=3;											// 화, 수, 명, 암속성
	
	int ELEMENTNUM_START = 0;
	int ELEMENTNUM = 4;														// 총 속성 개수
	int ELEMENTNUM_END = ELEMENTNUM_START+ELEMENTNUM-1;
	///////////////////////////////////////
	
	int INTNUM_START = ELEMENTNUM_END+1;
	
	int WEP_PHY=INTNUM_START+0; int WEP_MAG=INTNUM_START+1; int  WEP_NODEF_PHY=INTNUM_START+2; int WEP_NODEF_MAG=INTNUM_START+3;	// 무기물공, 무기마공, 방무물공, 방무마공
	int WEP_IND=INTNUM_START+4; int WEP_IND_REFORGE=INTNUM_START+5; 																//독공, 재련독공
	int DEF_DEC_FIXED_PHY=INTNUM_START+6; int  DEF_DEC_PERCENT_PHY_ITEM=INTNUM_START+7;												// 고정물방깍, (아이템)퍼센트물방깍,
	int DEF_DEC_FIXED_MAG=INTNUM_START+8; int DEF_DEC_PERCENT_MAG_ITEM=INTNUM_START+9;												// 고정마방깍, (아이템)퍼센트마방깍
	int STR=INTNUM_START+10; int INT=INTNUM_START+11; int STA=INTNUM_START+12; int WILL=INTNUM_START+13;							// 힘, 지능, 체력, 정신력
	int STR_INC=INTNUM_START+14; int INT_INC=INTNUM_START+15;																		// 힘%증가, 지능%증가
	int DAM_INC=INTNUM_START+16; int  DAM_CRT=INTNUM_START+17; int  DAM_ADD=INTNUM_START+18; 										// 증뎀, 크증뎀, 추뎀합
	int DAM_INC_BACK=INTNUM_START+19; int DAM_CRT_BACK=INTNUM_START+20; int DAM_ADD_BACK=INTNUM_START+21;							//(조건부) 백어택 증뎀, 백어택 크증뎀, 백어택 추뎀
	int DAM_ADD_FIRE=INTNUM_START+22; int DAM_ADD_WATER=INTNUM_START+23;
	int DAM_ADD_LIGHT=INTNUM_START+24; int DAM_ADD_DARKNESS=INTNUM_START+25;														// 화속추뎀, 수속추뎀, 명속추뎀, 암속추뎀 (높은속성추뎀 필요)
	int ELEM_FIRE_DEC=INTNUM_START+26; int ELEM_WATER_DEC=INTNUM_START+27;
	int ELEM_LIGHT_DEC=INTNUM_START+28; int ELEM_DARKNESS_DEC=INTNUM_START+29;														// 화속저, 수속저, 명속저, 암속저
	int DAM_BUF=INTNUM_START+30;																									// 투함포 이그니스 공대버프
	
	int INTNUM = 31;								// 총 int형 스탯 개수
	int INTNUM_END = INTNUM_START+INTNUM-1;
	///////////////////////////////////////
	
	int DOUBLENUM_START = INTNUM_END+1;
	
	int DAM_SKILL =DOUBLENUM_START+0;																				// 스증뎀합
	int DEF_DEC_PERCENT_PHY_SKILL=DOUBLENUM_START+1; int DEF_DEC_PERCENT_MAG_SKILL=DOUBLENUM_START+2;				//(스킬) 퍼센트물방깍, 퍼센트마방깍
	int CRT_PHY=DOUBLENUM_START+3; int CRT_MAG=DOUBLENUM_START+4; int CRT_LOW=DOUBLENUM_START+5;
	int CRT_BACK_PHY=DOUBLENUM_START+6; int CRT_BACK_MAG=DOUBLENUM_START+7;											// 물크, 마크, 크리저항감소, 백물크, 백마크
	int MAST_IND=DOUBLENUM_START+8; int MAST_PHY=DOUBLENUM_START+9; int MAST_MAG=DOUBLENUM_START+10;				// 물공마스터리, 마공마스터리, 독공%증가
	int MAST_PHY_2=DOUBLENUM_START+11; int MAST_MAG_2=DOUBLENUM_START+12;											//물공마스터리(종류 2), 마공마스터리(종류 2)
	int BUF_INC=DOUBLENUM_START+13; int BUF_CRT=DOUBLENUM_START+14;													//(스킬) 증뎀버프, 크증뎀버프
	
	int DOUBLENUM = 15;														// 총 double형 스탯 개수
	int DOUBLENUM_END = DOUBLENUM_START+DOUBLENUM-1;
	///////////////////////////////////////
	
	int STATNUM = ELEMENTNUM+INTNUM+DOUBLENUM;								// 총 스탯 개수
}

public class Status implements Cloneable {
	
	private AbstractStatusInfo[] statInfo;
	private static HashMap<String, Integer> statHash = new HashMap<String, Integer>();
	private static boolean statHashsetted = false;
	
	public static final String[] infoStatOrder = new String[] {
		"힘", "지능", "체력", "정신력", "마을물공", "마을마공", "독립공격", "물리크리티컬", "마법크리티컬",
		"화속성강화", "수속성강화", "명속성강화", "암속성강화"};
	public static final int infoStatNum=13;
	
	public static final String[] nonInfoStatOrder = new String[] {
		"무기물공합", "물리방무뎀",
		"무기마공합", "마법방무뎀", 
		"재련독공수치", "독공뻥",
		"힘 %증가", "지능 %증가",
		"물리마스터리", "물리마스터리2",
		"마법마스터리", "마법마스터리2",
		"증뎀", "크증뎀",
		"추뎀", "스증뎀",
		"증뎀버프", "크증뎀버프",
		"화속성부여", "수속성부여",
		"명속성부여", "암속성부여",
		"화속깍", "화추뎀",
		"수속깍", "수추뎀",
		"명속깍", "명추뎀",
		"암속깍", "암추뎀",
		"고정물방깍", "고정마방깍",
		"%물방깍_템", "%마방깍_템",
		"%물방깍_스킬", "%마방깍_스킬",
		"크리저항감소", "투함포항"
	};
	public static final int nonInfoStatNum=38;
	
	
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
	}
	
	public static HashMap<String, Integer> getStatHash()
	{
		if(!statHashsetted) setStatHash();
		return statHash;
	}
	
	public static void setStatHash()
	{
		statHash.put("화속성강화", StatList.ELEM_FIRE); statHash.put("화속성", StatList.ELEM_FIRE); statHash.put("화속", StatList.ELEM_FIRE); statHash.put("화", StatList.ELEM_FIRE);
		statHash.put("수속성강화", StatList.ELEM_WATER); statHash.put("수속성", StatList.ELEM_WATER); statHash.put("수속", StatList.ELEM_WATER); statHash.put("수", StatList.ELEM_WATER);
		statHash.put("명속성강화", StatList.ELEM_LIGHT); statHash.put("명속성", StatList.ELEM_LIGHT); statHash.put("명속", StatList.ELEM_LIGHT); statHash.put("명", StatList.ELEM_LIGHT);
		statHash.put("암속성강화", StatList.ELEM_DARKNESS); statHash.put("암속성", StatList.ELEM_DARKNESS); statHash.put("암속", StatList.ELEM_DARKNESS); statHash.put("암", StatList.ELEM_DARKNESS);
		
		statHash.put("물공", StatList.WEP_PHY); statHash.put("무기물공", StatList.WEP_PHY); statHash.put("무기물공합", StatList.WEP_PHY);
		statHash.put("마공", StatList.WEP_MAG); statHash.put("무기마공", StatList.WEP_MAG); statHash.put("무기마공합", StatList.WEP_MAG);
		statHash.put("물리방무뎀", StatList.WEP_NODEF_PHY); statHash.put("마법방무뎀", StatList.WEP_NODEF_MAG);
		statHash.put("물리방무", StatList.WEP_NODEF_PHY); statHash.put("마법방무", StatList.WEP_NODEF_MAG);
		statHash.put("독공", StatList.WEP_IND); statHash.put("독립공격", StatList.WEP_IND);
		statHash.put("재련독공", StatList.WEP_IND_REFORGE); statHash.put("재련독공수치", StatList.WEP_IND_REFORGE);
	
		statHash.put("고정물방깍", StatList.DEF_DEC_FIXED_PHY); statHash.put("고정마방깍", StatList.DEF_DEC_FIXED_MAG);
		statHash.put("퍼물방깍_템", StatList.DEF_DEC_PERCENT_PHY_ITEM); statHash.put("퍼마방깍_템", StatList.DEF_DEC_PERCENT_MAG_ITEM);
		statHash.put("%물방깍_템", StatList.DEF_DEC_PERCENT_PHY_ITEM); statHash.put("%마방깍_템", StatList.DEF_DEC_PERCENT_MAG_ITEM);
		
		statHash.put("힘", StatList.STR); statHash.put("지능", StatList.INT);
		statHash.put("체력", StatList.STA); statHash.put("정신력", StatList.WILL);
		statHash.put("힘뻥", StatList.STR_INC); statHash.put("지능뻥", StatList.INT_INC);
		statHash.put("힘 %증가", StatList.STR_INC); statHash.put("지능 %증가", StatList.INT_INC);
		
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
		statHash.put("%물방깍_스킬", StatList.DEF_DEC_PERCENT_PHY_SKILL); statHash.put("%마방깍_스킬", StatList.DEF_DEC_PERCENT_MAG_SKILL);
		statHash.put("물크", StatList.CRT_PHY); statHash.put("마크", StatList.CRT_MAG); statHash.put("크리저항", StatList.CRT_LOW); statHash.put("크리저항감소", StatList.CRT_LOW);
		statHash.put("물리크리티컬", StatList.CRT_PHY); statHash.put("마법크리티컬", StatList.CRT_MAG);
		statHash.put("백물크", StatList.CRT_BACK_PHY); statHash.put("백마크", StatList.CRT_BACK_MAG);
		statHash.put("물리마스터리", StatList.MAST_PHY); statHash.put("마법마스터리", StatList.MAST_MAG); statHash.put("독공뻥", StatList.MAST_IND);
		statHash.put("물리마스터리2", StatList.MAST_PHY_2); statHash.put("마법마스터리2", StatList.MAST_MAG_2);
		
		statHashsetted=true;
	}
	
	public void setStat(int stat, int strength)	throws StatusTypeMismatch
	{
		statInfo[stat].setInfo(strength);
	}
	public void setStat(String stat, int strength) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) setStat(statHash.get(stat), strength);
		else throw new UndefinedStatusKey(stat);
	}
	
	
	public void setDoubleStat(int stat, double strength) throws StatusTypeMismatch
	{
		statInfo[stat].setInfo(strength);
	}
	public void setDoubleStat(String stat, double strength) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) setDoubleStat(statHash.get(stat), strength);
		else throw new UndefinedStatusKey(stat);
	}
	
	
	public void setElementStat(int stat, int strength, boolean activated) throws StatusTypeMismatch	// 특정 속성값+속성부여여부 변경
	{
		if(statInfo[stat] instanceof ElementInfo){
			ElementInfo temp = (ElementInfo)statInfo[stat];
			temp.setElementInfo(activated, strength);
		}
		else throw new StatusTypeMismatch("Element");
	}
	public void setElementStat(int stat, boolean activated) throws StatusTypeMismatch	// 특정 속성부여여부 변경
	{
		statInfo[stat].setInfo(activated);
	}
	public void setElementStat(String stat, int strength, boolean activated) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) setElementStat(statHash.get(stat), strength, activated);
		else throw new UndefinedStatusKey(stat);
	}
	public void setElementStat(String stat, boolean activated) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) setElementStat(statHash.get(stat), activated);
		else throw new UndefinedStatusKey(stat);
	}
	
	
	public double getStat(int stat) throws StatusTypeMismatch
	{
		return statInfo[stat].getStatToDouble();
	}
	public double getStat(String stat) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) return getStat(statHash.get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public boolean getEnabled(int stat) throws StatusTypeMismatch
	{
		if(statInfo[stat] instanceof ElementInfo){
			ElementInfo temp = (ElementInfo)statInfo[stat];
			return temp.getElementEnabled();
		}
		else throw new StatusTypeMismatch("Element");
	}
	public boolean getEnabled(String stat) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(statHash.containsKey(stat)) return getEnabled(statHash.get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public void addStat(int statNum, AbstractStatusInfo stat2)
	{
		AbstractStatusInfo stat1 = statInfo[statNum];
		try{
			switch(statNum)
			{
				case StatList.ELEM_FIRE: case StatList.ELEM_WATER:									//속성항
				case StatList.ELEM_LIGHT: case StatList.ELEM_DARKNESS:
					stat1.setInfo(((ElementInfo)stat1).getElementEnabled() && ((ElementInfo)stat2).getElementEnabled());
					stat1.setInfo(stat1.getStatToDouble()+stat2.getStatToDouble());
					break;
					
				case StatList.DAM_INC: case StatList.DAM_CRT:										//중첩불가항
				case StatList.DAM_INC_BACK: case StatList.DAM_CRT_BACK:
					if(stat2.getStatToDouble()>stat1.getStatToDouble()) stat1.setInfo(stat2.getStatToDouble()); 
					break;
					
				case StatList.DAM_SKILL: case StatList.BUF_INC: case StatList.BUF_CRT:				//복리중첩항
					double temp1=100.0+stat1.getStatToDouble();
					double temp2=100.0+stat2.getStatToDouble();
					stat1.setInfo(temp1*temp2/100-100);
					break;
					
				default:																			//단리중첩항
					stat1.setInfo(stat1.getStatToDouble()+stat2.getStatToDouble());
			}
		}
		catch(StatusTypeMismatch e){
			e.printStackTrace();
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Status temp = (Status)super.clone();
		temp.statInfo = (AbstractStatusInfo[])this.statInfo.clone();
		for(int i=0; i<StatList.STATNUM; i++)
			statInfo[i] = (AbstractStatusInfo)this.statInfo[i].clone();
		return temp;
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
