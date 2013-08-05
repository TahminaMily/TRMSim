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
import es.ants.felixgm.trmsim_wsn.network.Service;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>This class models a Sensor implementing PeerTrust</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.3
 * @since 0.2
 */
public class PeerTrust_Sensor extends Sensor {
    /** Window size for storing transactions outcomes */
    protected static int _windowSize;
    /** Collection of Transactions this sensor has had */
    protected Collection<Transaction> transactions;

    /**
     * This constructor creates a new Sensor implementing PeerTrust
     */
    public PeerTrust_Sensor () {
        super();
        transactions = new LinkedList<Transaction>();
    }

    /**
     * This constructor creates a new Sensor implementing PeerTrust
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public PeerTrust_Sensor(int id, double x, double y) {
        super(id,x,y);
        transactions = new LinkedList<Transaction>();
    }

    /**
     * This method adds a new Transaction to the collection of transactions of this sensor
     * @param client The client who requested the service
     * @param server The server who provided the service
     * @param outcome Outcome of the trnsaction to be added
     */
    public synchronized void addNewTransaction(PeerTrust_Sensor client, PeerTrust_Sensor server, Outcome outcome){
        if ((transactions.size() != 0) && (transactions.size() >= _windowSize))
            ((LinkedList<Transaction>)transactions).removeLast();

        ((LinkedList<Transaction>)transactions).addFirst(new Transaction(client, server,outcome));
    }

    /**
     * This method gets the collection of Transactions this sensor has had
     * @return The collection of Transactions this sensor has had
     */
    public synchronized Collection<Transaction> getTransactions(){
        return (LinkedList<Transaction>)transactions;
    }

    /**
     * Returns the number of Transactions this sensor has had
     * @return The number of Transactions this sensor has had
     */
    public synchronized int getNumTransactions() {
        return transactions.size();
    }

    @Override
    public void reset() {
        transactions = new LinkedList<Transaction>();
    }

    /**
     * Indicates if ther is a collusion or not
     * @return true, if there is a collusion, false otherwise
     */
    public static boolean collusion() { return collusion; }

    /**
     * Returns the service requested by the client
     * @return The service requested by the client
     */
    public Service get_requiredService() { return requiredService; }

    /**
     * Sets the window size for storing transactions outcomes
     * @param windowSize New window size for storing transactions outcomes
     */
    public static void set_windowSize(int windowSize) { _windowSize = windowSize; }
}