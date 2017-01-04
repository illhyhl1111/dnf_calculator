package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.StatusAndName;
import dnf_class.Drape;

public class DrapeInfo {
	public static void getInfo(LinkedList<Drape> drapeList)
	{
		Drape temp;
		
		temp = new Drape("레전더리 휘장", Item_rarity.LEGENDARY, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("힘", 48, true, true);
		temp.vStat.addStatList("지능", 48, true, true);
		temp.vStat.addStatList("물공", 20, true, true);
		temp.vStat.addStatList("마공", 20, true, true);
		temp.vStat.addStatList("독공", 30, true, true);
		temp.vStat.addStatList("화속", 7, true, true);
		temp.vStat.addStatList("수속", 7, true, true);
		temp.vStat.addStatList("명속", 7, true, true);
		temp.vStat.addStatList("암속", 7, true, true);
		temp.vStat.addStatList("물크", 1.5, true, true);
		temp.vStat.addStatList("마크", 1.5, true, true);
		temp.vStat.addStatList("힘", 25, true, true);
		temp.vStat.addStatList("지능", 25, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		temp.explanation.add("휘장 강화는 맨 아래의 힘/지능 스탯으로 대체합니다");
		drapeList.add(temp);
		
		temp = new Drape("유니크 휘장", Item_rarity.UNIQUE, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("힘", 24, true, true);
		temp.vStat.addStatList("지능", 24, true, true);
		temp.vStat.addStatList("물공", 10, true, true);
		temp.vStat.addStatList("마공", 10, true, true);
		temp.vStat.addStatList("독공", 15, true, true);
		temp.vStat.addStatList("화속", 7, true, true);
		temp.vStat.addStatList("수속", 7, true, true);
		temp.vStat.addStatList("명속", 7, true, true);
		temp.vStat.addStatList("암속", 7, true, true);
		temp.vStat.addStatList("물크", 1.5, true, true);
		temp.vStat.addStatList("마크", 1.5, true, true);
		temp.vStat.addStatList("힘", 25, true, true);
		temp.vStat.addStatList("지능", 25, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		temp.explanation.add("휘장 강화는 맨 아래의 힘/지능 스탯으로 대체합니다");
		drapeList.add(temp);
		
		temp = new Drape("레어 휘장", Item_rarity.RARE, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("힘", 18, true, true);
		temp.vStat.addStatList("지능", 18, true, true);
		temp.vStat.addStatList("화속", 7, true, true);
		temp.vStat.addStatList("수속", 7, true, true);
		temp.vStat.addStatList("명속", 7, true, true);
		temp.vStat.addStatList("암속", 7, true, true);
		temp.vStat.addStatList("물크", 1.5, true, true);
		temp.vStat.addStatList("마크", 1.5, true, true);
		temp.vStat.addStatList("힘", 25, true, true);
		temp.vStat.addStatList("지능", 25, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		temp.explanation.add("휘장 강화는 맨 아래의 힘/지능 스탯으로 대체합니다");
		drapeList.add(temp);
		
		temp = new Drape("언커먼 휘장", Item_rarity.UNCOMMON, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("힘", 12, true, true);
		temp.vStat.addStatList("지능", 12, true, true);
		temp.vStat.addStatList("화속", 7, true, true);
		temp.vStat.addStatList("수속", 7, true, true);
		temp.vStat.addStatList("명속", 7, true, true);
		temp.vStat.addStatList("암속", 7, true, true);
		temp.vStat.addStatList("물크", 1.5, true, true);
		temp.vStat.addStatList("마크", 1.5, true, true);
		temp.vStat.addStatList("힘", 25, true, true);
		temp.vStat.addStatList("지능", 25, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		temp.explanation.add("휘장 강화는 맨 아래의 힘/지능 스탯으로 대체합니다");
		drapeList.add(temp);
		
		temp = new Drape("커먼 휘장", Item_rarity.COMMON, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("힘", 6, true, true);
		temp.vStat.addStatList("지능", 6, true, true);
		temp.vStat.addStatList("화속", 7, true, true);
		temp.vStat.addStatList("수속", 7, true, true);
		temp.vStat.addStatList("명속", 7, true, true);
		temp.vStat.addStatList("암속", 7, true, true);
		temp.vStat.addStatList("물크", 1.5, true, true);
		temp.vStat.addStatList("마크", 1.5, true, true);
		temp.vStat.addStatList("힘", 25, true, true);
		temp.vStat.addStatList("지능", 25, true, true);
		for(StatusAndName s : temp.vStat.statList)
			s.enabled=false;
		temp.explanation.add("휘장 강화는 맨 아래의 힘/지능 스탯으로 대체합니다");
		drapeList.add(temp);
	}
}
