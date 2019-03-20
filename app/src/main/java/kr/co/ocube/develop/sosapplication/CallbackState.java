package kr.co.ocube.develop.sosapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class CallbackState extends State {
    private static final String TAG = "CallbackState";
    Handler mHandler;
    Timer mCallbackTimeout;
    Sos_SM hsm;

    public void setHandler( Handler hd){
        mHandler = hd;
    }

    @Override
    public void enter() {
        Log.d(TAG, "Enter");
        hsm = Sos_SM.getInstance();
        mHandler.sendMessage(mHandler.obtainMessage(Const.MSG_CHANGE_SOS_STATE_UPDATE,"CALLBACK MODE"));

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                hsm.sendMessage(hsm.obtainMessage(Sos_SM.CMD_TRANSITION_TO_IDLE));
            }
        };
        mCallbackTimeout = new Timer();
        mCallbackTimeout.schedule(tt,20000); //TODO : need to changed variable value
//        new SmsProcess(this);


    }

    @Override
    public boolean processMessage(Message message) {
        boolean retVal;
        Log.d(TAG, "processMessage what=" + message.what);
        switch (message.what) {
            case Sos_SM.CMD_TRANSITION_TO_IDLE:
                hsm.tryToTransmit(message.what);
                retVal = HANDLED;
                break;

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
        mCallbackTimeout.cancel();

    }
}
