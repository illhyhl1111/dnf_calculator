package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Creature;

public class CreatureInfo {
	public static void getInfo(LinkedList<Creature> creatureList)
	{
		Creature temp;
		String icon;
		Item_rarity rarity= Item_rarity.RARE;
		
		//역천
		temp = new Creature("역천의 베히모스(15~20Lv)", rarity);
		temp.vStat.addStatList("힘", new StatusInfo(55));
		temp.vStat.addStatList("지능", new StatusInfo(55));
		temp.vStat.addStatList("무기물공", new StatusInfo(50));
		temp.vStat.addStatList("무기마공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(80));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(15, 20, 1, false);
		creatureList.add(temp);
	}
}
