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

package es.ants.felixgm.trmsim_wsn.trm.trip;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.network.Service;
import es.ants.felixgm.trmsim_wsn.outcomes.EnergyConsumptionOutcome;
import es.ants.felixgm.trmsim_wsn.outcomes.Outcome;
import es.ants.felixgm.trmsim_wsn.satisfaction.SatisfactionInterval;
import es.ants.felixgm.trmsim_wsn.search.NumHopsSearchCondition;
import es.ants.felixgm.trmsim_wsn.trm.GatheredInformation;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import es.ants.felixgm.trmsim_wsn.trm.libs.fuzzylib.LinguisticTerm;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>This class models TRIP (Trust and Reputation Infrastructure-based Proposal for Vehicular Ad-hoc Networks)
 * algorithm used by a client in a P2P, Ad-hoc or Wireless Sensor Network,
 * in order to find the most trustworthy server offering a desired service.</p>
 * <p><a name="TRIPparameters"></a>It needs some parameters to be passed as a
 * {@link TRIP_Parameters} object. To do this, a file can be given following the next structure:</p>
 * <pre>
 *    ####################################
 *    # TRIP parameters file
 *    # Mon Jul 26 11:59:36 CEST 2010
 *    ####################################
 *    initialAlpha=0.3
 *    initialBeta=0.4
 *    initialGamma=0.3
 *    rsuPercentage=0.05
 *    numHopsQueriedRecommenders=3
 *    notTrustFuzzySetMean=0.0
 *    notTrustFuzzySetStDev=0.2
 *    moreOrLessTrustFuzzySetMean=0.5
 *    moreOrLessTrustFuzzySetStDev=0.15
 *    trustFuzzySetMean=1.0
 *    trustFuzzySetStDev=0.2
 * </pre>
 * This file can be downloaded
 * <a href="http://ants.dif.um.es/~felixgm/research/trmsim-wsn/resources/TRIPparameters.txt" target=_blank">here</a>.
 * But if any of the parameters can not be successfully extracted from the file, they are set
 * to a default value.
 * <br></br>
 * For more information regarding TRIP algorithm, please check the following reference:
 * <ul>
 *   <li>F&eacute;lix G&oacute;mez M&aacute;rmol, Gregorio Mart&iacute;nez
 *       P&eacute;rez, &quot;<strong>TRIP, a Trust and Reputation 
 *       Infrastructure-based Proposal for Vehicular Ad-hoc Networks</strong>&quot;, 
 *       <a href="http://journals.elsevier.com/10848045/journal-of-network-and-computer-applications" target="_blank">
 *       Journal of Network and Computer Applications</a>, vol. 35, no. 3, pp 934-941, 2012
 *       <a href="http://dx.doi.org/10.1016/j.jnca.2011.03.028" target="_blank"><img src="http://ants.dif.um.es/~felixgm/img/adobe.gif" border="0"></a>
 *   </li>
 * </ul>
 *
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 * @since 0.5
 */
public class TRIP extends TRModel_WSN {

    /**
     * Class TRIP constructor
     * @param trip_parameters Parameters needed for the algorithm, as described <a href="#TRIPparameters">before</a>
     */
    public TRIP(TRIP_Parameters trip_parameters) {
    	super(trip_parameters);
    }

    /**
     * Returns the name of this model, i.e., "TRIP"
     * @return The name of this model, i.e., "TRIP"
     */
    public static String get_name() { return "TRIP"; }

    @Override
    public GatheredInformation gatherInformation(Sensor client, Service service) {
        Collection<Vector<Sensor>> pathsToServers = client.findSensors(new NumHopsSearchCondition(client, ((TRIP_Parameters)trmParameters).get_numHopsQueriedRecommenders()));
        return new GatheredInformation(pathsToServers);
    }

    @Override
    public Vector<Sensor> scoreAndRanking(Sensor client, GatheredInformation gi) {
        Vector<Sensor> selectedNeighbors = new Vector<Sensor>();
        selectedNeighbors.add(client);

        //If there is no benevolent node reachable, any trust and reputation model makes sense
        boolean atLeastOneBenevolentNeighbor = false;
        for (Sensor neighbor : client.getNeighbors())
            try {
                if ((!((TRIP_Sensor)neighbor).isRSU()) && 
                        (neighbor.get_goodness(client.get_requiredService()) > 0.5)) {
                    atLeastOneBenevolentNeighbor = true;
                    break;
                }
            } catch (Exception ex) {}

        if ((((TRIP_Sensor)client).isRSU()) || !atLeastOneBenevolentNeighbor)
            return selectedNeighbors;

        // "+/- Trust" fuzzy set mean
        double muMLT = ((TRIP_Parameters)trmParameters).get_moreOrLessTrustFuzzySetMean();
        // "Not Trust" fuzzy set mean
        double muNT = ((TRIP_Parameters)trmParameters).get_notTrustFuzzySetMean();
        // "Not Trust" fuzzy set standard deviation
        double sigmaNT = ((TRIP_Parameters)trmParameters).get_notTrustFuzzySetStDev();
        for (Sensor neighbor : client.getNeighbors())
            if (!((TRIP_Sensor)neighbor).isRSU() && neighbor.offersService(client.get_requiredService())){
                double alpha = ((TRIP_Sensor)client).get_alpha();
                double beta = ((TRIP_Sensor)client).get_beta();
                double gamma = ((TRIP_Sensor)client).get_gamma();
                double vehiclesRecommendations = 0.0;
                double rsuRecommendations = 0.0;

                Vector<Sensor> queriedRecommenders = new Vector<Sensor>();
                boolean recalculateVehiclesRecommendations = false;
                for (Vector<Sensor> pathToServer : gi.getPathsToServers()) 
                    for (Sensor _recommender : pathToServer)
                        if (!_recommender.equals(client) && !_recommender.equals(neighbor) &&
                            !queriedRecommenders.contains(_recommender)) {
                            queriedRecommenders.add(_recommender);
                            TRIP_Sensor recommender = (TRIP_Sensor)_recommender;
                            if (recommender.isRSU())
                                rsuRecommendations = recommender.get_recommendation((TRIP_Sensor)neighbor);
                            else {
                                ((TRIP_Sensor)client).addRecommender((TRIP_Sensor)neighbor, recommender, recommender.get_recommendation((TRIP_Sensor)neighbor));
                                if (Double.isNaN(((TRIP_Sensor)client).get_weight(recommender))) {
                                    recalculateVehiclesRecommendations = true;
                                    ((TRIP_Sensor)client).set_weight(recommender,0.5);
                                }
                                vehiclesRecommendations += ((TRIP_Sensor)client).get_weight(recommender)*
                                                        recommender.get_recommendation((TRIP_Sensor)neighbor);
                            }
                        }

                if (recalculateVehiclesRecommendations) {
                    vehiclesRecommendations = 0.0;
                    for (Sensor _recommender : queriedRecommenders) {
                        TRIP_Sensor recommender = (TRIP_Sensor)_recommender;
                        if (!recommender.isRSU()) {
                            vehiclesRecommendations += ((TRIP_Sensor)client).get_weight(recommender)*
                                                    recommender.get_recommendation((TRIP_Sensor)neighbor);
                        }
                    }
                }

                if (rsuRecommendations == 0.0) {
                    alpha += gamma/2.0;
                    beta += gamma/2.0;
                }
                double neighborTrustValue = alpha*((TRIP_Sensor)client).get_trustValue((TRIP_Sensor)neighbor)+
                        beta*vehiclesRecommendations +
                        gamma*rsuRecommendations;

                LinguisticTerm trustLevel = ((TRIP_Sensor)client).get_trustLevel(neighborTrustValue);

                if (trustLevel.getTermName().equalsIgnoreCase("Trust"))
                    selectedNeighbors.add(neighbor);
                else if (trustLevel.getTermName().equalsIgnoreCase("+/- Trust")) {
                    //muMLT-muNT-sigmaNT is the probability of accepting messages of nodes placed in "+/- Trust" trust level
                    if (Math.random() < (muMLT-muNT-sigmaNT))
                        selectedNeighbors.add(neighbor);
                }

                ((TRIP_Sensor)client).set_trustValue((TRIP_Sensor)neighbor, neighborTrustValue);
            }

        return selectedNeighbors;
    }

    @Override
    public Outcome performTransaction(Vector<Sensor> selectedNeighbors, Service service) {
        Outcome outcome = null;
        if ((selectedNeighbors == null) || (selectedNeighbors.size() <= 1))
            return outcome;

        Vector<Outcome> outcomes = new Vector<Outcome>();
        for (int i = 1; i < selectedNeighbors.size(); i++) {
            Sensor neighbor = selectedNeighbors.get(i);
            Service receivedService = neighbor.serve(service,selectedNeighbors);
            outcomes.add(new EnergyConsumptionOutcome(new SatisfactionInterval(0.0,1.0,(receivedService != null) ? 1.0 : 0.0),1.0));
        }

        return Outcome.computeOutcomes(outcomes);
    }

    @Override
    public Outcome reward(Vector<Sensor> selectedNeighbors, Outcome outcome) {
        TRIP_Sensor client = (TRIP_Sensor) selectedNeighbors.get(0);
        for (int i = 1; i < selectedNeighbors.size(); i++) {
            TRIP_Sensor neighbor = (TRIP_Sensor)selectedNeighbors.get(i);
            double trustValue = client.get_trustValue(neighbor);
            Vector<Hashtable<TRIP_Sensor,Double>> recommenders = client.getRecommenders(neighbor);
            if (recommenders != null) {
                for (Hashtable<TRIP_Sensor,Double> _recommender : recommenders)
                    try {
                        double recommendation = _recommender.elements().nextElement();
                        TRIP_Sensor recommender = _recommender.keySet().iterator().next();
                        double weight = client.get_weight(recommender);
                        if (neighbor.get_goodness(client.get_requiredService()) > 0.5) {
                            if (recommendation > 0.5) {  //reward
                                client.set_weight(recommender, Math.pow(weight, Math.abs(recommendation-trustValue)));
                            } else {  //punish
                                client.set_weight(recommender, Math.pow(weight, 1.0/(1.0-Math.abs(recommendation-trustValue))));
                            }
                        } else {
                            if (recommendation <= 0.5) { //reward
                                client.set_weight(recommender, Math.pow(weight, Math.abs(recommendation-trustValue)));
                            } else { //punish
                                client.set_weight(recommender, Math.pow(weight, 1.0/(1.0-Math.abs(recommendation-trustValue))));
                            }
                        }
                    } catch (Exception ex) {}
            }
            client.resetRecommenders(neighbor);
        }
        return outcome;
    }

    @Override
    public Outcome punish(Vector<Sensor> selectedNeighbors, Outcome outcome) {
        return reward(selectedNeighbors,outcome);
    }

    @Override
    public Network generateRandomNetwork(
            int numSensors,
            double probClients,
            double rangeFactor,
            Collection<Double> probServices,
            Collection<Double> probGoodness,
            Collection<Service> services) {
        return new TRIP_Network(numSensors,probClients,rangeFactor,probServices,probGoodness,services);
    }

    @Override
    public Network loadCurrentNetwork(String xmlFilePath) throws Exception {
        return new TRIP_Network(xmlFilePath);
    }
}