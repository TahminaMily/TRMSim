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

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.outcomes.EnergyConsumptionOutcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;
import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class models TemplateTRM
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network,
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="TemplateTRMparameters"></a>It needs some parameters to be passed as a
 * {@link TemplateTRM_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # TemplateTRM parameters file
 *    ####################################
 *    parameter1Name=parameter1Value
 *    parameter2Name=parameter2Value
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/TemplateTRMparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.3
 * @since 0.3
 */
public class TemplateTRM  extends TRModel_WSN {

    /** Minimum satisfaction value: {@value} */
    protected final double MIN_SATISFACTION = 0.0;
    /** Maximum satisfaction value: {@value} */
    protected final double MAX_SATISFACTION = 1.0;
    
    
    /**
     * Class TemplateTRM constructor
     * @param templateTRM_parameters Parameters needed for the algorithm, as described <a href="#TemplateTRMparameters">before</a>
     */
    public TemplateTRM(TemplateTRM_Parameters templateTRM_parameters) {
    	super(templateTRM_parameters);
    }

    /**
     * Returns the name of this model, i.e., "TemplateTRM"
     * @return The name of this model, i.e., "TemplateTRM"
     */
    public static String get_name() { return "TemplateTRM"; }

    @Override
    public GatheredInformation gatherInformation(Sensor client, Service service) {
        Collection<Vector<Sensor>> pathsToServers = client.findSensors(new IsServerSearchCondition(service));
        return new GatheredInformation(pathsToServers);
    }

    @Override
    public Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        
        double delta = Double.NEGATIVE_INFINITY;
        Vector<Sensor> mostTrustworthyPath = new Vector<Sensor>();
        double[] newTrustVector = new double[TemplateTRM_Sensor.getNumSensors()];
        do{
            
            ((TemplateTRM_Sensor)client).setWeightVector();
            double maxTrustworthyness = 0.0;
            int i = 0;
            for(Vector<Sensor> pathToServer : gi.getPathsToServers()){
                TemplateTRM_Sensor server = (TemplateTRM_Sensor)pathToServer.lastElement();
                double trustworthyness = 1/server.getBeliefDivergence();
                newTrustVector[i] = trustworthyness;
                if(trustworthyness > maxTrustworthyness){ 
                    maxTrustworthyness = trustworthyness;
                    mostTrustworthyPath = pathToServer;
                }
                i++;
            }
            
            delta = trustVectorsDistance(((TemplateTRM_Sensor)client).getTrustVector(),newTrustVector);
            ((TemplateTRM_Sensor)client).setTrustVector(newTrustVector);
            
        }while(delta > ((TemplateTRM_Parameters)trmParameters).get_epsilon());        
        
        return mostTrustworthyPath;
    }

    @Override
    public Outcome performTransaction(Vector<Sensor> path, Service service) {
        Outcome outcome = null;
        if ((path == null) || (path.size() == 0) || (!path.lastElement().isActive()))
            return outcome;
        
        TemplateTRM_Sensor client = (TemplateTRM_Sensor)path.firstElement();
        TemplateTRM_Sensor server = (TemplateTRM_Sensor)path.lastElement();
        Service receivedService = server.serve(service,path);
        if (receivedService == null)
            outcome = new EnergyConsumptionOutcome(new SatisfactionInterval(MIN_SATISFACTION,MAX_SATISFACTION,MIN_SATISFACTION),path.size());
        else
            outcome = new EnergyConsumptionOutcome(new SatisfactionInterval(MIN_SATISFACTION,MAX_SATISFACTION,MAX_SATISFACTION),path.size());

        server.addNewTransaction(client, server, null);
        client.addNewTransaction(client, server, outcome);

        return outcome;
    }

    @Override
    public Outcome reward(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }

    @Override
    public Outcome punish(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }

    @Override
    public Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services)  {
        return new TemplateTRM_Network(numSensors,probClients,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String fileName) throws Exception {
        return new TemplateTRM_Network(fileName);
    }
    /**
     * This method computes the difference: ||t^{k+1} - t^k||
     * @param globalTrustVector1 t^{k+1}
     * @param globalTrustVector2 t^k
     * @return ||t^{k+1} - t^k||
     */
    private double trustVectorsDistance(double[] globalTrustVector1, double[] globalTrustVector2) {
        double distance = 0.0;

        for (int i = 0 ; i < globalTrustVector1.length; i++)
            distance += Math.pow(globalTrustVector1[i]-globalTrustVector2[i], 2);

        if (globalTrustVector1.length != 0)
            distance = Math.sqrt(distance/globalTrustVector1.length);

        return distance;
    }
    
    
}