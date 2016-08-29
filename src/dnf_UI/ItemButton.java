package dnf_UI;

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
import dnf_calculator.ElementInfo;
import dnf_calculator.StatusAndName;
import dnf_class.Equipment;
import dnf_class.Item;

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
	
	public void setSetInfoComposite(Composite itemInfo)
	{
		Label name = new Label(itemInfo, SWT.WRAP);
		name.setText("안녕");
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
		
		stat = new Label(itemInfo, SWT.WRAP);
		stat.setText("――――――――――――――――――――\n");
		
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
		String strength;
		Label stat;
		strength = String.valueOf(s.stat.getStatToDouble());
		if(strength.contains(".0")) strength=strength.substring(0, strength.length()-2); 
		stat = new Label(itemInfo, SWT.WRAP);
		stat.setText(StatusAndName.getStatHash().get(s.name)+strength);
		
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
