package com.example.mywords.ui.notifications

import android.content.Context
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mywords.databinding.FragmentNotificationsBinding
import java.io.BufferedReader
import java.io.FileReader
import java.lang.reflect.Method
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import kotlin.collections.ArrayList


class NotificationsFragment : Fragment() {
    private val TAG = "NotificationsFragment"

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.textBtn.setOnClickListener {
            getWifiApState(requireContext())
            printHotIp()
            getWifiIp(requireContext())?.let {
                Log.i(TAG, "wifi ip: $it")
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    val WIFI_AP_STATE_DISABLING = 10
    val WIFI_AP_STATE_DISABLED = 11
    val WIFI_AP_STATE_ENABLING = 12
    val WIFI_AP_STATE_ENABLED = 13
    val WIFI_AP_STATE_FAILED = 14
    fun getWifiApState(mContext: Context): Int {
        val wifiManager = mContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return try {
            val method: Method = wifiManager.javaClass.getMethod("getWifiApState")
            val i = method.invoke(wifiManager) as Int
            //ap 状态
            Log.i(TAG, "wifi ap state: $i")
        } catch (e: Exception) {
            Log.e(TAG, "Cannot get WiFi AP state$e")
        }
    }

    private fun getConnectedHotIP(): ArrayList<String>? {
        val connectedIP = ArrayList<String>()
        try {
            val br = BufferedReader(
                FileReader("/proc/net/arp")
            )
            var line: String
            while (br.readLine().also { line = it } != null) {
                val splitted = line.split(" +").toTypedArray()
                if (splitted.size == 4) {
                    val ip = splitted[0]
                    connectedIP.add(ip)
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return connectedIP
    }

    //输出链接到当前设备的IP地址
    fun printHotIp() {
        val connectedIP = getConnectedHotIP()
        val resultList = StringBuilder()
        for (ip in connectedIP!!) {
            resultList.append(ip)
            resultList.append("\n")
        }
        Log.i(TAG, "printHotIp resultList: ${resultList}")
    }


    fun getWifiIp(context: Context): String? {
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager //获取WifiManager
        if (!manager.isWifiEnabled) {
            manager.isWifiEnabled = true
        }
        val wifiinfo = manager.connectionInfo
        return intToIp(wifiinfo.ipAddress)
    }

    fun getWifiInfo(context: Context): WifiInfo? {
        val manager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager //获取WifiManager
        if (!manager.isWifiEnabled) {
            manager.isWifiEnabled = true
        }
        return manager.connectionInfo
    }

    private fun intToIp(i: Int): String? {
        //Logcat 会检测ip格式,显示的时候替换一下
        //Loggers.d(TAG, "intToIp: ip="+ip.replace(".", "-") );
        return (i and 0xFF).toString() + "." + (i shr 8 and 0xFF) + "." + (i shr 16 and 0xFF) + "." + (i shr 24 and 0xFF)
    }

}
