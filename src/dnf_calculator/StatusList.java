package dnf_calculator;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;

@SuppressWarnings("serial")
public class StatusList implements java.io.Serializable, Cloneable {
	
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
	
	public void addSkill(String skillName, int skillLevel)
	{
		try {
			statList.add(new StatusAndName("스킬", new SkillStatusInfo(skillLevel, skillName)));
		} catch (UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	public void addSkillRange(int start, int end , int skillLevel)
	{
		try {
			statList.add(new StatusAndName("스킬범위", new SkillRangeStatusInfo(skillLevel, start, end)));
		} catch (UndefinedStatusKey e) {
			e.printStackTrace();
		}
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