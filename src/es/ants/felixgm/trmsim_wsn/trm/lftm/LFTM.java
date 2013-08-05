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

import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.FuzzyRuleSet;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.DefuzzifierCenterOfGravity;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.Variable;
import es.ants.felixgm.trmsim_wsn.outcomes.BasicOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.FuzzyOutcome;

import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionFuzzy;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import java.util.Collection;
import java.util.Vector;


/**
 * <p>This class models LFTM (Linguistic Fuzzy Trust Model)
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network,
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="LFTMparameters"></a>It needs some parameters to be passed as a
 * {@link LFTM_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # LFTM parameters file
 *    ####################################
 *    phi=0.01
 *    rho=0.87
 *    q0=0.45
 *    numAnts=0.35
 *    numIterations=0.59
 *    alpha=1.0
 *    beta=1.0
 *    initialPheromone=0.85
 *    pathLengthFactor=0.71
 *    transitionThreshold=0.66
 *    punishmentThreshold=0.48
 *    U_MIN=0.0
 *    U_MAX=1.0
 *    VH_A=0.7
 *    VH_B=0.9
 *    VH_C=1.0
 *    VH_D=1.0
 *    H_A=0.55
 *    H_B=0.7
 *    H_C=0.8
 *    H_D=0.9
 *    M_A=0.3
 *    M_B=0.45
 *    M_C=0.55
 *    M_D=0.7
 *    L_A=0.1
 *    L_B=0.2
 *    L_C=0.3
 *    L_D=0.45
 *    VL_A=0.0
 *    VL_B=0.0
 *    VL_C=0.1
 *    VL_D=0.3
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/LFTMparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * <br></br>
 * For more information regarding LFTM algorithm, please check the following reference:
 * <ul>
 *   <li>F&eacute;lix G&oacute;mez M&aacute;rmol, Javier G&oacute;mez-Mar&iacute;n 
 *       Bl&aacute;zquez,Gregorio Mart&iacute;nez P&eacute;rez,  &quot;<strong>LFTM
 *       Linguistic Fuzzy Trust Mechanism for Distributed Networks</strong>&quot;,
 *       <a href="http://eu.wiley.com/WileyCDA/WileyTitle/productCd-CPE.html" target="_blank">
 *       Concurrency and Computation: Practice &amp; Experience</a>, 2012
 *       <a href="http://dx.doi.org/10.1002/cpe.1825" target="_blank"><img src="http://ants.dif.um.es/~felixgm/img/adobe.gif" border="0"></a>
 *   </li>
 * </ul>
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.4
 */
public class LFTM extends TRModel_WSN {

    /**
     * Class LFTM constructor
     * @param lftm_parameters Parameters needed for the algorithm, as described <a href="#LFTMparameters">before</a>
     */
    public LFTM(LFTM_Parameters lftm_parameters) {
    	super(lftm_parameters);
    }

    /**
     * Returns the name of this model, i.e., "LFTM"
     * @return The name of this model, i.e., "LFTM"
     */
    public static String get_name() { return "LFTM"; }

    /**
     * This method launches the Trust LFTM_Ant Colony System algorithm in order to find the path
     * leading to the most trustworthy server offering the desired service
     * @param client Client requesting the service
     * @return Path leading to the most trustworthy server offering the desired service
     */
    private Vector<Sensor> ACOalgorithm(LFTM_Sensor client, Service service) {
        double phi = ((LFTM_Parameters)trmParameters).get_phi();
        double rho = ((LFTM_Parameters)trmParameters).get_rho();
        double q0 = ((LFTM_Parameters)trmParameters).get_q0();
        double alpha = ((LFTM_Parameters)trmParameters).get_alpha();
        double beta = ((LFTM_Parameters)trmParameters).get_beta();
        double numAnts = ((LFTM_Parameters)trmParameters).get_numAnts();
        double numIterations = ((LFTM_Parameters)trmParameters).get_numIterations();
        double pathLengthFactor = ((LFTM_Parameters)trmParameters).get_pathLengthFactor();
        double transitionThreshold = ((LFTM_Parameters)trmParameters).get_transitionThreshold();

        int N_Ants = (int)Math.max(1.0,Math.pow(client.get_numServers(),numAnts));
        LFTM_Ant ants[] = new LFTM_Ant[N_Ants];
        for (int i = 0; i < ants.length; i++)
            ants[i] = new LFTM_Ant(service, client);
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
                        LFTM_Sensor nextSensor = ants[k].getNextSensor(alpha, beta, q0);
                        //There aren't not visited neighbors or there is a loop
                        if (nextSensor == null) {
                            Sensor newLastSensor = ants[k].moveBack();
                            //If there are no more paths to explore, stop
                            if (newLastSensor == null) {
                                ants[k].setSolutionFound();
                                foundSolutions++;
                                if (ants[k].get_solution() == null) {
                                    System.out.println("ants[k].get_solution() == null");
                                    System.out.flush();
                                } 
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
        pathsToServers.add(ACOalgorithm((LFTM_Sensor)client, service));
        return new GatheredInformation(pathsToServers);
    }

    @Override
    public Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        for (Vector<Sensor> path : gi.getPathsToServers())
            return path;
        return null;
    }

    @Override
    public Outcome performTransaction(Vector<Sensor> path, Service service) {
        Outcome outcome = null;
        if ((path == null) || (path.size() <= 1) || (!path.lastElement().isActive()))
            return outcome;

        LFTM_Sensor server = (LFTM_Sensor)path.lastElement();
        LFTM_Service receivedService = (LFTM_Service)server.serve(service,path);
        Variable servicesComparison = receivedService.compareTo((LFTM_Service)service, ((LFTM_Sensor)path.lastElement()).get_priceWeight(), ((LFTM_Sensor)path.lastElement()).get_costWeight(), ((LFTM_Sensor)path.lastElement()).get_deliveryWeight(), ((LFTM_Sensor)path.lastElement()).get_qualityWeight());
        Variable clientSatisfaction = ((LFTM_Sensor)path.firstElement()).evaluateSatisfaction(servicesComparison);
        outcome = new FuzzyOutcome(new SatisfactionFuzzy(clientSatisfaction),path.size());
        return outcome;
    }

    @Override
    public Outcome reward(Vector<Sensor> path, Outcome outcome) {
        Variable clientSatisfaction = ((SatisfactionFuzzy)((FuzzyOutcome)outcome).get_satisfaction()).getSaitsfaction();
        Variable clientGoodness = ((LFTM_Sensor)path.firstElement()).get_clientGoodness();
        Variable reward = new Variable("Reward", LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(), LFTM_Parameters.get_U_MAX()));

        FuzzyRuleSet frsPunishmentReward = LFTM_Parameters.getFRSPunishmentReward(clientGoodness,clientSatisfaction,reward);
        frsPunishmentReward.evaluate();

        double rewardValue = reward.getValue(); //This value has to be within the interval [0.5, 1.0]
        double rewardFactor = Math.pow(Math.pow(10,-4), rewardValue-0.5);

        double rho = ((LFTM_Parameters)trmParameters).get_rho();
        rho = Math.pow(rho, rewardFactor);
        int L = path.size();

        for (int i = 0; i < L-1; i++) {
            LFTM_Sensor n1 = (LFTM_Sensor)path.get(i);
            LFTM_Sensor n2 = (LFTM_Sensor)path.get(i+1);
            double pheromone = n1.getPheromone(n2);
            double heuristic = n1.getHeuristic(n1);
            double z2 = 1 + pheromone*heuristic*((BasicOutcome)outcome).get_avgSatisfaction();
            n1.setPheromone(n2, (1-rho)*pheromone + rho*z2*pheromone);
        }

        return outcome;
    }

    @Override
    public Outcome punish(Vector<Sensor> path, Outcome outcome) {
        Variable clientSatisfaction = ((SatisfactionFuzzy)((FuzzyOutcome)outcome).get_satisfaction()).getSaitsfaction();
        Variable clientGoodness = ((LFTM_Sensor)path.firstElement()).get_clientGoodness();
        Variable punishment = new Variable("Punishment", LFTM_Parameters.get_linguisticTerms(), new DefuzzifierCenterOfGravity(LFTM_Parameters.get_U_MIN(), LFTM_Parameters.get_U_MAX()));

        FuzzyRuleSet frsPunishmentReward = LFTM_Parameters.getFRSPunishmentReward(clientGoodness,clientSatisfaction,punishment);
        frsPunishmentReward.evaluate();

        double punishmentValue = punishment.getValue(); //This value has to be within the interval [0.0, 0.5]
        double punishmentFactor = -4.0*Math.pow(punishmentValue, 2.0) + 1.0;

        double phi = ((LFTM_Parameters)trmParameters).get_phi();
        double punishmentThreshold = ((LFTM_Parameters)trmParameters).get_punishmentThreshold();

        punishmentThreshold = Math.pow(punishmentThreshold,punishmentFactor);

        int L = path.size();

        for (int i = 0; i < L-1; i++) {
            LFTM_Sensor n1 = (LFTM_Sensor)path.get(i);
            LFTM_Sensor n2 = (LFTM_Sensor)path.get(i+1);
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
        LFTM_Sensor maliciousServer = (LFTM_Sensor)path.lastElement();
        if (((BasicOutcome)outcome).get_avgSatisfaction() < punishmentThreshold)
            for (Sensor neighbor : maliciousServer.getNeighbors()) {
                double pheromone = ((LFTM_Sensor)neighbor).getPheromone(maliciousServer);
                pheromone -= phi;
                pheromone *= ((BasicOutcome)outcome).get_avgSatisfaction();
                ((LFTM_Sensor)neighbor).setPheromone(maliciousServer, pheromone);
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
            Collection<Service> services)  {
        return new LFTM_Network(numSensors,probClients,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String fileName) throws Exception {
        return new LFTM_Network(fileName);
    }
}