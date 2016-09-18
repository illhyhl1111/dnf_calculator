package dnf_calculator;
import java.util.HashMap;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_infomation.GetDictionary;

public class Status implements Cloneable, java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4490242684644672237L;
	private AbstractStatusInfo[] statInfo;
	private LinkedList<AbstractStatusInfo> skillInfo;
	private static HashMap<String, Integer> statHash = new HashMap<String, Integer>();
	private static boolean statHashsetted = false;
	
	public static final String[] infoStatOrder = new String[] {
		"힘", "지능", "마을물공", "마을마공", "독립공격", "물리크리티컬", "마법크리티컬",
		"화속성강화", "수속성강화", "명속성강화", "암속성강화"};
	public static final int infoStatNum=11;
	
	public static final String[] nonInfoStatOrder = new String[] {
		"힘", "힘 %증가",
		"지능", "지능 %증가",
		"무기물공합", "물리방무뎀",
		"무기마공합", "마법방무뎀", 
		"재련독공수치", "독공뻥",
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
	public static final int nonInfoStatNum=nonInfoStatOrder.length;
	
	
	public Status()									// 모든 스탯 0으로 초기화(디폴트 장비)
	{
		statInfo = new AbstractStatusInfo[StatList.STATNUM];
		int i;
		for(i=0; i<=StatList.ELEMENTNUM_END; i++)
			statInfo[i] = new ElementInfo(false, 0);
		
		for(; i<=StatList.INTNUM_END; i++)
			statInfo[i] = new StatusInfo(0);
		
		for(; i<=StatList.DOUBLENUM_END; i++)
			statInfo[i] = new DoubleStatusInfo(0);
	
		skillInfo = new LinkedList<AbstractStatusInfo>();
	}
	
	public Status(JobList job, int level) throws ItemNotFoundedException
	{
		this();
		GetDictionary.charDictionary.getBasicStat(job, level).addListToStat(this);
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
		
		statHash.put("모속강", StatList.ELEM_ALL); statHash.put("모속", StatList.ELEM_ALL);
		
		statHash.put("스킬", StatList.SKILL); statHash.put("스킬레벨", StatList.SKILL);  
		statHash.put("스킬범위", StatList.SKILL_RANGE); 
		
		statHashsetted=true;
	}
	
	public void setStat(int stat, int strength)	throws StatusTypeMismatch
	{
		statInfo[stat].setInfo(strength);
	}
	public void setStat(String stat, int strength) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(!statHashsetted) setStatHash();
		if(statHash.containsKey(stat)) setStat(statHash.get(stat), strength);
		else throw new UndefinedStatusKey(stat);
	}
	
	
	public void setDoubleStat(int stat, double strength) throws StatusTypeMismatch
	{
		statInfo[stat].setInfo(strength);
	}
	public void setDoubleStat(String stat, double strength) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(!statHashsetted) setStatHash();
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
		if(!statHashsetted) setStatHash();
		if(statHash.containsKey(stat)) setElementStat(statHash.get(stat), strength, activated);
		else throw new UndefinedStatusKey(stat);
	}
	public void setElementStat(String stat, boolean activated) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(!statHashsetted) setStatHash();
		if(statHash.containsKey(stat)) setElementStat(statHash.get(stat), activated);
		else throw new UndefinedStatusKey(stat);
	}
	
	public void setSkillStat(String name, int skillNum, int skillInc)
	{
		skillInfo.add(new SkillStatusInfo(skillNum, skillInc, name));
	}
	public void setSkillRangeStat(int start, int end, int skillNum, boolean TP)
	{
		skillInfo.add(new SkillRangeStatusInfo(skillNum, start, end, TP));
	}
	
	public double getStat(int stat) throws StatusTypeMismatch
	{
		return statInfo[stat].getStatToDouble();
	}
	public double getStat(String stat) throws StatusTypeMismatch, UndefinedStatusKey
	{
		if(!statHashsetted) setStatHash();
		if(statHash.containsKey(stat)) return getStat(statHash.get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public LinkedList<AbstractStatusInfo> getSkillStatList() { return skillInfo;}
	
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
		if(!statHashsetted) setStatHash();
		if(statHash.containsKey(stat)) return getEnabled(statHash.get(stat));
		else throw new UndefinedStatusKey(stat);
	}
	
	public void addStat(int statNum, AbstractStatusInfo stat2)
	{
		try{
			AbstractStatusInfo stat1 = statInfo[statNum];
			
			try{
				switch(statNum)
				{
					case StatList.ELEM_FIRE: case StatList.ELEM_WATER:									//속성항
					case StatList.ELEM_LIGHT: case StatList.ELEM_DARKNESS:
						stat1.setInfo(((ElementInfo)stat1).getElementEnabled() || ((ElementInfo)stat2).getElementEnabled());
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
		
		catch(ArrayIndexOutOfBoundsException e)											//스킬레벨항
		{
			skillInfo.add(stat2);
		}
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		Status temp = (Status)super.clone();
		temp.statInfo = (AbstractStatusInfo[])this.statInfo.clone();
		for(int i=0; i<StatList.STATNUM; i++)
			statInfo[i] = (AbstractStatusInfo)this.statInfo[i].clone();
		temp.skillInfo = new LinkedList<AbstractStatusInfo>();
		for(AbstractStatusInfo s : this.skillInfo)
			temp.skillInfo.add(s);
		return temp;
	}
}
