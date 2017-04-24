package com.feicuiedu.permissionsdemo_0424;

import android.Manifest;
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
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testCall();
            }
        });
    }

    // 1. 检测清单的权限有没有授权成功
    private void testCall(){
        /**
         * 检测权限：
         * 返回值：
         * PERMISSION_GRANTED 授权已经成功
         * PERMISSION_DENIED 授权被拒绝
         */
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){

            // 授权没有成功，向用户申请权限
            // 2. 向用户申请权限:可以申请多个，都可以在String[] 里面加上
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.READ_EXTERNAL_STORAGE},CALL_PHONE);

        }else {
            // 授权成功：用户允许某项权限之后
            callPhone();
        }
    }

    // 拨打电话
    private void callPhone() {

        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
        startActivity(intent);
    }

    // 3. 处理权限的申请回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 如果是某一项权限的申请
        if (requestCode==CALL_PHONE){

            // 用户是不是授权成功了
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                // 用户授权成功
                callPhone();

            }else {
                // 用户没有授权成功
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();

                // 额外的处理：能不能在用户完全被拒绝的时候，友好的提示一下
                // 4. 彻底被拒绝，不会再展示权限申请的对话框信息，我们自己写个提示一下
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)){
                    showDialog();
                }
            }
//            if (grantResults[1]==PackageManager.PERMISSION_GRANTED){
//                // 读取SD卡信息等：根据不同的权限申请处理不同的事件
//            }else {
//                // 拒绝的处理
//            }
        }
    }

    // 显示的提示信息：提醒用户去更改权限设置
    private void showDialog() {
        new AlertDialog.Builder(this)
                .setMessage("权限被彻底拒绝，可以到设置页面打开，才可以使用此功能")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到本应用的设置页面，可以打开权限信息
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消",null)
                .create()
                .show();
    }
}
