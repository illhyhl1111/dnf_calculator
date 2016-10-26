package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.DefenceIgnorePenalty;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.StatusList;
import dnf_class.Monster;
import dnf_class.MonsterOption;

public class MonsterInfo {
	public static void getInfo(LinkedList<Monster> monsterList)
	{
		Monster monster;
		MonsterOption subMonster;
		StatusList statList;
		
		try {
			//누골
			monster = new Monster("진 : 거대 누골 100Lv(수련의 방)");
			monster.setStat("백어택", false); monster.setStat("카운터", true);
			monster.setStat("난이도", DefenceIgnorePenalty.TRAININGROOM);
			monster.setStat("화속저", -20); monster.setStat("수속저", -20);
			monster.setStat("물리방어력", 168882); monster.setStat("마법방어력", 168882);
			monster.setStat("레벨", 100); monster.setStat("타입", MonsterType.NORMAL);
			monster.setStat("체력", 1150000000);
			monsterList.add(monster);
			
			//퍼만
			monster = new Monster("허무의 퍼만 105Lv(수련의 방)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.TRAININGROOM);
			monster.setStat("화속저", 20); monster.setStat("수속저", -10);
			monster.setStat("명속저", -10); monster.setStat("암속저", 20);
			monster.setStat("물리방어력", 160855); monster.setStat("마법방어력", 160855);
			monster.setStat("레벨", 105); monster.setStat("타입", MonsterType.NORMAL);
			monster.setStat("체력", 1700000000);
			monsterList.add(monster);
			
			//토그
			monster = new Monster("토그(화)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", -1500);
			monster.setStat("수속저", 9999);
			monster.setStat("명속저", 9999);
			monster.setStat("암속저", 9999);
			monster.setStat("물리방어력", 1);
			monster.setStat("마법방어력", 1);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 478000000);
			monster.setStat("방깍제한", 99.500);
			monster.explanation.add("방깍제한 - 99.5%");
			monsterList.add(monster);
			
			monster = new Monster("토그(수)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 9999);
			monster.setStat("수속저", -1500);
			monster.setStat("명속저", 9999);
			monster.setStat("암속저", 9999);
			monster.setStat("물리방어력", 0);
			monster.setStat("마법방어력", 0);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 478000000);
			monster.setStat("방깍제한", 99.500);
			monster.explanation.add("방깍제한 - 99.5%");
			monsterList.add(monster);
			
			monster = new Monster("토그(명)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 9999);
			monster.setStat("수속저", 9999);
			monster.setStat("명속저", -1500);
			monster.setStat("암속저", 9999);
			monster.setStat("물리방어력", 0);
			monster.setStat("마법방어력", 0);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 478000000);
			monster.setStat("방깍제한", 99.500);
			monster.explanation.add("방깍제한 - 99.5%");
			monsterList.add(monster);
			
			monster = new Monster("토그(암)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 9999);
			monster.setStat("수속저", 9999);
			monster.setStat("명속저", 9999);
			monster.setStat("암속저", -1500);
			monster.setStat("물리방어력", 0);
			monster.setStat("마법방어력", 0);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 478000000);
			monster.setStat("방깍제한", 99.500);
			monster.explanation.add("방깍제한 - 99.5%");
			monsterList.add(monster);
			
			//네르베
			monster = new Monster("섬멸의 네르베");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 10);
			monster.setStat("수속저", -15);
			monster.setStat("명속저", 45);
			monster.setStat("물리방어력", 134347);
			monster.setStat("마법방어력", 134347);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1791000000);
			monster.setStat("방깍제한", 81.12);
			
			subMonster = new MonsterOption("구슬", monster);		
			statList = new StatusList();
			statList.addStatList("힘뻥", 50);
			monster.monsterFeature.put(subMonster, statList);
			monsterList.add(monster);
			
			//둠플
			monster = new Monster("둠 플레이너스");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("명속저", 40);
			monster.setStat("수속저", -20);
			monster.setStat("물리방어력", 134347);
			monster.setStat("마법방어력", 134347);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 563000000);
			monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//둠타
			monster = new Monster("둠 타이오릭");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 40);
			monster.setStat("수속저", -20);
			monster.setStat("물리방어력", 134347);
			monster.setStat("마법방어력", 134347);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 596000000);
			monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//크레이브
			monster = new Monster("염화의 크레이브");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 40);
			monster.setStat("수속저", -35);
			monster.setStat("물리방어력", 116824);
			monster.setStat("마법방어력", 116824);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 651000000);
			monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//크라텍
			monster = new Monster("수문장 크라텍");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 40);
			monster.setStat("수속저", -35);
			monster.setStat("물리방어력", 134347);
			monster.setStat("마법방어력", 134347);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 680000000);
			monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//아그네스
			monster = new Monster("찬란한 불꽃의 아그네스");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45);
			monster.setStat("수속저", -30);
			monster.setStat("물리방어력", 134347);
			monster.setStat("마법방어력", 134347);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1477000000);
			monster.setStat("방깍제한", 81.12);
			
			subMonster = new MonsterOption("람쥐터져욧", monster);
			subMonster.explanation.add("홀리님 람쥐좀요");
			subMonster.explanation.add("홀리님 람쥐좀");
			subMonster.explanation.add("아 홀리님");
			subMonster.explanation.add("아");		
			statList = new StatusList();
			statList.addStatList("%물방깍_스킬", -100);
			statList.addStatList("%마방깍_스킬", -100);
			monster.monsterFeature.put(subMonster, statList);
			monsterList.add(monster);
			
			//쿠로
			monster = new Monster("강완의 쿠로");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("명속저", 30); monster.setStat("암속저", -10);
			monster.setStat("물리방어력", 147781);
			monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 860000000);
			monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//로크
			monster = new Monster("흑화의 로크");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 996000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//아톨
			monster = new Monster("분쇄의 아톨");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45); monster.setStat("수속저", -15);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 878000000); monster.setStat("방깍제한", 81.12);
			
			subMonster = new MonsterOption("원", monster);		
			statList = new StatusList();
			statList.addStatList("물공뻥", 50);
			statList.addStatList("마공뻥", 50);
			monster.monsterFeature.put(subMonster, statList);
			subMonster.explanation.add("화르륵");
			monsterList.add(monster);
			
			//멜타
			monster = new Monster("멜타도록");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45); monster.setStat("수속저", -15);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 884000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//괴충
			monster = new Monster("용암괴충");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45); monster.setStat("수속저", -15);
			monster.setStat("물리방어력", 128506); monster.setStat("마법방어력", 128506);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 536000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//4념체
			monster = new Monster("공포의 사념체");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", -10); monster.setStat("수속저", 30);
			monster.setStat("명속저", -10); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 611000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//에게느
			monster = new Monster("흡수의 에게느");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 50); monster.setStat("수속저", -10);
			monster.setStat("물리방어력", 128506); monster.setStat("마법방어력", 128506);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1890000000); monster.setStat("방깍제한", 81.12);
			monster.explanation.add("뀨"); monsterList.add(monster);
			
			//크레스
			monster = new Monster("흑연의 크레스");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 50); monster.setStat("수속저", 50);
			monster.setStat("명속저", 50); monster.setStat("암속저", 50);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 826000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//마그토늄5
			monster = new Monster("마그토늄 파이브");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45); monster.setStat("수속저", -15);
			monster.setStat("명속저", 0); monster.setStat("암속저", 45);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 611000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//메델
			monster = new Monster("심연의 메델");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 45); monster.setStat("수속저", -15);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 731000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
			//마테카
			monster = new Monster("전능의 마테카");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_RAID);
			monster.setStat("화속저", 60); monster.setStat("수속저", 60);
			monster.setStat("명속저", 60); monster.setStat("암속저", 60);
			monster.setStat("물리방어력", 147781); monster.setStat("마법방어력", 147781);
			monster.setStat("레벨", 115); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1549000000); monster.setStat("방깍제한", 81.12);
			monsterList.add(monster);
			
		
			//오브젝트
			monster = new Monster("강화기");
			monster.setStat("백어택", false);
			monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.TRAININGROOM);
			monster.setStat("레벨", 100);
			monster.setStat("타입", MonsterType.NORMAL);
			monster.setStat("체력", 0);
			monsterList.add(monster);
			
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
}
