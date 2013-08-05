/**
 *  "TRMSim-WSN, Trust and Reputation Models Simulator for Wireless 
 * Sensor Networks" is free software: you can redistribute it and/or 
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version always keeping 
 * the additional terms specified in this license.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * 
 * Additional Terms of this License
 * --------------------------------
 * 
 * 1. It is Required the preservation of specified reasonable legal notices
 *   and author attributions in that material and in the Appropriate Legal
 *   Notices displayed by works containing it.
 * 
 * 2. It is limited the use for publicity purposes of names of licensors or
 *   authors of the material.
 * 
 * 3. It is Required indemnification of licensors and authors of that material
 *   by anyone who conveys the material (or modified versions of it) with
 *   contractual assumptions of liability to the recipient, for any liability
 *   that these contractual assumptions directly impose on those licensors
 *   and authors.
 * 
 * 4. It is Prohibited misrepresentation of the origin of that material, and it is
 *   required that modified versions of such material be marked in reasonable
 *   ways as different from the original version.
 * 
 * 5. It is Declined to grant rights under trademark law for use of some trade
 *   names, trademarks, or service marks.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (lgpl.txt).  If not, see <http://www.gnu.org/licenses/>
*/

package es.ants.felixgm.trmsim_wsn.network;

import es.ants.felixgm.trmsim_wsn.search.ISearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsClientSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/**
 * <p>This class models a P2P, Ad-hoc or Wireless Sensor Network, or even a
 * multi-agent system.</p>
 * <p>It is composed of several clients requesting services and servers offering services</p>
 * <p><a name="xmlFileStructure"></a>The constructor
 * {@link #Network Network}
 * requires a XML file following the next structure:</p>
 * <pre>
 *    &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *    &lt;wsn&gt;
 *        &lt;client id="1" x="30.5" y="56.2"&gt;
 *            &lt;server id="2"/&gt;
 *            &lt;server id="3"/&gt;
 *        &lt;/client&gt;
 *        &lt;client id="7" x="10.3" y="26.4"&gt;
 *            &lt;server id="4"/&gt;
 *            &lt;server id="6"/&gt;
 *        &lt;/client&gt;
 *        &lt;server id="2" x="14.8" y="63.7"&gt;
 *            &lt;service id="Relay" goodness="1.0"/&gt;
 *            &lt;server id="4"/&gt;
 *            &lt;server id="5"/&gt;
 *            &lt;client id="1"/&gt;
 *        &lt;/server&gt;
 *        &lt;server id="3" x="29.7" y="73.7"&gt;
 *            &lt;service id="Relay" goodness="1.0"/&gt;
 *            &lt;service id="Service 1" goodness="0.05"/&gt;
 *            &lt;server id="4"/&gt;
 *        &lt;/server&gt;
 *        &lt;server id="4" x="38.4" y="56.2"&gt;
 *            &lt;service id="Relay" goodness="1.0"/&gt;
 *            &lt;server id="6"/&gt;
 *        &lt;/server&gt;
 *        &lt;server id="5" x="95.6" y="36.8"&gt;
 *            &lt;service id="Relay" goodness="1.0"/&gt;
 *            &lt;service id="Service 1" goodness="0.12"/&gt;
 *            &lt;server id="6"/&gt;
 *            &lt;client id="7"/&gt;
 *        &lt;/server&gt;
 *        &lt;server id="6" x="53.5" y="97.4"&gt;
 *            &lt;service id="Relay" goodness="1.0"/&gt;
 *            &lt;service id="Service 1" goodness="0.98"/&gt;
 *            &lt;server id="3"/&gt;
 *        &lt;/server&gt;
 *    &lt;/tacs&gt;
 * </pre> 
 * <p><a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/wsn1.xml" target=_blank">
 * This file</a> represents the following network:</p>
 * <img src="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/img/wsn1.gif">
 <font color="#FF0000">
 <p><strong>A subclass of this class, modeling the specific network, has to be
 implemented in order to add a new Trust and Reputation Model</strong></p>
 </font>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.1
 */
public abstract class Network {
    /** Maximum distance between two nodes in the network */
    protected static final double maxDistance = 100.0;
    /** The clients requesting services */
    protected Collection<Sensor> clients;
    /** The servers offering services */
    protected Collection<Sensor> servers;
    /** All the sensors composing the network */
    protected Collection<Sensor> sensors;
    /** All the services offered by the network */
    protected Collection<Service> services;
    
    /**
     * This constructor creates a new Network from a given set of clients,
     * servers and services
     * @param clients The clients requesting services
     * @param servers The servers offering services
     * @param services All the services offered by the network
     */
    public Network(Collection<Sensor> clients, Collection<Sensor> servers, Collection<Service> services) {
        this.clients = clients;
        this.servers = servers;

        sensors = new ArrayList<Sensor>();
        if (clients != null)
            for (Sensor client : clients)
                sensors.add(client);
        if (servers != null)
            for (Sensor server : servers)
                sensors.add(server);

        this.services = services;
    }

    /**
     * This constructor creates a new random Network using the given parameters
     * @param numSensors Number of sensors composing the network
     * @param probClients Probability of a sensor to act as a client requesting services
     * @param rangeFactor Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param probServices A collection of probabilities of offering a certain service, one per service
     * @param probGoodness A collection of goodnesses about offering a certain service, one per service
     * @param services All the services offered by the generated Network
     */
    public Network(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        clients = new ArrayList<Sensor>();
        servers = new ArrayList<Sensor>();
        sensors = new ArrayList<Sensor>();
        this.services = new ArrayList<Service>();

        Sensor.resetId();
        Sensor.setMaxDistance(maxDistance);

        for (int i = 0; i < numSensors; i++) {
            if (Math.random() <= probClients) {
                Sensor client = newSensor();
                clients.add(client);
                sensors.add(client);
            } else {
                Sensor server = newSensor();
                servers.add(server);
                sensors.add(server);

                Iterator<Double> itProbServices = probServices.iterator();
                Iterator<Double> itProbGoodness = probGoodness.iterator();
                for (Service service : services)
                    if (Math.random() <= itProbServices.next().doubleValue()) {
                        if (Math.random() <= itProbGoodness.next().doubleValue())
                            server.addService(service, 1.0);
                        else
                            server.addService(service, 0.0);

                        if (!this.services.contains(service))
                            this.services.add(service);
                    }
            }
        }

        if (clients.size() == 0) {
            Sensor client = newSensor();
            clients.add(client);
            sensors.add(client);
        }

        // We are going to link the sensors in the network.
        double rangeThreshold = rangeFactor*Math.sqrt(2.0)*maxDistance;
        for (Sensor server : servers) {
            for (Sensor client : clients)
                if (server.distance(client) < rangeThreshold) {
                    client.addLink(server);
                    server.addLink(client);
                }

            for (Sensor server2 : servers)
                if ((!server.equals(server2)) && (server.distance(server2) < rangeThreshold)) {
                    server.addLink(server2);
                    server2.addLink(server);
                }
        }

        for (Sensor client1 : clients) {
            for (Sensor client2 : clients)
                if ((!client1.equals(client2)) && (client1.distance(client2) < rangeThreshold)) {
                    client1.addLink(client2);
                    client2.addLink(client1);
                }

            for (Sensor server : servers)
                if (server.distance(client1) < rangeThreshold) {
                    client1.addLink(server);
                    server.addLink(client1);
                }
        }
    }

    /**
     * <p>This method reads a Network from a XML file and builds its corresponding object</p>
     * <p>The XML file given should have <a href="#xmlFileStructure">this structure</a></p>
     * @param xmlFilePath Path of the XML file describing the Network to create
     * @throws java.lang.Exception If the XML file given does not have the structure shown before, or if
     * a node links to an undefined node, or if a node links to itself
     */
    public Network(String xmlFilePath) throws Exception {
        if (!xmlFilePath.endsWith(".xml"))
            throw new Exception("Only XML files are accepted");
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(xmlFilePath));
        doc.getDocumentElement().normalize();
        org.w3c.dom.Node root = doc.getDocumentElement();
        NodeList nodes = root.getChildNodes();

        clients = new ArrayList<Sensor>();
        servers = new ArrayList<Sensor>();
        sensors = new ArrayList<Sensor>();
        services = new ArrayList<Service>();
        Sensor.resetId();

        /*
         * In the first parsing we create every client and server
         * In the second one, all the links are eestablished
         */
        for (int i = 1; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                NamedNodeMap attributes = node.getAttributes();
                int id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                double x = Double.parseDouble(attributes.getNamedItem("x").getNodeValue());
                double y = Double.parseDouble(attributes.getNamedItem("y").getNodeValue());
                if (node.getNodeName().equals("client")) {
                    Sensor client = newSensor(id, x, y);
                    if (!clients.contains(client)) {
                        clients.add(client);
                        sensors.add(client);
                    }
                } else if ((node.getNodeName().equals("server")) && (attributes != null)) {
                    Sensor server = newSensor(id,x,y);

                    if (!servers.contains(server)) {
                        servers.add(server);
                        sensors.add(server);
                    }

                    NodeList nodeChildren = node.getChildNodes();
                    for (int j = 0; j < nodeChildren.getLength(); j++)
                        if (nodeChildren.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            if (nodeChildren.item(j).getNodeName().equals("service")) {
                                double goodness = Double.parseDouble(nodeChildren.item(j).
                                        getAttributes().getNamedItem("goodness").getNodeValue());
                                String idService = nodeChildren.item(j).
                                        getAttributes().getNamedItem("id").getNodeValue();

                                Service service = new Service(idService);
                                if (!services.contains(service))
                                    services.add(service);
                                server.addService(service, goodness);
                            } else if ((!nodeChildren.item(j).getNodeName().equals("server")) &&
                                    (!nodeChildren.item(j).getNodeName().equals("client")))
                                throw new Exception("Unexpected tag found '"+nodeChildren.item(j).getNodeName()+"' while looking for server "+id+" neighbors and services");
                        }
                } else
                    throw new Exception("Unexpected tag found '"+node.getNodeName()+"' while looking for servers");
            }
        }

        for (int i = 1; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                NamedNodeMap attributes = node.getAttributes();
                if ((node.getNodeName().equals("client")) && (attributes != null)) {
                    int id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());
                    Sensor client = null;
                    for (Sensor client_aux : clients)
                        if (client_aux.id() == id)
                            client = client_aux;

                    NodeList clientChildren = node.getChildNodes();
                    for (int j = 0; j < clientChildren.getLength(); j++)
                        if (clientChildren.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE)
                            if (clientChildren.item(j).getNodeName().equals("server")) {
                                int idServer = Integer.parseInt(clientChildren.item(j).
                                        getAttributes().getNamedItem("id").getNodeValue());

                                Sensor server = null;
                                for (Sensor server_aux : servers)
                                    if (server_aux.id() == idServer)
                                        server = server_aux;
                                client.addLink(server);
                            } else if (clientChildren.item(j).getNodeName().equals("client")) {
                                int idClient = Integer.parseInt(clientChildren.item(j).
                                        getAttributes().getNamedItem("id").getNodeValue());
                                if (id == idClient)
                                    throw new Exception("Client "+id+" has a link to itself");
                                Sensor client_aux = null;
                                for (Sensor client_aux2 : clients)
                                    if (client_aux2.id() == idClient)
                                        client_aux = client_aux2;
                                client.addLink(client_aux);
                            } else
                                throw new Exception("Unexpected tag found '"+clientChildren.item(i).getNodeName()+"' while looking for client's neighbors");

                } else if ((node.getNodeName().equals("server")) && (attributes != null)) {
                    int id = Integer.parseInt(attributes.getNamedItem("id").getNodeValue());

                    Sensor server = null;
                    for (Sensor server_aux : servers)
                        if (server_aux.id() == id)
                            server = server_aux;

                    NodeList nodeChildren = node.getChildNodes();
                    for (int j = 0; j < nodeChildren.getLength(); j++)
                        if (nodeChildren.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                            if (nodeChildren.item(j).getNodeName().equals("server")) {
                                int idServer = Integer.parseInt(nodeChildren.item(j).
                                        getAttributes().getNamedItem("id").getNodeValue());
                                Sensor neighbour = null;
                                for (Sensor server_aux : servers)
                                    if (server_aux.id() == idServer)
                                        neighbour = server_aux;
                                if (id == idServer)
                                    throw new Exception("Server "+id+" has a link to itself");

                                server.addLink(neighbour);
                            } else if (nodeChildren.item(j).getNodeName().equals("client")) {
                                int idClient = Integer.parseInt(nodeChildren.item(j).
                                        getAttributes().getNamedItem("id").getNodeValue());
                                if (id == idClient)
                                    throw new Exception("Client "+id+" has a link to itself");
                                Sensor client = null;
                                for (Sensor client_aux : clients)
                                    if (client_aux.id() == idClient)
                                        client = client_aux;
                                server.addLink(client);
                            } else if (!nodeChildren.item(j).getNodeName().equals("service"))
                                throw new Exception("Unexpected tag found '"+nodeChildren.item(j).getNodeName()+"' while looking for server "+id+" neighbors and services");
                        }
                } else
                    throw new Exception("Unexpected tag found '"+node.getNodeName()+"' while looking for servers");
            }
        }
    }

    /**
     * This method turns every benevolent server in the network into malicious and
     * counts the number of swapped servers. Then (when every server is malicious)
     * it randomly selects malicious servers and converts them into benevolent until
     * the number of benevolent servers is equal to the one before calling this method
     * @param service Service over what the oscillation is to be carried out
     */
    public void oscillate(Service service) {
        try {
            int numBenevolentServers = 0;
            for (Sensor server : servers)
                if ((server.offersService(service)) && (server.get_goodness(service) >= 0.5)) {
                    numBenevolentServers++;
                    server.set_goodness(service,0.0);
                }
            
            double prob = ((double)numBenevolentServers/servers.size());
            while (numBenevolentServers > 0)
                for (Sensor server : servers)
                    if ((Math.random() < prob) && (server.offersService(service)) && (server.get_goodness(service)< 0.5)) {
                        server.set_goodness(service,1.0);
                        numBenevolentServers--;
                        if (numBenevolentServers == 0)
                            break;
                    }
        } catch(Exception ex){ ex.printStackTrace(); }
    }
    
    /**
     * This method creates a new sensor. It must be redefined in each subclass
     * according to the requirements of each particular trust and reputation
     * model
     * @return New created sensor
     */
    public abstract Sensor newSensor();

    /**
     * This method creates a new sensor. It must be redefined in each subclass
     * according to the requirements of each particular trust and reputation
     * model
     * @param id Sensor's identifier
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     * @return New created sensor
     */
    public abstract Sensor newSensor(int id, double x, double y);

    /**
     * This method resets this network to its initial state
     */
    public void reset() {
        for (Sensor sensor : sensors)
            sensor.reset();
    }

    /**
     * This method establishes every sensor's new neighborhood according to
     * a new wireless range
     * @param newRange New wireless range determining every sensor's neighbors
     */
    public void setNewNeighbors(double newRange) {
        double rangeThreshold = newRange*Math.sqrt(2.0)*maxDistance;

        for (Sensor sensor : sensors) {
            sensor.removeAllNeighbors();
            for (Sensor sensor2 : sensors)
                if ((!sensor.equals(sensor2)) && (sensor.distance(sensor2) < rangeThreshold))
                {
                    sensor.addLink(sensor2);
                    sensor2.addLink(sensor);
                }
        }
    }
    
    /**
     * This method writes the current Network into a XML file following 
     * <a href="#xmlFileStructure">this structure</a>
     * @param fileName Path of the XML file where to write the current Network
     * @throws Exception If there is any problem when writing to the XML file
     */
    public void writeToXMLFile(String fileName) throws Exception {
        try {
            if (!fileName.endsWith(".xml"))
                fileName += ".xml";
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            
            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<wsn>\n");
            for (Sensor client : clients) {
                out.write("\t<client id=\""+client.id()
                +"\" x=\""+(((int)(client.getX()*100))/100.0)
                +"\" y=\""+(((int)(client.getY()*100))/100.0)
                +"\">\n");
                for (Sensor neighbor : client.getNeighbors())
                    if (neighbor.get_numServices() == 0)
                        out.write("\t\t<client id=\""+neighbor.id()+"\"/>\n");
                    else
                        out.write("\t\t<server id=\""+neighbor.id()+"\"/>\n");
                out.write("\t</client>\n");
            }
            
            for (Sensor server : servers) {
                out.write("\t<server id=\""+server.id()
                +"\" x=\""+(((int)(server.getX()*100))/100.0)
                +"\" y=\""+(((int)(server.getY()*100))/100.0)
                +"\">\n");
                for (Service service : server.get_services())
                    out.write("\t\t<service id=\""+service.id()+"\" "
                        +"goodness=\""+server.get_goodness(service)+"\""
                        +"/>\n");
                for (Sensor neighbor : server.getNeighbors())
                    if (neighbor.get_numServices() == 0)
                        out.write("\t\t<client id=\""+neighbor.id()+"\"/>\n");
                    else
                        out.write("\t\t<server id=\""+neighbor.id()+"\"/>\n");
                out.write("\t</server>\n");
                
            }
            
            out.write("</wsn>\n");
            out.flush();
            out.close();
        } catch (Exception ex) {
            throw new Exception("writeToXMLFile: "+ex);
        }
    }
    
    /**
     * Prints the whole Network, with all the offered services
     * @return A string with the whole Network, with all the offered services
     */
    @Override
    public String toString() {
        String s = "";
        for (Sensor client : clients)
            s += "C "+client+"\n";
        for (Sensor server : servers)
            s += "S "+server+"\n";
        return s;
    }

    /**
     * Average transmitted distance per sensor of this network
     * @param searchCondition Condition to be accomplished by the querying sensors
     * @param requiredService Service requested by the clients
     * @return Average transmitted distance per sensor of this network
     */
    public long get_sensorsTransmittedDistance(ISearchCondition searchCondition, Service requiredService) {
        long sensorsTransmittedDistance = 0;
        long numSensors = 0;

        for (Sensor sensor : sensors)
            if (searchCondition.sensorAcomplishesCondition(sensor) &&
                reachesQualifiedService(sensor,requiredService)) {
                sensorsTransmittedDistance += sensor.get_transmittedDistance();
                numSensors++;
            }

        if (numSensors != 0)
            return sensorsTransmittedDistance/numSensors;

        return 0;
    }

    /**
     * This method checks if a given sensor can reach any client and any benevolent server offering a given service
     * @param sensor Sensor to find out whether it can reach a benevolent server or not
     * @param requiredService Service requested by the clients
     * @return true if the given sensor can reach any client and any benevolent server offering the given service, false otherwise
     */
    protected boolean reachesQualifiedService(Sensor sensor, Service requiredService) {
        boolean reachableClient = false;
        boolean reachableBenevolentServer = false;
        if (sensor.isActive())
            try {
                reachableClient = (sensor.get_numServices() == 0);
                reachableBenevolentServer = ((sensor.get_numServices() > 1) && (sensor.get_goodness(requiredService) > 0.5));

                if (!reachableClient) {
                    Collection<Vector<Sensor>> pathsToClients = sensor.findSensors(new IsClientSearchCondition());
                    reachableClient = ((pathsToClients != null) && (pathsToClients.size() > 0));
                }
                if (!reachableBenevolentServer) {
                    Collection<Vector<Sensor>> pathsToServers = sensor.findSensors(new IsServerSearchCondition(requiredService,IsServerSearchCondition.BENEVOLENT_SERVER));
                    reachableBenevolentServer = ((pathsToServers != null) && (pathsToServers.size() > 0));
                }
            } catch (Exception ex) { ex.printStackTrace(); }

        return (reachableClient && reachableBenevolentServer);
    }

    /**
     * This method returns the sensor with identifier id. If such sensor does not exist, it returns null
     * @param id Identifier of the sensor to be retrieved
     * @return The sensor with identifier id. If such sensor does not exist, it returns null
     */
    public Sensor getSensor(int id) {
        if ((sensors == null) || (sensors.isEmpty()))
            return null;
        
        for (Sensor sensor : sensors) 
            if (sensor.id() == id)
                return sensor;
        
        return null;
    }
    
    /**
     * This method retrieves the set of clients belonging to this network
     * @return The set of clients belonging to this network
     */
    public Collection<Sensor> get_clients() { return clients; }

    /**
     * This method retrieves the set of servers belonging to this network
     * @return The set of servers belonging to this network
     */
    public Collection<Sensor> get_servers() { return servers; }

    /**
     * This method retrieves the set of sensors belonging to this network
     * @return The set of sensors belonging to this network
     */
    public Collection<Sensor> get_sensors() { return sensors; }

    /**
     * This method retrieves the set of services offered by this network
     * @return The set of services offered by this network
     */
    public Collection<Service> get_services() { return services; }

    /**
     * This method retrieves the total number of sensors composing this network
     * @return The total number of sensors composing this network
     */
    public int get_numSensors() { return sensors.size(); }

    /**
     * This method retrieves the number of clients belonging to this network
     * @return The number of clients belonging to this network
     */
    public int get_numClients() { return clients.size(); }

    /**
     * This method retrieves the number of servers belonging to this network
     * @return The number of servers belonging to this network
     */
    public int get_numServers() { return servers.size(); }

    /**
     * This method retrieves the maximum distance between two nodes in this network
     * @return The maximum distance between two nodes in this network
     */
    public static double get_maxDistance() { return maxDistance; }

    /**
     * This method establishes if a collusion is to be formed or not in this network
     * @param collusion Indicates if a collusion is to be formed or not in this network
     */
    public void set_collusion(boolean collusion) { Sensor.setCollusion(collusion); }

    /**
     * This method establishes if the topology of this network is dynamic, because
     * sensors can sleep or not
     * @param dynamic Indicates if the topology of this network is dynamic, because
     * sensors can sleep or not
     */
    public void set_dynamic(boolean dynamic) { Sensor.setDynamic(dynamic); }
}