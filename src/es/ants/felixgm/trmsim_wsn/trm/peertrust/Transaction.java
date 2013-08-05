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

package es.ants.felixgm.trmsim_wsn.trm.peertrust;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.satisfaction.Satisfaction;

/**
 * <p>This class models a transaction between two sensors and the corresponding
 * satisfaction of the client who received the service</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.3
 * @since 0.2
 */
public class Transaction {
    /** The client who requested the service */
    private Sensor client;
    /** The server who provided the service */
    private Sensor server;
    /** Outcome with the satisfaction of the client with the received service */
    private Outcome outcome;

    /**
     * Class Transaction constructor
     * @param client The client who requested the service
     * @param server The server who provided the service
     * @param outcome Outcome with the satisfaction of the client with the received service
     */
    public Transaction (Sensor client, Sensor server, Outcome outcome){
        this.client = client;
        this.server = server;
        this.outcome = outcome;
    }

    /**
     * Returns the client who requested the service
     * @return The client who requested the service
     */
    public Sensor getClient() {
        return client;
    }

    /**
     * Returns the server who provided the service
     * @return The server who provided the service
     */
    public Sensor getServer() {
        return server;
    }

    /**
     * Returns the satisfaction of the client with the received service
     * @return The satisfaction of the client with the received service
     */
    public Satisfaction getSatisfaction() {
        return outcome.get_satisfaction();
    }
}