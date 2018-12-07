package com.kryptono.testtask;

import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.mtramin.rxfingerprint.RxFingerprint;

import org.w3c.dom.Text;

import io.reactivex.disposables.Disposable;

import static com.mtramin.rxfingerprint.data.FingerprintResult.AUTHENTICATED;
import static com.mtramin.rxfingerprint.data.FingerprintResult.FAILED;
import static com.mtramin.rxfingerprint.data.FingerprintResult.HELP;

public class LockActivity extends AppCompatActivity {
    final static int PASSCODE_AUTHENTICATE = 1001;

    TextView tv_unlock_fingerprint;
    TextView tv_unlock_passcode;

    Disposable fingerprintDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.finger_guard);

        //Secure don't return thumbnail recent
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        tv_unlock_fingerprint = findViewById(R.id.tv_unlock_fingerprint);
        tv_unlock_passcode = findViewById(R.id.tv_unlock_passcode);

        tv_unlock_passcode.setOnClickListener(onClickListener);

        doFingerAuthentication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PASSCODE_AUTHENTICATE) {
            if (resultCode == RESULT_OK) {
                //do something you want when pass the security
                MainApplication.isForeground = true;
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.tv_unlock_passcode:
                    showPassCodeLock();
                    break;
            }

        }
    };


    private void doFingerAuthentication()
    {
        if (!RxFingerprint.hasEnrolledFingerprints(this))
            return;

        fingerprintDisposable = RxFingerprint.authenticate(this)
                .subscribe(fingerprintAuthenticationResult -> {
                    switch (fingerprintAuthenticationResult.getResult()) {
                        case FAILED:
                            tv_unlock_fingerprint.setTextColor(Color.RED);
                            tv_unlock_fingerprint.setText("Fingerprint not recognized, try again!");
                            break;
                        case HELP:
                            tv_unlock_fingerprint.setTextColor(Color.RED);
                            tv_unlock_fingerprint.setText(fingerprintAuthenticationResult.getMessage());
                            break;
                        case AUTHENTICATED:
                            tv_unlock_fingerprint.setTextColor(Color.GREEN);
                            tv_unlock_fingerprint.setText("Successfully authenticated!");

                            if (fingerprintDisposable != null)
                                fingerprintDisposable.dispose();

                            MainApplication.isForeground = true;
                            finish();
                            break;
                    }
                }, throwable -> {
                    Log.e("ERROR", "authenticate", throwable);
                    tv_unlock_fingerprint.setTextColor(Color.RED);
                    tv_unlock_fingerprint.setText(throwable.getMessage());

                    MainApplication.isForeground = false;

                    if (fingerprintDisposable != null)
                        fingerprintDisposable.dispose();


                });
    }



    private void showPassCodeLock()
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

            if (km.isKeyguardSecure()) {
                Intent authIntent = km.createConfirmDeviceCredentialIntent(null, null);
                startActivityForResult(authIntent, PASSCODE_AUTHENTICATE);
            }
        }

    }


}
