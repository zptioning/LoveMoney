package com.zptioning.module_funds.net_work;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HttpUtils
 * @Author zptioning
 * @Date 2019-11-01 14:36
 * @Version 1.0
 * @Description 单例
 * 参考：https://blog.csdn.net/demonliuhui/article/details/71453656
 */
public final class HttpUtils {
    private static final String TAG = HttpUtils.class.getSimpleName() + "_tag";

    // 超时时间设置为10s
    private static final int TIME_OUT = 10;

    // 私有构造方法
    private HttpUtils() {
    }

    private static class HttpUtilsHolder {
        private static HttpUtils mHttpUtils = new HttpUtils();
    }

    public static HttpUtils getInstance() {
        return HttpUtilsHolder.mHttpUtils;
    }

    /**
     * 简单的异步 get请求
     */
    public void httpGet(final String url, final OnResultListener resultListener) {
        ThreadUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String result = httpGetResult(url);
                if (null != resultListener) {
                    resultListener.onResult(result);
                }
            }
        });
    }

    /**
     * 简单的异步 post请求
     */
    public void httpPost(final String url, final Map<String, String> map,
                         final OnResultListener resultListener) {

        ThreadUtils.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                String result = httpPostResult(url, map);
                if (null != resultListener) {
                    resultListener.onResult(result);
                }
            }
        });
    }

    /**
     * 简单的同步get请求
     */
    public String httpGetResult(final String url) {
        Log.e(TAG, url);
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    URL requestUrl = new URL(url);
                    connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode() == 200) {
                        InputStream inputStream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream, "GB18030"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (null != reader) {
                        reader.close();
                    }
                    if (null != connection) {
                        connection.disconnect();
                    }
                }

                String msg = sb.toString();
                Log.d(TAG, msg);
                return "".equals(msg) ? null : msg;
            }
        });

        ThreadUtils.getInstance().execute(futureTask);

        String result = null;
        try {
            result = futureTask.get(TIME_OUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 简单的同步post请求
     */
    public String httpPostResult(final String url, final Map<String, String> map) {
        final StringBuilder sb = new StringBuilder();
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;

                try {
                    URL requestUrl = new URL(url);
                    connection = (HttpURLConnection) requestUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    // 发送post请求必须设置的参数
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(true);
                    connection.setInstanceFollowRedirects(true);
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");


                    DataOutputStream outputStream
                            = new DataOutputStream(connection.getOutputStream());

                    StringBuilder requet = new StringBuilder();
                    for (String key : map.keySet()) {
                        requet.append(key +
                                "=" +
                                URLEncoder.encode(map.get(key), "UTF-8") +
                                "&");
                    }

                    // 写入请求参数
                    outputStream.writeBytes(requet.toString());
                    outputStream.flush();
                    outputStream.close();

                    if (200 == connection.getResponseCode()) {
                        InputStream inputStream = connection.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        bufferedReader.close();//关闭流
                    }
                    if (connection != null) {
                        connection.disconnect();//断开连接，释放资源
                    }
                }

                Log.d(TAG, sb.toString());
                return sb.toString();
            }
        });

        ThreadUtils.getInstance().execute(futureTask);
        String result = null;
        try {
            result = futureTask.get(TIME_OUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
