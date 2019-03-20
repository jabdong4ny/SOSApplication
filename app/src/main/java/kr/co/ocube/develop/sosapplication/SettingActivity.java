package kr.co.ocube.develop.sosapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "Setting";
    Button ok_button;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    EditText input_phone_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        input_phone_num = (EditText) findViewById(R.id.edit_num);
        setting = getSharedPreferences("P_PHONE_NUM", 0);

        input_phone_num.setText(setting.getString("P_PHONE_NUM", "000 0000 0000"));
        Button apply = findViewById(R.id.apply);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sPhoneNum = input_phone_num.getText().toString();
                Log.d(TAG,"set Phone Number : ["+sPhoneNum+"]");
                editor= setting.edit();
                editor.putString("P_PHONE_NUM", sPhoneNum);
                editor.commit();

                finish(); // 종료
            }
        });
    }

}
