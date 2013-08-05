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

package es.ants.felixgm.trmsim_wsn.trm.powertrust;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.search.ISearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsClientSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsPowerNodeSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import java.util.Collection;
import java.util.List;

/**
 * <p>This class models a network composed by sensors implementing PowerTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.3
 */
public class PowerTrust_Network extends Network {
    /**
     * This constructor creates a new random PowerTrust Network using the given parameters
     * @param numSensors Number of sensors composing the network
     * @param probClients Probability of a sensor to act as a client requesting services
     * @param rangeFactor Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param probServices A collection of probabilities of offering a certain service, one per service
     * @param probGoodness A collection of goodnesses about offering a certain service, one per service
     * @param services All the services offered by the generated Network
     */
    public PowerTrust_Network(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        super(numSensors, probClients, rangeFactor, probServices, probGoodness, services);
        reset();
    }

    /**
     * This method loads a network from a XML file and creates the specific
     * corresponding PowerTrust Network
     * @param xmlFilePath Path of the XML to load the network from
     * @throws java.lang.Exception If the XML file given does not have the appropriate structure, or if
     * a sensor links to an undefined sensor, or if a sensor links to itself
     */
    public PowerTrust_Network(String xmlFilePath) throws Exception {
        super(xmlFilePath);
        reset();
    }

    @Override
    public void reset() {
        PowerTrust_Sensor.setNumSensors(sensors.size());
        super.reset();
        int m = (int)(sensors.size()*((PowerTrust_Parameters)PowerTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_powerNodesPercentage());
        if ((((PowerTrust_Parameters)PowerTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_powerNodesPercentage() > 0) && (m == 0))
            m = 1;

        while (m > 0) {
            int selectedServer = (int)(Math.random()*servers.size());
            if (!((PowerTrust_Sensor)(((List<Sensor>)servers).get(selectedServer))).isPowerNode()) {
                ((PowerTrust_Sensor)(((List<Sensor>)servers).get(selectedServer))).setPowerNode(true);
                m--;
            }
        }
    }

    @Override
    public void oscillate(Service service) {
        try {
            int numBenevolentServers = 0;
            for (Sensor server : servers)
                if ((server.offersService(service)) && (server.get_goodness(service) >= 0.5) &&
                    !(((PowerTrust_Sensor)server).isPowerNode())) {
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
        return new PowerTrust_Sensor();
    }

    @Override
    public Sensor newSensor(int id, double x, double y) {
        return new PowerTrust_Sensor(id,x,y);
    }

    @Override
    public long get_sensorsTransmittedDistance(ISearchCondition searchCondition, Service requiredService) {
        long sensorsTransmittedDistance = 0;
        long numSensors = 0;

        for (Sensor sensor : sensors)
            if (reachesQualifiedService(sensor,requiredService)) {
                if (searchCondition instanceof IsClientSearchCondition) {
                    if (searchCondition.sensorAcomplishesCondition(sensor)) {
                        sensorsTransmittedDistance += sensor.get_transmittedDistance();
                        numSensors++;
                    }
                } else if (searchCondition instanceof IsPowerNodeSearchCondition) {
                    if (((PowerTrust_Sensor)sensor).isPowerNode()) {
                        sensorsTransmittedDistance += sensor.get_transmittedDistance();
                        numSensors++;
                    }
                } else {
                    if (searchCondition.sensorAcomplishesCondition(sensor)) {
                        if (((IsServerSearchCondition)searchCondition).get_serverType() == IsServerSearchCondition.BENEVOLENT_SERVER) {
                            if (!((PowerTrust_Sensor)sensor).isPowerNode()) {
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
}