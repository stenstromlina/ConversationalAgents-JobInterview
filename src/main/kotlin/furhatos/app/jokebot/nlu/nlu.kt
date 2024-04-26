/***
 * In nlu.kt we can store words that we want the conversational agents to listen for
 */

package furhatos.app.jokebot.nlu

import furhatos.nlu.Intent
import furhatos.util.Language


//Intent for motivated user
//Answer to question "Why does this role interest you?"
class motivatedUser : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I am looking forward to",
            "I am motivated",
            "Passion",
            "Excited",
            "Challenging",
            "Growth",
            "Opportunity",
            "Skills",
            "Experience",
            "Team",
            "Culture",
            "Contribution",
            "Learning",
            "Development",
            "Progression",
            "Alignment",
            "Mission",
            "Vision",
            "Innovation",
            "Impact",
            "Responsibility",
            "Motivated",
            "Interest",
            "Enthusiasm",
            "Engagement",
            "Commitment",
            "Dedication",
            "Ambition",
            "Fulfillment",
            "Success",
            "Achievement",
            "Recognition",
            "Collaboration",
            "Empowerment",
            "Self-improvement",
            "Value",
            "Alignment",
            "Inspiration",
            "Creativity",
            "Problem-solving",
            "Initiative",
            "Drive",
            "Curiosity",
            "Aspiration",
            "Advancement"
        )
    }
}

class unMotivatedUser : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I am interested because of the money",
            "Money",
            "Not interested",
            "Why would I be interested",
            "Duty",
            "Job", //Couldn't this one be positive/neutral?
            "Salary",
            "Routine", //Couldn't this one be positive/neutral?
            "Stability",
            "Necessity",
            "Income",
            "Requirement", //Couldn't this one be positive/neutral?
            "Job security",
            "Convenience",
            "Task", //Couldn't this one be positive/neutral?
            "Employment",
            "Obligation",
            "Hours", //Couldn't this one be positive/neutral?
            "Location", //Couldn't this one be positive/neutral?
            "Conformity",
            "Basic",
            "Minimal",
            "Tolerable",
            "Acceptable",
            "Neutral",
            "Okay", //Couldn't this one be positive/neutral?
            "Just", //Couldn't this one be positive/neutral?
            "Fine",
            "Passable",
            "Endurance",
            "Resigned",
            "Compromise",
            "Satisfactory",
            "Manageable",
            "Bearable",
            "Indifference",
            "Minimum",
            "Lowest",
            "Least",
            "Subdued",
            "Content",
            "Existence",
            "Survival",
            "Practical", //Couldn't this one be positive/neutral?
            "Ordinary",
            "Commonplace"
        )
    }
}

/**
 * This we could use when the user is asked where did he hear about this position.
 */
class socialMedia : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Instagram",
            "TikTok",
            "Snapchat",
            "Twitter",
            "Facebook",
            "LinkedIn",
            "YouTube",
            "Pinterest",
            "Reddit",
            "Clubhouse",
            "Discord",
            "WhatsApp",
            "Telegram",
            "WeChat",
            "Twitch"
        )
    }
}

class becauseOfCourse : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Conversational agents",
            "CA",
            "My friend is taking this course",
            "asked me to do this interview",
            "asked me to take this interview"
        )
    }
}

class confusedUser : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What do you want to know", "confused", "I'm not sure what to answer.", "What do you want."
        )
    }
}