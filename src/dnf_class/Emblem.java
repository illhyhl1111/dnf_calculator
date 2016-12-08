package dnf_class;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.Skill_type;
import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_calculator.StatusList;
import dnf_infomation.GetDictionary;

public class Emblem extends Item
{
	private static final long serialVersionUID = 1635808919615364265L;
	public Emblem_type type;
	public LinkedList<String> platinumSkillList;
	private String platinumSkill;
	
	public Emblem(String name, Item_rarity rarity, Emblem_type type, String version)
	{
		super(name, "image\\Emblem\\"+name+".png", rarity, version);
		this.type=type;
	}
	public Emblem() {
		super("엠블렘 없음", null, Item_rarity.NONE, CalculatorVersion.DEFAULT);
		type = Emblem_type.NONE;
	}
	
	@Override
	public String getTypeName() { return "엠블렘";}
	
	public boolean setPlatinumOptionList(Job job){
		if(type!=Emblem_type.PLATINUM || !getName().contains("스킬")) return false;
		platinumSkillList = new LinkedList<String>();
		
		for(Skill skill : GetDictionary.getSkillList(job, 90)){
			if(skill.type!=Skill_type.TP && skill.maxLevel!=1 && skill.firstLevel!=1 && !skill.isOptionSkill() && !skill.isSubSkill() 
					&& (skill.firstLevel<48 || skill.firstLevel==60 || skill.firstLevel==70)
					&& !skill.getName().equals("광검 사용 가능") && !skill.getName().equals("권투 글러브 사용 가능") && !skill.getName().equals("원소폭격")
					&& !skill.getName().equals("유탄 마스터리") && !skill.getName().equals("강인한 신념")) 
				platinumSkillList.add(skill.getItemName());
		}
		platinumSkill = platinumSkillList.getLast();
		vStat = new StatusList();
		vStat.addStatList("힘", 8);
		vStat.addStatList("지능", 8);
		vStat.addSkill(platinumSkill, 1);
		return true;
	}
	
	public String getPlatinumSkill() {
		return platinumSkill;
	}

	public void setPlatinumSkill(String platinumSkill) {
		this.platinumSkill = platinumSkill;
		vStat = new StatusList();
		vStat.addStatList("힘", 8);
		vStat.addStatList("지능", 8);
		vStat.addSkill(platinumSkill, 1);
	}
	
	public boolean equipable(Avatar avatar)
	{
		return avatar.part.equipable(this);
	}
	
	//////정렬순서
	// 1. 종류 : 장비->칭호->보주->아바타->엠블렘->크리쳐->비장비
	// 2. 등급 : 플티->찬란->화려->빛나는
	// 3. 타입 : 붉->녹->붉녹->노->파->노파->다색
	// 4. 이름 : 사전순
	
	@Override
	public int compareTo(Item arg) {																		// 1.
		if(! (arg instanceof Emblem)){
			if(arg instanceof Creature | arg instanceof Buff) return 1;
			else return -1;
		}
		
		if(arg.getRarity()!=this.getRarity()) return this.getRarity().rarity-arg.getRarity().rarity;		// 2. 
		if(((Emblem)arg).type!=type) return type.order-((Emblem)arg).type.order;							// 3.
		else return arg.getName().compareTo(this.getName());												// 4.
	}
}
