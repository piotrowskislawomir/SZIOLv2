package services;

import org.json.JSONException;
import org.json.JSONObject;

import models.ClientModel;
import models.CoordinateModel;
import models.TicketModel;
import models.UserModel;

/**
 * Created by Slawek on 2015-03-21.
 */

public class RestService {
    public RestClientService restClientService;

    public final String Users = "Users";
    public final String Customers = "Customers";
    public final String Tickets = "Tickets";
    public final String Notifications = "Notifications";
    public final String Coordinates = "Coordinates";
    public final String Cards = "Cards";


    public RestService(RestClientService restClientService)
    {
        this.restClientService = restClientService;
    }

    public String GetContent()
    {
        return restClientService.GetContent();
    }

    public int RegisterUser(UserModel usr, String registryKey)
    {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("UserName", usr.getUserName());
            jsonData.put("Password", usr.getPassword());
            jsonData.put("FirstName", usr.getFirstName());
            jsonData.put("LastName", usr.getLastName());
            jsonData.put("ActivationCode", registryKey);
        }
        catch (JSONException jex)
        {}
        return restClientService.SendPost(Users, jsonData.toString());
    }

    public int LoginUser(UserModel user)
    {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("UserName", user.getUserName());
            jsonData.put("Password", user.getPassword());
        }
        catch (JSONException jex)
        {}

        return restClientService.SendPut(Users, jsonData.toString());
    }

    public int GetClientById(int id)
    {
        return restClientService.SendGet(Customers, id);
    }

    public int GetCustomerCard(int idCard)
    {
        return restClientService.SendGet(Cards, idCard);
    }

    public int GetCustomers()
    {
        return restClientService.SendGet(Customers);
    }

    public int GetTeams()
    {
        return restClientService.SendGet(Cards);
    }

    public int AddCustomer(ClientModel client, CoordinateModel cor) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("FirstName", client.getFirstName());
            jsonData.put("LastName", client.getLastName());
            jsonData.put("City", client.getCity());
            jsonData.put("Street", client.getStreet());
            jsonData.put("HomeNo", client.getHomeNumber());
            jsonData.put("FlatNo", client.getFlatNumber());
            jsonData.put("GpsLatitude", cor.getLatitude());
            jsonData.put("GpsLongitude", cor.getLongitude());
        } catch (JSONException jex) {
        }
        return restClientService.SendPost(Customers, jsonData.toString());
    }

    public int EditCustomer(int id, ClientModel client) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("FirstName", client.getFirstName());
            jsonData.put("LastName", client.getLastName());
            jsonData.put("City", client.getCity());
            jsonData.put("Street", client.getStreet());
            jsonData.put("HomeNo", client.getHomeNumber());
            jsonData.put("FlatNo", client.getFlatNumber());
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Customers, id, jsonData.toString());

    }

    public int GetClientPlaces(ClientModel client)
    {
            JSONObject jsonData = new JSONObject();
            try {
                jsonData.put("City", client.getCity());
                jsonData.put("Street", client.getStreet());
                jsonData.put("HomeNo", client.getHomeNumber());
            }
            catch (JSONException jex)
            {}
            return restClientService.SendPut(Coordinates, jsonData.toString());
    }

    public int DeleteCustomer(Integer id)
    {
        return restClientService.SendDelete(Customers, id);
    }

   public int DeleteTicket(int id)
    {
        return restClientService.SendDelete(Tickets, id);
    }

    public int AddTicket(TicketModel order)
    {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("Title", order.getTitle());
            jsonData.put("Description", order.getDescription());
            jsonData.put("Status", order.getStatus());
            jsonData.put("CustomerId", order.getCustomerId());
        }
        catch (JSONException jex)
        {}
        return restClientService.SendPost(Tickets, jsonData.toString());
    }

    public int GetTickets() // co jesli inne gruby zawodowe ???
    {
        return restClientService.SendGet(Tickets);
    }

    public int GetTicket(int id) // co jesli inne gruby zawodowe ???
    {
        return restClientService.SendGet(Tickets, id);
    }

    public int GetNotification()
    {
        return restClientService.SendGet(Notifications);
    }

    public int SendStatusNotification(int notificationId, boolean accepted)
    {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("Accepted", Boolean.toString(accepted));
            }
        catch (JSONException jex)
        {}
        return restClientService.SendPut(Notifications, notificationId, jsonData.toString());
    }


    public int EditTicket(int id, TicketModel order) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("Description", order.getDescription());
            jsonData.put("CustomerId", order.getCustomerId());
            jsonData.put("Status", order.getStatus());
            jsonData.put("Title", order.getTitle());
            jsonData.put("ExecutorId", order.getExecutorId());
            jsonData.put("AssignToTicket", order.getAssignToTicket());
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Tickets, id, jsonData.toString());
    }

    public int PinTicket(int id, TicketModel order) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("CreatorId", order.getCreatorId());
            jsonData.put("Description", order.getDescription());
            jsonData.put("CustomerId", order.getCustomerId());
            jsonData.put("Status", "AS");
            jsonData.put("Title", order.getTitle());
            jsonData.put("ExecutorId", order.getExecutorId());
            jsonData.put("AssignToTicket", true);
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Tickets, id, jsonData.toString());
    }

    public int UnPinTicket(int id, TicketModel order) {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("CreatorId", order.getCreatorId());
            jsonData.put("Description", order.getDescription());
            jsonData.put("CustomerId", order.getCustomerId());
            jsonData.put("Status", "CR");
            jsonData.put("Title", order.getTitle());
            jsonData.put("ExecutorId", "null");
            jsonData.put("AssignToTicket", false);
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Tickets + "/" + id, jsonData.toString());
    }

    public int ExecuteTicket(int id, TicketModel order) {

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("CreatorId", order.getCreatorId());
            jsonData.put("Description", order.getDescription());
            jsonData.put("CustomerId", order.getCustomerId());
            jsonData.put("Status", "EX");
            jsonData.put("Title", order.getTitle());
            jsonData.put("ExecutorId", "null"); // tutaj na stałę
            jsonData.put("AssignToTicket", true);
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Tickets, id, jsonData.toString());
    }

    public int CloseTicket(int id, TicketModel order) {

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("CreatorId", order.getCreatorId());
            jsonData.put("Description", order.getDescription());
            jsonData.put("CustomerId", order.getCustomerId());
            jsonData.put("Status", "CL");
            jsonData.put("Title", order.getTitle());
            jsonData.put("ExecutorId", "null");
            jsonData.put("AssignToTicket", false);
        } catch (JSONException jex) {
        }
        return restClientService.SendPut(Tickets, id, jsonData.toString());
    }

    public int GetNotifications()
    {
        return restClientService.SendGet(Notifications);
    }

    public int DeleteNotification(int id)
    {
        return restClientService.SendDelete(Notifications, id);
    }


    public int GetMyCard()
    {
        return restClientService.SendGet(Cards, 0);
    }

    public int SendCoordinate(CoordinateModel cor)
    {
        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("longitude", cor.getLongitude());
            jsonData.put("latitude", cor.getLatitude());
        }
        catch (JSONException jex)
        {}

        return restClientService.SendPost(Coordinates, jsonData.toString());
    }
}



