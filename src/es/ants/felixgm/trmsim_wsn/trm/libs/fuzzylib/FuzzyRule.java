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
import java.util.LinkedList;

/**
 * <p>This class models a Fuzzy rule with its antecedents ({@link FuzzyRuleExpression}s)
 * and consequents ({@link Variable}s), as well as its degree of support and weight</p>
 * <p>Example of FuzzyRule:	If (x1 is termX1) AND (x2 is termX2) ....  Then (y1 is termY1) AND (y2 is termY2) [weight: 1.0]
 * Notes:</p>
 * <ul>
 * 	<li>- "If" clause is called "antecedent"</li>
 * 	<li>- "then" clause is called "consequent"</li>
 *      <li>- There may be 1 or more antecedents connected using a {@link RuleConnectionMethod} (e.g. AND, OR)</li>
 * </ul>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzyRule {
    /** Rule antecedent ('if' part) */
    protected FuzzyRuleExpression antecedents;
    /** Rule consequent ('then' part) */
    protected Collection<FuzzyRuleTerm> consequents;
    /** Rule's weight */
    protected double weight;

    @Override
    public String toString() {
        String s = "IF "+antecedents+" THEN ";
        for (FuzzyRuleTerm fuzzyRuleTerm : consequents)
            s += fuzzyRuleTerm+" AND";
        return s.substring(0, s.length()-3)+"["+weight+"]";
    }

    public FuzzyRule() {
        this(null,null,1.0);
    }

    public FuzzyRule(FuzzyRuleExpression antecedents, Collection<FuzzyRuleTerm> consequents, double weight) {
        this.antecedents = antecedents;
        this.consequents = consequents;
        this.weight = weight;
    }

    /**
     * Add a condition "... AND ( variable is termName)" to this rule
     * @param variable : Variable to evaluate
     * @param termName : FuzzyRuleTerm for this condition
     * @return this FuzzyRule
     */
    public FuzzyRule addAntecedent(boolean negated, Variable variable, String termName) {
        if (antecedents == null)
            antecedents = new FuzzyRuleExpression();
        antecedents.add(new FuzzyRuleTerm(negated, variable, termName));
        return this;
    }

    /**
     * Add consequent "( variable is termName)" to this rule
     * @param variable : Variable to evaluate
     * @param termName : FuzzyRuleTerm for this condition
     * @return this FuzzyRule
     */
    public FuzzyRule addConsequent(boolean negated, Variable variable, String termName) {
        if (consequents == null)
            consequents = new LinkedList<FuzzyRuleTerm>();
        consequents.add(new FuzzyRuleTerm(negated, variable, termName));
        return this;
    }

    /**
     * Evaluate this rule using 'RuleImplicationMethod'
     * @param ruleImplicationMethod : Rule implication method to use
     */
    public void evaluate(RuleImplicationMethod ruleImplicationMethod) {
        //---
        // Evaluate antecedents (using 'and')
        //---
        double degreeOfSupport = antecedents.evaluate();

        // Apply weight
        degreeOfSupport *= weight;

        //---
        // Imply rule consequents: Apply degreeOfSupport to consequent linguisticTerms
        //---
        for (FuzzyRuleTerm fuzzyRuleTerm : consequents)
            ruleImplicationMethod.imply(fuzzyRuleTerm, degreeOfSupport);
    }

    public FuzzyRuleExpression getAntecedents() {
        return antecedents;
    }

    public Collection<FuzzyRuleTerm> getConsequents() {
        return consequents;
    }
}