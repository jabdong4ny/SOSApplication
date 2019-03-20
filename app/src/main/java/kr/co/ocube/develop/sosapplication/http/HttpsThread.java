package kr.co.ocube.develop.sosapplication.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import info.guardianproject.netcipher.NetCipher;
import kr.co.ocube.develop.sosapplication.EmlData;

public class HttpsThread extends Thread {
    private static final String TAG = "HttpsThread";
    String m_url;
    String m_kindOfMsg;
    HttpRespNotification m_noti = new HttpRespNotification();
    EmlData m_EmlData;

    public HttpsThread(final String url, final String kindOfMsg, EmlData emlData, Observer observer){
        m_url = url;
        m_kindOfMsg = kindOfMsg;
        m_noti.addObserver(observer);
        m_EmlData = emlData;
    }

    @Override
    public void run() {
        try {
            Boolean bIsSendSuccess = false;
            URL make_url = new URL(m_url);
            trustAllHosts();
            HttpsURLConnection connection = NetCipher.getHttpsURLConnection(make_url); //Android Ver 5.x 이하
    /*                          // Android 5.x 이상
                            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();

                            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {
                                @Override
                                public boolean verify(String s, SSLSession sslSession) {
                                    return true;
                                }
                            });
                            HttpURLConnection connection = httpsURLConnection;
    */
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/x-eML");

            //test code
    //                            List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(2);
    //                            nameValuePairs.add(new BasicNameValuePair("userId", "saltfactory"));
    //                            nameValuePairs.add(new BasicNameValuePair("password", "password"));

            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(m_kindOfMsg);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            connection.connect();

            StringBuilder responseStringBuilder = new StringBuilder();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                bIsSendSuccess = true;
                InputStream in = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                m_EmlData.decodeEMLData(in);
                m_noti.respChanged(bIsSendSuccess);

                for (;;){
                    String stringLine = bufferedReader.readLine();
                    if (stringLine == null ) break;
                    responseStringBuilder.append(stringLine + '\n');
                }
                bufferedReader.close();
            }
            connection.disconnect();
            String feedBack = responseStringBuilder.toString();
            Log.d(TAG, "feedBack Data = " + feedBack);

            //return feedBack;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] chain,
                    String authType)
                    throws java.security.cert.CertificateException {
                // TODO Auto-generated method stub
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class HttpRespNotification extends Observable {
        public void respChanged(Boolean bIsSendSuccess){
            setChanged();
//            notifyObservers();
            notifyObservers(bIsSendSuccess);
        }
    }

}
