package dnf_InterfacesAndExceptions;

public interface Monster_StatList
{
	int COUNTER=1000; int BACKATK=1001;																		//카운터여부, 백어택여부
	int FIRE_RESIST=1002; int WATER_RESIST=1003; int LIGHT_RESIST=1004; int DARKNESS_RESIST=1005;			// 화속저, 수속저, 명속저, 암속저
	int DIFFICULTY=1006;  																					// 난이도, 
	int DEFENSIVE_PHY=1007; int DEFENSIVE_MAG=1008;															// 물방, 마방
	int LEVEL=1009;	int TYPE=1010;																			// 몹 레벨, 등급
	
	int DEFENCE_LIMIT=1011;																					// 방깍하한선
	int HP=1012;																							// 체력
	
	int STARTNUM=1000; int STATNUM=13;	int BOOLNUM=2; int INTNUM=9; int DOUBLENUM=1; int LONGNUM=1; 	//StatList와 겹치지 않는 시작번호, 스탯개수, bool형 스탯 개수
}
