package com.blue.cat.fast.thirdbrowser.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.OnBackPressedDispatcherKt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBinderMapperImpl;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.blue.cat.fast.thirdbrowser.R;
import com.blue.cat.fast.thirdbrowser.StringFog;
import com.blue.cat.fast.thirdbrowser.databinding.ActivityMainBinding;
import com.blue.cat.fast.thirdbrowser.utils.BVDataUtils;
import com.blue.cat.fast.thirdbrowser.utils.BrowserDataBean;
import com.blue.cat.fast.thirdbrowser.utils.BrowserKey;
import com.blue.cat.fast.thirdbrowser.utils.BrowserServiceBean;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kotlin.LazyKt__LazyKt;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.SynchronizedLazyImpl;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: MainActivity.kt */
/* loaded from: classes.dex */
public final class MainActivity extends AppCompatActivity {
    public static boolean isHistory;
    public final SynchronizedLazyImpl binding$delegate = LazyKt__LazyKt.lazy(new Function0<ActivityMainBinding>() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$binding$2
        {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final ActivityMainBinding invoke() {
            LayoutInflater layoutInflater = MainActivity.this.getLayoutInflater();
            int i = ActivityMainBinding.$r8$clinit;
            DataBinderMapperImpl dataBinderMapperImpl = DataBindingUtil.sMapper;
            return (ActivityMainBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_main);
        }
    });
    public String loadUrl = "";
    public String webTitle = "";
    public String webUrl = "";
    public WebView webView;

    /* compiled from: MainActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static void loadWeb(AppCompatActivity appCompatActivity, String str, boolean z) {
            Intrinsics.checkNotNullParameter(appCompatActivity, StringFog.decrypt("5dUpezhsFOA=\n", "hLZdEk4FYJk=\n"));
            Intrinsics.checkNotNullParameter(str, StringFog.decrypt("zSp/\n", "uFgTcLp+7gE=\n"));
            Intent intent = new Intent(appCompatActivity, MainActivity.class);
            intent.putExtra(StringFog.decrypt("+ZJT\n", "jOA/jmVo81E=\n"), str);
            appCompatActivity.startActivity(intent);
            appCompatActivity.finish();
            MainActivity.isHistory = z;
        }
    }

    public static String searchGoogle(String str) {
        String decrypt = StringFog.decrypt("O0EwTMP0qNpKHi9P66runREdKEuNq72CEh4EFsvs5oEVU3cXy+zmgRUaYheYra2uBEQiCJq9z95N\nMgQV66rPjlQUA1ma/qLYXDRzEZ3YvK4ERCJlzLa+wBhBYmOHqauoHlh0Dcqtrd1KR3IRiKA=\n", "ZWlYOLeEkvU=\n");
        StringFog.decrypt("cR/bnogzsA==\n", "AX6v6u1B3uk=\n");
        Pattern compile = Pattern.compile(decrypt);
        Intrinsics.checkNotNullExpressionValue(compile, StringFog.decrypt("4sacT9KIBIvxyIVL3pYPig==\n", "ganxP7vkYaM=\n"));
        StringFog.decrypt("0fc2KqDbZP7L4icxuA==\n", "v5ZCQ9a+NJ8=\n");
        StringFog.decrypt("6Xrn1ZE=\n", "gBSXoOVPHR4=\n");
        if (compile.matcher(str).matches()) {
            if (!StringsKt__StringsJVMKt.startsWith$default(str, StringFog.decrypt("v+GRx2y07A==\n", "15Xlt1abw9E=\n")) && !StringsKt__StringsJVMKt.startsWith$default(str, StringFog.decrypt("SEvEI0kRPnA=\n", "ID+wUzorEV8=\n"))) {
                return StringFog.decrypt("36GkSAqe3gY=\n", "t9XQOHmk8Sk=\n").concat(str);
            }
            return str;
        }
        return StringFog.decrypt("YF7YAhMfIE9/XdtcB0pgB2RPghEPSCATbUveEQgafl0=\n", "CCqscmAlD2A=\n").concat(StringsKt__StringsJVMKt.replace$default(str, " ", StringFog.decrypt("mw==\n", "sH9xUd3Ryqw=\n")));
    }

    public final ActivityMainBinding getBinding() {
        return (ActivityMainBinding) this.binding$delegate.getValue();
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    public final void initWeb() {
        getBinding().homeWeb.removeAllViews();
        WebView webView = new WebView(this);
        webView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        boolean z = true;
        webView.getSettings().setJavaScriptEnabled(true);
        this.webView = webView;
        FrameLayout frameLayout = getBinding().homeWeb;
        WebView webView2 = this.webView;
        if (webView2 != null) {
            frameLayout.addView(webView2);
            getBinding().setShowWeb(Boolean.FALSE);
            WebView webView3 = this.webView;
            if (webView3 != null) {
                webView3.getSettings().setJavaScriptEnabled(true);
                WebView webView4 = this.webView;
                if (webView4 != null) {
                    webView4.setWebViewClient(new WebViewClient() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$initWeb$2
                        @Override // android.webkit.WebViewClient
                        public final void onPageFinished(WebView webView5, String str) {
                            MainActivity mainActivity = MainActivity.this;
                            ActivityMainBinding binding = mainActivity.getBinding();
                            Intrinsics.checkNotNullExpressionValue(binding, StringFog.decrypt("VZzPF4T5hQ==\n", "N/Whc+2X4vo=\n"));
                            mainActivity.setGoOrBackIcon(binding);
                            mainActivity.getBinding().progressBarLoading.setVisibility(8);
                        }

                        @Override // android.webkit.WebViewClient
                        public final void onPageStarted(WebView webView5, String str, Bitmap bitmap) {
                            String valueOf = String.valueOf(str);
                            MainActivity mainActivity = MainActivity.this;
                            mainActivity.webUrl = valueOf;
                            mainActivity.getBinding().progressBarLoading.setVisibility(0);
                            ActivityMainBinding binding = mainActivity.getBinding();
                            Intrinsics.checkNotNullExpressionValue(binding, StringFog.decrypt("ovt0P7hhDA==\n", "wJIaW9EPa6Y=\n"));
                            mainActivity.setGoOrBackIcon(binding);
                        }

                        @Override // android.webkit.WebViewClient
                        public final boolean shouldOverrideUrlLoading(WebView webView5, String str) {
                            Intrinsics.checkNotNullParameter(webView5, StringFog.decrypt("kQNLaQ==\n", "52ouHoKFbkQ=\n"));
                            Intrinsics.checkNotNullParameter(str, StringFog.decrypt("Ohpk\n", "T2gInTK5ikE=\n"));
                            if (Intrinsics.areEqual(MainActivity.this.loadUrl, str)) {
                                webView5.loadUrl(str);
                                return false;
                            }
                            return super.shouldOverrideUrlLoading(webView5, str);
                        }
                    });
                    WebView webView5 = this.webView;
                    if (webView5 != null) {
                        webView5.setWebChromeClient(new WebChromeClient() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$initWeb$3
                            @Override // android.webkit.WebChromeClient
                            public final void onReceivedTitle(WebView webView6, String str) {
                                super.onReceivedTitle(webView6, str);
                                String valueOf = String.valueOf(str);
                                MainActivity mainActivity = MainActivity.this;
                                mainActivity.webTitle = valueOf;
                                String str2 = mainActivity.webUrl;
                                BVDataUtils.INSTANCE.getClass();
                                BrowserDataBean browserDataBean = new BrowserDataBean(str2, valueOf, BVDataUtils.getCurrentTime(), false);
                                if (!MainActivity.isHistory) {
                                    StringFog.decrypt("M+x1kw==\n", "UYkU/RVhhPE=\n");
                                    List webPageHistory = BVDataUtils.getWebPageHistory();
                                    if (webPageHistory != null) {
                                        webPageHistory.add(browserDataBean);
                                        BrowserKey browserKey = BrowserKey.INSTANCE;
                                        String json = new Gson().toJson(webPageHistory);
                                        Intrinsics.checkNotNullExpressionValue(json, StringFog.decrypt("MkXJKMX4AhgafNUpg/lIDQFXjw==\n", "dTamRu3RLGw=\n"));
                                        browserKey.getClass();
                                        BrowserKey.setHistory_data_browser(json);
                                    } else {
                                        ArrayList arrayList = new ArrayList();
                                        arrayList.add(browserDataBean);
                                        BrowserKey browserKey2 = BrowserKey.INSTANCE;
                                        String json2 = new Gson().toJson(arrayList);
                                        Intrinsics.checkNotNullExpressionValue(json2, StringFog.decrypt("PXyib4P8mYEVRb5uxf3bnAl75A==\n", "eg/NAavVt/U=\n"));
                                        browserKey2.getClass();
                                        BrowserKey.setHistory_data_browser(json2);
                                    }
                                }
                                MainActivity.isHistory = false;
                            }
                        });
                        if (this.loadUrl.length() <= 0) {
                            z = false;
                        }
                        if (z) {
                            getBinding().setShowWeb(Boolean.TRUE);
                            WebView webView6 = this.webView;
                            if (webView6 != null) {
                                webView6.loadUrl(this.loadUrl);
                                return;
                            } else {
                                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("lHyY0Y6sfA==\n", "4xn6h+fJC38=\n"));
                                throw null;
                            }
                        }
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("YkY5nXIB0Q==\n", "FSNbyxtkpps=\n"));
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("W/oPJCs+EA==\n", "LJ9tckJbZxQ=\n"));
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("R2873cNfPQ==\n", "MApZi6o6Sgc=\n"));
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("P0rN4WnZLw==\n", "SC+vtwC8WJ8=\n"));
        throw null;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getBinding().mRoot);
        String stringExtra = getIntent().getStringExtra(StringFog.decrypt("adCq\n", "HKLGkPrnQKM=\n"));
        if (stringExtra == null) {
            stringExtra = "";
        }
        this.loadUrl = stringExtra;
        String decrypt = StringFog.decrypt("NxDO\n", "Y1GJ2yEWGpI=\n");
        Log.e(decrypt, StringFog.decrypt("doLFv1vELawjzA==\n", "GeyGzT6lWck=\n") + this.loadUrl);
        getBinding().constraintLayout.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("4J2Re9ui\n", "lPX4CP+SBQM=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                StringFog.decrypt("E8ZEBezdO9c=\n", "cqUwbJq0T64=\n");
                mainActivity.startActivity(new Intent(mainActivity, VpnActivity.class));
            }
        });
        getBinding().tvInstagram.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("H0d+04K1\n", "ay8XoKaF0T8=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    webView.loadUrl(StringFog.decrypt("ulAg899sMTelUyOtxThtbLNDJuLBeH13vw==\n", "0iRUg6xWHhg=\n"));
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("uusJtkM9Lg==\n", "zY5r4CpYWUQ=\n"));
                    throw null;
                }
            }
        });
        getBinding().tvVimor.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda10
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("UY6KiYJ4\n", "Jebj+qZIlcU=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    webView.loadUrl(StringFog.decrypt("BkiH0BdlBwoYVZ7FC3FLSgMT\n", "bjzzoGRfKCU=\n"));
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("vcsBRHk1aw==\n", "yq5jEhBQHAg=\n"));
                    throw null;
                }
            }
        });
        getBinding().tvFb.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda11
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("+7/bQ9TX\n", "j9eyMPDndJc=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    webView.loadUrl(StringFog.decrypt("YNOK9mhkLrF/0ImofT9i+2rIke01PW7z\n", "CKf+hhteAZ4=\n"));
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("mIWNefNGwQ==\n", "7+DvL5ojtjQ=\n"));
                    throw null;
                }
            }
        });
        getBinding().tvTiktok.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("hkkbovsm\n", "8iFy0d8WWuo=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    webView.loadUrl(StringFog.decrypt("XNXqIO4/gUhD1ul+6WzFE1vKsDPyaA==\n", "NKGeUJ0Frmc=\n"));
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("injpO3m7oQ==\n", "/R2LbRDe1iE=\n"));
                    throw null;
                }
            }
        });
        getBinding().imgLeft.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("Fl20KVJR\n", "YjXdWnZhNJ8=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    if (webView.canGoBack()) {
                        WebView webView2 = mainActivity.webView;
                        if (webView2 != null) {
                            webView2.goBack();
                            return;
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("TO3SCiuY5Q==\n", "O4iwXEL9knk=\n"));
                            throw null;
                        }
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("HvqVWdKFzQ==\n", "aZ/3D7vguvw=\n"));
                throw null;
            }
        });
        getBinding().imgRight.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("Am137zU5\n", "dgUenBEJpE8=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    if (webView.canGoForward()) {
                        WebView webView2 = mainActivity.webView;
                        if (webView2 != null) {
                            webView2.goForward();
                            return;
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("8pnvHeXETg==\n", "hfyNS4yhOUg=\n"));
                            throw null;
                        }
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("kxF1I5ariQ==\n", "5HQXdf/O/kc=\n"));
                throw null;
            }
        });
        getBinding().imgHome.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda15
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("V6VltgBi\n", "I80MxSRSSHg=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.loadUrl = "";
                mainActivity.initWeb();
            }
        });
        getBinding().imgMenu.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("YxYfQsSY\n", "F352MeCo9WQ=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                mainActivity.getBinding().setShowMenu(Boolean.TRUE);
            }
        });
        getBinding().viewMenuBg.setOnClickListener(new MainActivity$$ExternalSyntheticLambda17(0, this));
        getBinding().tvReload.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("vCbmD4GG\n", "yE6PfKW23tw=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                WebView webView = mainActivity.webView;
                if (webView != null) {
                    webView.reload();
                    mainActivity.getBinding().setShowMenu(Boolean.FALSE);
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("CttFD6YtrQ==\n", "fb4nWc9I2gs=\n"));
                throw null;
            }
        });
        getBinding().tvHistory.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("/nsqYPLW\n", "ihNDE9bmUzk=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                StringFog.decrypt("Fcimc54nkpI=\n", "dKvSGuhO5us=\n");
                mainActivity.startActivity(new Intent(mainActivity, HistoryActivity.class));
                mainActivity.getBinding().setShowMenu(Boolean.FALSE);
            }
        });
        getBinding().tvBookmark.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("190Z5deb\n", "o7VwlvOrIOM=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                StringFog.decrypt("9E+JYUYGmv0=\n", "lSz9CDBv7oQ=\n");
                mainActivity.startActivity(new Intent(mainActivity, BookmarkActivity.class));
                mainActivity.getBinding().setShowMenu(Boolean.FALSE);
            }
        });
        getBinding().tvAdd.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("H/yFgVx8\n", "a5Ts8nhM3nI=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                String str = mainActivity.webUrl;
                String str2 = mainActivity.webTitle;
                BVDataUtils.INSTANCE.getClass();
                BrowserDataBean browserDataBean = new BrowserDataBean(str, str2, BVDataUtils.getCurrentTime(), false);
                StringFog.decrypt("IGN5jQ==\n", "QgYY41yovC8=\n");
                List bookmarkList = BVDataUtils.getBookmarkList();
                if (bookmarkList != null) {
                    bookmarkList.add(browserDataBean);
                    BrowserKey browserKey = BrowserKey.INSTANCE;
                    String json = new Gson().toJson(bookmarkList);
                    Intrinsics.checkNotNullExpressionValue(json, StringFog.decrypt("I0Olj521UAcLermO27QaEhBR4w==\n", "ZDDK4bWcfnM=\n"));
                    browserKey.getClass();
                    BrowserKey.setBookmark_data_browser(json);
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(browserDataBean);
                    BrowserKey browserKey2 = BrowserKey.INSTANCE;
                    String json2 = new Gson().toJson(arrayList);
                    Intrinsics.checkNotNullExpressionValue(json2, StringFog.decrypt("QyhCEXR2typrEV4QMnf1N3cvBA==\n", "BFstf1xfmV4=\n"));
                    browserKey2.getClass();
                    BrowserKey.setBookmark_data_browser(json2);
                }
                mainActivity.getBinding().setShowMenu(Boolean.FALSE);
                Toast.makeText(mainActivity, StringFog.decrypt("80Q7zSUpj/uRSjDCLSzd48RIN8M7O5vl3Uct\n", "sStUpkhI/ZA=\n"), 0).show();
            }
        });
        getBinding().tvPrivate.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda7
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Unit unit;
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("XOKCKUgo\n", "KIrrWmwY1RU=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                Intent intent = new Intent(StringFog.decrypt("c0E+blHLf6F7QS55UNY17nFbM3NQjE3GV3g=\n", "Ei9aHD6iG48=\n"));
                intent.setData(Uri.parse(StringFog.decrypt("zhO/66mA3RjCAr3+ttWCUtRJqvW+yJ1ewkmo9LeVgUPTA6L0\n", "pmfLm9q68jc=\n")));
                if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
                    mainActivity.startActivity(intent);
                    unit = Unit.INSTANCE;
                } else {
                    unit = null;
                }
                if (unit == null) {
                    Toast.makeText(mainActivity, StringFog.decrypt("v4Wi9bGZJCSTmLmipJMjapk=\n", "/ffNgsL8VgQ=\n"), 0).show();
                }
                mainActivity.getBinding().setShowMenu(Boolean.FALSE);
            }
        });
        getBinding().imgVpn.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda8
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("0oT4TsUE\n", "puyRPeE0NiA=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                StringFog.decrypt("E8ZEBezdO9c=\n", "cqUwbJq0T64=\n");
                mainActivity.startActivity(new Intent(mainActivity, VpnActivity.class));
            }
        });
        initWeb();
        getBinding().edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda0
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("GLutfmVw\n", "bNPEDUFAcfY=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                boolean z2 = false;
                if (i != 6 && (keyEvent == null || keyEvent.getKeyCode() != 66)) {
                    return false;
                }
                if (StringsKt__StringsKt.trim(String.valueOf(mainActivity.getBinding().edtSearch.getText())).toString().length() > 0) {
                    z2 = true;
                }
                if (z2) {
                    mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                    WebView webView = mainActivity.webView;
                    if (webView != null) {
                        webView.loadUrl(MainActivity.searchGoogle(String.valueOf(mainActivity.getBinding().edtSearch.getText())));
                        Editable text = mainActivity.getBinding().edtSearch.getText();
                        if (text != null) {
                            text.clear();
                        }
                        BVDataUtils bVDataUtils = BVDataUtils.INSTANCE;
                        AppCompatEditText appCompatEditText = mainActivity.getBinding().edtSearch;
                        Intrinsics.checkNotNullExpressionValue(appCompatEditText, StringFog.decrypt("aeoG4jK/jeNu5xzVPrCYrmM=\n", "C4NohlvR6s0=\n"));
                        bVDataUtils.getClass();
                        BVDataUtils.closeKeyboard(appCompatEditText, mainActivity);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("TdaBv5zirg==\n", "OrPj6fWH2Zw=\n"));
                        throw null;
                    }
                }
                return true;
            }
        });
        getBinding().edtSearchWeb.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$$ExternalSyntheticLambda1
            @Override // android.widget.TextView.OnEditorActionListener
            public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean z = MainActivity.isHistory;
                String decrypt2 = StringFog.decrypt("Azz+oAMt\n", "d1SX0ycdPlA=\n");
                MainActivity mainActivity = MainActivity.this;
                Intrinsics.checkNotNullParameter(mainActivity, decrypt2);
                boolean z2 = false;
                if (i != 6 && (keyEvent == null || keyEvent.getKeyCode() != 66)) {
                    return false;
                }
                if (StringsKt__StringsKt.trim(String.valueOf(mainActivity.getBinding().edtSearchWeb.getText())).toString().length() > 0) {
                    z2 = true;
                }
                if (z2) {
                    mainActivity.getBinding().setShowWeb(Boolean.TRUE);
                    WebView webView = mainActivity.webView;
                    if (webView != null) {
                        webView.loadUrl(MainActivity.searchGoogle(String.valueOf(mainActivity.getBinding().edtSearchWeb.getText())));
                        Editable text = mainActivity.getBinding().edtSearchWeb.getText();
                        if (text != null) {
                            text.clear();
                        }
                        BVDataUtils bVDataUtils = BVDataUtils.INSTANCE;
                        AppCompatEditText appCompatEditText = mainActivity.getBinding().edtSearchWeb;
                        Intrinsics.checkNotNullExpressionValue(appCompatEditText, StringFog.decrypt("XyFKgiXkkU9YLFC1KeuEAlUfQYQ=\n", "PUgk5kyK9mE=\n"));
                        bVDataUtils.getClass();
                        BVDataUtils.closeKeyboard(appCompatEditText, mainActivity);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("FmBX7EO1lg==\n", "YQU1uirQ4ag=\n"));
                        throw null;
                    }
                }
                return true;
            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        Intrinsics.checkNotNullExpressionValue(onBackPressedDispatcher, StringFog.decrypt("/7Bs+D07kZ31rV38OhSonOC/Wvo2NbM=\n", "kN4umV5Qwe8=\n"));
        OnBackPressedDispatcherKt.addCallback$default(onBackPressedDispatcher, this, new Function1<OnBackPressedCallback, Unit>() { // from class: com.blue.cat.fast.thirdbrowser.view.MainActivity$onBackFun$1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final Unit invoke(OnBackPressedCallback onBackPressedCallback) {
                Intrinsics.checkNotNullParameter(onBackPressedCallback, StringFog.decrypt("a5jo2mbGrU8rr+HfeYCtSCQ=\n", "T+yAsxXizCs=\n"));
                MainActivity mainActivity = MainActivity.this;
                if (Intrinsics.areEqual(mainActivity.getBinding().mShowWeb, Boolean.TRUE)) {
                    mainActivity.loadUrl = "";
                    mainActivity.initWeb();
                } else {
                    mainActivity.finish();
                }
                return Unit.INSTANCE;
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public final void onResume() {
        Object createFailure;
        String decrypt;
        String country;
        super.onResume();
        try {
            Gson gson = new Gson();
            BrowserKey.INSTANCE.getClass();
            createFailure = (BrowserServiceBean) gson.fromJson(BrowserServiceBean.class, BrowserKey.getConnectVpn());
        } catch (Throwable th) {
            createFailure = ResultKt.createFailure(th);
        }
        if (createFailure instanceof Result.Failure) {
            createFailure = null;
        }
        BrowserServiceBean browserServiceBean = (BrowserServiceBean) createFailure;
        ImageView imageView = getBinding().imgVpn;
        BVDataUtils bVDataUtils = BVDataUtils.INSTANCE;
        String str = "";
        String str2 = (browserServiceBean == null || (str2 = browserServiceBean.getCountry()) == null) ? "" : "";
        bVDataUtils.getClass();
        imageView.setImageResource(BVDataUtils.getImageFlag(str2));
        ImageView imageView2 = getBinding().imgFlag;
        if (browserServiceBean != null && (country = browserServiceBean.getCountry()) != null) {
            str = country;
        }
        imageView2.setImageResource(BVDataUtils.getImageFlag(str));
        TextView textView = getBinding().tvCountry;
        if (browserServiceBean == null || (decrypt = browserServiceBean.getCountry()) == null) {
            decrypt = StringFog.decrypt("fRceQni5fIpYHw0=\n", "Lnp/MAyZL+8=\n");
        }
        textView.setText(decrypt);
    }

    public final void setGoOrBackIcon(ActivityMainBinding activityMainBinding) {
        StringFog.decrypt("eJ5/KD6HTg==\n", "GvcRTFfpKUQ=\n");
        WebView webView = this.webView;
        if (webView != null) {
            boolean canGoBack = webView.canGoBack();
            ImageView imageView = activityMainBinding.imgLeft;
            if (canGoBack) {
                imageView.setImageResource(R.drawable.icon_left_1);
            } else {
                imageView.setImageResource(R.drawable.icon_left_2);
            }
            WebView webView2 = this.webView;
            if (webView2 != null) {
                boolean canGoForward = webView2.canGoForward();
                ImageView imageView2 = activityMainBinding.imgRight;
                if (canGoForward) {
                    imageView2.setImageResource(R.drawable.icon_right_1);
                    return;
                } else {
                    imageView2.setImageResource(R.drawable.icon_right_2);
                    return;
                }
            }
            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("kxF1I5ariQ==\n", "5HQXdf/O/kc=\n"));
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("HvqVWdKFzQ==\n", "aZ/3D7vguvw=\n"));
        throw null;
    }
}