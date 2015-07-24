package conventers;

import org.json.JSONException;
import org.json.JSONObject;

import models.CardModel;
import models.ClientModel;
import models.CoordinateModel;
import models.NotificationModel;
import models.TicketModel;
import utils.BaseHelper;

/**
 * Created by Slawek on 2015-07-13.
 */
public class ModelConverter {

    public CardModel ConvertCard(JSONObject jsonObj)  {
        CardModel model = new CardModel();

        try {
            model.setCardId(Integer.parseInt(jsonObj.get("CardId").toString()));
            model.setFirstName(jsonObj.getJSONObject("Worker").getString("FirstName").toString());
            model.setLastName(jsonObj.getJSONObject("Worker").getString("LastName").toString());
        } catch (JSONException e) {
            return null;
        }

        return model;
    }

    public NotificationModel ConvertNotification(JSONObject jsonObj)  {
        NotificationModel model = new NotificationModel();

        try {
            model.setId(Integer.parseInt(jsonObj.get("Id").toString()));
            model.setTitle(jsonObj.get("Title").toString());
            model.setDescription(jsonObj.get("Description").toString());
            model.setTicketId(Integer.parseInt(jsonObj.get("TicketId").toString()));
            model.setType(jsonObj.get("Type").toString());
        } catch (JSONException e) {
            return null;
        }

        return model;
    }

    public CoordinateModel ConvertCoordinate(JSONObject jsonObj) {
        CoordinateModel model = new CoordinateModel();

        try {
            model.setLatitude(Double.parseDouble(jsonObj.get("Latitude").toString()));
            model.setLongitude(Double.parseDouble(jsonObj.get("Longitude").toString()));
            if(jsonObj.has("Name"))
                model.setNameCity(jsonObj.get("Name").toString());
        } catch (JSONException e) {
            return null;
        }

        return model;
    }

    public ClientModel ConvertClient(JSONObject jsonObj) {
        ClientModel model =new ClientModel();

        try {
            model.setFirstName(jsonObj.get("FirstName").toString());
            model.setLastName(jsonObj.get("LastName").toString());
            model.setTeamId(Integer.parseInt(jsonObj.get("TeamId").toString()));
            model.setId(Integer.parseInt(jsonObj.get("Id").toString()));
            model.setTeam(jsonObj.get("Team").toString());
            model.setCity(jsonObj.get("City").toString());
            model.setStreet(jsonObj.get("Street").toString());
            model.setHomeNumber(jsonObj.get("HomeNo").toString());
            model.setFlatNumber(jsonObj.get("FlatNo").toString());
            model.setCoordinate(ConvertCoordinate(jsonObj));
        } catch (JSONException e) {
            return null;
        }

        return model;
    }

    public TicketModel ConvertTicket(JSONObject jsonObj) {
        TicketModel model = new TicketModel();

        try {
            model.setId(Integer.parseInt(jsonObj.get("Id").toString()));
            model.setTitle(jsonObj.get("Title").toString());
            if(jsonObj.has("Description"))
                model.setDescription(jsonObj.get("Description").toString());
            if(jsonObj.has("Status"))
                model.setStatus(jsonObj.get("Status").toString());
            if(jsonObj.has("ExecutorId"))
            {
                Integer value = BaseHelper.TryParseInt(jsonObj.get("ExecutorId").toString());
                model.setExecutorId(value);
            }
            if(jsonObj.has("CustomerId"))
            {
                Integer value = BaseHelper.TryParseInt(jsonObj.get("CustomerId").toString());
                model.setCustomerId(value);
            }
        } catch (JSONException e) {
            return null;
        }

        return model;
    }
}
