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
 * <p>This class models a Fuzzy rule expression composed either by a single {@link Variable}
 * or a combination of other fuzzy rule expressions.
 * E.g.: "(temp IS hot AND pressure IS high) OR pressure IS low"
 * A fuzzy rule expression constitutes the antecedent of a {@link FuzzyRule}</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzyRuleExpression {
    /** Is it negated? */
    protected boolean negated;
    /** How are term1 and term2 connected? */
    protected RuleConnectionMethod ruleConnectionMethod;
    /** First term of this fuzzy rule expression */
    protected FuzzyRuleExpression term1;
    /** Second term of this fuzzy rule expression */
    protected FuzzyRuleExpression term2;

    @Override
    public String toString() {
        if ((term1 != null) && (term2 != null))
         return "("+term1+" "+ruleConnectionMethod+" "+term2+")";

        if (term1 != null)
            return "("+term1+")";

        return "";
    }

    /**
     * Default Constructor
     */
    public FuzzyRuleExpression() {
        this(false, null, null, new RuleConnectionMethodAndMin()); // Default connection method: AND (minimum)
    }

    public FuzzyRuleExpression(boolean negated, FuzzyRuleExpression term1, FuzzyRuleExpression term2,
            RuleConnectionMethod ruleConnectionMethod) {
        this.negated = negated;
        this.term1 = term1;
        this.term2 = term2;
        this.ruleConnectionMethod = ruleConnectionMethod;
    }

    /**
     * Add a new term (using default AND method)
     * @param fuzzyRuleTerm : term to add
     */
    public void add(FuzzyRuleTerm fuzzyRuleTerm) {
        // Can add it in term1? => add it
        if (term1 == null)
            term1 = fuzzyRuleTerm;
        // Can add it in term2? => add it
        else if (term2 == null)
            term2 = fuzzyRuleTerm;
        // Is term1 an expresion? => recurse
        else if (!(term1 instanceof FuzzyRuleTerm))
            term1.add(fuzzyRuleTerm);
        // Is term2 an expresion? => recurse
        else if (!(term2 instanceof FuzzyRuleTerm))
            term2.add(fuzzyRuleTerm);
        // ...there's nothing else I can do
        else throw new RuntimeException("Can't add term!");
        return;
    }

    public double evaluate() {
        // Results for each term
        double resultTerm1 = 0;
        double resultTerm2 = 0;
        double result = 0;

        resultTerm1 = term1.evaluate();
        if (term2 != null) {
            resultTerm2 = term2.evaluate();
            result = ruleConnectionMethod.connect(resultTerm1, resultTerm2);
        } else
            result = resultTerm1;

        // Is this clause negated?
        if( negated ) result = 1 - result;
        return result;
    }

    public FuzzyRuleExpression getTerm1() {
        return term1;
    }

    public FuzzyRuleExpression getTerm2() {
        return term2;
    }

    public void setTerm1(FuzzyRuleExpression term1) {
        this.term1 = term1;
    }

    public void setTerm2(FuzzyRuleExpression term2) {
        this.term2 = term2;
    }
}