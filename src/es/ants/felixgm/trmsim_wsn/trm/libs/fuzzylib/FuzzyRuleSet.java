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
 * <p>This class models a set of {@link FuzzyRule}s</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzyRuleSet {
    /** Rule implication method */
    protected RuleImplicationMethod ruleImplicationMethod;
    /** A list of fuzzy rules */
    protected LinkedList<FuzzyRule> fuzzyRules;

    public FuzzyRuleSet() {
        fuzzyRules = new LinkedList<FuzzyRule>();
        ruleImplicationMethod = new RuleImplicationMethodMin(); // Default implication method: Min
    }

    /**
     * Add a rule to this ruleSet
     * @param fuzzyRule : Rule to add
     * @return this
     */
    public FuzzyRuleSet add(FuzzyRule fuzzyRule) {
        fuzzyRules.add(fuzzyRule);
        return this;
    }

    /**
     * Evaluate fuzzy rule set
     */
    public void evaluate() {
        // First: Reset defuzzifiers, variables, etc.
        //reset();

        // Second: Apply each rule
        for (FuzzyRule fuzzyRule : fuzzyRules) {
            //System.out.println(fuzzyRule);
            fuzzyRule.evaluate(ruleImplicationMethod);
        }

        // Thrid: Defuzzify each consequent varaible
        for (FuzzyRule fuzzyRule : fuzzyRules)
            for (FuzzyRuleTerm fuzzyRuleTerm : fuzzyRule.getConsequents())
                fuzzyRuleTerm.getVariable().defuzzify();
    }

    public Collection<FuzzyRule> getFuzzyRules() {
        return fuzzyRules;
    }

    /**
     * Reset ruleset (should be done prior to each inference)
     * Also create 'variables' list (if needed)
     */
    /*public void reset() {
        boolean addToVariables = false;
        HashMap<Variable, Variable> resetted = new HashMap<Variable, Variable>();

        // Create a list of consequent variables if not already created
        // (all variables that must be defuzzified)
        if( variables == null ) {
            variables = new HashMap<String, Variable>();
            addToVariables = true;
        }

        //---
        // Reset every consequent variable on every rule
        //---
        for( Iterator it = fuzzyRules.iterator(); it.hasNext(); ) {
            FuzzyRule fr = (FuzzyRule) it.next();
            // Reset rule's degree of support
            fr.setDegreeOfSupport(0);

            //---
            // Reset every consequent variable (and add it to variables list if needed)
            //---
            Collection llc = fr.getConsequents();
            for( Iterator itc = llc.iterator(); itc.hasNext(); ) {
                FuzzyRuleTerm term = (FuzzyRuleTerm) itc.next();
                Variable var = term.getVariable();
                // Not already resetted?
                if( resetted.get(var) == null ) {
                    // Sanity check
                    if( var.getDefuzzifier() == null )
                        throw new RuntimeException("Defuzzifier not setted for output variable '" + var.getName() + "'");
                    // Reset variable
                    var.reset();
                    // Mark it as 'resetted' so we don't reset it again
                    resetted.put(var, var);
                    // Add this variable to variables's list (if not already added)
                    if( addToVariables && (!variableExists(var.getName())) )
                        variables.put(var.getName(), var);
                }
            }

            //---
            // Reset every antecedent's variable  (and add it to variables list if needed)
            //---
            for( Iterator itc = fr.getAntecedents().iteratorVariables(); itc.hasNext(); ) {
                Variable var = (Variable) itc.next();
                // Not already resetted?
                if( resetted.get(var) == null ) {
                    // Reset variable
                    var.reset();
                    // Mark it as 'resetted' so we don't reset it again
                    resetted.put(var, var);
                    // Add this variable to variables's list (if not already added)
                    if( addToVariables && (!variableExists(var.getName())) )
                        variables.put(var.getName(), var);
                }
            }
        }
    }*/

    @Override
    public String toString() {
        String s = "";
        for (FuzzyRule fuzzyRule : fuzzyRules)
            s += fuzzyRule+"\n";
        return s;
    }
}