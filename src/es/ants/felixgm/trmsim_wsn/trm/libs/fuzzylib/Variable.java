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

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * <p>This class models a Fuzzy Variable represented as a Fuzzy Set</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class Variable {
    /** Variable name */
    protected String name;
    /** Terms for this variable */
    protected HashMap<String, LinguisticTerm> linguisticTerms;
    /** Defuzzifier class */
    protected Defuzzifier defuzzifier;
    /** Universe max (range max) */
    protected double universeMax;
    /** Universe min (range min) */
    protected double universeMin;
    /** Variable's value */
    protected double value;

    public Variable(String name, Collection<LinguisticTerm> linguisticTerms, Defuzzifier defuzzifier) {
        if( name == null )
            throw new RuntimeException("Variable's name can't be null");
        if( universeMax < universeMin )
            throw new RuntimeException("Parameter error in variable \'" + name + "\' universeMax < universeMin");

        this.name = name;
        this.linguisticTerms = new HashMap<String, LinguisticTerm>();
        if (linguisticTerms != null)
            for (LinguisticTerm lt : linguisticTerms)
                this.linguisticTerms.put(lt.getTermName(), lt);
        this.defuzzifier = defuzzifier;
        this.universeMin = defuzzifier.getMin();
        this.universeMax = defuzzifier.getMax();
        value = Double.NaN;
    }

    public Variable(String name, Collection<LinguisticTerm> linguisticTerms, double universeMin, double universeMax) {
        if( name == null )
            throw new RuntimeException("Variable's name can't be null");
        if( universeMax < universeMin )
            throw new RuntimeException("Parameter error in variable \'" + name + "\' universeMax < universeMin");

        this.name = name;
        this.linguisticTerms = new HashMap<String, LinguisticTerm>();
        if (linguisticTerms != null)
            for (LinguisticTerm lt : linguisticTerms)
                this.linguisticTerms.put(lt.getTermName(), lt);
        this.defuzzifier = null;
        this.universeMin = universeMin;
        this.universeMax = universeMax;
        value = Double.NaN;
    }

    public void defuzzify() {
        value = defuzzifier.defuzzify();
    }

    public Variable aggregate(Variable variable) {
        return aggregate(variable,1.0,1.0);
    }


    public Variable aggregate(Variable variable, double weight1, double weight2) {
        if (variable.getUniverseMin() != universeMin)
            throw new RuntimeException("Minimum values do not match");
        if (variable.getUniverseMax() != universeMax)
            throw new RuntimeException("Maximum values do not match");

        Variable var = new Variable(name+"+"+variable.getName(),linguisticTerms.values(),universeMin,universeMax);
        defuzzifier.setRuleAggregationMethod(
                new RuleAggregationMethodWeightedSum(weight1, weight2));
        Defuzzifier _defuzzifier = getDefuzzifier().aggregate(variable.getDefuzzifier());
        var.setDefuzzifier(_defuzzifier);
        var.defuzzify();
        return var;
    }

    public String getName() {
        return name;
    }

    public Defuzzifier getDefuzzifier() {
        return defuzzifier;
    }

    public double getUniverseMax() {
        return universeMax;
    }

    public double getUniverseMin() {
        return universeMin;
    }

    public double getMembership(String termName) {
        LinguisticTerm lt = (LinguisticTerm) linguisticTerms.get(termName);
        return lt.membership(value);
    }

    public double getMembership(String termName, double input) {
        LinguisticTerm lt = (LinguisticTerm) linguisticTerms.get(termName);
        return lt.membership(input);
    }

    public double getValue() {
        return value;
    }

    public Collection<LinguisticTerm> getLinguisticTerms() {
        return linguisticTerms.values();
    }

    public void setDefuzzifier(Defuzzifier deffuzifier) {
        this.defuzzifier = deffuzifier;
    }

    public void setLinguisticTerms(Collection<LinguisticTerm> linguisticTerms) {
        for (LinguisticTerm lt : linguisticTerms)
            this.linguisticTerms.put(lt.getTermName(), lt);
    }

    public String getLinguisticTerm() {
        if (Double.isNaN(value)) {
            return null;
        }
        Vector<LinguisticTerm> _linguisticTerms = new Vector<LinguisticTerm>(linguisticTerms.values());
        double linguisticTermProbability[] = new double[_linguisticTerms.size()];
        double membershipSum = 0.0;

        for (int i = 0; i < _linguisticTerms.size(); i++) {
            linguisticTermProbability[i] = _linguisticTerms.get(i).membership(value);
            membershipSum += linguisticTermProbability[i];
        }

        if (membershipSum != 0.0)
            for (int i = 0; i < linguisticTermProbability.length; i++)
                linguisticTermProbability[i] = linguisticTermProbability[i]/membershipSum;

        int index = getLinguisticTermIndex(linguisticTermProbability);

        return _linguisticTerms.get(index).getTermName();
    }

    private int getLinguisticTermIndex(double linguisticTermProbability[]) {
        int index = 0;
        double r = Math.random();
        double accumulator = 0.0;

        for (int i = 0; i < linguisticTermProbability.length; i++) {
            if ((accumulator <= r) && (r < accumulator+linguisticTermProbability[i])) {
                index = i;
                break;
            }
            accumulator += linguisticTermProbability[i];
        }

        return index;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setValue(String termName) {
        value = linguisticTerms.get(termName).defuzzify();
    }

    public void chart(boolean showIt) {
        if( showIt ) FuzzySetWindow.showIt(name, getLinguisticTerms());
    }

    public void chartDefuzzifier(boolean showIt) {
        if( showIt ) FuzzySetWindow.showIt(name, this);
    }
}