package kr.co.ocube.develop.sosapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class IdleState extends State {
    private static final String TAG = "IdleState";
    public static final int CMD_2 = 2;
    public static final int CMD_3 = 3;

    Sos_SM hsm;
    Handler mHandler;

    public void setHandler( Handler hd){
        mHandler = hd;
    }

    @Override
    public void enter() {
        Log.d(TAG, "Enter");
        hsm = Sos_SM.getInstance();
        mHandler.sendMessage(mHandler.obtainMessage(Const.MSG_CHANGE_SOS_STATE_UPDATE,"IDLE MODE"));
    }


    @Override
    public boolean processMessage(Message message) {
        boolean retVal;
        Log.d(TAG, "processMessage what=" + message.what);
        switch (message.what) {
            case Sos_SM.CMD_TRANSITION_TO_ACTIVE:
                hsm.tryToTransmit(message.what);
                retVal = HANDLED;
                break;

            case Sos_SM.EVT_BUTTON_EVENT:
                Log.d(TAG, "SOS BUTTON_EVENT");
                hsm.sendMessage(hsm.obtainMessage(Sos_SM.CMD_TRANSITION_TO_ACTIVE));
                mHandler.sendMessage(mHandler.obtainMessage(Const.MSG_TRY_TO_CALLING));
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
        mHandler.sendMessage(mHandler.obtainMessage(Const.MSG_DATA_SEND,Const.MSG_SOS_NOTIFICATION,0));
    }
}