package com.beetle.chili.triggers.connection.uasndje

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.net.VpnService
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceDataStore
import com.beetle.chili.triggers.connection.R
import com.beetle.chili.triggers.connection.aleis.App
import com.beetle.chili.triggers.connection.brslke.SsSpeed
import com.beetle.chili.triggers.connection.databinding.ActivityMainBinding
import com.beetle.chili.triggers.connection.imsued.SpeedImp
import com.beetle.chili.triggers.connection.uskde.DataUtils
import com.beetle.chili.triggers.connection.uskde.DataUtils.shareText
import com.beetle.chili.triggers.connection.uskde.NetGet
import com.beetle.chili.triggers.connection.uskde.ZongData
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager
import com.github.shadowsocks.preference.DataStore
import com.github.shadowsocks.preference.OnPreferenceDataStoreChangeListener
import com.github.shadowsocks.utils.Key
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.text.SimpleDateFormat
import java.util.TimeZone

class MainActivity : AppCompatActivity(),
    ShadowsocksConnection.Callback,
    OnPreferenceDataStoreChangeListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var requestPermissionForResultVPN: ActivityResultLauncher<Intent?>
    val connection = ShadowsocksConnection(true)
    var nowClickState: Int = 1
    private var timeJob: Job? = null
    private var connectJob: Job? = null

    private val imp by lazy {
        object : SpeedImp {
            override fun speedLong(download: String, upload: String) {
                binding.tvDownValue.text = download
                binding.tvUpValue.text = upload
            }
        }
    }

    private val receiver by lazy { SsSpeed(imp) }
    val hisPage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
            }
        }
    val listPage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                setVpnUi()
                showVpnResult()
            }
        }

    val endPage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getStringExtra("end")?.let { endValue ->
                    Log.d("TAG", "Received end value: $endValue")
                    when (endValue) {
                        "fast" -> {
                            DataUtils.nowVpn = ""
                            showVpnResult()
                        }

                        "flushed" -> {
                            showVpnResult()
                        }
                    }
                }
                setVpnUi()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initData()
        clickMainBtn()
    }

    private fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(
                receiver,
                IntentFilter().apply { addAction("com.beetle.chili.speed") },
                Context.RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(receiver, IntentFilter().apply { addAction("com.beetle.chili.speed") })
        }
        connection.connect(this, this)
        DataStore.publicStore.registerChangeListener(this)
        requestPermissionForResultVPN =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                requestPermissionForResult(it)
            }
        connection.connect(this, this)
        setVpnUi()
        setTextSpan()
    }

    private fun setVpnUi() {
        val bean = DataUtils.getNowVpn()
        val icon = DataUtils.getCountryIcon(bean.getFistName())
        binding.mainFlag.setImageResource(icon)
        binding.tvName.text = bean.getName()
    }

    private fun clickMainBtn() {
        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawer.isOpen) {
                binding.drawer.close()
                return@addCallback
            }
            clickBlock {
                finish()
            }
        }
        binding.llHis.setOnClickListener {
            clickBlock {
                hisPage.launch(Intent(this, HisActivity::class.java))
            }
        }
        binding.appCompatTextView.setOnClickListener {
            clickBlock {
                binding.drawer.open()
            }
        }
        binding.llService.setOnClickListener {
            clickBlock {
                lifecycleScope.launch {
                    if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                        DataUtils.haveVpnData({ binding.showLoading = true }, {
                            binding.showLoading = false
                            jumpToList()
                        }, { jumpToList() })
                    }
                }

            }
        }
        binding.tvCurrent.setOnClickListener {
            clickBlock {
                jumpToEnd()
            }
        }
        binding.mainImgState.setOnClickListener {
            clickBlock {
                showVpnResult()
            }
        }

        binding.conVpnBut.setOnClickListener {
            clickBlock {
                showVpnResult()
            }
        }
        binding.tvPp.setOnClickListener {
            startActivity(
                Intent(
                    "android.intent.action.VIEW", Uri.parse(ZongData.ppUrl)
                )
            )
        }
        binding.tvShare.setOnClickListener {
            shareText(
                "https://play.google.com/store/apps/details?id=${this.packageName}",
                "Share App"
            )
        }
    }

    private fun jumpToList() {
        listPage.launch(Intent(this@MainActivity, ListActivity::class.java))
    }

    private fun jumpToEnd(){
        if (lifecycle.currentState == Lifecycle.State.RESUMED) {
            endPage.launch(Intent(this, EndActivity::class.java))
        }
    }

    private fun checkVPNPermission(): Boolean {
        VpnService.prepare(this).let {
            return it == null
        }
    }

    private fun requestPermissionForResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            startVpn()
        } else {
            Toast.makeText(
                this,
                "Please give permission to continue to the next step",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showVpnResult() {
        if (NetGet.inspectConnect(this)) return
        if (checkVPNPermission()) {
            startVpn()
        } else {
            VpnService.prepare(this).let {
                requestPermissionForResultVPN.launch(it)
            }
        }
    }

    private fun clickBlock(block: () -> Unit) {
        if (binding.showVpnState != 1) {
            block()
        } else {
            Toast.makeText(
                this,
                if (DataUtils.getVpnIsConnect()) "Disconnecting... Please wait" else "Connecting... Please wait",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun startVpn() {
        var noData = false
        connectJob?.cancel()
        connectJob = lifecycleScope.launch {
            DataUtils.haveVpnData(
                { binding.showLoading = true },
                { binding.showLoading = false },
                {
                    noData = true
                })
            if (!noData) {
                return@launch
            }
            nowClickState = if (DataUtils.getVpnIsConnect()) {
                2
            } else {
                0
            }
            binding.showVpnState = 1
            connectJob?.cancel()
            setVpnConfig()
            delay(2000)
            if (nowClickState == 2) {
                Core.stopService()
            }
            if (nowClickState == 0) {
                DataUtils.addHisVpn()
                Core.startService()
            }
        }
    }

    private fun startToEndActicirty() {
        if (nowClickState == 1) {
            return
        }
        nowClickState = 1
        jumpToEnd()
    }

    override fun onStart() {
        super.onStart()
        connection.bandwidthTimeout = 500
    }

    override fun onStop() {
        super.onStop()
        if (binding?.showVpnState!! == 1 && nowClickState == 0) {
            nowClickState = 1
            binding.showVpnState = 0
        }
        if (binding?.showVpnState!! == 1 && nowClickState == 2) {
            nowClickState = 1
            binding.showVpnState = 2
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        DataStore.publicStore.unregisterChangeListener(this)
        connection.disconnect(this)
    }


    private fun setVpnConfig() {
        ProfileManager.getProfile(DataStore.profileId).let {
            if (it != null) {
                ProfileManager.updateProfile(setSkServerData(it))
            } else {
                val profile = Profile()
                ProfileManager.createProfile(setSkServerData(profile))
            }
        }
        DataStore.profileId = 1L
    }

    private fun setSkServerData(profile: Profile): Profile {
        val data = DataUtils.getNowVpn()
        profile.name = data.getName()
        profile.host = data.host
        profile.password = data.password
        profile.method = data.methode
        profile.remotePort = data.port
        return profile
    }


    private fun startTime(t: Long = System.currentTimeMillis()) {
        timeJob = CoroutineScope(Dispatchers.Main).launch {
            DataUtils.selectTime = t
            while (true) {
                delay(1000)
                val t = SimpleDateFormat("HH:mm:ss").apply {
                    timeZone = TimeZone.getTimeZone("+8")
                }.format(System.currentTimeMillis() - DataUtils.selectTime)
                binding.mainTimeValue.text = t
                DataUtils.endTime = t
                DataUtils.editHisVpn(DataUtils.getNowVpn().vpnDate)
            }
        }
    }

    private fun stopTime() {
        timeJob?.cancel()
        binding.mainTimeValue.text = "00:00:00"
    }

    private fun connectFun() {
        lifecycleScope.launch(Dispatchers.Main) {
            startToEndActicirty()
            startTime()
            binding.showVpnState = 2
        }
    }

    private fun disConnectFun() {
        lifecycleScope.launch(Dispatchers.Main) {
            startToEndActicirty()
            stopTime()
            binding.showVpnState = 0
        }
    }

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        Log.e("TAG", "stateChanged: ${state.name}")
        when (state) {
            BaseService.State.Connected -> {
                App.vvState = true
                connectFun()
            }

            BaseService.State.Connecting -> {
            }

            BaseService.State.Stopped -> {
                App.vvState = false
                disConnectFun()
            }

            else -> {
            }
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        Log.e("TAG", "stateChanged-c: ${state.name}")
        if (state.name == "Connected") {
            App.vvState = true
            binding.showVpnState = 2
            val t =
                if (DataUtils.selectTime > 0L) DataUtils.selectTime else System.currentTimeMillis()
            startTime(t)
        }
        if (state.name == "Stopped") {
            App.vvState = false
            binding.showVpnState = 0
        }
    }

    override fun onPreferenceDataStoreChanged(store: PreferenceDataStore, key: String) {
        when (key) {
            Key.serviceMode -> {
                connection.disconnect(this)
                connection.connect(this, this)
            }
        }
    }
    fun setTextSpan(){
        val content = "Current Information"
        val spannable = SpannableString(content).apply {
            setSpan(UnderlineSpan(), 0, content.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvCurrent.text = spannable

    }
}