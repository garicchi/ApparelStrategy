//このソースは、VstoneMagicによって自動生成されました。
//ソースの内容を書き換えた場合、VstoneMagicで開けなくなる場合があります。
package	jp.co.mysota;
import	main.main.GlobalVariable;
import	jp.vstone.RobotLib.*;
import	jp.vstone.sotatalk.*;
import	jp.vstone.sotatalk.SpeechRecog.*;
import	java.io.*;
import	jp.vstone.camera.*;
import	java.awt.Color;

public class TalkChecker
{

	public String speechRecogResult;
	public RecogResult recogresult;
	public jp.co.mysota.WebsocketMessenger WebsocketMessenger;
	public CRobotPose pose;
	public TalkChecker()																								//@<BlockInfo>jp.vstone.block.func.constructor,16,16,560,16,False,5,@</BlockInfo>
	{
																														//@<OutputChild>
		/*String speechRecogResult*/;																					//@<BlockInfo>jp.vstone.block.variable,80,16,80,16,False,4,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*RecogResult recogresult*/;																					//@<BlockInfo>jp.vstone.block.variable,144,16,144,16,False,3,break@</BlockInfo>
																														//@<EndOfBlock/>
		WebsocketMessenger=new jp.co.mysota.WebsocketMessenger();														//@<BlockInfo>jp.vstone.block.variable,208,16,208,16,False,2,break@</BlockInfo>
																														//@<EndOfBlock/>
		/*CRobotPose pose*/;																							//@<BlockInfo>jp.vstone.block.variable,272,16,272,16,False,1,break@</BlockInfo>
																														//@<EndOfBlock/>
																														//@</OutputChild>
	}																													//@<EndOfBlock/>

	//@<Separate/>
	public void talkCheck()																								//@<BlockInfo>jp.vstone.block.func,0,192,960,304,False,14,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		WebsocketMessenger.connect((String)"ws://192.168.128.100:8080/ws");												//@<BlockInfo>jp.vstone.block.callfunc.base,112,192,112,192,False,13,@</BlockInfo>	@<EndOfBlock/>
		while(GlobalVariable.TRUE)																						//@<BlockInfo>jp.vstone.block.while.endless,192,192,752,192,False,12,Endless@</BlockInfo>
		{

																														//@<OutputChild>
			speechRecogResult = GlobalVariable.recog.getResponsewithAbort((int)10000,(int)3);							//@<BlockInfo>jp.vstone.block.talk.speechrecog.get,272,192,272,192,False,11,音声認識して、得られた結果（文字列）をspeechRecogResultに代入します。@</BlockInfo>
			if(speechRecogResult == null) speechRecogResult = "";

																														//@<EndOfBlock/>
			if (speechRecogResult.contains("向いて")																		//@<BlockInfo>jp.vstone.block.freeproc,336,192,336,192,False,15,@</BlockInfo>
			||speechRecogResult.contains("うしろ")
			speechRecogResult.contains("後ろ")){
																														//@<EndOfBlock/>
			pose = new CRobotPose();																					//@<BlockInfo>jp.vstone.block.pose,400,192,400,192,False,10,コメント@</BlockInfo>
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
			CRobotUtil.wait(1000);																						//@<EndOfBlock/>
			}																											//@<BlockInfo>jp.vstone.block.freeproc,464,192,464,192,False,9,@</BlockInfo>


			if(speechRecogResult.contains("終わ")
			||speechRecogResult.contains("着た")
			||speechRecogResult.contains("きた")
			||speechRecogResult.contains("おわっ")){
																														//@<EndOfBlock/>
			pose = new CRobotPose();																					//@<BlockInfo>jp.vstone.block.pose,528,192,528,192,False,8,コメント@</BlockInfo>
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
			CRobotUtil.wait(1000);																						//@<EndOfBlock/>
			}																											//@<BlockInfo>jp.vstone.block.freeproc,592,192,592,192,False,7,@</BlockInfo>
																														//@<EndOfBlock/>
			WebsocketMessenger.sendMessage((String)speechRecogResult);													//@<BlockInfo>jp.vstone.block.callfunc.base,672,192,672,192,False,6,@</BlockInfo>	@<EndOfBlock/>
																														//@</OutputChild>
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

}
