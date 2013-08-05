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
 * <p>This class models a Fuzzy rule term as the simplest {@link FuzzyRuleExpression}
 * composed only by a (possible negated) {@link Variable}</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzyRuleTerm extends FuzzyRuleExpression{
    protected Variable variable;
    protected String termName;

    public FuzzyRuleTerm(boolean negated, Variable variable, String termName) {
        super(negated,null, null,null);
        this.variable = variable;
        this.termName = termName;
        term1 = this;
    }

    @Override
    public double evaluate() {
        double membership = variable.getMembership(termName);
        if (negated)
            membership = 1.0 - membership;
        return membership;
    }

    public double membership(double input) {
        return variable.getMembership(termName, input);
    }

    public Variable getVariable() {
        return variable;
    }

    public String getTermName() {
        return termName;
    }

    public boolean isNegated() {
        return negated;
    }

    public void setVariable (Variable variable) {
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable.getName()+" IS "+termName;
    }
}