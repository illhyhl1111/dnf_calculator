package dnf_UI_32;

import java.util.LinkedList;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.DnFColor;
import dnf_InterfacesAndExceptions.Monster_StatList;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.UndefinedStatusKey;
import dnf_calculator.CalculateElement;
import dnf_calculator.Calculator;
import dnf_calculator.TrackRecord;
import dnf_calculator.TrackableStatus;
import dnf_class.Characters;
import dnf_class.Skill;
import dnf_class.SkillLevelInfo;
import dnf_infomation.GetDictionary;

public class CalculateProcessViewer extends Dialog {
	private Characters character;
	private Composite background;
	private TrackableStatus status;
	private Skill trackingSkill;
	private Group skillInfo;
	private Group elementInfo;
	private Group criticalInfo;
	private Group increaseInfo;
	private Group resultInfo;
	private Shell parent;
	
	private double fire_inc;
	private double water_inc;
	private double light_inc;
	private double darkness_inc;
	private double phy_crt_inc;
	private double mag_crt_inc;
	private double add_crt_inc;
	private double inc_result;
	private double phy_per;
	private double phy_fix;
	private double mag_per;
	private double mag_fix;
	private double subSkillDamage;
	
	public CalculateProcessViewer(Shell shell, Characters character){
		super(shell);
		parent=shell;
		this.character=character;
	}
	
	private void renew(Composite content){
		for(Control c : content.getChildren())
			c.dispose();
		subSkillDamage=0;
		
		character.trackStatus(true, trackingSkill);
		status = (TrackableStatus) character.dungeonStatus;

		ScrolledComposite resultScroll = new ScrolledComposite(content, SWT.H_SCROLL);
		resultScroll.setExpandHorizontal(true);
		resultScroll.setAlwaysShowScrollBars(true);
		ScrolledComposite skillScroll = new ScrolledComposite(content, SWT.H_SCROLL);
		skillScroll.setExpandHorizontal(true);
		skillScroll.setAlwaysShowScrollBars(true);
		
		setSkillInfo(content, skillScroll);
		FormData formData = new FormData(350, 425);
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(0, 10);
		skillScroll.setLayoutData(formData);
		
		resultInfo = new Group(resultScroll, SWT.NONE);
		resultInfo.setText("최종 결과");
		resultInfo.setLayout(new FormLayout());
	    formData = new FormData(1050, 425);
		formData.top = new FormAttachment(0, 0);
		formData.left = new FormAttachment(skillScroll, 10);
		resultScroll.setLayoutData(formData);
	
		setElementInfo(content);
		formData = new FormData(410, 290);
		formData.top = new FormAttachment(skillScroll, 0);
		formData.left = new FormAttachment(0, 10);
		elementInfo.setLayoutData(formData);
		
		setCriticalInfo(content);
		formData = new FormData(445, 290);
		formData.top = new FormAttachment(skillScroll, 0);
		formData.left = new FormAttachment(elementInfo, 10);
		criticalInfo.setLayoutData(formData);
		
		setIncreaseInfo(content);
		formData = new FormData(535, 325);
		formData.top = new FormAttachment(skillScroll, 0);
		formData.left = new FormAttachment(criticalInfo, 10);
		increaseInfo.setLayoutData(formData);
		
		setResultInfo(content);
		resultInfo.setSize(resultInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		resultScroll.setContent(resultInfo);
		resultScroll.setMinSize(resultInfo.getSize());

		skillInfo.setSize(skillInfo.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		skillScroll.setContent(skillInfo);
		skillScroll.setMinSize(skillInfo.getSize());
		
		Label notice = new Label(content, SWT.BORDER);
		notice.setText("※ 소숫점 버림과 오차에 의해 실제 딜과 계산기 딜, 계산과정의 딜에 소폭 차이가 생길 수 있습니다\n"
				+ "※ 계산기 버그로 의심되는 부분이 있을때는 스탯 정보와 계산과정을 확인해주세요. 대부분은 뭔가 잘못 생각한 부분이 있거나 실수에 의한 것입니다ㅜㅜ\n"
				+ "※ 계산기 버그가 확실하다면 제보 부탁드립니다. 이후 버전에서 수정해드리겠습니다.");
		formData = new FormData();
		formData.top = new FormAttachment(elementInfo, 5);
		formData.left = new FormAttachment(0, 10);
		notice.setLayoutData(formData);
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		trackingSkill=character.getRepresentSkill();
		
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FormLayout());
		content.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		content.setBackgroundMode(SWT.INHERIT_FORCE);
		background = content;
		
		renew(content);
		return content;
	}
	
	private void setSkillInfo(Composite content, Composite scroll){
		skillInfo = new Group(scroll, SWT.NONE);
		skillInfo.setText("스킬 정보");
		skillInfo.setLayout(new FormLayout());
		
		SkillLevelInfo levelInfo = trackingSkill.getCanonicalSkillLevelInfo(true, character.isBurning());
		
		Label icon = new Label(skillInfo, SWT.NONE);
		icon.setImage(GetDictionary.skillIconDictionary.get(trackingSkill.getName()));
		setFormData(icon, 0, 10, 0, 10);
		
		Combo skillCombo = new Combo(skillInfo, SWT.READ_ONLY);
		String[] items = new String[character.getTrackingSkillList().size()];
		int i=0;
		for(Skill skill : character.getTrackingSkillList())
			items[i++] = skill.getItemName();
		skillCombo.setItems(items);
		skillCombo.setText(trackingSkill.getName());
		setFormData(skillCombo, 0, 10, icon, 10);
		
		skillCombo.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(Skill s : character.characterInfoList.skillList){
					if(s.getName().equals(skillCombo.getText())){
						trackingSkill = s;
						renew(content);
						content.layout();
						break;
					}
				}
			}
		});
		
		Label levelInfoLabel = new Label(skillInfo, SWT.NONE);
		levelInfoLabel.setText("스킬 레벨 - ");
		levelInfoLabel.setFont(DnFColor.TEMP2);
		setFormData(levelInfoLabel, icon, 10, 0, 10);
		Composite levelComposite = new Composite(skillInfo, SWT.NONE);
		levelComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		LinkedList<TrackRecord> levelList = status.skillLevelList;
		levelList.addFirst(new TrackRecord("캐릭터 레벨", trackingSkill.getCharSkillLevel()));
		levelList.add(new TrackRecord('='));
		levelList.add(new TrackRecord("최종 레벨", trackingSkill.getSkillLevel(true, character.isBurning())));
		setNumberComposite(levelComposite, levelList, "스킬 레벨", '+');
		setFormData(levelComposite, icon, 10, levelInfoLabel, 10);
		
		boolean fire=false, water=false, light=false, darkness=false, none=false;
		boolean noPhy=false, noMag=false;
		switch(trackingSkill.element){
		case NONE: none=true; break;
		case FIRE: fire=true; break;
		case WATER: water=true; break;
		case LIGHT: light=true; break;
		case DARKNESS: darkness=true; break;
		case FIRE_LIGHT: fire=true; light=true; break;
		case WATER_LIGHT: water=true; light=true; break;
		case FIRE_DARKNESS: fire=true; darkness=true; break;
		case ALL: fire=true; water=true; light=true; darkness=true; break;
		}
		try {
			noPhy = (status.getEnabled(StatList.CONVERSION_NOPHY) && (levelInfo.hasMag_fix() || levelInfo.hasMag_per()));
			noMag = (status.getEnabled(StatList.CONVERSION_NOMAG) && (levelInfo.hasPhy_fix() || levelInfo.hasPhy_per()));
		} catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		
		Label containsFire = new Label(skillInfo, SWT.NONE);
		containsFire.setText("火");
		containsFire.setFont(DnFColor.TEMP3);
		containsFire.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		containsFire.setEnabled(fire);
		setFormData(containsFire, levelInfoLabel, 10, 0, 10);
		Label containsWater = new Label(skillInfo, SWT.NONE);
		containsWater.setText("水");
		containsWater.setFont(DnFColor.TEMP3);
		containsWater.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		containsWater.setEnabled(water);
		setFormData(containsWater, levelInfoLabel, 10, containsFire, 5);
		Label containsLight = new Label(skillInfo, SWT.NONE);
		containsLight.setText("明");
		containsLight.setFont(DnFColor.TEMP3);
		containsLight.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
		containsLight.setEnabled(light);
		setFormData(containsLight, levelInfoLabel, 10, containsWater, 5);
		Label containsDarkness = new Label(skillInfo, SWT.NONE);
		containsDarkness.setText("暗");
		containsDarkness.setFont(DnFColor.TEMP3);
		containsDarkness.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
		containsDarkness.setEnabled(darkness);
		setFormData(containsDarkness, levelInfoLabel, 10, containsLight, 5);
		Label containsNone = new Label(skillInfo, SWT.NONE);
		containsNone.setText("無");
		containsNone.setFont(DnFColor.TEMP3);
		containsNone.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		containsNone.setEnabled(none);
		setFormData(containsNone, levelInfoLabel, 10, containsDarkness, 5);
		
		Label skillIncreaseLabel = new Label(skillInfo, SWT.NONE);
		skillIncreaseLabel.setText("스킬 공격력 증가- ");
		skillIncreaseLabel.setFont(DnFColor.TEMP2);
		setFormData(skillIncreaseLabel, containsFire, 10, 0, 10);
		Composite increaseComposite = new Composite(skillInfo, SWT.NONE);
		increaseComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		double skillIncrease = setNumberComposite(increaseComposite, status.skillIncreaseList, "스킬 공격력 증가", '*');
		setFormData(increaseComposite, containsFire, 10, skillIncreaseLabel, 5);
		
		Label phy_perLabel = new Label(skillInfo, SWT.NONE);
		phy_perLabel.setText("물리 % 공격력 - ");
		phy_perLabel.setFont(DnFColor.TEMP2);
		setFormData(phy_perLabel, skillIncreaseLabel, 20, 0, 10);
		Composite phy_perComposite = new Composite(skillInfo, SWT.NONE);
		phy_perComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		LinkedList<TrackRecord> tempList = new LinkedList<TrackRecord>();
		tempList.add(new TrackRecord('['));
		tempList.add(new TrackRecord("물리 퍼뎀 수치", levelInfo.phy_atk));
		tempList.add(new TrackRecord("스킬 공격력 증가", skillIncrease));
		tempList.add(new TrackRecord(']'));
		phy_per = setNumberComposite(phy_perComposite, tempList, "물리 % 공격력", '*');
		setFormData(phy_perComposite, phy_perLabel, 3, 0, 10);
		
		Label phy_fixLabel = new Label(skillInfo, SWT.NONE);
		phy_fixLabel.setText("물리 고정공격력(독공 1당) - ");
		phy_fixLabel.setFont(DnFColor.TEMP2);
		setFormData(phy_fixLabel, phy_perComposite, 10, 0, 10);
		Composite phy_fixComposite = new Composite(skillInfo, SWT.NONE);
		phy_fixComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		tempList = new LinkedList<TrackRecord>();
		tempList.add(new TrackRecord("물리 고정 수치", levelInfo.phy_fix));
		tempList.add(new TrackRecord("스킬 공격력 증가", skillIncrease));
		phy_fix = setNumberComposite(phy_fixComposite, tempList, "물리 고정공격력(독공 1당)", '*');
		setFormData(phy_fixComposite, phy_fixLabel, 3, 0, 10);
		
		Label phy_conversion = new Label(skillInfo, SWT.NONE);
		phy_conversion.setText("마법 컨버전");
		phy_conversion.setVisible(noPhy);
		phy_conversion.setFont(DnFColor.TEMP);
		phy_conversion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
		setFormData_right(phy_conversion, phy_perLabel, 5, 100, -10);
		phy_perComposite.setVisible(!noPhy);
		phy_fixComposite.setVisible(!noPhy);
		if(noPhy){
			phy_per=0;
			phy_fix=0;
		}
		
		Label mag_perLabel = new Label(skillInfo, SWT.NONE);
		mag_perLabel.setText("마법 % 공격력 - ");
		mag_perLabel.setFont(DnFColor.TEMP2);
		setFormData(mag_perLabel, phy_fixComposite, 15, 0, 10);
		Composite mag_perComposite = new Composite(skillInfo, SWT.NONE);
		mag_perComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		tempList = new LinkedList<TrackRecord>();
		tempList.add(new TrackRecord('['));
		tempList.add(new TrackRecord("마법 퍼뎀 수치", levelInfo.mag_atk));
		tempList.add(new TrackRecord("스킬 공격력 증가", skillIncrease));
		tempList.add(new TrackRecord(']'));
		mag_per = setNumberComposite(mag_perComposite, tempList, "마법 % 공격력", '*');
		setFormData(mag_perComposite, mag_perLabel, 3, 0, 10);
		
		Label mag_fixLabel = new Label(skillInfo, SWT.NONE);
		mag_fixLabel.setText("마법 고정공격력(독공 1당) - ");
		mag_fixLabel.setFont(DnFColor.TEMP2);
		setFormData(mag_fixLabel, mag_perComposite, 10, 0, 10);
		Composite mag_fixComposite = new Composite(skillInfo, SWT.NONE);
		mag_fixComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		tempList = new LinkedList<TrackRecord>();
		tempList.add(new TrackRecord("마법 고정 수치", levelInfo.mag_fix));
		tempList.add(new TrackRecord("스킬 공격력 증가", skillIncrease));
		mag_fix = setNumberComposite(mag_fixComposite, tempList, "마법 고정공격력(독공 1당)", '*');
		setFormData(mag_fixComposite, mag_fixLabel, 3, 0, 10);
		
		Label mag_conversion = new Label(skillInfo, SWT.NONE);
		mag_conversion.setText("물리 컨버전");
		mag_conversion.setVisible(noMag);
		mag_conversion.setFont(DnFColor.TEMP);
		mag_conversion.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE));
		setFormData_right(mag_conversion, mag_perLabel, 5, 100, -10);
		mag_perComposite.setVisible(!noMag);
		mag_fixComposite.setVisible(!noMag);
		if(noMag){
			mag_per=0;
			mag_fix=0;
		}
		
		Label subSkillLabel = new Label(skillInfo, SWT.NONE);
		String subSkillList = "";
		if((!trackingSkill.hasBuff() && trackingSkill.getActiveEnabled()) || trackingSkill.buffEnabled(true)){
			for(Entry<String, Integer> entry : levelInfo.percentList.entrySet())
				subSkillList += entry.getKey()+"의 "+entry.getValue()+"%, ";
		}
		if(subSkillList.isEmpty()) subSkillList = "없음";
		else subSkillList = subSkillList.substring(0, subSkillList.length()-2);
		subSkillLabel.setText("귀속된 스킬 : "+subSkillList);
		subSkillLabel.setFont(DnFColor.TEMP2);
		setFormData(subSkillLabel, mag_fixComposite, 15, 0, 10);
		
		if(!subSkillList.equals("없음")){
			Composite subSkillDamageComposite = new Composite(skillInfo, SWT.NONE);
			subSkillDamageComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			tempList = new LinkedList<TrackRecord>();
			tempList.add(new TrackRecord('('));
			for(Entry<String, Integer> entry : levelInfo.percentList.entrySet()){
				tempList.add(new TrackRecord(entry.getKey()+"의 "+entry.getValue()+"%",
						(long) (Calculator.getDamage(character.characterInfoList.getSkill(entry.getKey()), character.target, character, 1)*entry.getValue()/100.0)));
			}
			tempList.add(new TrackRecord(')'));
			tempList.add(new TrackRecord('*'));
			tempList.add(new TrackRecord("현재 스킬 스증뎀", trackingSkill.dungeonIncrease));
			subSkillDamage = setNumberComposite(subSkillDamageComposite, tempList, "귀속된 스킬의 총 딜", '+');
			setFormData(subSkillDamageComposite, subSkillLabel, 10, 0, 10);
		}
	}
	
	private void setElementInfo(Composite content){
		elementInfo = new Group(content, SWT.NONE);
		elementInfo.setText("속성 정보");
		elementInfo.setLayout(new FormLayout());
		
		try{
			Label fireLabel = new Label(elementInfo, SWT.NONE);
			fireLabel.setText("火속성 강화");
			fireLabel.setFont(DnFColor.TEMP);
			setFormData(fireLabel, 0, 5, 0, 10);
			Composite fireComposite = new Composite(elementInfo, SWT.NONE);
			fireComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			LinkedList<TrackRecord> formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("화속강", status.getStat("화속강")+status.getStat("모속강")));
			formula.add(new TrackRecord("화속깍", status.getStat("화속깍")));
			formula.add(new TrackRecord("화속저", -character.target.getStat("화속저")));
			double fire_sum = setNumberComposite(fireComposite, formula, "화속성 강화", '+');
			setFormData(fireComposite, 0, 8, fireLabel, 10);
			
			Label fireDamageLabel = new Label(elementInfo, SWT.NONE);
			fireDamageLabel.setText("ㄴ 증뎀량");
			fireDamageLabel.setFont(DnFColor.TEMP);
			setFormData(fireDamageLabel, fireLabel, 10, 0, 10);
			Composite fireDamageComposite = new Composite(elementInfo, SWT.NONE);
			fireDamageComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("속강계수", 0.0045));
			formula.add(new TrackRecord("화속강", fire_sum));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1.05));
			fire_inc = setNumberComposite(fireDamageComposite, formula, "화속성 증뎀량", '*');
			setFormData(fireDamageComposite, fireLabel, 13, fireLabel, 10);
			
			Label fireEnabledLabel = new Label(elementInfo, SWT.NONE);
			String enabled = "";
			if(status.getEnabled("화속")) enabled = "√ 속성부여";
			fireEnabledLabel.setText(enabled);
			fireEnabledLabel.setFont(DnFColor.TEMP2);
			if(enabled.equals("√ 속성부여")){
				fireLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				fireDamageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
				fireEnabledLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
			}
			setFormData_right(fireEnabledLabel, 0, 30, 100, -10);
			
			Label waterLabel = new Label(elementInfo, SWT.NONE);
			waterLabel.setText("水속성 강화");
			waterLabel.setFont(DnFColor.TEMP);
			setFormData(waterLabel, fireDamageLabel, 20, 0, 10);
			Composite waterComposite = new Composite(elementInfo, SWT.NONE);
			waterComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("수속강", status.getStat("수속강")+status.getStat("모속강")));
			formula.add(new TrackRecord("수속깍", status.getStat("수속깍")));
			formula.add(new TrackRecord("수속저", -character.target.getStat("수속저")));
			double water_sum = setNumberComposite(waterComposite, formula, "수속성 강화", '+');
			setFormData(waterComposite, fireDamageLabel, 23, waterLabel, 10);
			
			Label waterDamageLabel = new Label(elementInfo, SWT.NONE);
			waterDamageLabel.setText("ㄴ 증뎀량");
			waterDamageLabel.setFont(DnFColor.TEMP);
			setFormData(waterDamageLabel, waterLabel, 10, 0, 10);
			Composite waterDamageComposite = new Composite(elementInfo, SWT.NONE);
			waterDamageComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("속강계수", 0.0045));
			formula.add(new TrackRecord("수속강", water_sum));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1.05));
			water_inc = setNumberComposite(waterDamageComposite, formula, "수속성 증뎀량", '*');
			setFormData(waterDamageComposite, waterLabel, 13, waterLabel, 10);
			
			Label waterEnabledLabel = new Label(elementInfo, SWT.NONE);
			enabled = "";
			if(status.getEnabled("수속")) enabled = "√ 속성부여";
			waterEnabledLabel.setText(enabled);
			waterEnabledLabel.setFont(DnFColor.TEMP2);
			if(enabled.equals("√ 속성부여")){
				waterLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				waterDamageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				waterEnabledLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
			}
			setFormData_right(waterEnabledLabel, fireDamageLabel, 40, 100, -10);
			
			Label lightLabel = new Label(elementInfo, SWT.NONE);
			lightLabel.setText("明속성 강화");
			lightLabel.setFont(DnFColor.TEMP);
			setFormData(lightLabel, waterDamageLabel, 20, 0, 10);
			Composite lightCompostie = new Composite(elementInfo, SWT.NONE);
			lightCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("명속강", status.getStat("명속강")+status.getStat("모속강")));
			formula.add(new TrackRecord("명속깍", status.getStat("명속깍")));
			formula.add(new TrackRecord("명속저", -character.target.getStat("명속저")));
			double light_sum = setNumberComposite(lightCompostie, formula, "명속성 강화", '+');
			setFormData(lightCompostie, waterDamageLabel, 23, lightLabel, 10);
			
			Label lightDamageLabel = new Label(elementInfo, SWT.NONE);
			lightDamageLabel.setText("ㄴ 증뎀량");
			lightDamageLabel.setFont(DnFColor.TEMP);
			setFormData(lightDamageLabel, lightLabel, 10, 0, 10);
			Composite lightDamageCompostie = new Composite(elementInfo, SWT.NONE);
			lightDamageCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("속강계수", 0.0045));
			formula.add(new TrackRecord("명속강", light_sum));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1.05));
			light_inc = setNumberComposite(lightDamageCompostie, formula, "명속성 증뎀량", '*');
			setFormData(lightDamageCompostie, lightLabel, 13, lightLabel, 10);
			
			Label lightEnabledLabel = new Label(elementInfo, SWT.NONE);
			enabled = "";
			if(status.getEnabled("명속")) enabled = "√ 속성부여";
			lightEnabledLabel.setText(enabled);
			lightEnabledLabel.setFont(DnFColor.TEMP2);
			if(enabled.equals("√ 속성부여")){
				lightLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
				lightDamageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
				lightEnabledLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW));
			}
			setFormData_right(lightEnabledLabel, waterDamageLabel, 40, 100, -10);
			
			Label darknessLabel = new Label(elementInfo, SWT.NONE);
			darknessLabel.setText("暗속성 강화");
			darknessLabel.setFont(DnFColor.TEMP);
			setFormData(darknessLabel, lightDamageLabel, 20, 0, 10);
			Composite darknessCompostie = new Composite(elementInfo, SWT.NONE);
			darknessCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("암속강", status.getStat("암속강")+status.getStat("모속강")));
			formula.add(new TrackRecord("암속깍", status.getStat("암속깍")));
			formula.add(new TrackRecord("암속저", -character.target.getStat("암속저")));
			double darkness_sum = setNumberComposite(darknessCompostie, formula, "암속성 강화", '+');
			setFormData(darknessCompostie, lightDamageLabel, 23, darknessLabel, 10);
			
			Label darknessDamageLabel = new Label(elementInfo, SWT.NONE);
			darknessDamageLabel.setText("ㄴ 증뎀량");
			darknessDamageLabel.setFont(DnFColor.TEMP);
			setFormData(darknessDamageLabel, darknessLabel, 10, 0, 10);
			Composite darknessDamageCompostie = new Composite(elementInfo, SWT.NONE);
			darknessDamageCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("속강계수", 0.0045));
			formula.add(new TrackRecord("암속강", darkness_sum));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1.05));
			darkness_inc = setNumberComposite(darknessDamageCompostie, formula, "암속성 증뎀량", '*');
			setFormData(darknessDamageCompostie, darknessLabel, 13, darknessLabel, 10);
			
			Label darknessEnabledLabel = new Label(elementInfo, SWT.NONE);
			enabled = "";
			if(status.getEnabled("암속")) enabled = "√ 속성부여";
			darknessEnabledLabel.setText(enabled);
			darknessEnabledLabel.setFont(DnFColor.TEMP2);
			if(enabled.equals("√ 속성부여")){
				darknessLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
				darknessDamageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
				darknessEnabledLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
			}
			setFormData_right(darknessEnabledLabel, lightDamageLabel, 40, 100, -10);
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	
	private void setCriticalInfo(Composite content){
		criticalInfo = new Group(content, SWT.NONE);
		criticalInfo.setText("크리티컬 정보");
		criticalInfo.setLayout(new FormLayout());
		
		boolean hasPhy=(phy_per!=0 || phy_fix!=0);
		boolean hasMag=(mag_per!=0 || mag_fix!=0);
		
		try {
			Label damage_crt_sumLabel = new Label(criticalInfo, SWT.NONE);
			damage_crt_sumLabel.setText("크증뎀 합 - ");
			damage_crt_sumLabel.setFont(DnFColor.TEMP);
			setFormData(damage_crt_sumLabel, 0, 5, 0, 10);
			Composite damage_crt_sumCompostie = new Composite(criticalInfo, SWT.NONE);
			damage_crt_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			LinkedList<TrackRecord> formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본값", 100));
			formula.add(new TrackRecord("크증뎀", status.trackList[StatList.DAM_CRT], '+'));
			formula.add(new TrackRecord("크증뎀 추가", status.trackList[StatList.DAM_CRT_ADD], '+'));
			double damage_crt_sum = setNumberComposite(damage_crt_sumCompostie, formula, "크증뎀 합", '+');
			damage_crt_sum = damage_crt_sum/100;
			setFormData(damage_crt_sumCompostie, 0, 8, damage_crt_sumLabel, 10);
			
			Label crtIncreaseLabel = new Label(criticalInfo, SWT.NONE);
			crtIncreaseLabel.setText("크리티컬시 증뎀 - ");
			crtIncreaseLabel.setFont(DnFColor.TEMP);
			setFormData(crtIncreaseLabel, damage_crt_sumLabel, 10, 0, 10);
			Composite crtIncreaseComposite = new Composite(criticalInfo, SWT.NONE);
			crtIncreaseComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본 크증뎀", 1.5));
			formula.add(new TrackRecord("크증뎀 합", damage_crt_sum));
			formula.add(new TrackRecord("크증뎀 버프", status.trackList[StatList.BUF_CRT], '*'));
			double inc_crt = setNumberComposite(crtIncreaseComposite, formula, "크리티컬시 증뎀", '*');
			setFormData(crtIncreaseComposite, damage_crt_sumLabel, 13, crtIncreaseLabel, 10);
			
			Label crt_phyLabel = new Label(criticalInfo, SWT.NONE);
			crt_phyLabel.setText("물리크리확률(%) - ");
			crt_phyLabel.setFont(DnFColor.TEMP);
			setFormData(crt_phyLabel, crtIncreaseLabel, 20, 0, 10);
			Composite crt_phyComposite = new Composite(criticalInfo, SWT.NONE);
			crt_phyComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본 크리", 3));
			formula.add(new TrackRecord("물크", status.getStat(StatList.CRT_PHY)));
			formula.add(new TrackRecord("크리 저항 감소", status.getStat(StatList.CRT_LOW)));
			double crt_phy = setNumberComposite(crt_phyComposite, formula, "물리크리티컬 확률(%)", '+');
			crt_phy = (crt_phy>100 ? 100 : crt_phy);
			setFormData(crt_phyComposite, crtIncreaseLabel, 23, crt_phyLabel, 10);
			setVisible(hasPhy, crt_phyLabel, crt_phyComposite);
			
			Label phy_incLabel = new Label(criticalInfo, SWT.NONE);
			phy_incLabel.setText("증뎀 기댓값(물리) - ");
			phy_incLabel.setFont(DnFColor.TEMP);
			setFormData(phy_incLabel, crt_phyLabel, 10, 0, 10);
			Composite phy_incComposite = new Composite(criticalInfo, SWT.NONE);
			phy_incComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("물리 크리 확률", crt_phy/100));
			formula.add(new TrackRecord("크리티컬시 증뎀", inc_crt));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("물리 노크리 확률", 1-crt_phy/100));
			phy_crt_inc = setNumberComposite(phy_incComposite, formula, "증뎀 기댓값(물리, 크리티컬)", '*');
			setFormData(phy_incComposite, crt_phyLabel, 13, phy_incLabel, 10);
			setVisible(hasPhy, phy_incLabel, phy_incComposite);
			
			Label crt_magLabel = new Label(criticalInfo, SWT.NONE);
			crt_magLabel.setText("마법크리확률(%) - ");
			crt_magLabel.setFont(DnFColor.TEMP);
			setFormData(crt_magLabel, phy_incLabel, 20, 0, 10);
			Composite crt_magComposite = new Composite(criticalInfo, SWT.NONE);
			crt_magComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본 크리", 3));
			formula.add(new TrackRecord("마크", status.getStat(StatList.CRT_MAG)));
			formula.add(new TrackRecord("크리 저항 감소", status.getStat(StatList.CRT_LOW)));
			double crt_mag = setNumberComposite(crt_magComposite, formula, "마법크리티컬 확률(%)", '+');
			crt_mag = (crt_mag>100 ? 100 : crt_mag);
			setFormData(crt_magComposite, phy_incLabel, 23, crt_magLabel, 10);
			setVisible(hasMag, crt_magLabel, crt_magComposite);
			
			Label mag_incLabel = new Label(criticalInfo, SWT.NONE);
			mag_incLabel.setText("증뎀 기댓값(마법) - ");
			mag_incLabel.setFont(DnFColor.TEMP);
			setFormData(mag_incLabel, crt_magLabel, 10, 0, 10);
			Composite mag_incComposite = new Composite(criticalInfo, SWT.NONE);
			mag_incComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("마법 크리 확률", crt_mag/100));
			formula.add(new TrackRecord("크리티컬시 증뎀", inc_crt));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("마법 노크리 확률", 1-crt_mag/100));
			mag_crt_inc = setNumberComposite(mag_incComposite, formula, "증뎀 기댓값(크리티컬, 마법)", '*');
			setFormData(mag_incComposite, crt_magLabel, 13, mag_incLabel, 10);
			setVisible(hasMag, mag_incLabel, mag_incComposite);
			
			Label crt_addLabel = new Label(criticalInfo, SWT.NONE);
			crt_addLabel.setText("추뎀크리확률(%) - ");
			crt_addLabel.setFont(DnFColor.TEMP);
			setFormData(crt_addLabel, mag_incLabel, 20, 0, 10);
			Composite crt_addComposite = new Composite(criticalInfo, SWT.NONE);
			crt_addComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본 크리", 3));
			formula.add(new TrackRecord("크리 저항 감소", status.getStat(StatList.CRT_LOW)));
			double crt_add = setNumberComposite(crt_addComposite, formula, "추뎀 크리 확률(%)", '+');
			crt_add = (crt_add>100 ? 100 : crt_add);
			setFormData(crt_addComposite, mag_incLabel, 23, crt_addLabel, 10);
			
			Label add_incLabel = new Label(criticalInfo, SWT.NONE);
			add_incLabel.setText("증뎀 기댓값(추뎀) - ");
			add_incLabel.setFont(DnFColor.TEMP);
			setFormData(add_incLabel, crt_addLabel, 10, 0, 10);
			Composite add_incComposite = new Composite(criticalInfo, SWT.NONE);
			add_incComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("추뎀 크리 확률", crt_add/100));
			formula.add(new TrackRecord("추뎀 크리티컬시 증뎀", 1.5));
			//formula.add(new TrackRecord("크증뎀 버프", 1+status.getStat(StatList.BUF_CRT)/100));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("추뎀 노크리 확률", 1-crt_add/100));
			add_crt_inc = setNumberComposite(add_incComposite, formula, "증뎀 기댓값(크리티컬, 추뎀)", '*');
			setFormData(add_incComposite, crt_addLabel, 13, add_incLabel, 10);
		} catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	private void setIncreaseInfo(Composite content){
		increaseInfo = new Group(content, SWT.NONE);
		increaseInfo.setText("증뎀 정보");
		increaseInfo.setLayout(new FormLayout());
		
		try {
			Label damage_inc_sumLabel = new Label(increaseInfo, SWT.NONE);
			damage_inc_sumLabel.setText("증뎀 합 - ");
			damage_inc_sumLabel.setFont(DnFColor.TEMP);
			setFormData(damage_inc_sumLabel, 0, 10, 0, 10);
			Composite damage_inc_sumCompostie = new Composite(increaseInfo, SWT.NONE);
			damage_inc_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			LinkedList<TrackRecord> formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본값", 100));
			formula.add(new TrackRecord("증뎀", status.trackList[StatList.DAM_INC], '+'));
			formula.add(new TrackRecord("크증뎀 추가", status.trackList[StatList.DAM_INC_ADD], '+'));
			double damage_inc = setNumberComposite(damage_inc_sumCompostie, formula, "증뎀 합(%)", '+');
			setFormData(damage_inc_sumCompostie, 0, 13, damage_inc_sumLabel, 10);
			
			Label skill_inc_sumLabel = new Label(increaseInfo, SWT.NONE);
			skill_inc_sumLabel.setText("스증뎀 증가량 - ");
			skill_inc_sumLabel.setFont(DnFColor.TEMP);
			setFormData(skill_inc_sumLabel, damage_inc_sumLabel, 10, 0, 10);
			Composite skill_inc_sumCompostie = new Composite(increaseInfo, SWT.NONE);
			skill_inc_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			double skill_inc = setNumberComposite(skill_inc_sumCompostie, status.trackList[StatList.DAM_SKILL], "스증뎀 증가량(곱)", '*');
			setFormData(skill_inc_sumCompostie, damage_inc_sumLabel, 13, skill_inc_sumLabel, 10);
			
			Label buff_inc_sumLabel = new Label(increaseInfo, SWT.NONE);
			buff_inc_sumLabel.setText("버프 증가량 - ");
			buff_inc_sumLabel.setFont(DnFColor.TEMP);
			setFormData(buff_inc_sumLabel, skill_inc_sumLabel, 10, 0, 10);
			Composite buff_inc_sumCompostie = new Composite(increaseInfo, SWT.NONE);
			buff_inc_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			double buff_inc = setNumberComposite(buff_inc_sumCompostie, status.trackList[StatList.BUF_INC], "버프 증가량(곱)", '*');
			setFormData(buff_inc_sumCompostie, skill_inc_sumLabel, 13, buff_inc_sumLabel, 10);
			
			Label all_inc_sumLabel = new Label(increaseInfo, SWT.NONE);
			all_inc_sumLabel.setText("모공증 합 - ");
			all_inc_sumLabel.setFont(DnFColor.TEMP);
			setFormData(all_inc_sumLabel, buff_inc_sumLabel, 10, 0, 10);
			Composite all_inc_sumCompostie = new Composite(increaseInfo, SWT.NONE);
			all_inc_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			double all_inc = setNumberComposite(all_inc_sumCompostie, status.trackList[StatList.DAM_INC_ALL], "모든 공격력 증가 합(%)", '+');
			setFormData(all_inc_sumCompostie, buff_inc_sumLabel, 13, all_inc_sumLabel, 10);
			Label dope_inc_sumLabel = new Label(increaseInfo, SWT.NONE);
			dope_inc_sumLabel.setText("투함포항 - ");
			dope_inc_sumLabel.setFont(DnFColor.TEMP);
			setFormData(dope_inc_sumLabel, all_inc_sumLabel, 10, 0, 10);
			Composite dope_inc_sumCompostie = new Composite(increaseInfo, SWT.NONE);
			dope_inc_sumCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			double dope_inc = setNumberComposite(dope_inc_sumCompostie, status.trackList[StatList.DAM_BUF], "투함포, 안톤공대버프, 이그니스(%)", '+');
			setFormData(dope_inc_sumCompostie, all_inc_sumLabel, 13, dope_inc_sumLabel, 10);
			
			Label fire_addLabel = new Label(increaseInfo, SWT.NONE);
			fire_addLabel.setText("화속추뎀 - ");
			fire_addLabel.setFont(DnFColor.TEMP2);
			setFormData(fire_addLabel, dope_inc_sumLabel, 10, 0, 5);
			Composite fire_addCompostie = new Composite(increaseInfo, SWT.NONE);
			fire_addCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("화속추", status.getStat("화속추")));
			formula.add(new TrackRecord("화속 증뎀", fire_inc));
			double fire_add = setNumberComposite(fire_addCompostie, formula, "화속성 추가데미지", '*');
			setFormData(fire_addCompostie, dope_inc_sumLabel, 10, fire_addLabel, 10);
			
			Label water_addLabel = new Label(increaseInfo, SWT.NONE);
			water_addLabel.setText("수속추뎀 - ");
			water_addLabel.setFont(DnFColor.TEMP2);
			setFormData(water_addLabel, dope_inc_sumLabel, 10, 50, 10);
			Composite water_addCompostie = new Composite(increaseInfo, SWT.NONE);
			water_addCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("수속추", status.getStat("수속추")));
			formula.add(new TrackRecord("수속 증뎀", water_inc));
			double water_add = setNumberComposite(water_addCompostie, formula, "수속성 추가데미지", '*');
			setFormData(water_addCompostie, dope_inc_sumLabel, 10, water_addLabel, 10);
			
			Label light_addLabel = new Label(increaseInfo, SWT.NONE);
			light_addLabel.setText("명속추뎀 - ");
			light_addLabel.setFont(DnFColor.TEMP2);
			setFormData(light_addLabel, fire_addLabel, 5, 0, 5);
			Composite light_addCompostie = new Composite(increaseInfo, SWT.NONE);
			light_addCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("명속추", status.getStat("명속추")));
			formula.add(new TrackRecord("명속 증뎀", light_inc));
			double light_add = setNumberComposite(light_addCompostie, formula, "명속성 추가데미지", '*');
			setFormData(light_addCompostie, fire_addLabel, 5, light_addLabel, 10);
			
			Label darkness_addLabel = new Label(increaseInfo, SWT.NONE);
			darkness_addLabel.setText("암속추뎀 - ");
			darkness_addLabel.setFont(DnFColor.TEMP2);
			setFormData(darkness_addLabel, fire_addLabel, 5, 50, 10);
			Composite darkness_addCompostie = new Composite(increaseInfo, SWT.NONE);
			darkness_addCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("암속추", status.getStat("암속추")));
			formula.add(new TrackRecord("암속 증뎀", darkness_inc));
			double darkness_add = setNumberComposite(darkness_addCompostie, formula, "암속성 추가데미지", '*');
			setFormData(darkness_addCompostie, fire_addLabel, 5, darkness_addLabel, 10);
			
			Label addLabel = new Label(increaseInfo, SWT.NONE);
			addLabel.setText("추뎀 증가량 - ");
			addLabel.setFont(DnFColor.TEMP2);
			setFormData(addLabel, light_addLabel, 10, 0, 5);
			Composite addCompostie = new Composite(increaseInfo, SWT.NONE);
			addCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("화속추", fire_add));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("수속추", water_add));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("명속추", light_add));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("암속추", darkness_add));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("추뎀", status.getStat("추뎀")));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord("크리티컬 증뎀", add_crt_inc));
			double counter_inc = 1;
			if(character.target.getBool("카운터")) counter_inc=1.25;
			formula.add(new TrackRecord("카운터", counter_inc));
			double add_inc = setNumberComposite(addCompostie, formula, "추뎀 증가량(%)", '*');
			setFormData(addCompostie, light_addLabel, 10, addLabel, 5);
			
			Label resultLabel = new Label(increaseInfo, SWT.NONE);
			resultLabel.setText("최종 증가량 - ");
			resultLabel.setFont(DnFColor.TEMP);
			setFormData(resultLabel, addLabel, 10, 0, 10);
			Composite resultCompostie = new Composite(increaseInfo, SWT.NONE);
			resultCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("증뎀 증가량", damage_inc/100));
			formula.add(new TrackRecord("스증뎀 증가량", skill_inc));
			formula.add(new TrackRecord("버프 증가량", buff_inc));
			formula.add(new TrackRecord("모공증 증가량", all_inc/100+1));
			formula.add(new TrackRecord("투함포항 증가량", dope_inc/100+1));
			formula.add(new TrackRecord("추뎀 증가량", add_inc/100+1));
			formula.add(new TrackRecord("카운터", counter_inc));
			inc_result = setNumberComposite(resultCompostie, formula, "최종 증가량(곱)", '*');
			setFormData(resultCompostie, resultLabel, 13, 0, 0);
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	
	private void setVisible(boolean bool, Label label, Composite composite){
		label.setEnabled(bool);
		composite.setVisible(bool);
	}
	
	private void setResultInfo(Composite content){
		
		int rightPosition = 50;
		boolean hasPhy_per=(phy_per!=0);
		boolean hasMag_per=(mag_per!=0);
		boolean hasIndep=(phy_fix!=0 || mag_fix!=0);
		boolean hasPhy=(phy_per!=0 || phy_fix!=0);
		boolean hasMag=(mag_per!=0 || mag_fix!=0);
		
		try {
			Label str_incLabel = new Label(resultInfo, SWT.NONE);
			str_incLabel.setText("힘 % 증가량 - ");
			str_incLabel.setFont(DnFColor.TEMP2);
			setFormData(str_incLabel, 0, 10, 0, 10);
			Composite str_incCompostie = new Composite(resultInfo, SWT.NONE);
			str_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			LinkedList<TrackRecord> formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본값", 100));
			formula.add(new TrackRecord("힘 %증가", status.trackList[StatList.STR_INC], '+'));
			double strInc = setNumberComposite(str_incCompostie, formula, "힘 % 증가량", '+');
			setFormData(str_incCompostie, 0, 12, str_incLabel, 10);
			setVisible(hasPhy, str_incLabel, str_incCompostie);
			
			Label strLabel = new Label(resultInfo, SWT.NONE);
			strLabel.setText("힘 데미지 증가 - ");
			strLabel.setFont(DnFColor.TEMP2);
			setFormData(strLabel, 0, 10, rightPosition, 0);
			Composite strCompostie = new Composite(resultInfo, SWT.NONE);
			strCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("힘", status.getStat("힘")));
			formula.add(new TrackRecord("힘 %증가", strInc/100));
			formula.add(new TrackRecord(']'));
			formula.add(new TrackRecord("스탯 계수(÷250)", 1.0/250));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1));
			double str_inc = setNumberComposite(strCompostie, formula, "힘 데미지 증가(곱)", '*');
			setFormData(strCompostie, 0, 12, strLabel, 10);
			setVisible(hasPhy, strLabel, strCompostie);
			
			Label int_incLabel = new Label(resultInfo, SWT.NONE);
			int_incLabel.setText("지능 % 증가량 - ");
			int_incLabel.setFont(DnFColor.TEMP2);
			setFormData(int_incLabel, str_incLabel, 10, 0, 10);
			Composite int_incCompostie = new Composite(resultInfo, SWT.NONE);
			int_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("기본값", 100));
			formula.add(new TrackRecord("지능 %증가", status.trackList[StatList.INT_INC], '+'));
			double intInc = setNumberComposite(int_incCompostie, formula, "지능 % 증가량", '+');
			setFormData(int_incCompostie, str_incLabel, 12, int_incLabel, 10);
			setVisible(hasMag, int_incLabel, int_incCompostie);
			
			Label intLabel = new Label(resultInfo, SWT.NONE);
			intLabel.setText("지능 데미지 증가 - ");
			intLabel.setFont(DnFColor.TEMP2);
			setFormData(intLabel, str_incLabel, 10, rightPosition, 0);
			Composite intCompostie = new Composite(resultInfo, SWT.NONE);
			intCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("지능", status.getStat("지능")));
			formula.add(new TrackRecord("지능 %증가", intInc/100));
			formula.add(new TrackRecord(']'));
			formula.add(new TrackRecord("스탯 계수(÷250)", 1.0/250));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("기본값", 1));
			double int_inc = setNumberComposite(intCompostie, formula, "지능 데미지 증가(곱)", '*');
			setFormData(intCompostie, str_incLabel, 12, intLabel, 10);
			setVisible(hasMag, intLabel, intCompostie);
			
			Label phy_atk_incLabel = new Label(resultInfo, SWT.NONE);
			phy_atk_incLabel.setText("물공 %증가(아이템) - ");
			phy_atk_incLabel.setFont(DnFColor.TEMP2);
			setFormData(phy_atk_incLabel, int_incLabel, 10, 0, 10);
			Composite phy_atk_incCompostie = new Composite(resultInfo, SWT.NONE);
			phy_atk_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			LinkedList<TrackRecord> incList = status.trackList[StatList.MAST_PHY_ITEM];
			incList.addFirst(new TrackRecord("기본값", 100));
			double phy_atkInc = setNumberComposite(phy_atk_incCompostie, incList, "물공 % 증가(아이템)", '+');
			setFormData(phy_atk_incCompostie, int_incLabel, 12, phy_atk_incLabel, 10);
			setVisible(hasPhy_per, phy_atk_incLabel, phy_atk_incCompostie);
			
			Label phy_atkLabel = new Label(resultInfo, SWT.NONE);
			phy_atkLabel.setText("최종 무기물공 - ");
			phy_atkLabel.setFont(DnFColor.TEMP2);
			setFormData(phy_atkLabel, int_incLabel, 10, rightPosition, 0);
			Composite phy_atkCompostie = new Composite(resultInfo, SWT.NONE);
			phy_atkCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("무기물공", status.getStat("물공")));
			formula.add(new TrackRecord("물공 %증가(아이템)", phy_atkInc/100));
			formula.add(new TrackRecord("물공 %증가(스킬)", status.trackList[StatList.MAST_PHY], '*'));
			formula.add(new TrackRecord(']'));
			double phy_atk_inc = setNumberComposite(phy_atkCompostie, formula, "최종 무기물공", '*');
			setFormData(phy_atkCompostie, int_incLabel, 12, phy_atkLabel, 10);
			setVisible(hasPhy_per, phy_atkLabel, phy_atkCompostie);
			
			Label mag_atk_incLabel = new Label(resultInfo, SWT.NONE);
			mag_atk_incLabel.setText("마공 %증가(아이템) - ");
			mag_atk_incLabel.setFont(DnFColor.TEMP2);
			setFormData(mag_atk_incLabel, phy_atk_incLabel, 10, 0, 10);
			Composite mag_atk_incCompostie = new Composite(resultInfo, SWT.NONE);
			mag_atk_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			incList = status.trackList[StatList.MAST_MAG_ITEM];
			incList.addFirst(new TrackRecord("기본값", 100));
			double mag_atkInc = setNumberComposite(mag_atk_incCompostie, incList, "마공 % 증가(아이템)", '+');
			setFormData(mag_atk_incCompostie, phy_atk_incLabel, 12, mag_atk_incLabel, 10);
			setVisible(hasMag_per, mag_atk_incLabel, mag_atk_incCompostie);
			
			Label mag_atkLabel = new Label(resultInfo, SWT.NONE);
			mag_atkLabel.setText("최종 무기마공 - ");
			mag_atkLabel.setFont(DnFColor.TEMP2);
			setFormData(mag_atkLabel, phy_atk_incLabel, 10, rightPosition, 0);
			Composite mag_atkCompostie = new Composite(resultInfo, SWT.NONE);
			mag_atkCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("무기마공", status.getStat("마공")));
			formula.add(new TrackRecord("마공 %증가(아이템)", mag_atkInc/100));
			formula.add(new TrackRecord("마공 %증가(스킬)", status.trackList[StatList.MAST_MAG], '*'));
			formula.add(new TrackRecord(']'));
			double mag_atk_inc = setNumberComposite(mag_atkCompostie, formula, "최종 무기마공", '*');
			setFormData(mag_atkCompostie, phy_atk_incLabel, 12, mag_atkLabel, 10);
			setVisible(hasMag_per, mag_atkLabel, mag_atkCompostie);
			
			Label indep_atk_incLabel = new Label(resultInfo, SWT.NONE);
			indep_atk_incLabel.setText("독공 %증가(아이템) - ");
			indep_atk_incLabel.setFont(DnFColor.TEMP2);
			setFormData(indep_atk_incLabel, mag_atk_incLabel, 10, 0, 10);
			Composite indep_atk_incCompostie = new Composite(resultInfo, SWT.NONE);
			indep_atk_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			incList = status.trackList[StatList.MAST_INDEP_ITEM];
			incList.addFirst(new TrackRecord("기본값", 100));
			double indep_atkInc = setNumberComposite(indep_atk_incCompostie, incList, "독공 % 증가(아이템)", '+');
			setFormData(indep_atk_incCompostie, mag_atk_incLabel, 12, indep_atk_incLabel, 10);
			setVisible(hasIndep, indep_atk_incLabel, indep_atk_incCompostie);
			
			Label indep_atkLabel = new Label(resultInfo, SWT.NONE);
			indep_atkLabel.setText("최종 독공 - ");
			indep_atkLabel.setFont(DnFColor.TEMP2);
			setFormData(indep_atkLabel, mag_atk_incLabel, 10, rightPosition, 0);
			Composite indep_atkCompostie = new Composite(resultInfo, SWT.NONE);
			indep_atkCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("독공", status.getStat("독공")));
			formula.add(new TrackRecord("독공 %증가(아이템)", indep_atkInc/100));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("재련독공", status.trackList[StatList.WEP_IND_REFORGE], '+'));
			formula.add(new TrackRecord(']'));
			formula.add(new TrackRecord("독공 %증가(스킬)", status.trackList[StatList.MAST_IND], '*'));
			formula.add(new TrackRecord(']'));
			double indep_atk_inc = setNumberComposite(indep_atkCompostie, formula, "최종 독공", '*');
			setFormData(indep_atkCompostie, mag_atk_incLabel, 12, indep_atkLabel, 10);
			setVisible(hasIndep, indep_atkLabel, indep_atkCompostie);
			
			Label phy_ing_incLabel = new Label(resultInfo, SWT.NONE);
			phy_ing_incLabel.setText("물리방무뎀 - ");
			phy_ing_incLabel.setFont(DnFColor.TEMP2);
			setFormData(phy_ing_incLabel, indep_atk_incLabel, 10, 0, 10);
			Composite phy_ing_incCompostie = new Composite(resultInfo, SWT.NONE);
			phy_ing_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("물리방무뎀", status.getStat(StatList.WEP_NODEF_PHY)));
			formula.add(new TrackRecord("방무 %증가", status.trackList[StatList.WEP_NODEF_PHY_INC], '*'));
			formula.add(new TrackRecord("방무패널티", (100.0-character.target.getStat(Monster_StatList.DIFFICULTY))/100));
			formula.add(new TrackRecord(']'));
			double phy_ingInc = setNumberComposite(phy_ing_incCompostie, formula, "최종 물리 방무뎀", '*');
			setFormData(phy_ing_incCompostie, indep_atk_incLabel, 12, phy_ing_incLabel, 10);
			setVisible(hasPhy_per, phy_ing_incLabel, phy_ing_incCompostie);
			
			Label mag_ing_incLabel = new Label(resultInfo, SWT.NONE);
			mag_ing_incLabel.setText("마법방무뎀 - ");
			mag_ing_incLabel.setFont(DnFColor.TEMP2);
			setFormData(mag_ing_incLabel, indep_atk_incLabel, 10, rightPosition, 0);
			Composite mag_ing_incCompostie = new Composite(resultInfo, SWT.NONE);
			mag_ing_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("마법방무뎀", status.getStat(StatList.WEP_NODEF_MAG)));
			formula.add(new TrackRecord("방무 %증가", status.trackList[StatList.WEP_NODEF_MAG_INC], '*'));
			formula.add(new TrackRecord("방무패널티", (100.0-character.target.getStat(Monster_StatList.DIFFICULTY))/100));
			formula.add(new TrackRecord(']'));
			double mag_ingInc = setNumberComposite(mag_ing_incCompostie, formula, "최종 마법 방무뎀", '*');
			setFormData(mag_ing_incCompostie, indep_atk_incLabel, 12, mag_ing_incLabel, 10);
			setVisible(hasMag_per, mag_ing_incLabel, mag_ing_incCompostie);
			
			Label def_phyLabel = new Label(resultInfo, SWT.NONE);
			def_phyLabel.setText("고정물방 - ");
			def_phyLabel.setFont(DnFColor.TEMP2);
			setFormData(def_phyLabel, phy_ing_incLabel, 25, 0, 10);
			Composite def_phyCompostie = new Composite(resultInfo, SWT.NONE);
			def_phyCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("몹 방어력", character.target.getStat(Monster_StatList.DEFENSIVE_PHY)));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("고정물방깍", -status.getStat("고정물방깍")));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord("스킬 물방깍", 1-status.getStat("%물방깍_스킬")/100));
			formula.add(new TrackRecord("아이템 물방깍*(보스, 네임드 패널티)", 
					1-status.getStat("%물방깍_템")/100*character.target.getStat(Monster_StatList.TYPE)/100.0));
			formula.add(new TrackRecord("적 방어력 무시 옵션", 1-status.getStat(StatList.DEF_DEC_IGN)/100));
			formula.add(new TrackRecord(']'));
			double def_phy = setNumberComposite(def_phyCompostie, formula, "고정 물리 방어력", '*');
			if(def_phy<0) def_phy=0;
			setFormData(def_phyCompostie, mag_ing_incLabel, 25, def_phyLabel, 10);
			setVisible(hasPhy, def_phyLabel, def_phyCompostie);
			
			Label def_phy_incLabel = new Label(resultInfo, SWT.NONE);
			def_phy_incLabel.setText("물방 딜감소율 - ");
			def_phy_incLabel.setFont(DnFColor.TEMP2);
			setFormData(def_phy_incLabel, mag_ing_incLabel, 25, rightPosition, 0);
			Composite def_phy_incCompostie = new Composite(resultInfo, SWT.NONE);
			def_phy_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			int level = trackingSkill.getCanonicalSkillLevelInfo(true, character.isBurning()).indep_level;
			if(level<0) level = character.getLevel();
			formula.add(new TrackRecord("공격자 레벨", level));
			formula.add(new TrackRecord("방어력 계수", 200));
			formula.add(new TrackRecord('÷'));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("공격자 레벨", level));
			formula.add(new TrackRecord("방어력 계수", 200));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("고정물리방어력", def_phy));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord('='));
			double result = 200*level/(200*level+def_phy);
			double bound = 1-character.target.getDoubleStat(Monster_StatList.DEFENCE_LIMIT)/100;
			formula.add(new TrackRecord("물방 딜감소율(방깍하한선 포함)", result>bound ? bound : result));
			double def_phy_inc = setNumberComposite(def_phy_incCompostie, formula, "물리방어력에 의한 데미지 감소율(곱)", '*');
			setFormData(def_phy_incCompostie, mag_ing_incLabel, 25, def_phy_incLabel, 10);
			setVisible(hasPhy, def_phy_incLabel, def_phy_incCompostie);
			
			Label def_magLabel = new Label(resultInfo, SWT.NONE);
			def_magLabel.setText("고정마방 - ");
			def_magLabel.setFont(DnFColor.TEMP2);
			setFormData(def_magLabel, def_phy_incLabel, 10, 0, 10);
			Composite def_magCompostie = new Composite(resultInfo, SWT.NONE);
			def_magCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("몹 방어력", character.target.getStat(Monster_StatList.DEFENSIVE_MAG)));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("고정마방깍", -status.getStat("고정마방깍")));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord("스킬 마방깍", 1-status.getStat("%마방깍_스킬")/100));
			formula.add(new TrackRecord("아이템 마방깍*(보스, 네임드 패널티)", 
					1-status.getStat("%마방깍_템")/100*character.target.getStat(Monster_StatList.TYPE)/100.0));
			formula.add(new TrackRecord("적 방어력 무시 옵션", 1-status.getStat(StatList.DEF_DEC_IGN)/100));
			formula.add(new TrackRecord(']'));
			double def_mag = setNumberComposite(def_magCompostie, formula, "고정 마법 방어력", '*');
			if(def_mag<0) def_mag=0;
			setFormData(def_magCompostie, def_phy_incLabel, 12, def_magLabel, 10);
			setVisible(hasMag, def_magLabel, def_magCompostie);
			
			Label def_mag_incLabel = new Label(resultInfo, SWT.NONE);
			def_mag_incLabel.setText("마방 딜감소율 - ");
			def_mag_incLabel.setFont(DnFColor.TEMP2);
			setFormData(def_mag_incLabel, def_phy_incLabel, 10, rightPosition, 0);
			Composite def_mag_incCompostie = new Composite(resultInfo, SWT.NONE);
			def_mag_incCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("공격자 레벨", level));
			formula.add(new TrackRecord("방어력 계수", 200));
			formula.add(new TrackRecord('÷'));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("공격자 레벨", level));
			formula.add(new TrackRecord("방어력 계수", 200));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("고정마법방어력", def_mag));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord('='));
			result = 200*level/(200*level+def_mag);
			formula.add(new TrackRecord("마방 딜감소율(방깍하한선 포함)", result>bound ? bound : result));
			double def_mag_inc = setNumberComposite(def_mag_incCompostie, formula, "마법방어력에 의한 데미지 감소율", '*');
			setFormData(def_mag_incCompostie, def_phy_incLabel, 12, def_mag_incLabel, 10);
			setVisible(hasMag, def_mag_incLabel, def_mag_incCompostie);
			
			CalculateElement elementCalculator = new CalculateElement(character.target, status, trackingSkill.element);
			
			Label phy_atk_dealLabel = new Label(resultInfo, SWT.NONE);
			phy_atk_dealLabel.setText("물리 %데미지 - ");
			phy_atk_dealLabel.setFont(DnFColor.TEMP2);
			setFormData(phy_atk_dealLabel, def_magLabel, 20, 0, 10);
			Composite phy_atk_dealCompostie = new Composite(resultInfo, SWT.NONE);
			phy_atk_dealCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("힘 데미지 증가", str_inc));
			formula.add(new TrackRecord("무기물공", phy_atk_inc));
			formula.add(new TrackRecord(elementCalculator.get_type().getElement()+"속성 뎀증", elementCalculator.get_inc_elem()));
			formula.add(new TrackRecord("물리방어력에 의한 데미지 감소율", def_phy_inc));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("물리방무뎀", phy_ingInc));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord("물리퍼뎀", phy_per/100));
			formula.add(new TrackRecord("물크 증뎀 기댓값", phy_crt_inc));
			formula.add(new TrackRecord("최증 증뎀 증가량", inc_result));
			formula.add(new TrackRecord(']'));
			double phy_per_damage = setNumberComposite(phy_atk_dealCompostie, formula, "물리 %데미지", '*');
			setFormData(phy_atk_dealCompostie, def_magLabel, 20, phy_atk_dealLabel, 10);
			setVisible(hasPhy_per, phy_atk_dealLabel, phy_atk_dealCompostie);
			
			Label phy_fix_dealLabel = new Label(resultInfo, SWT.NONE);
			phy_fix_dealLabel.setText("물리 고정데미지 - ");
			phy_fix_dealLabel.setFont(DnFColor.TEMP2);
			setFormData(phy_fix_dealLabel, phy_atk_dealLabel, 10, 0, 10);
			Composite phy_fix_dealCompostie = new Composite(resultInfo, SWT.NONE);
			phy_fix_dealCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("독공", indep_atk_inc));
			formula.add(new TrackRecord("물리고뎀", phy_fix));
			formula.add(new TrackRecord(']'));
			formula.add(new TrackRecord("힘 데미지 증가", str_inc));
			formula.add(new TrackRecord(elementCalculator.get_type().getElement()+"속성 뎀증", elementCalculator.get_inc_elem()));
			formula.add(new TrackRecord("물방 딜감소율", def_phy_inc));
			formula.add(new TrackRecord("물크 증뎀 기댓값", phy_crt_inc));
			formula.add(new TrackRecord("최증 증뎀 증가량", inc_result));
			formula.add(new TrackRecord(']'));
			double phy_fix_damage = setNumberComposite(phy_fix_dealCompostie, formula, "물리 고정데미지", '*');
			setFormData(phy_fix_dealCompostie, phy_atk_dealLabel, 12, phy_fix_dealLabel, 10);
			setVisible(hasIndep && hasPhy, phy_fix_dealLabel, phy_fix_dealCompostie);
			
			Label mag_atk_dealLabel = new Label(resultInfo, SWT.NONE);
			mag_atk_dealLabel.setText("마법 %데미지 - ");
			mag_atk_dealLabel.setFont(DnFColor.TEMP2);
			setFormData(mag_atk_dealLabel, phy_fix_dealLabel, 10, 0, 10);
			Composite mag_atk_dealCompostie = new Composite(resultInfo, SWT.NONE);
			mag_atk_dealCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('('));
			formula.add(new TrackRecord("지능 데미지 증가", int_inc));
			formula.add(new TrackRecord("무기물공", mag_atk_inc));
			formula.add(new TrackRecord(elementCalculator.get_type().getElement()+"속성 뎀증", elementCalculator.get_inc_elem()));
			formula.add(new TrackRecord("마법방어력에 의한 데미지 감소율", def_mag_inc));
			formula.add(new TrackRecord('+'));
			formula.add(new TrackRecord("마법방무뎀", mag_ingInc));
			formula.add(new TrackRecord(')'));
			formula.add(new TrackRecord("마법퍼뎀", mag_per/100));
			formula.add(new TrackRecord("마크 증뎀 기댓값", mag_crt_inc));
			formula.add(new TrackRecord("최증 증뎀 증가량", inc_result));
			formula.add(new TrackRecord(']'));
			double mag_per_damage = setNumberComposite(mag_atk_dealCompostie, formula, "마법 %데미지", '*');
			setFormData(mag_atk_dealCompostie, phy_fix_dealLabel, 12, mag_atk_dealLabel, 10);
			setVisible(hasMag_per, mag_atk_dealLabel, mag_atk_dealCompostie);
			
			Label mag_fix_dealLabel = new Label(resultInfo, SWT.NONE);
			mag_fix_dealLabel.setText("마법 고정데미지 - ");
			mag_fix_dealLabel.setFont(DnFColor.TEMP2);
			setFormData(mag_fix_dealLabel, mag_atk_dealLabel, 10, 0, 10);
			Composite mag_fix_dealCompostie = new Composite(resultInfo, SWT.NONE);
			mag_fix_dealCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord('['));
			formula.add(new TrackRecord("독공", indep_atk_inc));
			formula.add(new TrackRecord("마법고뎀", mag_fix));
			formula.add(new TrackRecord(']'));
			formula.add(new TrackRecord("지능 데미지 증가", int_inc));
			formula.add(new TrackRecord(elementCalculator.get_type().getElement()+"속성 뎀증", elementCalculator.get_inc_elem()));
			formula.add(new TrackRecord("마방 딜감소율", def_mag_inc));
			formula.add(new TrackRecord("마크 증뎀 기댓값", mag_crt_inc));
			formula.add(new TrackRecord("최증 증뎀 증가량", inc_result));
			formula.add(new TrackRecord(']'));
			double mag_fix_damage = setNumberComposite(mag_fix_dealCompostie, formula, "마법 고정데미지", '*');
			setFormData(mag_fix_dealCompostie, mag_atk_dealLabel, 12, mag_fix_dealLabel, 10);
			setVisible(hasIndep && hasMag, mag_fix_dealLabel, mag_fix_dealCompostie);
			
			Label finalResultLabel = new Label(resultInfo, SWT.NONE);
			finalResultLabel.setText("최종 데미지");
			finalResultLabel.setFont(DnFColor.TEMP3);
			finalResultLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
			setFormData(finalResultLabel, mag_fix_dealLabel, 20, 0, 10);
			Composite finalResultCompostie = new Composite(resultInfo, SWT.NONE);
			finalResultCompostie.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			formula = new LinkedList<TrackRecord>();
			formula.add(new TrackRecord("물리퍼뎀", phy_per_damage));
			formula.add(new TrackRecord("물리고뎀", phy_fix_damage));
			formula.add(new TrackRecord("마법퍼뎀", mag_per_damage));
			formula.add(new TrackRecord("마법고뎀", mag_fix_damage));
			formula.add(new TrackRecord("귀속 스킬 데미지", subSkillDamage));
			setNumberComposite(finalResultCompostie, formula, "최종 데미지", '+');
			setFormData(finalResultCompostie, mag_fix_dealLabel, 23, finalResultLabel, 10);
		} catch (StatusTypeMismatch | UndefinedStatusKey e) {
			e.printStackTrace();
		}
	}
	
	private void setFormData(Control control, Object top, int topMargin, Object left, int leftMargin)
	{
		FormData formData = new FormData();
		if(top instanceof Control) formData.top = new FormAttachment((Control) top, topMargin);
		else if(top instanceof Integer) formData.top = new FormAttachment((int) top, topMargin);
		if(left instanceof Control) formData.left = new FormAttachment((Control) left, leftMargin);
		else if(left instanceof Integer) formData.left = new FormAttachment((int) left, leftMargin);
		control.setLayoutData(formData);
	}
	private void setFormData_right(Control control, Object top, int topMargin, Object right, int rightMargin)
	{
		FormData formData = new FormData();
		if(top instanceof Control) formData.top = new FormAttachment((Control) top, topMargin);
		else if(top instanceof Integer) formData.top = new FormAttachment((int) top, topMargin);
		if(right instanceof Control) formData.right = new FormAttachment((Control) right, rightMargin);
		else if(right instanceof Integer) formData.right = new FormAttachment((int) right, rightMargin);
		control.setLayoutData(formData);
	}

	private double setNumberComposite(Composite composite, LinkedList<TrackRecord> list, String description, char operator){
		return setNumberComposite(composite, list, description, operator, false);
	}
	private double setNumberComposite(Composite composite, LinkedList<TrackRecord> list, String description, char operator, boolean addBracket){
		RowLayout layout = new RowLayout();
		layout.pack=true;
		layout.spacing=5;
		layout.marginTop=0;
		layout.marginBottom=0;
		composite.setLayout(layout);
		RecordLabel tempLabel;
		double result=0;
		
		if(addBracket) tempLabel = new RecordLabel(composite, new TrackRecord('('), DnFColor.TEMP2, character, background);
		
		if(list.isEmpty()){
			if(operator=='+') tempLabel = new RecordLabel(composite, new TrackRecord(description, 0, null, false), DnFColor.TEMP2, character, background);
			else if(operator=='*'){
				tempLabel = new RecordLabel(composite, new TrackRecord(description, 0, null, true), DnFColor.TEMP2, character, background);
				result = 1;
			}
		}		
		else{
			boolean flag = false;
			boolean plusOperator=false;
			boolean multipleOperator=false;
			boolean bracket=false;
			boolean hasResult=false;
			for(TrackRecord t : list){
				if(t.isOperator){
					tempLabel = new RecordLabel(composite, t, DnFColor.TEMP2, character, background);
					if(t.operator=='+') plusOperator=true;
					else if(t.operator=='*') multipleOperator=true;
					else if(t.operator=='(' || t.operator=='[') bracket=true;
					else if(t.operator==']') result = (long)result;
					else if(t.operator=='=') hasResult = true;
					continue;
				}
				else if(flag && !(plusOperator || multipleOperator || bracket || hasResult))
					tempLabel = new RecordLabel(composite, new TrackRecord(operator), DnFColor.TEMP2, character, background);
				bracket=false;
				
				if(t.isStatList){
					Composite temp = new Composite(composite, SWT.NONE);
					temp.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
					t.strength = setNumberComposite(temp, t.statList, t.description, t.operator, true);
				}
				else tempLabel = new RecordLabel(composite, t, DnFColor.TEMP2, character, background);
				if(!flag){
					flag=true;
					result=t.strength;
				}
				else{
					if(plusOperator){
						result+=t.strength;
						plusOperator=false;
					}
					else if(multipleOperator){
						result*=t.strength;
						multipleOperator=false; 
					}
					else if(hasResult){
						result=t.strength;
					}
					else if(operator=='+') result+=t.strength;
					else if(operator=='*') result*=t.strength;
				}
			}
			if(list.size()>1 && !hasResult){
				tempLabel = new RecordLabel(composite, new TrackRecord('='), DnFColor.TEMP2, character, background);
				tempLabel = new RecordLabel(composite, new TrackRecord(description, result, null, false), DnFColor.TEMP2, character, background);
			}
		}
		if(addBracket) tempLabel = new RecordLabel(composite, new TrackRecord(')'), DnFColor.TEMP2, character, background);
		return result;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("팩트");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.marginHeight = 0;
	}
	
	@Override
	protected void initializeBounds() 
	{ 
		super.initializeBounds(); 
		Shell shell = this.getShell(); 
		Rectangle bounds = parent.getBounds(); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + 10; 
		shell.setLocation (x, y); 
	} 
}

class RecordLabel
{
	TrackRecord record;
	Label label;
	
	RecordLabel(Composite parent, TrackRecord record, Font font, Characters character, Composite background)
	{
		label = new Label(parent, SWT.NONE);
		label.setText(record.toString());
		label.setFont(font);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		SetListener listenerGroup = new SetListener(label, character, parent);
		label.addListener(SWT.MouseEnter, listenerGroup.makeTrackingInfoListener(background, record));
		label.addListener(SWT.MouseMove, listenerGroup.moveItemInfoListener());
		label.addListener(SWT.MouseExit, listenerGroup.disposeItemInfoListener());
	}
}