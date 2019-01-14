package com.oneorange.utils;

import com.oneorange.base.BaseApplication;
import com.oneorange.entity.FormData;
import com.oneorange.volley.AuthFailureError;
import com.oneorange.volley.Request;
import com.oneorange.volley.RequestQueue;
import com.oneorange.volley.Response;
import com.oneorange.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/14.
 * <p/>
 * 网络控制类  配合volley框架使用
 */
public class NetworkController {

    private RequestQueue mRequestQueue = null;
    private String mRequestURLString = null;
    private HashMap<String, String> mHeadersMap = null;
    private JSONObject mReqJson = null;

    private List<FormData> formDatas = null;

    public NetworkController() {
        mRequestQueue = BaseApplication.getInstance().getRequestQueue();
    }


    public void stop() {
        mRequestQueue.stop();
    }

    /**
     * 设置请求头
     *
     * @param requestURLString 请求url
     * @param headersMap       请求url的头
     */
    public void setNetRequestHeader(String requestURLString, HashMap<String, String> headersMap) {
        this.mRequestURLString = requestURLString;
        this.mHeadersMap = headersMap;
        if (this.mHeadersMap == null) {
            this.mHeadersMap = new HashMap<String, String>() {
            };
        } else {
            LogUtil.d("url", mHeadersMap.toString());
        }

    }

    public byte[] getBody() throws AuthFailureError {
        if (formDatas == null||formDatas.size() == 0){
            return null ;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        FormData formData;
        for (int i = 0; i < formDatas.size(); i++) {
            formData=formDatas.get(i);
            StringBuffer sb= new StringBuffer() ;
            sb.append(formData.getName());
            sb.append("=");
            sb.append(formData.getValue());
            sb.append("\r\n") ;
            try {
                bos.write(sb.toString().getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    /**
     * 设置 post 数据
     *
     * @param mReqJson
     */
    public void setRequestPostParam(JSONObject mReqJson) {
        try {
            LogUtil.d("JSONObject", mReqJson.toString());
        } catch (NullPointerException e) {
            LogUtil.d("JSONObject", "jsonobjec->error");
        }
        this.mReqJson = mReqJson;
    }

    /**
     * post请求
     *
     * @param onSuccessListener
     * @param onErrorListener
     */
    public void startNetRequestPost(Response.Listener<JSONObject> onSuccessListener, Response.ErrorListener onErrorListener) {
        JsonObjectRequest mRequest = new JsonObjectRequest(Request.Method.POST, mRequestURLString, mReqJson, onSuccessListener, onErrorListener) {
            @Override
            public Map<String, String> getHeaders() {
                return mHeadersMap;
            }
        };
        mRequestQueue.add(mRequest);
    }

    /**
     * get请求
     *
     * @param onSuccessListener
     * @param onErrorListener
     */
    public void startNetRequestGet(Response.Listener<JSONObject> onSuccessListener, Response.ErrorListener onErrorListener) {
        JsonObjectRequest mRequest = new JsonObjectRequest(mRequestURLString, onSuccessListener, onErrorListener) {
            @Override
            public Map<String, String> getHeaders() {
                return mHeadersMap;
            }
        };
        mRequestQueue.add(mRequest);
    }


}
