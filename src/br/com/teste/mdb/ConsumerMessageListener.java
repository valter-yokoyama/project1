package br.com.teste.mdb;

import javax.ejb.MessageDriven;

import java.util.Date;

import javax.ejb.ActivationConfigProperty;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

@MessageDriven(activationConfig =
        {
        @ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
        @ActivationConfigProperty(propertyName="destination", propertyValue="myqueue")
        })
public class ConsumerMessageListener implements MessageListener
{
   public void onMessage(Message recvMsg)
   {
	   TextMessage msg = null;

	    try {
	        if (recvMsg instanceof TextMessage) {
	            msg = (TextMessage) recvMsg;
	            
	            MongoClient mongo = new MongoClient( "localhost" , 27017 );	    
	            DB db = mongo.getDB("local");
	            DBCollection table = db.getCollection("manifestation");
	            BasicDBObject document = new BasicDBObject();
	            String aux [] = msg.getText().split("@@");
	            document.put("clientId", aux[0]);
	            document.put("text", aux[1]);
	            document.put("email", aux[2]);
	            //document.put("createdDate", new Date());
	            
	            table.insert(document);
	           
	            BasicDBObject searchQuery = new BasicDBObject();
	            searchQuery.put("clientId", aux[0]);
	            searchQuery.put("text", aux[1]);

	            DBCursor cursor = table.find(searchQuery);

	            while (cursor.hasNext()) {
	            	System.out.println(cursor.next());
	            }
	            
	            System.out.println("MESSAGE BEAN: Message received: " +
	                msg.getText());
	        } else {
	        	System.out.println("Message of wrong type: " +
	                recvMsg.getClass().getName());
	        }
 
	    } catch (JMSException e) {
	        e.printStackTrace();
	    } catch (Throwable te) {
	        te.printStackTrace();
	    }   
	}
}

