package dnf_calculator;

import dnf_InterfacesAndExceptions.DefenceIgnorePenalty;
import dnf_InterfacesAndExceptions.JobList;
import dnf_InterfacesAndExceptions.MonsterType;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_class.Monster;
import dnf_class.Characters;

public class Calculator_test {

	public static void main(String[] args)
	{
		Status charStat = new Status();
		
		try{
			charStat.setElementStat("화속", 250, true);
			charStat.setElementStat("수속", 120, true);
			charStat.setElementStat("암속", 300, false);
		
			charStat.setStat("무기물공", 988+270+20+40+50+110+34);
			charStat.setStat("물리방무", 500);
			charStat.setStat("독공", 2350-449);
			charStat.setStat("재련독공", 449);
		
			charStat.setStat("고정물방깍", 12000);
			charStat.setDoubleStat("퍼물방깍_스킬", 27.0);
			charStat.setStat("퍼물방깍_템", 10);
		
			charStat.setStat("힘", 2780);
			charStat.setStat("힘뻥", 10);
			charStat.setStat("증뎀", 60);
			charStat.setStat("크증뎀", 30);
			charStat.setDoubleStat("스증", 10.0);
			charStat.setStat("추뎀", 35);
		
			charStat.setDoubleStat("증뎀버프", 224);
			charStat.setDoubleStat("크증뎀버프", 1);
			charStat.setDoubleStat("증뎀버프", 224);
		
			charStat.setStat("화속추", 1);
			charStat.setStat("수속추", 2);
			charStat.setStat("명속추", 3);
			charStat.setStat("암속추", 4);
		
			charStat.setStat("화속깍", 20);
			charStat.setStat("수속깍", 20);
			charStat.setStat("명속깍", 20);
			charStat.setStat("암속깍", 20);
			charStat.setStat("투함포항", 12);
			
			charStat.setDoubleStat("물크", 80);
			charStat.setDoubleStat("백물크", 50);
			charStat.setDoubleStat("크리저항", 5);
			charStat.setDoubleStat("물리마스터리", 20);
			charStat.setDoubleStat("독공뻥", 15);
			charStat.setDoubleStat("물리마스터리2", 1);
			
			Characters character = new Characters(86, JobList.LAUNCHER_F);
			character.villageStatus=charStat;
			
			Monster object = new Monster(new Status());
			object.setBooleanStat(Monster_StatList.BACKATK, true);
			object.setBooleanStat(Monster_StatList.COUNTER, true);
			object.setStat(Monster_StatList.DIFFICULTY, DefenceIgnorePenalty.ANTON_NOR);
			object.setStat(Monster_StatList.FIRE_RESIST, 40);
			object.setStat(Monster_StatList.DEFENSIVE_PHY, 135000);
			object.setStat(Monster_StatList.LEVEL, 115);
			object.setStat(Monster_StatList.TYPE, MonsterType.BOSS);
			
			long percentD=Calculator.percentDamage_physical(10000, object, character, 1);
			long fixD=Calculator.fixedDamage_physical(100000, 1000, object, character, 1);
			
			System.out.println("퍼뎀 : "+percentD);
			System.out.println("고뎀 : "+fixD);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
		
	}
}

class TempChar
{
	Status villageStatus;
	int level;
	public TempChar(Status stat, int lev)
	{
		villageStatus=stat;
		level=lev;
	}
}
