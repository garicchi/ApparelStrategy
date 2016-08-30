//このソースは、VstoneMagicによって自動生成されました。
//ソースの内容を書き換えた場合、VstoneMagicで開けなくなる場合があります。
package	jp.co.mysota;
import	main.main.GlobalVariable;
import	jp.vstone.RobotLib.*;
import	jp.vstone.sotatalk.*;
import	jp.vstone.sotatalk.SpeechRecog.*;
import	java.net.URI;
import	java.io.IOException;
import	javax.websocket.ClientEndpointConfig;
import	javax.websocket.CloseReason;
import	javax.websocket.ContainerProvider;
import	javax.websocket.DeploymentException;
import	javax.websocket.Endpoint;
import	javax.websocket.EndpointConfig;
import	javax.websocket.MessageHandler;
import	javax.websocket.Session;
import	javax.websocket.WebSocketContainer;
import	java.util.Properties;
import	java.io.*;
import	java.util.*;

public class WebsocketMessenger
{

	public String URL;
	public RecogResult recogresult;
	public Session session;
	public void connect(String URL)																						//@<BlockInfo>jp.vstone.block.func,560,400,864,400,False,2,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();										//@<BlockInfo>jp.vstone.block.freeproc,640,400,640,400,False,1,@</BlockInfo>
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

		try {

		this.session = container.connectToServer(new Endpoint(){
		@Override
		public void onOpen(Session session,EndpointConfig endpointConfig){
		try{
			onOpenSocket(session,endpointConfig);
		}catch(Exception e){
		e.printStackTrace();
		}
		}
		}, config, URI.create(URL));


		} catch (DeploymentException e) {

		e.printStackTrace();
		} catch (IOException e)
		{e.printStackTrace();}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void sendFile(String path)																					//@<BlockInfo>jp.vstone.block.func,48,416,240,416,False,3,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,144,416,144,416,False,15,@</BlockInfo>
		File f=new File(path);
		            int length=(int)f.length();
		            byte[] buf=new byte[length];
		            FileInputStream fs=new FileInputStream(path);
		            fs.read(buf);
		            fs.close();
		            String encoded = Base64.getEncoder().encodeToString(buf);
		String sendJson = "{ \"type\":\"picture\" , \"data\":\""+encoded+"\" }";
		            this.session.getBasicRemote().sendText(sendJson);

		} catch (IOException e) {

		e.printStackTrace();
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public WebsocketMessenger()																							//@<BlockInfo>jp.vstone.block.func.constructor,16,16,432,32,False,7,@</BlockInfo>
	{
																														//@<OutputChild>
		URL="";																											//@<BlockInfo>jp.vstone.block.variable,80,16,80,16,False,6,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*RecogResult recogresult*/;																					//@<BlockInfo>jp.vstone.block.variable,160,16,160,16,False,5,break@</BlockInfo>
																														//@<EndOfBlock/>
		session=null;																									//@<BlockInfo>jp.vstone.block.variable,272,16,272,16,False,4,break@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>
	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void sendMessage(String message)																				//@<BlockInfo>jp.vstone.block.func,48,288,240,288,False,9,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,128,288,128,288,False,8,@</BlockInfo>
		String sendJson = "{ \"type\":\"speech\" , \"data\":\""+message+"\" }";
		this.session.getBasicRemote().sendText(sendJson);

		} catch (IOException e) {

		e.printStackTrace();
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void onOpenSocket(Session session,EndpointConfig eptCfg)														//@<BlockInfo>jp.vstone.block.func,432,288,768,288,False,14,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		GlobalVariable.sotawish.Say((String)"セッションが開いたよ",MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);	//@<BlockInfo>jp.vstone.block.talk.say,512,288,512,288,False,13,@</BlockInfo>
																														//@<EndOfBlock/>
		session.addMessageHandler(new MessageHandler.Whole<String>() {													//@<BlockInfo>jp.vstone.block.freeproc,576,288,576,288,False,12,@</BlockInfo>

		public void onMessage(String message) {
																														//@<EndOfBlock/>
		GlobalVariable.sotawish.Say((String)message,MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);			//@<BlockInfo>jp.vstone.block.talk.say,640,288,640,288,False,11,@</BlockInfo>
																														//@<EndOfBlock/>
																														//@<BlockInfo>jp.vstone.block.freeproc,704,288,704,288,False,10,@</BlockInfo>
		}

		});
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

}
