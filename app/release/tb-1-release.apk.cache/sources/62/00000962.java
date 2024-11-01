package com.blue.cat.fast.thirdbrowser.view;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultRegistry;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBinderMapperImpl;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.preference.PreferenceDataStore;
import com.blue.cat.fast.thirdbrowser.App;
import com.blue.cat.fast.thirdbrowser.R;
import com.blue.cat.fast.thirdbrowser.StringFog;
import com.blue.cat.fast.thirdbrowser.databinding.ActivityVpnBinding;
import com.blue.cat.fast.thirdbrowser.model.TimerViewModel;
import com.blue.cat.fast.thirdbrowser.utils.BVDataUtils;
import com.blue.cat.fast.thirdbrowser.utils.BrowserKey;
import com.blue.cat.fast.thirdbrowser.utils.BrowserServiceBean;
import com.blue.cat.fast.thirdbrowser.view.ResultActivity;
import com.github.shadowsocks.aidl.IShadowsocksService;
import com.github.shadowsocks.aidl.ShadowsocksConnection;
import com.github.shadowsocks.aidl.ShadowsocksConnection$serviceCallback$1;
import com.github.shadowsocks.aidl.TrafficStats;
import com.github.shadowsocks.bg.BaseService$State;
import com.github.shadowsocks.database.KeyValuePair;
import com.github.shadowsocks.database.PrivateDatabase;
import com.github.shadowsocks.database.Profile;
import com.github.shadowsocks.database.ProfileManager;
import com.github.shadowsocks.preference.DataStore;
import com.github.shadowsocks.preference.OnPreferenceDataStoreChangeListener;
import com.github.shadowsocks.preference.RoomPreferenceDataStore;
import com.github.shadowsocks.utils.StartService;
import com.google.gson.Gson;
import kotlin.LazyKt__LazyKt;
import kotlin.SynchronizedLazyImpl;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.StandaloneCoroutine;

/* compiled from: VpnActivity.kt */
/* loaded from: classes.dex */
public final class VpnActivity extends AppCompatActivity implements ShadowsocksConnection.Callback, OnPreferenceDataStoreChangeListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ActivityResultRegistry.AnonymousClass2 connect;
    public final SynchronizedLazyImpl binding$delegate = LazyKt__LazyKt.lazy(new Function0<ActivityVpnBinding>() { // from class: com.blue.cat.fast.thirdbrowser.view.VpnActivity$binding$2
        {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final ActivityVpnBinding invoke() {
            LayoutInflater layoutInflater = VpnActivity.this.getLayoutInflater();
            int i = ActivityVpnBinding.$r8$clinit;
            DataBinderMapperImpl dataBinderMapperImpl = DataBindingUtil.sMapper;
            return (ActivityVpnBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_vpn);
        }
    });
    public final ShadowsocksConnection connection = new ShadowsocksConnection();
    public int jumpType = -1;

    public VpnActivity() {
        ActivityResultRegistry.AnonymousClass2 registerForActivityResult = registerForActivityResult(new ActivityResultCallback() { // from class: com.blue.cat.fast.thirdbrowser.view.VpnActivity$$ExternalSyntheticLambda0
            /* JADX WARN: Code restructure failed: missing block: B:17:0x0091, code lost:
                if (kotlin.jvm.internal.Intrinsics.areEqual(r2, com.blue.cat.fast.thirdbrowser.StringFog.decrypt("sto=\n", "/5U8+nU5510=\n")) == false) goto L18;
             */
            /* JADX WARN: Code restructure failed: missing block: B:38:0x00fa, code lost:
                r7 = false;
             */
            @Override // androidx.activity.result.ActivityResultCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final void onActivityResult(java.lang.Object r7) {
                /*
                    Method dump skipped, instructions count: 430
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.blue.cat.fast.thirdbrowser.view.VpnActivity$$ExternalSyntheticLambda0.onActivityResult(java.lang.Object):void");
            }
        }, new StartService());
        StringFog.decrypt("UY2UFnwo6kBlh4E+bCjmREqcii1qL/pewWhVXy98rxIDyNMCBXyvEgPI018vIYUSA8jTAg==\n", "I+jzfw9cjzI=\n");
        this.connect = registerForActivityResult;
    }

    public final void beforeClickVpn() {
        BuildersKt.launch$default(LifecycleOwnerKt.getLifecycleScope(this), null, new VpnActivity$beforeClickVpn$1(this, null), 3);
    }

    public final void connectSuccess() {
        getBinding().setShowGuide(Boolean.FALSE);
        getBinding().setVpnState(2);
        getBinding().tvVpnState.setText(StringFog.decrypt("wY2YqLL1Gofm\n", "guL2xteWbuI=\n"));
        if (this.jumpType != -1) {
            BVDataUtils.executeWithDebounce$default(BVDataUtils.INSTANCE, new Function0<Unit>() { // from class: com.blue.cat.fast.thirdbrowser.view.VpnActivity$connectSuccess$1
                {
                    super(0);
                }

                /* JADX WARN: Code restructure failed: missing block: B:17:0x0040, code lost:
                    if (r3 != false) goto L16;
                 */
                @Override // kotlin.jvm.functions.Function0
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public final kotlin.Unit invoke() {
                    /*
                        r5 = this;
                        int r0 = com.blue.cat.fast.thirdbrowser.view.ResultActivity.$r8$clinit
                        java.lang.String r0 = "vgzxMJI0GU0=\n"
                        java.lang.String r1 = "32+FWeRdbTQ=\n"
                        java.lang.String r0 = com.blue.cat.fast.thirdbrowser.StringFog.decrypt(r0, r1)
                        com.blue.cat.fast.thirdbrowser.view.VpnActivity r1 = com.blue.cat.fast.thirdbrowser.view.VpnActivity.this
                        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r1, r0)
                        android.content.Intent r0 = new android.content.Intent
                        java.lang.Class<com.blue.cat.fast.thirdbrowser.view.ResultActivity> r2 = com.blue.cat.fast.thirdbrowser.view.ResultActivity.class
                        r0.<init>(r1, r2)
                        r2 = 17492(0x4454, float:2.4512E-41)
                        r1.startActivityForResult(r0, r2)
                        com.blue.cat.fast.thirdbrowser.model.TimerViewModel r0 = com.blue.cat.fast.thirdbrowser.App.viewModel
                        if (r0 == 0) goto L57
                        kotlinx.coroutines.StandaloneCoroutine r1 = r0.timerJob
                        if (r1 == 0) goto L42
                        java.lang.Object r1 = r1.getState$kotlinx_coroutines_core()
                        boolean r2 = r1 instanceof kotlinx.coroutines.CompletedExceptionally
                        r3 = 0
                        r4 = 1
                        if (r2 != 0) goto L3c
                        boolean r2 = r1 instanceof kotlinx.coroutines.JobSupport.Finishing
                        if (r2 == 0) goto L3a
                        kotlinx.coroutines.JobSupport$Finishing r1 = (kotlinx.coroutines.JobSupport.Finishing) r1
                        boolean r1 = r1.isCancelling()
                        if (r1 == 0) goto L3a
                        goto L3c
                    L3a:
                        r1 = r3
                        goto L3d
                    L3c:
                        r1 = r4
                    L3d:
                        if (r1 != r4) goto L40
                        r3 = r4
                    L40:
                        if (r3 == 0) goto L57
                    L42:
                        long r1 = java.lang.System.currentTimeMillis()
                        r0.startTime = r1
                        kotlinx.coroutines.GlobalScope r1 = kotlinx.coroutines.GlobalScope.INSTANCE
                        com.blue.cat.fast.thirdbrowser.model.TimerViewModel$startTimer$1 r2 = new com.blue.cat.fast.thirdbrowser.model.TimerViewModel$startTimer$1
                        r3 = 0
                        r2.<init>(r0, r3)
                        r4 = 3
                        kotlinx.coroutines.StandaloneCoroutine r1 = kotlinx.coroutines.BuildersKt.launch$default(r1, r3, r2, r4)
                        r0.timerJob = r1
                    L57:
                        kotlin.Unit r0 = kotlin.Unit.INSTANCE
                        return r0
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.blue.cat.fast.thirdbrowser.view.VpnActivity$connectSuccess$1.invoke():java.lang.Object");
                }
            });
        }
    }

    public final void disConnectSuccess() {
        getBinding().setVpnState(0);
        getBinding().tvVpnState.setText(StringFog.decrypt("hfetgvu5nVyi6ruF\n", "wZ7e4ZTX8zk=\n"));
        if (this.jumpType != -1) {
            BVDataUtils.INSTANCE.getClass();
            StringFog.decrypt("YNM8SOca\n", "AbBIIYh09n0=\n");
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - BVDataUtils.lastExecutionTime > 1000) {
                ResultActivity.Companion.start(this);
                App app = App.instance;
                TimerViewModel timerViewModel = App.viewModel;
                if (timerViewModel != null) {
                    StandaloneCoroutine standaloneCoroutine = timerViewModel.timerJob;
                    if (standaloneCoroutine != null) {
                        standaloneCoroutine.cancel(null);
                    }
                    App.Companion.setTimerText(StringFog.decrypt("h+wsCxt6mPU=\n", "t9wWOytAqMU=\n"));
                }
                Unit unit = Unit.INSTANCE;
                BVDataUtils.lastExecutionTime = currentTimeMillis;
            }
        }
    }

    public final ActivityVpnBinding getBinding() {
        return (ActivityVpnBinding) this.binding$delegate.getValue();
    }

    public final void initializeServerData() {
        BVDataUtils.INSTANCE.getClass();
        BrowserServiceBean connectBrowserServiceBean = BVDataUtils.getConnectBrowserServiceBean();
        Profile profile = ProfileManager.getProfile(DataStore.getProfileId());
        if (profile != null) {
            setSkServerData(profile, connectBrowserServiceBean);
            ProfileManager.updateProfile(profile);
        } else {
            Profile profile2 = new Profile(0);
            setSkServerData(profile2, connectBrowserServiceBean);
            StringFog.decrypt("2OuIMc+bQg==\n", "qJnnV6b3J8c=\n");
            long j = 0;
            profile2.id = 0L;
            SynchronizedLazyImpl synchronizedLazyImpl = PrivateDatabase.instance$delegate;
            Long nextOrder = PrivateDatabase.Companion.getProfileDao().nextOrder();
            if (nextOrder != null) {
                j = nextOrder.longValue();
            }
            profile2.userOrder = j;
            profile2.id = PrivateDatabase.Companion.getProfileDao().create(profile2);
        }
        connectBrowserServiceBean.setBestService(true);
        String decrypt = StringFog.decrypt("FLENETJAPIkA\n", "ZMNid1ssWcA=\n");
        RoomPreferenceDataStore roomPreferenceDataStore = DataStore.publicStore;
        roomPreferenceDataStore.getClass();
        StringFog.decrypt("z1RA\n", "pDE5/n3qjYQ=\n");
        KeyValuePair keyValuePair = new KeyValuePair(decrypt);
        keyValuePair.put(1L);
        roomPreferenceDataStore.kvPairDao.put(keyValuePair);
        roomPreferenceDataStore.fireChangeListener(decrypt);
    }

    public final boolean isConnectionProcess() {
        Integer num = getBinding().mVpnState;
        if (num != null && num.intValue() == 1) {
            BrowserKey.INSTANCE.getClass();
            if (BrowserKey.sharedPreferences.getInt(StringFog.decrypt("aoLDZYphZIZPhsxSgw==\n", "HPKtJuYIB+0=\n"), -1) == 0) {
                return true;
            }
        }
        return false;
    }

    public final boolean isDisconnectionProcess() {
        Integer num = getBinding().mVpnState;
        if (num != null && num.intValue() == 1) {
            BrowserKey.INSTANCE.getClass();
            if (BrowserKey.sharedPreferences.getInt(StringFog.decrypt("aoLDZYphZIZPhsxSgw==\n", "HPKtJuYIB+0=\n"), -1) == 2) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public final void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1366) {
            BrowserKey.INSTANCE.getClass();
            if (!Intrinsics.areEqual(BrowserKey.getClickVpn(), "")) {
                if (BrowserKey.getVpnState() != 2) {
                    BrowserKey.setConnectVpn(BrowserKey.getClickVpn());
                }
                initializeServerData();
                beforeClickVpn();
            }
        }
        if (i == 17492) {
            BrowserKey.INSTANCE.getClass();
            if (!Intrinsics.areEqual(BrowserKey.getClickVpn(), "") && BrowserKey.getVpnState() != 2) {
                BrowserKey.setConnectVpn(BrowserKey.getClickVpn());
                initializeServerData();
                BrowserKey.setClickVpn("");
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x016a, code lost:
        if (r5.hasTransport(2) != false) goto L16;
     */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0170  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x017c  */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void onCreate(android.os.Bundle r5) {
        /*
            Method dump skipped, instructions count: 390
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.blue.cat.fast.thirdbrowser.view.VpnActivity.onCreate(android.os.Bundle):void");
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        RoomPreferenceDataStore roomPreferenceDataStore = DataStore.publicStore;
        RoomPreferenceDataStore roomPreferenceDataStore2 = DataStore.publicStore;
        roomPreferenceDataStore2.getClass();
        StringFog.decrypt("KYx8K8Nw8uA=\n", "ReUPX6Yel5I=\n");
        roomPreferenceDataStore2.listeners.remove(this);
        this.connection.disconnect(this);
    }

    @Override // com.github.shadowsocks.preference.OnPreferenceDataStoreChangeListener
    public final void onPreferenceDataStoreChanged(PreferenceDataStore preferenceDataStore, String str) {
        Intrinsics.checkNotNullParameter(preferenceDataStore, StringFog.decrypt("ea5U+i4=\n", "Cto7iEvhC84=\n"));
        StringFog.decrypt("f79I\n", "FNox1jXkTjU=\n");
        if (Intrinsics.areEqual(str, StringFog.decrypt("27rC5/x6aqTHu9U=\n", "qN+wkZUZD+k=\n"))) {
            ShadowsocksConnection shadowsocksConnection = this.connection;
            shadowsocksConnection.disconnect(this);
            shadowsocksConnection.connect(this, this);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onResume() {
        super.onResume();
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void onServiceConnected(IShadowsocksService iShadowsocksService) {
        StringFog.decrypt("FVW7o/Dysg==\n", "ZjDJ1ZmR1/k=\n");
        BaseService$State baseService$State = BaseService$State.values()[iShadowsocksService.getState()];
        String decrypt = StringFog.decrypt("b5kL\n", "O9hMHoD3XD4=\n");
        Log.e(decrypt, StringFog.decrypt("TgqPla7KOTVEJ7OestkzIkQA5tA=\n", "IWTc8Ny8UFY=\n") + baseService$State.canStop);
        boolean z = baseService$State.canStop;
        if (z) {
            BrowserKey.INSTANCE.getClass();
            BrowserKey.setVpnState(2);
            connectSuccess();
        } else if (!z) {
            BrowserKey.INSTANCE.getClass();
            BrowserKey.setVpnState(0);
            disConnectSuccess();
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onStop() {
        super.onStop();
        ShadowsocksConnection shadowsocksConnection = this.connection;
        ShadowsocksConnection$serviceCallback$1 shadowsocksConnection$serviceCallback$1 = shadowsocksConnection.serviceCallback;
        try {
            IShadowsocksService iShadowsocksService = shadowsocksConnection.service;
            if (iShadowsocksService != null) {
                iShadowsocksService.stopListeningForBandwidth(shadowsocksConnection$serviceCallback$1);
            }
        } catch (RemoteException unused) {
        }
        shadowsocksConnection.bandwidthTimeout = 0L;
        if (isConnectionProcess()) {
            this.jumpType = -1;
            disConnectSuccess();
        }
        if (isDisconnectionProcess()) {
            this.jumpType = -1;
            connectSuccess();
        }
    }

    public final void setSkServerData(Profile profile, BrowserServiceBean browserServiceBean) {
        profile.name = browserServiceBean.getCountry() + '-' + browserServiceBean.getCity();
        profile.setHost(browserServiceBean.getIp());
        profile.setPassword(browserServiceBean.getPassword());
        profile.setMethod(browserServiceBean.getMethod());
        profile.remotePort = browserServiceBean.getProxyPort();
        BrowserKey browserKey = BrowserKey.INSTANCE;
        String json = new Gson().toJson(browserServiceBean);
        Intrinsics.checkNotNullExpressionValue(json, StringFog.decrypt("zfwu4QWVmVTlxTLgQ5TVRfn7Be5Z3Z4=\n", "io9Bjy28tyA=\n"));
        browserKey.getClass();
        BrowserKey.setConnectVpn(json);
        ImageView imageView = getBinding().imgFast;
        BVDataUtils bVDataUtils = BVDataUtils.INSTANCE;
        String country = browserServiceBean.getCountry();
        bVDataUtils.getClass();
        imageView.setImageResource(BVDataUtils.getImageFlag(country));
        getBinding().tvServiceName.setText(browserServiceBean.getCountry());
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void stateChanged(BaseService$State baseService$State) {
        Intrinsics.checkNotNullParameter(baseService$State, StringFog.decrypt("VxJzrQ8=\n", "JGYS2WpXHvs=\n"));
        String decrypt = StringFog.decrypt("r/jL\n", "+7mMZ+rCV/I=\n");
        StringBuilder sb = new StringBuilder();
        sb.append(StringFog.decrypt("VQBL0mIpDYJIE0/CPUo=\n", "JnQqpgdqZeM=\n"));
        boolean z = baseService$State.canStop;
        sb.append(z);
        Log.e(decrypt, sb.toString());
        if (z) {
            BrowserKey.INSTANCE.getClass();
            BrowserKey.setVpnState(2);
            connectSuccess();
        } else if (!z) {
            BrowserKey.INSTANCE.getClass();
            BrowserKey.setVpnState(0);
            disConnectSuccess();
        }
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void trafficUpdated(TrafficStats trafficStats) {
        Intrinsics.checkNotNullParameter(trafficStats, StringFog.decrypt("tuZX0cs=\n", "xZI2pbjxVqM=\n"));
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void onBinderDied() {
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void onServiceDisconnected() {
    }

    @Override // com.github.shadowsocks.aidl.ShadowsocksConnection.Callback
    public final void trafficPersisted() {
    }
}