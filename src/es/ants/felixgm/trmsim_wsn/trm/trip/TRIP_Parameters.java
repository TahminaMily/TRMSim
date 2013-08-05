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

package es.ants.felixgm.trmsim_wsn.trm.trip;

import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.LinguisticTerm;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.MembershipFunctionGaussian;

/**
 * <p>This class represents the set of parameters' values of {@link TRIP}.</p>
 * <p>A TRIP parameters file has the following structure:</p>
 * <pre>
 *    ####################################
 *    # TRIP parameters file
 *    ####################################
 *    initialAlpha=0.0
 *    initialBeta=0.5
 *    initialGamma=0.5
 *    rsuPercentage=0.05
 *    notTrustFuzzySetMean=0.0
 *    notTrustFuzzySetStDev=0.2
 *    moreOrLessTrustFuzzySetMean=0.5
 *    moreOrLessTrustFuzzySetStDev=0.2
 *    trustFuzzySetMean=1.0
 *    trustFuzzySetStDev=0.2
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/TRIPparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.5
 */
public class TRIP_Parameters extends TRMParameters {
    /** Default parameters file name */
    public static final String defaultParametersFileName = "trmodels/trip/TRIPparameters.txt";

    private double initialAlpha;
    private double initialBeta;
    private double initialGamma;

    private double rsuPercentage;
    private int numHopsQueriedRecommenders;

    private double notTrustFuzzySetMean;
    private double notTrustFuzzySetStDev;
    private LinguisticTerm notTrustFuzzySet;

    private double moreOrLessTrustFuzzySetMean;
    private double moreOrLessTrustFuzzySetStDev;
    private LinguisticTerm moreOrLessTrustFuzzySet;

    private double trustFuzzySetMean;
    private double trustFuzzySetStDev;
    private LinguisticTerm trustFuzzySet;

    /**
     * Creates a new instance of TRIP_Parameters setting them to their default values
     */
    public TRIP_Parameters() {
        super();
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# TRIP parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        initialAlpha = 0.0;
        initialBeta = 0.5;
        initialGamma = 0.5;

        rsuPercentage = 0.05;
        numHopsQueriedRecommenders = 2;

        notTrustFuzzySetMean = 0.3;
        notTrustFuzzySetStDev = 0.2;
        notTrustFuzzySet = new LinguisticTerm("Not Trust",
                    new MembershipFunctionGaussian(notTrustFuzzySetMean,notTrustFuzzySetStDev));

        moreOrLessTrustFuzzySetMean = 0.5;
        moreOrLessTrustFuzzySetStDev = 0.2;
        moreOrLessTrustFuzzySet = new LinguisticTerm("+/- Trust",
                    new MembershipFunctionGaussian(moreOrLessTrustFuzzySetMean,moreOrLessTrustFuzzySetStDev));

        trustFuzzySetMean = 0.7;
        trustFuzzySetStDev = 0.2;
        trustFuzzySet = new LinguisticTerm("Trust",
                    new MembershipFunctionGaussian(trustFuzzySetMean,trustFuzzySetStDev));
    }

    /**
     * Creates a new instance of TRIP_Parameters from a given parameters file name
     * @param fileName TRIP parameters file name
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public TRIP_Parameters(String fileName) throws Exception {
        super(fileName);
        parametersFileHeader = "####################################\n";
        parametersFileHeader += "# TRIP parameters file\n";
        parametersFileHeader += "# "+(new java.util.Date())+"\n";
        parametersFileHeader += "####################################\n";

        initialAlpha = getDoubleParameter("initialAlpha");
        initialBeta = getDoubleParameter("initialBeta");
        initialGamma = getDoubleParameter("initialGamma");

        rsuPercentage = getDoubleParameter("rsuPercentage");
        numHopsQueriedRecommenders = getIntegerParameter("numHopsQueriedRecommenders");

        notTrustFuzzySetMean = getDoubleParameter("notTrustFuzzySetMean");
        notTrustFuzzySetStDev = getDoubleParameter("notTrustFuzzySetStDev");
        notTrustFuzzySet = new LinguisticTerm("Not Trust",
                    new MembershipFunctionGaussian(notTrustFuzzySetMean,notTrustFuzzySetStDev));

        moreOrLessTrustFuzzySetMean = getDoubleParameter("moreOrLessTrustFuzzySetMean");
        moreOrLessTrustFuzzySetStDev = getDoubleParameter("moreOrLessTrustFuzzySetStDev");
        moreOrLessTrustFuzzySet = new LinguisticTerm("+/- Trust",
                    new MembershipFunctionGaussian(moreOrLessTrustFuzzySetMean,moreOrLessTrustFuzzySetStDev));

        trustFuzzySetMean = getDoubleParameter("trustFuzzySetMean");
        trustFuzzySetStDev = getDoubleParameter("trustFuzzySetStDev");
        trustFuzzySet = new LinguisticTerm("Trust",
                    new MembershipFunctionGaussian(trustFuzzySetMean,trustFuzzySetStDev));
    }

    /**
     * Returns initialAlpha parameter value
     * @return initialAlpha parameter value
     */
    public double get_initialAlpha() { return initialAlpha; }

    /**
     * Returns initialBeta parameter value
     * @return initialBeta parameter value
     */
    public double get_initialBeta() { return initialBeta; }

    /**
     * Returns initialGamma parameter value
     * @return initialGamma parameter value
     */
    public double get_initialGamma() { return initialGamma; }

    /**
     * Returns rsuPercentage parameter value
     * @return rsuPercentage parameter value
     */
    public double get_rsuPercentage() { return rsuPercentage; }

    /**
     * Returns numHopsQueriedRecommenders parameter value
     * @return numHopsQueriedRecommenders parameter value
     */
    public int get_numHopsQueriedRecommenders() { return numHopsQueriedRecommenders; }

    /**
     * Returns notTrustFuzzySetMean parameter value
     * @return notTrustFuzzySetMean parameter value
     */
    public double get_notTrustFuzzySetMean() { return notTrustFuzzySetMean; }

    /**
     * Returns notTrustFuzzySetStDev parameter value
     * @return notTrustFuzzySetStDev parameter value
     */
    public double get_notTrustFuzzySetStDev() { return notTrustFuzzySetStDev; }

    /**
     * Returns moreOrLessTrustFuzzySetMean parameter value
     * @return moreOrLessTrustFuzzySetMean parameter value
     */
    public double get_moreOrLessTrustFuzzySetMean() { return moreOrLessTrustFuzzySetMean; }

    /**
     * Returns moreOrLessTrustFuzzySetStDev parameter value
     * @return moreOrLessTrustFuzzySetStDev parameter value
     */
    public double get_moreOrLessTrustFuzzySetStDev() { return moreOrLessTrustFuzzySetStDev; }

    /**
     * Returns trustFuzzySetMean parameter value
     * @return trustFuzzySetMean parameter value
     */
    public double get_trustFuzzySetMean() { return trustFuzzySetMean; }

    /**
     * Returns trustFuzzySetStDev parameter value
     * @return trustFuzzySetStDev parameter value
     */
    public double get_trustFuzzySetStDev() { return trustFuzzySetStDev; }


    /**
     * Sets a new initialAlpha parameter value
     * @param initialAlpha New initialAlpha parameter value
     */
    public void set_initialAlpha(double initialAlpha) {
        this.initialAlpha = initialAlpha;
        setDoubleParameter("initialAlpha",initialAlpha);
    }

    /**
     * Sets a new initialBeta parameter value
     * @param initialBeta New initialBeta parameter value
     */
    public void set_initialBeta(double initialBeta) {
        this.initialBeta = initialBeta;
        setDoubleParameter("initialBeta",initialBeta);
    }

    /**
     * Sets a new initialGamma parameter value
     * @param initialGamma New initialGamma parameter value
     */
    public void set_initialGamma(double initialGamma) {
        this.initialGamma = initialGamma;
        setDoubleParameter("initialGamma",initialGamma);
    }

    /**
     * Sets a new rsuPercentage parameter value
     * @param rsuPercentage New rsuPercentage parameter value
     */
    public void set_rsuPercentage(double rsuPercentage) {
        this.rsuPercentage = rsuPercentage;
        setDoubleParameter("rsuPercentage",rsuPercentage);
    }

    /**
     * Sets a new numHopsQueriedRecommenders parameter value
     * @param numHopsQueriedRecommenders New numHopsQueriedRecommenders parameter value
     */
    public void set_numHopsQueriedRecommenders(int numHopsQueriedRecommenders) {
        this.numHopsQueriedRecommenders = numHopsQueriedRecommenders;
        setIntegerParameter("numHopsQueriedRecommenders",numHopsQueriedRecommenders);
    }

    /**
     * Sets a new notTrustFuzzySetMean parameter value
     * @param notTrustFuzzySetMean New notTrustFuzzySetMean parameter value
     */
    public void set_notTrustFuzzySetMean(double notTrustFuzzySetMean) {
        this.notTrustFuzzySetMean = notTrustFuzzySetMean;
        setDoubleParameter("notTrustFuzzySetMean",notTrustFuzzySetMean);
        ((MembershipFunctionGaussian)notTrustFuzzySet.get_membershipFunction()).set_mean(notTrustFuzzySetMean);
    }

    /**
     * Sets a new notTrustFuzzySetStDev parameter value
     * @param notTrustFuzzySetStDev New notTrustFuzzySetStDev parameter value
     */
    public void set_notTrustFuzzySetStDev(double notTrustFuzzySetStDev) {
        this.notTrustFuzzySetStDev = notTrustFuzzySetStDev;
        setDoubleParameter("notTrustFuzzySetStDev",notTrustFuzzySetStDev);
        ((MembershipFunctionGaussian)notTrustFuzzySet.get_membershipFunction()).set_stDev(notTrustFuzzySetStDev);
    }

    /**
     * Sets a new moreOrLessTrustFuzzySetMean parameter value
     * @param moreOrLessTrustFuzzySetMean New moreOrLessTrustFuzzySetMean parameter value
     */
    public void set_moreOrLessTrustFuzzySetMean(double moreOrLessTrustFuzzySetMean) {
        this.moreOrLessTrustFuzzySetMean = moreOrLessTrustFuzzySetMean;
        setDoubleParameter("moreOrLessTrustFuzzySetMean",moreOrLessTrustFuzzySetMean);
        ((MembershipFunctionGaussian)moreOrLessTrustFuzzySet.get_membershipFunction()).set_mean(moreOrLessTrustFuzzySetMean);
    }

    /**
     * Sets a new moreOrLessTrustFuzzySetStDev parameter value
     * @param moreOrLessTrustFuzzySetStDev New moreOrLessTrustFuzzySetStDev parameter value
     */
    public void set_moreOrLessTrustFuzzySetStDev(double moreOrLessTrustFuzzySetStDev) {
        this.moreOrLessTrustFuzzySetStDev = moreOrLessTrustFuzzySetStDev;
        setDoubleParameter("moreOrLessTrustFuzzySetStDev",moreOrLessTrustFuzzySetStDev);
        ((MembershipFunctionGaussian)moreOrLessTrustFuzzySet.get_membershipFunction()).set_stDev(moreOrLessTrustFuzzySetStDev);
    }

    /**
     * Sets a new trustFuzzySetMean parameter value
     * @param trustFuzzySetMean New trustFuzzySetMean parameter value
     */
    public void set_trustFuzzySetMean(double trustFuzzySetMean) {
        this.trustFuzzySetMean = trustFuzzySetMean;
        setDoubleParameter("trustFuzzySetMean",trustFuzzySetMean);
        ((MembershipFunctionGaussian)trustFuzzySet.get_membershipFunction()).set_mean(trustFuzzySetMean);
    }

    /**
     * Sets a new trustFuzzySetStDev parameter value
     * @param trustFuzzySetStDev New trustFuzzySetStDev parameter value
     */
    public void set_trustFuzzySetStDev(double trustFuzzySetStDev) {
        this.trustFuzzySetStDev = trustFuzzySetStDev;
        setDoubleParameter("trustFuzzySetStDev",trustFuzzySetStDev);
        ((MembershipFunctionGaussian)trustFuzzySet.get_membershipFunction()).set_stDev(trustFuzzySetStDev);
    }

    public LinguisticTerm get_notTrustFuzzySet() { return notTrustFuzzySet; }
    public LinguisticTerm get_moreOrLessTrustFuzzySet() { return moreOrLessTrustFuzzySet; }
    public LinguisticTerm get_trustFuzzySet() { return trustFuzzySet; }

    @Override
    public String toString() {
        String s = parametersFileHeader;

        s += "initialAlpha="+initialAlpha+"\n";
        s += "initialBeta="+initialBeta+"\n";
        s += "initialGamma="+initialGamma+"\n";

        s += "rsuPercentage="+rsuPercentage+"\n";
        s += "numHopsQueriedRecommenders="+numHopsQueriedRecommenders+"\n";

        s += "notTrustFuzzySetMean="+notTrustFuzzySetMean+"\n";
        s += "notTrustFuzzySetStDev="+notTrustFuzzySetStDev+"\n";

        s += "moreOrLessTrustFuzzySetMean="+moreOrLessTrustFuzzySetMean+"\n";
        s += "moreOrLessTrustFuzzySetStDev="+moreOrLessTrustFuzzySetStDev+"\n";

        s += "trustFuzzySetMean="+trustFuzzySetMean+"\n";
        s += "trustFuzzySetStDev="+trustFuzzySetStDev+"\n";

        return s;
    }
}