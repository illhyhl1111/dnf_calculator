package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.Emblem_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Emblem;

public class EmblemInfo {
	public static void getInfo(HashSet<Emblem> emblemList)
	{
		Emblem temp;
		String icon=null;
		Item_rarity rarity;
		
		//////////찬란
		rarity = Item_rarity.UNIQUE;
		
		//힘
		temp = new Emblem("찬란한 붉은빛 엠블렘[힘]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		emblemList.add(temp);
		
		//지능
		temp = new Emblem("찬란한 붉은빛 엠블렘[지능]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("지능", new StatusInfo(25));
		emblemList.add(temp);
		
		//물크
		temp = new Emblem("찬란한 녹색빛 엠블렘[물리크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		emblemList.add(temp);
		
		//마크
		temp = new Emblem("찬란한 녹색빛 엠블렘[마법크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		emblemList.add(temp);
		
		//힘물크
		temp = new Emblem("찬란한 듀얼 엠블렘[힘 + 물리크리티컬]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(1.5));
		emblemList.add(temp);
		
		//지능마크
		temp = new Emblem("찬란한 듀얼 엠블렘[지능 + 마법크리티컬]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(1.5));
		emblemList.add(temp);
		
		//옐로우
		temp = new Emblem("찬란한 옐로우 엠블렘[힘]", icon, rarity, Emblem_type.YELLOW);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.explanation.add("단종된 이벤트 엠블렘입니다");
		emblemList.add(temp);
		
		//그린
		temp = new Emblem("찬란한 그린 엠블렘[지능]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.explanation.add("단종된 이벤트 엠블렘입니다. 근데 무쓸모");
		emblemList.add(temp);
		
		//
		temp = new Emblem("찬란한 듀얼 엠블렘[힘 + (기타옵션)]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		emblemList.add(temp);
		
		//
		temp = new Emblem("찬란한 듀얼 엠블렘[지능 + (기타옵션)]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(15));
		emblemList.add(temp);
		
		
		////////////화려한
		rarity = Item_rarity.RARE;
		
		//힘
		temp = new Emblem("화려한 붉은빛 엠블렘[힘]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("힘", new StatusInfo(17));
		emblemList.add(temp);
		
		//지능
		temp = new Emblem("화려한 붉은빛 엠블렘[지능]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("지능", new StatusInfo(17));
		emblemList.add(temp);
		
		//물크
		temp = new Emblem("화려한 녹색빛 엠블렘[물리크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(2.2));
		emblemList.add(temp);
		
		//마크
		temp = new Emblem("화려한 녹색빛 엠블렘[마법크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(2.2));
		emblemList.add(temp);
		
		//힘물크
		temp = new Emblem("화려한 듀얼 엠블렘[힘 + 물리크리티컬]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(1.1));
		emblemList.add(temp);
		
		//지능마크
		temp = new Emblem("화려한 듀얼 엠블렘[지능 + 마법크리티컬]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(1.1));
		emblemList.add(temp);
		
		//옐로우
		temp = new Emblem("화려한 옐로우 엠블렘[힘]", icon, rarity, Emblem_type.YELLOW);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.explanation.add("단종된 이벤트 엠블렘입니다");
		emblemList.add(temp);
		
		//그린
		temp = new Emblem("화려한 그린 엠블렘[지능]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.explanation.add("단종된 이벤트 엠블렘입니다. 근데 무쓸모");
		emblemList.add(temp);
		
		//
		temp = new Emblem("화려한 듀얼 엠블렘[힘 + (기타옵션)]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		emblemList.add(temp);
		
		//
		temp = new Emblem("화려한 듀얼 엠블렘[지능 + (기타옵션)]", icon, rarity, Emblem_type.RED_GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(10));
		emblemList.add(temp);
		
		
		/////////빛나는
		rarity = Item_rarity.UNCOMMON;
		
		//힘
		temp = new Emblem("빛나는 붉은빛 엠블렘[힘]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		emblemList.add(temp);
		
		//지능
		temp = new Emblem("빛나는 붉은빛 엠블렘[지능]", icon, rarity, Emblem_type.RED);
		temp.vStat.addStatList("지능", new StatusInfo(10));
		emblemList.add(temp);
		
		//물크
		temp = new Emblem("빛나는 녹색빛 엠블렘[물리크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(1.2));
		emblemList.add(temp);
		
		//마크
		temp = new Emblem("빛나는 녹색빛 엠블렘[마법크리티컬]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(1.2));
		emblemList.add(temp);
		
		//옐로우
		temp = new Emblem("빛나는 옐로우 엠블렘[힘]", icon, rarity, Emblem_type.YELLOW);
		temp.vStat.addStatList("힘", new StatusInfo(5));
		temp.explanation.add("단종된 이벤트 엠블렘입니다");
		emblemList.add(temp);
		
		//그린
		temp = new Emblem("빛나는 그린 엠블렘[지능]", icon, rarity, Emblem_type.GREEN);
		temp.vStat.addStatList("지능", new StatusInfo(5));
		temp.explanation.add("단종된 이벤트 엠블렘입니다. 근데 무쓸모");
		emblemList.add(temp);
		
		///////////플래티넘
		rarity = Item_rarity.LEGENDARY;
		
		//잡플티
		temp = new Emblem("플레티넘 엠블렘[잡]", icon, rarity, Emblem_type.PLATINUM);
		temp.vStat.addStatList("힘", new StatusInfo(6));
		temp.vStat.addStatList("지능", new StatusInfo(6));
		emblemList.add(temp);
		
		//플티
		temp = new Emblem("플레티넘 엠블렘[스킬]", icon, rarity, Emblem_type.PLATINUM);
		temp.vStat.addStatList("힘", new StatusInfo(8));
		temp.vStat.addStatList("지능", new StatusInfo(8));
		//TODO 스킬
		emblemList.add(temp);

		
		/////없음
		temp = new Emblem("엠블렘 없음", null, Item_rarity.COMMON, Emblem_type.MULTIPLE_COLOR);
		emblemList.add(temp);
	}
}
