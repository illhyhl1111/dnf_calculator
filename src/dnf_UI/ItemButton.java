package dnf_UI;

import java.util.Collections;
import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import dnf_InterfacesAndExceptions.SetName;
import dnf_InterfacesAndExceptions.StatList;
import dnf_InterfacesAndExceptions.StatusTypeMismatch;
import dnf_InterfacesAndExceptions.ItemFileNotFounded;
import dnf_InterfacesAndExceptions.ItemFileNotReaded;
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_class.Equipment;
import dnf_class.Item;
import dnf_class.SetOption;
import dnf_infomation.GetItemDictionary;

public class ItemButton {
	private Button button;
	private Item item;
	private Composite parent;
	private int imageSize_x;
	private int imageSize_y;
	boolean enabled;
	
	public ItemButton(Composite parent, Item item, int x, int y, boolean enabled)
	{
		this.parent=parent;
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		this.enabled=enabled;
		button = new Button(parent, SWT.PUSH);
		renewImage();
	}
	
	public static Image resizeImage(Image image, int x, int y, Composite parent)
	{
		ImageData data = image.getImageData();
		data = data.scaledTo(x, y);
		return new Image(parent.getDisplay(), data);
	}
	public static Image resizeToButtonSize(Image image, Button button, Composite parent)
	{
		ImageData data = image.getImageData();
		data = data.scaledTo(button.getSize().x, button.getSize().y);
		return new Image(parent.getDisplay(), data);
	}
	
	public void renewImage()
	{
		if(button.getImage()!=null) button.getImage().dispose();
		Image image;
		if(item.getIcon()==null || !enabled) image = new Image(parent.getDisplay(), "image\\default.png");
		else image = new Image(parent.getDisplay(), item.getIcon());
		button.setImage(ItemButton.resizeImage(image, imageSize_x, imageSize_y, parent));
	}
	
	public boolean hasSetOption()
	{
		if(!(item instanceof Equipment) || ((Equipment)item).setName==SetName.NONE) return false;
		else return true;
	}
	
	public void setSetInfoComposite(Composite itemInfo, int setNum)
	{
		Label name = new Label(itemInfo, SWT.WRAP);
		Equipment equipment = (Equipment)item; 
		name.setText(equipment.setName.getName());
		name.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
		
		Label option;
		
		try
		{
			LinkedList<SetOption> setOptionList = GetItemDictionary.getSetOptions(equipment.setName);
			Collections.sort(setOptionList);
			
			for(SetOption s : setOptionList)
			{
				if(s.isEnabled(setNum))
				{
					option = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
					option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
					
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("["+s.requireNum+"]세트 효과");
					option.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_GREEN));
					
					for(StatusAndName s2 : s.vStat.statList)
						setText(itemInfo, s2);
					if(!item.dStat.statList.isEmpty())
					{
						option = new Label(itemInfo, SWT.WRAP);
						option.setText("\n――――――던전 입장 시 적용――――――\n\n");
						for(StatusAndName s2 : s.dStat.statList)
							setText(itemInfo, s2);
					}
				}
				
				else
				{
					option = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
					option.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
					
					option = new Label(itemInfo, SWT.WRAP);
					option.setText("["+s.requireNum+"]세트 효과");
					option.setEnabled(false);
					
					for(StatusAndName s2 : s.vStat.statList)
						setText(itemInfo, s2, false);
					if(!item.dStat.statList.isEmpty())
					{
						option = new Label(itemInfo, SWT.WRAP);
						option.setText("\n――――――던전 입장 시 적용――――――\n\n");
						option.setEnabled(false);
						for(StatusAndName s2 : s.dStat.statList)
							setText(itemInfo, s2, false);
					}
				}
			}			
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
		catch (ItemFileNotReaded | ItemFileNotFounded e) {
			e.printStackTrace();
		}
	}
	
	public void setItemInfoComposite(Composite itemInfo)
	{
		Label stat = new Label(itemInfo, SWT.WRAP);
		String temp = item.getName();
		if(item instanceof Equipment && ((Equipment)item).reinforce!=0) temp = "+"+((Equipment)item).reinforce+" "+temp;
		stat.setText(temp);
		
		Label rarity = new Label(itemInfo, SWT.WRAP);
		rarity.setText(item.getRarity().getName());
		rarity.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		
		//Color nameColor = new Color(itemInfo.getDisplay());
		switch(item.getRarity())
		{
		case EPIC:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW));
			break;
		case UNIQUE:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_MAGENTA));
			break;
		case LEGENDARY:
			stat.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			rarity.setForeground(itemInfo.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
			break;
			
		default:
		}
		
		Label type = new Label(itemInfo, SWT.WRAP);
		type.setText(item.getTypeName());
		type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		if(item.getTypeName2()!=null)
		{
			type = new Label(itemInfo, SWT.WRAP);
			type.setText(item.getTypeName2());
			type.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false));
		}
		
		stat = new Label(itemInfo, SWT.SEPARATOR | SWT.HORIZONTAL);
		stat.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 4, 1));
		
		try
		{
			for(StatusAndName s : item.vStat.statList)
				setText(itemInfo, s);
			
			if(!item.dStat.statList.isEmpty())
			{
				stat = new Label(itemInfo, SWT.WRAP);
				stat.setText("\n――――――던전 입장 시 적용――――――\n\n");
				for(StatusAndName s : item.dStat.statList)
					setText(itemInfo, s);
			}
		}
		catch (StatusTypeMismatch e) {
			e.printStackTrace();
		}
	}
	
	public void setText(Composite itemInfo, StatusAndName s) throws StatusTypeMismatch
	{
		setText(itemInfo, s, true);
	}
	
	public void setText(Composite itemInfo, StatusAndName s, boolean enable) throws StatusTypeMismatch
	{
		String strength;
		Label stat;
		
		strength = String.valueOf(s.stat.getStatToDouble());
		if(s.stat instanceof ElementInfo && strength.equals("0.0"));
		else{
			if(strength.contains(".0")){
				strength=strength.substring(0, strength.length()-2);
				stat = new Label(itemInfo, SWT.WRAP);
				
				if(strength.contains("-")){
					String name = StatusAndName.getStatHash().get(s.name);
					if(name.contains("+")) stat.setText(name.substring(0, name.length()-1)+strength);
					else if(name.contains("-")) stat.setText(name.substring(0, name.length()-1)+"+"+strength.substring(1, strength.length()));
					else stat.setText(name+strength);
				}
				else stat.setText(StatusAndName.getStatHash().get(s.name)+strength);
				stat.setEnabled(enable);
			}
		}
		
		if(s.stat instanceof ElementInfo && ((ElementInfo)s.stat).getElementEnabled()==true)
		{
			stat = new Label(itemInfo, SWT.WRAP);
			switch(s.name)
			{
			case StatList.ELEM_FIRE:
				stat.setText(" 무기에 화속성 부여");
				break;
			case StatList.ELEM_WATER:
				stat.setText(" 무기에 수속성 부여");
				break;
			case StatList.ELEM_LIGHT:
				stat.setText(" 무기에 명속성 부여");
				break;
			case StatList.ELEM_DARKNESS:
				stat.setText(" 무기에 암속성 부여");
				break;
			}
			stat.setEnabled(enable);
		}
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Composite getParent() {
		return parent;
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}
	public int getImageSize_y() {
		return imageSize_y;
	}
	public void setImageSize_y(int imageSize_y) {
		this.imageSize_y = imageSize_y;
	}
	public int getImageSize_x() {
		return imageSize_x;
	}
	public void setImageSize_x(int imageSize_x) {
		this.imageSize_x = imageSize_x;
	}
}
