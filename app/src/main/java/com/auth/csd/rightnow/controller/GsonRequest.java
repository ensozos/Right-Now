package com.auth.csd.rightnow.controller;




import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.*;


public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    // Used for request which do not return anything from the server
    private boolean muteRequest = false;
    private GsonRequest(int method, Class<T> classtype, String url, String requestBody,
                        Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, requestBody, listener,
                errorListener);
        clazz = classtype;
        this.headers = headers;
        configureRequest();
        setTimeout();
    }

    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, headers);
        setTimeout();
    }

    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, new HashMap<String, String>());
        setTimeout();
    }

    public GsonRequest(int method, String url, Class<T> classtype, String requestBody,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, classtype, url, requestBody, listener,
                errorListener, new HashMap<String, String>());
        setTimeout();
    }

    public GsonRequest(String url, Class<T> classtype, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Request.Method.GET, url, classtype, "", listener, errorListener);
        setTimeout();
    }


    public GsonRequest(String url, Class<T> classtype, Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        this(Request.Method.GET, classtype, url, "", listener, errorListener, headers);
        setTimeout();
    }


    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers, boolean mute) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, headers);
        this.muteRequest = mute;
        setTimeout();
    }


    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, boolean mute) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, new HashMap<String, String>());
        this.muteRequest = mute;
        setTimeout();
    }


    public GsonRequest(int method, String url, Class<T> classtype, String requestBody,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, boolean mute) {
        this(method, classtype, url, requestBody, listener,
                errorListener, new HashMap<String, String>());
        this.muteRequest = mute;
        setTimeout();
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        // The magic of the mute request happens here
        if (muteRequest) {
            if (response.statusCode >= 200 && response.statusCode <= 299) {
                // If the status is correct, we return a success but with a null object, because the server didn't return anything
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
        } else {
            try {
                // If it's not muted; we just need to create our POJO from the returned JSON and handle correctly the errors
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                T parsedObject = gson.fromJson(json, clazz);
                return Response.success(parsedObject, HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
        return null;
    }

    private void setTimeout() {
        setRetryPolicy(new DefaultRetryPolicy(
                30 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    private void configureRequest() {

    }
}