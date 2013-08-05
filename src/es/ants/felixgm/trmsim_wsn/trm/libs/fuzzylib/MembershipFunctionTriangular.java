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

package es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib;

/**
 * <p>This class models a Triangular Membership function</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class MembershipFunctionTriangular extends MembershipFunction {
    /**
     * Constructor
     * @param min : Begining of triangular function
     * @param med : Medium of triangular function
     * @param max : End of triangular function
     */
    public MembershipFunctionTriangular(double min, double med, double max) {
        super();
        this.parameters = new double[3];
        this.parameters[0] = min;
        this.parameters[1] = med;
        this.parameters[2] = max;
    }

    @Override
    public double membership(double input) {
        //input is out of bounds
        if( (input <= this.parameters[0]) || (input >= this.parameters[2]) )
            return 0;

        //input is in the first half (between min and med)
        if( input < this.parameters[1] )
            return ((input - this.parameters[0]) / (this.parameters[1] - this.parameters[0]));

        //input is in the second half (between med and max)
        return 1 - ((input - this.parameters[1]) / (this.parameters[2] - this.parameters[1]));
    }

    public double get_min() { return parameters[0]; }
    public double get_med() { return parameters[1]; }
    public double get_max() { return parameters[2]; }
}