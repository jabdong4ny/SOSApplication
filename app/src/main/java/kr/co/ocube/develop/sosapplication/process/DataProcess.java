package kr.co.ocube.develop.sosapplication.process;

import android.content.Context;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import kr.co.ocube.develop.sosapplication.Const;
import kr.co.ocube.develop.sosapplication.EmlData;
import kr.co.ocube.develop.sosapplication.MainActivity;
import kr.co.ocube.develop.sosapplication.http.HttpsThread;


public class DataProcess {
    private static final String TAG = "DataProcess";
    private static final String SOS_url = "https://testtbox.com/tsp/35/SOS";

    Context mContext;
    EmlData m_EmlData;

    public DataProcess(Context c){
        mContext = c;
        m_EmlData = new EmlData();
            Log.d(TAG, "ctor E");
            Log.d(TAG, "ctor X");
    }

    private void sendToDataForHttps(final String url, final String kindOfMsg, Observer noti) {
        //https
        HttpsThread thread = new HttpsThread(url, kindOfMsg, m_EmlData, noti);
        thread.start();
    }

    private Observer respHttp = new Observer() {
        @Override
        public void update(Observable o, Object bIsSendSucces) {
            Log.d(TAG, "HttpThread response result - Success:" + (Boolean) bIsSendSucces);
            if((boolean)bIsSendSucces == true) {
                ((MainActivity)mContext).onUpdateText(Const.MSG_DATA_KIND_OF, m_EmlData.getOperation() +" : " + m_EmlData.getType());
                if(m_EmlData.getOperation().equals("Confirm-termination") == true) {
                    if(m_EmlData.getType().equals("ACK")){
                        sendToMsgStateHandler(Const.MSG_CHANGE_STATE_TO_CALLBACK,false);
                    }
                }

                if(m_EmlData.getOperation().equals("Notification") == true) {
                    if(m_EmlData.getType().equals("ACK")){
                        boolean bIsDataSendSucess = true;
                        sendToMsgStateHandler(Const.MSG_DATA_SEND_RESULT, bIsDataSendSucess);
                    }
                }
            }
        }
    };

    public void sendDataMsg(int kindof){
        switch (kindof){
            case Const.MSG_SOS_NOTIFICATION:{
                boolean bIsDataSendSucessinit  = false; //init
                sendToMsgStateHandler(Const.MSG_DATA_SEND_RESULT, bIsDataSendSucessinit);
                ((MainActivity)mContext).onUpdateText(Const.MSG_DATA_KIND_OF, "Notification");
                sendToDataForHttps(SOS_url, Const.ConstEmlLowData.SOS_Noti_EML_RAW_DATA, respHttp);
            }break;

            case Const.MSG_SOS_VOICE_CALL_TERMINATION:{
                ((MainActivity)mContext).onUpdateText(Const.MSG_DATA_KIND_OF, "Confirm-termination");
                sendToDataForHttps(SOS_url, Const.ConstEmlLowData.SOS_VCT_EML_RAW_DATA, respHttp);
            }
            default:
                break;
        }
    }

    public void sendToMsgStateHandler(int msgId, boolean arg1){
        ((MainActivity)mContext).getStateHandler().sendMessage(
                ((MainActivity)mContext).getStateHandler().obtainMessage(msgId,(Object)arg1));
    }

}
