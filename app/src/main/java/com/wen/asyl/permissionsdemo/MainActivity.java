package com.wen.asyl.permissionsdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void callOnClick(View view){
        //检查app是否有拨打电话的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            //如果没有就进行申请
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
        }else {
            callPhone();
        }

    }

    private void callPhone() {
        Intent intent=new Intent(Intent.ACTION_CALL);
        Uri data= Uri.parse("tel:"+"10086");
        intent.setData(data);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==PERMISSION_REQUEST_CODE){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                callPhone();
            }else{
               // Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
                    AlertDialog dialog=new AlertDialog.Builder(this).setMessage("需要访问电话的权限，不开启将无法使用！").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            //设置去向意图
                            Uri uri = Uri.fromParts("package", MainActivity.this.getPackageName(), null);
                            intent.setData(uri);
                            //发起跳转
                            startActivity(intent);
                        }
                    }).create();
                    dialog.show();
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
