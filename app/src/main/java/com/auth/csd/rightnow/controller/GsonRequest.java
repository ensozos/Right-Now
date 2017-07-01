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

/**
 * Created by Panerass(dimidare@gmail.com) on 9/22/2016.
 */
public class GsonRequest<T> extends JsonRequest<T> {

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    // Used for request which do not return anything from the server
    private boolean muteRequest = false;

    /**
     * Basically, this is the constructor which is called by the others.
     * It allows you to send an object of type A to the server and expect a JSON representing a object of type B.
     * The problem with the #JsonObjectRequest is that you expect a JSON at the end.
     * We can do better than that, we can directly receive our POJO.
     * That's what this class does.
     *
     * @param method:        HTTP Method
     * @param classtype:     Classtype to parse the JSON coming from the server
     * @param url:           url to be called
     * @param requestBody:   The body being sent
     * @param listener:      Listener of the request
     * @param errorListener: Error handler of the request
     * @param headers:       Added headers
     */
    private GsonRequest(int method, Class<T> classtype, String url, String requestBody,
                        Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        super(method, url, requestBody, listener,
                errorListener);
        clazz = classtype;
        this.headers = headers;
        configureRequest();
        setTimeout();
    }

    /**
     * Method to be called if you want to send some objects to your server via body in JSON of the request (with headers and not muted)
     *
     * @param method:        HTTP Method
     * @param url:           URL to be called
     * @param classtype:     Classtype to parse the JSON returned from the server
     * @param toBeSent:      Object which will be transformed in JSON via Gson and sent to the server
     * @param listener:      Listener of the request
     * @param errorListener: Error handler of the request
     * @param headers:       Added headers
     */
    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> headers) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, headers);
        setTimeout();
    }

    /**
     * Method to be called if you want to send some objects to your server via body in JSON of the request (without header and not muted)
     *
     * @param method:        HTTP Method
     * @param url:           URL to be called
     * @param classtype:     Classtype to parse the JSON returned from the server
     * @param toBeSent:      Object which will be transformed in JSON via Gson and sent to the server
     * @param listener:      Listener of the request
     * @param errorListener: Error handler of the request
     */
    public GsonRequest(int method, String url, Class<T> classtype, Object toBeSent,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, classtype, url, new Gson().toJson(toBeSent), listener,
                errorListener, new HashMap<String, String>());
        setTimeout();
    }

    /**
     * Method to be called if you want to send something to the server but not with a JSON, just with a defined String (without header and not muted)
     *
     * @param method:        HTTP Method
     * @param url:           URL to be called
     * @param classtype:     Classtype to parse the JSON returned from the server
     * @param requestBody:   String to be sent to the server
     * @param listener:      Listener of the request
     * @param errorListener: Error handler of the request
     */
    public GsonRequest(int method, String url, Class<T> classtype, String requestBody,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(method, classtype, url, requestBody, listener,
                errorListener, new HashMap<String, String>());
        setTimeout();
    }

    /**
     * Method to be called if you want to GET something from the server and receive the POJO directly after the call (no JSON). (Without header)
     *
     * @param url:           URL to be called
     * @param classtype:     Classtype to parse the JSON returned from the server
     * @param listener:      Listener of the request
     * @param errorListener: Error handler of the request
     */
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

    /**
     * Sets the timeout of the calls.
     */
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