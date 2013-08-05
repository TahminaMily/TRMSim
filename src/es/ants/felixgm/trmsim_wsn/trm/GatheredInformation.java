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

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class represents the collected or gathered information of a
 * Trust and Reputation Model from the network in order to evaluate the reputation
 * and trustworthiness of every sensor and decide which one to interact with</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.2
 * @since 0.2
 */
public class GatheredInformation {
    /** Set of paths leading from a certain client to all the reachable servers */
    protected Collection<Vector<Sensor>> pathsToServers;

    /**
     * Class GatheredInformation constructor
     * @param pathsToServers Set of paths leading from a certain client to all the reachable servers
     */
    public GatheredInformation(Collection<Vector<Sensor>> pathsToServers){
        setCollectionServers(pathsToServers);
    }

    /**
     * Returns the set of paths leading from a certain client to all the reachable servers
     * @return The set of paths leading from a certain client to all the reachable servers
     */
    public Collection<Vector<Sensor>> getPathsToServers() {
        return pathsToServers;
    }

    /**
     * Sets the collection of paths leading from a certain client to all the reachable servers
     * @param pathsToServers Set of paths leading from a certain client to all the reachable servers
     */
    protected void setCollectionServers(Collection<Vector<Sensor>> pathsToServers){
        if (this.pathsToServers == null)
            this.pathsToServers = new Vector<Vector<Sensor>>();

        for (Vector<Sensor> v : pathsToServers)
            this.pathsToServers.add(v);
    }
}
