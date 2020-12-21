package cn.kk.serialport;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kongqw.serialportlibrary.Device;
import com.kongqw.serialportlibrary.SerialPortFinder;
import com.kongqw.serialportlibrary.SerialPortManager;
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener;
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener;

import java.io.File;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    private static final String path = "/dev/ttyS4";

    private SerialPortManager serialPortManager;
    private TextView tvSerialPortInfo;
    private TextView tvSerialPortStatus;
    private TextView tvMsg;
    private StringBuffer stringBuffer = new StringBuffer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvSerialPortInfo = findViewById(R.id.tvSerialPortInfo);
        tvSerialPortStatus = findViewById(R.id.tvSerialPortStatus);
        tvMsg = findViewById(R.id.tvMsg);
        tvSerialPortInfo.setText(path);

        findViewById(R.id.btnClear).setOnClickListener(v -> tvMsg.setText(""));

        SerialPortFinder serialPortFinder = new SerialPortFinder();
        ArrayList<Device> devices = serialPortFinder.getDevices();

        serialPortManager = new SerialPortManager();

        serialPortManager.setOnOpenSerialPortListener(new OnOpenSerialPortListener() {
            @Override
            public void onSuccess(File device) {

            }

            @Override
            public void onFail(File device, Status status) {

            }
        });

        serialPortManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
            @Override
            public void onDataReceived(byte[] bytes) {
                stringBuffer.append(new String(bytes) + "\n");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvMsg.append(new String(bytes) + "\n");
                    }
                });

            }

            @Override
            public void onDataSent(byte[] bytes) {

            }
        });


        boolean openSerialPort = serialPortManager.openSerialPort(new File("/dev/ttyS4"), 115200);


        tvSerialPortStatus.setText(openSerialPort ? "成功" : "失败");
    }

    @Override
    protected void onDestroy() {
        serialPortManager.closeSerialPort();
        super.onDestroy();

    }
}
