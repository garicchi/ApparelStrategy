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
import	jp.vstone.camera.*;
import	com.google.zxing.BinaryBitmap;
import	com.google.zxing.ChecksumException;
import	com.google.zxing.FormatException;
import	com.google.zxing.LuminanceSource;
import	com.google.zxing.MultiFormatReader;
import	com.google.zxing.NotFoundException;
import	com.google.zxing.Reader;
import	com.google.zxing.Result;
import	com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import	com.google.zxing.common.HybridBinarizer;
import	java.io.IOException;
import	javax.imageio.ImageIO;
import	java.awt.image.BufferedImage;

public class WebsocketMessenger
{

	public String URL;
	public RecogResult recogresult;
	public Session session;
	public boolean isScanComplete;
	public String scanData;
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
	public void sendFile(String path)																					//@<BlockInfo>jp.vstone.block.func,48,416,240,416,False,4,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,144,416,144,416,False,3,@</BlockInfo>
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
	public void sendMessage(String message)																				//@<BlockInfo>jp.vstone.block.func,48,288,240,288,False,6,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,128,288,128,288,False,5,@</BlockInfo>

		if(message!=""){
		String sendJson = "{ \"type\":\"speech\" , \"data\":\""+message+"\" }";
		this.session.getBasicRemote().sendText(sendJson);
		}else{
		System.out.println("do not send message because message is null");
		}

		} catch (IOException e) {

		e.printStackTrace();
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void onOpenSocket(Session session,EndpointConfig eptCfg)														//@<BlockInfo>jp.vstone.block.func,352,288,1392,288,False,20,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		GlobalVariable.sotawish.Say((String)"セッションが開いたよ",MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);	//@<BlockInfo>jp.vstone.block.talk.say,416,288,416,288,False,19,@</BlockInfo>
																														//@<EndOfBlock/>
		session.addMessageHandler(new MessageHandler.Whole<String>() {													//@<BlockInfo>jp.vstone.block.freeproc,480,288,480,288,False,18,@</BlockInfo>

		public void onMessage(String message) {
		System.out.println(message);
		String[] cols = message.split(",");
		String type = cols[0];
		String data = cols[1];

		if(type.equals("speech")){
																														//@<EndOfBlock/>
		GlobalVariable.sotawish.Say((String)data,MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);			//@<BlockInfo>jp.vstone.block.talk.say,544,288,544,288,False,17,@</BlockInfo>
																														//@<EndOfBlock/>
		}																												//@<BlockInfo>jp.vstone.block.freeproc,608,288,608,288,False,16,@</BlockInfo>

		if(type.equals("picture")){

		isScanComplete = false;
																														//@<EndOfBlock/>
		while(isScanComplete==false)																					//@<BlockInfo>jp.vstone.block.while,672,288,864,288,False,15,TRUE@</BlockInfo>
		{


																														//@<OutputChild>
			{																											//@<BlockInfo>jp.vstone.block.facedetect.stillpicture,736,288,736,288,False,7,still@</BlockInfo>
				String filepath = "/var/sota/photo/";
				filepath += (String)"picture";
				boolean isTrakcing=GlobalVariable.robocam.isAliveFaceDetectTask();
				if(isTrakcing) GlobalVariable.robocam.StopFaceTraking();
				GlobalVariable.robocam.initStill(new CameraCapture(CameraCapture.CAP_IMAGE_SIZE_5Mpixel, CameraCapture.CAP_FORMAT_MJPG));
				GlobalVariable.robocam.StillPicture(filepath);

				CRobotUtil.Log("stillpicture","save picthre file to \"" + filepath +"\"");
				if(isTrakcing) GlobalVariable.robocam.StartFaceTraking();
			}																											//@<EndOfBlock/>
			try{																										//@<BlockInfo>jp.vstone.block.freeproc,800,288,800,288,False,29,@</BlockInfo>
			String filePath="/var/sota/photo/picture.jpg";
			BufferedImage image = ImageIO.read(new File(filePath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			com.google.zxing.Reader reader = new MultiFormatReader();
			Result decodeResult = reader.decode(bitmap);
			//デコード処理
			String result = decodeResult.getText();
			System.out.println(result);
			scanData = result;
			isScanComplete = true;

			}catch(Exception e){
			e.printStackTrace();
			}
																														//@<EndOfBlock/>
																														//@</OutputChild>
		}
																														//@<EndOfBlock/>
		try{																											//@<BlockInfo>jp.vstone.block.freeproc,928,288,928,288,False,14,@</BlockInfo>
																														//@<EndOfBlock/>
		sendData((String)"scan",(String)scanData);																		//@<BlockInfo>jp.vstone.block.callfunc.base,1008,288,1008,288,False,13,@</BlockInfo>	@<EndOfBlock/>
		}catch(Exception e){																							//@<BlockInfo>jp.vstone.block.freeproc,1072,288,1072,288,False,12,@</BlockInfo>
		e.printStackTrace();
		}

		}

		if(type.equals("picture2")){

		try{
																														//@<EndOfBlock/>
		{																												//@<BlockInfo>jp.vstone.block.facedetect.stillpicture,1136,288,1136,288,False,11,still@</BlockInfo>
			String filepath = "/var/sota/photo/";
			filepath += (String)"picture";
			boolean isTrakcing=GlobalVariable.robocam.isAliveFaceDetectTask();
			if(isTrakcing) GlobalVariable.robocam.StopFaceTraking();
			GlobalVariable.robocam.initStill(new CameraCapture(CameraCapture.CAP_IMAGE_SIZE_5Mpixel, CameraCapture.CAP_FORMAT_MJPG));
			GlobalVariable.robocam.StillPicture(filepath);

			CRobotUtil.Log("stillpicture","save picthre file to \"" + filepath +"\"");
			if(isTrakcing) GlobalVariable.robocam.StartFaceTraking();
		}																												//@<EndOfBlock/>
		sendFile((String)"/var/sota/photo/picture.jpg");																//@<BlockInfo>jp.vstone.block.callfunc.base,1200,288,1200,288,False,10,@</BlockInfo>	@<EndOfBlock/>
		}catch(Exception e){																							//@<BlockInfo>jp.vstone.block.freeproc,1264,288,1264,288,False,9,@</BlockInfo>
		e.printStackTrace();
		}

		}
																														//@<EndOfBlock/>
																														//@<BlockInfo>jp.vstone.block.freeproc,1328,288,1328,288,False,8,@</BlockInfo>
		}

		});
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public WebsocketMessenger()																							//@<BlockInfo>jp.vstone.block.func.constructor,16,16,480,16,False,26,@</BlockInfo>
	{
																														//@<OutputChild>
		URL="";																											//@<BlockInfo>jp.vstone.block.variable,80,16,80,16,False,25,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*RecogResult recogresult*/;																					//@<BlockInfo>jp.vstone.block.variable,160,16,160,16,False,24,break@</BlockInfo>
																														//@<EndOfBlock/>
		session=null;																									//@<BlockInfo>jp.vstone.block.variable,272,16,272,16,False,23,break@</BlockInfo>
																														//@<EndOfBlock/>
		isScanComplete=false;																							//@<BlockInfo>jp.vstone.block.variable,352,16,352,16,False,22,break@</BlockInfo>
																														//@<EndOfBlock/>
		scanData="";																									//@<BlockInfo>jp.vstone.block.variable,416,16,416,16,False,21,break@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>
	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void sendData(String type,String message)																	//@<BlockInfo>jp.vstone.block.func,48,176,240,176,False,28,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,128,176,128,176,False,27,@</BlockInfo>

		if(message!=""){
		String sendJson = "{ \"type\":\""+type+"\" , \"data\":\""+message+"\" }";
		this.session.getBasicRemote().sendText(sendJson);
		}else{
		System.out.println("do not send message because message is null");
		}

		} catch (Exception e) {

		e.printStackTrace();
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

}
