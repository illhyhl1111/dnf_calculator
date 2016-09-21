package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.SetName;
import dnf_class.IconObject;
import dnf_class.Item;
import dnf_infomation.GetDictionary;

public class ItemButton<T extends IconObject>
{
	private Button button;
	private T item;
	private int imageSize_x;
	private int imageSize_y;
	
	public ItemButton(Composite parent, T item, int x, int y)
	{
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		button = new Button(parent, SWT.PUSH);
		GridData buttonGridData = new GridData(x, y);
		button.setLayoutData(buttonGridData);
		if(item instanceof Item) renewImage(((Item) item).getEnabled());
		else renewImage(true);
	}
	public ItemButton(Composite parent, T item, int x, int y, boolean enabled)
	{
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		button = new Button(parent, SWT.PUSH);
		GridData buttonGridData = new GridData(x, y);
		button.setLayoutData(buttonGridData);
		
		renewImage(enabled);
	}
	
	public void renewImage(boolean enabled)
	{
		Image image;
		if(item.getIcon()==null || !enabled) image = GetDictionary.iconDictionary.get("디폴트");
		else image = GetDictionary.iconDictionary.get(item.getName());
		button.setImage(image);
	}
	
	public boolean hasSetOption()
	{
		if(item instanceof Item && ((Item)item).getSetName()==SetName.NONE) return false;
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