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
	public void talkCheck()																								//@<BlockInfo>jp.vstone.block.func,0,192,960,304,False,9,@</BlockInfo>
	throws SpeechRecogAbortException {
		if(!GlobalVariable.TRUE) throw new SpeechRecogAbortException("default");

																														//@<OutputChild>
		WebsocketMessenger.connect((String)"ws://192.168.128.100:8080/ws");												//@<BlockInfo>jp.vstone.block.callfunc.base,112,192,112,192,False,8,@</BlockInfo>	@<EndOfBlock/>
		while(GlobalVariable.TRUE)																						//@<BlockInfo>jp.vstone.block.while.endless,192,192,704,192,False,7,Endless@</BlockInfo>
		{

																														//@<OutputChild>
			speechRecogResult = GlobalVariable.recog.getResponsewithAbort((int)10000,(int)3);							//@<BlockInfo>jp.vstone.block.talk.speechrecog.get,304,192,304,192,False,6,音声認識して、得られた結果（文字列）をspeechRecogResultに代入します。@</BlockInfo>
			if(speechRecogResult == null) speechRecogResult = "";

																														//@<EndOfBlock/>
			WebsocketMessenger.sendMessage((String)speechRecogResult);													//@<BlockInfo>jp.vstone.block.callfunc.base,400,192,400,192,False,5,@</BlockInfo>	@<EndOfBlock/>
																														//@</OutputChild>
		}
																														//@<EndOfBlock/>
																														//@</OutputChild>

	}																													//@<EndOfBlock/>

}
