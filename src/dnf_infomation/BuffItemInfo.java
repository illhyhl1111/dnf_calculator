package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Element_type;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_InterfacesAndExceptions.Job;
import dnf_calculator.StatusAndName;
import dnf_calculator.StatusList;
import dnf_class.Buff;
import dnf_class.ExclusiveBuff;
import dnf_class.SelectionBuff;

public class BuffItemInfo {
	public static void getInfo(LinkedList<Buff> buffList)
	{
		Buff buff;
		SelectionBuff selectionBuff;
		ExclusiveBuff exclusiveBuff;
		StatusList statList;
		
		buff = new Buff("투신의 함성 포션", Item_rarity.UNCOMMON, CalculatorVersion.VER_1_0_a);
		buff.dStat.addStatList("투함포항", 12);
		buffList.add(buff);
		
		buff = new Buff("고농축 힘/지능의 비약", Item_rarity.RARE, CalculatorVersion.VER_1_0_a);
		buff.dStat.addStatList("힘", 175);
		buff.dStat.addStatList("지능", 175);
		buffList.add(buff);
		
		buff = new Buff("활력의 비약", Item_rarity.UNCOMMON, CalculatorVersion.VER_1_0_a);
		buff.dStat.addStatList("힘", 50);
		buff.dStat.addStatList("지능", 50);
		buffList.add(buff);
		
		buff = new Buff("스킹/지축 주문서", Item_rarity.RARE, CalculatorVersion.VER_1_0_a);
		buff.fStat.statList.add("스킹주문서");
		buff.explanation.add("캐릭터의 레벨 x 0.5 만큼 물리/마법공격력 증가");
		buffList.add(buff);
		
		buff = new Buff("수련의 방 버프 1", Item_rarity.NONE, CalculatorVersion.VER_1_0_f);
		buff.dStat.addStatList("힘", 5000, true, true); buff.dStat.addStatList("지능", 5000, true, true);
		buff.dStat.addStatList("물공", 3000, true, true); buff.dStat.addStatList("마공", 3000, true, true);
		buff.dStat.addStatList("독공", 5000, true, true);
		buff.dStat.addStatList("화속", 500, true, true); buff.dStat.addStatList("수속", 500, true, true);
		buff.dStat.addStatList("명속", 500, true, true); buff.dStat.addStatList("암속", 500, true, true);
		buff.dStat.addStatList(Element_type.FIRE, 0, true, false, true); buff.dStat.addStatList(Element_type.WATER, 0, true, false, true);
		buff.dStat.addStatList(Element_type.LIGHT, 0, true, false, true); buff.dStat.addStatList(Element_type.DARKNESS, 0, true, false, true);
		buff.dStat.addStatList("힘뻥", 100, true, true); buff.dStat.addStatList("지능뻥", 100, true, true);
		buff.dStat.addStatList("물공뻥", 100, true, true); buff.dStat.addStatList("마공뻥", 100, true, true); buff.dStat.addStatList("독공뻥", 100, true, true);
		buff.dStat.addStatList("증뎀", 100, true, true); buff.dStat.addStatList("크증뎀", 100, true, true);
		buff.dStat.addStatList("추뎀", 100, true, true); buff.dStat.addStatList("화추뎀", 50, true, true);
		buff.dStat.addStatList("수추뎀", 50, true, true); buff.dStat.addStatList("명추뎀", 50, true, true); buff.dStat.addStatList("암추뎀", 50, true, true);
		buff.dStat.addStatList("화속깍", 100, true, true); buff.dStat.addStatList("수속깍", 100, true, true);
		buff.dStat.addStatList("명속깍", 100, true, true); buff.dStat.addStatList("암속깍", 100, true, true);
		for(StatusAndName s : buff.dStat.statList)
			s.enabled=false;
		buff.explanation.add("더블클릭하여 옵션을 변경하세요");
		buffList.add(buff);
		
		buff = new Buff("수련의 방 버프 2", Item_rarity.NONE, CalculatorVersion.VER_1_1_a);
		buff.dStat.addStatList("투함포항", 100, true, true); buff.dStat.addStatList("모공증", 100, true, true);
		buff.dStat.addStatList("적방무", 30, true, true); buff.dStat.addStatList("추증뎀", 100, true, true);
		buff.dStat.addStatList("추크증", 100, true, true); buff.dStat.addStatList("스증뎀", 100, true, true); buff.dStat.addStatList("스증뎀", 100, true, true);
		buff.dStat.addStatList("물크", 200, true, true); buff.dStat.addStatList("마크", 200, true, true); buff.dStat.addStatList("크리저항감소", 100, true, true);
		buff.dStat.addStatList("물리마스터리", 100, true, true); buff.dStat.addStatList("마법마스터리", 100, true, true); buff.dStat.addStatList("독공마스터리", 100, true, true);
		buff.dStat.addStatList("증뎀버프", 100, true, true); buff.dStat.addStatList("증뎀버프", 100, true, true);
		buff.dStat.addStatList("크증뎀버프", 100, true, true); buff.dStat.addStatList("크증뎀버프", 100, true, true);
		buff.dStat.addStatList("고정물방깍", 100000, true, true); buff.dStat.addStatList("고정마방깍", 100000, true, true);
		buff.dStat.addStatList("%물방깍_템", 100, true, true); buff.dStat.addStatList("%마방깍_템", 50, true, true);
		buff.dStat.addStatList("%물방깍_스킬", 100, true, true); buff.dStat.addStatList("%마방깍_스킬", 100, true, true);
		buff.dStat.addStatList("물리방무뻥", 100, true, true); buff.dStat.addStatList("마법방무뻥", 100, true, true);
		for(StatusAndName s : buff.dStat.statList)
			s.enabled=false;
		buff.explanation.add("더블클릭하여 옵션을 변경하세요");
		buffList.add(buff);
		
		
		selectionBuff = new SelectionBuff("큐브의 계약", Item_rarity.NONE, true, CalculatorVersion.VER_1_0_a);
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
		
		selectionBuff = new SelectionBuff("길드 스탯", Item_rarity.NONE, false, CalculatorVersion.VER_1_0_a);
		statList = new StatusList();
		statList.addStatList("힘", 60);
		statList.addStatList("지능", 60);
		selectionBuff.makeSelectionOption("무제한 스탯", statList);
		statList = new StatusList();
		statList.addStatList("힘", 60);
		statList.addStatList("지능", 60);
		selectionBuff.makeSelectionOption("기간 제한 스탯", statList);
		selectionBuff.enabled=true;
		selectionBuff.setSelection(new String[]{"무제한 스탯", "기간 제한 스탯"}, new boolean[]{true, true});
		buffList.add(selectionBuff);
		
		selectionBuff = new SelectionBuff("모험단", Item_rarity.NONE, true, CalculatorVersion.VER_1_0_e);
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
		selectionBuff.enabled=true;
		selectionBuff.setSelection(new String[]{"실버크라운 모험단", "멜트다운 모험단", "표류동굴 모험단", "역천의 폭포 모험단", "체념의 빙벽 모험단",
				"안트베르 협곡 모험단", "해상열차 모험단", "시간의 문 모험단", "파워스테이션 모험단", "노블스카이 모험단", "젤바 모험단"},
				new boolean[]{false, false, false, false, false, false, false, false, true, false, false});
		buffList.add(selectionBuff);
		
		selectionBuff = new SelectionBuff("펠 로스 글로리", Item_rarity.UNIQUE, false, CalculatorVersion.VER_1_0_d);
		statList = new StatusList();
		statList.addStatList("힘뻥", 11);
		selectionBuff.makeSelectionOption("펠 로스 글로리", statList);
		statList = new StatusList();
		statList.addStatList("힘뻥", 10);
		selectionBuff.makeSelectionOption("잊혀진(골든) 펠 로스 글로리", statList);
		buffList.add(selectionBuff);
		selectionBuff = new SelectionBuff("에이션트 엘븐 링", Item_rarity.UNIQUE, false, CalculatorVersion.VER_1_0_d);
		statList = new StatusList();
		statList.addStatList("지능뻥", 9);
		selectionBuff.makeSelectionOption("에이션트 엘븐 링", statList);
		statList = new StatusList();
		statList.addStatList("지능뻥", 8);
		selectionBuff.makeSelectionOption("잊혀진(골든) 에이션트 엘븐", statList);
		buffList.add(selectionBuff);
		
		exclusiveBuff = new ExclusiveBuff("무기->케세라", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("추뎀", 17);
		exclusiveBuff.dStat.addStatList("스증뎀", 20);
		exclusiveBuff.dStat.addStatList("모공증", 20);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 무기에서 케세라세라로 스위칭합니다");
		buffList.add(exclusiveBuff);
		exclusiveBuff = new ExclusiveBuff("무기->이기(미해방)", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("스증뎀", 35);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 무기에서 이기무기로 스위칭합니다");
		buffList.add(exclusiveBuff);
		exclusiveBuff = new ExclusiveBuff("무기->창성", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("스증뎀", 70.8);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 무기에서 창성무기로 스위칭합니다");
		buffList.add(exclusiveBuff);
		exclusiveBuff = new ExclusiveBuff("악세->황홀경", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("증뎀", 20);
		exclusiveBuff.dStat.addStatList("크증뎀", 20);
		exclusiveBuff.dStat.addStatList("모공증", 17);
		exclusiveBuff.dStat.addStatList("스증뎀", 5);
		exclusiveBuff.dStat.addStatList("스증뎀", 12);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 악세에서 황홀경(12강)으로 스위칭합니다");
		buffList.add(exclusiveBuff);
		exclusiveBuff = new ExclusiveBuff("방어구->택틱컬", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("추뎀", 55);
		exclusiveBuff.dStat.addStatList("마크", 12);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 방어구에서 택틱(4인)으로 스위칭합니다");
		buffList.add(exclusiveBuff);
		exclusiveBuff = new ExclusiveBuff("특수장비->헤블론", Item_rarity.EPIC, new Job[] {Job.ELEMENTALMASTER}, CalculatorVersion.VER_1_0_f);
		exclusiveBuff.dStat.addStatList("모공증", 20);
		exclusiveBuff.dStat.addStatList("스증뎀", 10);
		exclusiveBuff.dStat.addStatList("스증뎀", 20);
		exclusiveBuff.fStat.statList.add("엘마스위칭");
		exclusiveBuff.explanation.add("착용한 특수장비에서 헤블론(스증셋옵)으로 스위칭합니다");
		buffList.add(exclusiveBuff);
	}
}
