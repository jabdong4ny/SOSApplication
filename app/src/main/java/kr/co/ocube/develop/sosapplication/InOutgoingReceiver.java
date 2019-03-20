package kr.co.ocube.develop.sosapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

//This Class is purpose is switching screens to SOS app
public class InOutgoingReceiver extends BroadcastReceiver {
    private static final String TAG = "InOutgoingReceiver";
    private static String mLastState;
    Context mContext;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        mContext = context;
        Log.w(TAG, "State = " + state);

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)
        ||TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state))
        {

//            if (state.equals(mLastState))
//            {
//                Log.w(TAG, "RINGING! Or OFFHOOK!");
//                Intent new_intent = new Intent(mContext, BackFakeCallActivity.class);
//                mContext.startActivity(new_intent);
//                return;
//            } else
//            {
//                mLastState = state;
//            }

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "postDelay");
                    Intent new_intent = new Intent(mContext, BackFakeCallActivity.class);
                    new_intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(new_intent);
                }
            }, 1000 );
        }
    }
}