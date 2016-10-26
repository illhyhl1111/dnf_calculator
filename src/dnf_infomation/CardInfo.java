package dnf_infomation;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Card;

public class CardInfo {
	public static void getInfo(LinkedList<Card> cardList)
	{
		Card temp;
		Item_rarity rarity;
		
		////////// 무기, 상의, 하의
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//아그네스
		temp = new Card("찬란한 불꽃의 아그네스 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", new StatusInfo(50));
		temp.vStat.addStatList("무기물공", new StatusInfo(20));
		cardList.add(temp);
		
		//쿠로
		temp = new Card("강완의 쿠로 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.ROBE);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		cardList.add(temp);
		
		//로크
		temp = new Card("흑화의 로크 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", new StatusInfo(45));
		temp.vStat.addStatList("지능", new StatusInfo(45));
		cardList.add(temp);
		
		temp = new Card("임시 물공 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("물공", new StatusInfo(45), true);
		cardList.add(temp);
		
		///////////어깨, 벨트, 신발
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//노블보주
		temp = new Card("노블스카이 황옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.addPart(Equip_part.BELT);
		temp.addPart(Equip_part.SHOES);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5));
		cardList.add(temp);
		
		temp = new Card("노블스카이 녹옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.addPart(Equip_part.BELT);
		temp.addPart(Equip_part.SHOES);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5));
		cardList.add(temp);
		
		
		//////레어
		rarity = Item_rarity.RARE;
		
		//케인보주
		temp = new Card("케인 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(9), true);
		cardList.add(temp);
		temp = new Card("임시 물크 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(100), true);
		cardList.add(temp);
		
		//////////// 악세사리
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//진하메타
		temp = new Card("진 : 하이퍼 메카 타우 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", new ElementInfo(20));
		cardList.add(temp);
		
		temp = new Card("임시 속강 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", new ElementInfo(20), true);
		temp.vStat.addStatList("수속", new ElementInfo(20), true);
		temp.vStat.addStatList("명속", new ElementInfo(20), true);
		temp.vStat.addStatList("암속", new ElementInfo(20), true);
		cardList.add(temp);
		
		
		////////////// 보장
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//노블보주
		temp = new Card("노블스카이 홍옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.AIDEQUIPMENT);
		temp.vStat.addStatList("무기물공", new StatusInfo(34));
		temp.vStat.addStatList("독공", new StatusInfo(42));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(2));
		cardList.add(temp);
		
		temp = new Card("노블스카이 청옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.AIDEQUIPMENT);
		temp.vStat.addStatList("무기마공", new StatusInfo(34));
		temp.vStat.addStatList("독공", new StatusInfo(42));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(2));
		cardList.add(temp);
		
		
		////////// 마법석
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//심카
		temp = new Card("안톤의 심장 보주(노작)", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.vStat.addStatList("모속강", new StatusInfo(12));
		cardList.add(temp);
		
		temp = new Card("안톤의 심장 보주(풀업)", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.vStat.addStatList("모속강", new StatusInfo(15), true);
		cardList.add(temp);
		
		
		///////////칭호
		/////레어
		rarity = Item_rarity.RARE;
		
		//이름뭐더라
		temp = new Card("황룡의 백옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.TITLE);
		temp.vStat.addStatList("모속강", new StatusInfo(3));
		cardList.add(temp);
	}
}
