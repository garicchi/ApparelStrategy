//このソースは、VstoneMagicによって自動生成されました。
//ソースの内容を書き換えた場合、VstoneMagicで開けなくなる場合があります。
package	jp.co.mysota;
import	main.main.GlobalVariable;
import	jp.vstone.RobotLib.*;
import	jp.vstone.sotatalk.*;
import	jp.vstone.sotatalk.SpeechRecog.*;
import	java.io.*;
import	jp.vstone.camera.*;

public class TalkChecker
{

	public String speechRecogResult;
	public RecogResult recogresult;
	public jp.co.mysota.WebsocketMessenger WebsocketMessenger;
	public TalkChecker()																								//@<BlockInfo>jp.vstone.block.func.constructor,16,16,496,16,False,4,@</BlockInfo>
	{
																														//@<OutputChild>
		/*String speechRecogResult*/;																					//@<BlockInfo>jp.vstone.block.variable,80,16,80,16,False,3,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*RecogResult recogresult*/;																					//@<BlockInfo>jp.vstone.block.variable,144,16,144,16,False,2,break@</BlockInfo>
																														//@<EndOfBlock/>
		WebsocketMessenger=new jp.co.mysota.WebsocketMessenger();														//@<BlockInfo>jp.vstone.block.variable,208,16,208,16,False,1,break@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>
	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void talkCheck()																								//@<BlockInfo>jp.vstone.block.func,0,192,960,304,False,13,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		WebsocketMessenger.connect((String)"ws://192.168.128.105:8080/ws");												//@<BlockInfo>jp.vstone.block.callfunc.base,112,192,112,192,False,12,@</BlockInfo>	@<EndOfBlock/>
		while(GlobalVariable.TRUE)																						//@<BlockInfo>jp.vstone.block.while.endless,192,192,800,192,False,11,Endless@</BlockInfo>
		{

																														//@<OutputChild>
			speechRecogResult = GlobalVariable.recog.getYesorNowithAbort((int)60000 , (int)3);							//@<BlockInfo>jp.vstone.block.talk.getyesno,288,96,688,96,False,10,音声認識を行い、肯定/否定を取得する。取得結果はメンバー変数のspeechRecogResultに格納され、肯定なら「YES」、否定なら「NO」、聞き取り失敗なら長さ0の文字列が代入される。@</BlockInfo>
			if(speechRecogResult==null) speechRecogResult="";

			if(speechRecogResult.equals("YES"))
			{
																														//@<OutputChild>
				{																										//@<BlockInfo>jp.vstone.block.facedetect.stillpicture,368,96,368,96,False,7,still@</BlockInfo>
					String filepath = "/var/sota/photo/";
					filepath += (String)"picture";
					boolean isTrakcing=GlobalVariable.robocam.isAliveFaceDetectTask();
					if(isTrakcing) GlobalVariable.robocam.StopFaceTraking();
					GlobalVariable.robocam.initStill(new CameraCapture(CameraCapture.CAP_IMAGE_SIZE_5Mpixel, CameraCapture.CAP_FORMAT_MJPG));
					GlobalVariable.robocam.StillPicture(filepath);

					CRobotUtil.Log("stillpicture","save picthre file to \"" + filepath +"\"");
					if(isTrakcing) GlobalVariable.robocam.StartFaceTraking();
				}																										//@<EndOfBlock/>
				WebsocketMessenger.sendFile((String)"/var/sota/photo/picture.jpg");										//@<BlockInfo>jp.vstone.block.callfunc.base,464,96,464,96,False,6,@</BlockInfo>	@<EndOfBlock/>
				GlobalVariable.sotawish.Say((String)"送ったぞ",MotionAsSotaWish.MOTION_TYPE_TALK,(int)11,(int)13,(int)11);	//@<BlockInfo>jp.vstone.block.talk.say,576,96,576,96,False,5,@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>

			}else if(speechRecogResult.equals("NO"))
			{
																														//@<OutputChild>
				speechRecogResult = GlobalVariable.recog.getResponsewithAbort((int)10000,(int)3);						//@<BlockInfo>jp.vstone.block.talk.speechrecog.get,384,192,384,192,False,9,音声認識して、得られた結果（文字列）をspeechRecogResultに代入します。@</BlockInfo>
				if(speechRecogResult == null) speechRecogResult = "";

																														//@<EndOfBlock/>
				WebsocketMessenger.sendMessage((String)speechRecogResult);												//@<BlockInfo>jp.vstone.block.callfunc.base,480,192,480,192,False,8,@</BlockInfo>	@<EndOfBlock/>
																														//@</OutputChild>

			}else
			{
																														//@<OutputChild>
																														//@</OutputChild>

			}
																														//@<EndOfBlock/>
																														//@</OutputChild>
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

}
