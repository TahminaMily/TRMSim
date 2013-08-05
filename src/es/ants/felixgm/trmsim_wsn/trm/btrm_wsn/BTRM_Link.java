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

package es.ants.felixgm.trmsim_wsn.trm.btrm_wsn;

import es.ants.felixgm.trmsim_wsn.network.Link;

/**
 * <p>This class models a link between two sensors, with a certain trace of pheromone and
 * a certain heuristic value.</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.2
 * @since 0.2
 */
public class BTRM_Link extends Link {
    /** Maximum value for a pheromone trace: {@value} */
    static public final double MAX_PHEROMONE = 0.999;
    /** Minimum value for a pheromone trace: {@value} */
    static public final double MIN_PHEROMONE = 0.001;
    /** Initial value of pheromone */
    static private double _initialPheromone = 0.5;
    /** Pheromone trace of this link */
    protected double pheromone;
    /** Heuristic value of this link */
    protected double heuristic;

    /**
     * Class BTRM_Link constructor
     * @param source Source sensor of the link
     * @param destination Destination sensor of the link
     */
    public BTRM_Link(BTRM_Sensor source, BTRM_Sensor destination) {
        super(source, destination);

        pheromone = pheromoneInitialization();
        heuristic = 1.0/source.distance(destination);
    }

    /**
     * Computes the initial value of pheromone of this link
     * @return The initial value of pheromone of this link
     */
    private double pheromoneInitialization() {
        return Math.min(MAX_PHEROMONE,
                Math.max(MIN_PHEROMONE,
                _initialPheromone+(2*Math.random()-1.0)*_initialPheromone*(1.0-_initialPheromone)));
    }

    /**
     * Sets this link to an initial state
     */
    public void reset() {
        pheromone = pheromoneInitialization();
    }

    @Override
    public BTRM_Sensor get_source() { return (BTRM_Sensor)source; }

    @Override
    public BTRM_Sensor get_destination() { return (BTRM_Sensor)destination; }

    /**
     * Returns the pheromone trace of this link 
     * @return The pheromone trace of this link 
     */
    public double get_pheromone() {
        return pheromone;
    }

    /**
     * Sets the pheromone trace of this link
     * @param value The new pheromone trace of this link
     */
    public void set_pheromone(double value) {
        pheromone = Math.min(MAX_PHEROMONE,Math.max(MIN_PHEROMONE,value));
    }

    /**
     * Returns the heuristic value of this link
     * @return The heuristic value of this link
     */
    public double get_heuristic() {
        return heuristic;
    }

    /**
     * Sets the heuristic value of this link
     * @param value The new heuristic value of this link
     */
    public void set_heuristic(double value) {
        heuristic = value;
    }

    /**
     * Sets the initial value of pheromone of this link
     * @param initialPheromone The new initial value of pheromone of this link
     */
    public static void set_initialPheromone(double initialPheromone) { _initialPheromone = initialPheromone; }
}