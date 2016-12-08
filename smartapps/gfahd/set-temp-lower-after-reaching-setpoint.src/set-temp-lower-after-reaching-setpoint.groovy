/**
 *  Set Temp Lower after reaching setpoint
 *
 *  Copyright 2016 Georges Fahd
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Set Temp Lower after reaching setpoint",
    namespace: "gfahd",
    author: "Georges Fahd",
    description: "Sets the temperature to a provided lower degree after reaching the setpoint",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Select Thermostat to set") {
		// TODO: put inputs here
        input "mythermostat", "capability.thermostat", required: true
	}    
    section("Se4tpoint") {
		// TODO: put inputs here
        input "mysetpoint", "capability.thermostatHeatingSetpoint", required: true
	}    
		
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.   
    log.debug "Trigger event..."
    subscribe(mysetpoint, "heatingSetpoint", setHeatingSetpointHandler)    
}

// TODO: implement event handlers
def setHeatingSetpointHandler(evt) {
	log.debug "Evaluate condition..."
	 if (evt.value >= mythermostat.temperature) {
        log.debug "Set to 18."
         mysetpoint.setHeatingSetpoint(18)
     }
}