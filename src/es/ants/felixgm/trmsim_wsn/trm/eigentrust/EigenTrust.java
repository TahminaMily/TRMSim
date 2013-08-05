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

package es.ants.felixgm.trmsim_wsn.trm.eigentrust;

import es.ants.felixgm.trmsim_wsn.search.IsServerSearchCondition;
import es.ants.felixgm.trmsim_wsn.search.IsSensorSearchCondition;

import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.outcomes.EigenTrustEnergyConsumptionOutcome;

import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;

import java.util.Collection;
import java.util.Vector;

/**
 * <p>This class models EigenTrust
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network, 
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="EigenTrustparameters"></a>It needs some parameters to be passed as a
 * {@link EigenTrust_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # EigenTrust parameters file
 *    ####################################
 *    windowSize=5
 *    epsilon=0.1
 *    preTrustedPeersPercentage=0.3
 *    preTrustedPeersWeight=0.25
 *    zeroTrustNodeSelectionProbability=0.2
 * </pre>
 * This file can be downloaded 
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/EigenTrustparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * <br></br>
 * For more information regarding EigenTrust algorithm, please check the following reference:
 * <ul>
 *   <li>Kamvar, S., Schlosser, M. and Garcia-Molina, H., &quot;<strong>The EigenTrust
 *       Algorithm for Reputation Management in P2P Networks</strong>&quot;, WWW03:
 *       Proceedings of the 12th international conference on World Wide Web,
 *       pp. 640-651, 2003
 *   </li>
 * </ul>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a> and Antonio Bern&aacute;rdez
 * @version 0.4
 * @since 0.2
 */
public class EigenTrust extends TRModel_WSN {
    /** Minimum satisfaction value: {@value} */
    protected final double MIN_SATISFACTION = -1.0;
    /** Maximum satisfaction value: {@value} */
    protected final double MAX_SATISFACTION = 1.0;

    /**
     * Class EigenTrust constructor
     * @param eigenTrust_parameters Parameters needed for the algorithm, as described <a href="#EigenTrustparameters">before</a>
     */
    public EigenTrust(EigenTrust_Parameters eigenTrust_parameters) {
    	super(eigenTrust_parameters);
    }

    /**
     * Returns the name of this model, i.e., "EigenTrust"
     * @return The name of this model, i.e., "EigenTrust"
     */
    public static String get_name() { return "EigenTrust"; }

    @Override
    public synchronized GatheredInformation gatherInformation(Sensor client, Service service) {
        GatheredInformationEigenTrust gatheredInfo = new GatheredInformationEigenTrust(client.findSensors(new IsServerSearchCondition(service)),EigenTrust_Sensor.getNumSensors());
        Collection<Vector<Sensor>> pathsToClients = client.findSensors(new IsServerSearchCondition());

        for (Vector<Sensor> pathToClient : pathsToClients) {
            if (pathToClient.lastElement().isActive()) {
                gatheredInfo.setNormalizedTrustValue((EigenTrust_Sensor)pathToClient.lastElement()); //Set c_ij
                for (int i = 0; i < pathToClient.size()-1; i++)
                    pathToClient.get(i).addTransmittedDistance((long)pathToClient.get(i).distance(pathToClient.get(i+1)));
            }
        }
        gatheredInfo.setNormalizedTrustValue((EigenTrust_Sensor) client);
        gatheredInfo.setCollectionClients(pathsToClients);

        return gatheredInfo;
    }

    @Override
    public synchronized Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        double delta = Double.NEGATIVE_INFINITY;
        double[] newGlobalTrustVector;
        int mostTrustworthyServerId = 0;

        double[][] normalizedLocalTrustValuesMatrix =
                ((GatheredInformationEigenTrust)gi).get_normalizedLocalTrustValuesMatrix();
        do {
            newGlobalTrustVector =
                    computeNewGlobalTrustVector(normalizedLocalTrustValuesMatrix,
                                               ((EigenTrust_Sensor)client).get_globalTrustVector());
            delta = globalTrustVectorsDistance(((EigenTrust_Sensor)client).get_globalTrustVector(),newGlobalTrustVector);
            ((EigenTrust_Sensor)client).set_globalTrustVector(newGlobalTrustVector);
        } while (delta >= ((EigenTrust_Parameters)trmParameters).get_epsilon());

        mostTrustworthyServerId = selectServiceProvider(client,((EigenTrust_Sensor)client).get_globalTrustVector());
        Vector<Sensor> path =
            ((GatheredInformationEigenTrust)gi).getPathToServer(mostTrustworthyServerId);
        return path;
    }

    @Override
    public synchronized Outcome performTransaction(Vector<Sensor> path, Service service) {
        Outcome outcome = null;
        if ((path == null) || (path.size() <= 0) || (!path.lastElement().isActive()))
            return outcome;
        
        EigenTrust_Sensor server = (EigenTrust_Sensor)path.lastElement();
        EigenTrust_Sensor client = (EigenTrust_Sensor)path.firstElement();
        Service receivedService = server.serve(service,path);

        if (receivedService == null)
            outcome = new EigenTrustEnergyConsumptionOutcome(new SatisfactionInterval(MIN_SATISFACTION,MAX_SATISFACTION,MIN_SATISFACTION),path.size());
        else
            outcome = new EigenTrustEnergyConsumptionOutcome(new SatisfactionInterval(MIN_SATISFACTION,MAX_SATISFACTION,MAX_SATISFACTION),path.size());
        
        client.addNewTransaction(client, server, outcome);

        return outcome;
    }

    /**
     * EigenTrust does not implement this method, since it does not apply any specific reward step
     * @param path
     * @param outcome
     */
    @Override
    public Outcome reward(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }    
    
    /**
     * EigenTrust does not implement this method, since it does not apply any specific punish step
     * @param path
     * @param outcome
     */
    @Override
    public Outcome punish(Vector<Sensor> path, Outcome outcome) {
        return outcome;
    }

    /**
     * This method selects the service provider to interact with
     * @param globalTrustVector Global trust vector
     * @return The service provider to interact with
     */
    private int selectServiceProvider(Sensor client, double[] globalTrustVector) {
        Vector<Integer> candidates = new Vector<Integer>();
        //Trust-proportional selection
        if (Math.random() > ((EigenTrust_Parameters)trmParameters).get_zeroTrustNodeSelectionProbability()) {
            Vector<Double> probailities = new Vector<Double>();
            double addition = 0.0;
            for (int i = 0; i < globalTrustVector.length; i++)
                if (globalTrustVector[i] > 0.0) {
                    addition += globalTrustVector[i];
                    probailities.add(globalTrustVector[i]);
                    candidates.add(i);
                }

            double aleat = Math.random();
            double accumulator = 0.0;
            for (int j = 0; j < probailities.size(); j++) {
                if ((accumulator <= aleat) &&
                                (aleat <= (accumulator + probailities.get(j)/addition)) &&
                                (client.id() != candidates.get(j)+1) &&
                                (client.findSensors(new IsSensorSearchCondition(candidates.get(j)+1)) != null))
                        return candidates.get(j)+1;
                accumulator += probailities.get(j)/addition;
            }
        } else { //Select a zero-trust service provider
            for (int i = 0; i < globalTrustVector.length; i++)
                if (globalTrustVector[i] <= Math.pow(10, -6))
                    candidates.add(i);
            int selectedServiceProvider = (int)(Math.random()*candidates.size());
            if ((selectedServiceProvider > 0) && (client.id() != candidates.get(selectedServiceProvider) + 1) && (client.findSensors(new IsSensorSearchCondition(candidates.get(selectedServiceProvider)+1)) != null))
                return candidates.get(selectedServiceProvider) + 1;
            else if ((candidates.size() > 0) && (client.id() != candidates.get(0) + 1) && (client.findSensors(new IsSensorSearchCondition(candidates.get(0)+1)) != null))
                return candidates.get(0) + 1;
        }
        //Select the most trustworthy service provider
        double maxTrustValue = Double.NEGATIVE_INFINITY;
        int serviceProvider = 0;
        for (int i = 0; i < globalTrustVector.length; i++)
            if ((globalTrustVector[i] > maxTrustValue) && (client.id() != i+1) && (client.findSensors(new IsSensorSearchCondition(i+1)) != null)) {
                maxTrustValue = globalTrustVector[i];
                serviceProvider = i+1;
            }
        return serviceProvider;
    }

    /**
     * This method computes the equation: t^{k+1} = C^T * t^k
     * @param normalizedLocalTrustValuesMatrix C matrix
     * @param globalTrustVector t^k vector
     * @return t^{k+1} = C^T * t^k
     */
    private double[] computeNewGlobalTrustVector(double[][] normalizedLocalTrustValuesMatrix, double[] globalTrustVector) {
        double[] newGlobalTrustVector = new double[globalTrustVector.length];
        double[] preTrustedPeersVector = EigenTrust_Sensor.get_preTrustedPeersVector();
        double preTrustedPeersWeight = ((EigenTrust_Parameters)trmParameters).get_preTrustedPeersWeight();
        double sum = 0.0;

        for (int l = 0; l < newGlobalTrustVector.length; l++) {
            newGlobalTrustVector[l] = 0.0;

            for (int j = 0; j < globalTrustVector.length; j++)
                newGlobalTrustVector[l] += normalizedLocalTrustValuesMatrix[j][l]*globalTrustVector[j];
            newGlobalTrustVector[l] = (1.0-preTrustedPeersWeight)*newGlobalTrustVector[l] + preTrustedPeersWeight*preTrustedPeersVector[l];
            sum += newGlobalTrustVector[l];
        }

        if (sum != 0.0)
            for (int i = 0; i < globalTrustVector.length; i++)
                newGlobalTrustVector[i] = newGlobalTrustVector[i]/sum;

        return newGlobalTrustVector;
    }

    /**
     * This method computes the difference: ||t^{k+1} - t^k||
     * @param globalTrustVector1 t^{k+1}
     * @param globalTrustVector2 t^k
     * @return ||t^{k+1} - t^k||
     */
    private double globalTrustVectorsDistance(double[] globalTrustVector1, double[] globalTrustVector2) {
        double distance = 0.0;

        for (int i = 0 ; i < globalTrustVector1.length; i++)
            distance += Math.pow(globalTrustVector1[i]-globalTrustVector2[i], 2);

        if (globalTrustVector1.length != 0)
            distance = Math.sqrt(distance/globalTrustVector1.length);

        return distance;
    }

    @Override
    public Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services)  {
        return new EigenTrust_Network(numSensors,1.0,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String fileName) throws Exception {
        return new EigenTrust_Network(fileName);
    }
}