package dnf_UI_32;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import dnf_InterfacesAndExceptions.DnFColor;

public class BugReportViewer extends Dialog {
	
	private Shell parent;
	
	public BugReportViewer(Shell shell){
		super(shell);
		parent=shell;
	}
	
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new FormLayout());
		
		Label label = new Label(content, SWT.CENTER);
		label.setText("-버그 제보-");
		label.setFont(DnFColor.TEMP3);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 10);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		label.setLayoutData(formData);
		Control top = label;
		
		top = setLabel(content, top, "계산기를 사용하시면서 발견한 버그나 건의사항을 제보해주세요", 10);
		
		try {
			// get URL content
			URL naverUrl = new URL("http://blog.naver.com/illhyhl1111/220900371228");
			URLConnection conn = naverUrl.openConnection();
			Label URLLabel = new Label(content, SWT.NONE);
	    	URLLabel.setText("네이버 블로그 : ");
	    	formData = new FormData();
			formData.top = new FormAttachment(top, 20);
			formData.left = new FormAttachment(0, 10);
			URLLabel.setLayoutData(formData);
	    	Link naverLink = new Link(content, SWT.NONE);
	    	naverLink.setText("<A>"+naverUrl+"</A>");
	    	naverLink.addSelectionListener(new SelectionAdapter(){
	    		@Override
		        public void widgetSelected(SelectionEvent e) {
	    			System.out.println("You have selected: "+e.text);
	    			Program.launch(e.text);
		        }
		    });
	    	formData = new FormData();
			formData.top = new FormAttachment(URLLabel, 10);
			formData.left = new FormAttachment(0, 10);
			naverLink.setLayoutData(formData);
			top = naverLink;
			
			URL gameChUrl = new URL("http://df.gamechosun.co.kr/board/view.php?bid=tip&num=2386728&col=subject&kw=%EA%B3%84%EC%82%B0%EA%B8%B0");
			conn = gameChUrl.openConnection();
			URLLabel = new Label(content, SWT.NONE);
	    	URLLabel.setText("던파 게임조선 : ");
	    	formData = new FormData();
			formData.top = new FormAttachment(top, 20);
			formData.left = new FormAttachment(0, 10);
			URLLabel.setLayoutData(formData);
	    	Link gameChLink = new Link(content, SWT.NONE);
	    	gameChLink.setText("<A>"+gameChUrl+"</A>");
	    	gameChLink.addSelectionListener(new SelectionAdapter(){
	    		@Override
		        public void widgetSelected(SelectionEvent e) {
	    			System.out.println("You have selected: "+e.text);
	    			Program.launch(e.text);
		        }
		    });
	    	formData = new FormData();
			formData.top = new FormAttachment(URLLabel, 10);
			formData.left = new FormAttachment(0, 10);
			gameChLink.setLayoutData(formData);
			top = gameChLink;
			
			URL githubUrl = new URL("https://github.com/illhyhl1111/dnf_calculator/tree/master");
			conn = githubUrl.openConnection();
			URLLabel = new Label(content, SWT.NONE);
	    	URLLabel.setText("github (발코드이므로 코드를 보시는건 추천하지 않습니다ㅎ) : ");
	    	formData = new FormData();
			formData.top = new FormAttachment(top, 20);
			formData.left = new FormAttachment(0, 10);
			URLLabel.setLayoutData(formData);
	    	Link githubLink = new Link(content, SWT.NONE);
	    	githubLink.setText("<A>"+githubUrl+"</A>");
	    	githubLink.addSelectionListener(new SelectionAdapter(){
	    		@Override
		        public void widgetSelected(SelectionEvent e) {
	    			System.out.println("You have selected: "+e.text);
	    			Program.launch(e.text);
		        }
		    });
	    	formData = new FormData();
			formData.top = new FormAttachment(URLLabel, 10);
			formData.left = new FormAttachment(0, 10);
			githubLink.setLayoutData(formData);
			top = githubLink;
		} catch (UnknownHostException e) {
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		top = setLabel(content, top, "이메일 - illhyhl1111@naver.com", 15);
		
		top = setLabel(content, top, "※ 계산에 관한 버그를 제보하실 때에는 제가 확인할 수 있는 근거가 필요합니다.", 30);
		top = setLabel(content, top, "※ 계산과정 확인은 '계산과정 보기' 버튼에서 확인하실 수 있습니다.", 10);
		top = setLabel(content, top, "※ 계산에 관련된 버그는 모두 위 기능에서 확인 후 제보해주시길 바랍니다.", 10);
		
		top = setLabel(content, top, "ex) 로제타랑 폐눈이랑 비교해봤는데 로제타가 더 약하다고 나옵니다 이상해요 - X", 15);
		top = setLabel(content, top, "    계산과정 보기 버튼에서 로제타의 스증이 18%가 아니라 8%로 적용된다고 합니다. - O", 10);
		
		top = setLabel(content, top, "※ 던조는 확인빈도가 좀 낮을 수 있습니다.", 30);
		top = setLabel(content, top, "※ 깃허브도요.", 10);
		
		return content;
	}
	
	private Label setLabel(Composite content, Control top, String text, int topMargin){
		Label label = new Label(content, SWT.NONE);
		label.setText(text);
		FormData formData = new FormData();
		formData.top = new FormAttachment(top, topMargin);
		formData.left = new FormAttachment(0, 10);
		label.setLayoutData(formData);
		return label;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
	    super.configureShell(newShell);
	    newShell.setText("버그 제보");
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, "나가기", false);
	}
	
	@Override
	protected void initializeBounds() 
	{ 
		super.initializeBounds(); 
		Shell shell = this.getShell(); 
		Rectangle bounds = parent.getBounds(); 
		Rectangle rect = shell.getBounds (); 
		int x = bounds.x + (bounds.width - rect.width) / 2; 
		int y = bounds.y + 50; 
		shell.setLocation (x, y); 
	} 
}
