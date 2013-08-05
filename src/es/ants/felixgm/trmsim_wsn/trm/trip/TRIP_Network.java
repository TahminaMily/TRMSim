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

package es.ants.felixgm.trmsim_wsn.trm.trip;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * <p>This class models a network composed by sensors implementing TRIP</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.5
 */
public class TRIP_Network extends Network {
    /**
     * This constructor creates a new random TRIP Network using the given parameters
     * @param numSensors Network number sensors
     * @param probClients The network will have a number of clients depending of this parameter.
     * @param rangeFactor Range Factor.
     * @param probServices Probability of servers offers services.
     * @param probGoodness Probability of servers being good.
     * @param services Services that servers offers to clients.
     */
    public TRIP_Network(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        super(numSensors, probClients, rangeFactor, probServices, probGoodness, services);
        servers.removeAll(servers);
        clients.removeAll(clients);
        for (Sensor sensor : sensors) {
            servers.add(sensor);
            clients.add(sensor);
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
        reset();
        setNewNeighbors(rangeFactor);
    }

    /**
     * This method loads a network from a XML file and creates the specific
     * corresponding TRIP Network
     * @param xmlFilePath Path of the XML to load the network from
     * @throws java.lang.Exception If the XML file given does not have the appropriate structure, or if
     * a sensor links to an undefined sensor, or if a sensor links to itself
     */
    public TRIP_Network(String xmlFilePath) throws Exception {
        super(xmlFilePath);
        for (Sensor client : clients) {
            client.addService(new Service("Relay"), 1.0);
            servers.add(client);
        }
        for (Sensor server : servers)
            if (!clients.contains(server))
                clients.add(server);
        reset();
    }

    @Override
    public void reset() {
        super.reset();
        for (Sensor sensor : sensors)
            if (((TRIP_Sensor)sensor).isRSU())
                return;
        
        int numRSUs = (int)(sensors.size()*((TRIP_Parameters)TRIP_Sensor.get_TRModel_WSN().get_TRMParameters()).get_rsuPercentage());
        if ((((TRIP_Parameters)TRIP_Sensor.get_TRModel_WSN().get_TRMParameters()).get_rsuPercentage() > 0) && (numRSUs == 0))
            numRSUs = 1;

        while (numRSUs > 0) {
            int selectedServer = (int)(Math.random()*servers.size());
            if (!((TRIP_Sensor)(((List<Sensor>)servers).get(selectedServer))).isRSU()) {
                ((TRIP_Sensor)(((List<Sensor>)servers).get(selectedServer))).setRSU(true);
                numRSUs--;
            }
        }
    }

    @Override
    public void setNewNeighbors(double newRange) {
        double rangeThreshold = newRange*Math.sqrt(2.0)*maxDistance;
        double rsuRangeThreshold = 2.0*rangeThreshold;

        for (Sensor sensor : sensors) {
            sensor.removeAllNeighbors();
            for (Sensor sensor2 : sensors) {
                if (((TRIP_Sensor)sensor).isRSU() || ((TRIP_Sensor)sensor2).isRSU()) {
                    if ((!sensor.equals(sensor2)) && (sensor.distance(sensor2) < rsuRangeThreshold)) {
                        sensor.addLink(sensor2);
                        sensor2.addLink(sensor);
                    }
                } else {
                    if ((!sensor.equals(sensor2)) && (sensor.distance(sensor2) < rangeThreshold)) {
                        sensor.addLink(sensor2);
                        sensor2.addLink(sensor);
                    }
                }
            }
        }
    }

    @Override
    public Sensor newSensor(){
        return new TRIP_Sensor();
    }

    @Override
    public Sensor newSensor(int id, double x, double y) {
        return new TRIP_Sensor(id,x,y);
    }
}