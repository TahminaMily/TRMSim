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

package es.ants.felixgm.trmsim_wsn.trm;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <p>This class represents the generic set of parameters' values of
  every Trust and Reputation Model</p>
 <font color="#FF0000">
 <p><strong>A subclass of this class, containing the specific parameters, has to be 
 implemented in order to add a new Trust and Reputation Model</strong></p>
 </font>
 * <p>A parameters file has the following structure (name=value):</p>
 * <pre>
 *    # List of default parameters for a Trust and Reputation Model
 *    parameter_name1=parameter_value1
 *    parameter_name2=parameter_value2
 *    ...
 * </pre>
 * @author <a href="http://ants.dif.um.es/~felixgm/en" target="_blank">F&eacute;lix G&oacute;mez M&aacute;rmol</a>, <a href="http://webs.um.es/gregorio" target="_blank">Gregorio Mart&iacute;nez P&eacute;rez</a>
 * @version 0.4
 * @since 0.2
 */
public abstract class TRMParameters {
    /** File containing the parameters of a certain trust and reputation model */
    protected String parametersFile;
    /** Header for the file containing these parameters */
    protected String parametersFileHeader;
    /** Trust and Reputation Model parameters */
    protected Properties parameters;
    
    /** 
     * Creates a new instance of TRMParameters 
     */
    public TRMParameters() {
        parameters = new Properties();
    }
    
    /** 
     * Creates a new instance of TRMParameters from a parameters file
     * @param parametersFile File containing the parameters of a certain trust and reputation model
     * @throws java.lang.Exception If any parameter can not be successfully retrieved
     */
    public TRMParameters(String parametersFile) throws Exception {
        this.parametersFile = parametersFile;
        parameters = new Properties();
        parameters.load(ClassLoader.getSystemClassLoader().getResourceAsStream(parametersFile));
    }
    
    /**
     * This method returns a double value parameter from its name
     * @param parameterName Parameter's name
     * @return Parameter's double value
     */
    protected double getDoubleParameter(String parameterName) {
        return Double.parseDouble(parameters.getProperty(parameterName));
    }
    
    /**
     * This method returns a integer value parameter from its name
     * @param parameterName Parameter's name
     * @return Parameter's integer value
     */
    protected int getIntegerParameter(String parameterName) {
        return Integer.parseInt(parameters.getProperty(parameterName));
    }
    
    /**
     * This method returns a String value parameter from its name
     * @param parameterName Parameter's name
     * @return Parameter's String value
     */
    protected String getStringParameter(String parameterName) {
        return parameters.getProperty(parameterName);
    }
    
    /**
     * This method returns a boolean value parameter from its name
     * @param parameterName Parameter's name
     * @return Parameter's boolean value
     */
    protected boolean getBooleanParameter(String parameterName) {
        return Boolean.parseBoolean(parameters.getProperty(parameterName));
    }
    
    /**
     * This method sets a double value parameter
     * @param parameterName Parameter's name
     * @param parameterValue Parameter's double value
     */
    protected void setDoubleParameter(String parameterName, double parameterValue) {
        parameters.setProperty(parameterName, String.valueOf(parameterValue));
    }
    
    /**
     * This method sets a integer value parameter
     * @param parameterName Parameter's name
     * @param parameterValue Parameter's integer value
     */
    protected void setIntegerParameter(String parameterName, int parameterValue) {
        parameters.setProperty(parameterName, String.valueOf(parameterValue));
    }
    
    /**
     * This method sets a boolean value parameter
     * @param parameterName Parameter's name
     * @param parameterValue Parameter's boolean value
     */
    protected void setBooleanParameter(String parameterName, boolean parameterValue) {
        parameters.setProperty(parameterName, String.valueOf(parameterValue));
    }
    
    /**
     * This method sets a String value parameter
     * @param parameterName Parameter's name
     * @param parameterValue Parameter's String value
     */
    protected void setStringParameter(String parameterName, String parameterValue) {
        parameters.setProperty(parameterName, parameterValue);
    }
    
    /**
     * This method saves current parameter into its associated parameters file
     */
    public void saveToFile() {
        saveToFile(parametersFile);
    }

    /**
     * This method saves current parameters into a specified file
     * @param parametersFile File where to save current parameters
     */
    public void saveToFile(String parametersFile) {
        try {
            parameters.store(new FileOutputStream(parametersFile),parametersFileHeader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public abstract String toString();
}
