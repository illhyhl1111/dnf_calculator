package dnf_UI_32;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

public class TextInputOnlyNumbers implements VerifyListener
{
	int max;
	TextInputOnlyNumbers(int max)
	{
		this.max=max;
	}
	TextInputOnlyNumbers()
	{
		this.max=Integer.MAX_VALUE;
	}
	
	@Override
    public void verifyText(VerifyEvent e) {

        Text text = (Text)e.getSource();

        // get old text and create new text by using the VerifyEvent.text
        final String oldS = text.getText();
        String newS = oldS.substring(0, e.start) + e.text + oldS.substring(e.end);

        boolean isFloat = true;
        try
        {
            Float.parseFloat(newS);
        }
        catch(NumberFormatException ex)
        {
            isFloat = false;
        }

        //System.out.println(newS);

        if(!isFloat || Character.isLetter(e.character))
            e.doit = false;
        	
        if(newS.isEmpty() || newS.equals("-")){
        	e.doit = true;
        }
        else if(isFloat && Float.valueOf(newS)>max){
        	e.doit = false;
        	((Text)e.widget).setText(String.valueOf(max));
        }
    }
}
