package dnf_calculator;

public class Calculator_test {

	public static void main(String[] args)
	{
		Status charStat = new Status();
		Status.PublicStatus publicStat = charStat.new PublicStatus();
		
		try{
			publicStat.setElementStat("화속", 250, true);
			publicStat.setElementStat("수속", 120, true);
			publicStat.setElementStat("암속", 300, false);
		
			publicStat.setStat("무기물공", 988+270+20+40+50+110+34);
			publicStat.setStat("물리방무", 500);
			publicStat.setStat("독공", 2350-449);
			publicStat.setStat("재련독공", 449);
		
			publicStat.setStat("고정물방깍", 12000);
			publicStat.setDoubleStat("퍼물방깍_스킬", 27.0);
			publicStat.setStat("퍼물방깍_템", 10);
		
			publicStat.setStat("힘", 2780);
			publicStat.setStat("힘뻥", 10);
			publicStat.setStat("증뎀", 60);
			publicStat.setStat("크증뎀", 30);
			publicStat.setStat("스증", 10);
			publicStat.setStat("추뎀", 35);
		
			publicStat.setDoubleStat("증뎀버프", 224);
			publicStat.setDoubleStat("크증뎀버프", 1);
			publicStat.setDoubleStat("증뎀버프", 224);
		
			publicStat.setDoubleStat("화속추", 1);
			publicStat.setDoubleStat("수속추", 2);
			publicStat.setDoubleStat("명속추", 3);
			publicStat.setDoubleStat("암속추", 4);
		
			publicStat.setDoubleStat("화속깍", 20);
			publicStat.setDoubleStat("수속깍", 20);
			publicStat.setDoubleStat("명속깍", 20);
			publicStat.setDoubleStat("암속깍", 20);
			publicStat.setDoubleStat("투함포항", 12);
			
			publicStat.setDoubleStat("물크", 80);
			publicStat.setDoubleStat("백물크", 50);
			publicStat.setDoubleStat("크리저항", 5);
			publicStat.setDoubleStat("물공마스터리", 20);
			publicStat.setDoubleStat("독공뻥", 15);
			publicStat.setDoubleStat("물공마스터리2", 1);
			
			charStat.setStatus(publicStat);
			Char character = new Char(charStat, 86);
			
			Mon object = new Mon(new Status());
			object.setBooleanStat(Monster_StatList.BACKATK, true);
			object.setBooleanStat(Monster_StatList.COUNTER, true);
			object.setStat(Monster_StatList.DIFFICULTY, DefenceIgnorePenalty.ANTON_NOR);
			object.setStat(Monster_StatList.FIRE_RESIST, 40);
			object.setStat(Monster_StatList.DEFENSIVE_PHY, 135000);
			object.setStat(Monster_StatList.LEVEL, 115);
			object.setStat(Monster_StatList.TYPE, MonsterType.BOSS);
			
			long percentD=Calculator.percentDamage_physical(10000, object, character, 1);
			long fixD=Calculator.fixedDamage_physical(100000, 2500, object, character, 1);
			
			System.out.println("퍼뎀 : "+percentD);
			System.out.println("고뎀 : "+fixD);
		}
		catch(StatusTypeMismatch | UndefinedStatusKey e)
		{
			e.printStackTrace();
		}
		
	}
}

class Mon extends Monster
{
	public Mon(Status stat) {
		super(stat);
	}
	
}
