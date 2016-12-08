/**
 *  My First SmartApp
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
    name: "My First SmartApp",
    namespace: "gfahd",
    author: "Georges Fahd",
    description: "Testing my first app",
    category: "",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Turn on light in basement when Front door open") {
		// TODO: put inputs here
        input "mylock", "capability.lock", required: true, title: "Select Door Lock?"
	}
    section("Turn off when there's been no movement for") {
        input "minutes", "number", required: true, title: "Minutes?"
    }
    section("Turn on this light") {
        input "theswitch", "capability.switch", required: true, title: "Select Switch?"
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
    log.debug "Is it Unlocked?."  
    subscribe(mylock, "lock.unlocked", doorunlockhandler)
    
    subscribe(mylock, "lock.locked", doorlockhandler)
}

// TODO: implement event handlers
def doorunlockhandler(evt) {
    log.debug "doorunlockhandler called: ${evt.value}"
    theswitch.on()
}

def doorlockhandler(evt) {
	log.debug "doorlockhandler called: ${evt.value}"
	runIn(minutes, checklock)
        
}

def checklock() {
    log.debug "In checklock scheduled method"
// get the current state object for the motion sensor
    def lockState = mylock.currentState("lock")

    if (lockState.value == "locked") {
            // get the time elapsed between now and when the motion reported inactive
        def elapsed = now() - lockState.date.time

        // elapsed time is in milliseconds, so the threshold must be converted to milliseconds too
        def threshold = 1000 *  minutes

            if (elapsed >= threshold) {
            log.debug "lock has stayed locked long enough since last check ($elapsed ms):  turning switch off"
            theswitch.off()
            } else {
            log.debug "lock has not stayed locked long enough since last check ($elapsed ms):  doing nothing"
        }
    } else {
            // lock active; just log it and do nothing
            log.debug "lock is active, do nothing and wait for lock"
    }
}