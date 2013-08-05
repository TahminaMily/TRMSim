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

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class models the information gathered by a Sensor implementing EigenTrust model</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.2
 * @since 0.2
 */
public class GatheredInformationEigenTrust extends GatheredInformation {
    /** Set of paths leading from a certain client to all the reachable clients */
    private Collection<Vector<EigenTrust_Sensor>> pathsToClients;
    /** Matrix C = (c_{ij}) */
    private double[][] normalizedLocalTrustValuesMatrix;
    /** Number of sensors composing the network */
    private int numSensors;

    /**
     * Class GatheredInformationEigenTrust constructor
     * @param pathsToServers Set of paths leading from a certain client to all the reachable servers
     * @param numSensors Number of sensors composing the network
     */
    public GatheredInformationEigenTrust(Collection<Vector<Sensor>> pathsToServers, int numSensors) {
        super(pathsToServers);
        this.numSensors = numSensors;
        pathsToClients = new Vector<Vector<EigenTrust_Sensor>>();
        normalizedLocalTrustValuesMatrix = new double[numSensors][numSensors];
        for (int i = 0; i < normalizedLocalTrustValuesMatrix.length; i ++)
            normalizedLocalTrustValuesMatrix[i][i] = 1.0;
    }

    /**
     * This method sets the normalized trust value c_{ij}
     * @param sensor Sensor j
     */
    public void setNormalizedTrustValue(EigenTrust_Sensor sensor) {
         for (int sensorId = 1; sensorId <= numSensors; sensorId++) 
             if (sensorId != sensor.id()) {
                 Vector<Sensor> pathToServer = getPathToServer(sensorId);
                 if (pathToServer != null) {
                     normalizedLocalTrustValuesMatrix[sensor.id()-1][sensorId-1] =
                         sensor.getNormalizedLocalTrustValue((EigenTrust_Sensor)pathToServer.lastElement());
                     pathToServer.lastElement().addTransmittedDistance((long)sensor.distance(pathToServer.lastElement()));
                 } else
                     normalizedLocalTrustValuesMatrix[sensor.id()-1][sensorId-1] = 0.0;
         }
     }

    /**
     * This method returns the path leading to a given server
     * @param serverId Server identifier
     * @return The path leading to a given server
     */
    public Vector<Sensor> getPathToServer(int serverId) {
        for (Vector<Sensor> pathToServer : pathsToServers) 
            if (pathToServer.lastElement().id() == serverId)
                return pathToServer;

        return null;
    }

    /**
     * This method establishes the set of paths leading from a certain client to all the reachable clients
     * @param pathsToClients Set of paths leading from a certain client to all the reachable clients
     */
    public void setCollectionClients(Collection<Vector<Sensor>> pathsToClients){
        if (this.pathsToClients == null)
            this.pathsToClients = new Vector<Vector<EigenTrust_Sensor>>();

        for (Vector<Sensor> pathToClient : pathsToClients) {
            Vector<EigenTrust_Sensor> pathToClientAux = new Vector<EigenTrust_Sensor>();
            for (Sensor sensor : pathToClient)
                pathToClientAux.add((EigenTrust_Sensor)sensor);
            this.pathsToClients.add(pathToClientAux);
        }
    }

    /**
     * Gets the normalized local trust values matrix, C=(c_{ij})
     * @return The normalized local trust values matrix, C=(c_{ij})
     */
    public double[][] get_normalizedLocalTrustValuesMatrix() { return normalizedLocalTrustValuesMatrix; }
}