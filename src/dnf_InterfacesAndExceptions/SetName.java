package dnf_InterfacesAndExceptions;

import java.io.Serializable;

public enum SetName implements Serializable
{
	NONE("세트 없음"),
	DARKGOTH("다크고스 세트"), CHAMELEON("무음 카멜레온 세트"), SUBMARINE("서브마린 볼케이노 세트"), DARKHOLE("미지의 다크홀 세트"), INFINITY("인피니티 레퀴엠 세트"),
	BURNINGSPELL("불타는 마력 세트"), DROPPER("엘리멘탈 드롭퍼 세트"), TACTICAL("웨슬리의 전술 세트"), ASSASSIN("암살자의 마음가짐 세트"), NATURALGARDIAN("자연의 수호자 세트"),
	EYESHIELD("아이실드 러데이니언 세트"), SPIDERQUEEN("거미 여왕의 숨결 세트"), FORBIDDENCONTRACT("금지된 계약 세트"), MAELSTORM("마력의 소용돌이 세트"), FULLPLATE("풀 플레이트 아머 세트"),
	
	SUPERSTAR("슈퍼 스타 세트"), REFINEDSTONE("정제된 이계의 마석 세트"), ICEQUEEN("얼음 공주의 숨결 세트"), HUGEFORM("거대한 형상의 기운 세트"),
	
	GREATGLORY("고대 왕국의 기사 세트"), GRACIA("그라시아 가문의 상징 세트"), DEVASTEDGRIEF("애끓는 비탄 세트"), BURIEDSCREAM("파묻힌 비명의 영혼 세트"),
	ROOTOFDISEASE("질병의 근원 세트"), ROMANTICE("황야의 로멘티스트 세트"), CURSEOFSEAGOD("해신의 저주를 받은 세트"),
	RELIC_80("유물 : 오로바스의 세라믹 세트"), RELIC_85("유물 : 정화된 오로바스의 세라믹 세트"),
	HOLYRELIC_80("성물 : 오로바스의 세라믹 세트"), HOLYRELIC_85("성물 : 정화된 오로바스의 세라믹 세트"),
	
	OGGEILL("오기일의 꽃 세트"), GESPENST("게슈펜슈트의 환각 세트"), BLACKFORMAL("마계의 정중한 신사 세트"), FIENDVENATOR("핀드 베나토르 세트"), GOLDENARMOR("찬란한 명예의 상징 세트"),
	SUPERCONTINENT("초대륙의 붕괴 세트"), ANCIENTWAR("고대전쟁의 여신 세트"), NAGARAJA("나가라자의 탐식 세트"), CENTURYONHERO("센츄리온 히어로 세트"), SEVENSINS("타락의 칠죄종 세트"),
	
	SKYTRAVELER("하늘의 여행자 세트"), ECSTATICSENCE("오감의 황홀경 세트"),
	
	REAL_PROFIGHTER_FABRIC("진 : 프로 싸움꾼의 천 방어구 세트"), REAL_PROFIGHTER_LEATHER("진 : 프로 싸움꾼의 가죽 방어구 세트"), REAL_PROFIGHTER_MAIL("진 : 프로 싸움꾼의 경갑 방어구 세트"), 
	REAL_PROFIGHTER_HARMOR("진 : 프로 싸움꾼의 중갑 방어구 세트"),  REAL_PROFIGHTER_PLATE("진 : 프로 싸움꾼의 판금 방어구 세트"), GODOFFIGHT("무신의 정수가 담긴 세트"),
	REAL_PROFIGHTER_ACCESSORY("진 : 프로 싸움꾼의 악세서리 세트"), REAL_PROFIGHTER_SPECIALEQUIP("진 : 프로 싸움꾼의 특수장비 세트"),
	GUILDACCESSORY_FIRE("타오르는 불꽃의 분노 세트"), GUILDACCESSORY_WATER("얼어붙은 냉기의 슬픔 세트"),
	
	TACITCONSTRUCTOR("무언의 건설자 세트"),
	
	RAREAVATAR("레어 아바타 세트"), AVATAR("상급 아바타 세트");
	
	String name;
	
	SetName(String name)
	{
		this.name=name;
	}
	
	public String getName() {return name;}
	
	public int compare(SetName arg)
	{
		if(arg==this) return 0;
		else if(arg==NONE) return 1;
		else if(this==NONE) return -1;
		
		return name.compareTo(arg.getName()); 
	}
}