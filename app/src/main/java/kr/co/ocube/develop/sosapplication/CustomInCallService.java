package kr.co.ocube.develop.sosapplication;

import android.os.Build;
import android.os.Messenger;
import android.support.annotation.RequiresApi;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

import kr.co.ocube.develop.sosapplication.process.VoiceProcess;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CustomInCallService extends InCallService {
    public static final String TAG = "CallStatusPlugin";
    public static final int MSG_SEND_TO_VOICE = 1;
    public static final int MSG_REGISTER_CLIENT = 2;
    private Messenger mClient = null;   // Activity 에서 가져온 Messenger
    @Override
    public void onCreate() {
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        String CHANNEL_ID = "my_channel_01";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build();
        startForeground(1,notification);
        }*/
    }

    @Override
    public void onCallAdded(Call call) {
        Log.d(TAG, "onCallAdded: " + call.getState());
        VoiceProcess.start(call);
    }
        //TODO : onCallRemoved
    @Override
    public void onCallRemoved(Call call) {
            Log.d(TAG, "onCallRemoved ");
    }
// if this app is not call app, you should check permission
/*    protected int versionCheck(final Context c) {
// 사용자의 OS 버전이 마시멜로우 이상인지 체크한다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            *//** * 사용자 단말기의 권한 중 "전화걸기" 권한이 허용되어 있는지 확인한다. * Android는 C언어 기반으로 만들어졌기 때문에 Boolean 타입보다 Int 타입을 사용한다.*//*
            int permissionResult = checkSelfPermission(Manifest.permission.CALL_PHONE);
            *//** * 패키지는 안드로이드 어플리케이션의 아이디이다. * 현재 어플리케이션이 CALL_PHONE에 대해 거부되어있는지 확인한다. *//*
            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                *//** * 사용자가 CALL_PHONE 권한을 거부한 적이 있는지 확인한다. * 거부한적이 있으면 True를 리턴하고 * 거부한적이 없으면 False를 리턴한다. *//*
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(c);
                    dialog.setTitle("권한이 필요합니다.")
                            .setMessage("이 기능을 사용하기 위해서는 단말기의 \"전화걸기\" 권한이 필요합니다. 계속 하시겠습니까?")
                            .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    *//** * 새로운 인스턴스(onClickListener)를 생성했기 때문에 * 버전체크를 다시 해준다. *//*
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        // CALL_PHONE 권한을 Android OS에 요청한다.
                                        ActivityCompat.requestPermissions((Activity) c,new String[]{Manifest.permission.CALL_PHONE}, 1000);
                                    }
                                }
                            })
                            .setNegativeButton("아니요", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Toast.makeText(c, "기능을 취소했습니다", Toast.LENGTH_SHORT).show();
                                }
                            }) .create().show();
                } // 최초로 권한을 요청할 때
                else {
                    // CALL_PHONE 권한을 Android OS에 요청한다.
                    ActivityCompat.requestPermissions((Activity) c, new String[]{Manifest.permission.CALL_PHONE}, 1000);
                }
            } // CALL_PHONE의 권한이 있을 때
            else { // 즉시 실행
                Log.d(TAG, "you have permission about Call");
                return 1;
            }
        } // 마시멜로우 미만의 버전일 때
        else { // 즉시 실행
            Log.d(TAG, "permission check to need 5.0 over");
            return 1;
        }
        return 0;
    }*/

}
