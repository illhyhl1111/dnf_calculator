package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Avatar_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_calculator.StatusList;
import dnf_infomation.GetDictionary;

@SuppressWarnings("serial")
public class Avatar extends Item
{
	public final Avatar_part part;
	private Emblem emblem1;
	private Emblem emblem2;
	private Emblem platinumEmblem;
	public final SetName setName;
	public LinkedList<String> coatSkillList;
	private String coatSkill;
	
	public Avatar(String name, Item_rarity rarity, Avatar_part part, Emblem emblem1, Emblem emblem2, Emblem platinum, SetName setName)
	{
		super(name, "image\\Avatar\\"+name+".png", rarity);
		this.part=part;
		this.emblem1=emblem1;
		this.emblem2=emblem2;
		platinumEmblem=platinum;
		this.setName=setName;
	}
	
	public Avatar(String name, Item_rarity rarity, Avatar_part part, SetName setName)
	{
		super(name, "image\\Avatar\\"+name+".png", rarity);
		this.part=part;
		emblem1 = new Emblem();
		emblem2 = new Emblem();
		platinumEmblem = new Emblem();
		this.setName=setName;
	}
	public Avatar(Avatar_part part) {
		super();
		this.part=part;
		emblem1 = new Emblem();
		emblem2 = new Emblem();
		platinumEmblem = new Emblem();
		setName = SetName.NONE;
	}
	
	public boolean setCoatOptionList(Job job){
		if(part!=Avatar_part.COAT) return false;
		coatSkillList = new LinkedList<String>();
		
		for(Skill skill : GetDictionary.charDictionary.getSkillList(job, 90)){
			if(skill.type!=Skill_type.TP && skill.maxLevel!=1)
				coatSkillList.add(skill.getItemName());
		}
		coatSkill = coatSkillList.getLast();
		vStat = new StatusList();
		vStat.addSkill(coatSkill, 1);
		return true;
	}
	
	@Override
	public String getTypeName() { return part.getName();}
	
	@Override
	public SetName getSetName() {return setName;}
	
	@Override
	public Emblem[] getEmblem()
	{
		return new Emblem[] {emblem1, emblem2, platinumEmblem};
	}
	
	@Override
	public boolean setEmblem1(Emblem emblem){
		if(!emblem.equipable(this)) return false;
		emblem1 = emblem;
		return true;
	}
	
	@Override
	public boolean setEmblem2(Emblem emblem){
		if(!emblem.equipable(this)) return false;
		emblem2 = emblem;
		return true;
	}
	
	@Override
	public boolean setPlatinum(Emblem emblem){
		if(!emblem.equipable(this)) return false;
		platinumEmblem = emblem;
		return true;
	}
	
	@Override
	public Object clone()
	{
		Avatar temp = (Avatar) super.clone();
		temp.emblem1 = (Emblem) this.emblem1.clone();
		temp.emblem2 = (Emblem) this.emblem2.clone();
		temp.platinumEmblem = (Emblem) this.platinumEmblem.clone();
		return temp;
	}
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 아바타 종류 : 레어->상압 
	// 3. 부위 : 모자->머리->얼굴->목가슴->상의->허리->하의->신발 ->피부->오라
	// 4. 오라 : 사전순
	@Override
    public int compareTo(Item arg) {																		// 1.
		if(!(arg instanceof Avatar)){
			if(arg instanceof Equipment || arg instanceof Card) return -1;
			else return 1;
		}
		Avatar arg2 = (Avatar)arg;
		
		if(arg2.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg2.getRarity().rarity;		// 2.
		if(arg2.part!=part) return part.order-arg2.part.order;												// 3.
		else return arg2.getName().compareTo(this.getName());												// 4.
    }

	public String getCoatSkill() {
		return coatSkill;
	}

	public void setCoatSkill(String coatSkill) {
		this.coatSkill = coatSkill;
		vStat = new StatusList();
		vStat.addSkill(coatSkill, 1);
	}
}
