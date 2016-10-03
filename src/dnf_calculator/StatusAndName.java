package dnf_calculator;

import java.util.HashMap;

import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

@SuppressWarnings("serial")
public class StatusAndName implements java.io.Serializable, Cloneable
{
	public int name;
	public AbstractStatusInfo stat;
	public boolean changeable;
	public boolean enableable;
	public boolean enabled;
	private static HashMap<Integer, String> reverseHash = new HashMap<Integer, String>();
	private static boolean reverseHashsetted = false;
	
	public StatusAndName(int name, AbstractStatusInfo stat, boolean changeable, boolean enableable)
	{
		this.name=name;
		this.stat=stat;
		this.changeable=changeable;
		this.enableable=enableable;
		enabled=true;
	}
	public StatusAndName(String name, AbstractStatusInfo stat, boolean changeable, boolean enableable) throws UndefinedStatusKey
	{
		this.stat=stat;
		this.changeable=changeable;
		this.enableable=enableable;
		enabled=true;
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
	}
	public StatusAndName(int name, AbstractStatusInfo stat)
	{
		this(name, stat, false, false);
	}
	public StatusAndName(String name, AbstractStatusInfo stat) throws UndefinedStatusKey
	{
		this(name, stat, false, false);
	}
	public StatusAndName(int name, AbstractStatusInfo stat, boolean changeable)
	{
		this(name, stat, changeable, false);
	}
	public StatusAndName(String name, AbstractStatusInfo stat, boolean changeable) throws UndefinedStatusKey
	{
		this(name, stat, changeable, false);
	}
	
	public StatusAndName(String name, int stat, boolean changeable, boolean enableable) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this.changeable=changeable;
		this.enableable=enableable;
		enabled=true;
		
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
		
		if(this.name<=StatList.ELEMENTNUM_END) this.stat=new ElementInfo(stat);
		else if(this.name<=StatList.INTNUM_END) this.stat=new StatusInfo(stat);
		else if(this.name<=StatList.DOUBLENUM_END) this.stat=new DoubleStatusInfo(stat);
		else throw new StatusTypeMismatch("int");
	}
	public StatusAndName(String name, double stat, boolean changeable, boolean enableable) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this.changeable=changeable;
		this.enableable=enableable;
		enabled=true;
		
		if(Status.getStatHash().containsKey(name)) this.name = Status.getStatHash().get(name);
		else throw new UndefinedStatusKey(name);
		
		if(StatList.DOUBLENUM_START<=this.name && this.name<=StatList.DOUBLENUM_END) this.stat=new DoubleStatusInfo(stat);
		else if(StatList.INTNUM_START<=this.name && this.name<=StatList.INTNUM_END) this.stat=new StatusInfo((int) (stat+0.00001));
		else throw new StatusTypeMismatch("double");
	}
	public StatusAndName(String name, int stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this(name, stat, false, false);
	}
	public StatusAndName(String name, double stat) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this(name, stat, false, false);
	}
	public StatusAndName(String name, int stat, boolean changeable) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this(name, stat, changeable, false);
	}
	public StatusAndName(String name, double stat, boolean changeable) throws UndefinedStatusKey, StatusTypeMismatch
	{
		this(name, stat, changeable, false);
	}
	
	public static HashMap<Integer, String> getStatHash()
	{
		if(!reverseHashsetted) setStatHash();
		return reverseHash;
	}
	
	public static void setStatHash()
	{
		reverseHash.put(StatList.ELEM_FIRE, "화속성 강화 +"); reverseHash.put(StatList.ELEM_WATER, "수속성 강화 +");
		reverseHash.put(StatList.ELEM_LIGHT, "명속성 강화 +"); reverseHash.put(StatList.ELEM_DARKNESS, "암속성 강화 +");
		
		reverseHash.put(StatList.WEP_PHY, "물리 공격력 +"); reverseHash.put(StatList.WEP_MAG, "마법 공격력 +");
		reverseHash.put(StatList.WEP_NODEF_PHY, " 방어무시 물리 공격력 +"); reverseHash.put(StatList.WEP_NODEF_MAG, " 방어무시 마법 공격력 +");
		reverseHash.put(StatList.WEP_IND, "독립 공격력 +"); reverseHash.put(StatList.WEP_IND_REFORGE, " 독립 공격력(재련) +");
		
		reverseHash.put(StatList.DEF_DEC_FIXED_PHY, "적 물리방어력(고정) -"); reverseHash.put(StatList.DEF_DEC_FIXED_MAG, "적 마법방어력(고정) -");
		reverseHash.put(StatList.DEF_DEC_PERCENT_MAG_ITEM, "적 마법방어력(%) -"); reverseHash.put(StatList.DEF_DEC_PERCENT_PHY_ITEM, "적 물리방어력(%) -");
		
		reverseHash.put(StatList.STR, "힘 +"); reverseHash.put(StatList.INT, "지능 +");
		reverseHash.put(StatList.STA, "체력 +"); reverseHash.put(StatList.WILL, "정신력 +");
		reverseHash.put(StatList.STR_INC, "힘 %증가 +"); reverseHash.put(StatList.INT_INC, "지능 %증가 +");
		
		reverseHash.put(StatList.DAM_INC, "데미지 증가(%) +"); reverseHash.put(StatList.DAM_CRT, "크리티컬 데미지 증가(%) +"); reverseHash.put(StatList.DAM_ADD, "추가 데미지(%) +");
		reverseHash.put(StatList.DAM_INC_BACK, "백어택시 데미지 증가(%) +"); reverseHash.put(StatList.DAM_CRT_BACK, "백어택시 크리티컬 데미지 증가(%) +"); reverseHash.put(StatList.DAM_ADD_BACK, "백어택시 추가 데미지(%) +");
		reverseHash.put(StatList.DAM_ADD_FIRE, "화속성 추가 데미지(%) +"); reverseHash.put(StatList.DAM_ADD_WATER, "수속성 추가 데미지(%) +");
		reverseHash.put(StatList.DAM_ADD_LIGHT, "명속성 추가 데미지(%) +"); reverseHash.put(StatList.DAM_ADD_DARKNESS, "암속성 추가 데미지(%) +");
		
		reverseHash.put(StatList.ELEM_FIRE_DEC, "적 화속성 저항 -"); reverseHash.put(StatList.ELEM_WATER_DEC, "적 수속성 저항 -");
		reverseHash.put(StatList.ELEM_LIGHT_DEC, "적 명속성 저항 -"); reverseHash.put(StatList.ELEM_DARKNESS_DEC, "적 암속성 저항 -");
		reverseHash.put(StatList.ELEM_ALL, "모든 속성 강화 +"); reverseHash.put(StatList.DAM_BUF, "데미지 증가 : ");
		
		reverseHash.put(StatList.DAM_SKILL, "스킬 데미지 증가(%) +");
		reverseHash.put(StatList.DEF_DEC_PERCENT_PHY_SKILL, "적 물리방어력(%) -"); reverseHash.put(StatList.DEF_DEC_PERCENT_MAG_SKILL, "적 마법방어력(%) -");
		reverseHash.put(StatList.CRT_PHY, "물리 크리티컬 히트 +"); reverseHash.put(StatList.CRT_MAG, "마법 크리티컬 히트 +"); reverseHash.put(StatList.CRT_LOW, "적 크리티컬 저항 -");
		reverseHash.put(StatList.CRT_BACK_PHY, "백어택 물리 크리티컬 히트 +"); reverseHash.put(StatList.CRT_BACK_MAG, "백어택 마법 크리티컬 히트 +");
		
		reverseHash.put(StatList.MAST_IND, "독립 공격력 %증가 +"); reverseHash.put(StatList.MAST_PHY, "물리 공격력 %증가 +"); reverseHash.put(StatList.MAST_MAG, "마법 공격력 %증가 +");
		reverseHash.put(StatList.MAST_PHY_2, "물리 공격력 %증가(2) +"); reverseHash.put(StatList.MAST_MAG_2, "마법 공격력 %증가(2) +");
		reverseHash.put(StatList.BUF_INC, "스킬 데미지 증가(%) +"); reverseHash.put(StatList.BUF_CRT, "스킬 크리티컬 데미지 증가(%) +");
		
		reverseHash.put(StatList.SKILL, " Lv + "); reverseHash.put(StatList.SKILL_RANGE, " Lv 스킬 + ");
		
		reverseHash.put(StatList.DAM_INC_ALL, "모든 공격력 증가(%) +");
		reverseHash.put(StatList.DAM_INC_ADD, "데미지 추가 증가(%) +"); reverseHash.put(StatList.DAM_CRT_ADD, "크리티컬 데미지 추가 증가(%) +");
		reverseHash.put(StatList.DEF_DEC_IGN, "적 방어력 무시(%) -");
		reverseHash.put(StatList.WEP_NODEF_PHY_INC, "방어무시 물리 공격력 증가(%) +"); reverseHash.put(StatList.WEP_NODEF_MAG_INC, "방어무시 마법 공격력 증가(%) +");
		
		reverseHashsetted=true;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		StatusAndName temp = (StatusAndName) super.clone();
		temp.stat = (AbstractStatusInfo) stat.clone();
		return temp;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o==null) return false;
		if(o instanceof StatusAndName)
			return (name==((StatusAndName) o).name);
		if(o instanceof Integer)
			return (name==(int)o);
		if(o instanceof String)
			return (name==Status.getStatHash().get((String)o));
		return false;
	}
}
