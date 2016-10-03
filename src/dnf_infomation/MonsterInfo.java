package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.DefenceIgnorePenalty;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.StatusList;
import dnf_class.Monster;
import dnf_class.MonsterOption;

public class MonsterInfo {
	public static void getInfo(HashSet<Monster> monsterList)
	{
		Monster monster;
		MonsterOption subMonster;
		StatusList statList;
		
		try {
			monster = new Monster("찬란한 불꽃의 아그네스");
			monster.setStat("백어택", false);
			monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_NOR);
			monster.setStat("화속저", 40);
			monster.setStat("물리방어력", 135000);
			monster.setStat("마법방어력", 135000);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1100000000);
			
			subMonster = new MonsterOption("람쥐터져욧", null, monster);
			subMonster.explanation.add("홀리님 람쥐좀요");
			subMonster.explanation.add("홀리님 람쥐좀");
			subMonster.explanation.add("아 홀리님");
			subMonster.explanation.add("아");
			
			statList = new StatusList();
			statList.addStatList("%물방깍_스킬", -100);
			statList.addStatList("%마방깍_스킬", -100);
			monster.monsterFeature.put(subMonster, statList);
			
			monsterList.add(monster);
			
			
			
			monster = new Monster("오브젝트");
			monster.setStat("백어택", false);
			monster.setStat("카운터", false);
			monster.setStat("난이도", DefenceIgnorePenalty.NORMAL);
			monster.setStat("레벨", 100);
			monster.setStat("타입", MonsterType.NORMAL);
			monster.setStat("체력", 0);
			
			monsterList.add(monster);
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
}
