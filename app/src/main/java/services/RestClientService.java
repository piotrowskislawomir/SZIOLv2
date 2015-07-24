package services;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import models.ConfigurationDictioniary;
import models.ConfigurationModel;
import services.databases.DatabaseLogic;

/**
 * Created by Slawek on 2015-03-21.
 */

public class RestClientService {
    HttpClient client = new DefaultHttpClient();

    private String serviceUrl;
    private static String token = "";
    private HttpResponse response;
    DatabaseLogic dbLogic;

    public RestClientService(String serviceUrl, Context context)
    {
        this.serviceUrl = serviceUrl;
        dbLogic = new DatabaseLogic(context);
        SetToken();
    }

    private void SetToken()
    {
        ConfigurationModel configToken = dbLogic.GetConfiguration(ConfigurationDictioniary.USER_TOKEN);
        if(configToken != null)
        {
            token = "?token=" + configToken.getValue();
        }
        else
        {
            token = "";
        }
    }

    public int SendPut(String resource, int id, String jsonData)
    {
        return SendPut(resource + "/" + id, jsonData);
    }

    public int SendPut(String resource, String jsonData)
    {
        try {
            HttpPut req = new HttpPut(serviceUrl + resource + token);
            req.setHeader("Content-type", "application/json");
            req.setEntity(new ByteArrayEntity(jsonData.getBytes("UTF8")));
            response = client.execute(req);

            return response.getStatusLine().getStatusCode();
        }
        catch (UnsupportedEncodingException ignored)
        {}
        catch (ClientProtocolException ignored)
        {}
        catch (IOException ignored)
        {}

        return -1;
    }

    public int SendDelete(String resource, int id) {

        return SendDelete(resource + "/" + id);
    }

    public int SendDelete(String resource)
    {
        try {
            HttpDelete req = new HttpDelete(serviceUrl + resource + token);
            req.setHeader("Content-type", "application/json");
            response = client.execute(req);

            return response.getStatusLine().getStatusCode();
        }
        catch (UnsupportedEncodingException ignored)
        {}
        catch (ClientProtocolException ignored)
        {}
        catch (IOException ignored)
        {}

        return -1;
    }

    public int SendGet(String resource, int id)
    {
        return SendGet(resource+ "/" + id);
    }

    public int SendGet(String resource)
    {
        try {
            HttpGet req = new HttpGet(serviceUrl + resource + token);
            req.setHeader("Content-type", "application/json");
            response = client.execute(req);

            return response.getStatusLine().getStatusCode();
        }
        catch (UnsupportedEncodingException ignored)
        {}
        catch (ClientProtocolException ignored)
        {}
        catch (IOException ignored)
        {}

        return -1;
    }

    public int SendPost(String resource,String jsonData)
    {
        try {
            HttpPost request = new HttpPost(serviceUrl + resource + token);
            request.setHeader("Content-type", "application/json");
            request.setEntity(new ByteArrayEntity(jsonData.getBytes("UTF8")));
            response = client.execute(request);

            return response.getStatusLine().getStatusCode();
        }
        catch (UnsupportedEncodingException ignored)
        {}
        catch (ClientProtocolException ignored)
        {}
        catch (IOException ignored)
        {}

        return -1;
    }

    public String GetContent()
    {
        try
        {
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null)
            {
                str.append(line + "\n");
            }
            in.close();
            return str.toString();
        }
        catch(Exception ex)
        {}

        return "";
    }
}

