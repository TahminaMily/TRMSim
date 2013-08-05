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

import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;
import es.ants.felixgm.trmsim_wsn.trm.peertrust.Transaction;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

/**
 * <p>This class models a Sensor implementing EigenTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.3
 * @since 0.2
 */
public class EigenTrust_Sensor extends Sensor {
    /** Number of sensors composing the network this sensor belongs to */
    protected static int _numSensors = 0;
    /** Window size for storing transactions outcomes */
    protected static int _windowSize;
    /** Collection of Transactions this sensor has had */
    protected Collection<Transaction> transactions;
    /** Vector t */
    protected double[] globalTrustVector;
    /** Vector p */
    protected static double[] _preTrustedPeersVector = null;
    /** Collection of severs who this sensor has interacted with */
    protected Collection<EigenTrust_Sensor> interactedServers;
    /** Indicates if this sensor is a pre-trusted peer (true) or not (false) */
    protected boolean isPreTrustedPeer;

    /**
     * This constructor creates a new Sensor implementing EigenTrust
     */
    public EigenTrust_Sensor () {
        super();
    }

    /**
     * This constructor creates a new Sensor implementing EigenTrust
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public EigenTrust_Sensor(int id, double x, double y) {
        super(id,x,y);
    }

    /**
     * This method computes the local trust value s_{ij}\in \mathbb{N}
     * @param server Server j
     * @return s_{ij}
     */
    private double getLocalTrustValue(EigenTrust_Sensor server) {
        double localTrustValue = 0.0;

        for (Transaction transaction : transactions) 
            if (transaction.getServer().equals(server))
                localTrustValue += ((SatisfactionInterval)transaction.getSatisfaction()).getSatisfactionValue();

        return localTrustValue;
    }

    /**
     * This method computes the normalized local trust value c_{ij}\in[0,1]
     * @param server Server j 
     * @return c_{ij}
     */
    public synchronized double getNormalizedLocalTrustValue(EigenTrust_Sensor server) {
        double localTrustValuesSum = 0.0;
        double normalizedLocalTrustValue = 0.0;

        try {
            if (collusion && offersService(requiredService) && (get_goodness(requiredService) < 0.5) &&
                    (server.get_numServices() > 0)) {
                if (server.get_goodness(requiredService) < 0.5)
                    return 1.0;
                else
                    return 0.0;
            } else {
                for (EigenTrust_Sensor _server : interactedServers) {
                    double localTrustValue = Math.max(getLocalTrustValue(_server), 0.0);
                    localTrustValuesSum += localTrustValue;
                    if (_server.equals(server)) {
                        normalizedLocalTrustValue = localTrustValue;
                    }
                }
                if (localTrustValuesSum != 0.0) {
                    return normalizedLocalTrustValue / localTrustValuesSum;
                } else {
                    return _preTrustedPeersVector[server.id() - 1];
                }
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return 0.0;
    }

    /**
     * This method adds a new Transaction to the collection of transactions of this sensor
     * @param client The client who requested the service
     * @param server The server who provided the service
     * @param outcome Outcome of the trnsaction to be added
     */
    public synchronized void addNewTransaction(EigenTrust_Sensor client, EigenTrust_Sensor server, Outcome outcome){
        if ((transactions.size() != 0) && (transactions.size() >= _windowSize))
            ((LinkedList<Transaction>)transactions).removeLast();

        ((LinkedList<Transaction>)transactions).addFirst(new Transaction(client,server,outcome));
        if (!interactedServers.contains(server))
            interactedServers.add(server);
    }

    @Override
    public void reset() {
        transactions = new LinkedList<Transaction>();
        interactedServers = new LinkedList<EigenTrust_Sensor>();
        globalTrustVector = new double[_numSensors];
        if (_preTrustedPeersVector == null)
            _preTrustedPeersVector = new double[_numSensors];
        if (((EigenTrust_Parameters)(trmmodelWSN.get_TRMParameters())).get_preTrustedPeersPercentage() > 0) {
            for (int i = 0; i < globalTrustVector.length; i++)
                globalTrustVector[i] = _preTrustedPeersVector[i];
        } else {
            Collection<Vector<Sensor>> pathsToServers = findSensors(new IsServerSearchCondition(requiredService));
            if (pathsToServers != null)
                for (Vector<Sensor> pathToServer : pathsToServers)
                    globalTrustVector[pathToServer.lastElement().id()-1] = 1.0/pathsToServers.size();
        }
    }

    /**
     * Sets the new global trust vector t_i^{k+1}
     * @param globalTrustVector New global trust vector t_i^{k+1}
     */
    public void set_globalTrustVector(double[] globalTrustVector) {
        for (int i = 0; i < globalTrustVector.length; i++)
            this.globalTrustVector[i] = globalTrustVector[i];
    }

    /**
     * Returns the current global trust vector t_i^k
     * @return The current global trust vector t_i^k
     */
    public double[] get_globalTrustVector() { return globalTrustVector; }

    /**
     * Sets the new pre-trusted peers vector p
     * @param preTrustedPeersVector New pre-trusted peers vector p
     */
    public static void set_preTrustedPeersVector(double[] preTrustedPeersVector) {
        _preTrustedPeersVector = new double[preTrustedPeersVector.length];
        for (int i = 0; i < preTrustedPeersVector.length; i++) 
            _preTrustedPeersVector[i] = preTrustedPeersVector[i];
    }

    /**
     * Returns the current pre-trusted peers vector p
     * @return The current pre-trusted peers vector p
     */
    public static double[] get_preTrustedPeersVector() { return _preTrustedPeersVector; }

    /**
     * Returns the number of sensors composing the network this sensor belongs to
     * @return The number of sensors composing the network this sensor belongs to
     */
    public static int getNumSensors() { return _numSensors; }

    /**
     * Indicates if this sensor is a pre-trusted peer (true) or not (false)
     * @return true if this sensor is a pre-trusted peer, false otherwise
     */
    public boolean isPreTrustedPeer() { return isPreTrustedPeer; }

    /**
     * Sets the number of sensors composing the network this sensor belongs to
     * @param numSensors The number of sensors composing the network this sensor belongs to
     */
    public static void setNumSensors(int numSensors){ _numSensors = numSensors; }

    /**
     * Sets the window size for storing transactions outcomes
     * @param windowSize New window size for storing transactions outcomes
     */
    public static void set_windowSize(int windowSize) { _windowSize = windowSize; }

    /**
     * Sets this senor as a pre-trusted peer or not
     * @param isPreTrustedPeer true if this sensor is to be a pre-trusted peer, false otherwise
     */
    public void setPreTrustedPeer(boolean isPreTrustedPeer) { this.isPreTrustedPeer = isPreTrustedPeer; }
}