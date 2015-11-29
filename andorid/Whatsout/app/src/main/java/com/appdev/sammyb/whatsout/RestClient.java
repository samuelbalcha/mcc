package com.appdev.sammyb.whatsout;

    import android.content.Context;

    import java.io.UnsupportedEncodingException;
    import java.util.HashMap;

    import com.appdev.sammyb.whatsout.models.EventModel;
    import com.loopj.android.http.AsyncHttpResponseHandler;
    import com.loopj.android.http.RequestParams;
    import com.loopj.android.http.AsyncHttpClient;

    import org.json.JSONObject;

    import cz.msebera.android.httpclient.HttpEntity;
    import cz.msebera.android.httpclient.entity.StringEntity;

public class RestClient {

    protected AsyncHttpClient client;
    protected static HashMap<Class<? extends RestClient>, RestClient> instances =
            new HashMap<Class<? extends RestClient>, RestClient>();

    public static final String REST_URL = "http://whats-out-samuelbalcha.c9.io/api/v1"; // Change this, base API URL
    private Context context;

    public RestClient(Context context) {
        this.context = context;
        this.client = new AsyncHttpClient();
    }

    /**
     *  Controller
     *
     */

    public void getEvents(AsyncHttpResponseHandler handler) {

        String apiUrl = this.getApiURL("events");
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }

    public void updateEvent(AsyncHttpResponseHandler handler, String id, JSONObject data){
        String apiUrl = this.getApiURL("events/" + id);

        StringEntity entity;

        try {
            entity = new StringEntity(data.toString());
            client.put(context, apiUrl, entity, "application/json", handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void deleteEvent(AsyncHttpResponseHandler handler, String id){
        String apiUrl = this.getApiURL("events/" + id);
        client.delete(context, apiUrl, null, "application/json", handler);
    }

    public void createEvent(AsyncHttpResponseHandler handler, JSONObject data){
        String apiUrl = this.getApiURL("events");
        StringEntity entity;

        try {
            entity = new StringEntity(data.toString());
            client.post(context, apiUrl, entity, "application/json", handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getEvent(String id, AsyncHttpResponseHandler handler) {

        String apiUrl = this.getApiURL("events/"+ id);
        // Can specify query string params directly or through RequestParams.
        RequestParams params = new RequestParams();
        params.put("format", "json");
        client.get(apiUrl, params, handler);
    }


    public String getApiURL(String part) {
        String apiURL = REST_URL + "/" + part;
        return apiURL;
    }

    public static RestClient getInstance(Class<? extends RestClient> klass, Context context) {
        RestClient instance = instances.get(klass);
        if (instance == null) {
            try {
                instance = (RestClient) klass.getConstructor(Context.class).newInstance(context);
                instances.put(klass, instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}