package kr.co.ocube.develop.sosapplication.process;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.telecom.Call;
import android.telecom.VideoProfile;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.ocube.develop.sosapplication.Const;
import kr.co.ocube.develop.sosapplication.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.M)
public class VoiceProcess {
    private static final String TAG = "VoiceProcess";
    public static final int CALL_STATE_CANCEL = 4;
    Context mContext;
    int mPreviousCallState;
    Timer mVoiceCallTimeout;
    int nRetryCount = 0;            //TODO : need to changed variable value
    final int nMaxRetryCount = 3; //TODO : need to changed variable value
    TimerTask m_Tt;
    static Call.Callback mCallback;
    SharedPreferences setting;
    static String PHONE_NUM = "00000000000";

    public VoiceProcess(Context c) {
        Log.d(TAG, "ctor E");
        mContext = c;
        setting = mContext.getSharedPreferences("P_PHONE_NUM", 0);
        PHONE_NUM = setting.getString ("P_PHONE_NUM", "000 0000 0000");

        registerCallState();
        mVoiceCallTimeout = new Timer();
        Log.d(TAG, "ctor X");
    }

    public enum CallCommand {
        END_CALL,
        ANSWER_CALL
    }

    VoiceProcess() {
        Log.d(TAG, "ctor E");
        Log.d(TAG, "ctor X");
    }

    public void registerCallState() {
        mCallback = new Call.Callback() {
            @Override
            public void onConnectionEvent(Call call, String event, Bundle extras) {
                Log.d(TAG, "Call.Callback.onConnectionEvent: " + event + ", " + call.getState());
            }

            @Override
            public void onStateChanged(Call call, int state) {
                Log.d(TAG, "Call.Callback.onStateChanged: " + state + ", " + toString(call.getState()));
                ((MainActivity) getContext()).onUpdateText(Const.MSG_CALL_STATUS, toString(state));

                switch (state) {
                    case Call.STATE_DIALING: {
                    }
                    break;
                    case Call.STATE_DISCONNECTED:
                    case Call.STATE_DISCONNECTING:
                        if (Call.STATE_ACTIVE == mPreviousCallState) {
                            sendToStateHandler(Const.MSG_DATA_SEND, Const.MSG_SOS_VOICE_CALL_TERMINATION, 0);
                        }
                        break;

                    case Call.STATE_RINGING: {
                        String incomingNumber = call.getDetails().getHandle().toString();
                        Log.d(TAG, "CALL_STATE_RINGING: incomingNum:" + incomingNumber);
                        final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber);
                        if (mContext != null)
                            Toast.makeText(mContext, "Incoming: " + phone_number, Toast.LENGTH_LONG).show();
                        ((MainActivity) mContext).onUpdateText(Const.MSG_DATA_KIND_OF, "-");
                        if (incomingNumber.contains(VoiceProcess.PHONE_NUM)) {
                            if ((((MainActivity) mContext).getSosState()).equals("CALLBACK MODE")) {
                                call.answer(VideoProfile.STATE_AUDIO_ONLY);
//                                sosCallCommand(CallCommand.ANSWER_CALL);
                            } else {
                                if (mContext != null)
                                    Toast.makeText(mContext, "reject Call!", Toast.LENGTH_LONG).show();
                                call.disconnect();
//                                sosCallCommand(CallCommand.END_CALL);
                            }
                        } else {
                            call.disconnect();
//                            sosCallCommand(CallCommand.END_CALL);
                            if (mContext != null) Toast.makeText(mContext, "Not allow number!", Toast.LENGTH_LONG).show();
                        }
                    }
                    break;

                    case Call.STATE_ACTIVE: {
                        if (mPreviousCallState == Call.STATE_RINGING ) {
                            sendToStateHandler(Const.MSG_CHANGE_STATE_TO_ACTIVE,0,0);
                        } else {
                            Log.d(TAG, "retry Timeout clear! ");
                            m_Tt.cancel();
                        }
                        break;
                    }
                    default:
                        break;
                }
                mPreviousCallState = state;
                Log.d(TAG, "mPreviousCallState : "+mPreviousCallState);
            }

            private String toString(int state) {
                switch (state) {
                    case Call.STATE_ACTIVE:
                        return "STATE_ACTIVE";
                    case Call.STATE_CONNECTING:
                        return "STATE_CONNECTING";
                    case Call.STATE_DIALING:
                        return "STATE_DIALING";
                    case Call.STATE_DISCONNECTED:
                        return "STATE_DISCONNECTED";
                    case Call.STATE_DISCONNECTING:
                        return "STATE_DISCONNECTING";
                    case Call.STATE_HOLDING:
                        return "STATE_HOLDING";
                    case Call.STATE_NEW:
                        return "STATE_NEW";
                    case Call.STATE_PULLING_CALL:
                        return "STATE_PULLING_CALL";
                    case Call.STATE_RINGING:
                        return "STATE_RINGING";
                    case Call.STATE_SELECT_PHONE_ACCOUNT:
                        return "STATE_SELECT_PHONE_ACCOUNT";
                    default:
                        return "NULL";
                }
            }
        };
    }
    public void tryToCalling() {
        nRetryCount = 0; // init Retry Count
        /*if (versionCheck(mContext) == 1)*/
        PHONE_NUM = setting.getString ("P_PHONE_NUM", "000 0000 0000");
        calling(PHONE_NUM);
    }

    @SuppressLint("MissingPermission")
    private void calling(String PhoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + PhoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        m_Tt = createTimerTack();
        mVoiceCallTimeout.schedule(m_Tt, 20000); //TODO : need to changed variable value
        mContext.startActivity(intent);
    }


    @Deprecated
    int sosCallCommand(CallCommand cmd) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> mClass = Class.forName(mTelephonyManager.getClass().getName());
            Method mMethod = mClass.getDeclaredMethod("getITelephony");
            mMethod.setAccessible(true);
            ITelephony mITelephony = (ITelephony) mMethod.invoke(mTelephonyManager);
            if (cmd == CallCommand.END_CALL) {
                Log.d(TAG, "endCall");
                mITelephony.endCall();
            } else {
                Log.d(TAG, "answerRingingCall");
                //mITelephony.answerRingingCall(); //Can not run this API because permission android 6.0(API 23) >= current version
                try {
                    Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
                    buttonUp.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
                    mContext.sendOrderedBroadcast(buttonUp, null);
                    buttonUp = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private TimerTask createTimerTack() {
        return new TimerTask() {
            @Override
            public void run() {
                sosCallCommand(CallCommand.END_CALL);

                nRetryCount++;
                if (nRetryCount > nMaxRetryCount) {
                    sendToUiHandler(Const.MSG_TOAST_UPDATE, "Max retry expired");
                    if (((MainActivity) mContext).getDataSendSuccess() == true) {
                        Log.d(TAG, "retry count expired But Data send successed So try to send to vct msg!!");
                        sendToStateHandler(Const.MSG_DATA_SEND, Const.MSG_SOS_VOICE_CALL_TERMINATION, 0);
                    } else {
                        Log.d(TAG, "retry count expired And Data send fail. So state change to Callback!!");
                        sendToStateHandler(Const.MSG_CHANGE_STATE_TO_CALLBACK,0,0);
                    }
                    return;
                }
                sendToUiHandler(Const.MSG_TOAST_UPDATE, "Call Retry [" + nRetryCount + "]");
                try
                {
                    Thread.sleep(5 * 1000);
                }catch(InterruptedException ie)
                {
                    ie.printStackTrace();
                }
                Log.d(TAG, "Calling reTry num[" + nRetryCount +"]");
                calling(PHONE_NUM);
            }
        };
    }

    public static void start(Call call) {
        call.registerCallback(mCallback);
        mCallback.onStateChanged(call,call.getState());
    }

    public Context getContext(){
        return mContext;
    }

    public void sendToUiHandler(int msgId, Object o){
        ((MainActivity) mContext).getUiHandler().sendMessage(((MainActivity) mContext).getUiHandler().obtainMessage(msgId, o));
    }

    public void sendToStateHandler(int msgId, int arg1, int arg2){
        ((MainActivity) mContext).getStateHandler().sendMessage(((MainActivity) mContext).getStateHandler().obtainMessage(msgId, arg1, arg2));
    }
}