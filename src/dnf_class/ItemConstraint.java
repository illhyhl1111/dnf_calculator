package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;

public class ItemConstraint {
	public LinkedList<Equip_part> partList;
	public LinkedList<Equip_type> typeList;
	public int upperLevel;
	public int lowerLevel;
	public LinkedList<Item_rarity> rarityList;
	public LinkedList<SetName> setList;
	public Job job;
	
	public ItemConstraint(int lowerLevel, int upperLevel, Job job)
	{
		partList = new LinkedList<Equip_part>();
		rarityList = new LinkedList<Item_rarity>();
		typeList = new LinkedList<Equip_type>();
		setList = new LinkedList<SetName>();
		this.upperLevel=upperLevel;
		this.lowerLevel=lowerLevel;
		this.job=job;
	}
	
	public void levelRange(int lowerLevel, int upperLevel)
	{
		this.upperLevel=upperLevel;
		this.lowerLevel=lowerLevel;
	}
	
	public boolean equipInConstraint(Equipment equip)
	{
		if( (partList.contains(equip.getPart()) || typeList.contains(equip.getEquipType()))
				&& (setList.size()==0 || setList.contains(equip.getSetName())) 
				&& rarityList.contains(equip.getRarity()) && (lowerLevel <= equip.level && equip.level <= upperLevel) )
			return true;
		return false;
	}
}