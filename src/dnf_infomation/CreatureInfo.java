package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Creature;

public class CreatureInfo {
	public static void getInfo(LinkedList<Creature> creatureList)
	{
		Creature temp;
		Item_rarity rarity= Item_rarity.RARE;
		
		//이그니스
		temp = new Creature("강인한 이그니스", rarity, CalculatorVersion.VER_1_1_g);
		temp.vStat.addStatList("힘", new StatusInfo(20));
		temp.dStat.addStatList("투함포항", 15, false, true);
		creatureList.add(temp);
		//아쿠아젤로
		temp = new Creature("명석한 아쿠아젤로", rarity, CalculatorVersion.VER_1_1_g);
		temp.vStat.addStatList("지능", new StatusInfo(20));
		temp.dStat.addStatList("투함포항", 15, false, true);
		creatureList.add(temp);
		//말밥
		temp = new Creature("마르바스", Item_rarity.UNCOMMON, CalculatorVersion.VER_1_1_b);
		temp.vStat.addStatList("물크", 2);
		temp.vStat.addStatList("마크", 2);
		temp.dStat.addStatList("투함포항", 20, false, true);
		creatureList.add(temp);
		
		//역천
		temp = new Creature("역천의 베히모스(15~20Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(55));
		temp.vStat.addStatList("지능", new StatusInfo(55));
		temp.vStat.addStatList("무기물공", new StatusInfo(50));
		temp.vStat.addStatList("무기마공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(80));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(15, 20, 1, false);
		creatureList.add(temp);
		temp = new Creature("역천의 베히모스(20~25Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(55));
		temp.vStat.addStatList("지능", new StatusInfo(55));
		temp.vStat.addStatList("무기물공", new StatusInfo(50));
		temp.vStat.addStatList("무기마공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(80));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(20, 25, 1, false);
		creatureList.add(temp);
		temp = new Creature("역천의 베히모스(25~30Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(55));
		temp.vStat.addStatList("지능", new StatusInfo(55));
		temp.vStat.addStatList("무기물공", new StatusInfo(50));
		temp.vStat.addStatList("무기마공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(80));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(25, 30, 1, false);
		creatureList.add(temp);
		//혈옥
		temp = new Creature("혈옥의 베히모스(15~20Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		temp.vStat.addStatList("지능", new StatusInfo(65));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(15, 20, 1, false);
		creatureList.add(temp);
		temp = new Creature("혈옥의 베히모스(20~25Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		temp.vStat.addStatList("지능", new StatusInfo(65));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(20, 25, 1, false);
		creatureList.add(temp);
		temp = new Creature("혈옥의 베히모스(25~30Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		temp.vStat.addStatList("지능", new StatusInfo(65));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(25, 30, 1, false);
		creatureList.add(temp);
		//연옥
		temp = new Creature("연옥의 베히모스(15~20Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("무기물공", new StatusInfo(55));
		temp.vStat.addStatList("무기마공", new StatusInfo(55));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(15, 20, 1, false);
		creatureList.add(temp);
		temp = new Creature("연옥의 베히모스(20~25Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("무기물공", new StatusInfo(55));
		temp.vStat.addStatList("무기마공", new StatusInfo(55));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(20, 25, 1, false);
		creatureList.add(temp);
		temp = new Creature("연옥의 베히모스(25~30Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("무기물공", new StatusInfo(55));
		temp.vStat.addStatList("무기마공", new StatusInfo(55));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addSkillRange(25, 30, 1, false);
		creatureList.add(temp);
		
		//베키
		temp = new Creature("쁘띠 베키", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("크증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(20, 20, 1, false);
		creatureList.add(temp);
		//아르고스
		temp = new Creature("쁘띠 아르고스", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("크증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(25, 25, 1, false);
		creatureList.add(temp);
		//골크
		temp = new Creature("쁘띠 골드크라운", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("크증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(30, 30, 1, false);
		creatureList.add(temp);
		//휴먼로이드
		temp = new Creature("휴먼로이드 P [페이탈]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("크증뎀", new StatusInfo(10), true);
		creatureList.add(temp);
		temp = new Creature("휴먼로이드 B [페이탈]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("크증뎀", new StatusInfo(10), true);
		creatureList.add(temp);
		temp = new Creature("휴먼로이드 P [강타]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("증뎀", new StatusInfo(10), true);
		creatureList.add(temp);
		temp = new Creature("휴먼로이드 B [강타]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("증뎀", new StatusInfo(10), true);
		creatureList.add(temp);
		temp = new Creature("휴먼로이드 P [크리티컬]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(15));
		creatureList.add(temp);
		temp = new Creature("휴먼로이드 B [크리티컬]", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(15));
		creatureList.add(temp);
		
		//노르닐
		temp = new Creature("SD 초월의 노르닐(15~20Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(15, 20, 1, false);
		creatureList.add(temp);
		temp = new Creature("SD 초월의 노르닐(20~25Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(20, 25, 1, false);
		creatureList.add(temp);
		temp = new Creature("SD 초월의 노르닐(25~30Lv)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("증뎀", new StatusInfo(10));
		temp.vStat.addSkillRange(25, 30, 1, false);
		creatureList.add(temp);
		
		//요정
		temp = new Creature("꿀잠 요정", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("모속강", 10);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		creatureList.add(temp);
		
		//뇌염룡
		temp = new Creature("뇌룡", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.vStat.addStatList("수속", 12);
		temp.vStat.addStatList("명속", 12);
		temp.vStat.addSkillRange(1, 15, 1, false);
		temp.explanation.add("아이콘 못찾겠음ㅜㅜ");
		creatureList.add(temp);
		temp = new Creature("염룡", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.vStat.addStatList("화속", 12);
		temp.vStat.addStatList("암속", 12);
		temp.vStat.addSkillRange(1, 15, 1, false);
		creatureList.add(temp);
		
		//SD
		temp = new Creature("SD 크리쳐", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("크증뎀", new StatusInfo(10));
		creatureList.add(temp);
		
		//해태
		temp = new Creature("용맹한 해태", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("물크", 12);
		creatureList.add(temp);
		temp = new Creature("총명한 해태", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(25));
		temp.vStat.addStatList("지능", new StatusInfo(25));
		temp.vStat.addStatList("마크", 12);
		creatureList.add(temp);
		
		//빙수
		temp = new Creature("수박 빙수 알바생", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("물공", new StatusInfo(55));
		temp.vStat.addStatList("마공", new StatusInfo(55));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", 3);
		temp.vStat.addStatList("마크", 3);
		creatureList.add(temp);
		temp = new Creature("망고 빙수 알바생", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		temp.vStat.addStatList("지능", new StatusInfo(65));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", 3);
		temp.vStat.addStatList("마크", 3);
		creatureList.add(temp);
		temp = new Creature("메론 빙수 알바생", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("물공", new StatusInfo(25));
		temp.vStat.addStatList("마공", new StatusInfo(25));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("힘", new StatusInfo(30));
		temp.vStat.addStatList("지능", new StatusInfo(30));
		temp.vStat.addStatList("물크", 3);
		temp.vStat.addStatList("마크", 3);
		creatureList.add(temp);
		
		//핫섬머
		temp = new Creature("핫섬머 SD 크리쳐", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(15));
		temp.vStat.addStatList("지능", new StatusInfo(15));
		temp.vStat.addStatList("크증뎀", 7);
		temp.vStat.addSkillRange(29, 30, 1, false);
		creatureList.add(temp);
		
		//라브 쥬엘
		temp = new Creature("라브 크리쳐", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("물공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("물크", 5);
		creatureList.add(temp);
		temp = new Creature("쥬엘 크리쳐", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("마공", new StatusInfo(50));
		temp.vStat.addStatList("독공", new StatusInfo(75));
		temp.vStat.addStatList("마크", 5);
		creatureList.add(temp);
		
		//쁘띠 바칼
		temp = new Creature("쁘띠 애쉬코어", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.vStat.addStatList("화속", 5);
		creatureList.add(temp);
		temp = new Creature("쁘띠 네이저", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.vStat.addStatList("암속", 5);
		creatureList.add(temp);
		temp = new Creature("쁘띠 이트레녹", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.vStat.addStatList("수속", 5);
		creatureList.add(temp);
		temp = new Creature("쁘띠 느마우그", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(10));
		temp.vStat.addStatList("지능", new StatusInfo(10));
		temp.vStat.addStatList("명속", 5);
		creatureList.add(temp);
		

		//헬로우 브라운
		temp = new Creature("헬로우 브라운", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addSkillRange(30, 40, 1, false);
		creatureList.add(temp);
		temp = new Creature("시니컬 토끼", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addSkillRange(20, 30, 1, false);
		creatureList.add(temp);
		
		//잡크리쳐
		temp = new Creature("기타등등등", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(30), true);
		temp.vStat.addStatList("지능", new StatusInfo(30), true);
		temp.vStat.addStatList("물크", 5, true);
		temp.vStat.addStatList("마크", 5, true);
		creatureList.add(temp);
	}
}
