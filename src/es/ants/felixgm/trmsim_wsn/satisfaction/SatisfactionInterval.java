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
 * <p>This class models the representation of the satisfaction of a client with
 * a received service, through an interval of real numbers</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.2
 */
public class SatisfactionInterval implements Satisfaction {
    /** Lower bound of the interval */
    private double min;
    /** Upper bound of the interval */
    private double max;
    /** Actual satisfaction value */
    private double value;

    /**
     * Class SatisfactionInterval constructor
     * @param min Lower bound of the interval
     * @param max Upper bound of the interval
     * @param value Actual satisfaction value
     */
    public SatisfactionInterval(double min, double max, double value) {
        if (min > max)
            min = max;
        if ((min > value) || (max < value))
            value = min;
        this.min = min;
        this.max = max;
        this.value = value;
    }

    /**
     * Returns the actual satisfaction value
     * @return The actual satisfaction value
     */
    public double getSatisfactionValue() {
        return value;
    }

    /**
     * This method returns the lower bound of the interval
     * @return The lower bound of the interval
     */
    public double getMin() { return min; }

    /**
     * This method returns the upper bound of the interval
     * @return The upper bound of the interval
     */
    public double getMax() { return max; }

    @Override
    public boolean isSatisfied() {
        return (value > ((max-Math.abs(min))/2));
    }

    @Override
    public Satisfaction aggregate(Satisfaction satisfaction) {
        return new SatisfactionInterval(Math.min(min, ((SatisfactionInterval)satisfaction).getMin()),
                                        Math.max(max, ((SatisfactionInterval)satisfaction).getMax()),
                                        (value+((SatisfactionInterval)satisfaction).getSatisfactionValue())/2.0);
    }
}