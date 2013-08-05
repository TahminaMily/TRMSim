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

import es.ants.felixgm.trmsim_wsn.satisfaction.Satisfaction;
import java.util.Collection;

/**
 * <p>This class models the outcome of a trust and reputation model. It includes the path leading to the
 * most trustworthy and/or reputable server found as well as the satisfaction perceived by the client</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.4
 */
public class BasicOutcome extends Outcome {
    /** Average satisfaction of the client with the received service */
    protected double avgSatisfaction;
    /** Average length of all the paths found */
    protected double avgPathLength;

    /**
     * Class BasicOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgPathLength Average length of all the paths found
     */
    public BasicOutcome(Satisfaction satisfaction, double avgPathLength) {
        super(satisfaction);
        if (satisfaction.isSatisfied())
            avgSatisfaction = 1.0;
        else
            avgSatisfaction = 0.0;

        this.avgPathLength = avgPathLength;
    }

    /**
     * Class BasicOutcome constructor
     * @param satisfaction Clients satisfaction
     * @param avgSatisfaction Average satisfaction of the client with the received service
     * @param avgPathLength Average length of all the paths found
     */
    protected BasicOutcome(Satisfaction satisfaction, double avgSatisfaction, double avgPathLength) {
        super(satisfaction);
        this.avgSatisfaction = avgSatisfaction;
        this.avgPathLength = avgPathLength;
    }

    @Override
    public Outcome aggregate(Collection<Outcome> outcomes) {
        double _avgSatisfaction = avgSatisfaction;
        double _avgPathLength = avgPathLength;
        int numBenevolentPaths = outcomes.size()+1;
        Satisfaction aggregatedSatisfaction = null;

        if ((outcomes == null) || (outcomes.size() == 0))
            return null;

        for (Outcome outcome : outcomes) {
            _avgSatisfaction += ((BasicOutcome)outcome).get_avgSatisfaction();
            _avgPathLength += ((BasicOutcome)outcome).get_avgPathLength();

            if (aggregatedSatisfaction == null)
                aggregatedSatisfaction = outcome.get_satisfaction();
            else
                aggregatedSatisfaction = aggregatedSatisfaction.aggregate(outcome.get_satisfaction());

            if (((BasicOutcome)outcome).get_avgPathLength() == 0.0)
                numBenevolentPaths--;
        }
        if (avgPathLength == 0.0)
            numBenevolentPaths--;

        _avgSatisfaction = _avgSatisfaction/(outcomes.size()+1);
        if (numBenevolentPaths > 0)
            _avgPathLength = _avgPathLength/numBenevolentPaths;

        return new BasicOutcome(aggregatedSatisfaction,_avgSatisfaction, _avgPathLength);
    }

    /**
     * This method returns the average satisfaction of the client with the received service
     * @return The average satisfaction of the client with the received service
     */
    public double get_avgSatisfaction() { return avgSatisfaction; }

    /**
     * This method returns the average length of all the paths found
     * @return The average length of all the paths found
     */
    public double get_avgPathLength() { return avgPathLength; }

    @Override
    public String toString() {
        String s = super.toString()+"\r\n";
        s += "Average satisfaction: "+(((int)(avgSatisfaction*1000000))/10000.0)+"%\r\n";
        s += "Average path length: "+(((int)(avgPathLength*10000))/10000.0)+"\r\n";
        s += "****************************************************";

        return s;
    }

    @Override
    protected String getOutcomesFileLine() {
        return super.getOutcomesFileLine()+"\t"+avgSatisfaction+"\t"+avgPathLength;
    }
}