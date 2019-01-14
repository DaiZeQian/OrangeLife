/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oneorange.volley.toolbox;


import com.oneorange.volley.AuthFailureError;
import com.oneorange.volley.NetworkResponse;
import com.oneorange.volley.ParseError;
import com.oneorange.volley.Request;
import com.oneorange.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * POST����
 * 
 * @author yuanc
 */
public abstract class JsonObjectPostRequest extends Request<JSONObject> {
	private Response.Listener<JSONObject> mListener;

	public JsonObjectPostRequest(String url, Response.Listener<JSONObject> listener, Response.ErrorListener erroListener) {
		super(Method.POST, url, erroListener);
		this.mListener = listener;
	}

	/** Default charset for JSON request. */
	protected static final String PROTOCOL_CHARSET = "utf-8";

	/**POST����������*/
	@Override
	protected abstract Map<String, String> getParams() throws AuthFailureError;

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
		catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		if (mListener != null) {
			mListener.onResponse(response);
		}
	}
}
