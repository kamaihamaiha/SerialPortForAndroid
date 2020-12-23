package cn.kk.serialport

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kongqw.serialportlibrary.SerialPortFinder
import com.kongqw.serialportlibrary.SerialPortManager
import com.kongqw.serialportlibrary.listener.OnOpenSerialPortListener
import com.kongqw.serialportlibrary.listener.OnSerialPortDataListener
import java.io.File
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val PATH = "/dev/ttyS4"
    private val BAUD_RATE = 115200
    private var serialPortManager: SerialPortManager? = null
    private lateinit var tvSerialPortInfo: TextView
    private lateinit var tvSerialPortStatus: TextView
    private lateinit var tvMsg: TextView
    private lateinit var tvLineNumber: TextView
    private var lineNumber = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        tvSerialPortInfo = findViewById(R.id.tvSerialPortInfo)
        tvSerialPortStatus = findViewById(R.id.tvSerialPortStatus)
        tvMsg = findViewById(R.id.tvMsg)
        tvLineNumber = findViewById(R.id.tvLineNumber)

        tvMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvLineNumber.setMovementMethod(ScrollingMovementMethod.getInstance());


        tvSerialPortInfo.setText(PATH)
        findViewById<View>(R.id.btnClear).setOnClickListener {
            run {
                tvMsg.text = ""
                tvLineNumber.text = ""
                lineNumber = 0
            }
        }

        val serialPortFinder = SerialPortFinder()
        val devices = serialPortFinder.devices

        serialPortManager = SerialPortManager()
        serialPortManager!!.setOnOpenSerialPortListener(object : OnOpenSerialPortListener {
            override fun onSuccess(device: File) {

            }

            override fun onFail(device: File, status: OnOpenSerialPortListener.Status) {

            }
        })
        serialPortManager!!.setOnSerialPortDataListener(object : OnSerialPortDataListener {
            override fun onDataReceived(bytes: ByteArray) {

                runOnUiThread {
                    refresh(String(bytes).toUpperCase(Locale.ROOT), tvMsg)

                    refresh((++lineNumber).toString(), tvLineNumber)

                }
            }

            override fun onDataSent(bytes: ByteArray) {

            }
        })
        val openSerialPort = serialPortManager!!.openSerialPort(File(PATH), BAUD_RATE)
        tvSerialPortStatus.setText(if (openSerialPort) "成功" else "失败")

    }

    private fun refresh(msg: String, textView: TextView) {
        textView.append(msg + "\n")

        val offset: Int = textView.lineCount * textView.lineHeight
        if (offset > textView.height) {
            textView.scrollTo(0, offset - textView.height)
        }
    }

    override fun onDestroy() {
        serialPortManager!!.closeSerialPort()
        super.onDestroy()
    }

}