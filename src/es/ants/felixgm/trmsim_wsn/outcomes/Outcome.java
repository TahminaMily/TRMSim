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
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class models the outcome of a trust and reputation model. It includes
 * the {@link Satisfaction} of the client with the received service</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.1
 */
public abstract class Outcome {
    /** Satisfaction of the client with the received service */
    protected Satisfaction satisfaction;

    /**
     * Class Outcome constructor
     * @param satisfaction Satisfaction of the client with the received service
     */
    protected Outcome(Satisfaction satisfaction){
        this.satisfaction = satisfaction;
    }

    /**
     * This method computes the average of all the given outcomes
     * @param outcomes Outcomes to be aggregated
     * @return The aggregation of the given outcomes
     */
    public abstract Outcome aggregate(Collection<Outcome> outcomes);

    /**
     * This method computes the average of all the given outcomes, provided by the
     * clients of the given network and, additionally,
     * @param outcomes Outcomes to be aggregated
     * @param network The network the given outcomes come from
     * @param requiredService The requested service
     * @param numExecutions The number of executions of the trust and/or reputation model over the given network
     * @return The aggregation of the given outcomes plus the transmitted distance for each kind of sensor
     */
    public static Outcome computeOutcomes(Collection<Outcome> outcomes, Network network, Service requiredService, int numExecutions) {
        Outcome outcome = computeOutcomes(outcomes);

        if ((outcome != null) && (outcome instanceof EnergyConsumptionOutcome)) {
            ((EnergyConsumptionOutcome)outcome).setEnergyConsumption(network, requiredService, numExecutions);
        }
        return outcome;
    }

    /**
     * Returns an outcome with the average and standard deviation of a collection
     * of outcomes
     * @param outcomes Outcomes to compute (aggregate)
     * @return An outcome with the average and standard deviation of a collection
     * of outcomes
     */
    public static Outcome computeOutcomes(Collection<Outcome> outcomes) {
        if ((outcomes == null) || (outcomes.size() == 0))
            return null;

        Object outcomesArray[] = outcomes.toArray();
        Outcome outcome = (Outcome)outcomesArray[0];

        if (outcomes.size() == 1)
            return outcome;
        else {
            Vector<Outcome> outcomesAux = new Vector<Outcome>();
            for (int i = 1; i < outcomesArray.length; i++)
                outcomesAux.add((Outcome)outcomesArray[i]);

            return outcome.aggregate(outcomesAux);
        }
    }

    @Override
    public String toString() {
        String s = "####################################################\r\n";
        if (satisfaction.isSatisfied())
            s += "Satisfaction: satisfied\r\n";
        else
            s += "Satisfaction: not satisfied\r\n";
        s += "####################################################";
       
        return s;
    }

    /**
     * This method returns the line of the file where to save this outcome, including all its elements
     * @return The line of the file where to save this outcome, including all its elements
     */
    protected String getOutcomesFileLine() {
        if (satisfaction.isSatisfied())
            return "1";
        else
            return "0";
    }

    /**
     * Writes the output of this outcome toString() method into a given file
     * @param fileName Path of the file where to write this outcome
     */
    public void writeToFile(String fileName) throws Exception {
        FileWriter outFile = new FileWriter(fileName);
        outFile.write(toString());
        outFile.flush();
        outFile.close();
    }

    /**
     * Writes the elements of a collection of outcomes into a given file.
     * Very useful when plotting results is desired
     * @param outcomes Outcomes where to get the average and standard deviation satisfaction to
     * write to the file
     * @param fileName Path of the file where to write
     */
    public static void writeToFile(Collection<Outcome> outcomes, String fileName) {
        try { 
            FileWriter out = new FileWriter(fileName);

            int i = 0;
            for (Outcome outcome : outcomes)
                out.write((i++)+"\t"+outcome.getOutcomesFileLine()+"\n");

            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method returns the satisfaction of the client with the received service
     * @return The satisfaction of the client with the received service
     */
    public Satisfaction get_satisfaction() { return satisfaction; }
}