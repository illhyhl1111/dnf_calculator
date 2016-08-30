package dnf_InterfacesAndExceptions;

public enum SetName
{
	NONE("세트 없음"),
	DARKGOTH("다크고스 세트"), CHAMELEON("무음 카멜레온 세트"), SUBMARINE("서브마린 볼케이노 세트"), DARKHOLE("미지의 다크홀 세트"), INFINITY("인피니티 레퀴엠 세트"),
	BURNINGSPELL("불타는 마력 세트"), DROPPER("엘리멘탈 드롭퍼 세트"), TACTICAL("웨슬리의 전술 세트"), ASSASSIN("암살자의 마음가짐 세트"), NATURALGARDIAN("자연의 수호자 세트"),
	EYESHIELD("아이실드 러데이니언 세트"), SPIDERQUEEN("거미 여왕의 숨결 세트"), FORBIDDENCONTRACT("금지된 계약 세트"), MAELSTORM("마력의 소용돌이 세트"), FULLPLATE("풀 플레이트 아머 세트"),
	SUPERSTAR("슈퍼 스타 세트"), REFINEDSPELL("정제된 이계의 마석 세트"), ICEQUEEN("얼음 공주의 숨결 세트"), HUGEFORM("거대한 형상의 기운 세트"),
	GREATGLORY("고대 왕국의 기사 세트"), GRACIA("그라시아 가문의 상징 세트"), DEVASTEDGRIEF("애끓는 비탄 세트"), BURIEDSCREAM("파묻힌 비명의 영혼 세트"),
	ROOTOFDISEASE("질병의 근원 세트"), ROMANTICE("황야의 로멘티스트 세트"), CURSEOFSEAGOD("해신의 저주를 받은 세트"),
	RELIC_80("유물 : 오로바스의 세라믹 세트"), RELIC_85("유물 : 정화된 오로바스의 세라믹 세트"),
	HOLYRELIC_80("성물 : 오로바스의 세라믹 세트"), HOLYRELIC_85("성물 : 정화된 오로바스의 세라믹 세트");
	
	String name;
	
	SetName(String name)
	{
		this.name=name;
	}
	
	public String getName() {return name;}
}