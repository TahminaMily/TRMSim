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

package es.ants.felixgm.trmsim_wsn;

import es.ants.felixgm.trmsim_wsn.network.Network;
import es.ants.felixgm.trmsim_wsn.network.Service;

import es.ants.felixgm.trmsim_wsn.gui.parameterpanels.TRMParametersPanel;

import es.ants.felixgm.trmsim_wsn.network.Sensor;
import es.ants.felixgm.trmsim_wsn.trm.TRModel_WSN;
import es.ants.felixgm.trmsim_wsn.trm.TRMParameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Observer;
import java.util.Observable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * <p>This class acts as a controller between classes representing the GUI, and classes
 * belonging to the model, as in the MVC pattern</p>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.5
 */
public class Controller implements Observer {
    /** Singleton instance of Controller */
    private static Controller controlador = null;
    /** Current working network*/
    private Network currentNetwork;
    /** Parameters file name for the current trust and reputation model */
    private String parametersFile;
    /** Current trust and reputation model */
    private TRModel_WSN trmodel_wsn;
    /** Service requested by every client in the network */
    private Service requiredService;
    /** Delay used to allow a better visualization of some simulationThread */
    private long delay;
    /** Thread controlling current simulation */
    private Thread simulationThread;
    /** Current simulation */
    private Simulation simulation;
    
    /**
     * Returns the current instance of this controller
     * @return Current instance of this controller
     * @throws Exception If there is any problem loading default Trust and Reputation Model parameters
     */
    public static Controller C() throws Exception {
        if (controlador == null)
            controlador = new Controller();
        return controlador;
    }
    
    /**
     * Creates a new instance of Controller
     * @throws Exception If there is any problem loading default Trust and Reputation Model parameters
     */
    private Controller() throws Exception {
        requiredService = new Service("My service");
        delay = 0;
    }
    
    /**
     * This method creates a new wireless sensor network using the specified parameters
     * @param minNumSensors Minimum number of sensors composing the WSN
     * @param maxNumSensors Maximum number of sensors composing the WSN
     * @param probClients The probability of a sensor to act as a client
     * @param probRelay The probability of a server to act just as a relay sensor (not offering the required service)
     * @param probMalicious The probability of a server offering the required service to act as a 
     * malicious server (not providing the offered service, or providong a worse or different one).
     * @param radioRange Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @return The generated random Wireless Sensor Network
     */
    public Network createNewNetwork(int minNumSensors, int maxNumSensors,
                                double probClients, double probRelay, double probMalicious,
                                double radioRange, boolean dynamic, boolean oscillating, 
                                boolean collusion) {
        // We randomly calculate the number of sensors of the network.
        int numSensors = (int)(minNumSensors + Math.random()*Math.abs(maxNumSensors-minNumSensors));
        ArrayList<Double> probServices = new ArrayList<Double>();
        ArrayList<Double> probGoodness = new ArrayList<Double>();
        ArrayList<Service> services = new ArrayList<Service>();

        services.add(new Service("Relay")); // Only two types of services: Relay or RequiredService.
        services.add(requiredService);

        probServices.add(1.0);
        probGoodness.add(1.0);
        probServices.add(1.0-probRelay);
        probGoodness.add(1.0-probMalicious);

        currentNetwork = trmodel_wsn.generateRandomNetwork(numSensors, probClients, radioRange, probServices, probGoodness, services);

        currentNetwork.set_collusion(collusion);
        currentNetwork.set_dynamic(dynamic);
        return currentNetwork;
    }
    
    /**
     * This method recalculates the neighborhood of every sensor composing the current network given a new range
     * @param newRange New wireless range applied to every sensor
     * @return The new current WSN
     */
    public Network setNewNeighborsNetwork(double newRange) {
        if (currentNetwork != null) {
            currentNetwork.setNewNeighbors(newRange);
            return currentNetwork;
        }
        return null;
    }

    /**
     * This method resets the current network to its initial state
     */
    public void resetCurrentNetwork() {
        if (currentNetwork != null)
            currentNetwork.reset();
    }

    /**
     * This method is used to communicate the controller with the GUI
     * @param observable
     * @param arg
     */
    public void update(Observable observable, Object arg) {
        if (arg instanceof Network)
            currentNetwork = (Network) arg;
    }   
    
    /**
     * This method runs a set of simulations over a number of random WSN, showing its outcomes
     * @param observer Used to communicate changes to the GUI
     * @param minNumSensors Minimum number of sensors composing every WSN
     * @param maxNumSensors Maximum number of sensors composing every WSN
     * @param probClients The probability of a node to act as a client
     * @param probRelay The probability of a server to act just as a relay node (not offering the required service)
     * @param probMalicious The probability of a server offering the required service to act as a 
     * malicious server (not providing the offered service, or providong a worse or different one).
     * @param radioRange Maximum wireless range of every sensor. It determines the neighborhood of every sensor
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @param numNetworks Number of wireless sensor networks to test
     * @param numExecutions Number of service requests of every client composing each WSN
     */
    public void runSimulations(Observer observer, int minNumSensors, int maxNumSensors,
                                double probClients, double probRelay, double probMalicious,
                                double radioRange, boolean dynamic, boolean oscillating, 
                                boolean collusion, int numNetworks, int numExecutions) {
        Collection<Observer> observers = new LinkedList<Observer>();
        observers.add(observer);
        observers.add(this);
        simulation = new Simulation(observers,requiredService,minNumSensors,maxNumSensors,
                        probClients,probRelay,probMalicious,radioRange,
                        dynamic, oscillating, collusion, 
                        numNetworks,numExecutions);
        simulationThread = new Thread(simulation);
        simulationThread.start();
    }
    
    /**
     * This method stops the current simulation process
     */
    public void stopSimulations() {
        if (simulationThread != null)
            simulation.stop();
    }
    
    /**
     * This method runs a set of simulations over current WSN, showing its outcomes
     * @param observer Used to communicate changes to the GUI
     * @param dynamic It determines if the WSN will be dynamic (nodes sometimes switch off in order to save battery, breaking all their links)
     * @param oscillating It determines if the goodness of the servers belonging to the created WSN will change along the time
     * @param collusion It determines if the malicious servers belonging to the created WSN will form a collusion among them
     * @param numExecutions Number of service requests of every client composing current WSN
     */
    public void runTRM_WSN(Observer observer, boolean dynamic, boolean oscillating, 
                            boolean collusion, int numExecutions) {
        Collection<Observer> observers = new LinkedList<Observer>();
        observers.add(observer);
        observers.add(this);
        simulation = new Simulation(observers, requiredService, 
                        dynamic, oscillating, collusion, 
                        numExecutions, currentNetwork);
        simulationThread = new Thread(simulation);
        simulationThread.start();
    }
    
    /**
     * This method saves the current Wireless Sensor Network into a specified XML file 
     * @param fileName Path of the XML file where to write the current Network
     * @throws Exception If there is any problem when writing to the XML file
     */
    public void saveCurrentNetwork(String fileName) throws Exception {
        currentNetwork.writeToXMLFile(fileName);
    }
    
    /**
     * This method loads a Wireless Sensor Network from a XML file 
     * @param fileName Path of the XML file describing the Network to load
     * @return The loaded WSN from XML file
     * @throws Exception If the given XML file hasn't the appropriate structure, or if
     * there is any problem reading the file
     */
    public Network loadCurrentNetwork(String fileName) throws Exception {
        currentNetwork = trmodel_wsn.loadCurrentNetwork(fileName);
        return currentNetwork;
    }

    /**
     * This method makes the current thread to sleep for a given amount of time
     */
    public void sleep() { 
        try {
            Thread.sleep(delay); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This method returns current network
     * @return Current Network
     */
    public Network get_currentNetwork() { return currentNetwork; }

    /**
     * This method returns the service requested by every client in the network
     * @return The service requested by every client in the network
     */
    public Service get_requiredService() { return requiredService; }

    /**
     * This method returns the parameters file name for the current trust and reputation model
     * @return The parameters file name for the current trust and reputation model
     */
    public String get_parametersFile() { return parametersFile; }

    /**
     * This method returns the parameters of the current trust and/or reputation model
     * @return The parameters of the current trust and/or reputation model
     */
    public TRMParameters get_TRMParameters() { return trmodel_wsn.get_TRMParameters(); }

    /**
     * This method returns a boolean indicating whether there is currently a simulation running or not
     * @return Boolean indicating whether there is currently a simulation running or not
     */
    public boolean isSimulationRunning() { return Sensor.isRunningSimulation(); }
    
    /**
     * This method sets the parameters of current Trust and Reputation Model
     * through a {@link TRMParametersPanel}
     * @param trmParametersPanel Trust and Reputation Model parameters panel to get
     * parameters from
     */
    public void set_TRMParameters(TRMParametersPanel trmParametersPanel) {
        trmodel_wsn.set_TRMParameters(trmParametersPanel.get_TRMParameters());
        Sensor.set_TRModel_WSN(trmodel_wsn);
    }
    
    /**
     * This method sets the parameters of current Trust and Reputation Model through a parameters file
     * @param parametersFile File where to extract parameters from
     * @return Trust and Reputation Model Parameters
     * @throws Exception If the given file does not contain the appropriate parameters for current
     * Trust and Reputation Model
     */
    public TRMParameters set_TRMParameters(String parametersFile) throws Exception {
        try {
            Class[] set_TRMParametersParametersTypes = {Class.forName("java.lang.String")};
            Object[] set_TRMParametersParametersValues = {parametersFile};
            trmodel_wsn.set_TRMParameters((TRMParameters)Class.forName(trmodel_wsn.getClass().getName()+"_Parameters").getConstructor(set_TRMParametersParametersTypes).newInstance(set_TRMParametersParametersValues));
        } catch (Exception ex) {
            trmodel_wsn.set_TRMParameters((TRMParameters) Class.forName(trmodel_wsn.getClass().getName()+"_Parameters").newInstance());
        }
        Sensor.set_TRModel_WSN(trmodel_wsn);

        return trmodel_wsn.get_TRMParameters();
    }

    /**
     * This method sets the current Trust and Reputation Model
     * @param trmodel_wsn Name of the desired Trust and Reputation Model
     * @throws Exception If the default parameters file does not contain the appropriate parameters 
     * for its Trust and Reputation Model
     */
    public void set_TRModel_WSN(String trmodel_wsn) throws Exception {
        TRMParameters trm_parameters = null;
        String packageName = "es.ants.felixgm.trmsim_wsn.trm."+trmodel_wsn.toLowerCase()+".";

        parametersFile = (String)Class.forName(packageName+trmodel_wsn+"_Parameters").getDeclaredField("defaultParametersFileName").get(null);
        Class[] trmParametersConstructorParametersTypes = {Class.forName("java.lang.String")};
        Object[] trmParametersConstructorParametersValues = {parametersFile};
        try {
            trm_parameters = (TRMParameters) Class.forName(packageName+trmodel_wsn+"_Parameters").getConstructor(trmParametersConstructorParametersTypes).newInstance(trmParametersConstructorParametersValues);
        } catch (java.lang.Exception ex) {
            trm_parameters = (TRMParameters) Class.forName(packageName+trmodel_wsn+"_Parameters").newInstance();
            //ex.printStackTrace();
        }
        Class[] trmodel_wsnConstructorParametersTypes = {Class.forName(packageName+trmodel_wsn+"_Parameters")};
        Object[] trmodel_wsnConstructorParametersValues = {trm_parameters};
        this.trmodel_wsn = (TRModel_WSN) Class.forName(packageName+trmodel_wsn).getConstructor(trmodel_wsnConstructorParametersTypes).newInstance(trmodel_wsnConstructorParametersValues);

        Sensor.set_TRModel_WSN(this.trmodel_wsn);
    }

    /**
     * This method saves a new content into the selected parameters file
     * @param filePath Path of the selected parameters file
     * @param newContent New content to be saved in the selected parameters file
     * @throws Exception If an error occurs while saving the new content into the selected parameters file
     */
    public void saveParametersFileContent(String filePath, String newContent) throws Exception {
        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(newContent);
        fileWriter.flush();
        fileWriter.close();
    }

    /**
     * This method retrieves the content of the current parameters file
     * @return The content of the current parameters file
     * @throws Exception If an error occurs while retrieving the content of the current parameters file
     */
    public String get_ParametersFileContent() throws Exception {
        String defaultParametersFileContent = "";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(parametersFile));
        String newLine = null;
        while ((newLine = bufferedReader.readLine()) != null)
            defaultParametersFileContent += newLine+"\n";

        bufferedReader.close();
        return defaultParametersFileContent;
    }

    /**
     * This method retrieves the content of the default parameters file of the given trust model
     * @param trmodel_wsn Trust model whose default parameters file content is to be retrieved
     * @return The content of the default parameters file of the given trust model
     * @throws Exception If an error occurs while retrieving the content of the default parameters file of the given trust model
     */
    public String get_DefaultParametersFileContent(String trmodel_wsn) throws Exception {
        String defaultParametersFileContent = "";
        String packageName = "es.ants.felixgm.trmsim_wsn.trm."+trmodel_wsn.toLowerCase()+".";

        try {
            String defaultParametersFilePath = (String)Class.forName(packageName+trmodel_wsn+"_Parameters").getDeclaredField("defaultParametersFileName").get(null);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(defaultParametersFilePath)));
            String newLine = null;
            while ((newLine = bufferedReader.readLine()) != null)
                defaultParametersFileContent += newLine+"\n";

            bufferedReader.close();
        } catch (Exception ex) {
            TRMParameters trm_parameters = (TRMParameters) Class.forName(packageName+trmodel_wsn+"_Parameters").newInstance();
            defaultParametersFileContent = trm_parameters.toString();
        }
        
        return defaultParametersFileContent;
    }

    /**
     * This method sets the delay used to slow the simulations visualization
     * @param delay Delay in milliseconds
     */
    public void set_delay(long delay) { this.delay = delay; }

    /**
     * This method sets the parameters file name for the current trust and reputation model
     * @param parametersFile Parameters file name for the current trust and reputation model
     */
    public void set_parametersFile(String parametersFile) { this.parametersFile = parametersFile; }

    
    public Sensor getSensorAtCoordinate(double x, double y) {
        if (currentNetwork == null)
            return null;
        int error = 2;
        for (Sensor sensor : currentNetwork.get_sensors()) {
            if (((x <= (sensor.getX()+error)) && (x >= (sensor.getX()-error)))
                    &&
                ((y <= (sensor.getY()+error)) && (y >= (sensor.getY()-error))))
                return sensor;
        }
        
        return null;
    }
    
    /**
     * This method returns the sensor with identifier id. If such sensor does not exist, it returns null
     * @param id Identifier of the sensor to be retrieved
     * @return The sensor with identifier id. If such sensor does not exist, it returns null
     */
    public Sensor getSensor(int id) {
        if (currentNetwork == null)
            return null;

        return currentNetwork.getSensor(id);
    }
}