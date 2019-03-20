package kr.co.ocube.develop.sosapplication.process;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMSBroadCast extends BroadcastReceiver {
    private static final String TAG = "SMSBroadCast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[])bundle.get("pdus");
            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for(int i = 0; i < messages.length; i++) {
                smsMessage[i] = SmsMessage.createFromPdu((byte[])messages[i]);
            }
            Date curDate = new Date(smsMessage[0].getTimestampMillis());
            SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분 ss초", Locale.KOREA);

            String originDate = mDateFormat.format(curDate);
            String origNumber = smsMessage[0].getOriginatingAddress();
            String Message = smsMessage[0].getMessageBody().toString();

            Log.d(TAG, "receive SMS : Date : "+ originDate + ", origNumber : " + origNumber +", Message:" + Message);
            Toast.makeText(context,"receive SMS : Date : "+ originDate + ", origNumber : " + origNumber +", Message:" + Message, Toast.LENGTH_SHORT).show();
        }
    }
}
