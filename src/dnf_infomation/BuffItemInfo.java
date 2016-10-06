package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.Characters;
import dnf_class.Equipment;
import dnf_class.Monster;
import dnf_class.SelectionBuff;

public class BuffItemInfo {
	public static void getInfo(LinkedList<Buff> buffList)
	{
		Buff buff;
		SelectionBuff selectionBuff;
		StatusList statList;
		
		buff = new Buff("투신의 함성 포션", Item_rarity.UNCOMMON);
		buff.dStat.addStatList("투함포항", 12);
		buffList.add(buff);
		
		buff = new Buff("고농축 힘의 비약", Item_rarity.RARE);
		buff.dStat.addStatList("힘", 175);
		buffList.add(buff);
		
		buff = new Buff("고농축 지능의 비약", Item_rarity.RARE);
		buff.dStat.addStatList("지능", 175);
		buffList.add(buff);
		
		buff = new Buff("활력의 비약", Item_rarity.UNCOMMON);
		buff.dStat.addStatList("힘", 50);
		buff.dStat.addStatList("지능", 50);
		buffList.add(buff);
		
		buff = new Buff("스트라이킹 주문서", Item_rarity.RARE);
		buff.fStat.statList.add(new FunctionStat() {
			private static final long serialVersionUID = -2044206307504181197L;
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				StatusList statList = new StatusList();
				statList.addStatList("물공", character.getLevel()/2);
				return statList;
			}
		});
		buff.explanation.add("캐릭터의 레벨 x 0.5 만큼 물리공격력 증가");
		buffList.add(buff);
		
		buff = new Buff("지혜의 축복 주문서", Item_rarity.RARE);
		buff.fStat.statList.add(new FunctionStat() {
			private static final long serialVersionUID = 1074153181255890241L;
			@Override
			public StatusList function(Characters character, Monster monster, Equipment equipment) {
				StatusList statList = new StatusList();
				statList.addStatList("마공", character.getLevel()/2);
				return statList;
			}
		});
		buff.explanation.add("캐릭터의 레벨 x 0.5 만큼 마법공격력 증가");
		buffList.add(buff);
		
		selectionBuff = new SelectionBuff("큐브의 계약", Item_rarity.NONE, true);
		statList = new StatusList();
		statList.addStatList("물공", 40);
		statList.addStatList("마공", 40);
		selectionBuff.makeSelectionOption("무색 큐브", statList);
		statList = new StatusList();
		statList.addStatList(Element_type.FIRE, 0, true, false, false);
		selectionBuff.makeSelectionOption("적색 큐브", statList);
		statList = new StatusList();
		statList.addStatList(Element_type.WATER, 0, true, false, false);
		selectionBuff.makeSelectionOption("청색 큐브", statList);
		statList = new StatusList();
		statList.addStatList(Element_type.LIGHT, 0, true, false, false);
		selectionBuff.makeSelectionOption("흰색 큐브", statList);
		statList = new StatusList();
		statList.addStatList(Element_type.DARKNESS, 0, true, false, false);
		selectionBuff.makeSelectionOption("흑색 큐브", statList);
		statList = new StatusList();
		statList.addStatList("물크", 5);
		statList.addStatList("마크", 5);
		selectionBuff.makeSelectionOption("황금 큐브", statList);
		buffList.add(selectionBuff);
	}
}
