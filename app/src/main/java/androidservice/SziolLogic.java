package androidservice;

import models.ClientModel;
import models.TicketModel;

/**
 * Created by Slawek on 2015-07-22.
 */
public class SziolLogic {

    private static String GetStatus(String shortStatus)
    {

        if(shortStatus.equalsIgnoreCase("CR"))
        {
            return "Utworzone";
        }
        if(shortStatus.equalsIgnoreCase("AS"))
        {
            return "Przypisane";
        }
        if(shortStatus.equalsIgnoreCase("EX"))
        {
            return "Realizowane";
        }
        if(shortStatus.equalsIgnoreCase("CL"))
        {
            return  "Wykonane";
        }

        return "";
    }

    public static String GetTextFromTicket(TicketModel ticket)
    {
        String text = ticket.getTitle() + "\n";
        text += ticket.getDescription() + "\n";
        text += "Status: " +  GetStatus(ticket.getStatus()) + "\n";

        return text;
    }

    public static String GetTextFromClient(ClientModel client)
    {
        String text = "Klient\n";
        text += client.getFirstName() + " " + client.getLastName() + "\n";
        text += client.getCity() + "\n";
        text += client.getStreet() + " ";
        if(client.getHomeNumber() != null && !client.getHomeNumber().isEmpty())
        {
            text += client.getHomeNumber();

            if(client.getFlatNumber() != null && !client.getFlatNumber().isEmpty())
            {
                text += " / " + client.getFlatNumber();
            }
        }

        text += "\n";
        return text;
    }
}
