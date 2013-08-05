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

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.outcomes.PowerTrustEnergyConsumptionOutcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionBinary;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class models PowerTrust
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network,
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="PowerTrustparameters"></a>It needs some parameters to be passed as a
 * {@link PowerTrust_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # PowerTrust parameters file
 *    ####################################
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/PowerTrustparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * <br></br>
 * For more information regarding PowerTrust algorithm, please check the following reference:
 * <ul>
 *   <li>Zhou, R. and Hwang, K., &quot;<strong>PowerTrust: A Robust and Scalable
 *       Reputation System for Trusted Peer-to-Peer Computing</strong>&quot;,
 *       IEEE Transactions on Parallel and Distributed Systems, vol 18, no. 4,
 *       pp. 460-473, 2007
 *   </li>
 * </ul>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.3
 */
public class PowerTrust extends TRModel_WSN {
    /**
     * Class PowerTrust constructor
     * @param powerTrust_parameters Parameters needed for the algorithm, as described <a href="#PowerTrustparameters">before</a>
     */
    public PowerTrust(PowerTrust_Parameters powerTrust_parameters) {
    	super(powerTrust_parameters);
    }

    /**
     * Returns the name of this model, i.e., "PowerTrust"
     * @return The name of this model, i.e., "PowerTrust"
     */
    public static String get_name() { return "PowerTrust"; }

    @Override
    public GatheredInformation gatherInformation(Sensor client, Service service) {
        Collection<Vector<Sensor>> pathsToServers = client.findSensors(new IsServerSearchCondition(service));
        return new GatheredInformation(pathsToServers);
    }

    @Override
    public Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        Vector<Sensor> mostReputablePath = null;
        double maxReputation = Double.NEGATIVE_INFINITY;
        for (Vector<Sensor> pathToServer : gi.getPathsToServers()) {
            PowerTrust_Sensor server = (PowerTrust_Sensor)pathToServer.lastElement();
            if (server.computeGlobalReputation() > maxReputation) {
                maxReputation = server.get_globalReputationScore();
                mostReputablePath = pathToServer;
            }
            client.addTransmittedDistance((long)client.distance(server));
        }
        return mostReputablePath;
    }

    @Override
    public Outcome performTransaction(Vector<Sensor> path, Service service) {
        Outcome outcome = null;
        if ((path == null) || (path.size() == 0) || (!path.lastElement().isActive()))
            return outcome;

        PowerTrust_Sensor client = (PowerTrust_Sensor)path.firstElement();
        PowerTrust_Sensor server = (PowerTrust_Sensor)path.lastElement();
        Service receivedService = server.serve(service,path);
        outcome = new PowerTrustEnergyConsumptionOutcome(new SatisfactionBinary(receivedService != null),path.size());

        client.addNewTransaction(client, server, outcome);
        server.addNewTransaction(client, server, outcome);

        return outcome;
    }

    /**
     * PowerTrust does not implement this method, since it does not apply any specific reward step
     * @param path
     * @param outcome
     */
    @Override
    public Outcome reward(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }

    /**
     * PowerTrust does not implement this method, since it does not apply any specific punish step
     * @param path
     * @param outcome
     */
    @Override
    public Outcome punish(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }

    @Override
    public Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services)  {
        return new PowerTrust_Network(numSensors,probClients,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String fileName) throws Exception {
        return new PowerTrust_Network(fileName);
    }
}