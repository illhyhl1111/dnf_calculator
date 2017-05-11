package dnf_calculator;

import java.util.LinkedList;

import dnf_InterfacesAndExceptions.ItemNotFoundedException;
import dnf_InterfacesAndExceptions.Job;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_class.Skill;
import dnf_infomation.GetDictionary;

public class TrackableStatus extends Status {
	private static final long serialVersionUID = -6825809934060880378L;
	public final Skill trackingSkill;
	public final LinkedList<TrackRecord>[] trackList;
	public final LinkedList<TrackRecord> skillLevelList;
	public final LinkedList<TrackRecord> skillIncreaseList;
	
	@SuppressWarnings("unchecked")
	public TrackableStatus(Job job, int level, Skill skill) throws ItemNotFoundedException{
		super();
		trackingSkill=skill;
		LinkedList<?>[] tempList = new LinkedList<?>[StatList.STAT_END+1];
		for(int i=0; i<StatList.STAT_END+1; i++)
			tempList[i] = new LinkedList<TrackRecord>();
		trackList = (LinkedList<TrackRecord>[]) tempList;
		skillLevelList = new LinkedList<TrackRecord>();
		skillIncreaseList = new LinkedList<TrackRecord>();
		GetDictionary.charDictionary.getBasicStat(job, level).addListToStat(this, "기본 스탯");
	}
	
	@Override
	public void trackStat(int statNum, AbstractStatusInfo stat2, String source)
	{
		addStat(statNum, stat2);
		try {
			switch(statNum){
			case StatList.SKILL:
				SkillStatusInfo stat3 = (SkillStatusInfo)stat2;
				if(stat3.getStatToString().equals(trackingSkill.getName())){
					if(Double.compare(stat3.getStatToDouble(), 0)!=0){
						TrackRecord temp = new TrackRecord(statNum, stat3.getStatToDouble(), source, false);
						skillLevelList.add(temp);
					}
					if(Double.compare(stat3.getIncrease(), 0)!=0){
						TrackRecord temp = new TrackRecord(statNum, stat3.getIncrease(), source, true);
						skillIncreaseList.add(temp);
					}
				}
				break;
			case StatList.SKILL_RANGE:
				SkillRangeStatusInfo stat4 = (SkillRangeStatusInfo)stat2;
				if(trackingSkill.isInRange(stat4.getStartRange(), stat4.getEndRange(), stat4.getTP())){
					TrackRecord temp = new TrackRecord(statNum, stat4.getStatToDouble(), source, false);
					skillLevelList.add(temp);
				}
				break;
			case StatList.DAM_SKILL: case StatList.BUF_INC: case StatList.BUF_CRT:				//복리중첩항
			case StatList.MAST_PHY: case StatList.MAST_MAG: case StatList.MAST_IND: 
			case StatList.WEP_NODEF_MAG_INC: case StatList.WEP_NODEF_PHY_INC:
				TrackRecord temp = new TrackRecord(statNum, stat2.getStatToDouble(), source, true);
				trackList[statNum].add(temp);
				break;
			case StatList.DAM_INC: case StatList.DAM_CRT:										//중첩불가항
			case StatList.DAM_INC_BACK: case StatList.DAM_CRT_BACK:
			case StatList.DEF_DEC_IGN:
				if(trackList[statNum].isEmpty())
					trackList[statNum].add(new TrackRecord(statNum, stat2.getStatToDouble(), source, false));
				else if(stat2.getStatToDouble()>trackList[statNum].getFirst().strength){
					trackList[statNum].getFirst().strength=0;
					trackList[statNum].addFirst(new TrackRecord(statNum, stat2.getStatToDouble(), source, false));
				}
				else
					trackList[statNum].add(new TrackRecord(statNum, 0, source, false));
				break;
			case StatList.CONVERSION_NOPHY: case StatList.CONVERSION_NOMAG:						//부울스탯
				break;
			default:
				trackList[statNum].add(new TrackRecord(statNum, stat2.getStatToDouble(), source, false));
			}	
		}catch(StatusTypeMismatch e){
			e.printStackTrace();
		}
	}
}
