package kr.co.ocube.develop.sosapplication.process;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;


public class SmsProcess {

    private static final String TAG = "SmsProcess";
    private static final String PhoneNum = "01066767793"; //TODO: my phone number for test
    private static final String SendData= "SOS 테스트 전송 Data"; //TODO: SMS comment for test
    Context mContext;
    BroadcastReceiver mReceiver;

    public SmsProcess(Context c){
        mContext = c;
        smsRegister();

    }

    public void finalize() throws Throwable {
        mContext.unregisterReceiver(mReceiver);
        Log.d(TAG, "ctor X");
        super.finalize();
    }

    public void sendSMS(){
        String smsNum = PhoneNum;
        String smsText = SendData;

        if (smsNum.length()>0 && smsText.length()>0){
            sendSMS(smsNum, smsText);
        } else {
            Log.d(TAG, "error");
        }
    }


    private void smsRegister() {
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()){
                    case Activity.RESULT_OK:
                        // 전송 성공
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        // 서비스 지역 아님
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        // 무선 꺼짐
                        Toast.makeText(context, "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        // PDU 실패
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        mContext.registerReceiver(mReceiver, new IntentFilter("SMS_SENT_ACTION"));

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK:
                        // 도착 완료
                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // 도착 안됨
                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter("SMS_DELIVERED_ACTION"));

        mContext.registerReceiver(new SMSBroadCast(), new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }

    public void sendSMS(String smsNumber, String smsText){
        Log.d(TAG, "try to send SMS");
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("SMS_DELIVERED_ACTION"), 0);
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }
}