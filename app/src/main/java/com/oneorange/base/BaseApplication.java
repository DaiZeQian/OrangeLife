package com.oneorange.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.oneorange.manager.OrangeLifeHelper;
import com.oneorange.utils.LogUtil;
import com.oneorange.utils.UserPrefenceManager;
import com.oneorange.volley.RequestQueue;
import com.oneorange.volley.toolbox.Volley;
import com.umeng.comm.core.CommunitySDK;
import com.umeng.comm.core.impl.CommunityFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by dzq on 2015/7/23.
 */
public class BaseApplication extends LitePalApplication {
    /**
     * 本项目的唯一请求队列(Volley)
     */
    private RequestQueue requestQueue;

    private static BaseApplication instance;

    public static BaseApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtil.d("BaseApplication", "BaseApplication onCreate");
        UserPrefenceManager.init(this);
        OrangeLifeHelper.getInstance().init(this);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        initImageLoader(getApplicationContext());
        SQLiteDatabase db = Connector.getDatabase();//创建数据库

        initOKhttp();
        //友盟微社区初始化
        CommunitySDK mCommSDK = CommunityFactory.getCommSDK(getApplicationContext()); // 初始化sdk，请传递ApplicationContext
        mCommSDK.initSDK(getApplicationContext());
    }

    /**
     * 获取请求队列(Volley)
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }


    public void initOKhttp() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//设置可以访问所有的https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                        //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


    /**
     * 初始化图片加载
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "chengyun/Cache");
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPoolSize(3);//线程池加载数量
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        //config.diskCacheFileNameGenerator(new Md5FileNameGenerator());//将保存的url做md5加密  方法注释为以什么什么命名
        config.memoryCacheExtraOptions(480, 800); // max width, max height，即保存的每个缓存文件的最大长宽
        config.diskCacheSize(50 * 1024 * 1024); // 10 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.diskCacheFileCount(100);
        config.writeDebugLogs(); // 千万要记得 上线了把这个删了 做debug测试用的
        config.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存目录
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000));// connectTimeout (5 s), readTimeout (30 s)超时时间
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

}
