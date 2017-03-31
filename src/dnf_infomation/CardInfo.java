package dnf_infomation;
import java.util.LinkedList;

import dnf_InterfacesAndExceptions.CalculatorVersion;
import dnf_InterfacesAndExceptions.Equip_part;
import dnf_InterfacesAndExceptions.Item_rarity;
import dnf_calculator.DoubleStatusInfo;
import dnf_calculator.StatusInfo;
import dnf_class.Card;
import dnf_class.SkillCard;

public class CardInfo {
	public static void getInfo(LinkedList<Card> cardList)
	{
		Card temp;
		Item_rarity rarity;
		
		temp = new Card("보주 제거", Item_rarity.NONE, CalculatorVersion.VER_1_1_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.addPart(Equip_part.SHOULDER);
		temp.addPart(Equip_part.BELT);
		temp.addPart(Equip_part.SHOES);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.addPart(Equip_part.AIDEQUIPMENT);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.addPart(Equip_part.EARRING);
		cardList.add(temp);
		
		////////// 무기, 상의, 하의
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//메델
		temp = new Card("심연의 메델 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", new StatusInfo(50));
		temp.vStat.addStatList("무기물공", new StatusInfo(20));
		cardList.add(temp);
		//진:디리지에
		temp = new Card("진 : 디리지에의 환영 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("지능", new StatusInfo(50));
		temp.vStat.addStatList("무기마공", new StatusInfo(20));
		cardList.add(temp);
		//길던
		temp = new Card("타오르는 분노의 왕자 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.vStat.addStatList("화속강", 12, true);
		cardList.add(temp);
		temp = new Card("얼어붙은 슬픔의 왕녀 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.vStat.addStatList("수속강", 12, true);
		cardList.add(temp);
		//로크
		temp = new Card("흑화의 로크 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", new StatusInfo(45));
		temp.vStat.addStatList("지능", new StatusInfo(45));
		cardList.add(temp);
		//칼바리
		temp = new Card("철완의 공주 칼바리 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("지능", 30, true);
		temp.vStat.addStatList("무기마공", 20, true);
		cardList.add(temp);
		//골고타
		temp = new Card("거완의 왕자 골고타 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", 30, true);
		temp.vStat.addStatList("무기물공", 20, true);
		cardList.add(temp);
		//범용
		temp = new Card("물공 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("물공", 45, true);
		cardList.add(temp);
		temp = new Card("마공 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("마공", 45, true);
		cardList.add(temp);
		temp = new Card("힘 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("힘", 55, true);
		cardList.add(temp);
		temp = new Card("지능 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.WEAPON);
		temp.addPart(Equip_part.ROBE);
		temp.addPart(Equip_part.TROUSER);
		temp.vStat.addStatList("지능", 55, true);
		cardList.add(temp);
		
		/////상의
		//쿠로
		temp = new Card("강완의 쿠로 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.ROBE);
		temp.vStat.addStatList("힘", new StatusInfo(65));
		cardList.add(temp);
		//나잘로
		temp = new Card("진 : 주술사 나잘로 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.ROBE);
		temp.vStat.addStatList("지능", new StatusInfo(65));
		cardList.add(temp);
		
		///////////어깨, 벨트, 신발
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//노블보주
		temp = new Card("노블스카이 황옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.addPart(Equip_part.BELT);
		temp.addPart(Equip_part.SHOES);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(5), true);
		cardList.add(temp);
		
		temp = new Card("노블스카이 녹옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.addPart(Equip_part.BELT);
		temp.addPart(Equip_part.SHOES);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(5), true);
		cardList.add(temp);
		//아이리스
		temp = new Card("아이리스 포츈싱어 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(9), true);
		cardList.add(temp);
		//콰트로
		temp = new Card("콰트로 마누스 mark-2 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(8), true);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(8), true);
		cardList.add(temp);
		//장총
		temp = new Card("진 : 장총 맥스 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(10));
		cardList.add(temp);
		//아그네스
		temp = new Card("찬란한 불꽃의 아그네스 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("마크", new DoubleStatusInfo(10));
		cardList.add(temp);

		rarity = Item_rarity.RARE;
		//케인보주
		temp = new Card("케인 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.SHOULDER);
		temp.vStat.addStatList("물크", new DoubleStatusInfo(9), true);
		cardList.add(temp);
		
		
		//////////// 악세사리
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//진하메타
		temp = new Card("진 : 하이퍼 메카 타우 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", 20, true);
		cardList.add(temp);
		//미스트랄
		temp = new Card("더 세븐 미스트랄 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("수속", 20, true);
		cardList.add(temp);
		//호루스
		temp = new Card("빛의 우상 호루스 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("명속", 20, true);
		cardList.add(temp);
		//크레스
		temp = new Card("흑연의 크레스 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("암속", 20, true);
		cardList.add(temp);
		//노르닐
		temp = new Card("초월의 노르닐 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("모속", 18, true);
		cardList.add(temp);
		//하부브
		temp = new Card("파탄의 하부브 보주", rarity, CalculatorVersion.VER_1_0_d);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", 20, true);
		temp.vStat.addStatList("암속", 20, true);
		cardList.add(temp);
		//그라골
		temp = new Card("그라골 보주", rarity, CalculatorVersion.VER_1_0_d);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("수속", 20, true);
		temp.vStat.addStatList("명속", 20, true);
		cardList.add(temp);
		//야신
		temp = new Card("달빛을 걷는자 야신 보주", rarity, CalculatorVersion.VER_1_0_d);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("수속", 20, true);
		temp.vStat.addStatList("암속", 20, true);
		cardList.add(temp);
		//악몽의 네르베
		temp = new Card("악몽의 네르베 보주", rarity, CalculatorVersion.VER_1_0_d);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("명속", 20, true);
		temp.vStat.addStatList("암속", 20, true);
		cardList.add(temp);
		//아이언 비스트
		temp = new Card("아이언 비스트 보주", rarity, CalculatorVersion.VER_1_0_d);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", 20, true);
		temp.vStat.addStatList("명속", 20, true);
		cardList.add(temp);
		//크레이브
		temp = new Card("염화의 크레이브 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("화속", 16);
		temp.vStat.addStatList("독공", 10);
		cardList.add(temp);
		//에게느
		temp = new Card("흡수의 에게느 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("독공", 64, true);
		cardList.add(temp);
		//스컬나이트
		temp = new Card("스컬나이트 보주", Item_rarity.RARE, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.BRACELET);
		temp.addPart(Equip_part.NECKLACE);
		temp.addPart(Equip_part.RING);
		temp.vStat.addStatList("암속", 9, true);
		temp.vStat.addStatList("독공", 10, true);
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
		
		//무제
		temp = new Card("아가멤논의 역천사 보주", Item_rarity.RARE, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.AIDEQUIPMENT);
		temp.vStat.addStatList("무기물공", new StatusInfo(12));
		temp.vStat.addStatList("물크", new DoubleStatusInfo(1));
		cardList.add(temp);
		temp = new Card("아가멤논의 지천사 보주", Item_rarity.RARE, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.AIDEQUIPMENT);
		temp.vStat.addStatList("무기마공", new StatusInfo(12));
		temp.vStat.addStatList("마크", new DoubleStatusInfo(1));
		cardList.add(temp);
		
		////////// 마법석
		/////유니크
		rarity = Item_rarity.UNIQUE;
		
		//심카		
		temp = new Card("안톤의 심장 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.vStat.addStatList("모속강", new StatusInfo(15), true);
		cardList.add(temp);
		//네이트람
		temp = new Card("네이트람의 붉은 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.vStat.addStatList("모속강", new StatusInfo(5));
		temp.vStat.addStatList("물크", 1);
		cardList.add(temp);
		temp = new Card("네이트람의 푸른 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.MAGICSTONE);
		temp.vStat.addStatList("모속강", new StatusInfo(5));
		temp.vStat.addStatList("마크", 1);
		cardList.add(temp);
		
		///////////칭호
		rarity = Item_rarity.UNIQUE;
		temp = new Card("황룡의 홍옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.TITLE);
		temp.vStat.addStatList("물공", new StatusInfo(10));
		cardList.add(temp);
		temp = new Card("황룡의 청옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.TITLE);
		temp.vStat.addStatList("마공", new StatusInfo(10));
		cardList.add(temp);
		temp = new Card("청룡의 흑옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.TITLE);
		temp.vStat.addStatList("독공", new StatusInfo(15));
		cardList.add(temp);
		
		rarity = Item_rarity.RARE;
		temp = new Card("황룡의 백옥 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.TITLE);
		temp.vStat.addStatList("모속강", new StatusInfo(3));
		cardList.add(temp);
		
		//귀걸이
		rarity = Item_rarity.LEGENDARY;
		temp = new Card("건설자 루크(어둠) 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.EARRING);
		temp.vStat.addStatList("힘", new StatusInfo(50));
		temp.vStat.addStatList("지능", new StatusInfo(50));
		cardList.add(temp);

		temp = new Card("건설자 루크(빛) 보주", rarity, CalculatorVersion.VER_1_0_a);
		temp.addPart(Equip_part.EARRING);
		temp.vStat.addStatList("힘", new StatusInfo(125), true);
		temp.vStat.addStatList("지능", new StatusInfo(125), true);
		temp.explanation.add("이걸 진짜 풀업하라고 진지하게 만든건가여?");
		cardList.add(temp);
		
		temp = new SkillCard("스킬 보주", rarity, CalculatorVersion.VER_1_1_b);
		temp.addPart(Equip_part.SKILLTITLE);
		cardList.add(temp);
	}
}
