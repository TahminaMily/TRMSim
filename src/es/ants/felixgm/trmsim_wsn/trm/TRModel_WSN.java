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

package es.ants.felixgm.trmsim_wsn.trm;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;

import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class represents a generic Trust and Reputation Model for Wireless Sensor Networks,
 * also applicable to P2P and Ad-hoc ones</p>
 <font color="#FF0000">
 <p><strong>A subclass of this class has to be 
 implemented in order to add a new Trust and Reputation Model</strong></p>
 </font>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.2
 */
public abstract class TRModel_WSN {
    /** Trust and Reputation Model parameters */
    protected TRMParameters trmParameters;
    
    /**
     * Creates a new instance of TRModel_WSN
     * @param trmParameters Trust and Reputation Model parameters
     */
    protected TRModel_WSN(TRMParameters trmParameters) {
        this.trmParameters = trmParameters;
    }

    /**
     * This method collects or gathers the required information from the network needed
     * to evaluate each sensor offering a given service in order to determine whether to
     * have a transaction with it or not
     * @param client The client applying this trust and reputation model in order to
     * find the most trustworthy and reputable server offering a given service
     * @param service The service requested by the given client
     * @return The required information from the network needed to evaluate each
     * sensor offering a given service in order to determine whether to have a
     * transaction with it or not
     */
    public abstract GatheredInformation gatherInformation(Sensor client, Service service);

    /**
     * This method computes a trust and/or reputation value for each reachable server
     * from the specified client and returns either a sorted list of all the reachable
     * servers or a path leading directly to the most trustworthy and/or reputable one
     * @param client The client applying this trust and reputation model in order to
     * find the most trustworthy and reputable server offering a given service
     * @param gi The gathered information fro the network needed in order to compute
     * the trust and/or reputation value for each reachable server
     * @return Either a path leading to the most trustworthy server found, or a
     * sorted list of all the reachable servers
     */
    public abstract Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi);
    
    /**
     * This method actually requests a desired service to a specified server and
     * evaluates the satisfaction of the client with the actually received service
     * @param path Path of sensors leading to the most trustworthy and reputable server
     * or sorted list of all the reachable servers
     * @param service Service requested by the client running this trust and reputation model
     * @return Outcome with the satisfaction of the client with the received service
     */
    public abstract Outcome performTransaction(Vector<Sensor> path, Service service);
    
    /**
     * This method rewards a server if the client is satisfied with the received
     * service, according to that precise satisfaction
     * @param path Path of sensors leading to the requesting server
     * @param outcome Client's satisfaction with the received service
     * @return Outcome with the updated transmitted distance of the messages sent
     */
    public abstract Outcome reward(Vector<Sensor> path, Outcome outcome);
    
    /**
     * This method punishes a server if the client is not satisfied with the received
     * service, according to that precise dissatisfaction
     * @param path Path of sensors leading to the requesting server
     * @param outcome Client's dissatisfaction with the received service
     * @return Outcome with the updated transmitted distance of the messages sent
     */
    public abstract Outcome punish(Vector<Sensor> path, Outcome outcome);

    /**
     * This method generates a new random network specific for this trust and
     * reputation model
     * @param numSensors Number of sensors composing the network
     * @param probClients Probability of a sensor to act as a client requesting services
     * @param rangeFactor Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param probServices A collection of probabilities of offering a certain service, one per service
     * @param probGoodness A collection of goodnesses about offering a certain service, one per service
     * @param services All the services offered by the generated Network
     * @return A new random network specific for this trust and reputation model
     */
    public abstract Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services);

    /**
     * This method loads a network from a XML file and creates the specific network
     * corresponding to this trust and reputation model
     * @param xmlFilePath Path of the XML to load the network from
     * @return The loaded and generated specific network for this trust and
     * reputation model
     * @throws java.lang.Exception If the XML file given does not have the appropriate structure, or if
     * a sensor links to an undefined sensor, or if a sensor links to itself
     */
    public abstract Network loadCurrentNetwork(String xmlFilePath) throws Exception;

    /**
     * Returns the associated trust and reputation model´s parameters
     * @return The associated trust and reputation model´s parameters
     */
    public TRMParameters get_TRMParameters() { return trmParameters; }

    /**
     * Sets the associated trust and reputation model´s parameters
     * @param trmParameters The associated trust and reputation model´s parameters
     */
    public void set_TRMParameters(TRMParameters trmParameters) { this.trmParameters = trmParameters; }
}