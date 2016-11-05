package dnf_calculator;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

public class StatusList implements java.io.Serializable, Cloneable {
	
	private static final long serialVersionUID = -950365505210777854L;
	public LinkedList<StatusAndName> statList;
	
	public StatusList(){
		statList = new LinkedList<StatusAndName>();
	}
	
	public void addStatList(int name, AbstractStatusInfo addStat)
	{
		statList.add(new StatusAndName(name, addStat));
	}
	public void addStatList(String name, AbstractStatusInfo addStat)
	{
		try{
			statList.add(new StatusAndName(name, addStat));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	public void addStatList(int name, AbstractStatusInfo addStat, boolean changeable)
	{
		statList.add(new StatusAndName(name, addStat, changeable));
	}
	public void addStatList(String name, AbstractStatusInfo addStat, boolean changeable)
	{
		try{
			statList.add(new StatusAndName(name, addStat, changeable));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	public void addStatList(int name, AbstractStatusInfo addStat, boolean changeable, boolean enableable)
	{
		statList.add(new StatusAndName(name, addStat, changeable, enableable));
	}
	public void addStatList(String name, AbstractStatusInfo addStat, boolean changeable, boolean enableable)
	{
		try{
			statList.add(new StatusAndName(name, addStat, changeable, enableable));
		}
		catch(UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
	}
	
	public void addStatList(String name, int stat)
	{
		try {
			statList.add(new StatusAndName(name, stat));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	public void addStatList(String name, double stat)
	{
		try {
			statList.add(new StatusAndName(name, stat));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	public void addStatList(int name, double stat) {
		if(name<=StatList.ELEMENTNUM_END) statList.add(new StatusAndName(name, new ElementInfo((int) Math.round(stat))));
		else if(name<=StatList.INTNUM_END) statList.add(new StatusAndName(name, new StatusInfo((int) Math.round(stat))));
		else if(name<=StatList.DOUBLENUM_END) statList.add(new StatusAndName(name, new DoubleStatusInfo(stat)));
	}
	public void addStatList(String name, int stat, boolean changeable)
	{
		try {
			statList.add(new StatusAndName(name, stat, changeable));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	public void addStatList(String name, double stat, boolean changeable)
	{
		try {
			statList.add(new StatusAndName(name, stat, changeable));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	public void addStatList(String name, int stat, boolean changeable, boolean enableable)
	{
		try {
			statList.add(new StatusAndName(name, stat, changeable, enableable));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	public void addStatList(String name, double stat, boolean changeable, boolean enableable)
	{
		try {
			statList.add(new StatusAndName(name, stat, changeable, enableable));
		} catch (UndefinedStatusKey | StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public void addStatList(Element_type type, int stat, boolean enableElement, boolean changeable, boolean enableable)
	{
		switch(type)
		{
		case FIRE:
			statList.add(new StatusAndName(StatList.ELEM_FIRE, new ElementInfo(enableElement, stat), changeable, enableable));
			break;
		case WATER:
			statList.add(new StatusAndName(StatList.ELEM_WATER, new ElementInfo(enableElement, stat), changeable, enableable));
			break;
		case LIGHT:
			statList.add(new StatusAndName(StatList.ELEM_LIGHT, new ElementInfo(enableElement, stat), changeable, enableable));
			break;
		case DARKNESS:
			statList.add(new StatusAndName(StatList.ELEM_DARKNESS, new ElementInfo(enableElement, stat), changeable, enableable));
			break;
		case NONE:
			break;
		}
	}
	
	public void addSkill(String skillName, int skillLevel, boolean changeable, boolean enableable)
	{
		try {
			statList.add(new StatusAndName("스킬", new SkillStatusInfo(skillLevel, 0, skillName), changeable, enableable));
		} catch (UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	public void addSkill(String skillName, int skillLevel)
	{
		addSkill(skillName, skillLevel, false, false);
	}
	public void addSkillRange(int start, int end , int skillLevel, boolean TP, boolean changeable, boolean enableable)
	{
		try {
			statList.add(new StatusAndName("스킬범위", new SkillRangeStatusInfo(skillLevel, start, end, TP), changeable, enableable));
		} catch (UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	public void addSkillRange(int start, int end , int skillLevel, boolean TP)
	{
		addSkillRange(start, end , skillLevel, TP, false, false);
	}
	
	public void addSkill_damage(String skillName, double skillIncrease, boolean changeable, boolean enableable)
	{
		try {
			statList.add(new StatusAndName("스킬", new SkillStatusInfo(0, skillIncrease, skillName), changeable, enableable));
		} catch (UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	public void addSkill_damage(String skillName, double skillIncrease)
	{
		addSkill_damage(skillName, skillIncrease, false, false);
	}
	
	public void changeStat(int order, AbstractStatusInfo stat)
	{
		StatusAndName temp = statList.get(order);
			if(temp.changeable) temp.stat=stat;
	}
	public void changeStat(int order, double stat, boolean enable)
	{
		StatusAndName temp = statList.get(order);
		if(temp.changeable)
			try {
				temp.stat.setInfo(stat);
			} catch (StatusTypeMismatch e) {
				e.printStackTrace();
			}
		if(temp.enableable)
			temp.enabled=enable;
	}
	
	public StatusAndName findStat(int name)
	{
		for(StatusAndName s : statList)
			if(s.name==name){
				return s;
			}
		return null;
	}
	
	public void addListToStat(Status stat)
	{
		for(StatusAndName s : statList)
			if(s.enabled) stat.addStat(s.name, s.stat);
	}
	
	public double getStatSum(int name)
	{
		double temp=0;
		for(StatusAndName s : statList)
			if(s.name==name)
				try {
					temp+=s.stat.getStatToDouble();
				} catch (StatusTypeMismatch e) {
					e.printStackTrace();
				}
		return temp;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		StatusList temp = (StatusList) super.clone();
		temp.statList = new LinkedList<StatusAndName>();
		for(StatusAndName s : statList)
		{
			temp.statList.add((StatusAndName)s.clone());
		}
		return temp;
	}
}