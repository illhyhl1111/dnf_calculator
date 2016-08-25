package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import dnf_class.Item;

public class ItemButton {
	private Button button;
	private Item item;
	private Composite parent;
	private int imageSize_x;
	private int imageSize_y;
	
	public ItemButton(Composite parent, Item item)
	{
		this.parent=parent;
		this.item=item;
		button = new Button(parent, SWT.PUSH);
		if(item.getIcon()==null) button.setImage(new Image(parent.getDisplay(), "image\\default.png"));
		else button.setImage(new Image(parent.getDisplay(), item.getIcon()));
	}
	public ItemButton(Composite parent, Item item, int x, int y)
	{
		this.parent=parent;
		this.item=item;
		button = new Button(parent, SWT.PUSH);
		if(item.getIcon()==null) button.setImage(resizeImage(new Image(parent.getDisplay(), "image\\default.png"), x, y, parent));
		else button.setImage(resizeImage(new Image(parent.getDisplay(), item.getIcon()), x, y, parent));
		imageSize_x=x;
		imageSize_y=y;
	}
	
	public static Image resizeImage(Image image, int x, int y, Composite parent)
	{
		ImageData data = image.getImageData();
		data = data.scaledTo(x, y);
		return new Image(parent.getDisplay(), data);
	}
	public static Image resizeToButtonSize(Image image, Button button)
	{
		ImageData data = image.getImageData();
		data = data.scaledTo(button.getSize().x, button.getSize().y);
		return new Image(Display.getDefault(), data);
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
