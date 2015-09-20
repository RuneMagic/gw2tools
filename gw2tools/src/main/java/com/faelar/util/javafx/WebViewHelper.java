package com.faelar.util.javafx;



import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javafx.application.Platform;
import javafx.concurrent.Worker.State;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebViewHelper
{
	private final WebView webView;
	private final WebEngine engine;

	private final List<HyperlinkEventListener> listeners=new LinkedList<HyperlinkEventListener>();

	protected WebViewHelper(WebView webView)
	{
		this.webView=webView;
		this.engine=webView.getEngine();
		installHyperlinkEventListener();
	}

	private void installHyperlinkEventListener()
	{
		/*webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
		    {
		        try
		        {
		            URI address = new URI(newValue);
		            if ((address.getQuery() + "").indexOf("_openmodal=true") > -1)
		            {
		                // wv.getEngine().load(oldValue); // 1
		            	webView.getEngine().getLoadWorker().cancel(); // 2
		                // wv.getEngine().executeScript("history.back()"); // 3
		                d.browse(address);
		            }
		        }
		        catch (IOException | URISyntaxException e)
		        {
		            displayError(e);
		        }
		    }
		});*/
		engine.getLoadWorker().stateProperty().addListener((obs,oldVal,newVal)->{
			if (newVal==State.SCHEDULED)
			{
				String loc=engine.getLocation();
				if (loc==null || loc.isEmpty()) return;
				//System.out.println("location="+engine.getLocation());
				try
				{
					boolean canceled=fireHyperlinkEvent(new URL(engine.getLocation()));
					if (canceled) Platform.runLater(()->engine.getLoadWorker().cancel());
				}
				catch (Exception e)
				{
					e.printStackTrace();
					return;
				}
			}
		});
	}

	private boolean fireHyperlinkEvent(URL url)
	{
		HyperlinkEvent evt=new HyperlinkEvent(url);
		for (HyperlinkEventListener listener:listeners) listener.onHyperlinkClicked(evt);
		return evt.isCanceled();
	}

	/*private void updateHyperlinkClickListeners()
	{
		NodeList nodeList = engine.getDocument().getElementsByTagName("a");
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Node node= nodeList.item(i);
			EventTarget eventTarget = (EventTarget) node;
			eventTarget.addEventListener("click", new EventListener()
			{
				@Override
				public void handleEvent(Event evt)
				{
					EventTarget target = evt.getCurrentTarget();
					HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
					String href = anchorElement.getHref();
					//handle opening URL outside JavaFX WebView
					System.out.println(href);
					evt.preventDefault();
				}
			}, false);
		}
	}*/

	public static WebViewHelper create(HTMLEditor htmlEditor)
	{
		WebView webView = (WebView)htmlEditor.lookup("WebView");
		if (webView==null) return null;
		return new WebViewHelper(webView);
	}

	public static WebViewHelper create(WebView webView)
	{
		if (webView==null) return null;
		return new WebViewHelper(webView);
	}

	public void setPopup()
	{

	}

	public WebView getWebView()
	{
		return webView;
	}

	public void replaceSelectedTextHTML(String html)
	{
		executeScript("var html='"+html+"';"//TODO make this injection-proof
				+ "var range, node;"
				+ "if (window.getSelection && window.getSelection().getRangeAt) {"
				+ "range = window.getSelection().getRangeAt(0);"
				+ "node = range.createContextualFragment(html);"
				+ "range.deleteContents();"
				+ "range.insertNode(node);"
				+ "} else if (document.selection && document.selection.createRange) {"
				+ "document.selection.createRange().text=html; }");
	}

	public void insertHTML(String html)
	{
		executeScript("var html='"+html+"';"//TODO make this injection-proof
				+ "var range, node;"
				+ "if (window.getSelection && window.getSelection().getRangeAt) {"
				+ "range = window.getSelection().getRangeAt(0);"
				+ "node = range.createContextualFragment(html);"
				+ "range.insertNode(node);"
				+ "} else if (document.selection && document.selection.createRange) {"
				+ "document.selection.createRange().pasteHTML(html); }");
	}

	public String getSelectedText()
	{
		return (String) executeScript("window.getSelection().toString()");
	}

	public Object executeScript(String script)
	{
		return engine.executeScript(script);
	}

	public void addHyperlinkEventListener(HyperlinkEventListener listener)
	{
		listeners.add(listener);
	}

	public void removeHyperlinkEventListener(HyperlinkEventListener listener)
	{
		listeners.remove(listener);
	}
}
