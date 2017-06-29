package br.com.teste.servlet;
 
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
  
@Path("/message")
public class RestService
{
	@Resource(mappedName = "myqueue")
	private Queue queue;
	 
	@Resource(mappedName = "jms/QueueConnectionFactory")
	private QueueConnectionFactory queueConnectionFactory;
    @GET
    @Path("{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registry(@PathParam( "clientId") String id, @QueryParam("text") String text,@QueryParam("email") String email)
    {
    	QueueConnection queueConnection = null;
        try {
			queueConnection = queueConnectionFactory.createQueueConnection();
	        queueConnection.start();
	        QueueSession queueSession = queueConnection.createQueueSession(false,
	                Session.AUTO_ACKNOWLEDGE);
	        QueueSender sender = queueSession.createSender(queue);

	        TextMessage msg = queueSession.createTextMessage();
	        msg.setText(id + "@@" + text + "@@" + email);
	        msg.setStringProperty("name", "MessageServlet");

	        sender.send(msg);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	JSONObject obj = new JSONObject();
    	try {
			obj.put("status", "waiting");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.status(200).entity(obj.toString()).build();
    }
    @GET
    @Path("/protocols/{clientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProtocols(@PathParam( "clientId") String id)
    {
        MongoClient mongo=null;
		try {
			mongo = new MongoClient( "localhost" , 27017 );
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	    
        DB db = mongo.getDB("local");
        DBCollection table = db.getCollection("manifestation");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("clientId", id);

        DBCursor cursor = table.find(searchQuery);

        List<Object> objs = new ArrayList<Object>();
        while (cursor.hasNext()) {
           	JSONObject obj = new JSONObject();
           	DBObject dbo = cursor.next();
            System.out.println(dbo);
            try {
				obj.put("protocol", dbo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	objs.add(obj);
        }

    	JSONObject obj = new JSONObject();
    	try {
    		obj.put("protocols", objs);
    		if (objs.isEmpty()) {
    			obj.put("status", "waiting");
    		}else {
    			obj.put("status","ok");
    		}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Response.status(200).entity(obj.toString()).build();
    }    
 }