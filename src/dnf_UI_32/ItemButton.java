package dnf_UI_32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.SetName;
import dnf_class.Buff;
import dnf_class.IconObject;
import dnf_class.Item;
import dnf_class.Monster;
import dnf_class.Skill;
import dnf_infomation.GetDictionary;

public class ItemButton<T extends IconObject>
{
	private Button button;
	private T item;
	private int imageSize_x;
	private int imageSize_y;
	private boolean offImageMode=false;
	
	public ItemButton(Composite parent, T item, int x, int y)
	{
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		button = new Button(parent, SWT.PUSH);
		if(parent.getLayout() instanceof GridLayout){
			GridData buttonGridData = new GridData(x, y);
			button.setLayoutData(buttonGridData);
		}
		else if(parent.getLayout() instanceof FormLayout){
			FormData buttonFormData = new FormData(x, y);
			button.setLayoutData(buttonFormData);
		}

		if(item instanceof Item) renewImage(((Item) item).getEnabled());
		else renewImage(true);
	}
	public ItemButton(Composite parent, T item, int x, int y, boolean enabled)
	{
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		button = new Button(parent, SWT.PUSH);
		if(parent.getLayout() instanceof GridLayout){
			GridData buttonGridData = new GridData(x, y);
			button.setLayoutData(buttonGridData);
		}
		else if(parent.getLayout() instanceof FormLayout){
			FormData buttonFormData = new FormData(x, y);
			button.setLayoutData(buttonFormData);
		}
		
		renewImage(enabled);
	}
	
	public void renewImage(boolean enabled)
	{
		if(offImageMode) return;
		Image image;
		if(item.getIcon()==null || !enabled){
			image = GetDictionary.iconDictionary.get("디폴트");
		}
		else{
			if(item instanceof Skill && !((Skill)item).getActiveEnabled())
				image = GetDictionary.iconDictionary.get(item.getDisabledName());
			else if(item instanceof Buff && !((Buff)item).enabled)
				image = GetDictionary.iconDictionary.get(item.getDisabledName());
			else image = GetDictionary.iconDictionary.get(item.getItemName());
		}
		button.setImage(image);
		//button.setImage(GetDictionary.iconDictionary.get("아이템_투명"));
	}
	
	public void setOnOffImage(boolean setOffMode)
	{
		Image image;
		if(item.getIcon()==null){
			image = GetDictionary.iconDictionary.get("디폴트");
		}
		else{
			if(setOffMode) image = GetDictionary.iconDictionary.get(item.getDisabledName());
			else image = GetDictionary.iconDictionary.get(item.getItemName());
		}
		button.setImage(image);
		offImageMode=true;
	}
	
	public boolean hasSetOption()
	{
		if(item instanceof Item && ((Item)item).getSetName()==SetName.NONE) return false;
		else if(item instanceof Monster && ((Monster)item).getAdditionalStatList().statList.size()==0) return false;
		else return true;
	}

	public Button getButton() {
		return button;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public Composite getParent() {
		return button.getParent();
	}

	public void setParent(Composite parent) {
		this.button.setParent(parent);
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