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

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the set of parameters' values of {@link PeerTrust}.</p>
 * <p>A PeerTrust parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # PeerTrust parameters file
 *    ####################################
 *    windowSize=5
 *    alpha=1.0
 *    beta=0.0
 * </pre>
 * This file can be downloaded 
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/PeerTrustparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * 
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.4
 * @since 0.2
 */
public class PeerTrust_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/peertrust/PeerTrustparameters.txt";
    /** Window size for storing transactions outcomes */
    private int windowSize;
    /** Feedback-based evaluation weight */
    private double alpha;
    /** Community context evaluation weight */
    private double beta;
       
    /**
     * Creates a new instance of PeerTrust_Parameters setting them to their default values
     */
    public PeerTrust_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PeerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        windowSize = 5;
        alpha = 1.0;
        beta = 0.0;
    }
    
    /**
     * Creates a new instance of PeerTrust_Parameters from a given parameters file name
     * @param fileName PeerTrust parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public PeerTrust_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# PeerTrust parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        windowSize = getIntegerParameter("windowSize");
        alpha = getDoubleParameter("alpha");
        beta = getDoubleParameter("beta");
    }
    
    /**
     * Returns alpha parameter value
     * @return alpha parameter value
     */
    public double get_alpha() { return alpha; }
    
    /**
     * Returns beta parameter value
     * @return beta parameter value
     */
    public double get_beta() { return beta; }
      
    /**
     * Sets a new alpha parameter value
     * @param alpha New alpha parameter value
     */
    public void set_alpha(double alpha) {
        this.alpha = alpha;
        setDoubleParameter("alpha",alpha);
    }
    
    /**
     * Sets a new beta parameter value
     * @param beta New beta parameter value
     */
    public void set_beta(double beta) {
        this.beta = beta;
        setDoubleParameter("beta",beta);
    }

     /**
     * Returns window size parameter value
     * @return window size parameter value
     */
    public int get_windowSize() { return windowSize; }

    /**
     * Sets a new window size parameter value
     * @param windowSize New window size parameter value
     */
    public void set_windowSize(int windowSize) {
        this.windowSize = windowSize;
        setIntegerParameter("windowSize",windowSize);
    }

    @Override
    public String toString() {
        String s = parametersFileHeader;
        
        s += "windowSize="+windowSize+"\n";
        s += "alpha="+alpha+"\n";
        s += "beta="+beta+"\n";

        return s;
    }
}