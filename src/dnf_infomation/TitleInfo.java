package dnf_infomation;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Card;
import dnf_class.Title;

public class TitleInfo {
	public static void getInfo(LinkedList<Title> titleList)
	{
		Title temp;
		Title temp2;
		Title temp3;
		Card card;
		Item_rarity rarity = Item_rarity.RARE;
		
		//물풍선
		temp = new Title("물풍선 던지기(화)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(44), true);
		temp.vStat.addStatList("지능", new StatusInfo(44), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("추뎀", new StatusInfo(10));
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("화속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp.setCard(card);
		titleList.add(temp);

		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("수속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("물풍선 던지기(수)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("명속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("물풍선 던지기(명)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("암속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("물풍선 던지기(암)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("모속강", 4);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("물풍선 던지기(모속)");
		temp.setCard(card);
		titleList.add(temp);
		
		//로맨틱
		temp = new Title("러블리", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(40), true);
		temp.vStat.addStatList("지능", new StatusInfo(40), true);
		temp.vStat.addStatList("물크", 12);
		temp.vStat.addStatList("마크", 12);
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		
		temp2 = new Title("스위티", rarity, CalculatorVersion.VER_1_0_c);
		temp2.vStat.addStatList("힘", new StatusInfo(40), true);
		temp2.vStat.addStatList("지능", new StatusInfo(40), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp2.vStat.addStatList("모속강", new ElementInfo(16));
		temp2.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp2.dStat.addStatList("지능", new StatusInfo(25), false, true);
		
		temp3 = new Title("로맨틱", rarity, CalculatorVersion.VER_1_0_a);
		temp3.vStat.addStatList("힘", new StatusInfo(40), true);
		temp3.vStat.addStatList("지능", new StatusInfo(40), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp3.vStat.addStatList("증뎀", new StatusInfo(10));
		temp3.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp3.dStat.addStatList("지능", new StatusInfo(25), false, true);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("물공", 20);
		card.vStat.addStatList("마공", 20);
		card.vStat.addStatList("독공", 25);
		card.addPart(Equip_part.TITLE);
		temp.setCard(card);
		temp2.setCard(card);
		temp3.setCard(card);
		titleList.add(temp);
		titleList.add(temp2);
		titleList.add(temp3);
		
		//선선
		temp = new Title("선남 선녀", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(40), true);
		temp.vStat.addStatList("지능", new StatusInfo(40), true);
		temp.vStat.addStatList("증뎀", new StatusInfo(10), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.dStat.addStatList("추뎀", 5);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("선남 선녀(5%)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.dStat.addStatList("추뎀", 3);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("선남 선녀(3%)");
		temp.setCard(card);
		titleList.add(temp);
		
		//몽롱한 시선
		temp = new Title("몽롱한 시선", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(33), true);
		temp.vStat.addStatList("지능", new StatusInfo(33), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("증뎀", new StatusInfo(10));
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		titleList.add(temp);
		
		//해변
		temp = new Title("해변의 화끈한 그녀(화)", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(40), true);
		temp.vStat.addStatList("지능", new StatusInfo(40), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("크증뎀", 10);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("화속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp.setCard(card);
		titleList.add(temp);

		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("수속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("해변의 화끈한 그녀(수)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("명속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("해변의 화끈한 그녀(명)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("암속", new ElementInfo(6));
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("해변의 화끈한 그녀(암)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.vStat.addStatList("모속강", 4);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("해변의 화끈한 그녀(모속)");
		temp.setCard(card);
		titleList.add(temp);
		
		//앱클스
		temp = new Title("애프터 크리스마스", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(70), true);
		temp.vStat.addStatList("지능", new StatusInfo(70), true);
		temp.vStat.addStatList("물공", new StatusInfo(70), true);
		temp.vStat.addStatList("마공", new StatusInfo(70), true);
		temp.vStat.addStatList("독공", new StatusInfo(85), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.dStat.addStatList("추뎀", 5);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("애프터 크리스마스(5%)");
		temp.setCard(card);
		titleList.add(temp);
		
		card = new Card("전용보주", Item_rarity.NONE, CalculatorVersion.VER_1_0_a);
		card.dStat.addStatList("추뎀", 3);
		card.addPart(Equip_part.TITLE);
		temp = (Title) temp.clone();
		temp.setName("애프터 크리스마스(3%)");
		temp.setCard(card);
		titleList.add(temp);
		
		//베히모스
		temp = new Title("해적왕의 금은", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("물공", new StatusInfo(75), true);
		temp.vStat.addStatList("마공", new StatusInfo(75), true);
		temp.vStat.addStatList("독공", new StatusInfo(75), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		titleList.add(temp);
		temp = new Title("해적왕의 보화", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(100), true);
		temp.vStat.addStatList("지능", new StatusInfo(100), true);
		temp.vStat.addStatList("독공", new StatusInfo(75), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		titleList.add(temp);
		
		//시계태엽
		temp = new Title("황금의 시계 태엽", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(30), true);
		temp.vStat.addStatList("지능", new StatusInfo(30), true);
		temp.vStat.addStatList("물공", new StatusInfo(50), true);
		temp.vStat.addStatList("마공", new StatusInfo(50), true);
		temp.vStat.addStatList("독공", new StatusInfo(75), true);
		temp.vStat.addStatList("모속강", new ElementInfo(10), true);
		titleList.add(temp);
		temp = new Title("강철의 시계 태엽", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("물공", new StatusInfo(70), true);
		temp.vStat.addStatList("마공", new StatusInfo(70), true);
		temp.vStat.addStatList("독공", new StatusInfo(75), true);
		temp.vStat.addStatList("모속강", new ElementInfo(10), true);
		titleList.add(temp);
		temp = new Title("백은의 시계 태엽", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(70), true);
		temp.vStat.addStatList("지능", new StatusInfo(70), true);
		temp.vStat.addStatList("독공", new StatusInfo(75), true);
		temp.vStat.addStatList("모속강", new ElementInfo(10), true);
		titleList.add(temp);
		
		//2014 대박기원
		temp = new Title("2014 대박기원 [화]", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(33), true);
		temp.vStat.addStatList("지능", new StatusInfo(33), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("화속강", new ElementInfo(22), true);
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		titleList.add(temp);
		temp = new Title("2014 대박기원 [수]", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(33), true);
		temp.vStat.addStatList("지능", new StatusInfo(33), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("수속강", new ElementInfo(22), true);
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		titleList.add(temp);
		temp = new Title("2014 대박기원 [명]", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(33), true);
		temp.vStat.addStatList("지능", new StatusInfo(33), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("명속강", new ElementInfo(22), true);
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		titleList.add(temp);
		temp = new Title("2014 대박기원 [암]", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("힘", new StatusInfo(33), true);
		temp.vStat.addStatList("지능", new StatusInfo(33), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("암속강", new ElementInfo(22), true);
		temp.dStat.addStatList("힘", new StatusInfo(25), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(25), false, true);
		titleList.add(temp);
		
		//사도의 후예
		temp = new Title("사도의 후예", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(29), true);
		temp.vStat.addStatList("지능", new StatusInfo(32), true);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(3));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(3));
		temp.dStat.addStatList("힘", new StatusInfo(30), false, true);
		temp.dStat.addStatList("지능", new StatusInfo(30), false, true);
		temp.dStat.addStatList("추뎀", new StatusInfo(7), false, true);
		titleList.add(temp);
		
		//70렙
		temp = new Title("나는 70레벨이다", rarity, CalculatorVersion.VER_1_0_a);
		temp.vStat.addStatList("힘", new StatusInfo(22));
		temp.vStat.addStatList("지능", new StatusInfo(22));
		titleList.add(temp);
		//퍼만
		temp = new Title("육식주의자", rarity, CalculatorVersion.VER_1_0_c);
		temp.vStat.addStatList("모속강", new ElementInfo(12));
		titleList.add(temp);
		
	}
}
