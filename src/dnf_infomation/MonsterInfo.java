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
			monster.setStat("백어택", false); monster.setStat("카운터", false);
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
			monster.setStat("물리방어력", 1);
			monster.setStat("마법방어력", 1);
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
			monster.setStat("물리방어력", 1);
			monster.setStat("마법방어력", 1);
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
			monster.setStat("물리방어력", 1);
			monster.setStat("마법방어력", 1);
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
			
			
			//메탈기어
			monster = new Monster("메탈기어 카나프스(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 60); monster.setStat("수속저", 40);
			monster.setStat("명속저", 40); monster.setStat("암속저", 60);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1666350000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//베키
			monster = new Monster("양산형 베키(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 35); monster.setStat("수속저", 35);
			monster.setStat("명속저", 35); monster.setStat("암속저", 35);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1191400000); monster.setStat("방깍제한", 89.04);
			
			subMonster = new MonsterOption("속성 베키", monster);		
			statList = new StatusList();
			statList.addStatList("화속깍", -50);
			statList.addStatList("수속깍", -50);
			statList.addStatList("명속깍", -50);
			statList.addStatList("암속깍", -50);
			monster.monsterFeature.put(subMonster, statList);
			monsterList.add(monster);

			//카리나
			monster = new Monster("오염의 카리나(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 50); monster.setStat("수속저", 50);
			monster.setStat("명속저", 50); monster.setStat("암속저", 50);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1654560000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//미스트랄
			monster = new Monster("더 세븐 미스트랄(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 100);
			monster.setStat("명속저", 0); monster.setStat("암속저", 0);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1102680000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//로사우라
			monster = new Monster("점성술사 로사우라(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 70); monster.setStat("수속저", 70);
			monster.setStat("명속저", 70); monster.setStat("암속저", 70);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 2757560000L); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			

			//아이언 비스트
			monster = new Monster("아이언 비스트(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 100); monster.setStat("수속저", 30);
			monster.setStat("명속저", 10); monster.setStat("암속저", 10);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1600000000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//탄식의 램퍼드
			monster = new Monster("탄식의 램퍼드(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 70); monster.setStat("수속저", 40);
			monster.setStat("명속저", 0); monster.setStat("암속저", 0);
			monster.setStat("물리방어력", 1); monster.setStat("마법방어력", 1);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 99.5);
			monsterList.add(monster);

			//자켈리네
			monster = new Monster("하이퍼스피드 자켈리네(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 30);
			monster.setStat("명속저", 30); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1400000000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			

			//하부브
			monster = new Monster("파탄의 하부브(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 100); monster.setStat("수속저", 30);
			monster.setStat("명속저", 60); monster.setStat("암속저", 60);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			

			//레크
			monster = new Monster("레드 크라운(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 35); monster.setStat("수속저", 35);
			monster.setStat("명속저", 45); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 2362650000L); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			

			//네르베
			monster = new Monster("악몽의 네르베(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 30);
			monster.setStat("명속저", 30); monster.setStat("암속저", 90);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 2189810000L); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//아르고스
			monster = new Monster("고강화 아르고스(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 35); monster.setStat("수속저", 35);
			monster.setStat("명속저", 40); monster.setStat("암속저", 40);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//콰트로
			monster = new Monster("콰트로 마누스 mark-2(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 0); monster.setStat("수속저", 0);
			monster.setStat("명속저", 0); monster.setStat("암속저", 0);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//부폰
			monster = new Monster("비통의 부폰(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 60); monster.setStat("수속저", 40);
			monster.setStat("명속저", 40); monster.setStat("암속저", 60);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
			
			subMonster = new MonsterOption("배리어", monster);		
			statList = new StatusList();
			statList.addStatList("고정물방깍", -282521);
			statList.addStatList("고정마방깍", -282521);
			monster.monsterFeature.put(subMonster, statList);
			monsterList.add(monster);

			//아슬란
			monster = new Monster("망각의 아슬란(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 35); monster.setStat("수속저", 35);
			monster.setStat("명속저", 45); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1901200000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//베일
			monster = new Monster("증오의 베일(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 0); monster.setStat("수속저", 0);
			monster.setStat("명속저", 0); monster.setStat("암속저", 0);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1115180000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);

			//호루스
			monster = new Monster("빛의 우상 호루스(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 40); monster.setStat("수속저", 40);
			monster.setStat("명속저", 50); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1670375000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			
			//호구1
			monster = new Monster("거완의 왕자 골고타(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 0); monster.setStat("수속저", 0);
			monster.setStat("명속저", 30); monster.setStat("암속저", 100);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1886430000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			//호구2
			monster = new Monster("철완의 공주 칼바리(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 0); monster.setStat("수속저", 0);
			monster.setStat("명속저", 100); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 2193100000L); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			
			//조무래기
			monster = new Monster("정화의 스네이더(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 100); monster.setStat("수속저", 30);
			monster.setStat("명속저", 30); monster.setStat("암속저", 100);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1287300000); monster.setStat("방깍제한", 89.04);
			monster.explanation.add("조무래기쟝");
			monsterList.add(monster);
			//야신
			monster = new Monster("달빛을 걷는자 - 야신(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 0); monster.setStat("수속저", 0);
			monster.setStat("명속저", 100); monster.setStat("암속저", 100);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1166375000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			//아누비스
			monster = new Monster("어둠의 우상 아누비스(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 30);
			monster.setStat("명속저", 30); monster.setStat("암속저", 95);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.NAMED);
			monster.setStat("체력", 1654250000); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			//베아라
			monster = new Monster("악검 베아라(일반)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 60); monster.setStat("수속저", 40);
			monster.setStat("명속저", 40); monster.setStat("암속저", 100);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 3628170000L); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			
			//빛루크
			monster = new Monster("건설자 루크(빛)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 30);
			monster.setStat("명속저", 100); monster.setStat("암속저", 30);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
			monsterList.add(monster);
			//어둠루크
			monster = new Monster("건설자 루크(어둠)");
			monster.setStat("백어택", false); monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.LUKE_NOR);
			monster.setStat("화속저", 30); monster.setStat("수속저", 30);
			monster.setStat("명속저", 30); monster.setStat("암속저", 100);
			monster.setStat("물리방어력", 189914); monster.setStat("마법방어력", 189914);
			monster.setStat("레벨", 110); monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 0); monster.setStat("방깍제한", 89.04);
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
