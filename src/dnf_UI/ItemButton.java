package dnf_UI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

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
