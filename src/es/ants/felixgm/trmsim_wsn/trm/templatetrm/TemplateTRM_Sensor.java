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

package es.ants.felixgm.trmsim_wsn.trm.templatetrm;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;
import es.ants.felixgm.trmsim_wsn.trm.peertrust.Transaction;
import java.util.Collection;
import java.util.LinkedList;

/**
 * <p>This class models a Sensor implementing TemplateTRM</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.3
 */
public class TemplateTRM_Sensor extends Sensor {

    /** Number of sensors composing the network this sensor belongs to */
    protected static int _numSensors = 0;
    /** Collection of Transactions this sensor has had */
    protected Collection<Transaction> transactions;
    
    private double[] trustVector;
    private double[] weightVector;
    
    /**
     * This constructor creates a new Sensor implementing TemplateTRM
     */
    public TemplateTRM_Sensor () {
        super();
    }

    /**
     * This constructor creates a new Sensor implementing TemplateTRM
     * @param id Identifier of the new sensor
     * @param x X coordinate of the new sensor
     * @param y Y coordinate of the new sensor
     */
    public TemplateTRM_Sensor(int id, double x, double y) {
        super(id,x,y);
    }

    /**
     * Returns the number of sensors composing the network this sensor belongs to
     * @return The number of sensors composing the network this sensor belongs to
     */
    public static int getNumSensors() { return _numSensors; }
    
    /**
     * Sets the number of sensors composing the network this sensor belongs to
     * @param numSensors The number of sensors composing the network this sensor belongs to
     */
    public static void setNumSensors(int numSensors){ _numSensors = numSensors; }
    
    @Override
    public void reset() {
        transactions = new LinkedList<Transaction>();
        weightVector = new double[_numSensors];
        trustVector = new double[_numSensors];
        for (int i = 0; i < trustVector.length; i ++)
            trustVector[i] = 1.0;
        setWeightVector();
    }
    
    /**
     * This method adds a new Transaction to the collection of transactions of this sensor
     * @param client The client who requested the service
     * @param server The server who provided the service
     * @param outcome Outcome of the trnsaction to be added
     */
    public synchronized void addNewTransaction(TemplateTRM_Sensor client, TemplateTRM_Sensor server, Outcome outcome){
        //if ((transactions.size() != 0) && (transactions.size() >= _windowSize))
          //  ((LinkedList<Transaction>)transactions).removeLast();

        ((LinkedList<Transaction>)transactions).addFirst(new Transaction(client,server,outcome));
        
    }
    
    public synchronized void setWeightVector(){
        int sum = 0;
        for(int i = 0; i< trustVector.length; i++)
            sum += trustVector[i];
        for (int i = 0; i<weightVector.length; i++){
            if(sum!=0)
                weightVector[i] = trustVector[i]/sum;
        } 
    }
    public synchronized void setTrustVector(double[] trustVector){
        for(int i = 0; i< trustVector.length; i++)
            this.trustVector[i] = trustVector[i];
    }
    public double[] getTrustVector(){ return trustVector;}
    
    public synchronized double getReputation(){
        double reputation = 0.0;
        int i = 0;
        for (Transaction transaction : transactions){
             TemplateTRM_Sensor client = (TemplateTRM_Sensor)transaction.getClient();
             reputation += weightVector[i]*client.getRating(this);
             i++;
        }
        return reputation; 
    }
    
    public synchronized double getBeliefDivergence(){
        
        double beliefDivergence = 0.0;
        double sum = 0.0;
        int i = 0;
        for (Transaction transaction : transactions) {
             TemplateTRM_Sensor client = (TemplateTRM_Sensor)transaction.getClient();
             sum += Math.pow(client.getRating(this) - client.getReputation(),2.0);
             i++;
        }
        if(transactions.isEmpty()|| sum == 0){
           beliefDivergence = 0.0; 
        }
        else
            beliefDivergence = 1/transactions.size() *sum;
        return beliefDivergence;
    }
    private synchronized double getRating(TemplateTRM_Sensor server) {
        double ratingValue = 0.0;

        for (Transaction transaction : transactions) 
            if (transaction.getServer().equals(server))
                ratingValue = ((SatisfactionInterval)transaction.getSatisfaction()).getSatisfactionValue();
        return ratingValue;
    }
}