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
import	java.awt.Color;
import	imageprocessor.*;

public class WebsocketMessenger
{

	public String URL;
	public RecogResult recogresult;
	public Session session;
	public boolean isScanComplete;
	public String scanData;
	public CRobotPose pose;
	public void sendData(String type,String message)																	//@<BlockInfo>jp.vstone.block.func,48,176,240,176,False,2,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,128,176,128,176,False,1,@</BlockInfo>

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

	//@<Separate/>
	public void connect(String URL)																						//@<BlockInfo>jp.vstone.block.func,32,528,240,528,False,4,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();										//@<BlockInfo>jp.vstone.block.freeproc,112,528,112,528,False,3,@</BlockInfo>
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
	public void onOpenSocket(Session session,EndpointConfig eptCfg)														//@<BlockInfo>jp.vstone.block.func,384,160,1120,544,False,26,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		GlobalVariable.sotawish.Say((String)"セッションが開いたよ",MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);	//@<BlockInfo>jp.vstone.block.talk.say,448,160,448,160,False,25,@</BlockInfo>
																														//@<EndOfBlock/>
		session.addMessageHandler(new MessageHandler.Whole<String>() {													//@<BlockInfo>jp.vstone.block.freeproc,512,160,512,160,False,24,@</BlockInfo>

		public void onMessage(String message) {
		System.out.println(message);
		String[] cols = message.split(",");
		String type = cols[0];
		String data = cols[1];

		if(type.equals("speech")){
																														//@<EndOfBlock/>
		GlobalVariable.sotawish.Say((String)data,MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);			//@<BlockInfo>jp.vstone.block.talk.say,576,160,576,160,False,23,@</BlockInfo>
																														//@<EndOfBlock/>
		}																												//@<BlockInfo>jp.vstone.block.freeproc,384,288,384,288,False,22,@</BlockInfo>

		if(type.equals("picture")){

		isScanComplete = false;
																														//@<EndOfBlock/>
		CRobotUtil.wait((int)1000);																						//@<BlockInfo>jp.vstone.block.wait,448,288,448,288,False,21,コメント@</BlockInfo>	@<EndOfBlock/>
		while(isScanComplete==false)																					//@<BlockInfo>jp.vstone.block.while,528,288,800,288,False,20,TRUE@</BlockInfo>
		{


																														//@<OutputChild>
			{																											//@<BlockInfo>jp.vstone.block.facedetect.stillpicture,592,288,592,288,False,7,still@</BlockInfo>
				String filepath = "/var/sota/photo/";
				filepath += (String)"picture";
				boolean isTrakcing=GlobalVariable.robocam.isAliveFaceDetectTask();
				if(isTrakcing) GlobalVariable.robocam.StopFaceTraking();
				GlobalVariable.robocam.initStill(new CameraCapture(CameraCapture.CAP_IMAGE_SIZE_5Mpixel, CameraCapture.CAP_FORMAT_MJPG));
				GlobalVariable.robocam.StillPicture(filepath);

				CRobotUtil.Log("stillpicture","save picthre file to \"" + filepath +"\"");
				if(isTrakcing) GlobalVariable.robocam.StartFaceTraking();
			}																											//@<EndOfBlock/>
			GlobalVariable.sotawish.Say((String)"読み取ってるよ。少し待ってね",MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);	//@<BlockInfo>jp.vstone.block.talk.say,672,288,672,288,False,6,@</BlockInfo>
																														//@<EndOfBlock/>
			try{																										//@<BlockInfo>jp.vstone.block.freeproc,736,288,736,288,False,5,@</BlockInfo>
			String filePath="/var/sota/photo/picture.jpg";
			String processPath = filePath+"_process.jpg";

			ImageProcessor.GrayScale(new File(filePath));

			BufferedImage image = ImageIO.read(new File(processPath));
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			com.google.zxing.Reader reader = new MultiFormatReader();
			Result decodeResult = reader.decode(bitmap);

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
		try{																											//@<BlockInfo>jp.vstone.block.freeproc,912,288,912,288,False,19,@</BlockInfo>
																														//@<EndOfBlock/>
		sendData((String)"scan",(String)scanData);																		//@<BlockInfo>jp.vstone.block.callfunc.base,992,288,992,288,False,18,@</BlockInfo>	@<EndOfBlock/>
		}catch(Exception e){																							//@<BlockInfo>jp.vstone.block.freeproc,528,416,528,416,False,38,@</BlockInfo>
		e.printStackTrace();
		}

		}

		if(type.equals("capture")){

		try{
																														//@<EndOfBlock/>
		{																												//@<BlockInfo>jp.vstone.block.facedetect.stillpicture,592,416,592,416,False,17,still@</BlockInfo>
			String filepath = "/var/sota/photo/";
			filepath += (String)"picture";
			boolean isTrakcing=GlobalVariable.robocam.isAliveFaceDetectTask();
			if(isTrakcing) GlobalVariable.robocam.StopFaceTraking();
			GlobalVariable.robocam.initStill(new CameraCapture(CameraCapture.CAP_IMAGE_SIZE_5Mpixel, CameraCapture.CAP_FORMAT_MJPG));
			GlobalVariable.robocam.StillPicture(filepath);

			CRobotUtil.Log("stillpicture","save picthre file to \"" + filepath +"\"");
			if(isTrakcing) GlobalVariable.robocam.StartFaceTraking();
		}																												//@<EndOfBlock/>
		sendFile((String)"/var/sota/photo/picture.jpg");																//@<BlockInfo>jp.vstone.block.callfunc.base,656,416,656,416,False,16,@</BlockInfo>	@<EndOfBlock/>
		}catch(Exception e){																							//@<BlockInfo>jp.vstone.block.freeproc,720,416,720,416,False,15,@</BlockInfo>
		e.printStackTrace();
		}

		}
																														//@<EndOfBlock/>
																														//@<BlockInfo>jp.vstone.block.freeproc,512,512,512,512,False,14,@</BlockInfo>

		if(type.equals("hide")){

		System.out.println("hide through");

		try{
																														//@<EndOfBlock/>
		pose = new CRobotPose();																						//@<BlockInfo>jp.vstone.block.pose,592,512,592,512,False,13,コメント@</BlockInfo>
		pose.SetPose(	new Byte[]{1,2,3,4,5,6,7,8},
						new Short[]{1315,145,-698,-186,695,-1,50,-6}
						);
		pose.SetTorque(	new Byte[]{1,2,3,4,5,6,7,8},
						new Short[]{100,100,100,100,100,100,100,100}
						);
		pose.SetLed(	new Byte[]{0,1,2,8,9,10,11,12,13},
						new Short[]{0,-255,0,255,0,0,255,5,0}
						);
		GlobalVariable.motion.play(pose,1000);
		CRobotUtil.wait(1000);																							//@<EndOfBlock/>
		}catch(Exception e){																							//@<BlockInfo>jp.vstone.block.freeproc,704,512,704,512,False,12,@</BlockInfo>
		e.printStackTrace();
		}

		}
																														//@<EndOfBlock/>
																														//@<BlockInfo>jp.vstone.block.freeproc,560,608,560,608,False,11,@</BlockInfo>

		if(type.equals("normal")){
																														//@<EndOfBlock/>
		pose = new CRobotPose();																						//@<BlockInfo>jp.vstone.block.pose,640,608,640,608,False,10,コメント@</BlockInfo>
		pose.SetPose(	new Byte[]{1,2,3,4,5,6,7,8},
						new Short[]{0,-900,0,900,0,0,0,0}
						);
		pose.SetTorque(	new Byte[]{1,2,3,4,5,6,7,8},
						new Short[]{100,100,100,100,100,100,100,100}
						);
		pose.SetLed(	new Byte[]{0,1,2,8,9,10,11,12,13},
						new Short[]{0,-255,0,180,80,0,180,80,0}
						);
		GlobalVariable.motion.play(pose,1000);
		CRobotUtil.wait(1000);																							//@<EndOfBlock/>
		}																												//@<BlockInfo>jp.vstone.block.freeproc,768,608,768,608,False,9,@</BlockInfo>
																														//@<EndOfBlock/>
																														//@<BlockInfo>jp.vstone.block.freeproc,1056,544,1056,544,False,8,@</BlockInfo>
		}

		});
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

	//@<Separate/>
	public WebsocketMessenger()																							//@<BlockInfo>jp.vstone.block.func.constructor,16,16,736,16,False,33,@</BlockInfo>
	{
																														//@<OutputChild>
		URL="";																											//@<BlockInfo>jp.vstone.block.variable,80,16,80,16,False,32,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*RecogResult recogresult*/;																					//@<BlockInfo>jp.vstone.block.variable,160,16,160,16,False,31,break@</BlockInfo>
																														//@<EndOfBlock/>
		session=null;																									//@<BlockInfo>jp.vstone.block.variable,272,16,272,16,False,30,break@</BlockInfo>
																														//@<EndOfBlock/>
		isScanComplete=false;																							//@<BlockInfo>jp.vstone.block.variable,352,16,352,16,False,29,break@</BlockInfo>
																														//@<EndOfBlock/>
		scanData="";																									//@<BlockInfo>jp.vstone.block.variable,416,16,416,16,False,28,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*CRobotPose pose*/;																							//@<BlockInfo>jp.vstone.block.variable,480,16,480,16,False,27,break@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>
	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void sendFile(String path)																					//@<BlockInfo>jp.vstone.block.func,48,416,304,416,False,35,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,144,416,144,416,False,34,@</BlockInfo>
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
	public void sendMessage(String message)																				//@<BlockInfo>jp.vstone.block.func,48,288,272,288,False,37,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		try {																											//@<BlockInfo>jp.vstone.block.freeproc,128,288,128,288,False,36,@</BlockInfo>

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

}
