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

package es.ants.felixgm.trmsim_wsn.trm.eigentrust;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the set of parameters' values of {@link EigenTrust}.</p>
 * <p>An EigenTrust parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # EigenTrust parameters file
 *    ####################################
 *    windowSize=5
 *    epsilon=0.1
 *    preTrustedPeersPercentage=0.3
 *    preTrustedPeersWeight=0.25
 *    zeroTrustNodeSelectionProbability=0.2
 * </pre>
 * This file can be downloaded 
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/EigenTrustparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * 
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.4
 * @since 0.2
 */
public class EigenTrust_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/eigentrust/EigenTrustparameters.txt";
    /** Window size for storing transactions outcomes */
    private int windowSize;
    /** Percentage of pre-trusted peers */
    private double preTrustedPeersPercentage;
    /** Weight of pre-trusted peers */
    private double preTrustedPeersWeight;
    /** Similarity threshold for computing the difference between consecutive global trust vectors */
    private double epsilon;
    /** Probability that a node with global trust value 0 is selected as service provider */
    private double zeroTrustNodeSelectionProbability;
       
    /**
     * Creates a new instance of EigenTrust_Parameters setting them to their default values
     */
    public EigenTrust_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# EigenTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        windowSize = 5;
        epsilon = 0.1;
        preTrustedPeersPercentage = 0.1;
        preTrustedPeersWeight = 0.1;
        zeroTrustNodeSelectionProbability = 0.1;
    }
    
    /**
     * Creates a new instance of EigenTrust_Parameters from a given parameters file name
     * @param fileName EigenTrust parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public EigenTrust_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# EigenTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";


        windowSize = getIntegerParameter("windowSize");
        epsilon = getDoubleParameter("epsilon");
        preTrustedPeersPercentage = getDoubleParameter("preTrustedPeersPercentage");
        preTrustedPeersWeight = getDoubleParameter("preTrustedPeersWeight");
        zeroTrustNodeSelectionProbability = getDoubleParameter("zeroTrustNodeSelectionProbability");
    }    

     /**
     * Returns window size parameter value
     * @return window size parameter value
     */
    public int get_windowSize() { return windowSize; }

    /**
     * Returns the percentage of pre-trusted peers value
     * @return The percentage of pre-trusted peers value
     */
    public double get_preTrustedPeersPercentage() { return preTrustedPeersPercentage; }

    /**
     * Returns the weight of pre-trusted peers value
     * @return The weight of pre-trusted peers value
     */
    public double get_preTrustedPeersWeight() { return preTrustedPeersWeight; }

    /**
     * Returns the epsilon parameter value
     * @return The epsilon parameter value
     */
    public double get_epsilon() { return epsilon; }

    /**
     * Returns the probability that a node with global trust value 0 is selected as service provider
     * @return The probability that a node with global trust value 0 is selected as service provider
     */
    public double get_zeroTrustNodeSelectionProbability() { return zeroTrustNodeSelectionProbability; }

    /**
     * Sets a new window size parameter value
     * @param windowSize New window size parameter value
     */
    public void set_windowSize(int windowSize) {
        this.windowSize = windowSize;
        setIntegerParameter("windowSize",windowSize);
    }

    /**
     * This method sets the percentage of pre-trusted peers value
     * @param preTrustedPeersPercentage New percentage of pre-trusted peers value
     */
    public void set_preTrustedPeersPercentage(double preTrustedPeersPercentage) {
        this.preTrustedPeersPercentage = preTrustedPeersPercentage;
        setDoubleParameter("preTrustedPeersPercentage",preTrustedPeersPercentage);
    }

    /**
     * This method sets the weight of pre-trusted peers value
     * @param preTrustedPeersWeight New weight of pre-trusted peers value
     */
    public void set_preTrustedPeersWeight(double preTrustedPeersWeight) {
        this.preTrustedPeersWeight = preTrustedPeersWeight;
        setDoubleParameter("preTrustedPeersWeight",preTrustedPeersWeight);
    }

    /**
     * This method sets a new epsilon parameter value
     * @param epsilon New epsilon parameter value
     */
    public void set_epsilon(double epsilon) {
        this.epsilon = epsilon;
        setDoubleParameter("epsilon", epsilon);
    }

    /**
     * This method sets a new zeroTrustNodeSelectionProbability parameter value
     * @param zeroTrustNodeSelectionProbability New zeroTrustNodeSelectionProbability parameter value
     */
    public void set_zeroTrustNodeSelectionProbability(double zeroTrustNodeSelectionProbability) {
        this.zeroTrustNodeSelectionProbability = zeroTrustNodeSelectionProbability;
        setDoubleParameter("zeroTrustNodeSelectionProbability", zeroTrustNodeSelectionProbability);
    }

    @Override
    public String toString() {
        String s = parametersFileHeader;

        s += "windowSize="+windowSize+"\n";
        s += "percentagePreTrustedPeers="+preTrustedPeersPercentage+"\n";
        s += "preTrustedPeersWeight="+preTrustedPeersWeight+"\n";
        s += "zeroTrustNodeSelectionProbability="+zeroTrustNodeSelectionProbability+"\n";
        s += "epsilon="+epsilon+"\n";

        return s;
    }
}