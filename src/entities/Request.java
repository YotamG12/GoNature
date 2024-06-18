package entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import common.RequestType;
/**
 * Represents a request in the system, holding both the type of request and its content.
 */
public class Request implements Serializable {
	private RequestType type;
	private Object request;
	
	public Request() {};
	
	public Object getRequest() {
		return request;
	}
	public RequestType getType() {
		return type;
	}
	public void setRequest(Object request) {
		this.request = request;
	}
	public void setType(RequestType type) {
		this.type = type;
	}
	 @Override
	    public String toString() {
	        return "Request{" +
	                "request=" + request +
	                ", type=" + type +
	                '}';
	    }
	 /**
	     * Serializes this request into a byte array for transmission or storage.
	     * @return A byte array representing this serialized request.
	     * @throws IOException If an I/O error occurs during serialization.
	     */
	public byte[] getBytes () throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(this);
        oos.flush();
        return bos.toByteArray();
		 
	}
	 /**
     * Deserializes a byte array back into a Request object.
     * 
     * @param bytes The byte array to deserialize.
     * @return The deserialized  Request object.
     * @throws IOException If an I/O error occurs during deserialization.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
	public static Request fromBytesToObject (byte[] bytes) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (Request) ois.readObject();
	}
	
}
