package kr.co.ocube.develop.sosapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import kr.co.ocube.develop.sosapplication.process.SmsProcess;

public class ActiveState extends State {
    private static final String TAG = "ActiveState";
    Handler mHandler;

    public void setHandler( Handler hd){
        mHandler = hd;
    }

    @Override
    public void enter() {
        Log.d(TAG, "Enter");
        mHandler.sendMessage(mHandler.obtainMessage(Const.MSG_CHANGE_SOS_STATE_UPDATE,"ACTIVE MODE"));
//        new SmsProcess(this);
    }

    @Override
    public boolean processMessage(Message message) {
        boolean retVal;
        Log.d(TAG, "processMessage what=" + message.what);
        switch (message.what) {
            case Sos_SM.CMD_TRANSITION_TO_IDLE:
                Sos_SM.getInstance().tryToTransmit(message.what);
                retVal = HANDLED;
                break;

            case Sos_SM.CMD_TRANSITION_TO_CALLBACK:
                Sos_SM.getInstance().tryToTransmit(message.what);
                retVal = HANDLED;
                break;
            case Sos_SM.EVT_BUTTON_EVENT:
                Log.d(TAG, "SOS BUTTON_EVENT bypass! Already running!!");
                retVal = HANDLED;
                break;


            default:
                // Any message we don't understand in this state invokes unhandledMessage
                retVal = NOT_HANDLED;
                break;
        }
        return retVal;
    }

    @Override
    public void exit() {
        Log.d(TAG, "exit");

    }
}