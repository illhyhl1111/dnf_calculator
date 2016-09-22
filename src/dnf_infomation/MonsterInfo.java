package dnf_infomation;

import java.util.HashSet;

import dnf_InterfacesAndExceptions.DefenceIgnorePenalty;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Monster;


public class MonsterInfo {
	public static void getInfo(HashSet<Monster> monsterList)
	{
		Monster monster;
	
		try {
			monster = new Monster("임시몬스터");
			monster.setStat("백어택", true);
			monster.setStat("카운터", true);
			monster.setStat("난이도", DefenceIgnorePenalty.ANTON_NOR);
			monster.setStat("화속저", 40);
			monster.setStat("물리방어력", 135000);
			monster.setStat("마법방어력", 135000);
			monster.setStat("레벨", 115);
			monster.setStat("타입", MonsterType.BOSS);
			monster.setStat("체력", 1100000000);
			
			monsterList.add(monster);
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
}
