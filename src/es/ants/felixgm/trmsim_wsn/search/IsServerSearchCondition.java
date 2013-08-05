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

package es.ants.felixgm.trmsim_wsn.search;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;

/**
 * <p>This class represents a searching condition which is accomplished if
 * a given sensor acts as a server, providing some services</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.2
 */
public class IsServerSearchCondition implements ISearchCondition {
    /** Used in order to find just a server regardless its goodness */
    private static final int UNDEFINED = -1;
    /** Used in order to find a server offering a specific service maliciously */
    public static final int MALICIOUS_SERVER = 0;
    /** Used in order to find a server offering a specific service benevolently */
    public static final int BENEVOLENT_SERVER = 1;
    /** Used in order to find a server offering only the standard relay service */
    public static final int RELAY_SERVER = 2;
    /** The specific service to be found */
    private Service service;
    /** The type of server (malicious, benevolent, etc.) to be found */
    private int serverType;

    /**
     * Class IsServerSearchCondition constructor.
     * Used in order to find just a server regardless its goodness and its offered services
     */
    public IsServerSearchCondition() {
        this(null,UNDEFINED);
    }

    /**
     * Class IsServerSearchCondition constructor.
     * Used in order to find just a server regardless its goodness over a specific service
     * @param service Service to be found
     */
    public IsServerSearchCondition(Service service) {
        this(service,UNDEFINED);
    }

    /**
     * Class IsServerSearchCondition constructor.
     * Used in order to find a server offering a specific service with a specific goodness
     * @param service Service to be found
     * @param serverType Type of server to be found (benevolent, malicious, etc.)
     */
    public IsServerSearchCondition(Service service, int serverType) {
        this.service = service;
        this.serverType = serverType;
    }

    /**
     * Indicates if a sensor acts as a server, offering a given service
     * with or without a given goodness
     * @param sensor Sensor to be queried
     * @return true if the given sensor offers the specific service with or
     * without a specific goodness, false otherwise
     */
    public boolean sensorAcomplishesCondition(Sensor sensor) {
        try {
            switch (serverType) {
                case MALICIOUS_SERVER:
                    return (sensor.get_numServices() > 1) &&
                            sensor.offersService(service) &&
                           (sensor.get_goodness(service) < 0.5);
                case BENEVOLENT_SERVER:
                    return (sensor.get_numServices() > 1) &&
                            sensor.offersService(service) &&
                           (sensor.get_goodness(service) >= 0.5);
                case RELAY_SERVER:
                    return (sensor.get_numServices() == 1);
                case UNDEFINED:
                    if (service != null)
                        return ((sensor.get_numServices() > 0) && sensor.offersService(service));
                    else
                        return (sensor.get_numServices() > 0);
                default:
                    return false;
            }
        } catch(Exception ex) { ex.printStackTrace(); }
        return false;
    }

    /**
     * Returns the type of server to be searched (benevolent, malicious or relay)
     * @return The type of server to be searched (benevolent, malicious or relay)
     */
    public int get_serverType() { return serverType; }
}