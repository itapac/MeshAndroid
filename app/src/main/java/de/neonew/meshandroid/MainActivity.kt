package de.neonew.meshandroid

import android.app.Activity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.apache.commons.io.IOUtils
import java.io.IOException

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runAsRoot("ifconfig wlan0 down")
        runAsRoot("echo \"/system/etc/wifi/bcmdhd_ibss.bin\" >> /sys/module/dhd/parameters/firmware_path")
        runAsRoot("echo \"/system/etc/wifi/nvram_net.txt\" >> /sys/module/dhd/parameters/nvram_path")
        runAsRoot("ifconfig wlan0 10.0.0.1 netmask 255.255.0.0 up")
        runAsRoot("iw wlan0 set type ibss")
        runAsRoot("iw wlan0 ibss join MeshAndroid 2412")

        Log.i("MainActivity", "OK")

        sample_text.text = stringFromJNI()
    }

    private fun runAsRoot(command: String) {
        try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            val returnValue = process.waitFor()
            val output = IOUtils.toString(process.inputStream)
            if (returnValue != 0) {
                throw IllegalStateException("command failed: $command; ret: $returnValue; output: $output")
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}