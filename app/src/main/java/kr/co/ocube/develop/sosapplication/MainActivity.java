package kr.co.ocube.develop.sosapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.ocube.develop.sosapplication.process.DataProcess;
import kr.co.ocube.develop.sosapplication.process.SmsProcess;
import kr.co.ocube.develop.sosapplication.process.VoiceProcess;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SOS";
    private static final String SOS_url = "https://testtbox.com/tsp/35/SOS";
//    private static final String Sample_url = "https://192.168.111.79/login"; //"https://localhost/login";
    public static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int REQUEST_CODE_SET_DEFAULT_DIALER = 123;

    ImageButton mSosBtn;
    SmsProcess m_sms;
    DataProcess m_data;
    VoiceProcess m_voice;

    TextView mSosState;
    TextView mCallStatus;
    TextView mDataKindOf;
    TextView mHeadUnit;
    ImageView mHeadUnitImg;

    Handler mUiHandler;
    Handler mStateHandler;
    Sos_SM hsm;

    String mSosStateValue;

    boolean m_bIsDataSendSucess = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runProcess();
        checkDefaultDialer();

        mSosState = findViewById(R.id.SOSstatusTextView);
        mCallStatus = findViewById(R.id.CallstatusTextView);
        mDataKindOf = findViewById(R.id.DataKindOfstatusTextView);
        mHeadUnit = findViewById(R.id.HeadUintTextView);
        mHeadUnitImg = findViewById(R.id.HeadUnitImageView);

        mUiHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch(msg.what){
                    case Const.MSG_SOS_STATE:{
                        mSosStateValue =(String)msg.obj;
                        if((mSosStateValue).equals("ACTIVE MODE") != true) {
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(Const.MSG_HEAD_UNIT_DISPLAY, "Normal"));
                        } else {
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(Const.MSG_HEAD_UNIT_DISPLAY,"Alert SOS"));
                        }
                        mSosState.setText((String)msg.obj);
                    }break;

                    case Const.MSG_CALL_STATUS:{
                        mCallStatus.setText((String)msg.obj);
                    } break;

                    case Const.MSG_DATA_KIND_OF:{
                        mDataKindOf.setText((String)msg.obj);
                    } break;

                    case Const.MSG_HEAD_UNIT_DISPLAY:{
                        mHeadUnit.setText((String)msg.obj);
                        if(((String)msg.obj).equals("Normal")) {
                            mHeadUnitImg.setBackgroundColor(0xFF00FF00);
                        } else {
                            mHeadUnitImg.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        }
                    } break;

                    case Const.MSG_TOAST_UPDATE:{
                        toast((String)msg.obj);
                    }break;
                    default:
                        Log.e(TAG, "exception ");
                        break;
                }
            }
        };

        mStateHandler = new Handler(){
            public void handleMessage(Message msg)
            {
                switch(msg.what){
                    case Const.MSG_CHANGE_SOS_STATE_UPDATE:{
                        msg.what = Const.MSG_SOS_STATE;
                        mUiHandler.handleMessage(msg);
                    } break;

                    case Const.MSG_DATA_SEND:{
                        m_data.sendDataMsg(msg.arg1);
                    } break;

                    case Const.MSG_DATA_SEND_RESULT:{
                        setDataSendSucess(msg.arg1);
                    } break;

                    case Const.MSG_CHANGE_STATE_TO_CALLBACK:{
                        hsm.sendMessage(hsm.obtainMessage(hsm.CMD_TRANSITION_TO_CALLBACK));
                    } break;

                    case Const.MSG_CHANGE_STATE_TO_ACTIVE:{
                        hsm.sendMessage(hsm.obtainMessage(hsm.CMD_TRANSITION_TO_ACTIVE));
                    } break;

                    case Const.MSG_TRY_TO_CALLING:{
                        m_voice.tryToCalling();
                    } break;

                    default:
                        Log.e(TAG, "exception ");
                        break;
                }
            }
        };
        //create state machine
        hsm = Sos_SM.getInstance(mStateHandler);

        mSosBtn = (ImageButton)findViewById(R.id.sos_button);
        mSosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSosBtn.setEnabled(false);
                //mOutputText.setText("");
                hsm.sendMessage(hsm.obtainMessage(hsm.EVT_BUTTON_EVENT));
                mSosBtn.setEnabled(true);
            }
        });
        mHeadUnitImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
            try {
                TelecomManager tm = (TelecomManager)getSystemService(TELECOM_SERVICE);
                boolean isAlreadyDefaultDialer = (getPackageName() == tm.getDefaultDialerPackage());
                if (isAlreadyDefaultDialer) return;

                Intent intent = new Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, getPackageName());
                startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_SET_DEFAULT_DIALER:
                    checkSetDefaultDialerResult(resultCode);
                    break;
            }
        }
    }

    private void checkSetDefaultDialerResult(int resultCode) {
        toast("resultCode " + resultCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void runProcess() {
        m_sms = new SmsProcess(this);
        m_data = new DataProcess(this);
        m_voice = new VoiceProcess(this);
    }


    public void onUpdateText(final int kindof, final String value){
        new Thread()
        {
            public void run()
            {
                Message msg = mUiHandler.obtainMessage();
                msg.what = kindof;
                msg.obj = (Object)value;
                mUiHandler.sendMessage(msg);
            }
        }.start();
    }

    public Handler getStateHandler(){
        return mStateHandler;
    }

    public Handler getUiHandler(){
        return mUiHandler;
    }

    public String getSosState(){
        return mSosStateValue;
    }

    public void toast(String msg){
            Toast.makeText(this, msg , Toast.LENGTH_LONG).show();
    }

    public void setDataSendSucess(int bIsDataSendSucess){
        m_bIsDataSendSucess = (bIsDataSendSucess == 1)? true:false;
    }

    public boolean getDataSendSuccess(){
        return m_bIsDataSendSucess;
    }

}