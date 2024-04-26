package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.app.jokebot.nlu.confusedUser
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.app.jokebot.jokes.*

val startConsent: State = state(Parent) {
    /**
     * Robot Johnny welcomes user to job interview and asks for user's name
     */

    onEntry {
        furhat.gesture(welcome)
        furhat.gesture(Gestures.Nod(strength = 0.5))
        furhat.say("Hi there. Welcome to this job interview for the IT junior position at our company.")
        furhat.gesture(Gestures.Smile)
        furhat.say("My name is Johnny, and I am going to lead this interview.")
        furhat.say("Before we start with the interview, I would like to say that this interview is going to be recorded")
        furhat.gesture(Gestures.Roll(strength = 0.3))
        furhat.say("and sent to our HR team for further evaluation.")
        furhat.gesture(consent)
        furhat.ask("Do you give consent for this interview to be recorded?")
    }

    onReentry {
        furhat.gesture(welcome)
        furhat.gesture(Gestures.Nod(strength = 0.5))
        furhat.say(
            "Hi there. Welcome to this job interview for the IT junior position at our company. My name is Johnny " +
                    "and I am going to lead this interview."
        )
        goto(askName)
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Thank you. I will start recording this session.")
        goto(askName)
    }

    onResponse<No> {
        call(noConsent)
        reentry()
    }

    onResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("Could you be more precise maybe? Is it a yes or a no?")
        goto(responseConsent)
    }

    /**
     * Please check if the below code onNoResponse works
     */
    onNoResponse {
        furhat.say("I couldn't really hear you.")
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("Is it a yes or a no?")
        goto(responseConsent)
    }
}

/**
 * State for handling responses other than "yes" or "no" in startConsent
 */
val responseConsent: State = state(Parent) {

    onEntry {
        furhat.listen()
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Thank you. I will start recording this session.")
        goto(askName)
    }

    onResponse<No> {
        goto(noConsent)
    }

    onResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("Could you repeat?")
        reentry()
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("Could you repeat?")
        reentry()
    }
}

/**
 * No recording consent given.
 * The interview ends here.
 */
val noConsent: State = state(Parent) {
    onEntry {
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("In that case, I'm afraid we cannot continue with the interview.")
        furhat.say("Because of this, our HR team will only have a look at your CV, and after that, they will decide if you'll be invited to an in-person interview.")
        furhat.gesture(Gestures.BrowRaise(duration = 3.0))
        furhat.ask("Do you give consent for this interview to be recorded?")

    }
    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say(
            "Thank you. I will start recording the interview right now."
        )
        terminate()
    }
    onResponse<No> {
        furhat.gesture(Gestures.Nod)
        furhat.say(
            "In that case I will close this session and let the HR team know that they should have a look " +
                    "at your CV. Have a great day."
        )
        goto(Idle)
    }

    onResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say(
            "Could you be more precise maybe? I'll repeat my question, and please, " +
                    "answer with a yes or a no."
        )
        reentry()
    }

    onNoResponse {
        furhat.say("I couldn't hear you. I'll repeat.")
        reentry()
    }
}

val askName: State = state(Parent) {

    onEntry {
        furhat.gesture(Gestures.Nod(strength = 0.3, duration = 2.0))
        furhat.ask("What is your name?")
    }
    /**
     * Says "Nice to meet you *user's name*
     */
    onResponse<PersonName> {
        furhat.gesture(Gestures.Nod(duration = 0.5))
        furhat.say("Nice to meet you " + it.intent)
        goto(canWeStart)
    }

    /**
     * If the user says something else or the name is not in library
     */
    onResponse {
        furhat.gesture(Gestures.Nod(duration = 0.5))
        furhat.say("Nice to meet you!")
        goto(canWeStart)
    }

    /**
     * If the user does not respond, proceed anyway
     */
    onNoResponse {
        furhat.gesture(Gestures.Nod(duration = 0.15))
        furhat.say("I get it,")
        furhat.say(
            "I was also really nervous at my first job interview. " +
                    "I also forgot my own name then."
        )
        furhat.gesture(Gestures.Surprise, async = false)
        goto(canWeStart)
    }
}

/**
 * Starting the interview state
 */
val canWeStart: State = state(Parent) {

    onEntry {
        furhat.gesture(ready)
        furhat.gesture(Gestures.Smile(strength = 0.25))
        furhat.ask("Are you comfortable and ready to start the interview?", timeout = 4000)
    }

    /**
     * If user responds yes, proceed to tell me about yourself question
     */
    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Perfect! We will start then.")
        goto(tellMeAboutYourself)
    }

    /**
     * If user says no, proceed anyway
     */
    onResponse<No> {
        furhat.gesture(Gestures.BrowFrown(duration = 0.5))
        furhat.say("Okay well that's a shame, we really need to start though.")
        goto(tellMeAboutYourself)
    }

    /**
     * If user responds something else, proceed anyway
     */
    onResponse {
        furhat.gesture(Gestures.Nod)
        furhat.say("I will take that as a yes.")
        goto(tellMeAboutYourself)
    }

    /**
     * If user doesn't respond, proceed anyway
     */
    onNoResponse {
        furhat.gesture(Gestures.Nod)
        furhat.say("I will take that as a yes.")
        goto(tellMeAboutYourself)
    }
}

/**
 * Tell me about yourself question, user gets to answer for 1 minute
 */

val tellMeAboutYourself: State = state(Parent) {
    onEntry {
        furhat.ask("So, tell me a little bit about yourself.", timeout = 60000)
    }

    /**
     * User responds something, proceed
     */
    onResponse {
        furhat.gesture(Gestures.BigSmile)
        furhat.gesture(Gestures.BrowRaise)
        furhat.say("That sounds interesting!")
        goto(roleInterest)
    }

    /**
     * User doesn't respond, the user should repeat oneself
     */
    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I can't really hear you. Could you repeat that?")
        goto(repeatAboutYourself)
    }
    onResponse<confusedUser> {
        furhat.say("Just tell me any basic background about yourself - hobbies, school and stuff")
    }
}

/**
 * For repetition of tell me about yourself question
 */
val repeatAboutYourself: State = state(Parent) {
    onEntry {
        furhat.listen(timeout = 60000)
    }

    /**
     * User answers something, proceed
     */
    onResponse {
        furhat.gesture(Gestures.Smile)
        furhat.say("That sounds interesting!")
        goto(roleInterest)
    }

    /**
     * User doesn't respond, proceed anyway
     */
    onNoResponse {
        furhat.say("I couldn't hear you now either. Could you repeat that?")
        //We re-enter the repeating, robot will listen again
        //User can then repeat their answer
        reentry()
    }
}