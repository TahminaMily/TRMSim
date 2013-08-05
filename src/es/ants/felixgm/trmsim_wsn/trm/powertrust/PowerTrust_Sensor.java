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
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.trm.peertrust.Transaction;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

/**
 * <p>This class models a Sensor implementing PowerTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.3
 */
public class PowerTrust_Sensor extends Sensor implements Comparable<PowerTrust_Sensor>{
    /** Number of sensors composing the network this sensor belongs to */
    protected static int _numSensors = 0;
    /** Score v_i */
    protected double globalReputationScore;
    /** Vector r_i */
    protected double[] normalizedLocalTrustVector;
    /** Vector s_i */
    protected double[] mostRecentFeedbackScoreVector;
    /** Collection of Transactions this sensor has had */
    protected Collection<Transaction> transactions;
    /** Indicates if this sensor is a power node (true) or not (false) */
    protected boolean isPowerNode;

    /**
     * This constructor creates a new Sensor implementing PowerTrust
     */
    public PowerTrust_Sensor () {
        super();
    }

    /**
     * This constructor creates a new Sensor implementing PowerTrust
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public PowerTrust_Sensor(int id, double x, double y) {
        super(id,x,y);
    }

    @Override
    public void reset() {
        transactions = new LinkedList<Transaction>();
        globalReputationScore = 1.0/_numSensors;
        normalizedLocalTrustVector = new double[_numSensors];
        mostRecentFeedbackScoreVector = new double[_numSensors];
        isPowerNode = false;
    }

    /**
     * This method computes the normalized local trust score r_{ij}\in[0,1]
     * @param server Server j
     * @return r_{ij}
     */
    public double getNormalizedLocalTrustScore(PowerTrust_Sensor server) {
        this.transmittedDistance += this.distance(server);
        try {
            if (collusion) {
                if (server.get_goodness(requiredService) < 0.5)
                    return 1.0;
                else
                    return 0.0;
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return normalizedLocalTrustVector[server.id()-1];
    }

    /**
     * This method computes the global retupation of this node according to
     * algorithm 3 of PowerTrust, i.e., v_i = (1-alpha)*sum(v_j*r_{ji})+alpha/m
     * if this sensor is a power node and v_i = (1-alpha)*sum(v_j*r_{ji}), otherwise
     * @return The global reputation of this node after computing it
     */
    public synchronized double computeGlobalReputation() {
        if (transactions.size() > 0) {
            double epsilon = ((PowerTrust_Parameters)trmmodelWSN.get_TRMParameters()).get_epsilon();
            double pre = globalReputationScore;
            do {
                pre = globalReputationScore;
                double sum = 0.0;
                for (Transaction transaction : transactions) {
                    PowerTrust_Sensor client = (PowerTrust_Sensor)transaction.getClient();
                    sum += client.get_globalReputationScore()*client.getNormalizedLocalTrustScore(this);
                }
                double alpha = ((PowerTrust_Parameters)trmmodelWSN.get_TRMParameters()).get_powerNodesWeight();
                int m = (int)(_numSensors*((PowerTrust_Parameters)trmmodelWSN.get_TRMParameters()).get_powerNodesPercentage());
                if (isPowerNode)
                    globalReputationScore = (1.0 - alpha)*sum + m/alpha;
                else
                    globalReputationScore = (1.0 - alpha)*sum;
            } while (Math.abs(globalReputationScore-pre) > epsilon);
        }
        return globalReputationScore;
    }

    /**
     * This method adds a new Transaction to the collection of transactions of this sensor
     * @param client The client who requested the service
     * @param server The server who provided the service
     * @param outcome Outcome of the trnsaction to be added
     */
    public synchronized void addNewTransaction(PowerTrust_Sensor client, PowerTrust_Sensor server, Outcome outcome){
        ((LinkedList<Transaction>)transactions).addFirst(new Transaction(client,server,outcome));

        if (get_numServices() == 0) {
            mostRecentFeedbackScoreVector[server.id()-1] = outcome.get_satisfaction().isSatisfied() ? 1.0 : 0.0;
            double localTrustValueSum = 0.0;
            for (int i = 0; i < mostRecentFeedbackScoreVector.length; i++)
                localTrustValueSum += mostRecentFeedbackScoreVector[i];
            normalizedLocalTrustVector[server.id()-1] = mostRecentFeedbackScoreVector[server.id()-1]/localTrustValueSum;
        }
    }

    /**
     * This method returns the last outcome of a performed transaction and,
     * additionally, dynamically selects the new set of m power nodes
     * @return The last outcome of a performed transaction
     */
    @Override
    public Outcome get_outcome() {
        int numPowerNodes = (int)(_numSensors*((PowerTrust_Parameters)PowerTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_powerNodesPercentage());
        if ((((PowerTrust_Parameters)PowerTrust_Sensor.get_TRModel_WSN().get_TRMParameters()).get_powerNodesPercentage() > 0) && (numPowerNodes == 0))
            numPowerNodes = 1;
        
        Collection<Vector<Sensor>> pathsToServers = this.findSensors(new IsServerSearchCondition(requiredService));
        Vector<PowerTrust_Sensor> reachableServers = new Vector<PowerTrust_Sensor>();
        if (pathsToServers != null) {
            for (Vector<Sensor> pathToServer : pathsToServers) {
                PowerTrust_Sensor server = (PowerTrust_Sensor)pathToServer.lastElement();
                reachableServers.add(server);
                server.setPowerNode(false);
            }

            Collections.sort(reachableServers);
            int index = 0;
            while ((index < reachableServers.size()) && (numPowerNodes > 0)) {
                reachableServers.get(index).setPowerNode(true);
                index++;
                numPowerNodes--;
            }
        }
        return outcome;
    }

    /**
     * This method compares two PowerTrust sensors according to their global
     * reputation scores
     * @param comparedSensor Sensor to be compared with this sensor
     * @return 1, 0 or -1 if this sensor's global reputation is greater than,
     * equal to, or less than comparedSensor's global reputation
     */
    public int compareTo(PowerTrust_Sensor comparedSensor) {
        if (this.globalReputationScore > comparedSensor.get_globalReputationScore())
            return 1;
        else if (this.globalReputationScore < comparedSensor.get_globalReputationScore())
            return -1;
        else
            return 0;
    }

    /**
     * Sets the number of sensors composing the network this sensor belongs to
     * @param numSensors The number of sensors composing the network this sensor belongs to
     */
    public static void setNumSensors(int numSensors){ _numSensors = numSensors; }

    /**
     * Sets this senor as a power node or not
     * @param isPowerNode true if this sensor is to be a power node, false otherwise
     */
    public void setPowerNode(boolean isPowerNode){ this.isPowerNode = isPowerNode; }

    /**
     * Indicates if this sensor is a power node (true) or not (false)
     * @return true if this sensor is a power node, false otherwise
     */
    public boolean isPowerNode() { return isPowerNode; }

    /**
     * Returns the number of sensors composing the network this sensor belongs to
     * @return The number of sensors composing the network this sensor belongs to
     */
    public static int getNumSensors() { return _numSensors; }

    /**
     * Returns the current global reputation score v_i^t
     * @return The current global reputation score v_i^t
     */
    public double get_globalReputationScore() { return globalReputationScore; }
}