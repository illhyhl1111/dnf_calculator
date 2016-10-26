package dnf_InterfacesAndExceptions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public interface DnFColor {
	Color infoBackground_transparent = new Color(Display.getDefault(), 30, 30, 30, 255);
	Color infoBackground = new Color(Display.getDefault(), 30, 30, 30);
	Color infoStat = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	Color infoExplanation = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	Color EPIC = Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
	Color LEGENDARY = new Color(Display.getDefault(), 255, 121, 0);
	Color UNIQUE = Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
	Color RARE = new Color(Display.getDefault(), 165, 126, 205);
	Color UNCOMMON = Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	Color COMMON = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
	Color INCHANT = new Color(Display.getDefault(), 88, 239, 33);
	Color DARK = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	Color DARK_YELLOW = new Color(Display.getDefault(), 212, 214, 94);
}
