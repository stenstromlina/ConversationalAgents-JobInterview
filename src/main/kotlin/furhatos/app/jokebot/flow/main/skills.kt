package furhatos.app.jokebot.flow.main

import furhatos.app.jokebot.flow.Parent
import furhatos.app.jokebot.gestures.proceed
import furhatos.app.jokebot.nlu.confusedUser
import furhatos.autobehavior.defineMicroexpression
import furhatos.flow.kotlin.*
import furhatos.gestures.BasicParams.*
import furhatos.gestures.Gestures
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val skillSecIntro: State = state(Parent) {

    onEntry {
        furhat.gesture(Gestures.BrowRaise)
        furhat.say("Now I would like to talk about your skills and experience.")
        furhat.gesture(proceed)
        furhat.ask("Can we proceed?")
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.Smile)
        furhat.say("Perfect!")
        goto(pythonCheck)
    }

    onResponse<No> {
        furhat.say("Well, we still need to continue.")
        goto(pythonCheck)
    }

    onResponse {
        furhat.gesture(Gestures.Nod)
        furhat.say("I'll take that as a yes.")
        goto(pythonCheck)
    }

    onNoResponse {
        furhat.gesture(Gestures.Nod)
        furhat.say("I'll take that as a yes.")
        goto(pythonCheck)
    }

}

/**
 * If the person mentions projects here we could ask what kind of python projects they worked on
 */
val pythonCheck: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Since previous experience with Python would be appreciated for this position, " +
                    "I would like to ask you if you have programmed in Python before?", timeout = 60000
        )
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.Surprise)
        furhat.say("Amazing!")
        goto(pythonProjectExperience)
    }

    onResponse<No> {
        furhat.say {
            random {
                +"That's okay! We have resources and support to help you get up to speed."
                +"No worries! Many programming concepts are transferable between languages. Your experience with other languages might give you a unique perspective when learning Python."
                +"It's never too late to learn Python! We offer training and mentorship programs to help you build your skills and confidence with the language."
                +"That's alright! We value a willingness to learn and grow. If you're open to it, we can provide resources to help you get started with Python."
                +"Don't worry if you haven't used Python before. We have a supportive environment where you'll have the opportunity to learn and develop your skills."
                +"If you're curious about Python, this could be a great opportunity to dive into something new. We're here to support you along the way!"
                +"Our team is collaborative, and we often learn from each other. If you're interested in Python, you'll have a supportive team to learn with."
                +"Even if you haven't used Python, we offer training sessions and workshops to help our team members learn and grow in their roles."
                +"We believe in continuous learning and personal growth. If you're motivated to learn Python, we'll provide the resources and support you need."
                +"While Python experience is beneficial, problem-solving skills and a strong foundation in programming principles are equally important. We value diverse experiences and perspectives."
            }
        }
        goto(programmingLanguages)
    }

    onResponse {
        furhat.say("Okay.")
        goto(programmingLanguages)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you.")
        reentry()
    }
}

val pythonProjectExperience: State = state(Parent) {
    onEntry {
        furhat.gesture(Gestures.Thoughtful(strength = 0.5))
        furhat.ask("Could you elaborate on that?", timeout = 60000)
        furhat.setMicroexpression(
            defineMicroexpression {
                repeat(200..500) {
                    adjust(-3.0..3.0, NECK_PAN)
                    adjust(-3.0..3.0, NECK_TILT)
                }
            })
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("Perfect")
        furhat.gesture(proceed)
        furhat.say("let's move onto the next question.")
        goto(programmingLanguages)
    }

    onResponse<No> {
        furhat.gesture(Gestures.BrowFrown)
        furhat.say("That's weird")
        goto(programmingLanguages)
    }

    onResponse {
        furhat.gesture(Gestures.Smile)
        furhat.say("That sounds interesting.")
        goto(programmingLanguages)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you. I'll repeat myself.")
        reentry()

    }
}

val programmingLanguages: State = state(Parent) {

    onEntry {
        furhat.ask(
            "Do you have any experience with some other programming languages? If so, please state them.",
            timeout = 60000
        )
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("That sounds really good.")
        goto(studies)
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("Well that is unfortunate.")
        goto(studies)
    }

    onResponse {
        furhat.say("Alright.")
        goto(studies)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you. I'll repeat the question.")
        reentry()
    }
}

val studies: State = state(Parent) {
    onEntry {
        furhat.say("Could you tell me more about your studies?")
        furhat.gesture(proceed)
        furhat.ask("Maybe about your favorite subjects " + "or projects that you have worked on.", timeout = 60000)

    }

    onResponse {
        furhat.gesture(Gestures.Smile)
        furhat.say("Sounds good!")
        goto(personalProjects)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I could not hear you. I will repeat the question.")
        reentry()
    }
}

val personalProjects: State = state(Parent) {
    onEntry {
        furhat.ask(
            "Do you have any personal projects that you have worked on? Could be some personal programming " +
                    "project or something else, doesn't have to be commercial.", timeout = 60000
        )
    }

    //For now, no further questions about this
    onResponse<Yes> {
        furhat.gesture(Gestures.BigSmile)
        furhat.say("That is perfect.")
        goto(personalProjectElaborate)
    }

    onResponse<No> {
        furhat.gesture(Gestures.ExpressSad)
        furhat.say("That's unfortunate.")
        goto(internationalSetting)
    }

    onResponse<confusedUser> {
        furhat.say(
            "You could tell me specific things you did in the project or how long it took you." +
                    "Just some basic information."
        )
        goto(confusedPersonalProjectElaborate)
    }

    onResponse {
        furhat.say("Alright!")
        goto(internationalSetting)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you. I'll repeat my question.")
        reentry()
    }
}

/**
 * For if user is confused about the personal project question
 */
val confusedPersonalProjectElaborate: State = state(Parent) {
    onEntry {
        furhat.listen()
    }

    onResponse {
        furhat.say("Okay!")
        goto(internationalSetting)
    }

    onNoResponse {
        furhat.say("Could you repeat?")
        reentry()
    }
}

val personalProjectElaborate: State = state(Parent) {
    onEntry {
        furhat.ask(
            "We are looking for someone who also has some personal projects going on. " +
                    "Please tell me more about those projects."
        )
    }

    onResponse {
        furhat.gesture(Gestures.Smile)
        furhat.say("That's impressive.")
        goto(internationalSetting)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you. I'll repeat my question.")
        reentry()
    }
}

val internationalSetting: State = state(Parent) {

    onEntry {
        furhat.ask(
            "Have you ever worked in an international setting? Could be in school or at previous " +
                    "jobs.", timeout = 60000
        )
    }

    onResponse<Yes> {
        furhat.gesture(Gestures.Smile)
        furhat.say(
            "Okay, that sounds good. This is something that we view as a crucial " +
                    "thing at our company."
        )
        goto(teamRole)
    }

    onResponse<No> {
        furhat.say("Okay!")
        furhat.gesture(Gestures.Nod)
        furhat.say(
            "But if you are comfortable working in an international work environment, " +
                    "you could fit in well here."
        )
        goto(teamRole)
    }

    onResponse {
        furhat.say("Alright.")
        goto(teamRole)
    }

    onNoResponse {
        furhat.gesture(Gestures.Thoughtful)
        furhat.say("I couldn't hear you. I'll repeat my question.")
        reentry()
    }
}

val teamRole: State = state(Parent) {

    onEntry {
        furhat.ask(
            "How would you describe your role in a team project? What kind of responsibilities do you " +
                    "usually take on?", timeout = 60000
        )
    }

    onResponse {
        furhat.say("Okay. I see.")
        goto(endSecIntro)
    }

    onNoResponse {
        furhat.say("I could not hear you. I'll repeat what I just said.")
        reentry()
    }
}