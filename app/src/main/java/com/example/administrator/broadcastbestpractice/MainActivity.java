package com.example.administrator.broadcastbestpractice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends BaseActivity {

    private ForceOfflineBroadcast receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);

        Button forceOffline=(Button)findViewById(R.id.force_offline);
        forceOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("com.example.broadcastbestpractice.FORCE_OFFLINE");
        receiver=new ForceOfflineBroadcast();
        registerReceiver(receiver,intentFilter);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private class ForceOfflineBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("Warning")
                    .setMessage("You are forced to be offline. Please try to login again.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCollector.finishAll();//销毁所有活动
                            Intent intent=new Intent(context,LoginActivity.class);
                            context.startActivity(intent);
                        }
                    }).show();

        }
    }
}
