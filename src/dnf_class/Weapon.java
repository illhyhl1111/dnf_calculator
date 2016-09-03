package dnf_class;

import dnf_InterfacesAndExceptions.AddOn;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Equip_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UnknownInformationException;
import dnf_InterfacesAndExceptions.Weapon_detailType;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusInfo;
import dnf_infomation.GetItemDictionary;

public class Weapon extends Equipment{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2317758055158333589L;

	public Weapon_detailType weaponType;
	public int reforge;
	
	public Weapon(String name, String icon,Item_rarity rarity, Card card, SetName setName, Weapon_detailType weaponType, int level, int reforge)
	{	
		super(name, icon, rarity, Equip_part.WEAPON, card, setName, Equip_type.NONE, level);
		this.weaponType =weaponType;
		this.reforge=reforge;

		vStat.addStatList("물리방무뎀", new StatusInfo(0), true);
		vStat.addStatList("마법방무뎀", new StatusInfo(0), true);
	}
	public Weapon(String name, String icon, Item_rarity rarity, Weapon_detailType weaponType, int level)
	{
		this(name, icon, rarity, new Card("없음", null, Item_rarity.NONE), SetName.NONE, weaponType, level, 0);
	}
	public Weapon() {
		super(Equip_part.WEAPON);
		this.weaponType=Weapon_detailType.NONE;
		reforge=0;
		
		vStat.addStatList("물리방무뎀", new StatusInfo(0), true);
		vStat.addStatList("마법방무뎀", new StatusInfo(0), true);
	}
	
	@Override
	public void setReinforce(int num) throws UnknownInformationException
	{
		super.setReinforce(num);
		try {			
			int ignIndex = getIgnIndex();
			if(ignIndex>=0){
				int ignPhy = GetItemDictionary.getReinforceInfo_phy(num, super.getRarity(), level, weaponType);
				int ignMag = GetItemDictionary.getReinforceInfo_mag(num, super.getRarity(), level, weaponType);
				vStat.statList.get(ignIndex).stat.setInfo(ignPhy);
				vStat.statList.get(ignIndex+1).stat.setInfo(ignMag);
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getIgnIndex()
	{
		int iter=0;
		for(StatusAndName s : vStat.statList){
			if(s.equals("물리방무뎀")){
				return iter;
			}
			iter++;
		}
		return -1;
	}
	
	@Override
	public int getItemStatIndex()
	{
		if(getIgnIndex()!=-1) return getIgnIndex()+2;
		else if(getDimStatIndex()!=-1) return getDimStatIndex()+1;
		return 0;
	}
	
	public boolean enabled(JobList job)
	{
		return weaponType.enabled(job);
	}
	
	@Override
	public String getTypeName() { return part.getName();}
	@Override
	public String getTypeName2() { return weaponType.getName();}
}
