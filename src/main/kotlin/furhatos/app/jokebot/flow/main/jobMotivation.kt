package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.app.jokebot.nlu.becauseOfCourse
import furhatos.app.jokebot.nlu.motivatedUser
import furhatos.app.jokebot.nlu.socialMedia
import furhatos.app.jokebot.nlu.unMotivatedUser
import furhatos.flow.kotlin.*
import furhatos.gestures.*
import furhatos.gestures.CharParams.*


/** In interview bot we want to proceed with questions and respond according to
 * the user's answers.
 */

val reset = defineGesture("reset") {
    frame(0.0) {
        // Reset all facial features to their default positions
        CHEEK_BONES_DOWN to 0.0
        CHEEK_BONES_NARROWER to 0.0
        CHEEK_BONES_UP to 0.0
        CHEEK_BONES_WIDER to 0.0
        CHEEK_FULLER to 0.0
        CHEEK_THINNER to 0.0
        CHIN_DOWN to 0.0
        CHIN_NARROWER to 0.0
        CHIN_UP to 0.0
        CHIN_WIDER to 0.0
        EYEBROW_DOWN to 0.0
        EYEBROW_LARGER to 0.0
        EYEBROW_NARROWER to 0.0
        EYEBROW_SMALLER to 0.0
        EYEBROW_TILT_DOWN to 0.0
        EYEBROW_TILT_UP to 0.0
        EYEBROW_UP to 0.0
        EYEBROW_WIDER to 0.0
        EYES_DOWN to 0.0
        EYES_NARROWER to 0.0
        EYES_SCALE_DOWN to 0.0
        EYES_SCALE_UP to 0.0
        EYES_TILT_DOWN to 0.0
        EYES_TILT_UP to 0.0
        EYES_UP to 0.0
        EYES_WIDER to 0.0
        MOUTH_DOWN to 0.0
        MOUTH_FLATTER to 0.0
        MOUTH_NARROWER to 0.0
        MOUTH_SCALE to 0.0
        MOUTH_UP to 0.0
        MOUTH_WIDER to 0.0
        NOSE_DOWN to 0.0
        NOSE_NARROWER to 0.0
        NOSE_UP to 0.0
        NOSE_WIDER to 0.0
        LIP_BOTTOM_THICKER to 0.0
        LIP_BOTTOM_THINNER to 0.0
        LIP_TOP_THICKER to 0.0
        LIP_TOP_THINNER to 0.0
    }
}

val curiosity = defineGesture("curiosity") {
    frame(0.1, 0.5) {
        EYEBROW_UP to 1.0
        EYES_WIDER to -0.25
        MOUTH_UP to 0.5
    }
    reset(2.0)
}


val roleInterest: State = state(Parent) {

    //Starting the real questions in the interview
    onEntry {
        furhat.gesture(Gestures.BrowRaise)
        //User has 30 seconds to answer
        furhat.ask("Okay, so why does this role interest you?", timeout = 30000)
    }

    //User answers with motivational intent
    onResponse<motivatedUser> {
        furhat.gesture(Gestures.Smile)
        furhat.say("That sounds really good!")
        //Go to next question
        goto(positionExpectations)
    }

    //User answers with unmotivational intent (look nlu)
    /**
     * We should think about more ways how we could answer
     */
    onResponse<unMotivatedUser> {
        furhat.gesture(Gestures.GazeAway(duration = 1.5))
        furhat.say("Alrighty then, moving on.")
        //Go to next question
        goto(positionExpectations)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay.")
        //Go to next question
        goto(positionExpectations)
    }

    //User doesn't answer, proceed to repetition (look nlu)
    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("Sorry I could not hear you. Could you repeat that?")
        goto(repeatMotivation)
    }
}

//For repetition of motivation question
val repeatMotivation: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    //User responds with motivational intent
    onResponse<motivatedUser> {
        furhat.gesture(Gestures.Smile)
        furhat.say("That sounds really good!")
        goto(positionExpectations)
    }

    //User answers with unmotivational intent
    onResponse<unMotivatedUser> {
        furhat.gesture(Gestures.GazeAway(duration = 1.5))
        furhat.say("Alrighty then, moving on.")
        goto(positionExpectations)
    }

    //User answers with none of the trigger words/phrases in nlu
    onResponse {
        furhat.say("Okay.")
        //Go to next question
        goto(positionExpectations)
    }

    //User doesn't respond, proceed anyway
    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you now either. Could you repeat that?")
        //We re-enter the repeating, robot will listen again
        //User can then repeat their answer
        reentry()
    }
}

/** TODO
 * in positionExpectations and growth we should either created seperated word list that we want to listen for and
 * probably comment or we could keep the motivated and unMotivatedUser
 */

val positionExpectations: State = state(Parent) {

    onEntry {
        furhat.gesture(curiosity)
        furhat.ask(
            "I'd like to ask you what your expectations are from this role. Not salary-wise but " +
                    "rather about the experience you can get or projects you might work on."
        )
    }

    onResponse<motivatedUser> {
        furhat.gesture(Gestures.Smile)
        furhat.say("Perfect!")
        goto(growth)
    }

    onResponse<unMotivatedUser> {
        furhat.gesture(Gestures.BrowFrown(duration = 0.5))
        furhat.say("Okay.")
        goto(growth)
    }

    onResponse {
        furhat.say("Okay.")
        goto(growth)
    }

    onNoResponse {
        furhat.say("I could not hear you. I'll repeat the question.")
        reentry()
    }
}

val growth: State = state(Parent) {
    onEntry {
        furhat.gesture(curiosity)
        furhat.ask("How do you think this position will improve your professional and personal skills?")
    }

    onResponse {
        furhat.say("Okay!")
        goto(hearAboutPosition)
    }

    onNoResponse {
        furhat.say("I couldn't hear you. I'll repeat myself.")
        reentry()
    }
}

//Question where answer doesn't need to be stored
val hearAboutPosition: State = state(Parent) {

    onEntry {
        //User has 30 seconds to answer
        furhat.ask("Where did you hear about this position?", timeout = 30000)
    }

    /**
     * it.intent doesn't work, should have a look at it later
     */
    onResponse<socialMedia> {
        furhat.say("We've been working on our public profile on" + it.intent +"for quite some time")
        furhat.gesture(Gestures.Surprise)
        furhat.say("so we are glad that you found us there.")
        goto(companyReason)
    }

    //If user answers that it's because of this course
    onResponse<becauseOfCourse> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Hahaa, yes, I know. Let's still pretend this is a job interview.")
        goto(companyReason)
    }

    //User answers something random, we proceed to next question
    onResponse {
        furhat.say("Okay. We are really happy that you are here.")
        goto(companyReason)
    }

    //User doesn't answer -> Robot asks for repetition
    onNoResponse {
        furhat.say("Could you repeat that?")
        goto(repeatHearAboutPosition)
    }

}

//For repetition of "hear about position" question
val repeatHearAboutPosition: State = state(Parent) {
    onEntry {
        //User has 30 seconds to answer
        furhat.listen(timeout = 30000)
    }

    onResponse<socialMedia> {
        furhat.say("We've been working on our public profile there for quite some time")
        furhat.gesture(Gestures.Surprise, async=false)
        furhat.say("so we are glad that you found us there.")
        goto(companyReason)
    }

    onResponse<becauseOfCourse> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Hahaa, yes, I know. Let's still pretend this is a job interview.")
        goto(companyReason)
    }

    //User responds, we go to next question
    onResponse {
        furhat.say("Okay. We are really happy that you are here.")
        goto(companyReason)
    }

    //User doesn't answer, repeat
    onNoResponse {
        furhat.say("I can't hear you. Could you repeat yourself?")
        //Re-enter repetition
        reentry()
    }
}

val companyReason: State = state(Parent) {

    onEntry {
        furhat.ask("Could you please tell me why you would like to work specifically at our company?")
    }

    onResponse {
        furhat.say("Alright.")
        goto(skillSecIntro)
    }

    onNoResponse {
        furhat.say("I couldn't quite hear you.")
        reentry()
    }
}