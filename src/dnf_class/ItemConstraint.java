package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.JobList;

public class ItemConstraint {
	public LinkedList<Equip_part> partList;
	public int upperLevel;
	public int lowerLevel;
	public LinkedList<Item_rarity> rarityList;
	public JobList job;
	
	public ItemConstraint(int lowerLevel, int upperLevel, JobList job)
	{
		partList = new LinkedList<Equip_part>();
		rarityList = new LinkedList<Item_rarity>();
		this.upperLevel=upperLevel;
		this.lowerLevel=lowerLevel;
		this.job=job;
	}
	
	public void levelRange(int lowerLevel, int upperLevel)
	{
		this.upperLevel=upperLevel;
		this.lowerLevel=lowerLevel;
	}
}