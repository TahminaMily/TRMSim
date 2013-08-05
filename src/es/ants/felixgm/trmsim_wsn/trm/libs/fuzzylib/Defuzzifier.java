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
 * <p>This class models a generic Defuzzifier used to get a crisp value from a fuzzy set</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public abstract class Defuzzifier {

    /** Default number of points for 'values[]' */
    public static int DEFAULT_NUMBER_OF_POINTS = 1000;

    /** Where function ends */
    protected double max;
    /** Where function begins */
    protected double min;
    /**
     * Step size between each element in 'values[]'
     * 			stepSize = (max - min) / values.length
     */
    protected double stepSize;
    /**
     * Function values: A generic continuous function
     * 			y = f(x)
     * where x : [min, max]
     * Values are stored in 'values[]' array.
     * Array's index is calculated as:
     * 			index = (x - min) / (max - min) * (values.length)
     */
    protected double values[];

    protected RuleAggregationMethod ruleAggregationMethod;

    /** Constructor */
    public Defuzzifier(double universeMin, double universeMax) {
        ruleAggregationMethod = new RuleAggregationMethodMax();
        init(universeMin, universeMax, DEFAULT_NUMBER_OF_POINTS);
    }

    public abstract double defuzzify();
    public abstract Defuzzifier newDefuzzifierContinuous();

    public Defuzzifier aggregate(Defuzzifier deffuzifier) {
        try {
            Defuzzifier deffuzifierAux = (Defuzzifier)deffuzifier;
            if (deffuzifierAux.getMin() != min)
                throw new RuntimeException("Minimum values do not match");
            if (deffuzifierAux.getMax() != max)
                throw new RuntimeException("Maximum values do not match");

            Defuzzifier deffuzifierContinuous = newDefuzzifierContinuous();
            int ratioLength;
            if (getLength() < deffuzifierAux.getLength()) {
                deffuzifierContinuous.init(deffuzifierAux.getMin(),
                                           deffuzifierAux.getMax(),
                                           deffuzifierAux.getLength());
                ratioLength = deffuzifierAux.getLength()/getLength();
                for (int i = 0; i < deffuzifierContinuous.getLength(); i++) {
                    deffuzifierContinuous.setValue(i,ruleAggregationMethod.aggregate(
                            getValue(i/ratioLength),
                            deffuzifierAux.getValue(i)));
                }
            } else {
                deffuzifierContinuous.init(getMin(),
                                           getMax(),
                                           getLength());
                ratioLength = getLength()/deffuzifierAux.getLength();
                for (int i = 0; i < deffuzifierContinuous.getLength(); i++) {
                    deffuzifierContinuous.setValue(i,ruleAggregationMethod.aggregate(
                            getValue(i),
                            deffuzifierAux.getValue(i/ratioLength)));
                }
            }

            return deffuzifierContinuous;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void init(double min, double max, int numberOfPoints) {
        values = new double[numberOfPoints];
        this.min = min;
        this.max = max;
        stepSize = (max - min) / numberOfPoints;
        reset();
    }

    private void reset() {
        if (values != null)
            for( int i = 0; i < values.length; i++ )
                values[i] = 0.0;
    }

    public int getLength() {
        if (values != null)
            return values.length;
        return 0;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    public double getStepSize() {
        return stepSize;
    }

    public double getValue(int index) {
        return values[index];
    }

    public void setValue(int index, double value) {
        values[index] = value;
    }

    public RuleAggregationMethod getRuleAggregationMethod() {
        return ruleAggregationMethod;
    }

    public void setRuleAggregationMethod(RuleAggregationMethod ruleAggregationMethod) {
        this.ruleAggregationMethod = ruleAggregationMethod;
    }
}