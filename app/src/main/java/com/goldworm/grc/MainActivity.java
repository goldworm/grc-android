package com.goldworm.grc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.goldworm.net.Constants;
import com.goldworm.net.RemoteControl;
import com.goldworm.net.VirtualKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RemoteControl remoteControl = RemoteControl.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remoteControl.start();

        findViewById(R.id.explorer_button).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        remoteControl.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.explorer_button:
                int[] keyCodes = new int[2];
                keyCodes[0] = VirtualKey.VK_CTRL;
                keyCodes[1] = 65; // A
                remoteControl.controlKeyboard(2, keyCodes);
                break;
        }
    }
}
