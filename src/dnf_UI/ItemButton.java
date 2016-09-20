package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import dnf_InterfacesAndExceptions.SetName;
import dnf_class.IconObject;
import dnf_class.Item;

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
		if(item instanceof Item) renewImage(((Item) item).getEnabled());
		else renewImage(true);
	}
	public ItemButton(Composite parent, T item, int x, int y, boolean enabled)
	{
		this.item=item;
		imageSize_x=x;
		imageSize_y=y;
		button = new Button(parent, SWT.PUSH);
		renewImage(enabled);
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
	
	public void renewImage(boolean enabled)
	{
		if(button.getImage()!=null) button.getImage().dispose();
		Image image;
		if(item.getIcon()==null || !enabled) image = new Image(button.getParent().getDisplay(), "image\\default.png");
		else image = new Image(button.getParent().getDisplay(), item.getIcon());
		//button.setImage(image);
		button.setImage(ItemButton.resizeImage(image, imageSize_x, imageSize_y, button.getParent()));
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