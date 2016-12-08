/**
 *  Lock Door after Unloked
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
    name: "Lock Door after Unloked",
    namespace: "gfahd",
    author: "Georges Fahd",
    description: "Lock the door after it is unlovked",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Lock door after unlocked for 10 seconds") {
		// TODO: put inputs here
         input "lock", "capability.lock", title:"door lock", required: true, multiple: true
	}
    section("How many minutes?") {        
    	input "time", "int", required: true
     }   
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}
import groovy.time.*

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	// TODO: subscribe to attributes, devices, locations, etc.
    subscribe(lock, "lock.unlocked",  lockHandler)   
}

// TODO: implement event handlers
def lockHandler(evt) {
  // just for debugging
  log.debug "lock status changed to ${evt.value}."  
  //wait 10 second
  log.debug "Waiting ${time.toInteger()} minutes."  
 runIn(time.toInteger()*60, lockdoor)
  
 }
 
 def lockdoor () {  
 	log.debug "locking the door."  
	//Lock the door
  	lock.lock()   
}