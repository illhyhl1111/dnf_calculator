package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.FunctionStat;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.Characters;
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
			public StatusList function(Characters character, Monster monster, Object item) {
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
			public StatusList function(Characters character, Monster monster, Object item) {
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
		
		selectionBuff = new SelectionBuff("길드 스탯", Item_rarity.NONE, false);
		statList = new StatusList();
		statList.addStatList("힘", 60);
		statList.addStatList("지능", 60);
		selectionBuff.makeSelectionOption("무제한 스탯", statList);
		statList = new StatusList();
		statList.addStatList("힘", 60);
		statList.addStatList("지능", 60);
		selectionBuff.makeSelectionOption("기간 제한 스탯", statList);
		buffList.add(selectionBuff);
		
		selectionBuff = new SelectionBuff("모험단", Item_rarity.NONE, true);
		statList = new StatusList();
		statList.addStatList("힘", 10);
		statList.addStatList("지능", 10);
		selectionBuff.makeSelectionOption("실버크라운 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 30);
		statList.addStatList("지능", 30);
		selectionBuff.makeSelectionOption("멜트다운 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 50);
		statList.addStatList("지능", 50);
		selectionBuff.makeSelectionOption("표류동굴 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 70);
		statList.addStatList("지능", 70);
		selectionBuff.makeSelectionOption("역천의 폭포 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 90);
		statList.addStatList("지능", 90);
		selectionBuff.makeSelectionOption("체념의 빙벽 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 110);
		statList.addStatList("지능", 110);
		selectionBuff.makeSelectionOption("안트베르 협곡 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 125);
		statList.addStatList("지능", 125);
		selectionBuff.makeSelectionOption("해상열차 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 140);
		statList.addStatList("지능", 140);
		selectionBuff.makeSelectionOption("시간의 문 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 155);
		statList.addStatList("지능", 155);
		selectionBuff.makeSelectionOption("파워스테이션 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 170);
		statList.addStatList("지능", 170);
		selectionBuff.makeSelectionOption("노블스카이 모험단", statList);
		statList = new StatusList();
		statList.addStatList("힘", 185);
		statList.addStatList("지능", 185);
		selectionBuff.makeSelectionOption("젤바 모험단", statList);
		buffList.add(selectionBuff);
		
		buff = new Buff("펠 로스 글로리", Item_rarity.UNIQUE);
		buff.dStat.addStatList("힘뻥", 11);
		buffList.add(buff);
		buff = new Buff("잊혀진 펠 로스 글로리", Item_rarity.UNIQUE);
		buff.dStat.addStatList("힘뻥", 10);
		buffList.add(buff);
		buff = new Buff("에이션트 엘븐 링", Item_rarity.UNIQUE);
		buff.dStat.addStatList("지능뻥", 9);
		buffList.add(buff);
		buff = new Buff("잊혀진 에이션트 엘븐 링", Item_rarity.UNIQUE);
		buff.dStat.addStatList("지능뻥", 8);
		buffList.add(buff);
	}
}
