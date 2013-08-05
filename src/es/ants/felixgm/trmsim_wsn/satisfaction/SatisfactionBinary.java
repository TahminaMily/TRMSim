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

package es.ants.felixgm.trmsim_wsn.satisfaction;

/**
 * <p>This class models a binary representation of the satisfaction of a client
 * with a received service. This satisfaction can only take two values:
 * satisfied (true) or unsatisfied (false)</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.2
 */
public class SatisfactionBinary implements Satisfaction {

    /** Indicates if a client is satisfied with a received service (true) or not (false) */
    private boolean satisfied;

    /**
     * Class SatisfactionBinary constructor
     * @param satisfied Indicates if a client is satisfied with a received service (true) or not (false)
     */
    public SatisfactionBinary(boolean satisfied) {
        this.satisfied = satisfied;
    }

    @Override
    public boolean isSatisfied() {
        return satisfied;
    }

    @Override
    public Satisfaction aggregate(Satisfaction satisfaction) {
        if ((satisfied) && (((SatisfactionBinary)satisfaction).isSatisfied()))
            return new SatisfactionBinary(satisfied);

        if (!(satisfied) && !(((SatisfactionBinary)satisfaction).isSatisfied()))
            return new SatisfactionBinary(satisfied);

        if (Math.random() > 0.5)
            return new SatisfactionBinary(true);
        else
            return new SatisfactionBinary(false);
    }
}