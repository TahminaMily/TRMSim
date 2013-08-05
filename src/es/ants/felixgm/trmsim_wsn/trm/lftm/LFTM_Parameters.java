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

package es.ants.felixgm.trmsim_wsn.trm.lftm;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.DefuzzifierCenterOfGravity;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRule;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRuleSet;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.LinguisticTerm;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.MembershipFunctionTrapezoidal;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.MembershipFunctionTriangular;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.Variable;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class represents the set of parameters' values of {@link LFTM}.</p>
 * <p>A LFTM parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # LFTM parameters file
 *    ####################################
 *    phi=0.01
 *    rho=0.87
 *    q0=0.45
 *    numAnts=0.35
 *    numIterations=0.59
 *    alpha=1.0
 *    beta=1.0
 *    initialPheromone=0.85
 *    pathLengthFactor=0.71
 *    transitionThreshold=0.66
 *    punishmentThreshold=0.48
 *    U_MIN=0.0
 *    U_MAX=1.0
 *    VH_A=0.7
 *    VH_B=0.9
 *    VH_C=1.0
 *    VH_D=1.0
 *    H_A=0.55
 *    H_B=0.7
 *    H_C=0.8
 *    H_D=0.9
 *    M_A=0.3
 *    M_B=0.45
 *    M_C=0.55
 *    M_D=0.7
 *    L_A=0.1
 *    L_B=0.2
 *    L_C=0.3
 *    L_D=0.45
 *    VL_A=0.0
 *    VL_B=0.0
 *    VL_C=0.1
 *    VL_D=0.3
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/LFTMparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class LFTM_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/lftm/LFTMparameters.txt";

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

    private static double U_MIN;
    private static double U_MAX;
    private static Collection<LinguisticTerm> linguisticTerms;

    /**
     * Creates a new instance of LFTM_Parameters setting them to their default values
     */
    public LFTM_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# LFTM parameters file\n";
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

        U_MIN = 0.0;
        U_MAX = 1.0;
        linguisticTerms = getDefaultLinguisticTerms();
    }

    /**
     * Creates a new instance of LFTM_Parameters from a given parameters file name
     * @param fileName LFTM parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public LFTM_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# LFTM parameters file\n";
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

        U_MIN = getDoubleParameter("U_MIN");
        U_MAX = getDoubleParameter("U_MAX");
        linguisticTerms  = loadLinguisticTerms();
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
     * Returns U_MIN parameter value
     * @return U_MIN parameter value
     */
    public static double get_U_MIN() { return U_MIN; }

    /**
     * Returns U_MAX parameter value
     * @return U_MAX parameter value
     */
    public static double get_U_MAX() { return U_MAX; }

    /**
     * Returns linguisticTerms parameter value
     * @return linguisticTerms parameter value
     */
    public static Collection<LinguisticTerm> get_linguisticTerms() { return linguisticTerms; }


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

    /**
     * Sets a new U_MIN parameter value
     * @param _U_MIN New U_MIN parameter value
     */
    public void set_U_MIN(double _U_MIN) {
        U_MIN = _U_MIN;
        setDoubleParameter("U_MIN",U_MIN);
    }

    /**
     * Sets a new U_MAX parameter value
     * @param _U_MAX New U_MAX parameter value
     */
    public void set_U_MAX(double _U_MAX) { 
        U_MAX = _U_MAX;
        setDoubleParameter("U_MAX",U_MAX);
    }

    /**
     * Sets a new linguisticTerms parameter value
     * @param _linguisticTerms New linguisticTerms parameter value
     */
    public void set_linguisticTerms(Collection<LinguisticTerm> _linguisticTerms) {
        linguisticTerms = _linguisticTerms;
        String paramA = "";
        String paramB = "";
        String paramC = "";
        String paramD = "";
        for (LinguisticTerm linguisticTerm : linguisticTerms) {
            String termName = linguisticTerm.getTermName();
            if (termName.equals("Very High")) {
                paramA = "VH_A";
                paramB = "VH_B";
                paramC = "VH_C";
                paramD = "VH_C";
            } else if (termName.equals("High")) {
                paramA = "H_A";
                paramB = "H_B";
                paramC = "H_C";
                paramD = "H_C";
            } else if (termName.equals("Medium")) {
                paramA = "M_A";
                paramB = "M_B";
                paramC = "M_C";
                paramD = "M_C";
            } else if (termName.equals("Low")) {
                paramA = "L_A";
                paramB = "L_B";
                paramC = "L_C";
                paramD = "L_C";
            } else if (termName.equals("Very Low")) {
                paramA = "VL_A";
                paramB = "VL_B";
                paramC = "VL_C";
                paramD = "VL_C";
            }

            if (linguisticTerm.get_membershipFunction() instanceof MembershipFunctionTriangular) {
                setDoubleParameter(paramA,((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_min());
                setDoubleParameter(paramB,((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_med());
                setDoubleParameter(paramC,((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_max());
            } else if (linguisticTerm.get_membershipFunction() instanceof MembershipFunctionTrapezoidal) {
                setDoubleParameter(paramA,((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_min());
                setDoubleParameter(paramB,((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_medLow());
                setDoubleParameter(paramC,((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_medHigh());
                setDoubleParameter(paramD,((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_max());
            }
        }
    }

    /**
     * This method loads the linguisticTerm parameter from the parameters file
     * @return The linguisticTerm parameter loaded from the parameters file
     */
    private Collection<LinguisticTerm> loadLinguisticTerms() {
        Vector<LinguisticTerm> _linguisticTerms = new Vector<LinguisticTerm>();
        LinguisticTerm veryHigh = null;
        LinguisticTerm high = null;
        LinguisticTerm medium = null;
        LinguisticTerm low = null;
        LinguisticTerm veryLow = null;
        
        double veryHighAParameter = getDoubleParameter("VH_A");
        double veryHighBParameter = getDoubleParameter("VH_B");
        double veryHighCParameter = getDoubleParameter("VH_C");
        try {
            double veryHighDParameter = getDoubleParameter("VH_D");
            veryHigh = new LinguisticTerm("Very High",
                    new MembershipFunctionTrapezoidal(veryHighAParameter*U_MAX,veryHighBParameter*U_MAX,veryHighCParameter*U_MAX,veryHighDParameter*U_MAX));
        } catch (Exception ex) {
            veryHigh = new LinguisticTerm("Very High",
                    new MembershipFunctionTriangular(veryHighAParameter*U_MAX,veryHighBParameter*U_MAX,veryHighCParameter*U_MAX));
        } finally {
            veryHigh.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            _linguisticTerms.add(veryHigh);
        }

        double highAParameter = getDoubleParameter("H_A");
        double highBParameter = getDoubleParameter("H_B");
        double highCParameter = getDoubleParameter("H_C");
        try {
            double highDParameter = getDoubleParameter("H_D");
            high = new LinguisticTerm("High",
                    new MembershipFunctionTrapezoidal(highAParameter*U_MAX,highBParameter*U_MAX,highCParameter*U_MAX,highDParameter*U_MAX));
        } catch (Exception ex) {
            high = new LinguisticTerm("High",
                    new MembershipFunctionTriangular(highAParameter*U_MAX,highBParameter*U_MAX,highCParameter*U_MAX));
        } finally {
            high.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            _linguisticTerms.add(high);
        }

        double mediumAParameter = getDoubleParameter("M_A");
        double mediumBParameter = getDoubleParameter("M_B");
        double mediumCParameter = getDoubleParameter("M_C");
        try {
            double mediumDParameter = getDoubleParameter("M_D");
            medium = new LinguisticTerm("Medium",
                    new MembershipFunctionTrapezoidal(mediumAParameter*U_MAX,mediumBParameter*U_MAX,mediumCParameter*U_MAX,mediumDParameter*U_MAX));
        } catch (Exception ex) {
            medium = new LinguisticTerm("Medium",
                    new MembershipFunctionTriangular(mediumAParameter*U_MAX,mediumBParameter*U_MAX,mediumCParameter*U_MAX));
        } finally {
            medium.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            _linguisticTerms.add(medium);
        }
        
        double lowAParameter = getDoubleParameter("L_A");
        double lowBParameter = getDoubleParameter("L_B");
        double lowCParameter = getDoubleParameter("L_C");
        try {
            double lowDParameter = getDoubleParameter("L_D");
            low = new LinguisticTerm("Low",
                    new MembershipFunctionTrapezoidal(lowAParameter*U_MAX,lowBParameter*U_MAX,lowCParameter*U_MAX,lowDParameter*U_MAX));
        } catch (Exception ex) {
            low = new LinguisticTerm("Low",
                    new MembershipFunctionTriangular(lowAParameter*U_MAX,lowBParameter*U_MAX,lowCParameter*U_MAX));
        } finally {
            low.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            _linguisticTerms.add(low);
        }
        
        double veryLowAParameter = getDoubleParameter("VL_A");
        double veryLowBParameter = getDoubleParameter("VL_B");
        double veryLowCParameter = getDoubleParameter("VL_C");
        try {
            double veryLowDParameter = getDoubleParameter("VL_D");
            veryLow = new LinguisticTerm("Very Low",
                    new MembershipFunctionTrapezoidal(veryLowAParameter*U_MAX,veryLowBParameter*U_MAX,veryLowCParameter*U_MAX,veryLowDParameter*U_MAX));
        } catch (Exception ex) {
            veryLow = new LinguisticTerm("Very Low",
                    new MembershipFunctionTriangular(veryLowAParameter*U_MAX,veryLowBParameter*U_MAX,veryLowCParameter*U_MAX));
        } finally {
            veryLow.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            _linguisticTerms.add(veryLow);
        }
        
        return _linguisticTerms;
    }

    /**
     * This method returns a default set of linguistic terms
     * @return A default set of linguistic terms
     */
    private static Collection<LinguisticTerm> getDefaultLinguisticTerms() {
        if (linguisticTerms == null) {
            linguisticTerms = new Vector<LinguisticTerm>();
            LinguisticTerm veryLow = new LinguisticTerm("Very Low",
                    new MembershipFunctionTrapezoidal(U_MIN,U_MIN,0.15*U_MAX,0.2*U_MAX));
            LinguisticTerm low = new LinguisticTerm("Low",
                    new MembershipFunctionTrapezoidal(0.2*U_MAX,0.25*U_MAX,0.35*U_MAX,0.4*U_MAX));
            LinguisticTerm medium = new LinguisticTerm("Medium",
                    new MembershipFunctionTrapezoidal(0.4*U_MAX,0.45*U_MAX,0.55*U_MAX,0.6*U_MAX));
            LinguisticTerm high = new LinguisticTerm("High",
                    new MembershipFunctionTrapezoidal(0.6*U_MAX,0.65*U_MAX,0.75*U_MAX,0.8*U_MAX));
            LinguisticTerm veryHigh = new LinguisticTerm("Very High",
                    new MembershipFunctionTrapezoidal(0.8*U_MAX,0.85*U_MAX,U_MAX,U_MAX));
            

            veryLow.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            low.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            medium.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            high.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));
            veryHigh.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN,U_MAX));

            linguisticTerms.add(veryLow);
            linguisticTerms.add(low);
            linguisticTerms.add(medium);
            linguisticTerms.add(high);
            linguisticTerms.add(veryHigh);
        }
        return linguisticTerms;
    }

    /**
     * This method returns the set of fuzzy rules corresponding to the comparison
     * of those service attributes which improve when they increase (for example, the quality)
     * @param service1Property Service 1's property or attribute (quality...)
     * @param service2Property Service 2's property or attribute (quality...)
     * @param servicesPropertiesComparisson Output fuzzy set that will contain the actual
     * comparison between the two given service properties or attributes
     * @return The set of fuzzy rules corresponding to the comparison of those service
     * attributes which improve when they increase (for example, the price)
     */
    public static FuzzyRuleSet getFRSServicesAttributesPositive(Variable service1Property,
            Variable service2Property, Variable servicesPropertiesComparisson) {
        FuzzyRuleSet frsServicesAttributesPositive = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++) {
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false,service1Property,sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false,service2Property,sortedLT.get(j).getTermName());
                if (i == j)
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Medium");
                if ((j > i) && (j < i+2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Low");
                if ((j > i) && (j >= i+2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Very Low");
                if ((j < i) && (j > i-2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"High");
                if ((j < i) && (j <= i-2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Very High");

                frsServicesAttributesPositive.add(fr[i][j]);
            }
        }

        return frsServicesAttributesPositive;
    }

    /**
     * This method returns the set of fuzzy rules corresponding to the comparison
     * of those service attributes which improve when they decrease (for example, the price)
     * @param service1Property Service 1's property or attribute (price, delivery time...)
     * @param service2Property Service 2's property or attribute (price, delivery time...)
     * @param servicesPropertiesComparisson Output fuzzy set that will contain the actual
     * comparison between the two given service properties or attributes
     * @return The set of fuzzy rules corresponding to the comparison of those service
     * attributes which improve when they decrease (for example, the price)
     */
    public static FuzzyRuleSet getFRSServicesAttributesNegative(Variable service1Property,
            Variable service2Property, Variable servicesPropertiesComparisson) {

        FuzzyRuleSet frsServicesAttributesNegative = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++)
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false,service1Property,sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false,service2Property,sortedLT.get(j).getTermName());
                if (i == j)
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Medium");
                if ((j > i) && (j < i+2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"High");
                if ((j > i) && (j >= i+2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Very High");
                if ((j < i) && (j > i-2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Low");
                if ((j < i) && (j <= i-2))
                    fr[i][j].addConsequent(false,servicesPropertiesComparisson,"Very Low");

                frsServicesAttributesNegative.add(fr[i][j]);
            }
        return frsServicesAttributesNegative;
    }

    /**
     * This method returns the set of fuzzy rules which will determine the value of
     * the attribute of the service provided by a server, for those service attributes
     * which improve when they increase (for example, the quality)
     * @param serverGoodness Goodness of the server providing the service
     * @param offeredServiceProperty Property or attribute of the service offered by the server
     * @param givenServiceProperty Output fuzzy set that will contain the value of the
     * property or attribute of the service actually provided by the server
     * @return The set of fuzzy rules which will determine the value of
     * the attribute of the service provided by a server, for those service attributes
     * which improve when they increase (for example, the quality)
     */
    public static FuzzyRuleSet getFRSServerGoodnessPositive(Variable serverGoodness,
            Variable offeredServiceProperty, Variable givenServiceProperty) {

        FuzzyRuleSet frsServerGoodnessPositive = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++)
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false,serverGoodness,sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false,offeredServiceProperty,sortedLT.get(j).getTermName());
                if (j < 3-i)
                    fr[i][j].addConsequent(false,givenServiceProperty,"Very Low");
                if ((j >= 3-i) && (j < 4-i))
                    fr[i][j].addConsequent(false,givenServiceProperty,"Low");
                if (j == 4-i)
                    fr[i][j].addConsequent(false,givenServiceProperty,"Medium");
                if ((j > 4-i) && (j < 4-i+2))
                    fr[i][j].addConsequent(false,givenServiceProperty,"High");
                if (j >= 4-i+2)
                    fr[i][j].addConsequent(false,givenServiceProperty,"Very High");
                frsServerGoodnessPositive.add(fr[i][j]);
            }

        return frsServerGoodnessPositive;
    }

    /**
     * This method returns the set of fuzzy rules which will determine the value of
     * the attribute of the service provided by a server, for those service attributes
     * which improve when they decrease (for example, the price)
     * @param serverGoodness Goodness of the server providing the service
     * @param offeredServiceProperty Property or attribute of the service offered by the server
     * @param givenServiceProperty Output fuzzy set that will contain the value of the
     * property or attribute of the service actually provided by the server
     * @return The set of fuzzy rules which will determine the value of
     * the attribute of the service provided by a server, for those service attributes
     * which improve when they decrease (for example, the price)
     */
    public static FuzzyRuleSet getFRSServerGoodnessNegative(Variable serverGoodness,
            Variable offeredServiceProperty, Variable givenServiceProperty) {

        FuzzyRuleSet frsServerGoodnessNegative = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++)
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false,serverGoodness,sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false,offeredServiceProperty,sortedLT.get(j).getTermName());
                if (i == j)
                    fr[i][j].addConsequent(false,givenServiceProperty,"Medium");
                if ((j > i) && (j < i+2))
                    fr[i][j].addConsequent(false,givenServiceProperty,"High");
                if ((j > i) && (j >= i+2))
                    fr[i][j].addConsequent(false,givenServiceProperty,"Very High");
                if ((j < i) && (j > i-2))
                    fr[i][j].addConsequent(false,givenServiceProperty,"Low");
                if ((j < i) && (j <= i-2))
                    fr[i][j].addConsequent(false,givenServiceProperty,"Very Low");
                frsServerGoodnessNegative.add(fr[i][j]);
            }

        return frsServerGoodnessNegative;
    }

    /**
     * This method returns the set of fuzzy rules which will determine the satisfaction
     * of a client with the actually received service
     * @param clientConformity Client's conformity
     * @param servicesComparison Comparison between the requested service (the one
     * initially offered by the server) and the actually received one
     * @param clientSatisfaction Output fuzzy set that will contain the satisfaction
     * of a client with the actually received service
     * @return The set of fuzzy rules which determine the satisfaction
     * of a client with the actually received service
     */
    public static FuzzyRuleSet getFRSClientSatisfaction(Variable clientConformity, Variable servicesComparison,
            Variable clientSatisfaction) {

        FuzzyRuleSet frsSatisfaction = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++)
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false, clientConformity, sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false, servicesComparison, sortedLT.get(j).getTermName());
                if (j < 3-i)
                    fr[i][j].addConsequent(false,clientSatisfaction,"Very Low");
                if ((j >= 3-i) && (j < 4-i))
                    fr[i][j].addConsequent(false,clientSatisfaction,"Low");
                if (j == 4-i)
                    fr[i][j].addConsequent(false,clientSatisfaction,"Medium");
                if ((j > 4-i) && (j < 4-i+2))
                    fr[i][j].addConsequent(false,clientSatisfaction,"High");
                if (j >= 4-i+2)
                    fr[i][j].addConsequent(false,clientSatisfaction,"Very High");


                frsSatisfaction.add(fr[i][j]);
            }

        return frsSatisfaction;
    }

    /**
     * This method returns the set of fuzzy rules which will determine the
     * punishment or reward degree to be applied over the selected service provider
     * @param clientGoodness Client's goodness
     * @param clientSatisfaction Satisfaction of a client with the actually received service
     * @param punishmentReward Output fuzzy set that will contain the punishment
     * or reward degree to be applied over the selected service provider
     * @return The set of fuzzy rules which will determine the
     * punishment or reward degree to be applied over the selected service provider
     */
    public static FuzzyRuleSet getFRSPunishmentReward(Variable clientGoodness, Variable clientSatisfaction,
            Variable punishmentReward) {
        FuzzyRuleSet frsPunishment = new FuzzyRuleSet();
        FuzzyRule fr[][] = new FuzzyRule[5][5];

        Vector<LinguisticTerm> sortedLT = (Vector<LinguisticTerm>)linguisticTerms;
        for (int i = 0; i < fr.length; i++)
            for (int j = 0; j < fr[i].length; j++) {
                fr[i][j] = new FuzzyRule();
                fr[i][j].addAntecedent(false, clientGoodness, sortedLT.get(i).getTermName());
                fr[i][j].addAntecedent(false, clientSatisfaction, sortedLT.get(j).getTermName());
                if (j < 3-i)
                    fr[i][j].addConsequent(false,punishmentReward,"Very High");
                if ((j >= 3-i) && (j < 4-i))
                    fr[i][j].addConsequent(false,punishmentReward,"High");
                if (j == 4-i)
                    fr[i][j].addConsequent(false,punishmentReward,"Medium");
                if ((j > 4-i) && (j < 4-i+2))
                    fr[i][j].addConsequent(false,punishmentReward,"Low");
                if (j >= 4-i+2)
                    fr[i][j].addConsequent(false,punishmentReward,"Very Low");

                frsPunishment.add(fr[i][j]);
            }

        return frsPunishment;
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
        s += "U_MIN="+U_MIN+"\n";
        s += "U_MAX="+U_MAX+"\n";

        for (LinguisticTerm linguisticTerm : linguisticTerms) {
            String label = "";

            if (linguisticTerm.getTermName().equals("Very High"))
                label = "VH_";
            else if (linguisticTerm.getTermName().equals("High"))
                label = "H_";
            else if (linguisticTerm.getTermName().equals("Medium"))
                label = "M_";
            else if (linguisticTerm.getTermName().equals("Low"))
                label = "L_";
            else if (linguisticTerm.getTermName().equals("Very Low"))
                label = "VL_";

            if (linguisticTerm.get_membershipFunction() instanceof MembershipFunctionTriangular) {
                s += label+"A="+((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_min()+"\n";
                s += label+"B="+((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_med()+"\n";
                s += label+"C="+((MembershipFunctionTriangular)linguisticTerm.get_membershipFunction()).get_max()+"\n";
            } else if (linguisticTerm.get_membershipFunction() instanceof MembershipFunctionTrapezoidal) {
                s += label+"A="+((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_min()+"\n";
                s += label+"B="+((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_medLow()+"\n";
                s += label+"C="+((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_medHigh()+"\n";
                s += label+"D="+((MembershipFunctionTrapezoidal)linguisticTerm.get_membershipFunction()).get_max()+"\n";
            }
        }
        return s;
    }
}