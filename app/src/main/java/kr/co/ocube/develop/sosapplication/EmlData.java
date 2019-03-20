package kr.co.ocube.develop.sosapplication;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

//refer site : https://recipes4dev.tistory.com/134

public class EmlData {
    private static final String TAG = "EmlData";
    private String m_respType;
    private String m_respOperation;
    private String m_respService;


    public void decodeEMLData(InputStream is){
        try {
            final int STEP_NONE = 0 ;
            final int STEP_NO = 1 ;
            final int STEP_NAME = 2 ;
            final int STEP_TYPE = 3 ;
            final int STEP_SERVICE = 4 ;
            final int STEP_OPERATION = 5 ;

            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser() ;

            int step = STEP_NONE ;
            int no = -1 ;
            String name = null ;

            parser.setInput(is, null) ;

            int eventType = parser.getEventType() ;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    // XML 데이터 시작
                } else if (eventType == XmlPullParser.START_TAG) {
                    String startTag = parser.getName() ;
                    if (startTag.equals("Type")) {
                        step = STEP_TYPE;
                    } else if (startTag.equals("Service")) {
                        step = STEP_SERVICE;
                    } else if (startTag.equals("Operation")){
                        step = STEP_OPERATION;
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    /*String endTag = parser.getName() ;
                    if ((endTag.equals("NO") && step != STEP_NO) ||
                            endTag.equals("NAME") && step != STEP_NAME)
                    {
                        // TODO : error.
                    }*/
                } else if (eventType == XmlPullParser.TEXT) {
                    name = parser.getText() ;
                    if (step == STEP_TYPE) {
                        setType(name);
                    } else if (step == STEP_SERVICE) {
                        setService(name);
                    } else if (step == STEP_OPERATION){
                        setOperation(name);
                    }
                }

                eventType = parser.next();
            }

            if (no == -1 || name == null) {
                // ERROR : XML is invalid.
            }
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }

    private void setOperation(String text) {
        Log.d(TAG, "setOperation = " + text);
        m_respOperation = text;
    }

    private void setService(String text) {
        Log.d(TAG, "setService = " + text);
        m_respService = text;
    }

    private void setType(String text) {
        Log.d(TAG, "setType = " + text);
        m_respType = text;
    }

    public String getOperation(){
        return m_respOperation;
    }
    public String getService(){
        return m_respService;
    }
    public String getType(){
        return m_respType;
    }
}
