package entities;

import java.io.Serializable;

import ocsf.server.ConnectionToClient;

/**
 * The ConnectedClient class represents a client connected to the server.
 * It holds information about the client's connection, such as host, IP address, and status.
 */
public class ConnectedClient implements Serializable {
	private ConnectionToClient client;
	private String host;
	private String ip;
	private String status;

	/**
     * Constructs a ConnectedClient object with the given client connection, host, and IP address.
     *
     * @param client The connection to the client.
     * @param host   The host name of the client.
     * @param ip     The IP address of the client.
     */
	public ConnectedClient(ConnectionToClient client, String host, String ip) {
		this.client = client;
		this.host = host;
		this.ip = ip;
		this.status="connected";
	}

	public ConnectionToClient getClient() {
		return client;
	}

	public String getHost() {
		return host;
	}

	public String getIP() {
		return ip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}