package com.sdwnmt.smartcollector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionResource extends AppCompatActivity implements EasyPermissions.PermissionCallbacks  {

    /*Runtime permission */
    @AfterPermissionGranted(123)
    public  void requestPerm(Context context) {
        String[] perms = {android.Manifest.permission.CAMERA,
                android.Manifest.permission.INTERNET};

        if (EasyPermissions.hasPermissions(context, perms)) {
//            Toast.makeText(context, "Permission Granted, you can experience the features", Toast.LENGTH_LONG).show();

        } else {
            EasyPermissions.requestPermissions((Activity) context, "We need permissions for providing you the better Experience",
                    123, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }
}
