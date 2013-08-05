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

package es.ants.felixgm.trmsim_wsn.outcomes;

import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionFuzzy;
import java.util.Collection;
import java.util.HashMap;

/**
 * <p>This class models the outcome of a trust and reputation model It includes,
 * besides the elements of a {@link EnergyConsumptionOutcome}, the percentage of
 * clients with each type of fuzzy satisfaction (Very high, High, Medium, etc.)</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class FuzzyOutcome extends EnergyConsumptionOutcome {
    /** Percentage of clients with each type of satisfaction */
    private HashMap<String,Double> satisfactionLinguisticTermsPercentage;
    /** Number of clients with each type of satisfaction */
    private HashMap<String,Integer> satisfactionLinguisticTermsCount;
    /** Total number of outcomes */
    private int totalOutcomesNumber;

    /**
     * Class FuzzyOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgPathLength Average length of all the paths found
     */
    public FuzzyOutcome(SatisfactionFuzzy satisfaction, double avgPathLength) {
        super(satisfaction, avgPathLength);
        totalOutcomesNumber = 1;
        satisfactionLinguisticTermsPercentage = new HashMap<String,Double>();
        satisfactionLinguisticTermsCount = new HashMap<String,Integer>();
        satisfactionLinguisticTermsCount.put(((SatisfactionFuzzy)satisfaction).getSatisfactionLinguisticTerm(), 1);
        satisfactionLinguisticTermsPercentage.put(((SatisfactionFuzzy)satisfaction).getSatisfactionLinguisticTerm(), 100.0);
    }

    /**
     * Class FuzzyConsumptionOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgSatisfaction Average satisfaction of the client with the received service
     * @param avgPathLength Average length of all the paths found
     * @param clientEnergyConsumption Energy consumption per client and per each execution of the trust model
     * @param maliciousServerEnergyConsumption Energy consumption per malicious server and per each execution of the trust model
     * @param benevolentServerEnergyConsumption Energy consumption per benevolent server and per each execution of the trust model
     * @param relayServerEnergyConsumption Energy consumption per relay server and per each execution of the trust model
     * @param satisfactionLinguisticTermsPercentage Percentage of clients with each type of satisfaction
     * @param satisfactionLinguisticTermsCount Number of clients with each type of satisfaction
     * @param totalOutcomesNumber Total number of outcomes
     */
    protected FuzzyOutcome(SatisfactionFuzzy satisfaction, double avgSatisfaction, double avgPathLength,
                           double clientEnergyConsumption, double maliciousServerEnergyConsumption,
                           double benevolentServerEnergyConsumption, double relayServerEnergyConsumption,
                           HashMap<String,Double> satisfactionLinguisticTermsPercentage,
                           HashMap<String,Integer> satisfactionLinguisticTermsCount,
                           int totalOutcomesNumber) {
        super(satisfaction, avgSatisfaction, avgPathLength,
                clientEnergyConsumption, maliciousServerEnergyConsumption,
                benevolentServerEnergyConsumption, relayServerEnergyConsumption);
        this.satisfactionLinguisticTermsPercentage = satisfactionLinguisticTermsPercentage;
        this.satisfactionLinguisticTermsCount = satisfactionLinguisticTermsCount;
        this.totalOutcomesNumber = totalOutcomesNumber;
    }

    @Override
    public Outcome aggregate(Collection<Outcome> outcomes) {
        HashMap<String,Integer> _satisfactionLinguisticTermsCount = new HashMap<String,Integer>();
        HashMap<String,Double> _satisfactionLinguisticTermsPercentage = new HashMap<String,Double>();
        int _totalOutcomesNumber = totalOutcomesNumber;
        
        for (String satisfactionLinguisticTerm : satisfactionLinguisticTermsCount.keySet())
            _satisfactionLinguisticTermsCount.put(satisfactionLinguisticTerm, satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm));

        for (Outcome outcome : outcomes) 
            for (String satisfactionLinguisticTerm : ((FuzzyOutcome)outcome).getSatisfactionCount().keySet()) {
                if (_satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm) == null)
                    _satisfactionLinguisticTermsCount.put(satisfactionLinguisticTerm,0);
                _satisfactionLinguisticTermsCount.put(satisfactionLinguisticTerm,
                        _satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm)+((FuzzyOutcome)outcome).getSatisfactionCount().get(satisfactionLinguisticTerm));
                _totalOutcomesNumber +=((FuzzyOutcome)outcome).getSatisfactionCount().get(satisfactionLinguisticTerm);
            }

        for (String satisfactionLinguisticTerm : _satisfactionLinguisticTermsCount.keySet())
            _satisfactionLinguisticTermsPercentage.put(satisfactionLinguisticTerm,
                _satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm)/(double)_totalOutcomesNumber);

        Outcome outcome = super.aggregate(outcomes);
        return new FuzzyOutcome((SatisfactionFuzzy)outcome.get_satisfaction(),
                                ((BasicOutcome)outcome).get_avgSatisfaction(),
                                ((BasicOutcome)outcome).get_avgPathLength(),
                                ((EnergyConsumptionOutcome)outcome).get_clientEnergyConsumption(),
                                ((EnergyConsumptionOutcome)outcome).get_maliciousServerEnergyConsumption(),
                                ((EnergyConsumptionOutcome)outcome).get_benevolentServerEnergyConsumption(),
                                ((EnergyConsumptionOutcome)outcome).get_relayServerEnergyConsumption(),
                                _satisfactionLinguisticTermsPercentage,
                                _satisfactionLinguisticTermsCount,
                                _totalOutcomesNumber);
    }

    /**
     * This method returns the number of clients with each type of satisfaction
     * @return The number of clients with each type of satisfaction
     */
    public HashMap<String,Integer> getSatisfactionCount() {
        return satisfactionLinguisticTermsCount;
    }

    /**
     * This method returns the percentage of clients with the given fuzzy satisfaction
     * @param linguisticTerm The given linguistic term of the fuzzy satisfaction
     * @return The percentage of clients with the given fuzzy satisfaction
     */
    public double getSatisfactionPercentage(String linguisticTerm) {
        if (satisfactionLinguisticTermsPercentage.get(linguisticTerm) == null)
            return 0.0;
        return satisfactionLinguisticTermsPercentage.get(linguisticTerm);
    }

    @Override
    public String toString() {
        String s = super.toString()+"\r\n";
        for (String satisfactionLinguisticTerm : satisfactionLinguisticTermsCount.keySet())
            s += "Number of \""+satisfactionLinguisticTerm+"\" satisfactions: "+satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm)+"\r\n";
        for (String satisfactionLinguisticTerm : satisfactionLinguisticTermsPercentage.keySet())
            s += "Percentage of \""+satisfactionLinguisticTerm+"\" satisfactions: "+(((int)(satisfactionLinguisticTermsPercentage.get(satisfactionLinguisticTerm)*1000000))/10000.0)+"%\r\n";
        s += "****************************************************";

        return s;
    }

    @Override
    protected String getOutcomesFileLine() {
        String s = super.getOutcomesFileLine();
        for (String satisfactionLinguisticTerm : satisfactionLinguisticTermsCount.keySet())
            s += "\t"+satisfactionLinguisticTermsCount.get(satisfactionLinguisticTerm);
        for (String satisfactionLinguisticTerm : satisfactionLinguisticTermsPercentage.keySet())
            s += "\t"+(((int)(satisfactionLinguisticTermsPercentage.get(satisfactionLinguisticTerm)*1000000))/10000.0);
        return s;
    }
}