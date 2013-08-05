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

package es.ants.felixgm.trmsim_wsn.trm.eigentrust;

import es.ants.felixgm.trmsim_wsn.network.*;
import es.ants.felixgm.trmsim_wsn.search.ISearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsPreTrustedPeerSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * <p>This class models a network composed by sensors implementing EigenTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.4
 * @since 0.2
 */
public class EigenTrust_Network extends Network {
    /**
     * This constructor creates a new random EigenTrust Network using the given parameters
     * @param numSensors Number of sensors composing the network
     * @param probClients Probability of a sensor to act as a client requesting services
     * @param rangeFactor Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param probServices A collection of probabilities of offering a certain service, one per service
     * @param probGoodness A collection of goodnesses about offering a certain service, one per service
     * @param services All the services offered by the generated Network
     */
    public EigenTrust_Network (
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        super(numSensors, probClients, rangeFactor, probServices, probGoodness, services);

        for (Sensor sensor : sensors) {
            servers.add(sensor);
            Iterator<Double> itProbServices = probServices.iterator();
            Iterator<Double> itProbGoodness = probGoodness.iterator();
            for (Service service : services)
                if (Math.random() <= itProbServices.next().doubleValue()) {
                    if (Math.random() <= itProbGoodness.next().doubleValue())
                        sensor.addService(service, 1.0);
                    else
                        sensor.addService(service, 0.0);

                    if (!this.services.contains(service))
                        this.services.add(service);
                }
        }

        double preTrustedPeersPercentage = ((EigenTrust_Parameters)EigenTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_preTrustedPeersPercentage();
        if (preTrustedPeersPercentage > 0.0) {
            double preTrustedPeersVector[] = new double[get_numSensors()];
            int numPreTrustedPeers = 0;
            for (Sensor sensor : sensors)
                try {
                    if ((sensor.get_goodness(new Service("My Service")) > 0.5) &&
                            (Math.random() < preTrustedPeersPercentage)) {
                        ((EigenTrust_Sensor) sensor).setPreTrustedPeer(true);
                        numPreTrustedPeers++;
                    }
                } catch (Exception ex) { /*ex.printStackTrace();*/ }
            if (numPreTrustedPeers == 0)
                for (Sensor sensor : sensors)
                    try {
                        if (sensor.get_goodness(new Service("My Service")) > 0.5) {
                            ((EigenTrust_Sensor) sensor).setPreTrustedPeer(true);
                            numPreTrustedPeers++;
                            break;
                        }
                    } catch (Exception ex) { /*ex.printStackTrace();*/ }
            for (Sensor sensor : sensors)
                if (((EigenTrust_Sensor) sensor).isPreTrustedPeer())
                    preTrustedPeersVector[sensor.id() - 1] = 1.0 / numPreTrustedPeers;
            EigenTrust_Sensor.set_preTrustedPeersVector(preTrustedPeersVector);
        }
        reset();
    }

    /**
     * This method loads a network from a XML file and creates the specific
     * corresponding EigenTrust Network
     * @param xmlFilePath Path of the XML to load the network from
     * @throws java.lang.Exception If the XML file given does not have the appropriate structure, or if
     * a sensor links to an undefined sensor, or if a sensor links to itself
     */
    public EigenTrust_Network(String xmlFilePath) throws Exception {
        super(xmlFilePath);
        for (Sensor client : clients) {
            client.addService(new Service("Relay"), 1.0);
            servers.add(client);
        }
        for (Sensor server : servers)
            if (!clients.contains(server))
                clients.add(server);

        double preTrustedPeersPercentage = ((EigenTrust_Parameters)EigenTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_preTrustedPeersPercentage();
        if (preTrustedPeersPercentage > 0.0)
            try {
                double preTrustedPeersVector[] = new double[get_numSensors()];
                int numPreTrustedPeers = 0;
                for (Sensor sensor : sensors)
                    if((sensor.get_goodness(new Service("My Service")) > 0.5) &&
                        (Math.random() < preTrustedPeersPercentage)) {
                        ((EigenTrust_Sensor)sensor).setPreTrustedPeer(true);
                        numPreTrustedPeers++;
                    }
                if (numPreTrustedPeers == 0)
                    for (Sensor sensor : sensors)
                        if(sensor.get_goodness(new Service("My Service")) > 0.5) {
                            ((EigenTrust_Sensor)sensor).setPreTrustedPeer(true);
                            numPreTrustedPeers++;
                            break;
                        }
                for (Sensor sensor : sensors)
                    if (((EigenTrust_Sensor)sensor).isPreTrustedPeer())
                        preTrustedPeersVector[sensor.id()-1] = 1.0/numPreTrustedPeers;
                EigenTrust_Sensor.set_preTrustedPeersVector(preTrustedPeersVector);
            } catch(Exception ex) {}
        reset();
    }

    @Override
    public void reset() {
        EigenTrust_Sensor.setNumSensors(sensors.size());
        EigenTrust_Sensor.set_windowSize(
                ((EigenTrust_Parameters)EigenTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_windowSize());
        super.reset();
    }

    @Override
    public void writeToXMLFile(String fileName) throws Exception {
        try {
            if (!fileName.endsWith(".xml"))
                fileName += ".xml";
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));

            out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            out.write("<wsn>\n");

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

    @Override
    public long get_sensorsTransmittedDistance(ISearchCondition searchCondition, Service requiredService) {
        long sensorsTransmittedDistance = 0;
        long numSensors = 0;

        for (Sensor sensor : sensors)
            if (reachesQualifiedService(sensor,requiredService)) {
                if (searchCondition instanceof IsPreTrustedPeerSearchCondition) {
                    if (((EigenTrust_Sensor)sensor).isPreTrustedPeer()) {
                        sensorsTransmittedDistance += sensor.get_transmittedDistance();
                        numSensors++;
                    }
                } else {
                    if (searchCondition.sensorAcomplishesCondition(sensor)) {
                        if (((IsServerSearchCondition)searchCondition).get_serverType() == IsServerSearchCondition.BENEVOLENT_SERVER) {
                            if (!((EigenTrust_Sensor)sensor).isPreTrustedPeer()) {
                                sensorsTransmittedDistance += sensor.get_transmittedDistance();
                                numSensors++;
                            }
                        } else {
                            sensorsTransmittedDistance += sensor.get_transmittedDistance();
                            numSensors++;
                        }
                    }
                }
            }

        if (numSensors != 0)
            return sensorsTransmittedDistance/numSensors;

        return 0;
    }

    @Override
    protected boolean reachesQualifiedService(Sensor sensor, Service requiredService) {
        Collection<Vector<Sensor>> pathsToServers = sensor.findSensors(new IsServerSearchCondition(requiredService,IsServerSearchCondition.BENEVOLENT_SERVER));
        return ((pathsToServers != null) && (pathsToServers.size() > 0));
    }

    @Override
    public void oscillate(Service service) {
        try {
            int numBenevolentServers = 0;
            for (Sensor server : servers)
                if ((server.offersService(service)) && (server.get_goodness(service) >= 0.5) &&
                    !(((EigenTrust_Sensor)server).isPreTrustedPeer())) {
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

    @Override
    public Sensor newSensor(){
        return new EigenTrust_Sensor();
    }

    @Override
    public Sensor newSensor(int id, double x, double y) {
        return new EigenTrust_Sensor(id,x,y);
    }
}