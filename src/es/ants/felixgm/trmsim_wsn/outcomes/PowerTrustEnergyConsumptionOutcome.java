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

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.satisfaction.Satisfaction;
import es.ants.felixgm.trmsim_wsn.search.IsClientSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsPowerNodeSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import java.util.Collection;

/**
 * <p>This class models the outcome of PowerTrust model. It includes,
 * besides the elements of a {@link EnergyConsumptionOutcome}, the energy consumption of each
 * power node of a network</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class PowerTrustEnergyConsumptionOutcome extends EnergyConsumptionOutcome {

    /** Energy consumption per power node and per each execution of the trust model */
    protected double powerNodeEnergyConsumption;


    /**
     * Class PowerTrustEnergyConsumptionOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgPathLength Average length of all the paths found
     */
    public PowerTrustEnergyConsumptionOutcome(Satisfaction satisfaction, double avgPathLength) {
        super(satisfaction, avgPathLength);
        powerNodeEnergyConsumption = 0.0;
    }

    /**
     * Class PowerTrustEnergyConsumptionOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgSatisfaction Average satisfaction of the client with the received service
     * @param avgPathLength Average length of all the paths found
     * @param clientEnergyConsumption Energy consumption per client and per each execution of the trust model
     * @param maliciousServerEnergyConsumption Energy consumption per malicious server and per each execution of the trust model
     * @param benevolentServerEnergyConsumption Energy consumption per benevolent server and per each execution of the trust model
     * @param relayServerEnergyConsumption Energy consumption per relay server and per each execution of the trust model
     * @param powerNodeEnergyConsumption Energy consumption per power node and per each execution of the trust model
     */
    protected PowerTrustEnergyConsumptionOutcome(Satisfaction satisfaction, double avgSatisfaction, double avgPathLength,
            double clientEnergyConsumption, double maliciousServerEnergyConsumption,
            double benevolentServerEnergyConsumption, double relayServerEnergyConsumption,
            double powerNodeEnergyConsumption) {
        super(satisfaction, avgSatisfaction, avgPathLength, clientEnergyConsumption, maliciousServerEnergyConsumption, benevolentServerEnergyConsumption, relayServerEnergyConsumption);
        this.powerNodeEnergyConsumption = powerNodeEnergyConsumption;
        avgSensorEnergyConsumption = (clientEnergyConsumption+maliciousServerEnergyConsumption+benevolentServerEnergyConsumption+relayServerEnergyConsumption+powerNodeEnergyConsumption)/5.0;
    }

    @Override
    public void setEnergyConsumption(Network network, Service requiredService, int numExecutions) {
        clientEnergyConsumption = network.get_sensorsTransmittedDistance(new IsClientSearchCondition(),requiredService)/(double)numExecutions;
        maliciousServerEnergyConsumption = network.get_sensorsTransmittedDistance(new IsServerSearchCondition(requiredService,IsServerSearchCondition.MALICIOUS_SERVER),requiredService)/(double)numExecutions;
        benevolentServerEnergyConsumption = network.get_sensorsTransmittedDistance(new IsServerSearchCondition(requiredService,IsServerSearchCondition.BENEVOLENT_SERVER),requiredService)/(double)numExecutions;
        relayServerEnergyConsumption = network.get_sensorsTransmittedDistance(new IsServerSearchCondition(new Service("Relay"),IsServerSearchCondition.RELAY_SERVER),new Service("Relay"))/(double)numExecutions;
        powerNodeEnergyConsumption = network.get_sensorsTransmittedDistance(new IsPowerNodeSearchCondition(),requiredService)/(double)numExecutions;
        avgSensorEnergyConsumption = (clientEnergyConsumption+maliciousServerEnergyConsumption+benevolentServerEnergyConsumption+relayServerEnergyConsumption+powerNodeEnergyConsumption)/5.0;
    }

    @Override
    public Outcome aggregate(Collection<Outcome> outcomes) {
        double _powerNodeEnergyConsumption = powerNodeEnergyConsumption;

        for (Outcome outcome : outcomes) {
            _powerNodeEnergyConsumption += ((PowerTrustEnergyConsumptionOutcome)outcome).get_powerNodeEnergyConsumption();
        }
        _powerNodeEnergyConsumption = _powerNodeEnergyConsumption/(outcomes.size()+1);

        Outcome outcome = super.aggregate(outcomes);

        return new PowerTrustEnergyConsumptionOutcome(outcome.get_satisfaction(),
                     ((BasicOutcome)outcome).get_avgSatisfaction(),
                     ((BasicOutcome)outcome).get_avgPathLength(),
                     ((EnergyConsumptionOutcome)outcome).get_clientEnergyConsumption(),
                     ((EnergyConsumptionOutcome)outcome).get_maliciousServerEnergyConsumption(),
                     ((EnergyConsumptionOutcome)outcome).get_benevolentServerEnergyConsumption(),
                     ((EnergyConsumptionOutcome)outcome).get_relayServerEnergyConsumption(),
                     _powerNodeEnergyConsumption);
    }

    /**
     * This method returns the energy consumption per power node and per each execution of the trust model of this outcome
     * @return The energy consumption per power node and per each execution of the trust model of this outcome
     */
    public double get_powerNodeEnergyConsumption() { return powerNodeEnergyConsumption; }

    @Override
    public String toString() {
        String s = super.toString()+"\r\n";
        s += "Clients energy consumption: "+(((int)(clientEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "Malicious servers energy consumption: "+(((int)(maliciousServerEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "Benevolent servers energy consumption: "+(((int)(benevolentServerEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "Relay servers energy consumption: "+(((int)(relayServerEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "Power nodes energy consumption: "+(((int)(powerNodeEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "Sensors average energy consumption: "+(((int)(avgSensorEnergyConsumption*1000000))/10000.0)+"\r\n";
        s += "****************************************************";

        return s;
    }

    @Override
    protected String getOutcomesFileLine() {
        return super.getOutcomesFileLine()
                +"\t"+clientEnergyConsumption
                +"\t"+maliciousServerEnergyConsumption
                +"\t"+benevolentServerEnergyConsumption
                +"\t"+relayServerEnergyConsumption
                +"\t"+avgSensorEnergyConsumption
                +"\t"+powerNodeEnergyConsumption;
    }
}