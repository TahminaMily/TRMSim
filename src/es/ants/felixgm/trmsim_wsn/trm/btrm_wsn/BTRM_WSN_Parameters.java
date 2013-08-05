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

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

/**
 * <p>This class represents the set of parameters' values of {@link BTRM_WSN}.</p>
 * <p>A BTRM-WSN parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # BTRM-WSN parameters file
 *    # Thu Jun 19 14:07:13 CEST 2008
 *    ####################################
 *    pathLengthFactor=0.71
 *    alpha=1.0
 *    phi=0.01
 *    initialPheromone=0.85
 *    q0=0.45
 *    numIterations=0.59
 *    punishmentThreshold=0.48
 *    rho=0.87
 *    beta=1.0
 *    numAnts=0.35
 *    transitionThreshold=0.66
 * </pre>
 * This file can be downloaded 
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/BTRM-WSNparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * 
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.2
 */
public class BTRM_WSN_Parameters extends TRMParameters {
    
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/btrm-wsn/BTRM-WSNparameters.txt";

    private double phi;
    private double rho;
    private double q0;
    private double numAnts;
    private double numIterations;
    private double alpha;
    private double beta;
    private double initialPheromone;
    private double pathLengthFactor;
    private double transitionThreshold;
    private double punishmentThreshold;

    /**
     * Creates a new instance of BTRM_WSN_Parameters setting them to their default values
     */
    public BTRM_WSN_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# BTRM-WSN parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        phi = 0.1;
        rho = 0.2;
        q0 = 0.98;
        alpha = 1.0;
        beta = 1.0;
        numAnts = 0.5;
        numIterations = 0.5;
        initialPheromone = 0.5;
        pathLengthFactor = 0.5;
        transitionThreshold = 0.5;
        punishmentThreshold = 0.5;
    }

    /**
     * Creates a new instance of BTRM_WSN_Parameters from a given parameters file name
     * @param fileName BTRM-WSN parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public BTRM_WSN_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# BTRM-WSN parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";


        phi = getDoubleParameter("phi");
        rho = getDoubleParameter("rho");
        q0 = getDoubleParameter("q0");
        alpha = getDoubleParameter("alpha");
        beta = getDoubleParameter("beta");
        numAnts = getDoubleParameter("numAnts");
        numIterations = getDoubleParameter("numIterations");
        initialPheromone = getDoubleParameter("initialPheromone");
        pathLengthFactor = getDoubleParameter("pathLengthFactor");
        transitionThreshold = getDoubleParameter("transitionThreshold");
        punishmentThreshold = getDoubleParameter("punishmentThreshold");
    }
    
    /**
     * Returns phi parameter value
     * @return phi parameter value
     */
    public double get_phi() { return phi; }
    
    /**
     * Returns rho parameter value
     * @return rho parameter value
     */
    public double get_rho() { return rho; }
    
    /**
     * Returns q0 parameter value
     * @return q0 parameter value
     */
    public double get_q0() { return q0; }
    
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
     * Returns numAnts parameter value
     * @return numAnts parameter value
     */
    public double get_numAnts() { return numAnts; }
    
    /**
     * Returns numIterations parameter value
     * @return numIterations parameter value
     */
    public double get_numIterations() { return numIterations; }
    
    /**
     * Returns initialPheromone parameter value
     * @return initialPheromone parameter value
     */
    public double get_initialPheromone() { return initialPheromone; }
    
    /**
     * Returns pathLengthFactor parameter value
     * @return pathLengthFactor parameter value
     */
    public double get_pathLengthFactor() { return pathLengthFactor; }
    
    /**
     * Returns transitionThreshold parameter value
     * @return transitionThreshold parameter value
     */
    public double get_transitionThreshold() { return transitionThreshold; }
    
    /**
     * Returns punishmentThreshold parameter value
     * @return punishmentThreshold parameter value
     */
    public double get_punishmentThreshold() { return punishmentThreshold; }
    
    /**
     * Sets a new phi parameter value
     * @param phi New phi parameter value
     */
    public void set_phi(double phi) {
        this.phi = phi;
        setDoubleParameter("phi",phi);
    }

    /**
     * Sets a new rho parameter value
     * @param rho New rho parameter value
     */
    public void set_rho(double rho) {
        this.rho = rho;
        setDoubleParameter("rho",rho);
    }

    /**
     * Sets a new q0 parameter value
     * @param q0 New q0 parameter value
     */
    public void set_q0(double q0) {
        this.q0 = q0;
        setDoubleParameter("q0",q0);
    }
    
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
     * Sets a new numAnts parameter value
     * @param numAnts New numAnts parameter value
     */
    public void set_numAnts(double numAnts) {
        this.numAnts = numAnts;
        setDoubleParameter("numAnts",numAnts);
    }

    /**
     * Sets a new numIterations parameter value
     * @param numIterations New numIterations parameter value
     */
    public void set_numIterations(double numIterations) {
        this.numIterations = numIterations;
        setDoubleParameter("numIterations",numIterations);
    }

    /**
     * Sets a new initialPheromone parameter value
     * @param initialPheromone New initialPheromone parameter value
     */
    public void set_initialPheromone(double initialPheromone) {
        this.initialPheromone = initialPheromone;
        setDoubleParameter("initialPheromone",initialPheromone);
    }

    /**
     * Sets a new pathLengthFactor parameter value
     * @param pathLengthFactor New pathLengthFactor parameter value
     */
    public void set_pathLengthFactor(double pathLengthFactor) {
        this.pathLengthFactor = pathLengthFactor;
        setDoubleParameter("pathLengthFactor",pathLengthFactor);
    }

    /**
     * Sets a new transitionThreshold parameter value
     * @param transitionThreshold New transitionThreshold parameter value
     */
    public void set_transitionThreshold(double transitionThreshold) {
        this.transitionThreshold = transitionThreshold;
        setDoubleParameter("transitionThreshold",transitionThreshold);
    }

    /**
     * Sets a new punishmentThreshold parameter value
     * @param punishmentThreshold New punishmentThreshold parameter value
     */
    public void set_punishmentThreshold(double punishmentThreshold) {
        this.punishmentThreshold = punishmentThreshold;
        setDoubleParameter("punishmentThreshold",punishmentThreshold);
    }

    @Override
    public String toString() {
        String s = parametersFileHeader;
        
        s += "phi="+phi+"\n";
        s += "rho="+rho+"\n";
        s += "q0="+q0+"\n";
        s += "numAnts="+numAnts+"\n";
        s += "numIterations="+numIterations+"\n";
        s += "alpha="+alpha+"\n";
        s += "beta="+beta+"\n";
        s += "initialPheromone="+initialPheromone+"\n";
        s += "pathLengthFactor="+pathLengthFactor+"\n";
        s += "transitionThreshold="+transitionThreshold+"\n";
        s += "punishmentThreshold="+punishmentThreshold+"\n";
        
        return s;
    }
}