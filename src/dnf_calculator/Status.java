package dnf_calculator;
import java.util.HashMap;

class StatusList
{
	public static final int STATNUM = 18;
	public static final int ELEMENTNUM = 4;
	public static final String[] statusList =
		{
			"str", "intell", "sta", "will",
			"atk_phy", "atk_mag", "atk_noDef_phy", "atk_noDef_mag",
			"def_dec_fixed", "def_dec_percent",
			"dam_inc", "dam_crt", "dam_add",
			"element_fire","element_water", "element_light","element_darkenss"
		};
}

public class Status {
	
	private HashMap<Element, ElementInfo> elementMap;
	
	private StatusInfo<?>[] statInfo; 
	
	public Status()
	{
		elementMap = new HashMap<Element, ElementInfo>();
		elementMap.put(Element.FIRE, new ElementInfo(false, 0));
		elementMap.put(Element.WATER, new ElementInfo(false, 0));
		elementMap.put(Element.LIGHT, new ElementInfo(false, 0));
		elementMap.put(Element.DARKNESS, new ElementInfo(false, 0));
		
		statInfo = new StatusInfo<?>[StatusList.STATNUM];
		int i;
		for(i=0; i<StatusList.STATNUM-StatusList.ELEMENTNUM; i++)
			statInfo[i] = new StatusInfo<Integer>(StatusList.statusList[i], 0);
		
		for(; i<StatusList.STATNUM; i++)
			statInfo[i] = new StatusInfo<ElementInfo>(StatusList.statusList[i], new ElementInfo(false, 0));
		
	}
	
	public void setStatus(Status_Public stat)
	{
		
	}
}

class Status_Public
{
	int str;
	int intell;
	int sta;
	int will;
	
	HashMap<Element, ElementInfo> elementMap=new HashMap<Element, ElementInfo>();
	
	int atk_phy;
	int atk_mag;
	int atk_indp;
	int atk_noDef_phy;
	int atk_noDef_mag;
	
	int def_dec_fixed;
	int def_dec_percent;
	
	int dam_inc;
	int dam_crt;
	int dam_add;
	
	public Status_Public()
	{
		str=0; intell=0; sta=0; will=0;
		
		elementMap.put(Element.FIRE, new ElementInfo(false, 0));
		elementMap.put(Element.WATER, new ElementInfo(false, 0));
		elementMap.put(Element.LIGHT, new ElementInfo(false, 0));
		elementMap.put(Element.DARKNESS, new ElementInfo(false, 0));
		
		atk_phy=0; atk_mag=0; atk_noDef_phy=0; atk_noDef_mag=0;
		def_dec_fixed=0; def_dec_percent=0;
		dam_inc=0; dam_crt=0; dam_add=0;
	}
	
}

class ElementInfo
{
	boolean hasElement;
	int elem_str;
	
	public ElementInfo(boolean activated, int strength)
	{
		hasElement=activated;
		elem_str=strength;
	}
	
	public void setElementInfo(boolean activated, int strength)
	{
		hasElement=activated;
		elem_str=strength;
	}
}

enum Element
{
	FIRE, WATER, LIGHT, DARKNESS;
}

class StatusInfo<T>
{
	String statName;
	T str;
	
	public StatusInfo(String name, T strength){
		statName=name;
		str=strength;
	}
}