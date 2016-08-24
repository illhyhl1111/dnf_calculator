package dnf_InterfacesAndExceptions;

public interface StatList									// 스탯 종류에 붙는 고유한 식별번호
{
	int ELEM_FIRE=0; int  ELEM_WATER=1; int  ELEM_LIGHT=2; int  ELEM_DARKNESS=3;											// 화, 수, 명, 암속성
	
	int ELEMENTNUM_START = 0;
	int ELEMENTNUM = 4;														// 총 속성 개수
	int ELEMENTNUM_END = ELEMENTNUM_START+ELEMENTNUM-1;
	///////////////////////////////////////
	
	int INTNUM_START = ELEMENTNUM_END+1;
	
	int WEP_PHY=INTNUM_START+0; int WEP_MAG=INTNUM_START+1; int  WEP_NODEF_PHY=INTNUM_START+2; int WEP_NODEF_MAG=INTNUM_START+3;	// 무기물공, 무기마공, 방무물공, 방무마공
	int WEP_IND=INTNUM_START+4; int WEP_IND_REFORGE=INTNUM_START+5; 																//독공, 재련독공
	int DEF_DEC_FIXED_PHY=INTNUM_START+6; int  DEF_DEC_PERCENT_PHY_ITEM=INTNUM_START+7;												// 고정물방깍, (아이템)퍼센트물방깍,
	int DEF_DEC_FIXED_MAG=INTNUM_START+8; int DEF_DEC_PERCENT_MAG_ITEM=INTNUM_START+9;												// 고정마방깍, (아이템)퍼센트마방깍
	int STR=INTNUM_START+10; int INT=INTNUM_START+11; int STA=INTNUM_START+12; int WILL=INTNUM_START+13;							// 힘, 지능, 체력, 정신력
	int STR_INC=INTNUM_START+14; int INT_INC=INTNUM_START+15;																		// 힘%증가, 지능%증가
	int DAM_INC=INTNUM_START+16; int  DAM_CRT=INTNUM_START+17; int  DAM_ADD=INTNUM_START+18; 										// 증뎀, 크증뎀, 추뎀합
	int DAM_INC_BACK=INTNUM_START+19; int DAM_CRT_BACK=INTNUM_START+20; int DAM_ADD_BACK=INTNUM_START+21;							//(조건부) 백어택 증뎀, 백어택 크증뎀, 백어택 추뎀
	int DAM_ADD_FIRE=INTNUM_START+22; int DAM_ADD_WATER=INTNUM_START+23;
	int DAM_ADD_LIGHT=INTNUM_START+24; int DAM_ADD_DARKNESS=INTNUM_START+25;														// 화속추뎀, 수속추뎀, 명속추뎀, 암속추뎀 (높은속성추뎀 필요)
	int ELEM_FIRE_DEC=INTNUM_START+26; int ELEM_WATER_DEC=INTNUM_START+27;
	int ELEM_LIGHT_DEC=INTNUM_START+28; int ELEM_DARKNESS_DEC=INTNUM_START+29;														// 화속저, 수속저, 명속저, 암속저
	int DAM_BUF=INTNUM_START+30;																									// 투함포 이그니스 공대버프
	int ELEM_ALL=INTNUM_START+31;																									// 모속강
	
	int INTNUM = 32;								// 총 int형 스탯 개수
	int INTNUM_END = INTNUM_START+INTNUM-1;
	///////////////////////////////////////
	
	int DOUBLENUM_START = INTNUM_END+1;
	
	int DAM_SKILL =DOUBLENUM_START+0;																				// 스증뎀합
	int DEF_DEC_PERCENT_PHY_SKILL=DOUBLENUM_START+1; int DEF_DEC_PERCENT_MAG_SKILL=DOUBLENUM_START+2;				//(스킬) 퍼센트물방깍, 퍼센트마방깍
	int CRT_PHY=DOUBLENUM_START+3; int CRT_MAG=DOUBLENUM_START+4; int CRT_LOW=DOUBLENUM_START+5;
	int CRT_BACK_PHY=DOUBLENUM_START+6; int CRT_BACK_MAG=DOUBLENUM_START+7;											// 물크, 마크, 크리저항감소, 백물크, 백마크
	int MAST_IND=DOUBLENUM_START+8; int MAST_PHY=DOUBLENUM_START+9; int MAST_MAG=DOUBLENUM_START+10;				// 물공마스터리, 마공마스터리, 독공%증가
	int MAST_PHY_2=DOUBLENUM_START+11; int MAST_MAG_2=DOUBLENUM_START+12;											//물공마스터리(종류 2), 마공마스터리(종류 2)
	int BUF_INC=DOUBLENUM_START+13; int BUF_CRT=DOUBLENUM_START+14;													//(스킬) 증뎀버프, 크증뎀버프
	
	int DOUBLENUM = 15;														// 총 double형 스탯 개수
	int DOUBLENUM_END = DOUBLENUM_START+DOUBLENUM-1;
	///////////////////////////////////////
	
	int STATNUM = ELEMENTNUM+INTNUM+DOUBLENUM;								// 총 스탯 개수
}