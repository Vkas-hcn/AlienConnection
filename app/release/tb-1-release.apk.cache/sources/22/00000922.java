package com.blue.cat.fast.thirdbrowser.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;
import androidx.databinding.DataBinderMapperImpl;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blue.cat.fast.thirdbrowser.R;
import com.blue.cat.fast.thirdbrowser.StringFog;
import com.blue.cat.fast.thirdbrowser.databinding.ActivityBookmarkBinding;
import com.blue.cat.fast.thirdbrowser.utils.BVDataUtils;
import com.blue.cat.fast.thirdbrowser.utils.BrowserDataBean;
import com.blue.cat.fast.thirdbrowser.view.BrowserDataAdapter;
import com.blue.cat.fast.thirdbrowser.view.MainActivity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import kotlin.LazyKt__LazyKt;
import kotlin.SynchronizedLazyImpl;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt$sortedWith$1;
import kotlin.text.StringsKt__StringsKt;

/* compiled from: BookmarkActivity.kt */
/* loaded from: classes.dex */
public final class BookmarkActivity extends AppCompatActivity {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BrowserDataAdapter adapter;
    public ArrayList allBookmarkBeanData;
    public final SynchronizedLazyImpl binding$delegate = LazyKt__LazyKt.lazy(new Function0<ActivityBookmarkBinding>() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$binding$2
        {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final ActivityBookmarkBinding invoke() {
            LayoutInflater layoutInflater = BookmarkActivity.this.getLayoutInflater();
            int i = ActivityBookmarkBinding.$r8$clinit;
            DataBinderMapperImpl dataBinderMapperImpl = DataBindingUtil.sMapper;
            return (ActivityBookmarkBinding) ViewDataBinding.inflateInternal(layoutInflater, R.layout.activity_bookmark);
        }
    });

    public final ActivityBookmarkBinding getBinding() {
        return (ActivityBookmarkBinding) this.binding$delegate.getValue();
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public final void onCreate(Bundle bundle) {
        ArrayList arrayList;
        boolean z;
        super.onCreate(bundle);
        setContentView(getBinding().mRoot);
        BVDataUtils.INSTANCE.getClass();
        List bookmarkList = BVDataUtils.getBookmarkList();
        if (bookmarkList != null) {
            CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1 asSequence = CollectionsKt___CollectionsKt.asSequence(bookmarkList);
            Comparator comparator = new Comparator() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$initBookmarkAdapter$$inlined$sortedByDescending$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return MotionEventCompat.compareValues(((BrowserDataBean) t2).getTimeDate(), ((BrowserDataBean) t).getTimeDate());
                }
            };
            StringFog.decrypt("hiC/7lk9\n", "ulTXhyoDhVg=\n");
            StringFog.decrypt("KNIKTga7A4Ikzw==\n", "S71nPmfJYvY=\n");
            arrayList = SequencesKt___SequencesKt.toMutableList(new SequencesKt___SequencesKt$sortedWith$1(asSequence, comparator));
        } else {
            arrayList = null;
        }
        ActivityBookmarkBinding binding = getBinding();
        if (arrayList == null) {
            z = true;
        } else {
            z = false;
        }
        binding.setHaveData(Boolean.valueOf(z));
        this.allBookmarkBeanData = new ArrayList();
        if (arrayList != null) {
            this.allBookmarkBeanData = arrayList;
            this.adapter = new BrowserDataAdapter(arrayList);
            RecyclerView recyclerView = getBinding().rvBookmark;
            BrowserDataAdapter browserDataAdapter = this.adapter;
            if (browserDataAdapter != null) {
                recyclerView.setAdapter(browserDataAdapter);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(1);
                linearLayoutManager.setOrientation(1);
                getBinding().rvBookmark.setLayoutManager(linearLayoutManager);
                BrowserDataAdapter browserDataAdapter2 = this.adapter;
                if (browserDataAdapter2 != null) {
                    BrowserDataAdapter.OnItemClickListener onItemClickListener = new BrowserDataAdapter.OnItemClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$initBookmarkAdapter$1
                        @Override // com.blue.cat.fast.thirdbrowser.view.BrowserDataAdapter.OnItemClickListener
                        public final void onItemClick(int i) {
                            BookmarkActivity bookmarkActivity = BookmarkActivity.this;
                            ArrayList arrayList2 = bookmarkActivity.allBookmarkBeanData;
                            if (arrayList2 != null) {
                                String urlData = ((BrowserDataBean) arrayList2.get(i)).getUrlData();
                                boolean z2 = MainActivity.isHistory;
                                MainActivity.Companion.loadWeb(bookmarkActivity, urlData, false);
                                return;
                            }
                            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("f1dnN+WxJHh/SWA3778hUX9Pag==\n", "HjsLdYreTxU=\n"));
                            throw null;
                        }
                    };
                    StringFog.decrypt("G6NpO9gdy5Y=\n", "d8oaT71zruQ=\n");
                    browserDataAdapter2.listener = onItemClickListener;
                    BrowserDataAdapter browserDataAdapter3 = this.adapter;
                    if (browserDataAdapter3 != null) {
                        BrowserDataAdapter.OnItemDeleteListener onItemDeleteListener = new BrowserDataAdapter.OnItemDeleteListener() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$initBookmarkAdapter$2
                            @Override // com.blue.cat.fast.thirdbrowser.view.BrowserDataAdapter.OnItemDeleteListener
                            public final void onItemDelete(int i) {
                                BookmarkActivity bookmarkActivity = BookmarkActivity.this;
                                BrowserDataAdapter browserDataAdapter4 = bookmarkActivity.adapter;
                                if (browserDataAdapter4 != null) {
                                    browserDataAdapter4.deleteData(i, false);
                                    ActivityBookmarkBinding binding2 = bookmarkActivity.getBinding();
                                    ArrayList arrayList2 = bookmarkActivity.allBookmarkBeanData;
                                    if (arrayList2 != null) {
                                        binding2.setHaveData(Boolean.valueOf(arrayList2.isEmpty()));
                                        return;
                                    } else {
                                        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("nWcNkMQkR1ydeQqQzipCdZ1/AA==\n", "/Ath0qtLLDE=\n"));
                                        throw null;
                                    }
                                }
                                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("JnPuLtCCng==\n", "RxePXqTn7Fo=\n"));
                                throw null;
                            }
                        };
                        StringFog.decrypt("HFAXB/bWrc8=\n", "cDlkc5O4yL0=\n");
                        browserDataAdapter3.listenerDelete = onItemDeleteListener;
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("DXRAZw+j/g==\n", "bBAhF3vGjHM=\n"));
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("EQBuRrYUPA==\n", "cGQPNsJxTtw=\n"));
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("hgtTpMk55Q==\n", "528y1L1cl+c=\n"));
                throw null;
            }
        }
        getBinding().edtSearchBookmark.addTextChangedListener(new TextWatcher() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$editSearchFun$1
            @Override // android.text.TextWatcher
            public final void afterTextChanged(Editable editable) {
                ArrayList arrayList2;
                String lowerCase;
                String lowerCase2;
                BookmarkActivity bookmarkActivity = BookmarkActivity.this;
                if (bookmarkActivity.allBookmarkBeanData != null) {
                    if (!arrayList2.isEmpty()) {
                        ArrayList<BrowserDataBean> arrayList3 = bookmarkActivity.allBookmarkBeanData;
                        if (arrayList3 != null) {
                            for (BrowserDataBean browserDataBean : arrayList3) {
                                String urlTitle = browserDataBean.getUrlTitle();
                                Locale locale = Locale.getDefault();
                                Intrinsics.checkNotNullExpressionValue(locale, StringFog.decrypt("S1HUlKvCC2RAQIj5\n", "LDSg0M6kahE=\n"));
                                Intrinsics.checkNotNullExpressionValue(urlTitle.toLowerCase(locale), StringFog.decrypt("r2zpX0xAFa6xZfZNQk0H4Lwq01geSAjp8ir0QyBOEeupR+FfCQkK4bhl7ElF\n", "2wSALGwhZo4=\n"));
                                String valueOf = String.valueOf(editable);
                                Locale locale2 = Locale.getDefault();
                                Intrinsics.checkNotNullExpressionValue(locale2, StringFog.decrypt("JBI0lFWGyqEvA2j5\n", "Q3dA0DDgq9Q=\n"));
                                Intrinsics.checkNotNullExpressionValue(valueOf.toLowerCase(locale2), StringFog.decrypt("9cFpnT2b4GHryHaPM5byL+aHU5pvk/0mqId0gVGV5CTz6mGdeNL/LuLIbIs0\n", "gakA7h36k0E=\n"));
                                browserDataBean.setHaveShow(!StringsKt__StringsKt.contains$default(lowerCase, lowerCase2));
                            }
                            BrowserDataAdapter browserDataAdapter4 = bookmarkActivity.adapter;
                            if (browserDataAdapter4 != null) {
                                browserDataAdapter4.mObservable.notifyChanged();
                                ArrayList<BrowserDataBean> arrayList4 = bookmarkActivity.allBookmarkBeanData;
                                if (arrayList4 != null) {
                                    boolean z2 = false;
                                    for (BrowserDataBean browserDataBean2 : arrayList4) {
                                        if (!browserDataBean2.getHaveShow()) {
                                            z2 = true;
                                        }
                                    }
                                    bookmarkActivity.getBinding().setHaveData(Boolean.valueOf(!z2));
                                    return;
                                }
                                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("wXCZKJYpijzBbp4onCePFcFolA==\n", "oBz1avlG4VE=\n"));
                                throw null;
                            }
                            Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("v9thw+szGw==\n", "3r8As59Wafw=\n"));
                            throw null;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("dGyO5Ha6KKJ0conkfLQti3R0gw==\n", "FQDiphnVQ88=\n"));
                        throw null;
                    }
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException(StringFog.decrypt("UdFNub048QlRz0q5tzb0IFHJQA==\n", "ML0h+9JXmmQ=\n"));
                throw null;
            }

            @Override // android.text.TextWatcher
            public final void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public final void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }
        });
        getBinding().imgFinish.setOnClickListener(new View.OnClickListener() { // from class: com.blue.cat.fast.thirdbrowser.view.BookmarkActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                int i = BookmarkActivity.$r8$clinit;
                String decrypt = StringFog.decrypt("e/b5Ob3Y\n", "D56QSpnofgg=\n");
                BookmarkActivity bookmarkActivity = BookmarkActivity.this;
                Intrinsics.checkNotNullParameter(bookmarkActivity, decrypt);
                bookmarkActivity.finish();
            }
        });
    }
}