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

package es.ants.felixgm.trmsim_wsn.trm.btrm_wsn;

import es.ants.felixgm.trmsim_wsn.outcomes.BasicOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.EnergyConsumptionOutcome;
import java.util.Collection;
import java.util.Vector;

import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionBinary;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;

import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;

/**
 * <p>This class models BTRM_WSN (Bio-inspired Trust and Reputation Model for Wireless Sensor Networks) 
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network, 
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="BTRM_WSNparameters"></a>It needs some parameters to be passed as a 
 * {@link BTRM_WSN_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # BTRM-WSN parameters file
 *    # Thu Jun 19 14:07:13 CEST 2008
 *    ####################################
 *    pathLengthFactor=0.71
 *    alpha=1.0
 *    phi=0.01
 *    initialPheromone=0.85
 *    q0=0.45
 *    numIterations=0.59
 *    punishmentThreshold=0.48
 *    rho=0.87
 *    beta=1.0
 *    numAnts=0.35
 *    transitionThreshold=0.66
 * </pre>
 * This file can be downloaded 
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/BTRM-WSNparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * <br></br>
 * For more information regarding BTRM-WSN algorithm, please check the following reference:
 * <ul>
 *   <li>F&eacute;lix G&oacute;mez M&aacute;rmol, Gregorio Mart&iacute;nez
 *       P&eacute;rez, &quot;<strong>Providing Trust in Wireless Sensor Networks
 *       using a Bio-inspired Technic</strong>&quot;, <a href="http://www.springer.com/business/business+information+systems/journal/11235" target="_blank">
 *       Telecommunication Systems Journal</a>,
 *       vol. 46, no. 2, pp. 163-180, 2011
 *       <a href="http://dx.doi.org/10.1007/s11235-010-9281-7" target="_blank"><img src="http://ants.dif.um.es/~felixgm/img/adobe.gif" border="0"></a>
 *   </li>
 * </ul>
 * 
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.2
 */
public class BTRM_WSN extends TRModel_WSN
{
    /**
     * Class BTRM_WSN constructor
     * @param btrm_parameters Parameters needed for the algorithm, as described <a href="#BTRM_WSNparameters">before</a>
     */
    public BTRM_WSN(BTRM_WSN_Parameters btrm_parameters) {
    	super(btrm_parameters);
    }

    /**
     * Returns the name of this model, i.e., "BTRM_WSN"
     * @return The name of this model, i.e., "BTRM_WSN"
     */
    public static String get_name() { return "BTRM_WSN"; }

    /**
     * This method launches the Trust BTRM_Ant Colony System algorithm in order to find the path
     * leading to the most trustworthy server offering the desired service
     * @param client Client requesting the service
     * @return Path leading to the most trustworthy server offering the desired service
     */
    private Vector<Sensor> ACOalgorithm(BTRM_Sensor client, Service service) {
        double phi = ((BTRM_WSN_Parameters)trmParameters).get_phi();
        double rho = ((BTRM_WSN_Parameters)trmParameters).get_rho();
        double q0 = ((BTRM_WSN_Parameters)trmParameters).get_q0();
        double alpha = ((BTRM_WSN_Parameters)trmParameters).get_alpha();
        double beta = ((BTRM_WSN_Parameters)trmParameters).get_beta();
        double numAnts = ((BTRM_WSN_Parameters)trmParameters).get_numAnts();
        double numIterations = ((BTRM_WSN_Parameters)trmParameters).get_numIterations();
        double pathLengthFactor = ((BTRM_WSN_Parameters)trmParameters).get_pathLengthFactor();
        double transitionThreshold = ((BTRM_WSN_Parameters)trmParameters).get_transitionThreshold();
        
        int N_Ants = (int)Math.max(1.0,Math.pow(client.get_numServers(),numAnts));
        BTRM_Ant ants[] = new BTRM_Ant[N_Ants];
        for (int i = 0; i < ants.length; i++)
            ants[i] = new BTRM_Ant(service, client);
        int N_Iterations = (int)Math.max(1.0,Math.pow(client.get_numServers(),numIterations));
        double globalMax = Double.NEGATIVE_INFINITY;
        Vector<Sensor> solution = new Vector<Sensor>();
        boolean checkedSolution[] = new boolean[ants.length];
        
        for (int numIter = 0; numIter < N_Iterations; numIter++) {
            for (int i = 0; i < ants.length; i++) {
                ants[i].reset();
                checkedSolution[i] = false;
            }
            
            /* All the ants build their paths */
            int foundSolutions = 0;
            while (foundSolutions < ants.length) {
                for (int k = 0; k < ants.length; k++)
                    if (!ants[k].isSolutionFound()) {
                        BTRM_Sensor nextSensor = ants[k].getNextSensor(alpha, beta, q0);
                        //There aren't not visited neighbors or there is a loop
                        if (nextSensor == null) {
                            Sensor newLastSensor = ants[k].moveBack();
                            //If there are no more paths to explore, stop
                            if (newLastSensor == null) {
                                ants[k].setSolutionFound();
                                foundSolutions++;
                            }
                        } else {
                            ants[k].pheromoneLocalUpdating(phi, nextSensor);
                            ants[k].addSensor(nextSensor);
	                    //It is a server offering the required service
	                    if (nextSensor.offersService(service)) {
                                double averagePheromone = ants[k].getLastNodePheromone();
                                if (((averagePheromone > transitionThreshold) &&
                                        (Math.random() < averagePheromone))) {
                                    ants[k].setSolutionFound();
                                    foundSolutions++;
                                }
                            }   
                        }
                    }
            }

            /* Look for the best path */
            int bestAnt = 0;
            double currentMax = Double.NEGATIVE_INFINITY;
            for (int k = 0; k < ants.length; k++)
                if (!checkedSolution[k]) {
                    double antsPercentage = 1;
                    for (int l = k+1; l < ants.length; l++)
                    	if (ants[k].get_solution().equals(ants[l].get_solution())) {
                            antsPercentage++;
                            checkedSolution[l] = true;
                    	}
                    antsPercentage = antsPercentage/ants.length;
                    checkedSolution[k] = true;
                    double solutionQuality = 
                            (antsPercentage*ants[k].get_averagePheromone())
                            /(Math.pow(ants[k].get_solution().size()-1,pathLengthFactor));
                    if (solutionQuality > currentMax) {
                        currentMax = solutionQuality;
                        bestAnt = k;
                    }
                }
            
            if (currentMax > globalMax) {
                globalMax = currentMax;
                solution = (Vector<Sensor>)ants[bestAnt].get_solution().clone();
            }

            /* Pheromone global updating */
            ants[bestAnt].pheromoneGlobalUpdating(phi, rho, globalMax);
        }
        return solution;
    }

    @Override
    public GatheredInformation gatherInformation(Sensor client, Service service) {
        Collection<Vector<Sensor>> pathsToServers = new Vector<Vector<Sensor>>();
        pathsToServers.add(ACOalgorithm((BTRM_Sensor)client, service));
        return new GatheredInformation(pathsToServers);
    }

    @Override
    public Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        for (Vector<Sensor> v : gi.getPathsToServers())
            return v;
        return null;
    }

    @Override
    public Outcome performTransaction(Vector<Sensor> path, Service service) {
        Outcome outcome = null;
        if ((path == null) || (path.size() <= 0) || (!path.lastElement().isActive()))
            return outcome;

        BTRM_Sensor server = (BTRM_Sensor)path.lastElement();
        Service receivedService = server.serve(service,path);
        outcome = new EnergyConsumptionOutcome(new SatisfactionBinary((receivedService != null)),path.size());

        return outcome;
    }

    @Override
    public Outcome reward(Vector<Sensor> path, Outcome outcome) {
        double rho = ((BTRM_WSN_Parameters)trmParameters).get_rho();
        int L = path.size();

        for (int i = 0; i < L-1; i++) {
            BTRM_Sensor n1 = (BTRM_Sensor)path.get(i);
            BTRM_Sensor n2 = (BTRM_Sensor)path.get(i+1);
            double pheromone = n1.getPheromone(n2);
            double heuristic = n1.getHeuristic(n1);
            double z2 = (1 + pheromone*heuristic*((BasicOutcome)outcome).get_avgSatisfaction())*pheromone;
            n1.setPheromone(n2, (1-rho)*pheromone + rho*z2);
        }

        return outcome;
    }

    @Override
    public Outcome punish(Vector<Sensor> path, Outcome outcome) {
        double phi = ((BTRM_WSN_Parameters)trmParameters).get_phi();
        double punishmentThreshold = ((BTRM_WSN_Parameters)trmParameters).get_punishmentThreshold();
        int L = path.size();

        for (int i = 0; i < L-1; i++) {
            BTRM_Sensor n1 = (BTRM_Sensor)path.get(i);
            BTRM_Sensor n2 = (BTRM_Sensor)path.get(i+1);
            double distanceFactor = Math.sqrt((double)(i+1)/((L-1)*(L-i-1)));
            double pheromone = n1.getPheromone(n2);
            if (((BasicOutcome)outcome).get_avgSatisfaction() >= punishmentThreshold) {
                pheromone -= phi*(1-((BasicOutcome)outcome).get_avgSatisfaction())*2*distanceFactor;
            } else {
                if (i < L-2) {
                    pheromone -= phi*distanceFactor;
                    pheromone *= ((BasicOutcome)outcome).get_avgSatisfaction()*(1-distanceFactor);
                }
            }
            n1.setPheromone(n2, pheromone);
        }
        BTRM_Sensor maliciousServer = (BTRM_Sensor)path.lastElement();
        if (((BasicOutcome)outcome).get_avgSatisfaction() < punishmentThreshold)
            for (Sensor neighbor : maliciousServer.getNeighbors()) {
                double pheromone = ((BTRM_Sensor)neighbor).getPheromone(maliciousServer);
                pheromone -= phi;
                pheromone *= ((BasicOutcome)outcome).get_avgSatisfaction();
                ((BTRM_Sensor)neighbor).setPheromone(maliciousServer, pheromone);
            }
        return outcome;
    }

    @Override
    public Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        return new BTRM_Network(numSensors,probClients,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String fileName) throws Exception {
        return new BTRM_Network(fileName);
    }
}