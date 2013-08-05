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

import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.DefuzzifierCenterOfGravity;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRule;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRuleSet;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRuleTerm;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.LinguisticTerm;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.Variable;
import java.util.Collection;

/**
 * <p>This class models a service offered in a P2P, Ad-hoc or Wireless Sensor Network,
 * or even an agent in a multi-agent system.
 * Its several attributes (price, quality, deliveryTime, etc.) are defined through
 * fuzzy sets ({@link Variable}s)</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class LFTM_Service extends Service {

    /** Universe MIN. Minimum value for the fuzzy sets used in this class */
    protected final double U_MIN = LFTM_Parameters.get_U_MIN();
    /** Universe MAX. Maximum value for the fuzzy sets used in this class */
    protected final double U_MAX = LFTM_Parameters.get_U_MAX();

    /** Service's price fuzzy set */
    protected Variable price;
    /** Fuzzy rules set used to compare the price of two services */
    protected FuzzyRuleSet frsPrice = null;
    /** Service's cost fuzzy set */
    protected Variable cost;
    /** Fuzzy rules set used to compare the cost of two services */
    protected FuzzyRuleSet frsCost = null;
    /** Service's delivery time fuzzy set */
    protected Variable delivery;
    /** Fuzzy rules set used to compare the delivery time of two services */
    protected FuzzyRuleSet frsDelivery = null;
    /** Service's quality fuzzy set */
    protected Variable quality;
    /** Fuzzy rules set used to compare the quality of two services */
    protected FuzzyRuleSet frsQuality = null;

    /**
     * Class LFTM_Service constructor
     * @param service Service to "fuzzify"
     * @param linguisticTerms Set of linguistic terms applied in the fuzzy sets representing this service attributes (price, quality, etc.)
     * @param fuzzyPrice Price fuzzy value ("Very High", "High", etc.)
     * @param fuzzyCost Cost fuzzy value ("Very High", "High", etc.)
     * @param fuzzyDelivery Delivery time fuzzy value ("Very High", "High", etc.)
     * @param fuzzyQuality Quality fuzzy value ("Very High", "High", etc.)
     */
    public LFTM_Service(Service service, Collection<LinguisticTerm> linguisticTerms, String fuzzyPrice, String fuzzyCost, String fuzzyDelivery, String fuzzyQuality) {
        this(service.id(), linguisticTerms, fuzzyPrice, fuzzyCost, fuzzyDelivery, fuzzyQuality);
    }

    /**
     * Class LFTM_Service constructor
     * @param id Services's identifier
     * @param linguisticTerms Set of linguistic terms applied in the fuzzy sets representing this service attributes (price, quality, etc.)
     * @param fuzzyPrice Price fuzzy value ("Very High", "High", etc.)
     * @param fuzzyCost Cost fuzzy value ("Very High", "High", etc.)
     * @param fuzzyDelivery Delivery time fuzzy value ("Very High", "High", etc.)
     * @param fuzzyQuality Quality fuzzy value ("Very High", "High", etc.)
     */
    private LFTM_Service(String id, Collection<LinguisticTerm> linguisticTerms, String fuzzyPrice, String fuzzyCost, String fuzzyDelivery, String fuzzyQuality) {
        super(id);

        price = new Variable("Price",linguisticTerms,U_MIN, U_MAX);
        price.setValue(fuzzyPrice);

        cost = new Variable("Cost",linguisticTerms,U_MIN, U_MAX);
        cost.setValue(fuzzyCost);

        delivery = new Variable("Delivery",linguisticTerms,U_MIN, U_MAX);
        delivery.setValue(fuzzyDelivery);

        quality = new Variable("Quality",linguisticTerms,U_MIN, U_MAX);
        quality.setValue(fuzzyQuality);
    }

    /**
     * Class LFTM_Service constructor
     * @param id Services's identifier
     * @param price Fuzzy set representing the price of this service
     * @param cost Fuzzy set representing the cost of this service
     * @param delivery Fuzzy set representing the delivery time of this service
     * @param quality Fuzzy set representing the quality of this service
     */
    public LFTM_Service(String id, Variable price, Variable cost, Variable delivery, Variable quality) {
        super(id);

        this.price = price;
        this.cost = cost;
        this.delivery = delivery;
        this.quality = quality;

    }

    /**
     * This method compares two services, by comparing their attributes and returns
     * a fuzzy set representing such comparison. Each attribute is weighted with a given
     * weight. If service1.compareTo(service2) output is, for instance, "Very High",
     * it means that "service1 is much better than service2"
     * @param service Service to be compared with this service
     * @param prizeWeight Price weight
     * @param costWeight Cost weight
     * @param deliveryWeight Delivery time weight
     * @param qualityWeight Quality weight
     * @return A fuzzy set representing the comparison between this service and the given one
     */
    public Variable compareTo(LFTM_Service service, double prizeWeight, double costWeight, double deliveryWeight, double qualityWeight) {
        Variable priceComp = new Variable("PriceComp", LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
        Variable costComp = new Variable("CostComp",LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
        Variable deliveryComp = new Variable("DeliveryComp",LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
        Variable qualityComp = new Variable("QualityComp",LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(U_MIN, U_MAX));

        if (frsPrice == null)
            frsPrice = LFTM_Parameters.getFRSServicesAttributesNegative(price,service.get_price(),priceComp);
        else {
            priceComp.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
            for (FuzzyRule fuzzyRule : frsPrice.getFuzzyRules())
                ((FuzzyRuleTerm)fuzzyRule.getAntecedents().getTerm2()).setVariable(service.get_price());
        }

        if (frsCost == null)
            frsCost = LFTM_Parameters.getFRSServicesAttributesPositive(cost,service.get_cost(),costComp);
        else {
            costComp.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
            for (FuzzyRule fuzzyRule : frsCost.getFuzzyRules())
                ((FuzzyRuleTerm)fuzzyRule.getAntecedents().getTerm2()).setVariable(service.get_cost());
        }
        
        if (frsDelivery == null)
            frsDelivery = LFTM_Parameters.getFRSServicesAttributesNegative(delivery,service.get_delivery(),deliveryComp);
        else {
            deliveryComp.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
            for (FuzzyRule fuzzyRule : frsDelivery.getFuzzyRules())
                ((FuzzyRuleTerm)fuzzyRule.getAntecedents().getTerm2()).setVariable(service.get_delivery());
        }
        
        if (frsQuality == null)
            frsQuality = LFTM_Parameters.getFRSServicesAttributesPositive(quality,service.get_quality(),qualityComp);
        else {
            qualityComp.setDefuzzifier(new DefuzzifierCenterOfGravity(U_MIN, U_MAX));
            for (FuzzyRule fuzzyRule : frsQuality.getFuzzyRules())
                ((FuzzyRuleTerm)fuzzyRule.getAntecedents().getTerm2()).setVariable(service.get_quality());
        }

        frsPrice.evaluate();
        frsCost.evaluate();
        frsDelivery.evaluate();
        frsQuality.evaluate();

        double weightSum = prizeWeight + costWeight + deliveryWeight + qualityWeight;
        Variable servicesComp = priceComp.aggregate(costComp.aggregate(
                deliveryComp.aggregate(qualityComp,deliveryWeight/weightSum,qualityWeight/weightSum),
                costWeight/weightSum,1.0),prizeWeight/weightSum,1.0);

        return servicesComp;
    }

    @Override
    public Service clone() {
        return new LFTM_Service(id,price,cost,delivery,quality);
    }

    /**
     * This method returns the service's price fuzzy set
     * @return The service's price fuzzy set
     */
    public Variable get_price() { return price; }
    /**
     * This method returns the service's cost fuzzy set
     * @return The service's cost fuzzy set
     */
    public Variable get_cost() { return cost; }
    /**
     * This method returns the service's delivery time fuzzy set
     * @return The service's delivery time fuzzy set
     */
    public Variable get_delivery() { return delivery; }
    /**
     * This method returns the service's quality fuzzy set
     * @return The service's quality fuzzy set
     */
    public Variable get_quality() { return quality; }
}