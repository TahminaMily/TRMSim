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

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the set of parameters' values of {@link PowerTrust}.</p>
 * <p>A PowerTrust parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # PowerTrust parameters file
 *    ####################################
 *    epsilon=0.0001
 *    powerNodesPercentage=0.01
 *    powerNodesWeight=0.15
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/PowerTrustparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.3
 */
public class PowerTrust_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/powertrust/PowerTrustparameters.txt";
    /** Percentage of power nodes */
    private double powerNodesPercentage;
    /** Weight of power nodes */
    private double powerNodesWeight;
    /** Similarity threshold for computing the difference between consecutive global reputation vectors */
    private double epsilon;

    /**
     * Creates a new instance of PowerTrust_Parameters setting them to their default values
     */
    public PowerTrust_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PowerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        epsilon = 0.0001;
        powerNodesPercentage = 0.01;
        powerNodesWeight = 0.15;
    }

    /**
     * Creates a new instance of PowerTrust_Parameters from a given parameters file name
     * @param fileName PowerTrust parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public PowerTrust_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PowerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        epsilon = getDoubleParameter("epsilon");
        powerNodesPercentage = getDoubleParameter("powerNodesPercentage");
        powerNodesWeight = getDoubleParameter("powerNodesWeight");
    }

    /**
     * Returns the percentage of power nodes value
     * @return The percentage of power nodes value
     */
    public double get_powerNodesPercentage() { return powerNodesPercentage; }

    /**
     * Returns the weight of power nodes value
     * @return The weight of power nodes value
     */
    public double get_powerNodesWeight() { return powerNodesWeight; }

    /**
     * Returns the epsilon parameter value
     * @return The epsilon parameter value
     */
    public double get_epsilon() { return epsilon; }

    /**
     * This method sets the percentage of power nodes value
     * @param powerNodesPercentage New percentage of power nodes value
     */
    public void set_powerNodesPercentage(double powerNodesPercentage) {
        this.powerNodesPercentage = powerNodesPercentage;
        setDoubleParameter("powerNodesPercentage",powerNodesPercentage);
    }

    /**
     * This method sets the weight of power nodes value
     * @param powerNodesWeight New weight of power nodes value
     */
    public void set_powerNodesWeight(double powerNodesWeight) {
        this.powerNodesWeight = powerNodesWeight;
        setDoubleParameter("powerNodesWeight",powerNodesWeight);
    }

    /**
     * This method sets a new epsilon parameter value
     * @param epsilon New epsilon parameter value
     */
    public void set_epsilon(double epsilon) {
        this.epsilon = epsilon;
        setDoubleParameter("epsilon", epsilon);
    }

    @Override
    public String toString() {
        String s = parametersFileHeader;

        s += "epsilon="+epsilon+"\n";
        s += "powerNodesPercentage="+powerNodesPercentage+"\n";
        s += "powerNodesWeight="+powerNodesWeight+"\n";

        return s;
    }
}